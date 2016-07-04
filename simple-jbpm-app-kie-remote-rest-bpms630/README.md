simple-jbpm-app-kie-remote-rest
=============

This is a simple example web application for jBPM 6, which is a fork of 'rewards-basic' example app.

This simple example aims to provide an example usage of:
- kie-remote REST API usage
 - NOTE: BPMS 6.3 has 2 central process engine catpabilities. One is kie-remote API which runs inside business-central. The other is kie-server API.
 - Now kie-server API is recommended. See https://issues.jboss.org/browse/BXMSDOC-630
- Human Tasks
- Persistence
- Transactions
- Maven build

### Steps to run
- Make sure you have at least Java 7 and Maven 3 installed
- Download and install JBoss BPM Suite 6.3
- Make sure you have 3 users with 'rest-all' role
 for example)
 $ bin/add-user.sh -a --user bpmsAdmin --password password1! --role admin,analyst,kie-server,rest-all
 $ bin/add-user.sh -a --user john --password password1! --role user,rest-all,PM
 $ bin/add-user.sh -a --user mary --password password1! --role user,rest-all,HR
- Start BPMS
 - cd jboss-eap-6.4/bin
 - ./standalone.sh
- Build and install the example kjar:
 - cd jbpm6example/simple-jbpm-app-kie-remote-rest-bpmsXXX/simple-jbpm-kjar
 - mvn clean install
- Access to http://localhost:8080/business-central
 - Deploy com.example:simple-jbpm-kjar:1.0.0-SNAPSHOT in [Deploy]->[Process Deployments]
- Build and deploy the example application:
 - cd jbpm6example/simple-jbpm-app-kie-remote-rest-bpmsXXX/simple-jbpm-app-kie-remote-rest-web/
 - mvn clean package
 - copy to JBoss EAP
- Visit http://localhost:8080/simple-jbpm-app/ with a web browser
 - [Start Reward Process] is to start a new process
 - [John's Task] is to list John's tasks and approve them
 - [Mary's Task] is to list Mary's tasks and approve them
