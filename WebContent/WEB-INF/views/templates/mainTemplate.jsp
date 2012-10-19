<%@ include file="../common/baseHeader.jsp"%>
<!DOCTYPE html> 
<html> 
<head>
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.5.min.js"></script>
	<script type="text/javascript" src="/js/main.js"></script>
 
	<title><c:out value="${param.pageTitle}"/></title> 
	<link rel="stylesheet" href="http://code.jquery.com/mobile/1.0a3/jquery.mobile-1.0a3.min.css" />
	<link rel="stylesheet" href="/css/main.css" />
	
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.0a3/jquery.mobile-1.0a3.min.js"></script>
	<%@ include file="../common/headerInclude.jsp"%>
</head> 
<body> 

<div data-role="page" data-theme="${empty param.dataTheme ? 'a' : param.dataTheme}">

	<div data-role="header" data-backbtn="false">
		<c:if test="${empty param.hideHomeBtn}"><a href="/" data-icon="home" class="ui-btn-left">Home</a></c:if>
		<h1>${param.pageTitle}</h1>
		<c:if test="${empty param.hideLogoutBtn}"><a href="/logout.htm" data-icon="check" class="ui-btn-right">Logout</a></c:if>
	</div><!-- /header -->

	<div data-role="content">	
		<p><jsp:include page="${param.mainContent}"/></p>		
	</div><!-- /content -->

	<c:if test="${not empty param.footerContent}">
	<div data-role="footer">
		<jsp:include page="${param.footerContent}"/>
	</div><!-- /footer -->
	</c:if>
</div><!-- /page -->

</body>
</html>

