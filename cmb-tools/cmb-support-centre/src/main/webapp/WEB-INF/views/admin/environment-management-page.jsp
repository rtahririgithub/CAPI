<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<t:page activeActionId="environmentManagement">

	<h3 class="page-title"><spring:message code="page.title.environmentMgmt"/>  <small><spring:message code="page.title.environmentMgmt.description"/></small></h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="environmentManagement"/>
	</div>
				
	<t:status-message status="${status}"/>
				
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">Environment Definitions</span>
					</div>
					<div class="actions">
						<a href="admin/environments/create" class="btn btn-success ">New Environment</a>
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
					<c:forEach items="${environments}" var="environment">
					<tr>
						<td><a href="admin/environments/modify?environmentId=${environment.id}">${environment.name}</a></td>
						<td>${environment.code}</td>
						<td>${environment.description}</td>
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
});

</script>

