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

		<ul>
		<li><a href="http://www.myhippocampus.com/screencasts/FirstSteps/FirstSteps.html">First Steps</a></li>
<#--		<li><a href="http://www.myhippocampus.com/screencasts/Step2/Step2.html">First Steps</a></li>-->
		</ul>


		 <p class="subheading">Thanks for your interest in MyHippocampus. You can get in touch with us by emailing jdwyah here at myhippocampus.com</p>
	</div>
	
</body>
</html>