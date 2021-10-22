FROM maven:3.8.3-jdk-8-slim

LABEL maintainer="benwk<ben@delay.work>"

# Set build arguments
ARG app=meta-service-api
ARG port=8080

# Set environment variable
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ENV APP $app
ENV PORT $port

# Set build directory
WORKDIR /app
RUN mkdir -p /app /app/build /app/base
COPY . /app/build

# Build application
RUN cd build \
    && mvn clean package -Dmaven.test.skip=true \
    && mv target/*.jar /app/base/ \
    && rm -rf /app/build

# Set port
EXPOSE $PORT

# Start script
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app/base/$APP.jar