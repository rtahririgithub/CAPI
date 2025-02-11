<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/css/results.css" var="resultsCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/js/results.js" var="resultsJS" />
<spring:url value="/resources/js/jquery.highlight-5.js" var="highlightJS" />		
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
		<script src="${highlightJS}"></script>
		<script src="${resultsJS}"></script>
	</head>
	<body>
		<div class='ui fixed top sticky'>
			<div class='ui compact raised highlighter segment'>
				<div class='ui buttons'>
					<button class='ui compact positive check button' data-method='check'>Highlight All</button>
					<button class='ui compact negative uncheck button' data-method='uncheck'>Highlight None</button>
					<button class='ui minimizing icon button'><i class='minus square icon'></i></button>
				</div>
				<div class='ui horizontal segments'>
					<div class='ui compact raised segment'>
					<c:forEach items="${criteria}" var="criterion" varStatus="stat">
						<div class='ui search${stat.index + 1} toggle checkbox'>
							<input type='checkbox' value='${criterion}'>
							<label>${criterion}</label>
						</div>					
					</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<div class='title'>
			<div class='ui huge header'> Search Results for ${component} (${application.name}) <br/>in ${environment.name} ${dateRange.displayText} [in ${time}ms]</div>
		</div>
		<div class='content ui segments'>		
		<c:choose>
			<c:when test="${empty results}">
				<div class="ui segment">
					<div class="ui massive warning message">
						<div class="header">SORRY, NO RESULTS</div><p>There were no results obtained from the server.</p>
					</div>
				</div>
			</c:when>    
			<c:otherwise>				
				<c:forEach items="${results}" var="logresult">		
					<div class='ui vertical raised segment'>
						<div class='file header'>${logresult.filePath}</div>
						<div class='result url' style='display:none'>${readURL}/${logresult.logServer.shortname}</div>
						<div class='ui list'>
						<c:forEach items="${logresult.results}" var="lineresult">
							<div class='line item'>
								<i class='large search link icon' data-value="${lineresult.lineNumber}"></i>
								<div class='content'>${lineresult.lineNumber}) ${lineresult.lineContent}</div>
							</div>
						</c:forEach>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</div>		
		<div class='content ui segments'>
				<div class='ui vertical raised segment'>
				Searched in the following filepaths:
				<ul>
				<c:forEach items="${filepaths}" var="filepath">
					<li>${filepath}</li>
				</c:forEach>
				</ul>
				</div>
		</div>	
	</body>
</html>
