<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url value="/usage" var="usageURL"/>
		
<jsp:include page="common_login.jsp">
    <jsp:param name="title" value="CAPI EJB Usage"/>
    <jsp:param name="header" value="CAPI EJB USAGE"/>
    <jsp:param name="link" value="${usageURL}" />
    <jsp:param name="loginHeader" value="Enter login credentials for CAPI EJB usage" />    
    <jsp:param name="submitLink" value="${usageURL}"/>
</jsp:include>