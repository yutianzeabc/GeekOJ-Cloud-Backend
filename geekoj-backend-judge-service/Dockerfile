FROM azul/zulu-openjdk-alpine:21-latest
MAINTAINER Bill Yu
LABEL authors="Bill Yu"
LABEL version="1.0.0"

ENV TZ=Asia/Shanghai

WORKDIR /
COPY target/geekoj-backend-judge-service-1.0.0.jar app.jar
EXPOSE 8103

ENTRYPOINT ["java", "-server", "-jar", "app.jar"]