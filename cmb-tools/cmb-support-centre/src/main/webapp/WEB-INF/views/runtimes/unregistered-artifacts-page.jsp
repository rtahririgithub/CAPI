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
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">${environmentRuntime.environment.name} Unregistered Artifacts </span>
					</div>
					<div class="actions">
						<a href="runtime/environment?environmentId=${environmentRuntime.environment.environmentId}" class="btn green"><i class="fa  fa-angle-left"></i> Back To Environment </a>
					</div>
					
				</div>
				<div class="portlet-body">
					<table class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th>Artifact Name</th>
								<th>Instances</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${unregisteredRuntimes}" var="artifactRuntime">
							<tr>
								<td>${artifactRuntime.artifactCode}</td>
								<td>${artifactRuntime.instances.size()}</td>
								<td align="center"><a href="admin/artifacts/register?artifactCode=${artifactRuntime.artifactCode}" class="btn btn-xs green">Register</a></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
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

