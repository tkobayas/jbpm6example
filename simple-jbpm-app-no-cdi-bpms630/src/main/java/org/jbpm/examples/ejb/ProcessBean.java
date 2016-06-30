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
