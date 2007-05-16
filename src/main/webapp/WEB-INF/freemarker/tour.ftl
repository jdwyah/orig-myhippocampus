<html>
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "tour.title"/></title>
</head>

<body>
  		  
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "tour.1"/></div>
		  <p class="subheading"><@spring.message "tour.1.0"/></p>					
		  			<p><@spring.message "tour.1.1"/></p>
		  			<p><@spring.message "tour.1.2"/></p>
		  			<p><@spring.message "tour.1.3"/></p>
		  			<p><@spring.message "tour.1.4"/></p>
		  			<p><@spring.message "tour.1.5"/></p>		  			
          <p class="subheading"><@spring.message "tour.2.0"/></p>      
		  			<p><@spring.message "tour.2.1"/></p>
          <p class="subheading"><@spring.message "tour.3.0"/></p>      
		  			<p><@spring.message "tour.3.1"/></p>
          <p class="subheading"><@spring.message "tour.4.0"/></p>      
		  			<p><@spring.message "tour.4.1"/></p>  			
      
		  <p><@spring.message "manifesto1.5.1"/> <a href="<@spring.url "/site/manifesto2.html"/>"/><@spring.message "manifesto1.5.2"/></a></p>
     
      
 	  <#if !user?exists>
	      <@common.signupNow/>
	   </#if>
    </div>
  	 
</body>
</html>