This is a simple example to demonstrate kjar <- main application dependency.

cd ./jbpm-mvn-kjar
mvn clean install
cd ../jbpm-mvn-main
mvn clean test

If you have configured a container in kie-server, you can run the REST application

cd ../jbpm-mvn-rest
mvn clean test
