<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="dashboard">

	<h3 class="page-title">Environments Dashboard</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="dashboard"/>
	</div>
				
	<t:status-message status="${status}"/>
	
	<div class="row">
		<c:forEach items="${runtimes}" var="runtime" varStatus="loopStatus">
		<c:set var="artifactsSummary" value="${runtime.artifactStatusSummary}"/>
		<div class="col-md-6">
			<div class="portlet light bordered">
				<div class="portlet-title tabbable-line">
					<div class="caption">
						<i class="icon-target font-red-sunglo"></i>
						<span class="caption-subject bold font-red-sunglo uppercase">${runtime.environment.name}</span>
					</div>
					<ul class="nav nav-tabs">
						<li class="active"><a href="#artifactsTab_${loopStatus.index}" data-toggle="tab" aria-expanded="true">Artifacts</a></li>
						<li class=""><a href="#tasksTab_${loopStatus.index}" data-toggle="tab" aria-expanded="false">Tasks</a></li>
						<li class=""><a href="#alertsTab_${loopStatus.index}" data-toggle="tab" aria-expanded="false">Alerts</a></li>
					</ul>
				</div>
				<div class="portlet-body" style="height: 297px;">
					<div class="tab-content">
						<div class="tab-pane active" id="artifactsTab_${loopStatus.index}">
							<h4 style="margin-top: 0px; padding-bottom: 5px;">Artifact Versions Summary</h4>
							<ul class="list-group">
								<li class="list-group-item">Number of artifacts with correct version<span class="badge bg-green"> ${artifactsSummary.correctCount} </span>
								</li>
								<li class="list-group-item">Number of artifacts with incorrect version<span class="badge  bg-red-intense"> ${artifactsSummary.incorrectCount} </span>
								</li>
								<li class="list-group-item">Number of artifacts without reference version<span class="badge bg-blue-hoki"> ${artifactsSummary.unknownReferenceCount} </span>
								</li>
								<li class="list-group-item">Number of unresponsive artifacts<span class="badge badge-warning"> ${artifactsSummary.unresponsiveCount} </span>
								</li>
								<li class="list-group-item">Number of unregistered artifacts<span class="badge bg-purple-soft"> ${artifactsSummary.unknownCount} </span>
								</li>
							</ul>	
							<div>
								<a class="btn btn-success btn-sm pull-right" href="runtime/environment?environmentId=${runtime.environment.id}"> View Artifact Details </a>
							</div>		
						</div>
						<div class="tab-pane" id="alertsTab_${loopStatus.index}">
							<h4 style="margin-top: 0px; padding-bottom: 5px;">Alerts & Notifications</h4>
							<div class="panel panel-default" style="height: 199px; text-align: center;"><br/><br/><br/><br/>There are no active alerts or notifications</div>							
						</div>
						<div class="tab-pane" id="tasksTab_${loopStatus.index}">
							<h4 style="margin-top: 0px; padding-bottom: 5px;">Pending Tasks</h4>
							<div class="panel panel-default" style="height: 199px; text-align: center;"><br/><br/><br/><br/>There are no pending tasks</div>							
						</div>
					</div>
				</div>
			</div>
		</div>
		</c:forEach>
	</div>
		
</t:page>

<script type="text/javascript">

jQuery(document).ready(function() {

});


</script>

