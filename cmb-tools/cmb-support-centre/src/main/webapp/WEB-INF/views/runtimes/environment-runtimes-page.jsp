<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
	var pageData = [];
	
</script>
<t:page activeActionId="artifactsVersionMonitor">

	<h3 class="page-title">Environment Runtime Information Explorer</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="environmentRuntimes"/>
	</div>
				
	<t:status-message status="${status}"/>
	
					
					<div class="row">
					<c:forEach items="${environmentRuntimes}" var="runtime" varStatus="status">
						<c:set var="summary" value="${runtime.artifactStatusSummary}"/>
						<div class="col-md-4">
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption">
										<i class="fa fa-cube font-blue-hoki"></i>
										<span class="caption-subject bold font-blue-hoki uppercase">${runtime.environment.name}</span><br/>
									</div>
									<div class="actions">
										<a href="runtime/environment?environmentId=${runtime.environment.id}" class="btn btn-success ">Details</a>
									</div>
								</div>
								
								<div class="portlet-body">
									<script>
										var portletData = [${summary.correctCount}, ${summary.incorrectCount}, ${summary.unresponsiveCount}, ${summary.unknownCount}, ${summary.unknownReferenceCount}];
										pageData.push(portletData);
									</script>
									<div id="chartdiv_${status.index}" class="chart">
									</div>
								</div>
							</div>
							
						</div>
					</c:forEach>
					</div>
					
		
</t:page>

<script src="resources/theme/assets/global/plugins/amcharts/amcharts/amcharts.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/amcharts/amcharts/pie.js" type="text/javascript"></script>
<script src="resources/theme/assets/global/plugins/amcharts/amcharts/themes/light.js" type="text/javascript"></script>


<script type="text/javascript">

jQuery(document).ready(function() {
	
	for (idx = 0; idx < pageData.length; idx++) {
		initChart(idx, pageData[idx][0], pageData[idx][1], pageData[idx][2], pageData[idx][3], pageData[idx][4]);
	}
	
});

function initChart(idx, correct, incorrect, unresponsive, unknown, unknownReference) {
	var chart = AmCharts.makeChart( "chartdiv_" + idx, {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "Correct Version",
		    "visits": correct
		  }, {
		    "country": "Invalid Versions",
		    "visits": incorrect
		  }, {
			    "country": "Unresponsive",
			    "visits": unresponsive
		  }, {
			    "country": "Unknown",
			    "visits": unknown
		  }, {
		    "country": "Unknown Reference",
		    "visits": unknownReference
		  }],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 2,
		  "labelsEnabled": false,
		  "innerRadius": "30%",
		  "depth3D": 10,
		  "marginBottom" : 0,
		  
		  "balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 20

	});
}

</script>

