#!/bin/bash

# 변수 설정
TOMCAT_PATH="/path/to/your/tomcat"
WAR_FILE="/home/your_ec2_username/dondothat.war"
WEBAPPS_PATH="$TOMCAT_PATH/webapps"

# 톰캣 중지
$TOMCAT_PATH/bin/shutdown.sh

# 기존 애플리케이션 삭제
rm -rf $WEBAPPS_PATH/dondothat
rm -f $WEBAPPS_PATH/dondothat.war

# 새로운 WAR 파일 복사
mv $WAR_FILE $WEBAPPS_PATH/

# 톰캣 시작
$TOMCAT_PATH/bin/startup.sh
