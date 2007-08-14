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
			 <p class="message">${message}</p>
		 </#if>
		<p class="subheading">Available Screencasts</p>
		<ul>

		<li><a href="<@spring.url "/screencasts/5min/5min.html"/>">5 minute demo</a> Watch this first!</li>		
		<li><a href="<@spring.url "/screencasts/Whirlwind/Whirlwind.html"/>">The extended tour</a></li>		
		<li><a href="<@spring.url "/screencasts/FirstSteps/FirstSteps.html"/>">First Steps</a> How to get started.</li>
		<li><a href="<@spring.url "/screencasts/TipsAndTricks/TipsAndTricks.html"/>">Tips & Tricks</a> Browser plugins, Island properties & Changing a topic into an island </li>
		
<#--		<li><a href="http://www.myhippocampus.com/screencasts/Step2/Step2.html">First Steps</a></li>-->
		</ul>
		
		<p>
		(Please Note, these screencasts are for the Beta 1 release of MyHippocampus.<br>
		 The new Beta 2 has some significant differences, but we haven't had a chance to make the screencasts yet) 

	<p>
 	<#if !user?exists>
	      <@common.signupNow/>
	</#if>
		
	</div>
	
</body>
</html>