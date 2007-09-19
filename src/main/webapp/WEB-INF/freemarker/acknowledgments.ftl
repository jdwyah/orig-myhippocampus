<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "acknowledgments.title"/></title>
</head>

<body>
	
	
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "acknowledgments.1"/></div>
	 <#if message?exists>
		 <div class="message">${message}</div>
	 </#if>

	  <p class="subheading"><@spring.message "acknowledgments.2"/></p>
	  <ul>
	  <li><a href="http://www.famfamfam.com/lab/icons/silk/">FamFamFam Silk Icon Set</a></li>
  	  <li><a href="http://www.opensymphony.com/compass/">Compass Java Search Engine</a></li>
  	  <li><a href="http://code.google.com/webtoolkit/">Google Web Toolkit - GWT</a></li>
  	  <li><img align=middle src="http://www.gwtwindowmanager.org/images/logo-mini.png"><a href="http://www.gwtwindowmanager.org/">GWT Window Manager</a></li>
  	  <li><a href="http://code.google.com/p/gwt-dnd/">GWT-DND Drag-and-Drop</a></li>
  	  <li><a href="http://www.shareicons.com/">ShareIcons</a>
  	  <li>Finally, a brief acknowledgment that the graphic design of the site is not all that it could be. We at MyHippocampus want you to know that the site doesn't look like this because this is what we think is gorgeous. Nor is it because we don't value great design. We really do. We just can't afford it right now ;)   	  
  	  </ul>
  	  
	  
	  </div>

	
</body>
</html>