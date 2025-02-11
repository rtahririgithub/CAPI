<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/resources/css/README.css" var="readmeCSS" />
<spring:url value="/resources/Semantic_UI/dist/semantic.css" var="semanticCSS" />
<spring:url value="/resources/images/README/UnixAuth.jpg" var="unixAuthJPG" />
<spring:url value="/resources/images/README/EnvEJBSelect.jpg" var="envSelectJPG" />
<spring:url value="/resources/images/README/SearchCriteria.jpg" var="searchCriteriaJPG" />
<spring:url value="/resources/images/README/MaxResults.jpg" var="maxResultsJPG" />
<spring:url value="/resources/images/README/ResultsPageHighlighted.jpg" var="highlightedJPG" />
<spring:url value="/resources/images/README/LogReader.jpg" var="logReaderJPG" />
	
<!DOCTYPE html>
<html>
<head>
	<title>README - Log File Search and Reader Service - Telus BT CAPI Team</title>				
	<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700|Open+Sans:300italic,400,300,700' rel='stylesheet' type='text/css'>
	<link href="${semanticCSS}" rel="stylesheet" />
	<link href="${readmeCSS}" rel="stylesheet" />	  
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.js'></script>
		<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery.address/1.6/jquery.address.js'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.1.8/semantic.js'></script>	
</head>

