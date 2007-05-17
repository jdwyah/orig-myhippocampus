<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<html>
  <head>
    <title>Login</title>
  </head>

  <body>
  
	 <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Login</div>


	<#if login_error?has_content>
      <font color="red">
        Your login attempt was not successful, try again.<BR><BR>
        Reason: ${login_error}
      </font>
    </#if>


		<div id="loginBox">
		  <@common.loginForm/>
		</div>		 

	<p>
  		<#if !user?exists>
	      <@common.signupNow/>
	   </#if>
 	    
	</div>

  </body>
</html>
