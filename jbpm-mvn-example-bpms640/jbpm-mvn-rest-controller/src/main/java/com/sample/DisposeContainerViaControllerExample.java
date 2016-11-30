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

public class DisposeContainerViaControllerExample {

    private static final String KIESERVER_ID = "local-server-123";

    private static final String CONTROLLER_URL = "http://localhost:8080/business-central/rest/controller";
    private static final String USERNAME = "kieserver";
    private static final String PASSWORD = "kieserver1!";
    
    private static final String GROUP_ID = "com.sample";
    private static final String ARTIFACT_ID = "drools-mvn-kjar";
    private static final String VERSION = "1.0.0-SNAPSHOT";

    private static final String CONTAINER_ID = "MyContainer";

    public static void main(String[] args) {
        
        // Controller Client
        KieServerControllerClient controllerClient = new KieServerControllerClient(CONTROLLER_URL, USERNAME, PASSWORD);
        controllerClient.setMarshallingFormat(MarshallingFormat.JAXB);

        // Undeploy container for kie server instance.
        controllerClient.disposeContainer(KIESERVER_ID, CONTAINER_ID);
    }

}