<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<t:main-layout>

<script type="text/javascript">

$(document).ready(function(){

	$('#encryptButton').click(function(){
		$('#identityForm').attr('action', 'app-identity-mgmt/encrypt');
		$('#identityForm').submit();
	});
	
	$('#decryptButton').click(function(){
		$('#identityForm').attr('actioPlease help yourself with some cookies n', 'app-identity-mgmt/decrypt');
		$('#identityForm').submit();
	});
	
});




</script>

	<div class="ui grid">
		<div class="column">
			<h3 class="ui header">
				<i class="circular unlock alternate icon"></i>
				<div class="content">Application Identity Management</div>
			</h3>
			<div class="ui yellow message">Using tabs below select encryption or decryption direction and press the action button.</div>
			<form:form id="identityForm" cssClass="ui form" commandName="identityMgmtForm" action="" method="post">
				<div class="ui segment">
					<spring:hasBindErrors name="identityMgmtForm">
						<spring:bind path="identityMgmtForm">
						<div class="ui negative message">
							<div class="header">Error processing request. Please correct the error(s) below and try again:</div>
							<ul class="list">
							<c:forEach items="${status.errorMessages}" var="error">
								<li>${error}</li>
							</c:forEach>
							</ul>
						</div>
						</spring:bind>
					</spring:hasBindErrors>
					<spring:bind path="sourceText">
					<div class="field ${status.error ? 'error' : ''}">
						<label>Source Content: </label>
						<form:textarea path="sourceText" />
					</div>
					</spring:bind>
					<spring:bind path="resultText">
					<div class="field ${status.error ? 'error' : ''}">
						<label>Result Content:</label>
						<form:textarea path="resultText"/>
					</div>
					</spring:bind>
					<div class="ui teal button">Copy Result To Clipboard</div>
					<div id="decryptButton" class="ui right floated blue button">Decrypt Source</div>
					<div id="encryptButton" class="ui right floated blue button">Encrypt Source</div>
				</div>

			</form:form>
		</div>
	</div>


</t:main-layout>
