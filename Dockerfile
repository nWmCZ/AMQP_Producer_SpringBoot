FROM maven:3-jdk-8
RUN git clone https://github.com/nWmCZ/AMQP_Producer_SpringBoot.git /AMQP_Producer_SpringBoot.git
WORKDIR /AMQP_Producer_SpringBoot.git
RUN mvn clean install

FROM openjdk:8-alpine
COPY --from=0 /AMQP_Producer_SpringBoot.git/target/AMQPProducer-0.1-SNAPSHOT.jar /

ENTRYPOINT ["/usr/bin/java", "-jar", "/AMQPProducer-0.1-SNAPSHOT.jar"]
