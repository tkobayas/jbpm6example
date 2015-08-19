/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.examples.ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceUnit;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jbpm.examples.util.JbpmUtils;
import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class TaskBean implements TaskLocal {

    @PersistenceUnit(unitName = "org.jbpm.domain")
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction ut;

    @PostConstruct
    public void init() {
        JbpmUtils.init(emf);
    }

    public List<TaskSummary> retrieveTaskList(String actorId) throws Exception {

        ut.begin();

        List<TaskSummary> list;

        try {
            list = JbpmUtils.getReadOnlyTaskService().getTasksAssignedAsPotentialOwner(actorId, "en-UK");
            ut.commit();
        } catch (RollbackException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("retrieveTaskList by " + actorId);
        for (TaskSummary task : list) {
            System.out.println(" task.getId() = " + task.getId());
        }

        return list;
    }

    public void approveTask(String actorId, long taskId) throws Exception {

        ut.begin();

        try {
            System.out.println("approveTask (taskId = " + taskId + ") by " + actorId);

            // Use taskService from RuntimeEngine
            Task taskById = JbpmUtils.getReadOnlyTaskService().getTaskById(taskId);
            RuntimeManager manager = JbpmUtils.getRuntimeManager();
            RuntimeEngine engine = manager.getRuntimeEngine(ProcessInstanceIdContext.get(taskById.getTaskData()
                    .getProcessInstanceId()));
            TaskService taskService = engine.getTaskService();

            taskService.start(taskId, actorId);
            taskService.complete(taskId, actorId, null);

            ut.commit(); // engine is disposed on commit
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                ut.rollback();
            }
            // Probably the task has already been started by other users
            throw new ProcessOperationException("The task (id = " + taskId
                    + ") has likely been started by other users ", e);
        } catch (Exception e) {
            e.printStackTrace();
            if (ut.getStatus() == Status.STATUS_ACTIVE) {
                ut.rollback();
            }
            throw new RuntimeException(e);
        }
    }

}
