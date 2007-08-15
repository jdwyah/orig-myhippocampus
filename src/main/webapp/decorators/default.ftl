<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<!---NOTE: this is the decorator spring.ftl--->
<#import "spring.ftl" as spring/>
<#import "../WEB-INF/freemarker/common.ftl" as common/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
  <meta name="description" content="Tame information anxiety. Connect your thoughts, Write your novel, Start your business" />
  <meta name="keywords" content="personal,knowledge,management,visualization,collection,GWT,information,anxiety" />
  <meta name="author" content="Jeff Dwyer/" />
  
<link rel="icon" href="http://www.myhippocampus.com/favicon.ico" type="image/vnd.microsoft.icon" />
<link rel="shortcut icon" href="http://www.myhippocampus.com/favicon.ico" type="image/vnd.microsoft.icon" />

  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>

  <title>${title}</title>
  ${head}
</head>

<body onload="${page.properties["body.onload"]?default("")}">

<div id="wrapper">

	<div id="main">
       
    	<div id="sidebar">
    	
    	<a href="<@spring.url "/site/index.html"/>"><@common.pngImage src="/img/sideSeaHorse.png" width="129" height="225"/></a>
	      <p>
	      <#--<@spring.message "default.welcome.pre"/> <#if user?exists>${user.username}</#if> <@spring.message "default.welcome.post"/>-->
	      
	       <ul>
	       <#if user?exists>
				<li><a href="<@spring.url "/site/secure/userPage.html"/>"><@spring.message "sidebar.userPage"/></a></li>
				<li><a href="<@spring.url "/site/secure/account.html"/>"><@spring.message "sidebar.account"/></a></li>
				<li><a href="<@spring.url "/site/j_acegi_logout"/>">Logout</a></li>
			<#else>
				<li><a href="<@spring.url "/site/index.html"/>"><@spring.message "sidebar.frontPage"/></a></li>
				<li><a href="<@spring.url "/site/secure/userPage.html"/>"><@spring.message "sidebar.userPage"/></a></li>
			</#if>	       	
  			<li><a href="<@spring.url "/site/tour.html"/>"><@spring.message "sidebar.tour"/></a></li>
			<li><a href="<@spring.url "/site/screencasts.html"/>"><@spring.message "sidebar.screencasts"/></a></li>			
  			<li><a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "sidebar.manifestos"/></a></li>
			<li><a href="<@spring.url "/site/manifesto2.html"/>"><@spring.message "sidebar.manifesto2"/></a></li>
			
		   </ul>
	      
		</div>
     
	    <div id="header">
    		<@common.pngImage src="/img/myhippocampusLogo.png" width="552" height="82"/>			
	    </div>


    	<div id="content">
             ${body}
    	</div><!--content-->

	</div><!--main-->

    <div id="footer">
    	©2007 <a href="<@spring.url "/site/index.html"/>">MyHippocampus</a> 
    	| <a href="<@spring.url "/site/contact.html"/>">Contact Us</a> 
    	| <a href="http://myhippocampus.blogspot.com/">Blog</a>
    	| <a href="<@spring.url "/site/acknowledgments.html"/>">Acknowledgements</a> 
    	<br>
	</div>
	
	
</div><!--wrapper-->

		
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-1880676-1";
urchinTracker();
</script>


<#--<!--Preloader.-->
<#--<iframe src="<@spring.url "/com.aavu.HippoTest/HippoPreLoad.html"/>" style="visibility: hidden;"><\/iframe>
-->

</body>
</html>

