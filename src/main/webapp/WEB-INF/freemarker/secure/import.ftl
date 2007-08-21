<html>
<#import "/spring.ftl" as spring/>
<#import "../common.ftl" as common/>
<head>
  <title><@spring.message "import.title"/></title>
</head>

<body>
	

	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		<@spring.message "import.header"/>
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	<form action="<@spring.url "/site/secure/import.html?type=delicious"/>" method="POST">
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
	<p>
	<form action="<@spring.url "/site/secure/import.html?type=google"/>" method="POST">
		<fieldset>
			<legend><@spring.message "importGoogle.legend"/></legend>				
								
			 <label for="googleName"><@spring.formInput "command.googleName"/><@common.regError/>
			 <@spring.message "importGoogle.username"/>
			 </label>		(JohnDoe@gmail.com or JohnDoe@MyGoogleAppsDomain.com if you have GoogleApps)
			 <br>	
			 <label for="googlePass"><@spring.formPasswordInput "command.googlePass"/><@common.regError/>
			 <@spring.message "importGoogle.password"/>
			 </label>
			 <br>
			<input value="<@spring.message "importGoogle.submit"/>" type="submit">
		</fieldset>
	</form>
	<p>		
	Your password is not saved on our servers.
	</p> 
	
  </div>
		
</body>
</html>