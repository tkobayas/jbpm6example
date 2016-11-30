package com.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.server.api.marshalling.Marshaller;
import org.kie.server.api.marshalling.MarshallerFactory;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

public class MarshallerExample {

    private static final String BASE_URL = "http://localhost:8080/kie-server/services/rest/server";
    private static final String USERNAME = "kieserver";
    private static final String PASSWORD = "kieserver1!";

    private static final String COMTAINER_ID = "MyContainer";

    public static void main(String[] args) {

        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(BASE_URL, USERNAME, PASSWORD);
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(MyPojo.class);
        config.addJaxbClasses(classes);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(config);

        RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);

        List<Command<?>> commands = new ArrayList<Command<?>>();
        KieCommands commandsFactory = KieServices.Factory.get().getCommands();

        MyPojo myPojo = new MyPojo("John");

        commands.add(commandsFactory.newInsert(myPojo, "fact-1"));
        commands.add(commandsFactory.newFireAllRules("fire-result"));

        BatchExecutionCommand batchExecution = commandsFactory.newBatchExecution(commands);
        
        Marshaller marshaller = MarshallerFactory.getMarshaller(config.getExtraJaxbClasses(), config.getMarshallingFormat(), Thread.currentThread().getContextClassLoader());
        String xml = marshaller.marshall(batchExecution);
        System.out.println("============================");
        System.out.println(xml);
        System.out.println("============================");
    }

}