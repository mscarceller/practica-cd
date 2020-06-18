FROM maven:3.6.3-jdk-8 as builder
COPY . /project/
WORKDIR /project
RUN mvn -f pom.xml clean package -B -DskipTests=true

FROM openjdk:8
RUN apt-get update; apt-get install -y netcat
COPY --from=builder /project/target/*.jar /project/
COPY ./run.sh /project/
WORKDIR /project
RUN chmod 777 /project/run.sh
EXPOSE 8080
CMD ["/project/run.sh"]