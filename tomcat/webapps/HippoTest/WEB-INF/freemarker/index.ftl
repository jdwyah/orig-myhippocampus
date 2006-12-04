<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus</title>
</head>

<body>
									
	<!--
      <p class="splash-word">hippocampus(n)</p>
      <p class="splash-definition">A part of the brain that plays an essential role in the formation of new memories about experienced events.</p>
      <p class="splash-definition">The name derives from its curved shape in coronal sections of the brain, which resembles a seahorse. </p>
      <p class="splash-definition">(Greek: hippo=horse, kampos=sea monster) (wikipedia)</p>

      <p class="splash-word">MyHippocampus(n)</p>
	  <p class="splash-definition">What you see when you look in the mirror of your mind.</p>
  	  <p class="splash-definition">Your personal <A class="hid" HREF="<@spring.url "/site/index2.html"/>">sea monster</A>.</p>

	 

	  <p class="subheading">#4 Install the Firefox plugin! (<A HREF="<@spring.url "/resources/myhippo.xpi"/>">click here</A>)</p>
-->

 <#if message?exists>
	 <p class="message">${message}</p>
 </#if>


    <#if user?exists>

	    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/>My Page</a>
		<br>
		<a href="j_acegi_logout">Logout</a>
	<#else>
<p class="subheading">Login:</p>
      <div class="middle-column-box-blue">
        <div class="middle-column-box-title-white">
		 <form action="j_acegi_security_check" method="POST">
		      <table>
        		<tr><td>User:</td><td><input type='text' name='j_username' ></td></tr>
		        <tr><td>Password:</td><td><input type='password' name='j_password'></td></tr>
        		<tr><td><input type="checkbox" name="_acegi_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr>

		        <tr><td colspan='2'><input name="login" value="Login" type="submit"></td></tr>
		      </table>
	    </form>
	   </div>	   
	  </div>
	  	 	  
	</#if>
	

<p class="subheading">Want to know when we go live?</p>	
		<form action="<@spring.url "/site/interested.html"/>" method="POST">
		      <table>
        		<tr><td>Add your email:</td><td><input name="email" type="text"/></td></tr>

		        <tr><td colspan='2'><input value="Let me know when i can signup!" type="submit"></td></tr>        		
		      </table>
	    </form>
		

</body>
</html>