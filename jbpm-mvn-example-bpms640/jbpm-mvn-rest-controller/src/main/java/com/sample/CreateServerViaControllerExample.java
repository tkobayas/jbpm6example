package com.sample;

import java.util.ArrayList;
import java.util.List;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieContainerStatus;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.controller.api.model.KieServerInstance;

public class CreateServerViaControllerExample {

    private static final String KIESERVER_ID = "local-server-456";

    private static final String CONTROLLER_URL = "http://localhost:8080/business-central/rest/controller";
    private static final String USERNAME = "kieserver";
    private static final String PASSWORD = "kieserver1!";

    public static void main(String[] args) {
        
        // Controller Client
        KieServerControllerClient controllerClient = new KieServerControllerClient(CONTROLLER_URL, USERNAME, PASSWORD);
        controllerClient.setMarshallingFormat(MarshallingFormat.JAXB);

        List<String> capabilities = new ArrayList<String>();
        capabilities.add("KieServer");
        capabilities.add("BRM");
        
        KieServerInfo kieServerInfo = new KieServerInfo(KIESERVER_ID, KIESERVER_ID, "6.3.0.Final", capabilities, "http://localhost:8080/kie-server/services/rest/server");
        KieServerInstance createdServer = controllerClient.createKieServerInstance(kieServerInfo);
        System.out.println(createdServer);
    }

}