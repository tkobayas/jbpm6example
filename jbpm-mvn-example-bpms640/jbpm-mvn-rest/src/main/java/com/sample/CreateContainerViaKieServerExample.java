package com.sample;

import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

public class CreateContainerViaKieServerExample {

    private static final String BASE_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String USERNAME = "kieserver";
    private static final String PASSWORD = "kieserver1!";
    
    private static final String GROUP_ID = "com.sample";
    private static final String ARTIFACT_ID = "drools-mvn-kjar";
    private static final String VERSION = "1.0.0-SNAPSHOT";

    private static final String COMTAINER_ID = "MyContainer";

    public static void main(String[] args) {

        // WARN:
        // Do not use this example when you set up Managed Server.
        // In case of Managed Server, Controller (= business central) is responsible for creating containers
        // If you create a container via Kie Server REST (as this example),
        // actually it works. But it will not sync with Controller and be lost on restart.
        
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(BASE_URL, USERNAME, PASSWORD);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);

        
        ReleaseId releaseId = new ReleaseId(GROUP_ID, ARTIFACT_ID, VERSION);
        KieContainerResource resource = new KieContainerResource(COMTAINER_ID, releaseId);
        ServiceResponse<KieContainerResource> response = client.createContainer(COMTAINER_ID, resource);

        System.out.println(response);
    }

}