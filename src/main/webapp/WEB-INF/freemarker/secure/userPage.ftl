<html>
<#import "/spring.ftl" as spring/>
<#import "../common.ftl" as common/>
<head>
  <title><@spring.message "userPage.title"/></title>
</head>

<body>
	

	
 <div class="middle-column-box-white">
	<div class="middle-column-box-title-green">
		${bean.user.username}'s MyHippocampus account:
	</div>

	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>


	<form action="<@spring.url "/site/secure/search.html"/>" method="POST">
		<fieldset>
			<legend><@spring.message "userPage.search.legend"/></legend>				
								
			 <label for="searchTerm"><@spring.formInput "command.searchTerm"/><@common.regError/>
			 <@spring.message "userPage.search.label"/>
			 </label>			
			<input value="<@spring.message "userPage.search.submit"/>" type="submit">
		</fieldset>
	</form>		

	
	<#assign pct = bean.numberOfTopics / 400/>
	<#assign width= 100 + 200 * pct/>
	<#assign height= width / 1.47 />

	<p>	
    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/><img id="enterButton" width="${width}" height="${height}" src="<@spring.url "/img/enterMyHippocampus.png"/>"><br>Enter!</a>
	<div id="userDetails">
		Number of Islands: ${bean.numberOfIslands}<BR>
		Number of Links: ${bean.numberOfLinks}<BR>
		Number of Topics: ${bean.numberOfTopics}<BR>
	</div>
	</p>
	  			
	<#if bean.user?exists>
		<#if bean.user.supervisor>
			<p>
			<ul>
				<li><a href="<@spring.url "/site/secure/extreme/userManager.html?action=list"/>"/>Admin</a></li>
	     	 	<li><a href="<@spring.url "/site/secure/extreme/mailingList.html?action=list"/>"/>Mailing List</a></li>
	     	</ul>
        </#if>

        <#if (bean.user.invitations > 0)>
			<p class="subheading">
    	    Spread the word, you have ${bean.user.invitations} invitations left. 
        	</p>
			Invite a friend to use MyHippocampus! 
			<form method="post" action="<@spring.url "/site/secure/invite.html"/>">
				<input type="text" name="email"/><input type="submit" value="Invite"/>
			</form>
		<#else>
			<p class="subheading">
    	    You have ${bean.user.invitations} invitations left. 
        	</p>
        </#if>
        
    </#if>
		
	<div class="middle-column-box-title-green">Browser Plugins</div>		
	<p>Don't forget to install a browser plugin. You don't need one, but it will let you bookmark a web page right into your Hippo.</p>
    <p>For the Firefox plugin (v1.8 3/19/07) (<A HREF="<@spring.url "/resources/myhippo.xpi"/>">click here</A>)</p>
    <p>For the Internet Explorer plugin (new 3/13/07) (<A HREF="<@spring.url "/resources/MyHippocampusIE.msi"/>">click here</A>)
    <br>(note: It may appear under your toolbar's ">>". Right-Click "Customize Command Bar" to make it more visible.</p>

  </div>
		
</body>
</html>