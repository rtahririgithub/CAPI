<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
			
<spring:url value="/resources/css/logservicesmain.css" var="logCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/js/logservicesmain.js" var="logJS" />	
<spring:url value="/resources/images/TELUS_EN.jpg" var="telusLogoJPG" />
<spring:url value="/resources/images/logSearch/BunnyLantern.jpg" var="bunnyJPG" />
<spring:url value="/resources/images/logSearch/Lion.jpg" var="lionJPG" />	
<c:url value="/read" var="readURL"/>

<!DOCTYPE html>
<html>
	<head>
		<title>Log File Reader Tool</title>		
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>	
		<link href="${semanticCSS}" rel="stylesheet" />
		<link href="${logCSS}" rel="stylesheet" />	
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
		<script src="${logJS}"></script>
	</head>
	
	<body id='logSearch'>
		<div class='ui hidden divider'></div>

		<div class='ui title header segment'>
			<a href='${readURL}'>
				<img id='telus_logo' class='ui right floated image' src="${telusLogoJPG}" alt='telus logo' height='35px' width='168px' />
			</a>
			<h2 class='body title'>LOG FILE READER TOOL</h2>
		</div>
	
		<div class='ui large attached message'>
			<div class='header'>
				Connected to <a href="${readURL}">${logServer.name}</a>.  
			</div>
		</div>
		
		<form id='readerForm' class='ui form attached raised segment' method='post' action='${readURL}/${logServer.shortname}/results' target="_blank">
			<div class="ui large error message"></div>
			<div class="ui red authentication message" ${errorMessage == null || errorMessage == '' ? 'style="display:none"' : ''}><i class="close icon"></i>${errorMessage}</div>
			<div class="environment name" style="display:none">${environment.shortname}</div>
			<div class="ui grid">
				<div class="row">
					<div class="twelve wide column">						
						<div class="ui segment">
							<a class="ui green left ribbon label">Mandatory</a>
							<div class="hidden divider"></div>	
							<div class="field">
								<label>Enter file path:</label>
								<input type="text" placeholder="/folder/folder/.../filename.txt" name="file" value="${filePath}"/>
							</div>
							<div class="field">
								<label>Enter line number:</label>
								<input type="text" placeholder="Line number" name="lineNumber" value="${lineNumber}"/>
							</div>							
						</div>
					</div>
					<div class="four wide column">
						<div class="ui segment">
							<a class="ui orange left ribbon label">Optional</a>
							<div class="hidden divider"></div>						
							<div class="field">
								<label>Show number of lines before:</label>
								<input type="text" placeholder="Default value is 100. Max is 500." name="linesBefore"/>
							</div>
							<div class="field">
								<label>Show number of lines after:</label>
								<input type="text" placeholder="Default value is 100. Max is 500." name="linesAfter"/>
							</div>
						</div>
					</div>
				</div>
				<div class="one column centered row">
					<button type="submit" class="ui submit button">SUBMIT</button>
					<div class="ui clear button">CLEAR FIELDS</div>
					<div class="ui large error message"></div>
				</div>		
			</div>		
		</form>
	</body>
</html>