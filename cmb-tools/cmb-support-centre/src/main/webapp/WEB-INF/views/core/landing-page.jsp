<%@page contentType="text/html;charset=UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:page activeActionId="home">

	<h3 class="page-title">Home</h3>

	<div class="page-bar">
		<t:breadcrumbs activeActionId="home"/>
	</div>
				
	<t:status-message status="${status}"/>
	
					
</t:page>

<script type="text/javascript">

</script>

