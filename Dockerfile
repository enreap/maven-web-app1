FROM tomcat:9-jdk11

COPY target/maven-webapp.war /usr/local/tomcat/webapps/

EXPOSE 8080
