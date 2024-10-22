FROM ghg46061.live.dynatrace.com/linux/oneagent-codemodules:java AS dynaOAJava

FROM openjdk:17-buster AS thumbnail-generator

COPY --from=dynaOAJava / /
ENV LD_PRELOAD=/opt/dynatrace/oneagent/agent/lib64/liboneagentproc.so

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} thumbnail-generator-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/thumbnail-generator-0.0.1-SNAPSHOT.jar"]
