<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus - My Page</title>
</head>

<body>
	
	 

  <p class="subheading">Don't forget to install the Firefox plugin! (<A HREF="<@spring.url "/resources/myhippo.xpi"/>">click here</A>)</p>


	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


    <#if user?exists>

	    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/>My Page</a>
		<br>
		<a href="j_acegi_logout">Logout</a>
	
	  	 	  
	</#if>
	

		

</body>
</html>