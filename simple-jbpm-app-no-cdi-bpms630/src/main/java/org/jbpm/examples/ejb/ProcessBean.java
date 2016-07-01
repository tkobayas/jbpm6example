package org.jbpm.examples.ejb;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jbpm.examples.util.JBPMUtils;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ProcessBean {

    public long startProcess(String recipient) throws Exception {

        RuntimeManager runtimeManager = JBPMUtils.getRuntimeManager();
        RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(ProcessInstanceIdContext.get());
        KieSession ksession = runtimeEngine.getKieSession();
        
        long processInstanceId = -1;

        try {
            System.out.println("Starting a process: ksession.getId() = " + ksession.getIdentifier());
            
            // start a new process instance
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("recipient", recipient);
            ProcessInstance processInstance = ksession.startProcess("com.sample.rewards-basic", params);

            processInstanceId = processInstance.getId();

            System.out.println("Process started ... : processInstanceId = " + processInstanceId);

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return processInstanceId;
    }

}
