#!/bin/bash

# 환경 변수 설정 (GitHub Actions에서 주입)
# TOMCAT_HOME: 톰캣 설치 경로 (예: /opt/tomcat)
# EC2_USERNAME: EC2 사용자 이름

TOMCAT_VERSION="9.0.89" # 사용할 톰캣 버전
TOMCAT_URL="https://archive.apache.org/dist/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz"

TOMCAT_HOME="/opt/tomcat"
WAR_FILE="/home/${EC2_USERNAME}/dondothat.war"
WEBAPPS_PATH="${TOMCAT_HOME}/webapps"

# 톰캣 설치 확인 및 설치
if [ ! -d "${TOMCAT_HOME}" ]; then
    echo "Tomcat not found at ${TOMCAT_HOME}. Installing Tomcat..."

    # 톰캣 사용자 및 그룹 생성
    sudo groupadd tomcat
    sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat

    # 톰캣 다운로드 및 압축 해제
    mkdir -p /tmp/tomcat_install
    wget -q ${TOMCAT_URL} -O /tmp/tomcat_install/tomcat.tar.gz
    sudo tar -xzf /tmp/tomcat_install/tomcat.tar.gz -C /opt/
    sudo mv /opt/apache-tomcat-${TOMCAT_VERSION} ${TOMCAT_HOME}
    rm -rf /tmp/tomcat_install

    # 권한 설정
    sudo chown -RH tomcat:tomcat ${TOMCAT_HOME}
    sudo sh -c 'chmod +x ${TOMCAT_HOME}/bin/*.sh'

    # Systemd 서비스 파일 생성
    echo "Creating Tomcat systemd service file..."
    sudo bash -c "cat > /etc/systemd/system/tomcat.service <<EOF
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
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
EOF"

    # Systemd 데몬 리로드 및 톰캣 서비스 활성화
    echo "Enabling Tomcat service..."
    sudo systemctl daemon-reload
    sudo systemctl enable tomcat
    echo "Tomcat installation complete."
else
    echo "Tomcat already installed at ${TOMCAT_HOME}. Skipping installation."
fi

# 톰캣 중지
echo "Stopping Tomcat..."
sudo systemctl stop tomcat || true # 서비스가 실행 중이 아닐 수도 있으므로 오류 무시
sleep 10 # 톰캣이 완전히 중지될 때까지 대기

# 기존 애플리케이션 삭제
echo "Removing old application..."
sudo rm -rf "${WEBAPPS_PATH}/dondothat"
sudo rm -f "${WEBAPPS_PATH}/dondothat.war"

# 새로운 WAR 파일 복사
echo "Copying new WAR file..."
sudo mv "${WAR_FILE}" "${WEBAPPS_PATH}/"

# 톰캣 시작
echo "Starting Tomcat..."
sudo systemctl start tomcat

echo "Deployment complete."
