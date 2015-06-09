jBPMExample_persistence_basic_BPMS6 is a basic jBPM project for Eclipse. 

- JPA persistence
- Human Task
- RuntimeManager
- No dependency on JbpmJUnitTestCase
- Works with H2 database but easy to switch to your database

The main purpose is to be able to create various jBPM examples based on this easily. JbpmJUnitTestCase is nice but it hides some configuration details so this example doesn't depend on it.

To switch database, edit the boolean 'H2'. When H2 == false, it uses MySQL but you can change.

```java
    private static final boolean H2 = false;
```

