
<#import "/spring.ftl" as spring/>

<html>
  <head>
    <title>Scripts</title>
  </head>

  <body>
  
  
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Scripts</div>

   
   
 <#if message?exists>
	<div style="z-index: 99; position: absolute; left: 200px;">
	 <p class="message">${message}</p>
	</div>
 </#if>			  	 	  
 
	 <ul>
	    <li><a href="<@spring.url "/site/secure/extreme/scripts.html?action=upgrade"/>"/>Upgrade</a>							
	 </ul>
	</div>

  </body>
</html>
