<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="artifactsVersionMonitor">

	<h3 class="page-title">Environment Runtime Information</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="environmentRuntimes"/>
	</div>
				
	<c:set var="statusSummary" value="${environmentRuntime.artifactStatusSummary}"/>
					
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title tabbable-line">
					<div class="caption">
						<i class="icon-target font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">${environmentRuntime.environment.name} Environment Runtime Details </span>
					</div>
					<div class="actions">
						<div class="btn-group">
							<a class="btn btn-sm btn-default btn-circle" href="javascript:;" data-toggle="dropdown"> View Options&nbsp;&nbsp;<i class="fa fa-angle-down"></i></a>
								<div class="dropdown-menu dropdown-checkboxes pull-right" style="width: 200px;">
								</div>
						</div>					
					</div>
					<!-- 
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="runtime/environment/artgroups?environmentId=${param.environmentId}" aria-expanded="true">Registered Artifacts</a>
						</li>
						<li>
							<a href="runtime/environment/artunreg?environmentId=${param.environmentId}" aria-expanded="true">Unregistered Artifacts&nbsp;&nbsp;<span class="badge bg-blue">${statusSummary.unknownCount}</span></a>
						</li>
					</ul>
					 -->
				</div>
				
				<div class="portlet-body">
					<div class="row">
					
						<div class="col-md-3">
							<a class="dashboard-stat dashboard-stat-light green-soft" href="javascript:;">
								<div class="visual">
									<i class="fa fa-check-circle-o"></i>
								</div>
								<div class="details">
									<div class="number"> ${statusSummary.correctCount} </div>
									<div class="desc"> Correct Version Artifacts </div>
								</div>								
							</a>
						</div>
						<div class="col-md-3">
							<a class="dashboard-stat dashboard-stat-light red-soft" href="javascript:;">
								<div class="visual">
									<i class="fa fa-ban"></i>
								</div>
								<div class="details">
									<div class="number"> ${statusSummary.incorrectCount} </div>
									<div class="desc"> Incorrect Version Artifacts </div>
								</div>								
							</a>
						</div>
						<div class="col-md-3">
							<a class="dashboard-stat dashboard-stat-light purple-soft" href="javascript:;">
								<div class="visual">
									<i class="fa fa-clock-o"></i>
								</div>
								<div class="details">
									<div class="number"> ${statusSummary.unresponsiveCount} </div>
									<div class="desc"> Unresponsive Artifacts </div>
								</div>								
							</a>
						</div>
						<div class="col-md-3">
							<a class="dashboard-stat dashboard-stat-light blue-soft" href="javascript:;">
								<div class="visual">
									<i class="fa fa-exclamation-circle"></i>
								</div>
								<div class="details">
									<div class="number"> ${statusSummary.unknownReferenceCount} </div>
									<div class="desc"> Unknown Reference </div>
								</div>								
							</a>
						</div>
					</div>
						<div class="row">
							<div class="col-md-12"><br/>
								<p>There are <span class="badge bg-red">${statusSummary.unknownCount}</span> unregistered artifact instances in the environment.&nbsp;<a href="runtime/environment/unregistered-artifacts?environmentId=${param.environmentId}">Manage...</a></p>
							</div>
						</div>
				</div>
			</div>
		</div>
	</div>
					
	<c:forEach items="${runtimeGroups}" var="runtimeGroup">
	<div class="portlet light bordered">
		<div class="portlet-title">
			<div class="caption font-green-sharp">
				<span class="caption-subject bold uppercase">${runtimeGroup.group.name} Group</span>
				<span class="caption-helper">${runtimeGroup.group.description}</span>
			</div>
			<div class="tools">
				<a href="javascript:;" class="fullscreen" data-original-title="" title=""></a>&nbsp;
				<a href="" class="collapse" data-original-title="" title=""></a>
			</div>
		</div>
		<div class="portlet-body">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>Artifact Name</th>
						<th style="text-align: center;">Instances</th>
						<th style="text-align: center;">Reference Version</th>
						<th style="text-align: center;">Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${runtimeGroup.runtimes}" var="artifactRuntime">
					<c:set var="status" value="${artifactRuntime.statusSummary}"/>
					<tr>
						<td>
							<a href="runtime/artifact/details?artifactCode=${artifactRuntime.artifact.code}&environmentId=${param.environmentId}">${artifactRuntime.artifact.name}</a>
						</td>
						<td align="center">${artifactRuntime.instances.size()}</td>
						<td align="center">${artifactRuntime.referenceVersion.version}</td>
						<td align="center">
							<c:if test="${status.correctCount != 0}">
								<span class="badge bg-green">${status.correctCount}</span>
							</c:if>
							<c:if test="${status.incorrectCount != 0}">
								<span class="badge bg-red-intense">${status.incorrectCount}</span>
							</c:if>
							<c:if test="${status.unknownReferenceCount != 0}">
								<span class="badge bg-blue-hoki">${status.unknownReferenceCount}</span>
							</c:if>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	</c:forEach>
		
</t:page>

<script src="resources/theme/assets/global/plugins/amcharts/amcharts/amcharts.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/amcharts/amcharts/pie.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/amcharts/amcharts/themes/light.js" type="text/javascript"></script>


<script type="text/javascript">

jQuery(document).ready(function() {
	$('.table').dataTable();
});


</script>

