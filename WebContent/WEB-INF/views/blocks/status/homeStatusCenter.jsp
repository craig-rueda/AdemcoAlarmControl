<%@ include file="../../common/baseHeader.jsp"%>
<script type="text/javascript">
	function updateStatus(showLoading) {
		if (showLoading)
			$.mobile.pageLoading();
		$.ajax({
			  url: '/currentStatusInclude.htm',
			  success: function(data) {
				$("#statusBlock").html(data);
		    	$("div[data-role=page]").page("destroy").page();
			  
			    //$('#statusBlock').page("destroy").page();
			    //$('#statusBlock').page();
			    $.mobile.pageLoading(true);
				//document.location = document.location;
			  }
			});
	}

	var interval = setInterval("updateStatus(false)",5000);

</script>
<div id="statusBlock">
<%@ include file="homeStatusInclude.jsp"%>
</div>
<p><a href="javascript:updateStatus(true);" data-role="button">Refresh</a></p>
<p>&nbsp;</p>
<a href="/zoneStatus.htm" data-role="button">View Zone Statuses</a>
<a href="/panelControl.htm" data-role="button" data-theme="e">Arm/Disarm System</a>
