package com.sample;

import junit.framework.TestCase;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class CallKjarTest extends TestCase {

    @Test
    public void testProcess() throws Exception {

        String groupId = "com.sample";
        String artifactId = "drools-mvn-kjar";
//        String version = "1.0.0-SNAPSHOT";
        
        String version = "LATEST";


        KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId(groupId, artifactId, version);
        KieContainer kcontainer = ks.newKieContainer(releaseId);
        KieScanner kscanner = ks.newKieScanner(kcontainer);
        kscanner.start(2000L);

        for (int i = 0; i < 100; i++) {
            //kscanner.scanNow();

            KieSession ksession = kcontainer.newKieSession();
            ksession.insert(new MyPojo("John"));
            ksession.fireAllRules();

            ksession.dispose();
            
            Thread.sleep(2000);
        }
    }

}