<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "site"/></title>
</head>



<#macro showTopic topic instanceNotType >
<li>
	<div>
	<h3><@common.browseLink topic/>			
	</h3>
	<div class="info">
			<span class="type">User:</span> <@common.userLink topic.user/> <span class="type">Updated: ${topic.lastUpdated}</span>
		</div>
	
	<div class="subTopics">
	<ul>
	
	<#if instanceNotType>
		<#assign subset=topic.instances/>
		<#else>
		<#assign subset=topic.topics/>
	</#if>
	<#list subset as subTopic>		
		<li><@common.browseLink subTopic.topic/></li>
		<#if subTopic_index gt 10>| ...<#break></#if>
		<#if subTopic_has_next>|</#if>		
	</#list>
	</ul>
	</div>
	
	</div>
</li>		
</#macro>

<body>					
        
 <#if message?exists>
	<div style="z-index: 99; position: absolute; left: 200px;">
	 <div class="message">${message}</div>
	</div>
 </#if>			  	 	  


  	
  	<div id="side1">
  	  	
		<@common.box "boxStyle", "searchSection", "Search Public Accounts">
			<form action="<@spring.url "/site/publicSearch.html"/>" method="POST">
				<fieldset>
					<legend><@spring.message "userPage.search.legend"/></legend>				
								
			 		<label for="searchTerm"><input type="text" id="searchTerm" name="searchTerm">
			 			<@spring.message "userPage.search.label"/>
			 		</label>			
					<input value="<@spring.message "userPage.search.submit"/>" type="submit">
				</fieldset>
			</form>
		</@common.box>	
				
	
	<@common.box "boxStyle", "topTopicsSection", "MyHippocampus Zeitgeist:  Knowledge Made Public">
		<ul>
		<#list topTopics as topic>
			<@showTopic topic, true/>
		</#list>
		</ul>
		
	</@common.box>	
	
	<@common.box "boxStyle", "topWeblinksSection", "Most Popular Links">  	 	  
		<ul>
		<#list topWeblinks as weblink>
			<@showTopic weblink, false/>					
		</#list>
		</ul>
		
	</@common.box>	 	
	
	</div><!--end browseWidgets-->  

	
  	<div id="side2">  		
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
			<li><a href="<@spring.url "/site/seahorse.html"/>">Seahorse?</a></li>
			</ul>
			</h3>
  		</@common.box>  
  		<@common.box "boxStyleSm", "Intrigued", "Want your own?">  		
  			<h2>
	    	<a href="<@spring.url "/site/signupIfPossible.html"/>"><@spring.message "login.signup"/></a>
	    	</h2>		    				
		</@common.box>
			
	
  	</div>
    
</body>
</html>