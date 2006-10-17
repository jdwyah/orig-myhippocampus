<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer / Original design: Gerhard Erbes - gw@actamail.com/" />
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>
  <title>MyHippocampus - Tour</title>
</head>

<body>
  <div id="wrap">

    <!-- HEADER -->
	  <!-- Background -->
    <div id="header-section">
		  <a href="#"><img id="header-background-left" src="<@spring.url "/img/img_logo.jpg"/>" alt=""/></a>
          <img id="header-background-right" src="<@spring.url "/img/img_header.jpg"/>" alt=""/>
	</div>


	  <!-- Navigation -->
    <div id="header">
      <ul>
        <li><a href="index.html">Home</a></li>
        <li class="selected">Tour</li>
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
    </div>


	  <!-- MIDDLE COLUMN -->
    <div id="middle-column">
	 
									
	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
          <div class="middle-column-box-title-grey">Getting Started</div>
	          <p class="subheading">Create an Account</p>
	          	<p>Click on the signup tab and enter a username and password.</p>
	          	<p>It should take you to the main page where you can now login.</p>
          <div class="middle-column-box-title-grey">Topics</div>
	          <p class="subheading">Create a topic</p>
	          	<p>Creating a topic is a very simple affair.</p>
	          	<p>Open the link to "My Page" in a new tab so you can follow along. I often need to hold down shift and hit reload to get it to load up right.</p>	          	
  	         	<p>Once that's set, from the main page, hit add. Now hit edit. Add a title. Do the last book you read. Hit Save.</p>
  	         	<p>That's it!</p>
	          <p class="subheading">Format the topic</p>
	          	<p>MyHippocampus uses a simple version of the easy to use wikitext formatting style to help you style and bring order to your topics. </p>
          <div class="middle-column-box-title-grey">Tags</div>
 	         <p class="subheading">What's a tag?</p>
          			<p>Tags are optional, but will give you much greater capability to search through you information.</p>
    	      <p class="subheading">Create a new tag</p>
          			<p>You can create new tags easily. Tags can be as simple as a word, like "funny" that will let you search through everything you think is funny.</p>
          			<p>Go back to the edit the topic page. Click in "Add tag:" and type "Book" then hit enter to tag. Easy huh?<p>
          			<p>You can add any tags you like. "Funny", "LifeChanging", "Cooking" etc.
        	  <p class="subheading">Add attributes to a tag</p>
          			<p>You can also add meta attributes to a tag, so that you can search with even more precision.</p>
          			<p>Click on the "Tags" heading. Select the book tag you made to select it. Now click "add field." Add a field called author, of type text. Add a field called "date read" of type date.
					<p>Now go back and edit the Topic you made. (Add the Book tag again, you shouldn't have to but this is ALPHA!) You see that it adds the author & date read. Put in some values.
	          <p class="subheading">Referencing other topics</p>				
	          		<p>So what fun is it if Topics are all alone? No fun at all.</p>
	          		<p>Go back to the Tag organizer and add a tag called "SeeAlso" Give it a new attribute called "See" and make that of type "Topic List." Hit save.</p>
					<p>Now make another Topic. Let's say it's the second to last book you read. Tag it as a book, and also tag it as SeeAlso.</p>	          			          		
					<p>You can see the seealso tag is different. Click inside its box and type the first letter of the other book. Hit enter and it will select this book. Now save, and you've done it!</p>
	          <p class="subheading">Extend a tag</p>
          			<p>Last but not at all least, you can even extend a tag, which means you'll keep all of the meta attributes of the extended tag, but you can now add more. Take the Book tag. If you work in publishing, you might make Book+ which will store not just author &amp; date read, but publisher, agent, etc!</p>
          <div class="middle-column-box-title-grey">Share</div>
	          <p class="subheading">Share</p>
					<p>Any topic can be shared and made publicly visible and searchable. Just click on the 'public'/'private' toggle when you're editing a topic.</p>
	          <p class="subheading">Blog</p>
	          		<p>A special 'blog' tag will put a topic into your blog.</p>
          <div class="middle-column-box-title-grey">Inbox & Plugins</div>
	          <p class="subheading">Firefox Plugin</p>
					<p>Use the HippoPlugin to send links to your MyHippocampus inbox where it will wait for you to add it to your hippocampus.</p>
	          <p class="subheading">Email</p>
					<p>You can also just send an email to username@myhippocampus.com and that will show up here. Perfect for remembering those little thoughts you email to yourself.</p>
      </div>
	</div>










	  <!-- FOOTER -->
    <div id="footer">
       Copyright &copy; 2006 MyHippocampus | All Rights Reserved<br />Design by <a href="mailto:gw@actamail.com">Gerhard Erbes</a> | <a href="http://validator.w3.org/check?uri=referer" title="Validate code as W3C XHTML 1.1 Strict Compliant">W3C XHTML 1.1</a> | <a href="http://jigsaw.w3.org/css-validator/" title="Validate Style Sheet as W3C CSS 2.0 Compliant">W3C CSS 2.0</a>
    </div>
  </div>
</body>
</html>