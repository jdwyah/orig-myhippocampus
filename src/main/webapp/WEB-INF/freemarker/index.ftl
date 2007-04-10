<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus</title>
</head>

<body>
 <#if message?exists>
	 <p class="message">${message}</p>
 </#if>							
      <p class="splash-word">hippocampus(n)</p>
      <p class="splash-definition">A part of the brain that plays an essential role in the formation of new memories about experienced events.</p>
      <p class="splash-definition">The name derives from its curved shape in coronal sections of the brain, which resembles a seahorse. </p>
      <p class="splash-definition">(Greek: hippo=horse, kampos=sea monster)</p>

      <p class="splash-word">MyHippocampus(n)</p>
	  <p class="splash-definition">A personal intellectual tool, that helps capture your insight into the world around you.</p>
      <p class="splash-definition">Read more in the <a href="<@spring.url "/site/manifesto.html"/>"/>MyHippocampus Manifesto</a></p>
    <div class="middle-column-box-white">

    <#if user?exists>

	    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/>Goto My Page!</a>
		<br>
		<a href="j_acegi_logout">Logout</a>
	<#else>

	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro>
  
	
	<div id="loginBox">

		 <div>
		 <form id="loginForm" action="j_acegi_security_check" method="POST">
			<fieldset>

				<legend><@spring.message "login.1.header"/></legend>

				 <label for="j_username"><input type='text' name='j_username' id = 'j_username'><@spring.message "login.1.user"/>
				 </label>
			 <p>
				 <label for="j_password"><input type='password' name='j_password' id = 'j_password'><@spring.message "login.1.pass"/>
				 </label>
			 <p>
			 	<label for="_acegi_security_remember_me"><input type="checkbox" name="_acegi_security_remember_me"><@spring.message "login.1.dontask"/>
				 </label>
			 <p>
			 <input name="login" value="<@spring.message "login.1.button"/>" type="submit">

			</fieldset>
		 </form>	
		 </div>
		 
		  <div id="loginSection">
		    <div>
		    <form action="<@spring.url "/site/interested.html"/>" method="POST">
			<fieldset>
				<legend><@spring.message "login.2.header"/></legend>
				
					<@spring.message "login.2.addemail"/><@spring.formInput "command.email"/><@regError/>					
					<input value="<@spring.message "login.2.addemail"/>" type="submit">
			</fieldset>
		    </form>		
		    </div>
		  </div>
		 		 
	</div>	  	 	  
	  	 	  
	</#if>
	
    
</body>
</html>