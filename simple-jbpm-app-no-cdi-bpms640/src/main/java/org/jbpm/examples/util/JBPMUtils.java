package org.jbpm.examples.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.jbpm.services.task.HumanTaskConfigurator;
import org.jbpm.services.task.HumanTaskServiceFactory;
import org.jbpm.services.task.identity.DefaultUserInfo;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.services.task.impl.command.CommandBasedTaskService;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.RuntimeEnvironment;

public class JBPMUtils {
	
	private static RuntimeManager runtimeManager;
	private static TaskService sharedTaskService;
	
	private static EntityManagerFactory emf;

	public static RuntimeManager getRuntimeManager() {
		if (runtimeManager == null) {
			RuntimeEnvironment environment = RuntimeEnvironmentBuilder.getDefault()
				.entityManagerFactory( getEntityManagerFactory())
				.userGroupCallback(getUserGroupCalback())
				.addAsset(ResourceFactory.newClassPathResource("rewards-basic.bpmn2"), ResourceType.BPMN2)
				.get();
		
			runtimeManager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);
		}
		return runtimeManager;
	}
	
	public static TaskService getSharedTaskService() {
		if (sharedTaskService == null) {
			EntityManagerFactory emf = getEntityManagerFactory();
			HumanTaskConfigurator configurator = HumanTaskServiceFactory.newTaskServiceConfigurator()
					.entityManagerFactory(emf)
					.userGroupCallback(getUserGroupCalback())
					.userInfo(new DefaultUserInfo(true));

			sharedTaskService = (CommandBasedTaskService) configurator.getTaskService();
		}
		return sharedTaskService;
	}

	private static UserGroupCallback getUserGroupCalback() {
		return new JBossUserGroupCallbackImpl("classpath:/usergroups.properties");
	}

	private static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("org.jbpm.sample");
		}
		return emf;
	}
}
