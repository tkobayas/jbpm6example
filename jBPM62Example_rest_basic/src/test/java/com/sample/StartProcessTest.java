package com.sample;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

public class StartProcessTest extends TestCase 
{
    public void testRest() throws Exception
    {
       
        URL deploymentUrl = new URL( "http://localhost:8080/business-central/" );
        
        RuntimeEngine engine = RemoteRuntimeEngineFactory.newRestBuilder()
                .addUrl(deploymentUrl)
                .addUserName("bpmsAdmin")
                .addPassword("password1!")
                .addDeploymentId("org.kie.example:project1:1.0.0-SNAPSHOT")
                .build();

        KieSession ksession = engine.getKieSession();
        TaskService taskService = engine.getTaskService();
        AuditService auditService = engine.getAuditService();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("var1", "AAA");
        
        
        ProcessInstance processInstance = ksession.startProcess("project1.helloProcess", params);
        
        List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("bpmsAdmin", "en-UK");
        for (TaskSummary taskSummary : list) {
            System.out.println("taskSummary.getId() = " + taskSummary.getId());
            long taskId = taskSummary.getId();
            Task task = taskService.getTaskById(taskId);
            taskService.start(taskId, "bpmsAdmin");
            taskService.complete(taskId, "bpmsAdmin", null);
        }
    }
}