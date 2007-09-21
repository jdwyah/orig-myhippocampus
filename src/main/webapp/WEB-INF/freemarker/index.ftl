<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "site"/></title>
</head>

<body>					
     
   
 <#if message?exists>
	<div style="z-index: 99; position: absolute; left: 200px;">
	 <div class="message">${message}</div>
	</div>
 </#if>			  	 	  

  
  	<div id="whatIsIsland" class="island boxStyle">
		<div class="islandOverlay">
  			<p class="islandHeader"><@spring.message "index.whatIs.header"/></p>

			<#--
			<p class="subheading"> <a href="<@spring.url "/site/tour.html"/>"><@spring.message "index.whatIs.1"/></a><p>
			<p class="subheading"> <a href="<@spring.url "/site/screencasts.html"/>"><@spring.message "index.whatIs.2"/></a><p>
			<p class="subheading"> <a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "index.whatIs.3"/></a><p>
			-->
  			<ul>
			<li><a href="<@spring.url "/site/screencasts.html"/>"><strong><@spring.message "index.whatIs.2"/></strong></a></li>  			
  			<li><a href="<@spring.url "/site/tour.html"/>"><@spring.message "index.whatIs.1"/></a></li>			
  			<li><a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "index.whatIs.3"/></a></li>
			</ul>
  		</div>  
  		<div class="islandOverlay">		    		    
			    <h2 id="tryItNow">
	    		<a href="<@spring.url "/site/signupIfPossible.html"/>"><@spring.message "login.signup"/></a>
	    		</h2>		    
		</div>		
  	</div>
  
	<div id="browseIsland" class="island boxStyle">
	
		<p>Search<p>
		Search: <input type="text"/>
		<p>Browse</p>
		
		
		<ul>
		<#list topTopics as topic>
			<li>
			<a href="<@spring.url "/site/browser.html#${topic.id}"/>">${topic.title}</a>
			User: ${topic.user.username}
			</li>		
		</#list>
		</ul>
		
		
	</div>	  	 	  
	  	 	  
	<div id="browseLinksIsland" class="island boxStyle">
	
		<p>Most Popular Links<p>
		
		<ul>
		<#list topWeblinks as weblink>
			<li>
			<a href="<@spring.url "/site/browser.html#${weblink.id}"/>">${weblink.title}</a>
			${weblink.uri}   User: ${weblink.user.username}
			</li>		
		</#list>
		</ul>
		
	</div>	  	 	  

	
    
</body>
</html>