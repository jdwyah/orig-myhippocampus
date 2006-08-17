<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer / Original design: Gerhard Erbes - gw@actamail.com/" />
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>
  <title>MyHippocampus</title>
</head>

<body>
  <div id="wrap">

    <!-- HEADER -->
	  <!-- Background -->
    <div id="header-section">
		  <a href="#"><img id="header-background-left" src="<@spring.url "/img/img_logo.jpg"/>" alt=""/></a>
      <img id="header-background-right" src="<@spring.url "/img/img_header.jpg"/>" alt=""/>
		</div>

	  <!-- Navigation -->
    <div id="header">
      <ul>
        <li class="selected">Home</li>
        <li><a href="tour.html">Tour</a></li>
        <li><a href="manifesto.html">Manifesto</a></li>
		<#if user?exists>
	        <li><a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/>My Page</a></li>
        <#else>
        	<li><a href="signup.html">Sign up</a></li>  
        </#if>
      </ul>
    </div>

	  <!-- MIDDLE COLUMN -->
    <div id="middle-column">
	 
									
	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-grey">Welcome <#if user?exists>${user.username}</#if> to MyHippocampus!</div>
          <p class="subheading">#1 Take the tour</p>

          <p class="subheading">#2 Read the manifesto</p>

          <p class="subheading">#3 Signup for a free account!</p>
      </div>
		</div>

	<#if user?exists>
		<a href="j_acegi_logout">Logout</a>
	<#else>
      <div class="middle-column-box-blue">
        <div class="middle-column-box-title-white">
		 <form action="j_acegi_security_check" method="POST">
		      <table>
        		<tr><td>User:</td><td><input type='text' name='j_username' ></td></tr>
		        <tr><td>Password:</td><td><input type='password' name='j_password'></td></tr>
        		<tr><td><input type="checkbox" name="_acegi_security_remember_me"></td><td>Don't ask for my password for two weeks</td></tr>

		        <tr><td colspan='2'><input name="submit" type="submit"></td></tr>
        		<tr><td colspan='2'><input name="reset" type="reset"></td></tr>
		      </table>
	    </form>
	   </div>
	  </div>
	</#if>
	  <!-- FOOTER -->
    <div id="footer">
       Copyright &copy; 2006 MyHippocampus | All Rights Reserved<br />Design by <a href="mailto:gw@actamail.com">Gerhard Erbes</a> | <a href="http://validator.w3.org/check?uri=referer" title="Validate code as W3C XHTML 1.1 Strict Compliant">W3C XHTML 1.1</a> | <a href="http://jigsaw.w3.org/css-validator/" title="Validate Style Sheet as W3C CSS 2.0 Compliant">W3C CSS 2.0</a>
    </div>
  </div>
</body>
</html>