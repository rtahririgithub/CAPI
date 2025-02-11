<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>
		
		<title>CAPI Utility Services</title>
		
		<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
		<spring:url value="/resources/Semantic_UI/dist/semantic.js" var="semanticJS" />
		<spring:url value="/resources/css/index.css" var="indexCSS" />
		<spring:url value="/resources/images/TELUS_EN.jpg" var="telusLogoJPG" />
		
		<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700|Open+Sans:300italic,400,300,700' rel='stylesheet' type='text/css'>
		<link href="${semanticCSS}" rel="stylesheet" />
		<link href="${indexCSS}" rel="stylesheet" />
		
		<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.js"></script>
		<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js"></script>
		<script src="${semanticJS}"></script>
	</head>
	
	<body>
		<div id="title">
			<img class='ui right floated image' src="${telusLogoJPG}" alt="telus logo" height="55px" width="264px" />
			<p>CAPI Utility Services</p>
		</div>
		<div class='ui divider'></div>
		<div id="menu">
			<table class="ui collapsing celled table">
				<thead>
					<tr>
						<th>Service</th>
						<th>Description</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><a href='search' target='_blank'>Log File Search Tool</a></td>
						<td>Tool to search log files from various directories</td>
					</tr>	
					<tr>
						<td><a href='read' target='_blank'>Log File Reader Tool</a></td>
						<td>Tool to read range of lines from specified log file</td>
					</tr>
					<tr>
						<td><a href='monitor' target='_blank'>Log Monitoring</a></td>
						<td>Schedule and cancel log monitoring tasks</td>
					</tr>	
					<tr>
						<td><a href='usage' target='_blank'>CAPI EJB Usage</a></td>
						<td>Query Client API EJB volumes to identify provider usage</td>
					</tr>	
					<tr>
						<td><a href='ldif' target='_blank'>LDIF Shakedown</a></td>
						<td>Checking if LDAP entries are consistent with LDIF changes</td>
					</tr>	
					<tr>
						<td><a href='swagger' target='_blank'>CAPI Rest Services</a></td>
						<td>Swagger viewer for CAPI Rest services</td>
					</tr>	
					<tr>
						<td><a href='identity' target='_blank'>Identity Encryption Tool</a></td>
						<td>Password Encryption Tool for KB Identities</td>
					</tr>	
					<tr>
						<td><a href='readme' target='_blank'>README</a></td>
						<td>Instructions on how the above tools work, changelog</td>
					</tr>
				</tbody>
			</table>
			<br />
			<p>Last Updated: May 26, 2020 by Wilson Cheong</p>
	
		</div>
	</body>
</html>