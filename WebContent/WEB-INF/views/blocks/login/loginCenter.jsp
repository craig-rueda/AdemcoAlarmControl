<%@ include file="../../common/baseHeader.jsp"%>
<form method="post" action="/login.htm" id="frm">
<div data-role="fieldcontain">
	<c:if test="${not empty error}">
		<h3>Invalid username/password</h3>
	</c:if>
	<p>
    <label for="userName"><strong>User Name:</strong></label>
    <input type="email" name="userName" id="userName" value="${userName}" />
	</p>
	<p>
    <label for="password"><strong>Password:</strong></label>
    <input type="password" name="password" id="password" value="" />
	</p>
</div>
<input type="submit" value="Login" />
</form>
