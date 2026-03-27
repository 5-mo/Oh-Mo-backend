FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /build

# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함.
COPY gradlew build.gradle settings.gradle /build/
COPY gradle /build/gradle/
RUN chmod +x gradlew
RUN ./gradlew build -x test --parallel --continue > /dev/null 2>&1 || true

# 빌더 이미지에서 애플리케이션 빌드
COPY . /build
RUN ./gradlew build -x test --parallel

# APP
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar

EXPOSE 8080

# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT ["java", "-jar", "app.jar"]
