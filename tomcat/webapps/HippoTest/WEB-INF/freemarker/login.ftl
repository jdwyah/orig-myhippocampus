<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<#import "/spring.ftl" as spring/>

<html>
	<head>
		<title>Login</title>
	</head>

	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro>

	<body>


	<#if reason?exists>
		Last Login Failed<P>
		Reason: ${reason}<P>
	</#if>

    <form action="login.html" method="POST">
      <table>
        <tr><td>User:</td><td><@spring.formInput "command.username"/><@regError/></td></tr>
        <tr><td>Password:</td><td><@spring.formPasswordInput "command.password"/><@regError/></td></tr>
     <!--   <tr><td><input type="checkbox" name="_acegi_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr>-->

        <tr><td colspan='2'><input name="submit" type="submit"></td></tr>
        <tr><td colspan='2'><input name="reset" type="reset"></td></tr>
      </table>

    </form>

<!--
<form method="post" id="loginForm" action="j_acegi_security_check">
<fieldset>
<ul>

    <li>
            Username <span class="req">*</span><@spring.formInput "command.username"/><@regError/>
    </li>

    <li>
        	Password <span class="req">*</span><@spring.formPasswordInput "command.password"/><@regError/>
    </li>

</ul>
</fieldset>

    <input title="Clicking this button will log you in" class="button" type="submit" value="Login">
    
</form>-->

	</body>
</html>

 