package com.sample;

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

public class DeleteServerViaControllerExample {

    private static final String CONTROLLER_URL = "http://localhost:8080/business-central/rest/controller";
    private static final String USERNAME = "kieserver";
    private static final String PASSWORD = "kieserver1!";

    public static void main(String[] args) {
        
        // Controller Client
        KieServerControllerClient controllerClient = new KieServerControllerClient(CONTROLLER_URL, USERNAME, PASSWORD);
        controllerClient.setMarshallingFormat(MarshallingFormat.JAXB);

        controllerClient.deleteKieServerInstance("local-server-456");
    }

}