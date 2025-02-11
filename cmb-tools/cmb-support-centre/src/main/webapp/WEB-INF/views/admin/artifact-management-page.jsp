<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="artifactManagement">

	<h3 class="page-title"><spring:message code="page.title.artifactMgmt"/>  <small><spring:message code="page.title.artifactMgmt.description"/></small></h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="artifactManagement"/>
	</div>
				
	<t:status-message status="${status}"/>
				
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">Artifact Definitions</span>
					</div>
					<div class="actions">
						<a href="admin/artifacts/create" class="btn btn-success ">New Artifact</a>
					</div>
				</div>
				<div class="portlet-body">
					<table class="table table-striped table-bordered table-hover" id="environmentsTable">
					<thead>
					<tr>
						<th>Name</th>
						<th>Code</th>
						<th>Description</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${artifacts}" var="artifact">
					<tr>
						<td><a href="admin/artifacts/modify?artifactId=${artifact.id}">${artifact.name}</a></td>
						<td>${artifact.code}</td>
						<td>${artifact.description}</td>
					</tr>
					</c:forEach>
					</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">Group Definitions</span>
					</div>
					<div class="actions">
						<a href="admin/artifacts/group/create" class="btn btn-success ">New Group</a>
					</div>
				</div>
				<div class="portlet-body">
					<table class="table table-striped table-bordered table-hover" id="groupsTable">
					<thead>
					<tr>
						<th class="table-checkbox">
							<input type="checkbox" class="group-checkable" />
						</th>
						<th>Name</th>
						<th>Description</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${groups}" var="group">
					<tr>
						<td>
							<input type="checkbox" class="checkboxes" value="1"/>
						</td>
						<td><a href="admin/artifacts/group/modify?groupId=${group.id}">${group.name}</a></td>
						<td>${group.description}</td>
					</tr>
					</c:forEach>
					</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
		
</t:page>

<script type="text/javascript">

jQuery(document).ready(function() {
	
	$('#environmentsTable').dataTable();
	
	var tableWrapper = jQuery('#environmentsTable_wrapper');
	tableWrapper.find('.dataTables_length select').select2(); // initialize select2 dropdown
	
	$('#groupsTable').dataTable();
	
	tableWrapper = jQuery('#groupsTable_wrapper');
	tableWrapper.find('.dataTables_length select').select2(); // initialize select2 dropdown
	
});

</script>

