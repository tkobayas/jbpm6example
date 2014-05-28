This is a simple example to demonstrate jar <- kjar <- main application dependency.

cd jbpm-simple-dep-jar
mvn clean install
cd ../jbpm-simple-dep-kjar
mvn clean install
cd ../jbpm-simple-dep-main
mvn test
