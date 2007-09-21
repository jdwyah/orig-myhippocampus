<#macro gwtURL str>../../com.aavu.Interactive/${str}</#macro>

<#macro css>
<link href="<@gwtURL "themes/alphacube.css"/>" rel="stylesheet" type="text/css"></link>
  		<link href="<@gwtURL "themes/alphacube-off.css"/>" rel="stylesheet" type="text/css"></link>
    	<link href="<@gwtURL "themes/alphacube-green.css"/>" rel="stylesheet" type="text/css"></link>
		<link href="<@gwtURL "themes/alphacube-green-off.css"/>" rel="stylesheet" type="text/css"></link>    	
		<link href="<@gwtURL "themes/alphacube-blue.css"/>" rel="stylesheet" type="text/css"></link>
		<link href="<@gwtURL "themes/alphacube-blue-off.css"/>" rel="stylesheet" type="text/css"></link>    	
    	<link href="<@gwtURL "themes/overlay.css"/>" rel="stylesheet" type="text/css"></link>
</#macro>
<#macro mapScript>
<script language="JavaScript">
			<!--
			var getHead = function() {
            	return document.getElementsByTagName("head")[0];
        	};
	        var includeJavascriptFile = function(filename) {
    	        if (document.body == null) {
        	        document.write("<script src='" + filename + "' type='text/javascript'></script>");
            	} else {
                	var script = document.createElement("script");
	                script.type = "text/javascript";
    	            script.language = "JavaScript";
        	        script.src = filename;
            	    getHead().appendChild(script);
	            }
    	    };

		    var base="http://maps.google.com/maps?file=api&v=2&key=";
		    var key8888="ABQIAAAA6k005Q8HxzHfW1quTBpGbBTb-vLQlFZmc2N8bgWI8YDPp5FEVBTWhjnZZ1Ng4QF17g8wKpBPamJvLQ";
		    var key8080="ABQIAAAA6k005Q8HxzHfW1quTBpGbBTwM0brOpm-All5BF6PoaKBxRWWERQZI9AZyzT9VuBe2HlnaO-CGGJymg";
		    var keyHIPPO="ABQIAAAA6k005Q8HxzHfW1quTBpGbBTptlCPWuogQGEd6ELvQALNyQc3NBQeHOaiGCjJh1MrBRzgV2lZid74JA";

			   if(window.location.host.indexOf('localhost:8080') > -1){
				   	includeJavascriptFile(base+key8080);    			   	
    		   }else if(window.location.host.indexOf('localhost:8888') > -1){
    		   		includeJavascriptFile(base+key8888);    		   		
    		   }else {
    		   	    includeJavascriptFile(base+keyHIPPO);    		   		
    		   }   	
			   //window.alert("3 "+window.location.host+" infed "+window.location.host.indexOf('localhost:8888')+" "+window.location.host.indexOf('localhost:8080'));
    		   	   
    		 //-->
	    </script>
 
<style type="text/css">
			v\:* {
			 behavior:url(#default#VML);
			}
		</style>
</#macro>