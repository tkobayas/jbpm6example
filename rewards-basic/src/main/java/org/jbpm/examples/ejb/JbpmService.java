package org.jbpm.examples.ejb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.EmptyContext;

@Startup
@javax.ejb.Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class JbpmService {

    @Inject
    @org.kie.internal.runtime.manager.cdi.qualifier.Singleton
    private RuntimeManager runtimeManager;

    public long startProcess(String recipient) {

        RuntimeEngine runtime = runtimeManager.getRuntimeEngine(EmptyContext.get());
        KieSession ksession = runtime.getKieSession();

        long processInstanceId = -1;

        // start a new process instance
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recipient", recipient);
        ProcessInstance processInstance = ksession.startProcess("com.sample.rewards-basic", params);

        processInstanceId = processInstance.getId();

        System.out.println("Process started ... : processInstanceId = " + processInstanceId);

        return processInstanceId;
    }

    public List<TaskSummary> retrieveTaskList(String actorId) {

        TaskService taskService = runtimeManager.getRuntimeEngine(EmptyContext.get()).getTaskService();

        List<TaskSummary> list;

        list = taskService.getTasksAssignedAsPotentialOwner(actorId, "en-UK");

        System.out.println("retrieveTaskList by " + actorId);
        for (TaskSummary task : list) {
            System.out.println(" task.getId() = " + task.getId());
        }

        return list;
    }

    public void approveTask(String actorId, long taskId) throws ProcessOperationException {

        TaskService taskService = runtimeManager.getRuntimeEngine(EmptyContext.get()).getTaskService();

        try {
            System.out.println("approveTask (taskId = " + taskId + ") by " + actorId);
            taskService.start(taskId, actorId);
            taskService.complete(taskId, actorId, null);

            // Thread.sleep(10000); // To test OptimisticLockException

        } catch (PermissionDeniedException e) {
            e.printStackTrace();
            // Probably the task has already been started by other users
            throw new ProcessOperationException("The task (id = " + taskId
                    + ") has likely been started by other users ", e);
        }
    }
}
