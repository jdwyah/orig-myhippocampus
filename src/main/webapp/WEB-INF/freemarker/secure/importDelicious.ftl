<html>
<#import "/spring.ftl" as spring/>
<#import "../common.ftl" as common/>
<head>
  <title><@spring.message "importDelicious.title"/></title>
</head>

<body>
	

	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		<@spring.message "importDelicious.header"/>
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	<form action="<@spring.url "/site/secure/importDelicious.html"/>" method="POST">
		<fieldset>
			<legend><@spring.message "importDelicious.legend"/></legend>				
								
			 <label for="deliciousName"><@spring.formInput "command.deliciousName"/><@common.regError/>
			 <@spring.message "importDelicious.username"/>
			 </label>		
			 <br>	
			 <label for="deliciousPass"><@spring.formPasswordInput "command.deliciousPass"/><@common.regError/>
			 <@spring.message "importDelicious.password"/>
			 </label>
			 <br>
			<input value="<@spring.message "importDelicious.submit"/>" type="submit">
		</fieldset>
	</form>		
	
  </div>
		
</body>
</html>