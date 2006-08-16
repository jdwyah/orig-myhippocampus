<html>
  <head>
    <title>Login</title>
  </head>

  <body>
    <h1>Login FTL</h1>

	<#if login_error?has_content>
      <font color="red">
        Your login attempt was not successful, try again.<BR><BR>
        Reason: ${login_error}
      </font>
    </#if>

    <form action="j_acegi_security_check" method="POST">
      <table>
        <tr><td>User:</td><td><input type='text' name='j_username' ></td></tr>
        <tr><td>Password:</td><td><input type='password' name='j_password'></td></tr>
        <tr><td><input type="checkbox" name="_acegi_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr>

        <tr><td colspan='2'><input name="submit" type="submit"></td></tr>
        <tr><td colspan='2'><input name="reset" type="reset"></td></tr>
      </table>

    </form>
	
	<p>
	<a href="j_acegi_logout">Logout</a>

  </body>
</html>
