<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="updateMode" value="${not empty artifact.id}"/>

<t:page activeActionId="artifactManagement">

	<h3 class="page-title"><spring:message code="page.title.artifactMgmt"/></h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="artifactManagement"/>
	</div>
				
	<t:status-message status="${status}"/>
	<t:binding-errors name="artifact"/>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-cube font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">
						<c:choose>
							<c:when test="${updateMode}"><spring:message code="portlet.title.artifact.modify"/></c:when>
							<c:otherwise><spring:message code="portlet.title.artifact.create"/></c:otherwise>
						</c:choose>
						</span>
						<span class="caption-helper">create a new artifact definition</span>
					</div>
					<div class="actions">
						<a href="admin/artifacts/manage" class="btn default"><i class="fa  fa-angle-left"></i> Cancel</a>
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
				
				<form:form role="form" commandName="artifact" action="admin/artifacts/save" cssClass="horizontal-form">
					<form:hidden path="artifactId"/>
					<div class="form-body">
						<spring:bind path="name">
						<div class="form-group ${status.error ? 'has-error':''}">
							<label class="control-label">Artifact Name:<span class="required"> * </span></label>
							<form:input path="name" type="text" cssClass="form-control" placeholder="Name"/>
						</div>
						</spring:bind>
						<spring:bind path="code">
						<div class="form-group ${status.error ? 'has-error':''}">
							<label class="control-label">Artifact Identification Code:<span class="required"> * </span></label>
							<form:input path="code" type="text" cssClass="form-control" placeholder="Code"/>
						</div>
						</spring:bind>
						<spring:bind path="logPathPattern">
						<div class="form-group ${status.error ? 'has-error':''}">
							<label class="control-label">Logging Path Pattern:<span class="required"> * </span></label>
							<form:input path="logPathPattern" type="text" cssClass="form-control" placeholder="Code"/>
						</div>
						</spring:bind>
						<div class="form-group">
							<label>Description:</label>
							<form:input path="description" type="text" cssClass="form-control" placeholder="Description"/>
						</div>
						<div class="form-group">
							<label>Assigned Groups:</label>
							<table class="table table-bordered table-striped">
							<thead>
								<tr>
									<th></th>
									<th>Name</th>
									<th>Description</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${groups}" var="group">
								<tr>
									<td width="20px;"><input type="checkbox" name="groupId" value="${group.id}" <c:if test="${artifactGroups.contains(group)}">checked="checked"</c:if> ></td>
									<td>${group.name}</td>
									<td>${group.description}</td>
								</tr>
								</c:forEach>
							</tbody>
							</table>
						</div>
						<div class="row">
							<div class="col-md-12" style="text-align: right;">
								<a href="versions/manage?artifactId=${artifact.id}" class="btn btn-sm green">Manage Reference Versions</a>
							</div>
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
       $('#artifact').submit();
	});	
	
	$('#deleteButton').click(function(event){
        bootbox.confirm("Are you sure you want to delete the artifact?", function(result) {
            if (result) {
            	$('#artifact').attr('action', 'admin/artifacts/delete');
            	$('#artifact').submit();
            }
         });
        return false;
	});
});

</script>

