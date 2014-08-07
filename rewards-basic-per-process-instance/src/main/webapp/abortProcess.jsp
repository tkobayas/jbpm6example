<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Abort a process</title>
</head>
<body>
<table border="1">
<tr>
<th scope="col">ID</th>
<th scope="col">Process Name</th>
<th scope="col">Status</th>
<th scope="col"></th>
</tr>
<c:forEach var="processInstance" items="${processInstances}" varStatus="status">
<tr>
<td><c:out value="${processInstance.id}" /></td>
<td><c:out value="${processInstance.processName}" /></td>
<td><c:out value="${processInstance.status}" /></td>
<c:if test="${processInstance.status == 1}">
<td><a href="
<c:url value="process" >
<c:param name="cmd" value="abort" />
<c:param name="processInstanceId" value="${processInstance.id}" />
</c:url>
">Abort</a>
</td>
</c:if>
</tr>
</c:forEach>
</table>
</body>
</html>