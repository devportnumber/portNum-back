FROM amazoncorretto:17-alpine as corretto-jdk
RUN apk add --no-cache binutils
RUN jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /jre

FROM alpine:latest

ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=corretto-jdk /jre $JAVA_HOME

COPY build/libs/*.jar app.jar

# Jasypt 암호화 비밀번호를 위한 ARG 및 ENV 설정
ARG JASYPT_ENCRYPTOR_PASSWORD
ENV JASYPT_ENCRYPTOR_PASSWORD=${JASYPT_ENCRYPTOR_PASSWORD}

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=80.0", "-Dspring.profiles.active=dev", "-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}", "-jar", "/app.jar"]