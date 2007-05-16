<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title>MyHippocampus - Signup</title>
</head>

<body>
   
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Sign-up now!</div>
        
    <form action="<@spring.url "/site/signup.html"/>" method="POST" id="signupForm">
      <table>
      
            
      		<tr>
      		<td colspan="2" valign="top">
      			<fieldset>
					<legend>OpenID</legend>
			 		<label for="openIDusername"><@spring.formInput "command.openIDusername", "class=\"openid-identifier\""/><@common.regError/>OpenID
			 		</label>
		 			<p>
		 			What is <a href="http://openid.net/">OpenID</a>?<p>
		 			Get an OpenID from <a href="https://www.myopenid.com/">myOpenID.com</a>
				</fieldset>
			</td>
			<td>
				or
			</td>
			<td>
			<fieldset>
					<legend>Standard</legend>
			 		<label for="username"><@spring.formInput "command.username"/><@common.regError/>User
			 		</label>
		 		<p>
			 		<label for="password"><@spring.formPasswordInput "command.password"/><@common.regError/>Password
			 		</label>
		 		<p>
		 			<label for="password2"><@spring.formPasswordInput "command.password2"/><@common.regError/>Password Again
			 		</label>
		 		<p>		 		
				</fieldset>
				
			</td>
      		</tr>
      	
		<#if hideSecretKey?exists>
			<@spring.formHiddenInput "command.randomkey" /><@common.regError/>			
		<#else>
			<tr><td>Special Key:</td><td><@spring.formInput "command.randomkey"/><@common.regError/></td></tr>
		</#if>

		<tr><td>Email:</td><td><@spring.formInput "command.email"/><@common.regError/></td></tr>

        <tr><td colspan='2'><input name="submit" type="submit" value="Create Account"></td></tr>
      </table>

    </form>
    
    <#if !hideSecretKey?exists>
	    <@spring.message "signup.message"/>
	    <p>
	    Add your name to the <a href="<@spring.url "/site/mailinglist.html"/>"> mailing list.</a>
	   </div>
	 </#if>

		
	</form>


</body>
</html>