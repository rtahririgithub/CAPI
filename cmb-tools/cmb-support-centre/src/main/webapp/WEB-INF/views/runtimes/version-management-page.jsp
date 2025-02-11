<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="referenceVersionManagement">

	<h3 class="page-title"><spring:message code="page.title.referenceVersionMgmt"/>  <small><spring:message code="page.title.referenceVersionMgmt.description"/></small></h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="referenceVersionManagement"/>
	</div>
				
	<t:status-message status="${status}"/>
				
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">Artifact Reference Versions</span>
					</div>
					<div class="actions">
						<button id="saveButton" class="btn green"><i class="fa fa-check"></i> Update </a>
						<button id="resetButton" class="btn yellow"> Reset Changes</a>
					</div>
				</div>
				<div class="portlet-body">
				<form:form commandName="referenceVersionForm" action="versions/manage">
					<div class="form-body">
						<div class="form-group">
							<form:select path="artifactId" cssClass="form-control">
								<form:option value="0" label="-- Please Select --"/>
								<form:options items="${referenceVersionForm.artifacts}" itemLabel="name" itemValue="artifactId"/>
							</form:select>	
						</div>
						<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th class="col-sm-1">Environment</th>
								<th class="col-sm-4">Reference Version</th>
								<th class="col-sm-7">Notes</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${referenceVersionForm.entries}" var="entry" varStatus="status">
						<tr>
							<td style="vertical-align: middle; text-align: center;" nowrap="nowrap">${entry.environment.name}</td>
							<td><form:input path="entries[${status.index}].version" cssClass="form-control"/></td>
							<td><form:input path="entries[${status.index}].notes" cssClass="form-control"/></td>
						</tr>
						</c:forEach>
						<c:if test="${empty referenceVersionForm.entries}">
							<tr>
								<td colspan="3">Please select artifact to see reference versions</td>
							</tr>
						</c:if>
						</tbody>
						</table>
					</div>
				</form:form>
				</div>
			</div>
		</div>
	</div>
		
</t:page>

<script type="text/javascript">

jQuery(document).ready(function() {
	
	$('#artifactId').select2();
	
	$('#artifactId').change(function(){
		$('#referenceVersionForm').submit();
	});

	$('#saveButton').click(function(event){
		$('#referenceVersionForm').attr('action', 'versions/save');
        $('#referenceVersionForm').submit();
	});
	
	$('#resetButton').click(function(event){
		$('#referenceVersionForm').trigger('reset');
	});	
	
});

</script>

