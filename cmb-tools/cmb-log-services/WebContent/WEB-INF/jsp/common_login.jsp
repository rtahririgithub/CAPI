<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
			
<spring:url value="/resources/css/logservicesmain.css" var="logCSS" />
<spring:url value="/resources/js/logservicesmain.js" var="logJS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/images/TELUS_EN.jpg" var="telusLogoJPG" />
<spring:url value="/resources/images/logSearch/MonkeyBananaPile.jpg" var="monkeyJPG" />
		
<!DOCTYPE html>
<html>
	<head>
		<title>${param.link}</title>		
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>	
		<link href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/1.12.3/semantic.css" rel="stylesheet" />	
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
			<a href='${param.link}'>
				<img id='telus_logo' class='ui right floated image' src="${telusLogoJPG}" alt='telus logo' height='35px' width='168px' />
			</a>
			<h2 class='body title'>${param.header}</h2>
		</div>
	
		<div class='ui large attached message'>
			<div class='header'>
				${param.loginHeader}.
			</div>
		</div>
		
		<form id='searchForm' name='searchForm' class='ui form attached raised segment' method='post' action='${param.submitLink}/authorize'>
			<div class="ui large error message"></div>
			<div class="ui red authentication message" ${errorMessage == null || errorMessage == '' ? 'style="display:none"' : ''}><i class="close icon"></i>${errorMessage}</div>
			<div class="ui grid">	
				<div class="one wide column"></div>	
				<div class="four wide column">
					<img class='ui medium left floated medium animal image' id='monkey' src='${monkeyJPG}' />
				</div>	
				<div class="eight wide column">
					<div class='ui hidden divider'></div>
					<c:if test='${requireUnixLogin}'>
						<label>UNIX Login:</label>
						<div class="fields">
							<div class="field">														
								<input name='unixId' type='text' placeholder='T or X-ID'></input>
							</div>	
							<div class="inline field">
								<input name='unixPass' type='password' placeholder='UNIX Password'></input>							
								<div class='ui left pointing label'>
									<a href='http://habitat.tmi.telus.com/collaborate/display/sm/Client+API+New+Hire+Checklist#ClientAPINewHireChecklist-RequestaccesstoLogServers' target='_blank'>Request Access to Log Servers</a>
								</div>
							</div>
						</div>
					</c:if>
					<c:if test='${requireWindowsLogin}'>
						<label>Windows Login:</label>
						<div class="fields">
							<div class="field">														
								<input name='windowsId' type='text' placeholder='T or X-ID'></input>
							</div>	
							<div class="inline field">
								<input name='windowsPass' type='password' placeholder='Windows Password'></input>
								<c:if test='${!requireUnixLogin && requireWindowsLogin}'>
									<div class='ui left pointing label'>
										<a href='http://habitat.tmi.telus.com/collaborate/display/sm/Client+API+New+Hire+Checklist' target='_blank'>Request Access to Log Servers</a>
									</div>
								</c:if>
							</div>
						</div>
					</c:if>
					<button class='ui submit button' type='submit'>LOGIN</button>
					
				</div>	
				<div class="three wide column"></div>			
			</div>		
		</form>
	</body>
</html>