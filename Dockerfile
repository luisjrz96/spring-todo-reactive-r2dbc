FROM maven:3.6.3-jdk-11-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
WORKDIR /home/app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/spring-reactive-todo-0.0.1.jar"]
