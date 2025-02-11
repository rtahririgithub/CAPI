<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/css/logservicesmain.css" var="logCSS" />
<spring:url value="/resources/js/logservicesmain.js" var="logJS" />	
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />	
<spring:url value="/resources/images/TELUS_EN.jpg" var="telusLogoJPG" />
<spring:url value="/resources/images/logSearch/RedPandaBalls.jpg" var="redPandaJPG" />
<spring:url value="/resources/images/logSearch/Lovebird.jpg" var="lovebirdJPG" />
<c:url value="/ldif" var="ldifURL"/>

<!DOCTYPE html>
<html>
	<head>
		<title>Ldif Shakedown</title>		
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
			<a href='${ldifURL}'>
				<img id='telus_logo' class='ui right floated image' src="${telusLogoJPG}" alt='telus logo' height='35px' width='168px' />
			</a>
			<h2 class='body title'>LDIF SHAKEDOWN</h2>
		</div>
	
		<div class='ui large attached message'>
			<div class='header'>
				Select an environment.
			</div>
		</div>
		
		<div class="ui segment">
			<div class="ui grid">
				<div class="two wide column"></div>
				<div class="four wide middle aligned column">
					<img class='ui medium centered animal image' id='panda' src='${redPandaJPG}' />
				</div>
				<div class="four wide middle aligned column">
					<div class='ui fluid green selection dropdown'>
						<input type='hidden' name='search_environment'>
						<div class='default text'>Select an environment...</div>				
						<i class='dropdown icon'></i>
						<div class='menu'>						
							<c:forEach items="${environments}" var="environment" varStatus="status">
								<a href="${ldifURL}/${environment.shortname}" class='item'>${environment.name}</a>
							</c:forEach>					
						</div>
					</div>
				</div>
				<div class="six wide column"></div>
			</div>
		</div>
	</body>
</html>