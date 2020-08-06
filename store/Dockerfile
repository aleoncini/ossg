# Build stage
FROM registry.redhat.io/ubi8/openjdk-8 as builder
RUN pwd
COPY --chown=jboss:jboss ./pom.xml ./
RUN ls -lrt /home/jboss
RUN mvn dependency:go-offline -B
COPY --chown=jboss:jboss ./ ./
RUN mvn clean package

# Create final image
FROM registry.redhat.io/ubi8/openjdk-8
COPY --from=builder /home/jboss/target/store-1.0-SNAPSHOT-runner.jar ./
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dquarkus.mongodb.mrt.connection-string=${MONGOCONNSTRING}","-jar","./store-1.0-SNAPSHOT-runner.jar"]
