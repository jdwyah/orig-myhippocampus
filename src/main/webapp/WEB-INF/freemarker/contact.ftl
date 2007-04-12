<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "contact.title"/></title>
</head>

<body>
	
	
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "contact.1"/></div>
	
	
	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	  <p class="subheading"><@spring.message "contact.1.0"/></p>

	</div>
	
</body>
</html>