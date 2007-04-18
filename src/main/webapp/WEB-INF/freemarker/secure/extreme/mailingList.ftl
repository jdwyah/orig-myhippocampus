
<#import "/spring.ftl" as spring/>

<html>
  <head>
    <title>Mailing List</title>
  </head>

  <body>
  
  
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Here's the wait list:</div>
	
    <table>
    <tr>
    <th>email</th>
    <th>key</th>    
    <th>inviter</th>    
    <th>signedup</th>    
    <th>mail ok</th>        
    <th></th>
    </tr>

	<#list list as entry>
		<tr>
		<td>${entry.email?default("")}</td>
	    <td>${entry.randomkey?default("")}</td>
	    <td><#if entry.inviter?exists>${entry.inviter.username}<#else>None</#if></td>
	    <td><#if entry.signedUpUser?exists>${entry.signedUpUser.username}<#else>None</#if></td>
	    <td>${entry.sentEmailOk?string}</td>
		<td><a href="<@spring.url "/site/secure/extreme/mailingList.html?action=resendInvite&entry=${entry.id?c}"/>"/>Send Invite</a></td>				
		</tr>
	</#list>
	</table>
	
	

	</div>

  </body>
</html>
