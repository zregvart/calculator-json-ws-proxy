FROM maven:3-jdk-8 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn -U -B -q dependency:go-offline
COPY src src
RUN mvn -B -q package -Dtarget=calculator-json-ws-proxy
RUN mvn -B -q package dependency:copy-dependencies

FROM openjdk:8-jre
WORKDIR /app
VOLUME /tmp
EXPOSE 8080
ARG TARGET=/build/target
COPY --from=build ${TARGET}/dependency lib
COPY --from=build ${TARGET}/calculator-json-ws-proxy.jar .
ENTRYPOINT ["java", "-cp", "calculator-json-ws-proxy.jar:lib/*", "com.github.zregvart.Calculator"]