FROM openjdk:17.0.1
ADD /target/MySpringConveyor-0.0.1-SNAPSHOT.jar conveyor.jar
ENTRYPOINT ["java", "-jar", "conveyor.jar"]