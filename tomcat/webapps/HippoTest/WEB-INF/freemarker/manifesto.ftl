<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

<head>
  <title>MyHippocampus - Manifesto</title>
</head>

<body>
  									
	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green">Our Manifesto</div>
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
					 <p>500 points will be awarded for humorous supreme court justice anecdotes.</p>
					 <p>5000 points will be awarded for late night insight into the post-modern signifigance of the movie title "Snakes on a Plane"</p>					 
          <p class="subheading">Responsibility</p>
					<p>MyHippocampus is a powerful tool, and like all tools it needs to be used with some care, lest it become another foot soldier
					 in the battle for a share of your conscious mind.</p>
					<p>Above all, do not let this become another burden. Are you slogging away adding an entry for every piece of romantic poetry you've read?
					Every bottle in you cellar? Every chemical in the periodic table? Forget it. Add what you want to read. The definition for 
					"Watermelon Environmentalist" (green on the outside, but pink in the middle) is worth 10 times the points. The wikipedia exists, use it.</p>
					<p>MyHippocampus works very well as a blog, but think carefully about using it this way. Sharing with the outside world
					 is interesting and somewhat thrilling, but consider making it a diary first and foremost. 
					 Then you can share the insights you truly want to be public.</p>
					<p>What's that you say? Is it possible that the 'My' in <em>My</em>Hippocampus has very little to do with another famous 'My'.com? Yep, that's the idea.
					What sort of 'My' do you want? Maybe this Internet is good for something besides spam, socializing teenagers & shopping after all.</p>
          <p class="subheading">Selfishness</p>
          			<p>Being a little selfish in your protection of your intellectual life is certainly not the worst thing in the world.
          			 To paraphrase Morrie "do not buy the culture" that isn't working for you. Whatever it is that you actually do like, write that down here.</p> 
		  			
      </div>
      
      <#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
 	  </#macro>
      <div class="middle-column-box-title-green">Interested? Want to know when we go live?</div>
		<form action="<@spring.url "/site/interested.html"/>" method="POST">
		      <table>
        		<tr><td>Add your email:</td><td><@spring.formInput "command.email"/><@regError/></td></tr>

		        <tr><td colspan='2'><input value="Let me know when i can signup!" type="submit"></td></tr>        		
		      </table>
	    </form>		
	   <p>
	   Back to the <a href="<@spring.url "/site/index.html"/>"/>main page.</a>
    </div>
</body>
</html>