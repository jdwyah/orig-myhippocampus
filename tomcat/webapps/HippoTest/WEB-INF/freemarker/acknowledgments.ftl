<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "acknowledgments.title"/></title>
</head>

<body>
	
	
	<div class="middle-column-box-title-green">
	  <p class="subheading"><@spring.message "acknowledgments.1"/></p>
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	  <p class="subheading"><@spring.message "acknowledgments.2"/></p>
	  <ul>
	  <li><a href="http://www.famfamfam.com/lab/icons/silk/">FamFamFam Silk Icon Set</a></li>
  	  <li><a href="http://simile.mit.edu/timeline/">Simile Timeline</a></li>
  	  </ul>
	  

	
</body>
</html>