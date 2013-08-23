jBPMExample_persistence_basic is a basic jBPM project for Eclipse. 

- JPA persistence
- Human Task
- RuntimeManager
- No dependency on JbpmJUnitTestCase
- Works with H2 database but easy to switch to your database

The main purpose is to be able to create various jBPM examples based on this easily. JbpmJUnitTestCase is nice but it hides some configuration details so this example doesn't depend on it.

To switch database, comment out JBPMHelper methods and uncomment setupDataSource()

```java
    private static void setup() {
        // for H2
//        JBPMHelper.startH2Server();
//        JBPMHelper.setupDataSource();
        
        // for your DB
        setupDataSource();
        
        UserGroupCallbackManager.getInstance().setCallback(new DefaultUserGroupCallbackImpl());
    }
```
configure your database and add jdbc jar to classpath

```java
    public static PoolingDataSource setupDataSource() {
        // Please edit here when you want to use your database
        PoolingDataSource pds = new PoolingDataSource();
        pds.setUniqueName("jdbc/jbpm-ds");
        pds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
        pds.setMaxPoolSize(5);
        pds.setAllowLocalTransactions(true);
        pds.getDriverProperties().put("user", "mysql");
        pds.getDriverProperties().put("password", "mysql");
        pds.getDriverProperties().put("url", "jdbc:mysql://localhost:3306/testbrms531");
        pds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
        pds.init();
        return pds;
    }
```

configure hibernate dialect.
```xml
<!--      <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>  -->
       <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5InnoDBDialect"/>
```
