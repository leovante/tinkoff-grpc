FROM openjdk:21
COPY tinkoff-grpc/build/rest-proxy-stub-1.0.jar /usr/local/service/
ENTRYPOINT ["java", "-jar", "-Dliquibase.hub.mode=off", "/usr/local/service/rest-proxy-stub-1.0.jar"]
#dev2

MAINTAINER Temnikov Dmitry