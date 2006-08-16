
<#import "/spring.ftl" as spring/>

<html>
  <head>
    <title>Create User</title>
  </head>


	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro>


  <body>
    <h1>Create a new User</h1>


    <form action="<@spring.url "/site/createUser.html"/>" method="POST">
      <table>
        <tr><td>User:</td><td><@spring.formInput "command.username"/><@regError/></td></tr>
        <tr><td>Password:</td><td><@spring.formInput "command.password"/><@regError/></td></tr>
        <tr><td>Password:</td><td><@spring.formInput "command.password2"/><@regError/></td></tr>

        <tr><td colspan='2'><input name="submit" type="submit"></td></tr>
        <tr><td colspan='2'><input name="reset" type="reset"></td></tr>
      </table>

    </form>
	
	<p>

  </body>
</html>
