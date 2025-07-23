FROM openjdk:17-jdk-slim AS build

WORKDIR /app
COPY . .

# Gradle Wrapper 실행 권한 부여
RUN chmod +x gradlew

# Gradle 빌드 (정확한 경로로 실행)
RUN ./gradlew clean build -x test --no-daemon

FROM tomcat:10.1-jdk17-openjdk-slim

# 기본 ROOT 앱 제거
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# 빌드된 WAR 파일을 ROOT.war로 복사
COPY --from=build /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]