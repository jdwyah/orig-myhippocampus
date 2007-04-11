<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "site"/></title>
</head>

<body>
 <#if message?exists>
	 <p class="message">${message}</p>
 </#if>							
     
   

	<#macro regError>
		<span class="error"><@spring.showErrors"<br>"/></span>
	</#macro>


  	<#macro acre left top color num type>
  		<div class="acre" style="left: ${left}px; top: ${top}px;">
			<img  src="../com.aavu.HippoTest/img/simplicity/type120px_${color}_${num}_${type}.png"/>				
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
  			<ul>
  			<li><a href="<@spring.url "/site/basics.html"/>"/><@spring.message "index.whatIs.1"/></a></li>
  			<li><a href="<@spring.url "/site/screencasts.html"/>"/><@spring.message "index.whatIs.2"/></a></li>
  			<li><a href="<@spring.url "/site/manifesto.html"/>"/><@spring.message "index.whatIs.3"/></a></li>
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
	    <a href="<@spring.url "/com.aavu.HippoTest/HippoTest.html"/>"/>Goto My Page!</a>
		<br>
		<a href="j_acegi_logout">Logout</a>
	<#else>
		 <div id="loginBox">
		 <form id="loginForm" action="j_acegi_security_check" method="POST">
			<fieldset>

				<legend><@spring.message "login.1.header"/></legend>

				 <label for="j_username"><input type='text' name='j_username' id = 'j_username'><@spring.message "login.1.user"/>
				 </label>
			 <p>
				 <label for="j_password"><input type='password' name='j_password' id = 'j_password'><@spring.message "login.1.pass"/>
				 </label>
			 <p>
			 	<label for="_acegi_security_remember_me"><input type="checkbox" name="_acegi_security_remember_me"><@spring.message "login.1.dontask"/>
				 </label>
			 <p>
			 <input name="login" value="<@spring.message "login.1.button"/>" type="submit">

			</fieldset>
		 </form>	
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
		 ["80","100","2"],		 
		 ["110","100","1"],		 
		 ["170","100","2"],	
		 ["210","-20","1"],	

 		 
	  	 ["100", "100","3"]]/>
		<@doacres seq, "Green"/>  
	</div>
	
	<div id="signupIsland" class="island">

		  <div class="islandOverlay">
		    <div>
		    <form action="<@spring.url "/site/interested.html"/>" method="POST">
			<fieldset>
				<legend><@spring.message "login.2.header"/></legend>				
	 							
					<@spring.message "login.2.addemail"/><@spring.formInput "command.email"/><@regError/>					
					<input value="<@spring.message "login.2.addemail"/>" type="submit">
			</fieldset>
		    </form>		
		    </div>
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