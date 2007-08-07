<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "acknowledgments.title"/></title>
</head>

<body>
	
	
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "acknowledgments.1"/></div>
	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	  <p class="subheading"><@spring.message "acknowledgments.2"/></p>
	  <ul>
	  <li><a href="http://www.famfamfam.com/lab/icons/silk/">FamFamFam Silk Icon Set</a></li>
  	  <li><a href="http://www.opensymphony.com/compass/">Compass Java Search Engine</a></li>
  	  <li><a href="http://code.google.com/webtoolkit/">Google Web Toolkit - GWT</a></li>
  	  <li><img align=middle src="http://www.gwtwindowmanager.org/images/logo-mini.png"><a href="http://www.gwtwindowmanager.org/">GWT Window Manager</a></li>
  	  </ul>
	  
	  </div>

	
</body>
</html>