package org.jbpm.examples.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.examples.util.RemoteUtils;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;

public class ProcessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String recipient = req.getParameter("recipient");

        long processInstanceId = -1;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("recipient", recipient);
            RuntimeEngine engine = RemoteUtils.getRemoteRuntimeEngineForAdmin();
            KieSession ksession = engine.getKieSession();
            ProcessInstance processInstance = ksession.startProcess("com.sample.rewards-basic", params);
            processInstanceId = processInstance.getId();
        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.setAttribute("message", "process instance (id = " + processInstanceId + ") has been started.");

        ServletContext context = this.getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");
        dispatcher.forward(req, res);
    }
}