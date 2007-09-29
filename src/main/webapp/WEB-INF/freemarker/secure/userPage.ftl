<html>
<#import "/spring.ftl" as spring/>
<#import "../common.ftl" as common/>
<head>
  <title><@spring.message "userPage.title"/></title>
</head>

<body>
	
	
	 <#if message?exists>
		 <p class="message">${message}</p>
	 </#if>

<div id="side1">
  	  	
  	  	<#if bean.user?exists>
		<@common.box "boxStyle", "importSection", "Import Del.icio.us Bookmarks & Google Apps Documents">		
		<p>
				
			Del.icio.us Bookmarks Updated: <a href="<@spring.url "/site/secure/import.html"/>">${(bean.user.lastDeliciousDate?date)?default("Never")}</a>	
			<br>			
			Google Docs Updated: <a href="<@spring.url "/site/secure/import.html"/>">${(bean.user.lastGoogleAppsDate?date)?default("Never")}</a>
			
			<br>
			<h2>
			<a href="<@spring.url "/site/secure/import.html"/>">Import Now</a>
			</h2>
		</p>		
		</@common.box>
		</#if>	
		
		
		<#if bean.user?exists>
			<#if bean.user.supervisor>
				<@common.box "boxStyle", "superVSection", "Supervisor">		
				<p>
				<ul>				
					<li><a href="<@spring.url "/site/secure/extreme/userManager.html?action=list"/>"/>Admin</a></li>
		     	 	<li><a href="<@spring.url "/site/secure/extreme/mailingList.html?action=list"/>"/>Mailing List</a></li>
	    	 	 	<li><a href="<@spring.url "/site/secure/extreme/scripts.html?action=list"/>"/>Scripts</a></li>
	     		</ul>       	
				</@common.box>
			</#if>
		</#if>
		
			<@common.box "boxStyle", "pluginSection", "Browser Plugins">
			<p>Don't forget to install a browser plugin. You don't need one, but it will let you bookmark a web page right into your Hippo.</p>
    		<p>For the Firefox plugin (v1.8 3/19/07) (<A HREF="<@spring.url "/resources/myhippo21.xpi"/>">click here</A>)</p>
    		<p>For the Internet Explorer plugin (new 3/13/07) (<A HREF="<@spring.url "/resources/MyHippocampusIE.msi"/>">click here</A>)
    		<br>(note: It may appear under your toolbar's ">>". Right-Click "Customize Command Bar" to make it more visible.</p>
			</@common.box>
		
		<@common.box "boxStyle", "tipsSection", "Tips">
		<ul>
	<li><span class="tip-question">Tired of typing?</span> Try just going to 'hipcamp.com' </li>
	<li><span class="tip-question">Have a scroll wheel?</span> Zoom In and Zoom Out with the scroll wheel on your mouse.</li>
	<#--<li><span class="tip-question">No scroll wheel?</span> Use the + and - keys to zoom.</li>-->
	<li><span class="tip-question">Wish you could save an email into you hippo?</span> You can! Email your username at hipcamp.com</li>
	<li><span class="tip-question">Need ideas?</span> Do you have an island for keeping track of world domination plans? Books you should read? Your happy place?</li>
	<li><span class="tip-question">Need Inspiration?</span> Hear us rant & rave in the <A HREF="<@spring.url "/site/manifesto.html"/>">manifesto.</A></li>
	<li><span class="tip-question">Want an easy way to store links?</span> Make sure to install the browser plugin (IE or Firefox), available on you user page.</li>
	</ul>
		</@common.box>
</div>

<div id="side2">  		
		  	
		<@common.box "boxStyleSm", "invitationSection", "Invitations">
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
		</@common.box>
		
		<@common.box "boxStyleSm", "openSection", "OpenID">
		 <#if bean.user.password?exists>			
        	<p class="subheading">
    	    	Changing to OpenID 
        	</p>
        	What's all this "OpenID" stuff you ask? Well, the news is that MyHippocampus has now started accepting OpenID logins. OpenID is a pretty neat thing that's
        	designed to save us all from the insanity of having to manage different usernames & passwords. Read more about it <a href="http://openid.net/">here.</a>
        	<p>If you'd like to start using an OpenID account please <a href="<@spring.url "/site/contact.html"/>"/>contact us</a> and we'll get back to you with the details about how to do that (it's easy!)
        </#if>
        </@common.box>
</div>

<#--
	<form action="<@spring.url "/site/secure/search.html"/>" method="POST">
		<fieldset>
			<legend><@spring.message "userPage.search.legend"/></legend>				
								
			 <label for="searchTerm"><@spring.formInput "command.searchTerm"/><@common.regError/>
			 <@spring.message "userPage.search.label"/>
			 </label>			
			<input value="<@spring.message "userPage.search.submit"/>" type="submit">
		</fieldset>
	</form>		
	
	<div id="userDetails">		
		Number of Links: ${bean.numberOfLinks}<BR>
		Number of Topics: ${bean.numberOfTopics}<BR>
	</div>
-->
	
		
</body>
</html>