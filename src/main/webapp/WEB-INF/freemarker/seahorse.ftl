<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title>Seahorse?</title>
</head>

<body>
	
	<#if message?exists>
		 <div class="message">${message}</div>
	 </#if>
	
	
		<@common.box "boxStyle", "seahorseSection", "So why the Seahorse?">
			<p>
			 The hippocampus is a part of the brain that is essential to the creation of new memories.			 
			</p>			  			 	 			  
			<p>
			The hippocampus takes its name from the genus for <a href="http://en.wikipedia.org/wiki/Seahorse">seahorse</a>, which comes from the hippos = horse, (kampos = sea monster & kampi = curve, there seems to be a bit of disagreement.)
			</p>
			 <p>
			 At this point the MyHippocampus staff does not include anyone remotely qualified to really explain the intricacies of the human hippocampus. We are not doctors and have not even played them on TV.
			 </p>
			 <p>
			 We do ask people 'in the know' from time to time and I have to say there has been almost unanimous support for the name choice. 
			 </p>
			<ul>
			<li>Wikipedia's thoughts on the <a href="http://en.wikipedia.org/wiki/Hippocampus">Hippocampus</a></li>
			  <li>A version <a href="http://faculty.washington.edu/chudler/hippo.html">for kids</a></li>
			  </ul>
			</p>
		</@common.box>	
	
	 
	  

	
</body>
</html>