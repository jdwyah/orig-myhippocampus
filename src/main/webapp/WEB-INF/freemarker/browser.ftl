<#import "commonGWT.ftl" as gwt/>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
	<head>
		
		<@gwt.css/>
		
    	    		
		<title>MyHippocampus Browser</title>
	
		<@gwt.mapScript/>
	
		 
	</head>
		<body onunload="GUnload()">
		<script language="JavaScript">
			var Vars = {
				page: "HippocampusBrowser",
				userID: "${foruser.id?c}",
				topicID: "${topic.id?c}"
			};
		</script>
		
		
		<script language='javascript' src='<@gwt.gwtURL "com.aavu.Interactive.nocache.js"/>'></script>

		<iframe id='__gwt_historyFrame' style='width:0;height:0;border:0'></iframe>
		<div id="slot1"></div>
		<div id="loading" class="loading"><p>Loading...</p></div>
		<div id="preload"></div>
	
		
<#--TODO move to decorator-->	
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-1880676-1";
urchinTracker();
</script>
	
	</body>
</html>