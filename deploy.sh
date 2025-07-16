#!/bin/bash
set -e # 어떤 명령이든 실패하면 즉시 스크립트 종료

# 환경 변수 설정 (GitHub Actions에서 주입)
# TOMCAT_HOME: 톰캣 설치 경로 (예: /opt/tomcat)
# EC2_USERNAME: EC2 사용자 이름

TOMCAT_VERSION="9.0.89" # 사용할 톰캣 버전
TOMCAT_URL="https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz"

TOMCAT_HOME="/opt/tomcat"
WEBAPPS_PATH="${TOMCAT_HOME}/webapps"

# EC2 사용자 이름을 여기에 직접 입력하세요. (예: ec2-user, ubuntu)
EC2_USERNAME_HARDCODED="ec2-user"

echo "Starting deploy.sh script..."

# Java 설치 및 경로 찾기
echo "Checking Java installation..."
if ! java -version 2>&1 | grep -q "17"; then
    echo "Installing Java 17..."
    sudo yum update -y
    sudo yum install -y java-17-amazon-corretto-devel
fi

# Java Home 경로 자동 감지
JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
echo "Found Java at: $JAVA_HOME"

# 톰캣 설치 확인 및 설치
if [ ! -d "${TOMCAT_HOME}" ] || [ ! -f "${TOMCAT_HOME}/bin/startup.sh" ]; then
    echo "Tomcat not found or corrupted at ${TOMCAT_HOME}. Proceeding with installation..."

    # 기존 Tomcat 디렉토리 정리
    if [ -d "${TOMCAT_HOME}" ]; then
        echo "Removing existing Tomcat directory..."
        sudo rm -rf "${TOMCAT_HOME}"
    fi

    # 톰캣 사용자 및 그룹 생성
    echo "Creating tomcat user and group..."
    sudo groupadd tomcat || true # 이미 존재하면 오류 무시
    sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat || true # 이미 존재하면 오류 무시

    # 톰캣 다운로드 및 압축 해제
    echo "Downloading Tomcat from ${TOMCAT_URL}..."
    mkdir -p /tmp/tomcat_install
    wget -q ${TOMCAT_URL} -O /tmp/tomcat_install/tomcat.tar.gz
    echo "Tomcat downloaded. Extracting to /opt/..."

    # 올바른 디렉토리 구조로 설치
    sudo mkdir -p ${TOMCAT_HOME}
    sudo tar -xzf /tmp/tomcat_install/tomcat.tar.gz -C /tmp/tomcat_install/
    sudo mv /tmp/tomcat_install/apache-tomcat-${TOMCAT_VERSION}/* ${TOMCAT_HOME}/
    rm -rf /tmp/tomcat_install

    echo "Tomcat extracted. Checking directory structure:"
    ls -la ${TOMCAT_HOME}/
    echo "Checking bin directory:"
    ls -la ${TOMCAT_HOME}/bin/

    # 권한 설정
    echo "Setting Tomcat directory permissions..."
    sudo chown -RH tomcat:tomcat ${TOMCAT_HOME}
    sudo chmod +x ${TOMCAT_HOME}/bin/*.sh
    echo "Permissions set."

    # Systemd 서비스 파일 생성
    echo "Creating Tomcat systemd service file..."
    sudo tee /etc/systemd/system/tomcat.service > /dev/null <<EOF
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment="JAVA_HOME=${JAVA_HOME}"
Environment="CATALINA_PID=${TOMCAT_HOME}/temp/tomcat.pid"
Environment="CATALINA_HOME=${TOMCAT_HOME}"
Environment="CATALINA_BASE=${TOMCAT_HOME}"
ExecStart=${TOMCAT_HOME}/bin/startup.sh
ExecStop=${TOMCAT_HOME}/bin/shutdown.sh

User=tomcat
Group=tomcat
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
EOF

    # Systemd 데몬 리로드 및 톰캣 서비스 활성화
    echo "Enabling Tomcat service..."
    sudo systemctl daemon-reload
    sudo systemctl enable tomcat
    echo "Tomcat installation complete."
else
    echo "Tomcat already installed at ${TOMCAT_HOME}. Checking structure..."
    ls -la ${TOMCAT_HOME}/bin/ || echo "Bin directory missing"
fi

# 톰캣 중지
echo "Stopping Tomcat..."
sudo systemctl stop tomcat || true # 서비스가 실행 중이 아닐 수도 있으므로 오류 무시
sleep 5 # 톰캣이 완전히 중지될 때까지 대기

# 기존 애플리케이션 삭제
echo "Removing old application..."
sudo rm -rf "${WEBAPPS_PATH}/dondothat"
sudo rm -f "${WEBAPPS_PATH}/dondothat.war"
sudo rm -f "${WEBAPPS_PATH}/DonDoThat-1.0-SNAPSHOT.war"

# webapps 디렉토리 확인 및 생성
if [ ! -d "${WEBAPPS_PATH}" ]; then
    echo "Creating webapps directory at ${WEBAPPS_PATH}..."
    sudo mkdir -p "${WEBAPPS_PATH}"
    sudo chown tomcat:tomcat "${WEBAPPS_PATH}"
fi

# 새로운 WAR 파일 복사
echo "Copying new WAR file..."

# /home/${EC2_USERNAME_HARDCODED}/build/libs/ 경로에서 *.war 파일을 찾아 이동
UPLOADED_WAR=$(find /home/${EC2_USERNAME_HARDCODED}/build/libs/ -maxdepth 1 -name "*.war" -print -quit)

if [ -z "$UPLOADED_WAR" ]; then
    echo "Error: No WAR file found in /home/${EC2_USERNAME_HARDCODED}/build/libs/"
    exit 1
fi

echo "Found WAR file: ${UPLOADED_WAR}"
echo "Moving to: ${WEBAPPS_PATH}/"
sudo cp "${UPLOADED_WAR}" "${WEBAPPS_PATH}/"
sudo chown tomcat:tomcat "${WEBAPPS_PATH}"/*.war

# 톰캣 시작 전 디렉토리 구조 최종 확인
echo "Final directory check:"
echo "Tomcat structure:"
ls -la ${TOMCAT_HOME}/
echo "Startup script exists:"
ls -la ${TOMCAT_HOME}/bin/startup.sh || echo "startup.sh not found!"

# 톰캣 시작
echo "Starting Tomcat..."
if ! sudo systemctl start tomcat; then
    echo "Failed to start Tomcat. Debugging..."
    echo "Service status:"
    sudo systemctl status tomcat.service || true
    echo "Service logs:"
    sudo journalctl -xeu tomcat.service --no-pager -n 10 || true
    echo "Java version:"
    java -version 2>&1 || echo "Java not found"
    echo "Checking startup script:"
    ls -la ${TOMCAT_HOME}/bin/startup.sh || echo "startup.sh missing"
    echo "Manual startup attempt:"
    sudo -u tomcat ${TOMCAT_HOME}/bin/startup.sh || echo "Manual startup failed"
    exit 1
fi

echo "Checking if Tomcat started successfully..."
sleep 10
if sudo systemctl is-active --quiet tomcat; then
    echo "✅ Tomcat is running successfully!"
    echo "Service status:"
    sudo systemctl status tomcat.service --no-pager
else
    echo "❌ Tomcat failed to start properly"
    sudo systemctl status tomcat.service --no-pager
    exit 1
fi

echo "Deployment complete."