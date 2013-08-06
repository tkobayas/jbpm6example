jbpm6example - rewards-basic
============

### This example is written by Jiri Svitak. Thank you!

This is a basic jBPM6 web application, which aims to provide an example usage of:
- Human Task
- Persistence
- RuntimeManager
- mvn building

### Steps to run
- Start EAP 6.1.0
- (Currently not required) Edit rewards-basic/pom.xml to change <brms.library.dir> properties for your environment
- mvn clean package
- cp ear/target/rewards-basic-1.0-SNAPSHOT.war $JBOSS_HOME/standalone/deployments
- access to http://localhost:8080/rewards-basic/ with a browser
 - [Start Reward Process] is to start a new process
 - [John's Task] is to list John's tasks and approve them
 - [Mary's Task] is to list Mary's tasks and approve them
 
- (Not yet verified) reward-basic.jmx is a jmeter test plan for this application.
 - You may see PermissionDeniedException or OptimisticLockException under load. It means that a user started a task which is already completed. It's expected because this test plan may cause concurrent accesses to the same task with the same user. (It may happen in real use cases)
 
