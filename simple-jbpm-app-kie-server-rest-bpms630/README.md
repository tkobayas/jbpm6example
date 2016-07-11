simple-jbpm-app-kie-server-rest
=============

This is a simple example web application for jBPM 6, which is a fork of 'rewards-basic' example app.

This simple example aims to provide an example usage of:
- kie-server REST API usage
 - NOTE: BPMS 6.3 has 2 central process engine catpabilities. One is kie-remote API which runs inside business-central. The other is kie-server API.
 - Now kie-server API is recommended. See https://issues.jboss.org/browse/BXMSDOC-630
- Human Tasks
- Persistence
- Transactions
- Maven build

### Steps to run
- Make sure you have at least Java 7 and Maven 3 installed
- Download and install JBoss BPM Suite 6.3
- Set up kie-server referring https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_BPM_Suite/6.3/html/User_Guide/Realtime_Decision_Server_Setup.html
- Make sure you have 3 users with 'kie-server' role
 for example)
 $ bin/add-user.sh -a --user bpmsAdmin --password password1! --role admin,analyst,kie-server,rest-all
 $ bin/add-user.sh -a --user john --password password1! --role user,kie-server,PM
 $ bin/add-user.sh -a --user mary --password password1! --role user,kie-server,HR
- Start BPMS
 - cd jboss-eap-6.4/bin
 - ./standalone.sh
- Build and install the example kjar:
 - cd jbpm6example/simple-jbpm-app-kie-server-rest-bpmsXXX/simple-jbpm-kjar
 - mvn clean install
- Access to http://localhost:8080/business-central
 - Create a container in [Deploy]->[Rule Deployments]
  - Name : MyContainer
  - Group Name : com.sample
  - Artifact Id : simple-jbpm-kjar
  - Version : 1.0.0-SNAPSHOT
- Build and deploy the example application:
 - cd jbpm6example/simple-jbpm-app-kie-server-rest-bpmsXXX/simple-jbpm-app-kie-server-rest-web/
 - mvn clean package
 - copy to JBoss EAP
- Visit http://localhost:8080/simple-jbpm-app/ with a web browser
 - [Start Reward Process] is to start a new process
 - [John's Task] is to list John's tasks and approve them
 - [Mary's Task] is to list Mary's tasks and approve them

### Extra note
This example passes username/password for each operatiing user (e.g. 'john', 'mary').

If you set system property "org.kie.server.bypass.auth.user" to true (in both server/client JVMs. In this case, the same one), you can use the same one user (e.g. 'kieserver') for all kie-server-client operations.

        <property name="org.kie.server.bypass.auth.user" value="true"/>
