<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/css/results.css" var="resultsCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/js/results.js" var="resultsJS" />
<spring:url value="/resources/js/jquery.highlight-5.js" var="highlightJS" />	
<spring:url value="/resources/js/tablesort.js" var="tablesortJS" />		
<c:url value="/read" var="readURL"/>		
		
<!DOCTYPE html>
<html>
	<head>
		<title>Log File Search Results</title>
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>
		<link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700|Open+Sans:300italic,400,300,700'>		
		<link href="${semanticCSS}" rel="stylesheet" />
		<link href="${resultsCSS}" rel="stylesheet" />			
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>		
		<script src="${tablesortJS}"></script>
		<script src="${highlightJS}"></script>
		<script src="${resultsJS}"></script>
	</head>
	<body>
		<div class='title'>
			<div class='ui huge header'> EJB Usage Results that match "<i>${methodText}</i>"</div>
		</div>
		<div class='content ui segments'>		
		<c:choose>
			<c:when test="${empty usageResultList}">
				<div class="ui segment">
					<div class="ui massive warning message">
						<div class="header">SORRY, NO RESULTS</div><p>There were no results obtained from the server.</p>
					</div>
				</div>
			</c:when>    
			<c:otherwise>
				<c:forEach items="${usageResultList}" var="usageResult">
					<div class='ui vertical raised segment'>
						<div class="file header" data-content="Test">${usageResult.operationShortname}</div><br/>
						<div class="line item">
							<div class="content">
								<i class="sitemap icon"></i> <b>Package: ${usageResult.operationPackage}</b><br/>
								<i class="code icon"></i> <b><i>Params: ${usageResult.operationParameters}</i></b><br/>
							</div>
						</div><br/>
						<div class="ui three cards">
							<c:forEach items="${usageResult.monthlyUsageList}" var="monthlyUsage">					
								<div class="ui card">
									<div class="content">
										<div class="header">${monthlyUsage.description}</div>
										<div class="meta">Average volume per day: ${monthlyUsage.averageUsage}</div>
										<div class="description">
											<table class="ui sortable fluid table">
												<thead>
													<tr>
														<th class="ten wide">Date</th>
														<th class="three wide right aligned">Volume</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${monthlyUsage.usageList}" var="usage">
														<tr>
															<td>${usage.dateDisplay}</td>
															<td class="right aligned">${usage.volume}</td>
														</tr>
													</c:forEach>
												</tbody>
												<tfoot>
													<tr>
														<th>Total</th>
														<th class="right aligned">${monthlyUsage.totalUsage}</th>
													</tr>
												</tfoot>
											</table>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</div>		
		<script>$('table').tablesort();	</script>
	</body>
</html>
