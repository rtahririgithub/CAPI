<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="identityManagement">

	<h3 class="page-title"><spring:message code="page.title.identityMgmt"/>  <small><spring:message code="page.title.identityMgmt.description"/></small></h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="identityManagement"/>
	</div>
				
	<t:status-message status="${status}"/>
	<t:binding-errors name="applicationIdentity"/>
				
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">Decrypted Application Identity</span>
					</div>
					<div class="actions">
						<button id="resetIdentityButton" class="btn yellow"> Reset </a>
						<button id="encryptButton" class="btn green"><i class="fa fa-lock"></i> Encrypt Identity</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="note note-info">
						Populate all required fields and press 'Encrypt Identities' button. A new encrypted identity configuration fragment will appear at 'Identity Configuration' bottom section.  
					</div>
				
				
				<form:form commandName="applicationIdentity" action="identity/encrypt" class="horizontal-form">
					<div class="row">
						<div class="col-md-9">
							<spring:bind path="applicationKey">
								<div class="form-group ${status.error ? 'has-error':''}">
									<label class="control-label">Application Lookup Key<span class="required"> * </span></label>
									<form:input path="applicationKey" cssClass="form-control"/>
									<span class="help-block">SOA application identifier</span>
								</div>
							</spring:bind>
						</div>
						<div class="col-md-3">
							<spring:bind path="applicationCode">
								<div class="form-group ${status.error ? 'has-error':''}">
									<label class="control-label">Application Code<span class="required"> * </span></label>
									<form:input path="applicationCode" cssClass="form-control"/>
									<span class="help-block">Knowbility application code</span>
								</div>
							</spring:bind>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label">Description</label>
								<form:input path="description" cssClass="form-control"/>
							</div>
						</div>
					</div>

					<br/>
					<div class="form-body">
						<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th class="col-sm-1">Environment</th>
								<th class="col-sm-4">Principal</th>
								<th class="col-sm-7">Credential</th>
								<th class="col-sm-7">Validate</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${applicationIdentity.entries}" var="entry" varStatus="status">
						<tr>
							<td style="vertical-align: middle; text-align: center;" nowrap="nowrap">${entry.environment.name}</td>
							<td><form:input path="entries[${status.index}].principal" cssClass="form-control"/></td>
							<td><form:input path="entries[${status.index}].decryptedCredentials" cssClass="form-control"/></td>
							<td align="center"><form:checkbox path="entries[${status.index}].validate" cssClass="form-control"/></td>
						</tr>
						</c:forEach>
						</tbody>
						</table>
					</div>
				</form:form>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-microphone font-blue-hoki"></i>
						<span class="caption-subject bold font-blue-hoki uppercase">Identity configuration</span>
					</div>
					<div class="actions">
						<button id="resetButton" class="btn yellow"> Reset </a>
						<button id="decryptButton" class="btn green"><i class="fa fa-unlock"></i> Decrypt Identity</a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="note note-info">
						Copy and paste a single encrypted application identity content from conf/identities.xml into the text area below and click 'Decrypt' button.<br/>
						The decrypted entries will appear in the top section.
					</div>
					<form:form commandName="decryptionForm" action="identity/decrypt" cssClass="horizontal-form">
						<div class="form-body">
							<spring:bind path="sourceContent">
							<div class="form-group ${status.error ? 'has-error':''}">
								<label class="control-label">Encrypted source:<span class="required"> * </span></label>
								<form:textarea rows="10" path="sourceContent" type="text" cssClass="form-control" cssStyle="font-size: 11px;"/>
							</div>
							</spring:bind>
						</div>
					</form:form>
					<!-- 
					<div class="pull-right">
						<button id="copyButton" class="btn btn-sm blue-madison"><i class="fa fa-copy"></i> Copy To Clipboard</a>
					</div>
					<div class="clearfix"></div>
					 -->
				</div>
			</div>
		</div>
	</div>
		
</t:page>

<script type="text/javascript">

jQuery(document).ready(function() {
	
	
	$('#resetIdentityButton').click(function(event){
		$('#applicationIdentity').trigger('reset');
	});

	$('#encryptButton').click(function(event){
		$('#applicationIdentity').submit();
	});
	
	$('#decryptButton').click(function(event){
		$('#decryptionForm').submit();
	});
	
	
});

</script>

