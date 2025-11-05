# Use official Tomcat base image
FROM tomcat:10.1-jdk17

# Set environment variables
ENV APP_NAME=company-webapp
ENV DEPLOY_DIR=/usr/local/tomcat/webapps

# Remove default ROOT app
RUN rm -rf $DEPLOY_DIR/ROOT

# Copy the built WAR file from Maven target/
COPY target/${APP_NAME}.war $DEPLOY_DIR/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
