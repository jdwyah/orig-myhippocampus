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

    <form action="j_acegi_security_check" method="POST">
      <table>
        <tr><td>User:</td><td><input type='text' name='j_username' ></td></tr>
        <tr><td>Password:</td><td><input type='password' name='j_password'></td></tr>
        <tr><td><input type="checkbox" name="_acegi_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr>

        <tr><td colspan='2'><input name="submit" value="Login" type="submit"></td></tr>
      </table>

    </form>

	</div>

  </body>
</html>
