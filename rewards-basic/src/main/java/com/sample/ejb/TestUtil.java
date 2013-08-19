package com.sample.ejb;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import org.jbpm.services.task.impl.model.TaskDataImpl;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.model.InternalTask;
import org.kie.internal.task.api.model.InternalTaskData;

public class TestUtil {

	private static EntityManager em;
	
	private static long taskId;

	public static void setEm(EntityManager entityManager) {
		em = entityManager;
	}
	
	public static void doPrepare() {
//		String sql = "insert into Task (priority) values (1)";
//		Query query = em.createNativeQuery(sql);
//		query.executeUpdate();
	
		InternalTask task = new TaskImpl();
		task.setPriority(1);
		InternalTaskData taskData = new TaskDataImpl();
		task.setTaskData(taskData);
		em.persist(task); // FlushModeType.COMMIT doesn't work for persist()
		
		taskId = task.getId();
	}

	public static void doTrick() {
		em.setFlushMode(FlushModeType.COMMIT);

		InternalTask task = (InternalTask)em.find(org.jbpm.services.task.impl.model.TaskImpl.class, taskId);
		task.setTaskData(null);
		//em.flush();
		
	}
}
