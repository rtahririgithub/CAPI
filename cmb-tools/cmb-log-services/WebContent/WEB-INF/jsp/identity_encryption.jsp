<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<spring:url value="/resources/css/logservicesmain.css" var="logCSS" />
<spring:url value="/resources/css/monitoring.css" var="monitoringCSS" />
<spring:url value="/resources/js/logservicesmain.js" var="logJS" />	
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />	
<spring:url value="/resources/images/TELUS_EN.jpg" var="telusLogoJPG" />
<spring:url value="/resources/images/logSearch/RedPandaBalls.jpg" var="redPandaJPG" />
<spring:url value="/resources/images/logSearch/Lovebird.jpg" var="lovebirdJPG" />
<c:url value="/identity" var="identityURL"/>

<!DOCTYPE html>
<html>
	<head>
		<title>Identity Encryption Tool</title>		
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>
		<link href="${semanticCSS}" rel="stylesheet" />	
		<link href="${logCSS}" rel="stylesheet" />			
		<link href="${monitoringCSS}" rel="stylesheet" />
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
		<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/1.5.10/clipboard.min.js"></script>
		<script src="${logJS}"></script>
	</head>

	<body id='logSearch'>
		<div class='ui hidden divider'></div>
	
		<div class='ui title header segment'>
			<a href='${searchURL}'> <img id='telus_logo'
				class='ui right floated image' src="${telusLogoJPG}" alt='telus logo'
				height='35px' width='168px' />
			</a>
			<h2 class='body title'>Identity Encryption Tool</h2>
		</div>
	
		<div class='ui attached message'>	
			<div class='ui segments' id='Search_Tool'>
				<c:if test="${exception != null}">
					<div class="ui red authentication message" ${exception == null ? 'style="display:none"' : ''}><i class="close icon"></i>${exception.message}</div>
				</c:if>			
				<div class='ui segment'>			
					<form class="ui form" method="POST" action="${identityURL}/encrypt">
						<div class="ui action input">
							<input name='clearText' value='${clearText}' type='text' placeholder='Enter text to encrypt'></input>
							<input type='hidden' name='encryptedText' value='${encryptedText}'>
							<input type='hidden' name='decryptedResult' value='${decryptedResult}'>
							<div class="ui right attached green submit button">
								<div class="center aligned">Encrypt!</div>
							</div>
						</div>
					</form>		
				</div>
				<div class="ui raised segment">
					<div style="word-wrap: break-word;">
					<h3>Result:<br/>
					<div style="font-family:'Courier New';"><p id="result">${encryptedResult}</p></div>
					</h3>
					</div>
					<button class="ui icon clipboard button" ${encryptedResult == null ? 'style="display:none"' : ''} data-clipboard-target='#result'><i class='copy outline icon'></i></button>	
				</div>
				
				<div class="ui divider"></div>
					
				<div class='ui segment'>								
					<form class="ui form" method="POST" action="${identityURL}/decrypt">
						<div class="ui action input">
							<input name='encryptedText' value='${encryptedText}' type='text' placeholder='Enter text to decrypt'></input>
							<input type='hidden' name='clearText' value='${clearText}'>
							<input type='hidden' name='encryptedResult' value='${encryptedResult}'>
							<div class="ui right attached purple submit button">
								<div class="center aligned">Decrypt!</div>
							</div>
						</div>
					</form>		
				</div>
				
				<div class="ui raised segment">
					<h3>Result:<br/><div style="font-family:'Courier New';">${decryptedResult}</div></h3>	
				</div>
			</div>				
		</div>		
		<script>
			new Clipboard('.clipboard.button');
		</script>
	</body>
</html>