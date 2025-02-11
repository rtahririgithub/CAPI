<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/css/results.css" var="resultsCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/js/results.js" var="resultsJS" />
<spring:url value="/resources/js/jquery.highlight-5.js" var="highlightJS" />	
		
<!DOCTYPE html>
<html>
	<head>
		<title>Log File Paths</title>
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>
		<link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700|Open+Sans:300italic,400,300,700'>		
		<link href="${semanticCSS}" rel="stylesheet" />
		<link href="${resultsCSS}" rel="stylesheet" />			
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
		<script src="${highlightJS}"></script>
		<script src="${resultsJS}"></script>
	</head>
	<body>
		<div class='title'>
			<div class='ui huge header'> Logfile Paths for ${component} (${application.name}) <br/>in ${environment.name}</div>
		</div>
		<div class='content ui segments'>		
		<c:choose>
			<c:when test="${empty filepaths}">
				<div class="ui segment">
					<div class="ui massive warning message">
						<div class="header">No logfile paths found.</div>
					</div>
				</div>
			</c:when>    
			<c:otherwise>				
				<c:forEach items="${filepaths}" var="filepath">		
					<div class='ui vertical raised segment'>
						<div class='file header'>${filepath}</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</div>		
	</body>
</html>
