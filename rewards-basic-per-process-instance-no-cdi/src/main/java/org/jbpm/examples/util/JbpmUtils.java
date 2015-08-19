package org.jbpm.examples.util;

import javax.persistence.EntityManagerFactory;

import org.jbpm.services.task.HumanTaskConfigurator;
import org.jbpm.services.task.HumanTaskServiceFactory;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;

public class JbpmUtils {

    private static RuntimeManager runtimeManager;
    private static TaskService readOnlyTaskService;
    
    public synchronized static void init(EntityManagerFactory emf) {
        if (runtimeManager == null) {
            RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
                    .newDefaultBuilder()
                    .entityManagerFactory(emf)
                    .userGroupCallback(new RewardsUserGroupCallback())
                    .addAsset(ResourceFactory.newClassPathResource("rewards-basic.bpmn"), ResourceType.BPMN2)
                    .get();
            runtimeManager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);
        }
        
        if (readOnlyTaskService == null) {
            HumanTaskConfigurator configurator = HumanTaskServiceFactory.newTaskServiceConfigurator()
                    .entityManagerFactory(emf)
                    .userGroupCallback(new RewardsUserGroupCallback());
            readOnlyTaskService = configurator.getTaskService();
        }
    }

    public static RuntimeManager getRuntimeManager() {
        return runtimeManager;
    }

    public static TaskService getReadOnlyTaskService() {
        return readOnlyTaskService;
    }
}
