<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>
<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer / Original design: Gerhard Erbes - gw@actamail.com/" />
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>" />
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/bcstyle.css"/>" />
  <title>MyHippocampus - Signup</title>
</head>

<body>
    
<!--
 <div class="tableholder">

		<table class="signup" cellspacing="0" cellpadding="0">

			<tr>
				<th class="details" style="border-left: none;"><strong>Select a plan &rarr;</strong><br />Signup takes just 1 minute</th>
				<th class="personal">Outrageous<br /><span class="price">$10/month</span><br /><a href="outrageous"><img width="68" height="21" src="<@spring.url "/img/signupbutton.gif"/>" alt="sign-up" /></a></th>
				<th class="personal">Personal<br /><span class="price">$5/month</span><br /><a href="Personal"><img width="68" height="21" src="<@spring.url "/img/signupbutton.gif"/>" alt="sign-up" /></a></th>
				<th class="personal">Free<br /><span class="price">Free</span><br /><a href="Free"><img width="68" height="21" src="<@spring.url "/img/signupbutton.gif"/>" alt="sign-up" /></a></th>
			</tr>
			<tr>
				<td class="item shaded"><strong>Total number of topics</strong></td>

				<td class="shaded projectnumber"><strong>Unlimited</strong></td>
				<td><strong>1000</strong></td>
				<td><strong>100</strong></td>

			</tr>
			<tr>
				<td class="item shaded"><strong>Email thoughts to yourself</strong><br /><small></small></td>
				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
				<td>&mdash;</td>
			</tr>
			<tr class="shaded">
				<td class="item shaded"><strong>30-day free trial</strong><br /><small>(cancel within 30 days &amp; you won't be charged)</small></td>

				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
				<td>Free forever</td>
			</tr>
			<tr>

				<td class="item"><strong>Use the Browser Plugins</strong></td>
				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
				<td><img src="<@spring.url "/img/bluecheck-bullet-14.gif"/>" alt="Yes" /></td>
			</tr>
			<tr>
				<td class="item">Usage may vary, but perfect for</td>
				<td class="perfect">Everybody really, but sometimes it takes a while to realize just how much you need something.</strong></td>
				<td class="perfect">The email thing is clutch. Really. Particularly if you've got a cool phone that does email.</td>
				<td class="perfect">The skeptic in all of us.</td>
			</tr>
		</table>
	  </div>

-->

  <!-- MIDDLE COLUMN -->
    <div id="middle-column">
	

	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro>


	<p>
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Sign-up now!</div>
        
    <form action="<@spring.url "/site/signup.html"/>" method="POST">
      <table>
        <tr><td>User:</td><td><@spring.formInput "command.username"/><@regError/></td></tr>
        <tr><td>Password:</td><td><@spring.formPasswordInput "command.password"/><@regError/></td></tr>
        <tr><td>Password Again:</td><td><@spring.formPasswordInput "command.password2"/><@regError/></td></tr>

        <tr><td colspan='2'><input name="submit" type="submit" value="Create Account"></td></tr>
      </table>

    </form>
	   </div>
	   
	   
	  </div>



</body>
</html>