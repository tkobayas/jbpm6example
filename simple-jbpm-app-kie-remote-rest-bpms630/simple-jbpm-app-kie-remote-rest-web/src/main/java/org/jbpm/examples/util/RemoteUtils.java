package org.jbpm.examples.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

public class RemoteUtils {

    public static final String DEPLOYMENT_URL = "http://localhost:8080/business-central/";
    public static final String ADMIN_USERNAME = "bpmsAdmin";
    public static final String ADMIN_PASSWORD = "password1!";

    public static final String DEPLOYMENT_ID = "com.sample:simple-jbpm-kjar:1.0.0-SNAPSHOT";

    public static RuntimeEngine getRemoteRuntimeEngineForAdmin() {
        return getRemoteRuntimeEngine(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public static RuntimeEngine getRemoteRuntimeEngine(String username, String password) {
        RuntimeEngine engine;
        try {
            engine = RemoteRuntimeEngineFactory.newRestBuilder()
                    .addUrl(new URL(DEPLOYMENT_URL))
                    .addUserName(username)
                    .addPassword(password)
                    .addDeploymentId(DEPLOYMENT_ID)
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return engine;
    }
}
