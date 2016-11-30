package com.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.RuleServicesClient;
import org.kie.server.client.UserTaskServicesClient;

public class KieServerClientTest extends TestCase {

    private static final String BASE_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String USERNAME = "bpmsAdmin";
    private static final String PASSWORD = "password1!";

    private static final String CONTAINER_ID = "MyContainer";
    
    private static final String PROCESS_ID = "defaultPackage.helloProcess";


    @Test
    public void testProcess() {

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(BASE_URL, USERNAME, PASSWORD);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);

        ProcessServicesClient processClient = client.getServicesClient(ProcessServicesClient.class);

        processClient.startProcess( CONTAINER_ID, PROCESS_ID );
        
        UserTaskServicesClient taskClient = client.getServicesClient(UserTaskServicesClient.class);

        List<TaskSummary> taskList = taskClient.findTasksAssignedAsPotentialOwner( "bpmsAdmin", 0, 10);
        for ( TaskSummary taskSummary : taskList ) {
            taskClient.startTask(CONTAINER_ID, taskSummary.getId(), "bpmsAdmin");
            taskClient.completeTask(CONTAINER_ID, taskSummary.getId(), "bpmsAdmin", null);
        }
    }

}