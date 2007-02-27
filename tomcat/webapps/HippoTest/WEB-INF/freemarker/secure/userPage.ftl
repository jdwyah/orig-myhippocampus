<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus - My Page</title>
</head>

<body>
	

	<div class="middle-column-box-title-green">
	  <p class="subheading">Hi ${bean.user.username}, welcome to your MyHippocampus!</p>
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	Number of Islands: ${bean.numberOfIslands}<BR>
	Number of Links: ${bean.numberOfLinks}<BR>
	Number of Topics: ${bean.numberOfTopics}<BR>
	
	<#assign pct = bean.numberOfTopics / 400/>
	<#assign width= 100 + 400 * pct/>
	
    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/><img border="0" width="${width}" src="<@spring.url "/img/enterMyHippocampus.png"/>"><br>Enter!</a>
	<P>
	<a href="j_acegi_logout">Logout</a>
	
	<#if bean.user?exists>
		<#if bean.user.supervisor>
			<p>
	         <a href="<@spring.url "/site/secure/extreme/userManager.html?action=list"/>"/>Admin</a></li>
        </#if>
    </#if>
		
	<div class="middle-column-box-title-green">
	Browser Plugins
	</div>		
	<p>Don't forget to install a browser plugin. You don't need one, but it will let you bookmark a web pages right into your Hippo.</p>
    <p>For the Firefox plugin (v1.8 2/9/07) (<A HREF="<@spring.url "/resources/myhippo.xpi"/>">click here</A>)</p>
    <p>For the Internet Explorer plugin (<A HREF="<@spring.url "/resources/MyHippocampusIE.msi"/>">click here</A>)
    <br>(note: It may appear under your toolbar's ">>". Right-Click "Customize Command Bar" to make it more visible.</p>
    
    <div class="middle-column-box-title-green">
	Tips!
	</div>		
	<ul>
	<li><span style="tip-question">Tired of typing?</span> Try just going to 'hipcamp.com' </li>
	<li><span style="tip-question">Have a scroll wheel?</span> Zoom In and Zoom Out with the scroll wheel on your mouse.</li>
	<li><span style="tip-question">No scroll wheel?</span> Use the + and - keys to zoom.</li>
	<li><span style="tip-question">Wish you could save an email into you hippo?</span> You can! Email your username at hipcamp.com</li>
	<li><span style="tip-question">Need ideas?</span> Do you have an island for keeping track of world domination plans? Books you should read? Your happy place?</li>
	</ul
	
		
</body>
</html>