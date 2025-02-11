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
				
	<t:status-message status="${status}"/>
	
					
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title tabbable-line">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">${environmentRuntime.environment.name} Artifacts Runtime Details </span>
					</div>
					<ul class="nav nav-tabs">
						<li class="active">
							<a href="runtime/environment/artgroups?environmentId=${param.environmentId}" aria-expanded="true">Artifact Groups</a>
						</li>
						<li>
							<a href="runtime/environment/artunreg?environmentId=${param.environmentId}" aria-expanded="true">Unregistered Artifacts</a>
						</li>
					</ul>
				</div>
				<div class="portlet-body">

				<c:forEach items="${runtimeGroups}" var="runtimeGroup">
				<div class="portlet light bordered">
					<div class="portlet-title">
						<div class="caption font-green-sharp">
							<span class="caption-subject bold uppercase">${runtimeGroup.group.name}</span>
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
											<span class="badge bg-grey-cascade">${status.unknownReferenceCount}</span>
										</c:if>
									</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				</c:forEach>
				
				</div>
			</div>
		</div>
	</div>
					
		
</t:page>

<script src="resources/theme/assets/global/plugins/amcharts/amcharts/amcharts.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/amcharts/amcharts/pie.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/amcharts/amcharts/themes/light.js" type="text/javascript"></script>


<script type="text/javascript">

jQuery(document).ready(function() {
	$('.table').dataTable();
});


</script>

