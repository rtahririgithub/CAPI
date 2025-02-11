<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url value="/read" var="readURL"/>
		
<jsp:include page="common_login.jsp">
    <jsp:param name="title" value="Log File Reader Tool"/>
    <jsp:param name="header" value="LOG FILE READER TOOL"/>
    <jsp:param name="link" value="${readURL}"/>
    <jsp:param name="loginHeader" value="Log into <a href='${readURL}'>${logServer.name}</a>"/>
    <jsp:param name="submitLink" value="${readURL}/${logServer.shortname}"/>
</jsp:include>