
<#import "/spring.ftl" as spring/>

<html>
  <head>
    <title>List of users</title>
  </head>

  <body>
  
  
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Here are all ${users?size} users: </div>
	
    <table>
    <tr>
    <th>Name</th>
    <th>NotExpired</th>    
    <th>NotLocked</th>    
    <th>Cred Not Expired</th>        
    <th>Enabled</th>    
    <th>Invitations</th>
    <th>Super</th>        
    <th>Subscription</th>            
    <th></th>
    </tr>
    
	<#list users as user>
		<tr>
		<td>${user.username}</td>
	    <td>${user.accountNonExpired?string}</td>
	    <td>${user.accountNonLocked?string}</td>
	    <td>${user.credentialsNonExpired?string}</td>
	    <td><a href="<@spring.url "/site/secure/extreme/userManager.html?action=enable&user=${user.id?c}"/>"/>${user.enabled?string}</a></td>
		<td>${user.invitations}</td>					
		<td><a href="<@spring.url "/site/secure/extreme/userManager.html?action=supervisor&user=${user.id?c}"/>"/>${user.supervisor?string}</a></td>
		<td><a href="<@spring.url "/site/secure/extreme/userManager.html?action=delete&user=${user.id?c}"/>"/>Delete</a></td>				
		<td>${user.subscription.id}</td>
		</tr>
	</#list>
	</table>

	</div>

  </body>
</html>
