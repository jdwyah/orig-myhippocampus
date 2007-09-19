<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "site"/></title>
</head>

<body>					
     
   
 <#if message?exists>
	<div style="z-index: 99; position: absolute; left: 200px;">
	 <div class="message">${message}</div>
	</div>
 </#if>			  	 	  


  	<#macro acre left top color num type>
  		<div class="acre" style="left: ${left}px; top: ${top}px;">
    		<@common.pngImage src="/img/simplicity/type120px_${color}_${num}_${type}.png" width="120" height="120"/>
		</div>
	</#macro>
  
	
	<#macro doacres seq color>
		<#list seq as coord>
	  		<@acre left=coord[0] top=coord[1] color="${color}" num=coord[2] type="I"/>
		</#list>  		
		<#list seq as coord>
	  		<@acre left=coord[0] top=coord[1] color="${color}" num=coord[2] type="Inner"/>
		</#list>  		
  	</#macro>
	
	<#macro buildIsland size>
		
	</#macro>
  
  	<div id="whatIsIsland" class="island">
		<div class="islandOverlay">
  			<p class="islandHeader"><@spring.message "index.whatIs.header"/></p>

			<#--
			<p class="subheading"> <a href="<@spring.url "/site/tour.html"/>"><@spring.message "index.whatIs.1"/></a><p>
			<p class="subheading"> <a href="<@spring.url "/site/screencasts.html"/>"><@spring.message "index.whatIs.2"/></a><p>
			<p class="subheading"> <a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "index.whatIs.3"/></a><p>
			-->
  			<ul>
			<li><a href="<@spring.url "/site/screencasts.html"/>"><strong><@spring.message "index.whatIs.2"/></strong></a></li>  			
  			<li><a href="<@spring.url "/site/tour.html"/>"><@spring.message "index.whatIs.1"/></a></li>			
  			<li><a href="<@spring.url "/site/manifesto.html"/>"><@spring.message "index.whatIs.3"/></a></li>
			</ul>
  		</div>
  	
	  	<#assign seq = 
	  	[["0","0","2"],	
		 ["30","0","1"],			 	 
		 ["80","0","2"],			 
		 ["110","10","1"],		 
		 ["170","30","2"],		 		 
		 ["200","0","3"],	
		 
		 ["0","40","3"],
		 ["30","40","1"],			 	 
		 ["80","60","2"],		 
		 ["110","80","1"],		 
		 ["170","20","2"],	
		 ["190","20","3"],
		 
		 ["0","100","2"],
		 ["30","80","1"]		
		 ]/>
		<@doacres seq, "Purple"/>  
  	</div>
  
	
	<div id="loginIsland" class="island">

		<div class="islandOverlay">
	<#if user?exists>
	    <a href="<@spring.url "/site/secure/mindscape.html"/>"/>Goto My Page!</a>
		<br>
		<a href="/j_acegi_logout">Logout</a>
	<#else>
		 <div id="loginBox">
			 <@common.loginForm/>
		 </div>		 		
	</#if>
	
		</div><!--islandOverlay-->
		
		
	  	<#assign seq = 
	  	[
		 ["-10","-40","2"],	
		 ["30","-40","1"],			 	 
		 ["80","-40","2"],			 
		 ["110","-20","1"],		 
		 ["170","-20","2"],		 		 
		 ["200","-20","1"],	
		 
		 ["-10","20","1"],
		 ["30","20","1"],			 	 
		 ["80","20","1"],		 
		 ["110","20","1"],		 
		 ["170","20","2"],	
		 ["190","-20","2"],	
		 
		 ["-10","100","2"], 	 
		 ["65","100","1"],		 
		 ["90","100","1"],		 
		 ["140","100","1"],	
		 ["210","-20","1"]	

 		 ]/>
		<@doacres seq, "Green"/>  
	</div>
	
	<div id="signupIsland" class="island">

		  <div class="islandOverlay">		    		    
			    <h2 id="tryItNow">
	    		<a href="<@spring.url "/site/signupIfPossible.html"/>"><@spring.message "login.signup"/></a>
	    		</h2>		    
		  </div>
		  
		 			
			
	  	<#assign seq = 
	  	[ ["0","20","2"],
		 ["50","20","1"],			 	 
		 ["100","0","2"],		 
		 ["170","0","2"],	
		 ["190","20","2"],
		 
		 ["0","50","1"],	
		 ["40","70","2"]		
		 
		 ]/>
		<@doacres seq, "Orange"/>   
	</div>	  	 	  
	  	 	  

	
    
</body>
</html>