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
	<table cellspacing="10px">
	<tr>
	<th>
		Del.icio.us Account
	</th>
	<th>
	    Del.icio.us XML Upload
	</th>
	</tr>
	<tr>
	<td valign=top>		
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
	<em>
	Your credentials are not stored, but they do go through our servers. If you are (justifiably) paranoid, please use the XML import.
	</em>
	</td>
	
	<td valign="top">
	<form action="<@spring.url "/site/secure/import.html?type=deliciousXML"/>" method="POST">
		<fieldset>
			<legend><@spring.message "importDelicious.legendXML"/></legend>				
								
			 <label for="deliciousXMLString"><@spring.formTextarea "command.deliciousXMLString"/><@common.regError/>
			 <@spring.message "importDelicious.deliciousXML"/>
			 </label>		
			
			<input value="<@spring.message "importDelicious.submitXML"/>" type="submit">
		</fieldset>
	</form> 
	</td>
	<td>
	To use this, 	 
	<ol>
	<li>Navigate to <a href="https://api.del.icio.us/v1/posts/all?">https://api.del.icio.us/v1/posts/all?</a>
	<li>Del.icio.us will prompt you for your password over a secure https connection.</li>
	<li>Del.icio.us will display your bookmarks in XML</li>
	<li>View->Source</li><li>Select All</li><li>Copy</li><li>Paste the results here</li></ol> 
	<em>Note: if you do not view source, many browser will insert funny things into the xml which will cause this to fail. Additionally, the XML import will not respect any bundles you've created.</em>
	</td>
	</tr>
	</table>
	
	
	<div class="middle-column-box-title-green">Google Docs & Spreadsheets</div>		
	<#--<h3>This Google service is down for the time being. See <a href="http://groups.google.com/group/Google-Docs-Data-APIs/browse_thread/thread/83bf5b9650af7ae8">this thread</a> for the status of the fix<h3>-->		
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
	<em>
	<br>
	For standard Google accounts (ie gmail), authorize MyHippocampus to access your docs.
	<br>
	MyHippocampus never sees your credentials.
	</em>
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
		<em>
		The Google API doesn't support the credential free import for Apps For Your Domain yet. Your credentials are not saved, but they will go through our servers.
		</em> 
	</form>
	</td>
	</tr>
	</table>
	
	
  </div>
		
</body>
</html>