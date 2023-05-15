# TWO-STAGE BUILD DOCKER FILE

ARG BUILD_IMAGE=maven:3.8.3-openjdk-17
ARG RUNTIME_IMAGE=openjdk:17

#############################################################################################
###                Stage where Docker is pulling all maven dependencies                   ###
#############################################################################################
FROM ${BUILD_IMAGE} as dependencies

COPY ./pom.xml ./

RUN mvn -B dependency:go-offline
#############################################################################################


#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM dependencies as build

COPY ./src ./src

RUN mvn -B clean package
#############################################################################################


#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM ${RUNTIME_IMAGE}
ARG JAR_FILE=target/server_coursework-0.1.jar
COPY --from=build ${JAR_FILE} app.jar
#RUN echo "#!/bin/bash" >> entrypoint.sh
#RUN echo "sleep \$ENTRY_DELAY" >> entrypoint.sh
#RUN echo "java -jar app.jar --spring.profiles.active=dev" >> entrypoint.sh
#RUN chmod +x entrypoint.sh
#ENTRYPOINT ["/entrypoint.sh"]
CMD ["java", "-jar", "app.jar"]