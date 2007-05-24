<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "browse.title"/> ${username} </title>
</head>

<body>
	
	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		${username} 
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	<#if (topics?size > 0)>
		<p class="subheading"><@spring.message "showTopic.tags"/><p>
		<#list topics as topicIdent>
			<div class="tag">
				<p>
					<a href="<@spring.url "/site/${username}/${topicIdent.topicTitle}"/>">${topicIdent.topicTitle}</a>
				</p>		  
			</div>	
		</#list>
	</#if>
	
	
	
	
	<#--
	<form action="<@spring.url "/site/secure/search.html"/>" method="POST">
		<fieldset>
			<legend><@spring.message "userPage.search.legend"/></legend>				
								
			 <label for="searchTerm"><@spring.formInput "command.searchTerm"/><@common.regError/>
			 <@spring.message "userPage.search.label"/>
			 </label>			
			<input value="<@spring.message "userPage.search.submit"/>" type="submit">
		</fieldset>
	</form>-->
	
  </div><!--boxwhite-->
		
</body>
</html>