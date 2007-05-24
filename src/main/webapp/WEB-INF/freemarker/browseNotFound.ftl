<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "browseError.title"/></title>
</head>

<body>
	
	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		<@spring.message "showTopic.title"/>
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


  </div><!--boxwhite-->
		
</body>
</html>