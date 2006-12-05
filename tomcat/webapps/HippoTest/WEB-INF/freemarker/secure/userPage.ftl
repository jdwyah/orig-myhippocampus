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
	
	<#assign pct = bean.numberOfTopics / 350/>
	<#assign width= 50 + 350 * pct/>
	
    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/><img border="0" width="${width}" src="<@spring.url "/img/realbrain.png"/>"><br>Enter!</a>
	<P>
	<a href="j_acegi_logout">Logout</a>
	

		

</body>
</html>