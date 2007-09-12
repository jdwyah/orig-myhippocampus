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

	<div class="middle-column-box-title-green">Del.icio.us Links</div>
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
	
	<div class="middle-column-box-title-green">Google Docs & Spreadsheets</div>				
	<table>
	<tr>
	<th>
		Basic Google Account
	</th>
	<th>
	    Google Apps For Your Domain Account
	</th>
	</tr>
	<tr>
	<td valign=top>		
	<br> 
	<button onclick="location.href='${googleRequestURL}'">Import My Google Docs</button>
	<br>
	For standard Google accounts (ie gmail), authorize MyHippocampus to access your docs.
	<br>
	MyHippocampus never sees your credentials.
	</p>
	</td>
	<td>	
	<form action="<@spring.url "/site/secure/import.html?type=google"/>" method="POST">
		<fieldset>						
								
			 <label for="googleName"><@spring.formInput "command.googleName"/><@common.regError/>
			 <@spring.message "importGoogle.username"/>
			 </label>		(username@myGoogleAppsDomain.com)
			 <br>				 
			 <label for="googlePass"><@spring.formPasswordInput "command.googlePass"/><@common.regError/>
			 <@spring.message "importGoogle.password"/>
			 </label>
			 <br>
			<input value="<@spring.message "importGoogle.submit"/>" type="submit">
		</fieldset>
		<p>
		The Google API doesn't support the credential free import for Apps For Your Domain yet. Your credentials are not saved, but they will go through our servers. 
	</form>
	</td>
	</tr>
	</table>
	
	
  </div>
		
</body>
</html>