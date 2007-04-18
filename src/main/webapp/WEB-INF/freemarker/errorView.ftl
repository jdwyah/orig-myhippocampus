<html>
	<head>
	<title>ERROR</title>		
	</head>
	
	
<body>
	
	
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">An Error Occurred</div>
	
	
	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	<#if exception?exists>

					${exception}
					<#list exception.stackTrace as st>
						${st}
					</#list>

	<#else>

					No Error Message found !!!

	</#if>

	</div>
	
</body>
</html>
