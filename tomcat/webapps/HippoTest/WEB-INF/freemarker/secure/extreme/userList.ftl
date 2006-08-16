
<#import "/spring.ftl" as spring/>

<html>
  <head>
    <title>List of users</title>
  </head>

  <body>
    <h1>Here are all the users:</h1>

    <table>
    <tr>
    <th>Name</th>
    <th>NotExpired</th>    
    <th>NotLocked</th>    
    <th>Cred Not Expired</th>        
    <th>Enabled</th>    
    <th>Password</th>
    <th>Super</th>        
    <th></th>
    </tr>
    <#macro isSuper user>
	  	<#list user.authorities as auth>
		  	<#assign a = auth.authority/>
			<#if a == "ROLE_SUPERVISOR">Yes
				<#return/>
			</#if>			
		</#list>
		No
    </#macro>
    
	<#list users as user>
		<tr>
		<td>${user.username}</td>
	    <td>${user.accountNonExpired?string}</td>
	    <td>${user.accountNonLocked?string}</td>
	    <td>${user.credentialsNonExpired?string}</td>
	    <td><a href="<@spring.url "/site/secure/extreme/userManager.html?action=enable&user=${user.id?c}"/>"/>${user.enabled?string}</a></td>
		<td>${user.password}</td>					
		<td><a href="<@spring.url "/site/secure/extreme/userManager.html?action=supervisor&user=${user.id?c}"/>"/><@isSuper user/></a></td>
		<td><a href="<@spring.url "/site/secure/extreme/userManager.html?action=delete&user=${user.id?c}"/>"/>Delete</a></td>				
		</tr>
	</#list>
	</table>

	<p>

  </body>
</html>
