package com.sample;

import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

public class DisposeContainerViaKieServerExample {

    private static final String BASE_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String USERNAME = "kieserver";
    private static final String PASSWORD = "kieserver1!";

    private static final String COMTAINER_ID = "MyContainer";

    public static void main(String[] args) {

        // WARN:
        // Do not use this example when you set up Managed Server.
        // In case of Managed Server, Controller (= business central) is responsible for disposing containers
        // If you dispose a container via Kie Server REST (as this example),
        // actually it works. But it will not sync with Controller and be re-registered on restart.
        
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(BASE_URL, USERNAME, PASSWORD);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);

        ServiceResponse<Void> response = client.disposeContainer(COMTAINER_ID);

        System.out.println(response);
    }

}