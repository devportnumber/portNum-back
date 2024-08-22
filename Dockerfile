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

ENTRYPOINT ["java","-XX:MaxRAMPercentage=80.0","-Dspring.profiles.active=dev","-jar","/app.jar"]