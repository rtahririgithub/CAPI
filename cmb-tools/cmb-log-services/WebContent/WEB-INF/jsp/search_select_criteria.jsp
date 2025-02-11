<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:url value="/resources/css/logservicesmain.css" var="logCSS" />
<spring:url value="/resources/css/daterangepicker.css" var="daterangeCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/js/logservicesmain.js" var="logJS" />	
<spring:url value="/resources/js/daterangepicker.js" var="daterangeJS" />	
<spring:url value="/resources/js/daterangepicker_custom.js" var="daterangeCustJS" />	
<spring:url value="/resources/js/moment.js" var="momentJS" />	
<spring:url value="/resources/images/TELUS_EN.jpg" var="telusLogoJPG" />
<spring:url value="/resources/images/logSearch/BunnyLantern.jpg" var="bunnyJPG" />
<spring:url value="/resources/images/logSearch/Lion.jpg" var="lionJPG" />	
<c:url value="/search" var="searchURL"/>

<!DOCTYPE html>
<html>
	<head>
		<title>Log File Search Tool</title>		
		<meta charset='utf-8' />
		<meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1' />
		<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0'>			
		<link href="${semanticCSS}" rel="stylesheet" />			
		<link href="${daterangeCSS}" media="all" type="text/css" rel="stylesheet" />
		<link href="${logCSS}" type="text/css" rel="stylesheet" />		
		<script src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script	src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
		<script type="text/javascript" src="${momentJS}"></script>
		<script type="text/javascript" src="${daterangeJS}"></script>
		<script type="text/javascript" src="${daterangeCustJS}"></script>
		<script type="text/javascript" src="${logJS}"></script>
	</head>
	
	<body id='logSearch'>
		<div class='ui hidden divider'></div>

		<div class='ui title header segment'>
			<a href='${searchURL}'>
				<img id='telus_logo' class='ui right floated image' src="${telusLogoJPG}" alt='telus logo' height='35px' width='168px' />
			</a>
			<h2 class='body title'>LOG FILE SEARCH TOOL</h2>
		</div>
	
		<div class='ui large attached message'>
			<div class='header'>
				Connected to <a href="${searchURL}">${environment.name}</a> and searching <a href="${searchURL}/${environment.shortname}">${application.name}</a> logs.  
			</div>
		</div>
		
		<form id='searchForm' class='ui form attached raised segment' method='post' action='${searchURL}/${environment.shortname}/${application.shortname}/results${live}' target="_blank">
			<div class="ui large error message"></div>
			<div class="ui red authentication message" ${errorMessage == null || errorMessage == '' ? 'style="display:none"' : ''}><i class="close icon"></i>${errorMessage}</div>
			<div class="environment name" style="display:none">${environment.shortname}</div>
			<div class="ui grid">
				<div class="two column middle aligned row">
					<div class="column">
						<div class="ui grid">
							<div class="middle aligned row">
								<div class="six wide column">
									<img class='ui medium left floated medium animal image' id='bunny' src='${bunnyJPG}' />
								</div>
								<div class="ten wide column">
									<div class="ui segment">
										<a class="ui green right ribbon label">Mandatory</a>
										<!-- 
										<div class="application field">
											<label>Application:</label>
											<div class='ui app fluid selection dropdown'>	
												<input type='hidden' name='application'>
												<div class='default text'>Select an application</div>				
												<i class='dropdown icon'></i>
												<div class='menu'>						
													<c:forEach items="${applications}" var="application" varStatus="status">
														<div class='item' data-value='${application}'>${application}</div>
													</c:forEach>					
												</div>
											</div>
										</div>
										 -->
										<div class="component field">
											<label>Component:</label>
											<div class='ui component fluid selection dropdown'>
												<input type='hidden' name='componentName'>
												<div class='default text'>Select a component</div>
												<i class='dropdown icon'></i>
												<div class='menu'>						
													<c:forEach items="${components}" var="component" varStatus="status">
														<div class='item' data-value='${component}'>${component}</div>
													</c:forEach>	
												</div>
											</div>
										</div>
										<div class="field">
											<label>Search criteria:</label>
											<input name='search_crit1' type='text' placeholder='Enter your search criteria. i.e. BAN or phone number'></input>
											<input name='search_crit2' type='text' placeholder='Enter another search criteria. (TBD)' disabled='disabled'></input>
											<input name='search_crit3' type='text' placeholder='Even more search criteria!!! (TBD)' disabled='disabled'></input>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>	
					<div class="column">
						<div class="ui grid">
							<div class="middle aligned row">
								<div class="ten wide column">	
									<div class="ui grid">
										<div class="column">
											<div class="ui segment">
												<a class="ui orange right ribbon label">Optional</a>
												<div class="field">			
													<label>Max results (per file):</label>			
													<input name='numResults' type='text' placeholder='Default value is 5'></input>	
												</div>
												<div class="field">
													<label>Date (YYYY/MM/DD - YYYY/MM/DD):</label>
													<div class="ui left icon input">
														<input class="daterange" name='dateSelect' id='dateSelect' type='text' placeholder='Date'></input>
														<i class="calendar icon"></i>
													</div>		
												</div>		
												<div class="field">		
													<label>Search archived log files (TBD):</label>
													<div class='ui checkbox'>					
														<input name='archived' type='checkbox' disabled='disabled'></input>	
														<label>Yes</label>																									
													</div>
												</div>
											</div>		
										</div>
									</div>
								</div>
								<div class="six wide column">
									<img class='ui medium left floated medium animal image' id='lion' src='${lionJPG}' />
								</div>
							</div>
						</div>
					</div>	
				</div>	
				<div class="one column centered row">	
					<button class='ui submit button' type='submit'>SEARCH</button>		
				</div>		
			</div>		
		</form>	
	</body>
</html>