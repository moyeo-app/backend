# 1. Gradle 기반 빌드 이미지
FROM gradle:8.5.0-jdk17 AS builder
WORKDIR /app

# 2. 의존성 캐싱을 위해 설정 파일만 먼저 복사
COPY build.gradle ./
COPY settings.gradle ./

# 3. Gradle 의존성 캐시 생성
RUN gradle dependencies --no-daemon

# 4. 전체 프로젝트 복사 및 빌드
COPY . .
RUN gradle build --no-daemon -x test

# 5. 실행용 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 6. 빌드된 JAR 복사 (이름은 빌드 결과에 맞게 수정)
COPY --from=builder /app/build/libs/*.jar app.jar

# 7. 실행 명령어 (Spring profile 설정 포함)
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=local"]

# 8. 포트 노출 (Spring 서버 포트)
EXPOSE 8080