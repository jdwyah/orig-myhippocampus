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
  
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>

  <title>${title}</title>
  ${head}
</head>

<body onload="${page.properties["body.onload"]?default("")}">

<div id="wrapper">

	<div id="main">
       
    
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

</body>
</html>

