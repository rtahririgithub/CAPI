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
<c:url value="/usage" var="usageURL"/>

<!DOCTYPE html>
<html>
	<head>
		<title>CAPI EJB Usage</title>		
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
			<h2 class='body title'>CAPI EJB USAGE</h2>
		</div>
	
		<div class='ui attached message'>	
			<c:choose>		
				<c:when test="${enabled}">
					<div class='ui segments' id='Search_Tool'>
											
						<div class='available title ui center aligned raised segment'><h1>Find usage based on EJB method call</h1></div>		
						<div class='ui raised segment'>
							<c:choose>
								<c:when test="${empty cacheList}">
									<h3><i>The usage cache is empty.  You need to load some usage stats into the cache before you can search.</i></h3>
								</c:when>
								<c:when test="${locked}">
									<h3><i>The usage cache is being refreshed - please wait until it's done.  The real-time search will be implemented later.</i></h3>
								</c:when>
								<c:otherwise>							
									<form class="ui form" method="POST" action="${usageURL}/search/results" target="_blank">
										<div class="ui action input">
											<input name='method' type='text' placeholder='Enter method signature'></input>
											<button class="ui icon submit button">
												<i class="search icon"></i>
											</button>	
										</div>
									</form>							
								</c:otherwise>
							</c:choose>
						</div>
						
						<div class="ui divider"></div>
						
						<div class='running title ui center aligned raised segment'><h1>EJB Usage Cache</h1></div>		
						<div class='ui raised segment'>
							<div class="ui two column very relaxed celled fluid stackable grid">
								<div class="column">
									<h3>Load usage stats into cache from performance logs:</h3>
									<div class="ui fluid top aligned card">											
										<div class="content">
											<div class="header">	
												<form class="ui form" method="POST" action="${usageURL}/cache/load">
													<div class="ui fields">													
														<div class="ui field">
															<div class='ui component selection dropdown'>
																<input type='hidden' name='month'>
																<div class='default text'>Select month</div>
																<i class='dropdown icon'></i>
																<div class='menu'>						
																	<div class='item' data-value='01'>January</div>
																	<div class='item' data-value='02'>February</div>
																	<div class='item' data-value='03'>March</div>
																	<div class='item' data-value='04'>April</div>
																	<div class='item' data-value='05'>May</div>
																	<div class='item' data-value='06'>June</div>
																	<div class='item' data-value='07'>July</div>
																	<div class='item' data-value='08'>August</div>
																	<div class='item' data-value='09'>September</div>
																	<div class='item' data-value='10'>October</div>
																	<div class='item' data-value='11'>November</div>
																	<div class='item' data-value='12'>December</div>
																</div>
															</div>
														</div>
														<div class="ui field">
														<div class='ui component selection dropdown'>
															<input type='hidden' name='year'>
															<div class='default text'>Select year</div>
															<i class='dropdown icon'></i>
															<div class='menu'>						
																<c:forEach items="${availableYears}" var="availableYear">																
																	<div class='item' data-value='${availableYear}'>${availableYear}</div>
																</c:forEach>
															</div>
														</div>
														</div>
														<div class="ui right attached fluid ${locked ? 'disabled' : ''} green submit button">
															<div class="center aligned">Load into cache</div>
														</div>
													</div>
												</form>
											</div>
										</div>
									</div>
									<div class="ui horizontal divider">OR</div>
									<form class="ui form" method="POST" action="${usageURL}/cache/load/latest">
										<button class='ui centered green fluid ${locked||true ? "disabled" : ""} submit button' type='submit'>Load data from the last 3 months</button>
									</form>
								</div>					
								<div class="column">
									<h3>Current cache of EJB usage (by month):</h3>											
									<c:choose>
										<c:when test="${empty cacheList}">																				
											<div class="ui fluid card">											
												<div class="content">												
													<div class="center aligned header">Empty Cache</div>
												</div>
											</div>		
											<c:if test="${locked}">												
												<div class="ui active dimmer">
													<div class="ui indeterminate text loader">Cache is being refreshed</div>
												</div>	
											</c:if>
										</c:when>
										<c:otherwise>
											<div class="ui three cards">							
												<c:forEach items="${cacheList}" var="usageCache">											
													<div class="ui card">											
														<div class="content">
															<div class="center aligned header">${usageCache.description}</div>
															<div class="center aligned meta">
																<c:choose>														
																	<c:when test="${usageCache.complete}">Complete<i class="right floated green check circle icon"></i></c:when>
																	<c:otherwise>Up to ${usageCache.lastDate}<i class="right floated red info circle icon"></i></c:otherwise>
																</c:choose>														
															</div>
														</div>
														<form class="ui form" method="POST" action="${usageURL}/cache/unload/${usageCache.id}">
															<div class="ui bottom attached red submit button">Clear</div>
														</form>
													</div>
												</c:forEach>
											</div>	
											<c:if test="${locked}">												
												<div class="ui active dimmer">
													<div class="ui indeterminate text loader">Cache is being refreshed</div>
												</div>					
											</c:if>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>				
				</c:when>
				<c:otherwise>							
					<div class='header'>
						CAPI EJB usage credentials has not been enabled yet. Click <a href="${usageURL}/login">here</a> to
						enable it.
					</div>
				</c:otherwise>
			</c:choose>
		</div>		
	</body>
</html>