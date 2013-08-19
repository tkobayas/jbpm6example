package com.sample.ejb;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.runtime.manager.context.EmptyContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessBean implements ProcessLocal {

    @Resource
    private UserTransaction ut;
    
    @PersistenceContext
    EntityManager em;

    @Inject
    @Singleton
    RuntimeManager singletonManager;
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public long startProcess(String recipient) throws Exception {
    	
    	// For test
    	TestUtil.setEm(em);
    	ut.begin();
    	TestUtil.doPrepare();
    	ut.commit();

        RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext.get());
        KieSession ksession = runtime.getKieSession();

        long processInstanceId = -1;
        
//        ut.begin();
//
//        try {
            // start a new process instance
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("recipient", recipient);
            ProcessInstance processInstance = null;
            
			try {
				processInstance = ksession.startProcess(
						"com.sample.rewards-basic", params);
			} catch (Exception e) {
				// JBPM-3934 : Can I catch the Exception on commit? (not on rollback)
				e.printStackTrace();
				return -1;
			}
            processInstanceId = processInstance.getId();

            System.out.println("Process started ... : processInstanceId = " + processInstanceId);

//            ut.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (ut.getStatus() == Status.STATUS_ACTIVE) {
//                ut.rollback();
//            }
//            throw e;
//        }

        return processInstanceId;
    }
    
}
