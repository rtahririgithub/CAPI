<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url value="/search" var="searchURL"/>
		
<jsp:include page="common_login.jsp">
    <jsp:param name="title" value="Log File Search Tool"/>
    <jsp:param name="header" value="LOG FILE SEARCH TOOL"/>
    <jsp:param name="link" value="${searchURL}" />
    <jsp:param name="loginHeader" value="Log into <a href='${searchURL}'>${environment.name}</a> for <a href='${searchURL}/${environment.shortname}'>${application.name}</a>" />    
    <jsp:param name="submitLink" value="${searchURL}/${environment.shortname}/${application.shortname}"/>
</jsp:include>