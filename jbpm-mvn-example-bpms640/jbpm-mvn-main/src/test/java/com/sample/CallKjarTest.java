package com.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.tools.Server;
import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.JBPMHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

import bitronix.tm.resource.jdbc.PoolingDataSource;

public class CallKjarTest {

    private static EntityManagerFactory emf;

    private static Server h2Server;
    private static PoolingDataSource ds;

    @Before
    public void setup() {

        // for H2 datasource
        h2Server = JBPMHelper.startH2Server();
        ds = JBPMHelper.setupDataSource();

        Map configOverrides = new HashMap();
        configOverrides.put( "hibernate.hbm2ddl.auto", "create" ); // Uncomment if you don't want to clean up tables

        configOverrides.put( "hibernate.dialect", "org.hibernate.dialect.H2Dialect" );

        emf = Persistence.createEntityManagerFactory( "org.jbpm.example", configOverrides );
    }

    @After
    public void teardown() {
        if ( ds != null ) {
            ds.close();
        }
        if ( h2Server != null ) {
            h2Server.shutdown();
        }
    }

    @Test
    public void testProcess() {

        String groupId = "com.sample";
        String artifactId = "jbpm-mvn-kjar";
        String version = "1.0.0-SNAPSHOT";

        KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId( groupId, artifactId, version );

        Properties properties = new Properties();
        properties.setProperty( "bpmsAdmin", "" );
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl( properties );

        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder( releaseId ).persistence( true ).entityManagerFactory( emf )
                .userGroupCallback( userGroupCallback ).get();
        RuntimeManager runtimeManager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager( environment );
        RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine( ProcessInstanceIdContext.get() );
        KieSession ksession = runtimeEngine.getKieSession();

        // start a new process instance
        Map<String, Object> params = new HashMap<String, Object>();
        ProcessInstance pi = ksession.startProcess("defaultPackage.helloProcess", params);
        long piid = pi.getId();
        System.out.println("A process instance started : piid = " + piid);
        
        runtimeManager.disposeRuntimeEngine(runtimeEngine);
        
        // work on task
        RuntimeEngine runtimeEngine2 = runtimeManager.getRuntimeEngine( ProcessInstanceIdContext.get( piid )) ;
        TaskService taskService = runtimeEngine2.getTaskService();

        {
            List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("bpmsAdmin", "en-UK");
            for (TaskSummary taskSummary : list) {
                System.out.println("bpmsAdmin starts a task : taskId = " + taskSummary.getId());
                taskService.start(taskSummary.getId(), "bpmsAdmin");
                taskService.complete(taskSummary.getId(), "bpmsAdmin", null);
            }
        }

        runtimeManager.disposeRuntimeEngine(runtimeEngine2);


        // -----------
    }

}