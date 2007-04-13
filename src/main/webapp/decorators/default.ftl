<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<!---NOTE: this is the decorator spring.ftl--->
<#import "spring.ftl" as spring/>
<#import "../WEB-INF/freemarker/common.ftl" as common/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management,visualization,collection,GWT" />
  <meta name="author" content="Jeff Dwyer/" />
  
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>

  <title>${title}</title>
  ${head}
</head>

<body onload="${page.properties["body.onload"]?default("")}">

<div id="wrapper">

	<div id="main">
       
    	<div id="sidebar">
    	<a href="<@spring.url "/site/index.html"/>"/><img border="0" src="<@spring.url "/img/myhippocampusLogo_140_254.png"/>"/></a>
	      <br>
	      <@spring.message "default.welcome.pre"/> <#if user?exists>${user.username}</#if> <@spring.message "default.welcome.post"/>
	      
	       <ul>
	       	<li><a href="<@spring.url "/site/index.html"/>"/><@spring.message "sidebar.frontPage"/></a></li>
	       	<li><a href="<@spring.url "/site/secure/userPage.html"/>"/><@spring.message "sidebar.userPage"/></a></li>
  			<li><a href="<@spring.url "/site/tour.html"/>"/><@spring.message "sidebar.tour"/></a></li>
  			<li><a href="<@spring.url "/site/screencasts.html"/>"/><@spring.message "sidebar.screencasts"/></a></li>
			<li><a href="<@spring.url "/site/tips.html"/>"/><@spring.message "sidebar.tips"/></a></li>
  			<li><a href="<@spring.url "/site/manifesto.html"/>"/><@spring.message "sidebar.manifestos"/></a></li>
			<#if user?exists>
<li><a href="<@spring.url "/site/secure/upgrade.html"/>"/><@spring.message "sidebar.account"/></a></li>
			</#if>
		   </ul>
	      
		</div>
     
	    <div id="header">
    
   		    <img src="<@spring.url "/img/myhippocampusLogo.png"/>"/>
			<#if user?exists><span class="slashUser">/${user.username}</span>
				<a href="<@spring.url "/site/j_acegi_logout"/>">(Logout)</a>
			<#else>
				<#--><div id="loginBox">
				 <@common.loginForm/>
				</div>		-->
			</#if>
	    </div>


    	<div id="content">
             ${body}
    	</div><!--content-->

	</div><!--main-->

    <div id="footer">
    	©2007 <a href="<@spring.url "/site/index.html"/>"/>MyHippocampus</a> 
    	| <a href="<@spring.url "/site/contact.html"/>"/>Contact Us</a> 
    	| <a href="http://myhippocampus.blogspot.com/"/>Blog</a>
    	| <a href="<@spring.url "/site/acknowledgments.html"/>"/>Acknowledgements</a> 
	</div>
	
	
</div><!--wrapper-->

		
<!-- Start of StatCounter Code -->
<script type="text/javascript" language="javascript">
var sc_project=2098040; 
var sc_invisible=1; 
var sc_partition=19; 
var sc_security="2d3896c1"; 
</script>

<script type="text/javascript" language="javascript" src="http://www.statcounter.com/counter/counter.js"></script><noscript><a href="http://www.statcounter.com/" target="_blank"><img  src="http://c20.statcounter.com/counter.php?sc_project=2098040&java=0&security=2d3896c1&invisible=1" alt="free stats" border="0"></a> </noscript>
<!-- End of StatCounter Code -->


<!--Preloader.-->
<iframe src="<@spring.url "/com.aavu.HippoTest/HippoPreLoad.html"/>" style="visibility: hidden;"><\/iframe>

</body>
</html>