<body id='README'>
	
	<div class='ui fixed bottom sticky'>
	<form>
		<button type='submit' class="ui labeled icon button" formaction='#Top'>
		<i class='arrow up icon'></i>TOP
		</button>
	</form>
	</div>
	
	<div class='ui segments' id='Top'>
	
		<div class='readme title ui center aligned purple raised segment'><h1>README - TELUS Client API Log File Tool</h1></div>		
		<div class='ui raised segment' id='TOC'>		
			<ol class="ui list">			
				<li><a href='#Search_Tool'>Log File Search Tool</a>
					<ol>
					<li><a href='#Using_the_Search_Tool'>Using the Log File Search Tool</a></li>
					<li><a href='#Viewing_Search_Results'>Viewing Log File Search Results</a></li>
					</ol>
				</li>				
				<li><a href='#Reader_Tool'>Log File Reader Tool</a>
					<ol>
					<li><a href='#Using_the_Reader_Tool'>Using the Reader Tool</a></li>
					</ol>
				</li>				
				<li><a href='#Log_Monitoring'>Log Monitoring</a></li>
				<li><a href='#CAPI_EJB_Usage'>CAPI EJB Usage</a></li>
				<li><a href='#CAPI_Rest_Services'>CAPI Rest Services</a></li>
				<li><a href='#Access_to_Unix_Servers'>Access To Unix Servers</a></li>				
				<li><a href='#Changelog'>Changelog</a></li>
			</ol>			
		</div>	
	</div>
	
	<div class='ui segments' id='Search_Tool'>
		<div class='logSearch title ui center aligned raised segment' id='Using_the_Search_Tool'><h1>Using the Log File Search Tool</h1></div>		
		<div class='ui raised segment'>
			<div class='ui large message'>
				<div class='header'>UNIX Authentication</div>				
				<p><b>Enter your Unix user (T or X-ID) and password.</b><br/>
				Ensure that you are using the proper credentials for prod or non-prod. 
				</p>
			</div>
			<img class='ui image' src='${unixAuthJPG}'/>
		</div>
		
		<div class='ui raised segment'>
			<div class='ui large message'>
				<div class='header'>Environment and EJB Selection</div>				
				<p><b>Select an environment and EJB from the dropdowns.</b><br/>
				Both fields are required when executing a search. The <b>All EJBs</b> selection is currently only implemented to <b>PT148</b> and <b>PT168</b>.
				</p>
			</div>
			<img class='ui image' src='${envSelectJPG}'/>
		</div>
		
		<div class='ui raised segment'>
			<div class='ui large message'>
				<div class='header'>Default Search Criteria</div>				
				<p><b>Enter search keywords into text boxes.</b><br/>
				At least one keyword is required when executing a search. Up to three separate keywords may be excepted.  Search criteria is all case insensitive.
				</p>
			</div>
			
			<img class='ui image' src='${searchCriteriaJPG}'/>
			
			<div class='ui large message'>
				<div class='header'>Keywords</div>
				
				<p><b>'Keyword'</b> can be a single word <b>(subscriber)</b>, or a phrase <b>(ExecuteThread: '5' for queue).</b><br/>
				Since this tool accesses the Unix server to search using 'grep', the keywords can be <b>regular expressions</b>, though this is not recommended. 
				Note that because of this, use of certain symbols may result in improper searches.</p>
				
				<p><b>Keywords are searched like so:</b><br/>
				Say you have three keywords, <b>'Alpha', 'Beta'</b> and <b>'Charlie'.</b> The tool will search for any line containing Alpha, Beta and Charlie, in any order.
				Example, it might return, "<b>Charlie</b> makes a <b>Beta</b> with <b>Alpha</b> about the cards Delta at the poker table."
				</p>
			</div>
		
			<div class='ui large message'>
				<div class='header'>Timestamp</div>				
				<p><b>Entering a value into the timestamp will search for that timestamp at the beginning of each line.<br/>
				Timestamp is to be entered as: DD Mon YYYY HH:MM:SS (if you do not know the exact time, that can be left out.)</b></p>
			</div>
		
			<div class='ui large message'>
				<div class='header'>Valid Date Values</div>				
				<div class='ui three column grid'>
					<div class='column'>
						<div class='ui basic horizontal segment'>
							<div class='ui bulleted list'>
								<div class='item'>Jan - January</div>
								<div class='item'>Feb - Febuary</div>
								<div class='item'>Mar - March</div>
								<div class='item'>Apr - April</div>
							</div>
						</div>
					</div>
					
					<div class='column'>
						<div class='ui basic horizontal segment'>
							<div class='ui bulleted list'>
								<div class='item'>May - May</div>
								<div class='item'>Jun - June</div>
								<div class='item'>Jul - July</div>
								<div class='item'>Aug - August</div>
							</div>
						</div>
					</div>
					
					<div class='column'>
						<div class='ui basic horizontal segment'>
							<div class='ui bulleted list'>
								<div class='item'>Sep - September</div>
								<div class='item'>Oct - October</div>
								<div class='item'>Nov - November</div>
								<div class='item'>Dec - December</div>
							</div>
						</div>
					</div>			
				</div>				
			</div>
		</div>
		
		<div class='ui raised segment'>		
			<img class='ui image' src='${maxResultsJPG}'/>
			<div class='ui large message'>
				<div class='header'>Max Results Per File</div>				
				<p><b>You can select a maximum amount of results to be read from each file. The default value is 5. The number must be between 1 and 100, or it will revert to the default value.</b></p>
			</div>			
		</div>
				
		<div class='logSearch title ui center aligned raised segment' id='Viewing_Search_Results'><h1>Viewing Log File Search Results</h1></div>
		<div class='ui raised segment'>
			<img class='ui rounded bordered image' src='${highlightedJPG}'/>
			<div class='ui large message'>
				<div class='header'>Search Results</div>				
				<ol class='ui ordered list'>
					<li>Shows the environment and EJB searched at top of page.</li>
					<li>Highlight buttons. Can toggle to highlight or unhighlight search keywords. Can highlight or dehighlight all.</li>
					<li>Shows the filepath and lines that come up as a search in that file. Line number is shown at the beginning <b>(<i class='search icon'></i> ###) '...' )</b></li>
					<li>Can use <i class='search icon'></i> to search that line in that file.</li>
				</ol>
			</div>
			
			<div class='ui large message'>
				<div class='header'>Highlighting</div>				
				<p>
				<span style="background-color: #FFFF00">Search Criteria 1</span><br/>
				<span style="background-color: #FF9933">Search Criteria 2</span><br/>
				<span style="background-color: #FF9999">Search Criteria 3</span><br/>
				<span style="background-color: #66FFFF">Search Criteria 4</span>				
				</p>
			</div>
		</div>

	</div>
	
	<div class='ui segments' id='Reader_Tool'>
		<div class='logReader title ui center aligned raised segment' id='Using_the_Reader_Tool'><h1>Using the Log File Reader Tool</h1></div>		
		<div class='ui raised segment'>		
			<img class='ui image' src='${logReaderJPG}'/>
			<div class='ui large message'>
				<div class='header'>Default Search Criteria</div>				
				<p>
					<b>File path:</b> Enter path of file you wish to search from. Can retrieve this from log file search tool.<br/>
					<b>Line number:</b> Enter which line number you wish to centre your reader around. Can retrieve this from log file search tool.<br/>
					<b>T-ID and UNIX Password:</b> Authentication for Unix servers access. Ensure you are using the proper credentials for prod or non-prod.<br/>
					<b>Lines before and after:</b> Enter the number of lines before and after you would like to read. Invalid entries (Less than 0) will be converted to a default of 10.
				</p>
			</div>			
			<div class='ui large message'>
				<div class='header'>Lines Before and After</div>
				
				<p><b>Enter the number of lines before and after that you wish to read. </b>The default value for lines before and after is 50.</p>
			</div>			
		</div>	
	</div>
	
	<div class='ui segments' id='Log_Monitoring'>
		<div class='logSearch title ui center aligned raised segment'><h1>Log Monitoring</h1></div>	
		<div class='ui raised segment'>			
			<div class='ui large message'>
				<div class='header'>Log Monitoring and Alerts</div>	
				<p>TBD</p>
			</div>
		</div>
	</div>
	
	<div class='ui segments' id='CAPI_EJB_Usage'>
		<div class='logReader title ui center aligned raised segment'><h1>CAPI EJB Usage</h1></div>	
		<div class='ui raised segment'>			
			<div class='ui large message'>
				<div class='header'>Client API EJB Usage</div>	
				<p>TBD</p>
			</div>
		</div>
	</div>
	
	<div class='ui segments' id='CAPI_Rest_Services'>
		<div class='logSearch title ui center aligned raised segment'><h1>CAPI Rest Services</h1></div>	
		<div class='ui raised segment'>			
			<div class='ui large message'>
				<div class='header'>Client API REST Service Inventory</div>	
				<p>TBD</p>
			</div>
		</div>
	</div>
		
	<div class='ui segments' id='Access_to_Unix_Servers'>
		<div class='unix title ui center aligned raised segment'><h1>Getting Access to Unix Servers</h1></div>		
		<div class='ui raised segment'>			
			<div class='ui large message'>
				<div class='header'>Get Access to Unix Servers</div>					
					<p>Request access through <a href='http://go/novo'>NOVO</a>. 
					Provide the reason you work on the Client API team and need access to inspect logs for troubleshooting purposes. 
					</p>
					<p>You will need access to the following servers:</p>					
					<ol>
						<li>Pre-Production: CRAB/HUDSON/ln98312 (LAIRD), btln000098 (QIDC/KIDC), ln99828 & ln99825 (New servers for LAIRD)</li>
						<li>Staging: ln99239 (New server for LAIRD), ln99587 (Newest server for LAIRD)
						<li>Production: CABOOSE/lp97204 (LAIRD), sp20490 (Notification Engine), btlp000064(QIDC/KIDC)</li>
						<li>Development: HOPPER</li>
					</ol>				
			</div>		
		</div>
	</div>
	
	
	<div class='ui segments' id='Changelog'>
		<div class='changelog title ui center aligned raised segment'><h1>Changelog</h1></div>		
		<div class='ui raised segment'>	
			<div class='ui large message'>
				<div class='header'>26 May 2020 - Changes</div>
				<ul>
					<li>Added more log services (again) - for staging.</li>
					<li>Updated README to include additional log servers.</li>
					<li>Added GUI (and support) for LDIF shakedown - both by release and ldif list.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>17 Mar 2020 - Changes</div>
				<ul>
					<li>Updated README to include additional log servers.</li>
					<li>Completed EJB Usage cache loading for current month (partial loads).</li>
					<li>Added EJB Cache task so the data can be downloaded automatically at the start of every month.</li>
					<li>EJB Cache task also archives (zip) folders that are very old (configurable) and removes the large log files.</li>
					<li>Added GUI for Identity Password Encryption/Decryption tool.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>29 Feb 2020 - Changes</div>
				<ul>
					<li>Since SOA isn't able to monitor EJB usage, added the CAPI EJB Usage service.</li>
					<li>Added capability to download files locally in properties file.</li>
					<li>Loads performance monitoring data (i.e. usage) into cache for 'performance' in search results.</li>
					<li>Volumes are loaded per month, but searchable by DAO operation name.</li>
					<li>In addition to daily loads, also displays monthly totals and averages.</li>
					<li>Can be extended to show other monitored statistics like failures, average response time, etc.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>12 Feb 2020 - Changes</div>
				<ul>
					<li>Updated inventory of CAPI Rest services.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>16 Dec 2019 - Changes</div>
				<ul>
					<li>Added the Welcome Email report monitoring service.</li>
					<li>Expanded search capabilities (for monitoring purposes).</li>
					<li>Added capability to call other web services (i.e. SIDGS, SIS).</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>25 Mar 2019 - Changes</div>
				<ul>
					<li>Added support for new PT140 log server ln99828.</li>
					<li>Updated logServer lookup via configuration (rather than enum).</li>					
					<li>Added download feature (only when viewing files).</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>10 Dec 2018 - Changes</div>
				<ul>
					<li>Added log monitoring services.</li>
					<li>Improved date range filtering.</li>
					<li>Added new CIS web services.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>15 Oct 2018 - Changes</div>
				<ul>
					<li>Updated root folder again to reflect PR changes - now uses 'logsa'.</li>
					<li>Added CAPI Rest services viewer.</li>
					<li>Fixed a few bugs related to date range filtering.</li>
					<li>Added new CIS web services.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>15 Jul 2018 - Changes</div>
				<ul>
					<li>Updated root folder again to reflect PR changes - now uses 'logsb'.</li>
					<li>Enabled date range filtering.</li>
					<li>Added new CIS web services.</li>
				</ul>
			</div>
			<div class='ui large message'>
				<div class='header'>10 Jan 2017 - Changes</div>
				<ul>
					<li>Fixed logs vs logs2 root folder defect</li>
					<li>Added support for QIDC, KIDC, and any additional log servers.</li>
					<li>Changed the UI flow to add application selection screen - this will determine which log server to connect to.</li>
					<li>Log File Reader Tool will select the log server rather than the environment.</li>
					<li>Added more log server configurations for CAPI CIS Web Services, Notification Platform, etc.</li>
				</ul>
			</div>
			
			<div class='ui large message'>
				<div class='header'>13 Jun 2016 - Changes</div>
				<ul>
					<li>Updated both Log File Search Tool and Reader Tool with new UI changes.</li>
					<li>Added login to session so you don't need log in every time.</li>
					<li>Added application grouping for all components - can support multiple applications (i.e. WPS, CAPI Web Services, SSF).</li>
					<li>Timestamp and multiple search criteria features temporarily disabled.</li>
					<li>New screens to select environment so that the proper login credentials are used (i.e. UNIX vs Windows).</li>
					<li>Code base has been refactored and is now maven and TFS ready.</li>
					<li>Log file paths are configurable via XML property files (not in code).</li>
					<li>Log file paths are shown in the result page.</li>
				</ul>
			</div>
			
			<div class='ui large message'>
				<div class='header'>20 Jul 2015 - Changes</div>
				<ul>
					<li>Log Reader now requires authentication with Unix user and password</li>
					<li>Log Reader UI formatted similar to Log Search UI</li>
					<li>Moved Highlighter segment on search results page to float right</li>
					<li>Added minimize and maximize buttons for Highlighter segment</li>
				</ul>
			</div>
			
			<div class='ui large message'>
				<div class='header'>14 Jul 2015 - Perfomance Changes</div>
				<ul>
					<li>Now only accesses Unix server once (used to access twice, once for file names, once for content)</li>
					<li>Slightly faster search times (noticeable on PT evironments)</li>
					<li>Added search time (in milliseconds)</li>
				</ul>
			</div>
			
			<div class='ui large message'>
				<div class='header'>13 Jul 2015 - Added Error Messages</div>
				<ul>
					<li>Server Errors - Auth Fail or other JSch connection exceptions</li>
					<li>Other Errors - Refers user to email Calvin Wong</li>
					<li>'No search results' message</li>
					<li>'Invalid directory' message</li>
				</ul>
			</div>
		
			<div class='ui large message'>
				<div class='header'>9 Jul 2015 - Aesthetic Changes</div>
				<ol>
				<li>Log Search
					<ul>
					<li>Reformatted UI segments</li>
					<li>Added <i class='warning icon'></i> to mandatory fields</li>
					</ul>
				</li>
				<li>Log Reader
					<ul>
					<li>Formatted results page, it actually looks decent now!</li>
					<li>Highlights line selected as start point</li>
					</ul>
				</li>
				</ol>
			</div>
			
			<div class='ui large message'>
				<div class='header'>3 Jul 2015 - Released!</div>				
				<p>Released v1.0
				</p>
			</div>
			
			<div class='ui large message'>
				<div class='header'>5 Apr 2015 - Easter</div>				
				<p>Easter Eggs</p>
				<ul>
					<li>The Old God Waits.</li>
					<li>Time to watch G.O.T.?</li>
					<li>Huehuehue</li>
				</ul>
			</div>
		
		</div>
		
	</div>
	<div class='ui hidden divider'></div>
	
</body>
</html>
