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
		<li><a href="http://www.myhippocampus.com/screencasts/Whirlwind/Whirlwind.html">The Whirlwind Tour</a></li>		
		<li><a href="http://www.myhippocampus.com/screencasts/FirstSteps/FirstSteps.html">First Steps</a></li>
		<li><a href="http://www.myhippocampus.com/screencasts/TipsAndTricks/TipsAndTricks.html">Tips & Tricks</a></li>
<#--		<li><a href="http://www.myhippocampus.com/screencasts/Step2/Step2.html">First Steps</a></li>-->
		</ul>


		
	</div>
	
</body>
</html>