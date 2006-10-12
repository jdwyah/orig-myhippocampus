<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer / Original design: Gerhard Erbes - gw@actamail.com/" />
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>
  <title>MyHippocampus - Manifesto</title>
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
        <li><a href="tour.html">Tour</a></li>
        <li class="selected">Manifesto</li>
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
        <div class="middle-column-box-title-grey">Our Manifesto</div>
		  <p class="subheading">Rant Part #1</p>					
		  			<p>When you take a look at your life, you may find that the bits worth remembering are... worth remembering. You may also find that you're
		  			forgetting them.
		  			So this is an opportunity to remember things. 
		  			Your analysis of Karl Marx for example. You really thought it was spot on and insightful, but that the 
		  			folks at wikipedia won't let you sneak in to the definition. Damn tyrants ;)
		  			Or perhaps the revelation you had that your friends father looks uncannily like
		  			that pig farmer from Babe, James Cromwell. Important stuff like this is worth remembering. It will make you chuckle in the future.
		  			So what if we can record these events, as a stick in the ever-moving mud-flats of knowledge? </p>
		  			<p>Think of it as a little way to say 'I was here.' A body builder's mirror for what's going on in our brain. 
		  			 It may not be all that important wether you've read 12 or 200 pieces of South African fiction,
		  			 but what matters is that the number was not 0.</p>
		  			<p>Record these things and in time you'll notice the synergies that exist in your collection. Your oevre.
		  			 Perhaps you'll synthesise two disparate parts of your experience and think of something truly unique. 
		  			 Might it change the world? Does it matter?</p>
		  			<p>This is not intended as a repository of reviews or a public facing blog, so much as an inner monologue stenographer.</p>
          <p class="subheading">MyHippocampus</p>      
			       	<p>MyHippocampus is a personal intellectual tool.</p>
			       	<p>We hope it will help capture your insight into the world around you. Your insight into your world.</p>
					<p>It is not about accumulation of factoids, although it is adept at this. Insight is the goal, but sometimes insight needs to
					 emerge out of well organized clutter, since we never know what facets of our imagination or experience we will require
					 to come to our next conclusion.</p>
					 <p>So make sure to add enough clutter.</p>
					 <p>5 points will be awarded for in depth reviews of foreign films.</p>
					 <p>500 points will be awarded for each joke.</p>
					 <p>5000 points will be awarded for late night blathering about the signifigance of the movie title "Snakes on a Plane"</p>
          <p class="subheading">Responsibility</p>
					<p>MyHippocampus is a powerful tool, and like all tools it needs to be used with some care, lest it become another foot soldier
					 in the battle for a share of your conscious mind.</p>
					<p>Above all, do not let this become another burden. Are you slogging away adding an entry for every piece of romantic poetry you've read?
					Every bottle in you cellar? Every chemical in the periodic table? Forget it. Add what you want to read. The definition for 
					"Watermelon Environmentalist" (green on the outside, but pink in the middle) is worth 10x the points.</p>
					<p>MyHippocampus works very well as a blog, but think carefully about using it this way. Sharing with the outside world
					 is interesting and somewhat thrilling, but consider making it a diary first and foremost. 
					 Then sharing insights you truly want to be public. Don't let us tell you what to do, but do consider the wisdom in being a bit selfish.</p>
          <p class="subheading">Selfishness</p>
          			<p>Being a little selfish in your protection of your intellectual life is certainly not the worst thing in the world.
          			 To paraphrase Morrie "do not buy the culture" that isn't working for you. Whatever it is that you actually do like, write that down here.</p> 
		  			
      </div>
      
       <div class="middle-column-box-white">
        <div class="middle-column-box-title-blue">Interested?</div>
        <p class="subheading">Sign up to find out more.</p>
        <form action="">email: <input type="text" name="email"><input value="send" type="submit"></form>
       </div>
	</div>










	  <!-- FOOTER -->
    <div id="footer">
       Copyright &copy; 2006 MyHippocampus | All Rights Reserved<br />Design by <a href="mailto:gw@actamail.com">Gerhard Erbes</a> | <a href="http://validator.w3.org/check?uri=referer" title="Validate code as W3C XHTML 1.1 Strict Compliant">W3C XHTML 1.1</a> | <a href="http://jigsaw.w3.org/css-validator/" title="Validate Style Sheet as W3C CSS 2.0 Compliant">W3C CSS 2.0</a>
    </div>
  </div>
</body>
</html>