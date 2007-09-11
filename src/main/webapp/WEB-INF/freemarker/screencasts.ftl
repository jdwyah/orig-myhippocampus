<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "screencasts.title"/></title>
</head>

<body>
	
	
	<div class="middle-column-box-white">
		<div class="middle-column-box-title-green"><@spring.message "screencasts.header"/></div>

		 <#if message?exists>
			 <div class="message">${message}</div>
		 </#if>
		<p class="subheading">Available Screencasts</p>
		<ul>

		<li><a href="<@spring.url "/screencasts/GoogleDocs/GoogleDocs.html"/>">Google Docs & MyHippocampus</a></li>
		
		</ul>
		
		<#--
		<p>
<script type="text/javascript">
//digg_bgcolor = '#ff9900';
digg_skin = 'compact';
</script>
<script src="http://digg.com/tools/diggthis.js" type="text/javascript"></script>
		</p>-->
		
	<p>
 	<#if !user?exists>
	      <@common.signupNow/>
	</#if>
		
	</div>
	
</body>
</html>