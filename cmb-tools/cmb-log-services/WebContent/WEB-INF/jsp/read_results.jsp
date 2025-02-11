<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
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
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
		<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/1.5.10/clipboard.min.js"></script>
		<script src="${highlightJS}"></script>
	</head>
	<body>		
		<div class='ui secondary segment'>
			<h1>${logfile}</h1>
			<c:choose>
				<c:when test="${errorMessage == null || errorMessage == ''}">			
					<div class='ui purple content segment'>
						<button class="ui right floated small blue clipboard button" data-clipboard-target='#results'><i class='plus icon'></i>Copy to clipboard</button>
						
								<p id='results'>
								<c:forEach items='${lines}' var ='line'>				
									<c:if test='${line.lineNumber == selectedLineNumber}'>
										<span style='background-color: yellow'>
									</c:if>
									(${line.lineNumber})... ${line.lineContent}
									<c:if test='${line.lineNumber == selectedLineNumber}'>
										</span>
									</c:if>
									<br/>
								</c:forEach>
								</p>
							
					</div>
				</c:when>
				<c:otherwise>
					<div class="ui red authentication message" ${errorMessage == null || errorMessage == '' ? 'style="display:none"' : ''}><i class="close icon"></i>${errorMessage}</div>
				</c:otherwise>
			</c:choose>	
			
		</div>
		<script>
			new Clipboard('.clipboard.button');
		</script>
	</body>
</html>
