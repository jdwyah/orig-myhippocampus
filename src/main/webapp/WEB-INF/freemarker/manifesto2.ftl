<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>
<#import "common.ftl" as common/>
<head>
  <title><@spring.message "manifesto2.title"/></title>
</head>

<body>

	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "manifesto2.1"/></div>
		  <p class="subheading"><@spring.message "manifesto2.1.0"/></p>					
		  			<p><@spring.message "manifesto2.1.1"/></p>
		  			<p><@spring.message "manifesto2.1.2"/></p>
		  			<p><@spring.message "manifesto2.1.3"/></p>
          <p class="subheading"><@spring.message "manifesto2.2.0"/></p>      
		  			<p><@spring.message "manifesto2.2.1"/></p>
		  			<p><@spring.message "manifesto2.2.2"/></p>
          <p class="subheading"><@spring.message "manifesto2.3.0"/></p>      
		  			<p><@spring.message "manifesto2.3.1"/></p>
          <p class="subheading"><@spring.message "manifesto2.4.0"/></p>      
		  			<p><@spring.message "manifesto2.4.1"/></p>
		  			<p><@spring.message "manifesto2.4.2"/></p>  			

      
 	  <#if !user?exists>
	      <@common.signupNow/>
	   </#if>
    </div>
</body>
</html>