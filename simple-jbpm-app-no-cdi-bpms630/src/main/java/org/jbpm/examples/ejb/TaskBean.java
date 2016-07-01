package org.jbpm.examples.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jbpm.examples.util.JBPMUtils;
import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class TaskBean {


    public List<TaskSummary> retrieveTaskList(String actorId) throws Exception {

        List<TaskSummary> list;

        list = JBPMUtils.getSharedTaskService().getTasksAssignedAsPotentialOwner(actorId, "en-UK");

        System.out.println("retrieveTaskList by " + actorId);
        for (TaskSummary task : list) {
            System.out.println(" task.getId() = " + task.getId() + ", task.getProcessInstanceId() = " + task.getProcessInstanceId());
        }

        return list;
    }

    public void approveTask(String actorId, long taskId) throws Exception {

        try {
            System.out.println("approveTask (taskId = " + taskId + ") by " + actorId);
            
            Task task = JBPMUtils.getSharedTaskService().getTaskById(taskId);
            
            String deploymentId = task.getTaskData().getDeploymentId();
            System.out.println("deploymentId = " + deploymentId);
            
            RuntimeManager runtimeManager = JBPMUtils.getRuntimeManager();
            RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get(task.getTaskData().getProcessInstanceId()));
            TaskService taskService = runtimeEngine.getTaskService();
            
            taskService.start(taskId, actorId);
            taskService.complete(taskId, actorId, null);

        } catch (PermissionDeniedException e) {
            e.printStackTrace();
            // Probably the task has already been started by other users
            throw new ProcessOperationException("The task (id = " + taskId
                    + ") has likely been started by other users ", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
