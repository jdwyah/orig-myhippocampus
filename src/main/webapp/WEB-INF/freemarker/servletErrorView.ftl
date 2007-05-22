<html>
	<head>
	<#--decorator won't be called on some errors-->
  	<link rel="stylesheet" type="text/css" href="/css/style.css"/>
	
	<title>ERROR</title>		
	</head>
	
	
<body>
	
	
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">An Servlet Error Occurred</div>
	
	
	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	<#if exception?exists>

					${exception}
					<#list exception.stackTrace as st>
						${st}
					</#list>

	<#else>
			<#if javax?exists && javax.servlet?exists && javax.servlet.error?exists && javax.servlet.error.exception?exists>
				Servlet Exception:<p>				
					${javax.servlet.error.exception} <BR>
					${javax.servlet.error.exception.message?default("")} <BR>
					<#list javax.servlet.error.exception.stackTrace as st>
					${st}<BR>
					</#list>	

			<#else>
					No Error Message found !!!
			</#if>
					
	</#if>

	</div>
	
</body>
</html>