<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ attribute name="name" required="true" rtexprvalue="false"%>

<spring:hasBindErrors name="${name}" >
	<div class="alert alert-danger alert-dismissable">
		<button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
		<c:if test="${errors.hasFieldErrors()}"><strong>Error: </strong>Please correct the highlighted errors below and try again.</c:if>
		<c:if test="${errors.hasGlobalErrors()}"><strong>Error: </strong>${errors.globalError.defaultMessage}</c:if>
	</div>
</spring:hasBindErrors>
