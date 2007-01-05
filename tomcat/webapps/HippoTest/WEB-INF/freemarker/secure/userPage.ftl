<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus - My Page</title>
</head>

<body>
	

	 

  <p class="subheading">Hi ${bean.user.username}, don't forget to install the Firefox plugin! (<A HREF="<@spring.url "/resources/myhippo.xpi"/>">click here</A>)</p>


	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	Number of Islands: ${bean.numberOfIslands}<BR>
	Number of Links: ${bean.numberOfLinks}<BR>
	Number of Topics: ${bean.numberOfTopics}<BR>
	
	<#assign pct = bean.numberOfTopics / 400/>
	<#assign width= 100 + 40 * pct/>
	
    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/><img border="0" width="${width}" src="<@spring.url "/img/enterMyHippocampus.png"/>"><br>Enter!</a>
	<P>
	<a href="j_acegi_logout">Logout</a>
	
	<#if bean.user?exists>
		<#if bean.user.supervisor>
			<p>
	         <a href="<@spring.url "/site/secure/extreme/userManager.html?action=list"/>"/>Admin</a></li>
        </#if>
    </#if>
		

</body>
</html>