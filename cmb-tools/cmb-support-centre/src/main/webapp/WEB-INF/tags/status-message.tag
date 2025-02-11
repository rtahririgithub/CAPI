<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="status" required="true" rtexprvalue="true" type="com.telus.cmsc.web.model.ResponseStatus"%>

<c:if test="${not empty status.messages}">
	<div class="alert alert-success alert-dismissable">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
	<c:forEach items="${status.messages}" var="message">
		<span class="status-message">${message}</span>
	</c:forEach>
	</div>
</c:if>

<c:if test="${not empty status.errors}">
	<div class="alert alert-danger alert-dismissable">
	<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
	<strong>Error: </strong>
	<c:forEach items="${status.errors}" var="error">
		<span class="status-error">${error}</span>
	</c:forEach>
	<br/>
	</div>
</c:if>
