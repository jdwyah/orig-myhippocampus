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


  	<div id="whatIsWidgets">  		
		<#assign title = springMacroRequestContext.getMessage("index.whatIs.header")>  	
		<@common.box "boxStyleSm", "whatIs", title>
  			
			<#--
			<p class="subheading"> <a href="<@spring.url "/site/tour.html"/>"><@spring.message "index.whatIs.1"/></a><p>
			<p class="subheading"> <a href="<@spring.url "/site/screencasts.html"/>"><@spring.message "index.whatIs.2"/></a><p>
			<p class="subheading"> <a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "index.whatIs.3"/></a><p>
			-->
			<h3>
  			<ul>
			<li><a href="<@spring.url "/site/screencasts.html"/>"><strong><@spring.message "index.whatIs.2"/></strong></a></li>  			
  			<li><a href="<@spring.url "/site/tour.html"/>"><@spring.message "index.whatIs.1"/></a></li>			
  			<li><a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "index.whatIs.3"/></a></li>
			</ul>
			</h3>
  		</@common.box>  
  		<@common.box "boxStyleSm", "Intrigued", "Intrigued?">  		
  			<h2>
	    	<a href="<@spring.url "/site/signupIfPossible.html"/>"><@spring.message "login.signup"/></a>
	    	</h2>		    				
		</@common.box>
		
  	</div>
  	
  	<div id="browseWidgets">
  	  	
		<@common.box "boxStyle", "searchSection", "Search Public Accounts">
			<form action="<@spring.url "/site/search.html"/>" method="POST">
				<fieldset>
					<legend><@spring.message "userPage.search.legend"/></legend>				
								
			 		<label for="searchTerm"><input type=text><#--<@spring.formInput "command.searchTerm"/><@common.regError/>-->
			 			<@spring.message "userPage.search.label"/>
			 		</label>			
					<input value="<@spring.message "userPage.search.submit"/>" type="submit">
				</fieldset>
			</form>
		</@common.box>	
				
	
	<@common.box "boxStyle", "topTopicsSection", "MyHippocampus Zeitgeist">
		<ul>
		<#list topTopics as topic>
			<li>
			<@common.pngImage src="/img/rorsharch32px.png" width="32" height="32"/>
			<a href="<@spring.url "/site/browser.html#${topic.id}"/>">${topic.title}</a>
			User: ${topic.user.username} Updated: ${topic.lastUpdated}
			</li>		
		</#list>
		</ul>
		
	</@common.box>	
	
	<@common.box "boxStyle", "topWeblinksSection", "Most Popular Links">  	 	  
		<ul>
		<#list topWeblinks as weblink>
			<li>
			<a href="<@spring.url "/site/browser.html#${weblink.id}"/>">${weblink.title}</a>
			${weblink.uri}   User: ${weblink.user.username}
			</li>		
		</#list>
		</ul>
		
	</@common.box>	 	
	</div><!--end browseWidgets-->  

	
    
</body>
</html>