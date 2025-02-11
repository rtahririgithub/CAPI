<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="activeActionId" required="true" rtexprvalue="true"%>

<ul class="page-breadcrumb">
	<li>
		<i class="fa fa-home"></i> <a href="index.html">Home</a> <i class="fa fa-angle-right"></i>
	</li>
	<c:forEach items="${actionManager.getActionChain(activeActionId)}" var="action" varStatus="status">
		<li>
			<a href="${action.url}">${action.title}</a> 
			<c:if test="${not status.last}"> 
				<i class="fa fa-angle-right"></i>
			</c:if>
		</li>
	</c:forEach>
</ul>
