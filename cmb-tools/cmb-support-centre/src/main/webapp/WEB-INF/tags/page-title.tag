<%@ tag description="Template for header" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%@ attribute name="title" required="true" %>
<%@ attribute name="description" required="true" %>
<%@ attribute name="icon" required="false" %>

<div class="page-title">
	<div class="title" style="font-family: TradeGothicNextLTPro; font-size: 25px; margin-bottom: 10px;">${title}</div>
	<p>${description}</p>
	<div style="clear: both;"></div>
	<div class="ui yellow floating message">
		<p>${description}</p>
	</div>
	
</div>


