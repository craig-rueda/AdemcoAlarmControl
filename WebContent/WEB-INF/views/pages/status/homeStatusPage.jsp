<%@ include file="../../common/baseHeader.jsp"%>

<jsp:include page="../../templates/mainTemplate.jsp">
	<jsp:param name="pageTitle" value="System Status"/>
	<jsp:param name="mainContent" value="../blocks/status/homeStatusCenter.jsp"/>
	<jsp:param name="hideHomeBtn" value="true"/>
</jsp:include>