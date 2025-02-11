<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/css/README.css" var="readmeCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
	
<!DOCTYPE html>
<html>
<head>
	<title>CAPI REST Services</title>				
	<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700|Open+Sans:300italic,400,300,700' rel='stylesheet' type='text/css'>
	<link href="${semanticCSS}" rel="stylesheet" />
	<link href="${readmeCSS}" rel="stylesheet" />	  
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
</head>

<body id='README'>
		
	<div class='ui segments'>
	
		<div class='readme title ui center aligned purple raised segment'><h1>CAPI REST Services</h1></div>
		<div class='ui raised segment' id='TOC'>	
			<div class='ui container'>	
				<div class='ui three column grid'>
					<div class='four wide column'></div>		
					<div class='eight wide column'>
						<ol class="ui list">	
							<c:forEach items="${services}" var="service">
								<li><a href="swagger/read/${service.name}" class='item' target='_blank'>${service.servicename}</a></li>
							</c:forEach>	
						</ol>
					</div>			
					<div class='four wide column'></div>
				</div>
			</div>	
		</div>
	</div>
		
</body>
</html>