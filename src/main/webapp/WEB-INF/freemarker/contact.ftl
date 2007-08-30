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
	  	<p><a href="mailto:help@myhippocampus.com">help@myhippocampus.com</a></p>
	  <p class="subheading"><@spring.message "contact.1.1"/></p>
	  	<p>Jeff Dwyer - <a href="mailto:jdwyah@myhippocampus.com">jdwyah@myhippocampus.com</a></p>
	  <p class="subheading"><@spring.message "contact.1.2"/></p>
	  	<p>
	  	MyHippocampus<br>
	  	18 Melview Ridge<br>
	  	Norwich, VT 05055<br>
	  	<p>
	</div>
	
</body>
</html>