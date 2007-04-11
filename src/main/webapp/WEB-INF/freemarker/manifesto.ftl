<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "manifesto1.title"/></title>
</head>

<body>
  		
  	  <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "manifesto1.1"/></div>
		  <p class="subheading"><@spring.message "manifesto1.1.0"/></p>					
		  			<p><@spring.message "manifesto1.1.1"/></p>
		  			<p><@spring.message "manifesto1.1.2"/></p>
		  			<p><@spring.message "manifesto1.1.3"/></p>
          <p class="subheading"><@spring.message "manifesto1.2.0"/></p>      
		  			<p><@spring.message "manifesto1.2.1"/></p>
		  			<p><@spring.message "manifesto1.2.2"/></p>
		  			<p><@spring.message "manifesto1.2.3"/></p>
		  			<p><@spring.message "manifesto1.2.4"/></p>
		  			<p><@spring.message "manifesto1.2.5"/></p>
		  			<p><@spring.message "manifesto1.2.6"/></p>
		  			<p><@spring.message "manifesto1.2.7"/></p>
          <p class="subheading"><@spring.message "manifesto1.3.0"/></p>      
		  			<p><@spring.message "manifesto1.3.1"/></p>
		  			<p><@spring.message "manifesto1.3.2"/></p>
		  			<p><@spring.message "manifesto1.3.3"/></p>
		  			<p><@spring.message "manifesto1.3.4"/></p>		  					  			
          <p class="subheading"><@spring.message "manifesto1.4.0"/></p>      
		  			<p><@spring.message "manifesto1.4.1"/></p>

		  <p><@spring.message "manifesto1.5.1"/><a href="<@spring.url "/site/manifesto2.html"/>"/><@spring.message "manifesto1.5.2"/></a></p>
     
        <#if !user?exists>
	      <@common.interested/>
 	    </#if>
 	    
      </div>
  						
      
      
            
 	

    </div>
</body>
</html>