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
<c:url value="/monitor" var="monitorURL"/>

<!DOCTYPE html>
<html>
	<head>
		<title>Log Monitoring</title>		
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>
		<link href="${semanticCSS}" rel="stylesheet" />	
		<link href="${logCSS}" rel="stylesheet" />			
		<link href="${monitoringCSS}" rel="stylesheet" />
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
		<script src="${logJS}"></script>
	</head>

	<body id='logSearch'>
		<div class='ui hidden divider'></div>
	
		<div class='ui title header segment'>
			<a href='${searchURL}'> <img id='telus_logo'
				class='ui right floated image' src="${telusLogoJPG}" alt='telus logo'
				height='35px' width='168px' />
			</a>
			<h2 class='body title'>LOG MONITORING</h2>
		</div>
	
		<div class='ui attached message'>	
			<c:choose>		
				<c:when test="${enabled}">
					<div class='ui segments' id='Search_Tool'>
						<div class='running title ui center aligned raised segment'><h1>Active scheduled monitoring jobs</h1></div>		
						<div class='ui raised segment'>
							<div class="ui grid">
								<div class="one wide column"></div>
									<div class="fourteen wide middle aligned column">									
										<c:choose>		
											<c:when test="${empty scheduledTasks}">	
												<div class="header">No jobs scheduled.</div>
											</c:when>
											<c:otherwise>		
												<div class="ui four cards">			
													<c:forEach items="${scheduledTasks}" var="scheduledTask">		
														<div class="ui card">		
															<div class="content">		
																<c:if test="${!scheduledTask.underCooldown}">									
																	<form class="ui form" method="POST" action="${monitorURL}/task/execute/${scheduledTask.shortname}/${scheduledTask.environment}">
																		<div class="circular ui right floated icon submit button" title="Run Now!"><i class="play icon"></i></div>																	
																	</form>										
																</c:if>	
																<div class="header">${scheduledTask.name} in ${fn:toUpperCase(scheduledTask.environment)}</div>
																<div class="meta"><i>${scheduledTask.description}</i></div>
																<div class="ui divider"></div>
																Scheduled ${scheduledTask.schedule} <br />
															</div>
															<form class="ui form" method="POST" action="${monitorURL}/task/cancel/${scheduledTask.shortname}/${scheduledTask.environment}">
																<div class="ui bottom attached red submit button">Cancel</div>
															</form>
														</div>
													</c:forEach>
												</div>
											</c:otherwise>		
										</c:choose>															
									</div>
								</div>					
								<div class="one wide column"></div>
							</div>
						</div>
					</div>				
				</c:when>
				<c:otherwise>							
					<div class='header'>
						Log monitoring has not been enabled yet. Click <a href="${monitorURL}/login">here</a> to
						enable it.
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	
		<div class='ui message'>
			<div class='ui segments' id='Search_Tool'>
				<div class='available title ui center aligned raised segment'><h1>Available monitoring jobs to schedule</h1></div>		
				<div class='ui raised segment'>
					<div class="ui grid">
						<div class="one wide column"></div>
						<div class="fourteen wide middle aligned column">			
							<div class="ui four cards">																					
								<c:forEach items="${availableTasks}" var="availableTask">								
									<div class="ui card">											
										<div class="content">
											<div class="header">${availableTask.name}</div>
											<div class="meta"><i>${availableTask.description}</i></div>
											<div class="ui divider"></div>
											Environments: 
											<c:choose>
												<c:when test="${enabled}">														
													[<c:forEach items="${availableTask.environments}" var="availableTaskEnv" varStatus="loop">
														<form class="ui form" method="POST" action="${monitorURL}/task/schedule/${availableTask.shortname}/${availableTaskEnv}" style="display:inline;">
															<a href="javascript:;" onclick="parentNode.submit();" ${availableTaskEnv == availableTask.currentProdEnv ? 'style="font-weight:bold"' : ''}>
																${fn:toUpperCase(availableTaskEnv)}</a>
														</form>
														<c:if test="${!loop.last}">
															,
														</c:if>									
													</c:forEach>]
												</c:when>
												<c:otherwise>
													${fn:toUpperCase(availableTask.environments)} <br />
												</c:otherwise>
											</c:choose>				
										</div>
										<form class="ui form" method="POST" action="${monitorURL}/task/schedule/${availableTask.shortname}">
											<div class="ui bottom ${enabled ? '' : 'disabled'} attached green submit button">Schedule ${fn:length(availableTask.environments) > 1 ? 'All' : ''}</div>
										</form>
									</div>								
								</c:forEach>
							</div>
							<div class="one wide column"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>