<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<t:main-layout>

	<script type="text/javascript">
		$(document).ready(function() {

			$('#container').jstree({
				'core' : {  
					'data' : {
			            'url' : function (node) {
			            	return 'http://localhost:8080/cmb-support-portal/config-mgmt/nodes?nodeId=' + node.id;	
			            },
			            'data' : function (node) {
			                return { 'id' : node.id };
			            }
					}
				}
			});		
			
			$('#environmentId').change( function() {
				$('#configExploreForm').submit();
			});
		});
	</script>

	<div class="ui grid">
		<div class="column">
		
			<t:page-title title="Configuration LDAP Explorer" description="Select an environment from the list box and navigate ldap tree using tree control. Select the node to see the value"/>
			
			<div class="ui segment">
				<form:form cssClass="ui form" commandName="configExploreForm" action="config-mgmt/selectEnvironment">
					<div class="field">
						<label>Environment</label> 
						<form:select path="environmentId" cssClass="ui search dropdown">
							<form:option value="0" label="Please Select..."/>
							<form:options items="${environments}" itemLabel="name" itemValue="id"/>
						</form:select>
					</div>
				</form:form>
				<h5 class="ui top attached message">LDAP Tree Control</h5>
				<div class="ui attached segment">

					<div id="container" style="height: 200px; overflow: auto;">
					</div>
				</div>
				
				<h5 class="ui header">Selected Node Value</h5>
				<div class="ui segment">
				xxxxxxx
				yyyy
				</div>
				<div class="ui teal right floated button">Copy Value To Clipboard</div>
				
			</div>
		</div>
	</div>


</t:main-layout>
