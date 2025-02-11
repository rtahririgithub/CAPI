<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="artifactsVersionMonitor">

	<link href="resources/theme/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
	<link href="resources/theme/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>
	<link href="resources/qtip/jquery.qtip.min.css" rel="stylesheet" type="text/css"/>

	<h3 class="page-title">Artifact Runtime Information</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="environmentRuntimes"/>
	</div>
				
	<t:status-message status="${status}"/>

	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title tabbable-line">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">${runtime.artifact.name} Details - ${environment.name}</span>
					</div>
					<div class="actions">
						<a href="runtime/environment?environmentId=${param.environmentId}" class="btn btn-success "><i class="fa  fa-angle-left"></i> Back to Environment Information</a>
					</div>
				</div>
				<div class="portlet-body form">
					<div style="padding-top: 10px;">Reference Version:&nbsp;&nbsp;&nbsp;
						<c:choose>
							<c:when test="${empty runtime.referenceVersion.version}">
								<a class="btn red-pink btn-sm" data-target="#referenceModal" data-toggle="modal" href="#">Not Defined</a>
							</c:when>
							<c:otherwise>
								<a class="btn btn-sm btn-default" data-target="#referenceModal" data-toggle="modal" href="#">${runtime.referenceVersion.version}</a>
								<c:if test="${not empty runtime.referenceVersion.notes}">
									&nbsp;&nbsp;&nbsp;${runtime.referenceVersion.notes}
								</c:if>
							</c:otherwise>
						</c:choose>
					</div>
					<hr>
					<table class="table table-bordered" id="instancesTable">
						<thead>
							<tr>
								<th>&nbsp</th>
								<th class="details-holder"></th>
								<th>Domain</th>
								<th class="centered">Node</th>
								<th class="centered">Version</th>
								<th class="centered">Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${runtime.instances}" var="instance">
							<tr>
								<td class="details-control" align="center">
									<span class="icon-magnifier-add"></span>
								</td>
								<td class="details-holder">
								<div class="instance-details">
									<div class="col-md-12">
									<table>
										<tr>
											<td class="label-cell">Domain Name:</td><td class="data-cell">${instance.domainName}</td>
											<td class="label-cell">Cluster Name:</td><td>${instance.clusterName}</td>
										</tr>
										<tr>
											<td class="label-cell">Node Name:</td><td class="data-cell">${instance.nodeName}</td>
											<td class="label-cell">Host Name:</td><td>${instance.hostName}</td>
										</tr>
										<tr>
											<td class="label-cell">Port Number:</td><td class="data-cell">${instance.portNumber}</td>
											<td class="label-cell">Admin Console:</td><td><a target="_blank" href="http://${instance.adminHostName}:${instance.adminPortNumber}/console">http://${instance.adminHostName}:${instance.adminPortNumber}/console</a></td>
										</tr>
										<tr>
											<td class="label-cell">Last Notificaton:</td><td>${instance.notificationTime}</td>
											<td colspan="2"><a href="logs/artifact?artifactCode=${runtime.artifact.code}&instanceId=${instance.instanceId}&environmentId=${param.environmentId}">View Logs & Statistics</a></td>
										</tr>
									</table>
									</div>
								</div>
								</td>
								<td>${instance.domainName}</td>
								<td align="center">${instance.nodeName}</td>
								<td align="center">${instance.version}</td>
								<td align="center">
									<c:choose>
										<c:when test="${instance.status == 'OK'}">
											<a href="javascript:;" class="popovers" data-trigger="hover" data-content="Correct Version" data-placement="left">
												<i class="icon-check font-green" style="font-size: 18px;"></i>
											</a>
										</c:when>
										<c:when test="${instance.status == 'INCORRECT_VERSION'}">
											<a href="javascript:;" class="popovers" data-trigger="hover" data-content="Incorrect Version" data-placement="left">
												<i class="icon-ban font-red" style="font-size: 18px;"></i>
											</a>
										</c:when>
										<c:when test="${instance.status == 'UNKNOWN_REFERENCE'}">
											<a href="javascript:;" class="popovers" data-trigger="hover" data-content="Unknown Reference" data-placement="left">
												<i class="icon-question font-purple" style="font-size: 18px;"></i>
											</a>
										</c:when>
									</c:choose>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				
				</div>
			</div>
		</div>
	</div>
	
	<div id="referenceModal" class="modal fade" tabindex="-1" data-focus-on="input:first">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
			<h4 class="modal-title">Reference Version Update</h4>
		</div>
		<div class="modal-body">
			<form id="referenceForm" action="runtime/artifact/update-reference-version">
				<input type="hidden" name="environmentId" value="${environment.environmentId}"/>
				<input type="hidden" name="artifactCode" value="${runtime.artifactCode}"/>
				<div class="form-group">
					<label>Version:</label>
					<input class="form-control" type="text" name="version" data-tabindex="1" value="${runtime.referenceVersion.version}">
				</div>
				<div class="form-group">
					<label>Notes:</label>
					<input class="form-control" type="text" name="notes" data-tabindex="1" value="${runtime.referenceVersion.notes}">
				</div>
			</form>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>
			<button type="submit" id="referenceSubmitBtn" class="btn btn-primary">Update</button>
		</div>
	</div>
		
</t:page>

<script src="resources/theme/assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>


<script type="text/javascript">


jQuery(document).ready(function() {

	$('#referenceSubmitBtn').click(function(event){
	       $('#referenceForm').submit();
		});	

    $.fn.modalmanager.defaults.resize = true;
	
    var instancesTable = $('#instancesTable').dataTable( {
        "order": [[1, 'asc']]
    } );

    $('#instancesTable .details-control').click(instancesTable, tableDetailsEventHandler);    
});

function tableDetailsEventHandler(event) {
	event.preventDefault();
	var content = $(this).next().html();
	var table = event.data;
	var row = $(this).parents('tr')[0];
	if (table.fnIsOpen(row)) {
		table.fnClose(row);
		$(this).html('<span class="icon-magnifier-add"></span>');
	} else {
		table.fnOpen(row, content, 'instance-details-row');
		$(this).html('<span class="icon-magnifier-remove"></span>');
	}
}

function format(d) {
    // `d` is the original data object for the row
    return '<div>xxx</div>';
}

</script>

