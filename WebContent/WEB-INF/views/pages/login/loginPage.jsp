<%@ include file="../../common/baseHeader.jsp"%>

<jsp:include page="../../templates/mainTemplate.jsp">
	<jsp:param name="pageTitle" value="Please Login"/>
	<jsp:param name="mainContent" value="../blocks/login/loginCenter.jsp"/>
	<jsp:param name="hideHomeBtn" value="true"/>
	<jsp:param name="hideLogoutBtn" value="true"/>
</jsp:include>