package org.jbpm.examples.util;

import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.services.api.model.DeploymentUnit;
import org.jbpm.services.ejb.api.DeploymentServiceEJBLocal;
import org.kie.internal.runtime.conf.RuntimeStrategy;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class StartupBean {

    public static final String DEPLOYMENT_ID = "com.sample:simple-jbpm-kjar:1.0.0-SNAPSHOT";

    @EJB
    DeploymentServiceEJBLocal deploymentService;

    @PostConstruct
    public void init() {
        String[] gav = DEPLOYMENT_ID.split(":");
        KModuleDeploymentUnit deploymentUnit = new KModuleDeploymentUnit(gav[0], gav[1], gav[2]);
        deploymentUnit.setStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE);
        deploymentService.deploy(deploymentUnit);
        System.out.println("DeploymentUnit : " + deploymentUnit);
    }
}
