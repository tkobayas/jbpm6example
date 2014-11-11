package org.jbpm.examples.util;

import javax.persistence.EntityManagerFactory;

import org.jbpm.services.task.HumanTaskConfigurator;
import org.jbpm.services.task.HumanTaskServiceFactory;
import org.jbpm.services.task.wih.ExternalTaskEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;

public class JbpmUtils {

    private static RuntimeManager runtimeManager;
    private static TaskService taskService;

    public synchronized static RuntimeManager getRuntimeManager(EntityManagerFactory emf) {
        if (runtimeManager == null) {
            RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
                    .newDefaultBuilder()
                    .entityManagerFactory(emf)
                    .userGroupCallback(new RewardsUserGroupCallback())
                    .addAsset(ResourceFactory.newClassPathResource("rewards-basic.bpmn"), ResourceType.BPMN2)
                    .get();
            runtimeManager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);
        }
        return runtimeManager;
    }

    public synchronized static TaskService getTaskService(EntityManagerFactory emf) {
        if (taskService == null) {
            // TODO: follow LocalTaskServiceFactory
            HumanTaskConfigurator configurator = HumanTaskServiceFactory.newTaskServiceConfigurator()
                    .entityManagerFactory(emf)
                    .userGroupCallback(new RewardsUserGroupCallback());
            configurator.listener(new ExternalTaskEventListener());
            taskService = configurator.getTaskService();
        }
        return taskService;
    }

}
