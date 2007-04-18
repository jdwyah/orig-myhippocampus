<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus - Signup</title>
</head>

<body>
    

	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro>


      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Sign-up now!</div>
        
    <form action="<@spring.url "/site/signup.html"/>" method="POST">
      <table>
        <tr><td>User:</td><td><@spring.formInput "command.username"/><@regError/></td></tr>
        <tr><td>Password:</td><td><@spring.formPasswordInput "command.password"/><@regError/></td></tr>
        <tr><td>Password Again:</td><td><@spring.formPasswordInput "command.password2"/><@regError/></td></tr>
		
		<tr><td>Special Key:</td><td><@spring.formInput "command.randomkey"/><@regError/></td></tr>

        <tr><td colspan='2'><input name="submit" type="submit" value="Create Account"></td></tr>
      </table>

    </form>
	   </div>
	   
	   


</body>
</html>