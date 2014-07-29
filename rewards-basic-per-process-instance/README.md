rewards-basic-per-process-instance
=============

jBPM 6 (BPMS 6) example on EAP 6.

- Human tasks
- Persistence
- Transactions
- PerProcessInstance runtime manager
- CMT by EJB (so rely on tx syncronization for disposeRuntimeEngine())
- Context dependency injection
- Maven

### Steps to run
- Make sure you have at least Java 6 and Maven 3 installed
- Download somewhere JBoss EAP 6.2 (was tested on JBoss EAP 6.2, other versions should work too)
- Start the application server (default datasource is ExampleDS, the same as in EAP, so the example works out of the box):
 - cd jboss-eap-6.2/bin
 - ./standalone.sh
- Build and deploy the example application:
 - cd jbpm-6-examples/rewards-basic
 - mvn clean package
 - mvn jboss-as:deploy
- Visit http://localhost:8080/rewards-basic/ with a web browser
 - [Start Reward Process] is to start a new process
 - [John's Task] is to list John's tasks and approve them
 - [Mary's Task] is to list Mary's tasks and approve them


