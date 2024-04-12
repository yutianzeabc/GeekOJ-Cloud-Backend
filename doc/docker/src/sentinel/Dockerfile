FROM zulu-openjdk-alpine:8-latest

ENV JAVA_OPTS="-Dsentinel.dashboard.auth.username=sentinel -Dsentinel.dashboard.auth.password=sentinel"

WORKDIR /
COPY sentinel-dashboard-1.8.7.jar app.jar

EXPOSE 8858
EXPOSE 8719

CMD java -server $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dserver.port=8858 -Dcsp.sentinel.api.port=8719 -Dcsp.sentinel.dashboard.server=localhost:8858 -Dproject.name=sentinel-dashboard -jar app.jar