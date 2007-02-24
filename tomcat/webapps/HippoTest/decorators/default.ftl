<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<!---NOTE: this is the decorator spring.ftl--->
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


<table class="container">
       
    <tr>
     <td class="sidePanel">
      <img src="<@spring.url "/img/sideSeaHorse.png"/>"/>
     </td>
     
         
     <td class="mainPanel" >
    
     
    <table class="header">
    <tr><td>
   	    <img src="<@spring.url "/img/myhippocampusLogo.png"/>"/>
   	    <BR>   	    
   	    Welcome <#if user?exists>${user.username}</#if> to MyHippocampus!
   	 <!--   <div class="navigation">
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
 
       </div>-->
    </tr></td>
    </table>
    

    <div id="content">
             ${body}
    </div>


    <div id="footer">
    	©2007 MyHippocampus
	</div>
	
	</td><!--end main panel-->
	</tr>
	</table>

		
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
