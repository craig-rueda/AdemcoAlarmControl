<%@ include file="../../common/baseHeader.jsp"%>
<c:if test="${not empty status}">
<c:choose>
	<c:when test="${status.faulted}"><c:set var="dividerTheme" value="e"/></c:when>
	<c:when test="${status.armed}"><c:set var="dividerTheme" value="red"/></c:when>
	<c:otherwise><c:set var="dividerTheme" value="green"/></c:otherwise>
</c:choose>
	<ul data-role="listview" data-inset="true" data-theme="d" data-dividertheme="${dividerTheme}"> 
		<li data-role="list-divider">Current Status</li>
	 	<li data-icon="info"><a href="/currentStatus.htm" data-rel="dialog">${status.msgData}</a></li>
	</ul>
</c:if>