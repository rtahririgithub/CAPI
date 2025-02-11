<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="artifactsVersionMonitor">

	<h3 class="page-title">Artifact Version Explorer</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="artifactsVersionMonitor"/>
	</div>
				
	<t:status-message status="${status}"/>
	
	<div class="row">
		<c:forEach items="${summaries}" var="summary" varStatus="status">
			<div class="col-md-12">
				<div class="portlet light">
					<div class="portlet-title">
						<div class="caption">
							<i class="fa fa-cube font-blue-hoki"></i>
							<span class="caption-subject bold font-blue-hoki uppercase">${summary.group.name}</span><br/>
						</div>
					</div>
					<div class="portlet-body">
					<c:forEach items="${summary.versionsSummaries}" var="artifactSummary">
						<div class="row">
							<div class="col-md-12">
								<div class="portlet box green-seagreen">
									<div class="portlet-title">
										<div class="caption">${artifactSummary.artifact.name}</div>
									</div>
									<div class="portlet-body">
									xxx
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
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

