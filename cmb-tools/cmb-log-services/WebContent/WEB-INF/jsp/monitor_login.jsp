<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url value="/monitor" var="monitorURL"/>
		
<jsp:include page="common_login.jsp">
    <jsp:param name="title" value="Log Monitoring"/>
    <jsp:param name="header" value="LOG MONITORING"/>
    <jsp:param name="link" value="${monitorURL}" />
    <jsp:param name="loginHeader" value="Enter login credentials for log monitoring" />    
    <jsp:param name="submitLink" value="${monitorURL}"/>
</jsp:include>