<%@ include file="../../common/baseHeader.jsp"%>
<c:choose>
<c:when test="${not empty statusList && fn:length(statusList) > 0}">
	<ul data-role="listview" data-inset="true" data-theme="d" data-dividertheme="e"> 
		<li data-role="list-divider">Zones:</li>
		<c:forEach items="${statusList}" var="status">
			<li data-icon="info"><a href="/currentStatus.htm" data-rel="dialog">${status.msgData}</a></li>
		</c:forEach>	
	</ul>
</c:when>
<c:otherwise>
	<ul data-role="listview" data-inset="true" data-theme="d" data-dividertheme="e"> 
		<li data-role="list-divider">Zones:</li>
		<li data-icon="info">All zones ready...</li>
	</ul>
</c:otherwise>
</c:choose>
