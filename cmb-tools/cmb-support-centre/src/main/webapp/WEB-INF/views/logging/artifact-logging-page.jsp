<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:page activeActionId="dashboard">

	<h3 class="page-title">Artifact Logs & Performance</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId=""/>
	</div>
				
	<t:status-message status="${status}"/>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-target font-red-sunglo"></i>
						<span class="caption-subject bold font-red-sunglo uppercase">${artifact.name} - Logs</span>
					</div>
					<div class="actions">
						<a href="runtime/artifact/details?artifactCode=${artifact.code}&environmentId=${environment.environmentId}" class="btn btn-success "><i class="fa  fa-angle-left"></i> Back to Runtime Information</a>
					</div>
				</div>
				<div class="portlet-body">
				<div class="row">
					<div class="col-md-12">
					<div class="panel panel-default">
						<div class="panel-heading">Instance Summary</div>
						<div class="panel-body">
							<table class="instance-details">
								<tr>
									<td class="label-cell">Artifact Name:</td><td class="data-cell">${artifact.name}</td>
									<td class="label-cell">Host Name:</td><td class="data-cell">${artifactInstance.hostName}</td>
								</tr>
								<tr>
									<td class="label-cell">Environment Name:</td><td class="data-cell">${environment.name}</td>
									<td class="label-cell">Node Name:</td><td class="data-cell">${artifactInstance.nodeName}</td>
								</tr>
							</table>
						</div>
					</div>
					
					</div>				
				</div>
					<table class="table table-striped table-bordered table-hover" id="logFilesTable">
					<thead>
					<tr>
						<th>File Name</th>
						<th class="centered">Size</th>
						<th class="centered">Timestamp</th>
					</tr>
					</thead>
					<tbody>
					<jsp:useBean id="dateValue" class="java.util.Date"/>
					<c:forEach items="${logFiles}" var="logFile">
					<jsp:setProperty name="dateValue" property="time" value="${logFile.lastModified()}"/>
					<tr>
						<td><a href="logs/file?environmentId=${environment.environmentId}&instanceId=${artifactInstance.instanceId}&artifactCode=${artifact.code}&fileName=${logFile.name}">${logFile.name}</a></td>
						<td align="center"><fmt:formatNumber value="${logFile.length() / 1024}" maxFractionDigits="0"/>&nbsp;KB</td>
						<td align="center"><fmt:formatDate value="${dateValue}" pattern="MMM dd, yyyy HH:mm"/> </td>
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
	
	$('#logFilesTable').dataTable();
	
	var tableWrapper = jQuery('#logFilesTable_wrapper');
	tableWrapper.find('.dataTables_length select').select2(); // initialize select2 dropdown
});


</script>

