FROM gradle:8.5-jdk17 AS build

WORKDIR /app
COPY . .

# Gradle로 WAR 빌드
RUN gradle clean build -x test

FROM tomcat:10.1-jdk17-openjdk-slim

# 기본 ROOT 앱 제거
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# 빌드된 WAR 파일을 ROOT.war로 복사
COPY --from=build /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]