<%@ include file="../../common/baseHeader.jsp"%>
<script type="text/javascript">
	function doSubmit(action, pwdNeeded) {
		if (pwdNeeded && $("#password").val() == '') {
			alert('Please enter your keypad passcode');
			return false;
		}
		
		$("#action").val(action);
		$.mobile.pageLoading();

		$.ajax({
			  url: '/panelControl.htm',
			  type: 'POST',
			  data: $("#frm").serialize(),
			  success: function() {
			  	setTimeout(function() {document.location = '/index.htm';}, 5000);
			  }
			});
	}

	
</script>
<form method="post" action="/panelControl.htm" id="frm">
<div data-role="fieldcontain">
    <label for="password"><strong>User Code:</strong></label>
    <input type="password" name="password" id="password" value="" />
</div>
<input type="hidden" value="" name="action" id="action"/>
</form>
<a href="#" data-theme="b" data-role="button" onclick="javascript:doSubmit('disarm', true);">DISARM</a>
<a href="#" data-theme="e" data-role="button" onclick="javascript:doSubmit('stay', true);">ARM STAY</a>
<a href="#" data-theme="e" data-role="button" onclick="javascript:doSubmit('night', false);">ARM NIGHT-STAY</a>
<a href="#" data-role="button" onclick="javascript:doSubmit('away', true);">ARM AWAY</a>
