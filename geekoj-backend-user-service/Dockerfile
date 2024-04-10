FROM azul/zulu-openjdk-alpine:17-latest
MAINTAINER Bill Yu
LABEL authors="Bill Yu"
LABEL version="1.0.0"

ENV TZ=Asia/Shanghai

WORKDIR /
COPY target/geekoj-backend-user-service-1.0.0.jar app.jar
EXPOSE 8101

ENTRYPOINT ["java", "-server", "-jar", "app.jar"]