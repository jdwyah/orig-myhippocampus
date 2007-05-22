<html>
<#import "/spring.ftl" as spring/>
<#import "../common.ftl" as common/>
<head>
  <title><@spring.message "searchResults.title"/></title>
</head>

<body>
	

	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		<@spring.message "searchResults.header"/>
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	<form action="<@spring.url "/site/secure/search.html"/>" method="POST">
		<fieldset>
			<legend><@spring.message "userPage.search.legend"/></legend>				
								
			 <label for="searchTerm"><@spring.formInput "command.searchTerm"/><@common.regError/>
			 <@spring.message "userPage.search.label"/>
			 </label>			
			<input value="<@spring.message "userPage.search.submit"/>" type="submit">
		</fieldset>
	</form>		

	<#if !results?exists || results?size < 1>
		<@spring.message "userPage.search.noresults"/>
	<#else>
	<#list results as result>
		<div class="searchRes">
			<p class="searchTitle subheading">
			${result_index + 1})  <@spring.message "userPage.search.score"/>${result.score}
				<a href="<@spring.url "/site/secure/showTopic.html?id=${result.topicID?c}"/>">${result.title?default("undefined")}</a>
			</p>
		   ${result.text?default("")} 
		</div>	
	</#list>
	</#if>	
	
	

	<p>	
    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/><img id="enterButton" width="200" height="160" src="<@spring.url "/img/enterMyHippocampus.png"/>"><br>Enter!</a>	
	</p>
	
	
  </div>
		
</body>
</html>