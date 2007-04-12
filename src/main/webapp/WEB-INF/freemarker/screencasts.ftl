<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "screencasts.title"/></title>
</head>

<body>
	
	
	<div class="middle-column-box-white">
	<div class="middle-column-box-title-green">Coming soon.     No really, coming soon!</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	 <p class="subheading">Thanks for your interest in MyHippocampus. You can get in touch with us by emailing jdwyah here at myhippocampus.com</p>
</div>
	
</body>
</html>