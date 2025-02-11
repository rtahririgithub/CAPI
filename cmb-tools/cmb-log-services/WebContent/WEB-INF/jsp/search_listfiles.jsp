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
		<title>LogFile List</title>
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
			<div class='ui huge header'>All files in ${component} (${application.name}) <br/>in ${environment.name}</div>
		</div>
		<div class='content ui segments'>		
		<c:choose>
			<c:when test="${empty listresults}">
				<div class="ui segment">
					<div class="ui massive warning message">
						<div class="header">No files found.</div>
					</div>
				</div>
			</c:when>    
			<c:otherwise>				
				<c:forEach items="${listresults}" var="listresult">
					<div class='ui vertical raised segment'>
						<div class='file header'>${listresult.folder}</div>
						<div class='ui list' style='padding-left: 3em;'>
							<c:forEach items="${listresult.fileInfoList}" var="fileinfo">
								<div class='line item'>
									<div class='content'>
										<form id="form_${listresult.folder}_${fileinfo.fileName}" method="post" action="${readURL}/${listresult.logServer.shortname}/download">
											<input type="hidden" name="file" value="${listresult.folder}/${fileinfo.fileName}" /> 
											<a class="filelink" onclick="document.getElementById('form_${listresult.folder}_${fileinfo.fileName}').submit();">${fileinfo.fileName}</a>
											(<i>Size: ${fileinfo.fileSize}, Last Modified: ${fileinfo.lastModifiedTime}</i>)
										</form>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</div>		
	</body>
</html>
