FROM azul/zulu-openjdk-alpine:21-latest
MAINTAINER Bill Yu
LABEL authors="Bill Yu"
LABEL version="1.0.0"

ENV TZ=Asia/Shanghai

WORKDIR /
COPY target/geekoj-backend-gateway-1.0.0.jar app.jar
EXPOSE 8100

ENTRYPOINT ["java", "-server", "-jar", "app.jar"]