<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "spring.ftl" as spring/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer/" />
  
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>

  <title>${title}</title>
  ${head}
</head>

<body onload="${page.properties["body.onload"]?default("")}">


<div id="container">
       
    <img src="<@spring.url "/img/sideSeaHorse.png"/>"/>

    <div id="mainPanel">
    
    <div id="header">
   	    <img src="<@spring.url "/img/myhippocampusLogo.png"/>"/>
   	    <BR>   	    
   	    Welcome <#if user?exists>${user.username}</#if> to MyHippocampus!
   	    <div id="navigation">
      <ul>
        <li class="selected">Home</li>
        <li><a href="tour.html">Tour</a></li>
        <li><a href="manifesto.html">Manifesto</a></li>
		<#if user?exists>
	        <li><a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/>My Page</a></li>
			<#if user.supervisor>
	            <li><a href="<@spring.url "/site/secure/extreme/userManager.html?action=list"/>"/>Admin</a></li>
            </#if>
        <#else>
        	<li><a href="signup.html">Sign up</a></li>  
        </#if>
      </ul>
    	</div>
    </div>
    

    <div id="content">
             ${body}
    </div>


    <div id="footer">
    
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
    </div>	
   </div>  <!--end mainpanel-->
</div>

</body>
</html>
