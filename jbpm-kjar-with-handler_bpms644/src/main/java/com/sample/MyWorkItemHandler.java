package com.sample;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public class MyWorkItemHandler implements WorkItemHandler {
    
	private KieSession ksession;
	
    public MyWorkItemHandler(KieSession ksession) {
        this.ksession = ksession;
    }

    public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
        // TODO Auto-generated method stub
        
    }

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        System.out.println("MyWorkItemHandler.executeWorkItem()!!!");
        manager.completeWorkItem(workItem.getId(), null);
    }

}
