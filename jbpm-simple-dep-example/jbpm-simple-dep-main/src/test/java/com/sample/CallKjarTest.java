package com.sample;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;

import org.jbpm.test.JBPMHelper;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;

public class CallKjarTest extends TestCase {

    @Test
	public void testProcess() {
                
        String groupId = "com.sample";
        String artifactId = "jbpm-simple-dep-kjar";
        String version = "1.0.0-SNAPSHOT";
        
		KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId(groupId, artifactId, version);
		RuntimeManager manager = createRuntimeManager(releaseId);
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();

		ksession.startProcess("sample");

		manager.disposeRuntimeEngine(engine);
	}

	private RuntimeManager createRuntimeManager(ReleaseId releaseId) {
		JBPMHelper.startH2Server();
		JBPMHelper.setupDataSource();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get()
			.newDefaultBuilder(releaseId).entityManagerFactory(emf)
			;
		return RuntimeManagerFactory.Factory.get()
			.newSingletonRuntimeManager(builder.get());
	}

}