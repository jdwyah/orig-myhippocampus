<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "browse.title"/> ${username} / ${topic.title}</title>
</head>

<body>
	
	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		${username} / ${topic.title}
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

	<#if topic.latestEntry.data?exists>
		<p class="subheading"><@spring.message "showTopic.entry"/></p>
		${topic.latestEntry.dataWithoutBodyTags}
	</#if>

	<#if (topic.typesAsTopics?size > 0)>
		<p class="subheading"><@spring.message "showTopic.tags"/><p>
		<#list topic.typesAsTopics as tag>
			<div class="tag">
				<p>
					<a href="<@spring.url "/site/${username}/${tag.title}"/>">${tag.title}</a>
				</p>		  
			</div>	
		</#list>
	</#if>
	
		
	<#if (onThisIsland?size > 0)>
		<p class="subheading"><@spring.message "showTopic.onThisIsland"/><p>
		<#list onThisIsland as topicIdent>
			<div class="topic">
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