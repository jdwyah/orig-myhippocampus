<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "screencasts.title"/></title>
</head>

<body>
	
	
	<div class="middle-column-box-white">
		<div class="middle-column-box-title-green"><@spring.message "screencasts.header"/></div>

		 <#if message?exists>
			 <p class="message">${message}</p>
		 </#if>
		<p class="subheading">Available Screencasts</p>
		<ul>

		<li><a href="<@spring.url "/screencasts/5min/5min.html"/>">5 minute demo</a></li>		
		<li><a href="<@spring.url "/screencasts/Whirlwind/Whirlwind.html"/>">The extended tour</a></li>		
		<li><a href="<@spring.url "/screencasts/FirstSteps/FirstSteps.html"/>">First Steps</a> How to get started.</li>
		<li><a href="<@spring.url "/screencasts/TipsAndTricks/TipsAndTricks.html"/>">Tips & Tricks</a> Browser plugins, Island Properties & Changing a topic into an island </li>
		
<#--		<li><a href="http://www.myhippocampus.com/screencasts/Step2/Step2.html">First Steps</a></li>-->
		</ul>


		
	</div>
	
</body>
</html>