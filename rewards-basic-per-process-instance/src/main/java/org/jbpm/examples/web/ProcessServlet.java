/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.examples.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.JPAAuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.examples.ejb.JbpmService;

public class ProcessServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private JbpmService jbpmService;

	private static final String PERSISTENCE_UNIT_NAME = "org.jbpm.examples.rewards-basic";
	@PersistenceUnit(unitName = PERSISTENCE_UNIT_NAME)
	private EntityManagerFactory emf;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String cmd = req.getParameter("cmd");
		System.out.println("### cmd: " + cmd);

		if (cmd.equals("start")) {

			long processInstanceId = -1;
			try {
				String recipient = req.getParameter("recipient");
				processInstanceId = jbpmService.startProcess(recipient);
			} catch (Exception e) {
				throw new ServletException(e);
			}

			req.setAttribute("message", "process instance (id = " + processInstanceId + ") has been started.");

			ServletContext context = this.getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");
			dispatcher.forward(req, res);

		} else if (cmd.equals("abort")) {

			try {
				List<Long> processInstanceIds = new ArrayList<Long>();
				
				long processInstanceId = Long.parseLong(req.getParameter("processInstanceId"));
				System.out.println("### process instance id: " + processInstanceId);
				
//				AuditLogService auditLogService = new JPAAuditLogService(emf);
//				List<ProcessInstanceLog> processInstances = auditLogService.findSubProcessInstances(processInstanceId);
//
//				for (ProcessInstanceLog processInstance : processInstances) {
//					System.out.println("### sub process instance id: " + processInstance.getId());
//					processInstanceIds.add(processInstance.getId());
//				}
				processInstanceIds.add(processInstanceId);

				for (Long pid : processInstanceIds) {
					System.out.println("<<< process instance id: " + pid);
				}
				
				jbpmService.abortProcess(processInstanceIds);
			} catch (Exception e) {
				throw new ServletException(e);
			}

			ServletContext context = this.getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher("/index.jsp");
			dispatcher.forward(req, res);
			
		}	else if (cmd.equals("list")) {

			AuditLogService auditLogService = new JPAAuditLogService(emf);
			List<ProcessInstanceLog> processInstances = auditLogService.findProcessInstances();
			
			req.setAttribute("processInstances", processInstances);

			ServletContext context = this.getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher("/abortProcess.jsp");
			dispatcher.forward(req, res);
		}
	}
}