<%@ include file="../../common/baseHeader.jsp"%>

<jsp:include page="../../templates/mainTemplate.jsp">
	<jsp:param name="pageTitle" value="Current Status"/>
	<jsp:param name="dataTheme" value="e"/>
	<jsp:param name="mainContent" value="../blocks/status/currentStatusCenter.jsp"/>
</jsp:include>