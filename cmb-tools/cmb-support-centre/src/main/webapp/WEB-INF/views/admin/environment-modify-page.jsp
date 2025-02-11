<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="updateMode" value="${not empty environment.id}"/>

<t:page activeActionId="environmentManagement">

	<h3 class="page-title"><spring:message code="page.title.environmentMgmt"/>  <small><spring:message code="page.title.environmentMgmt.description"/></small></h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="environmentManagement"/>
	</div>
				
	<t:status-message status="${status}"/>
	<t:binding-errors name="environment"/>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-cube font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">
						<c:choose>
							<c:when test="${updateMode}"><spring:message code="portlet.title.environment.modify"/></c:when>
							<c:otherwise><spring:message code="portlet.title.environment.create"/></c:otherwise>
						</c:choose>
						</span>
						<span class="caption-helper">create a new environment definition</span>
					</div>
					<div class="actions">
						<a href="admin/environments/manage" class="btn default"><i class="fa  fa-angle-left"></i> Cancel</a>
						<c:choose>
							<c:when test="${updateMode}">
								<button id="deleteButton" class="btn red"><i class="fa fa-trash-o"></i> Delete </a>
								<button id="createButton" class="btn green"><i class="fa fa-check"></i> Update </a>
							</c:when>
							<c:otherwise>
								<button id="createButton" class="btn green"><i class="fa fa-check"></i> Create </a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="portlet-body form">
					<form:form role="form" commandName="environment" action="admin/environments/save">
						<form:hidden path="environmentId"/>
						<div class="form-body">
							
							<spring:bind path="name">
							<div class="form-group ${status.error ? 'has-error':''}">
								<label class="control-label">Environment Name:<span class="required"> * </span></label>
								<form:input path="name" type="text" cssClass="form-control" placeholder="Environment Name"/>
							</div>
							</spring:bind>
							
							<div class="row">
								<div class="col-md-6">
									<spring:bind path="code">
									<div class="form-group ${status.error ? 'has-error':''}">
										<label class="control-label">Notification Code:<span class="required"> * </span></label>
										<form:input path="code" type="text" cssClass="form-control" placeholder="Environment Code"/>
									</div>
									</spring:bind>
								</div>
								<div class="col-md-6">
									<spring:bind path="configCode">
									<div class="form-group ${status.error ? 'has-error':''}">
										<label class="control-label">Configuration Code:<span class="required"> * </span></label>
										<form:input path="configCode" type="text" cssClass="form-control" placeholder="Environment Code"/>
									</div>
									</spring:bind>
								</div>
							</div>

							<div class="form-group">
								<form:checkbox path="flipperMember" cssClass="form-control" label="Member of Flipper infrastructure"/>
							</div>
							
							<spring:bind path="ldapUrl">
							<div class="form-group ${status.error ? 'has-error':''}">
								<label class="control-label">Configuration LDAP URL:<span class="required"> * </span></label>
								<form:input path="ldapUrl" type="text" cssClass="form-control" placeholder="LDAP URL"/>
							</div>
							</spring:bind>
							<div class="form-group">
								<label>Comments:</label>
								<form:input path="description" type="text" cssClass="form-control" placeholder="Description"/>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
		
</t:page>

<script type="text/javascript">

jQuery(document).ready(function() {
	
	$('#createButton').click(function(event){
	       $('#environment').submit();
	});	

	$('#deleteButton').click(function(event){
        bootbox.confirm("Are you sure you want to delete the environment?", function(result) {
            if (result) {
            	$('#environment').attr('action', 'admin/environments/delete');
            	$('#environment').submit();
            }
         });
        return false;
	});
});

</script>

