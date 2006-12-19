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


    <div class="middle-column-box-title-green">
	Have an account? (you lucky dog!)
	</div>
	
		 <form action="j_acegi_security_check" method="POST">
		      <table>
        		<tr><td>User:</td><td><input type='text' name='j_username' ></td></tr>
		        <tr><td>Password:</td><td><input type='password' name='j_password'></td></tr>
        		<tr><td><input type="checkbox" name="_acegi_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr>

		        <tr><td colspan='2'><input name="login" value="Login" type="submit"></td></tr>
		      </table>
	    </form>	
	  	 	  
	</#if>
	

    <div class="middle-column-box-title-green">No account, but want to know when we go live?</div>
		<form action="<@spring.url "/site/interested.html"/>" method="POST">
		      <table>
        		<tr><td>Add your email:</td><td><input name="email" type="text"/></td></tr>

		        <tr><td colspan='2'><input value="Let me know when i can signup!" type="submit"></td></tr>        		
		      </table>
	    </form>		
    </div>
    
</body>
</html>