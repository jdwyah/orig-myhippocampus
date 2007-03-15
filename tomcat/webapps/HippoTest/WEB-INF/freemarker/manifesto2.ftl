<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

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
      </div>
      
      <#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
 	  </#macro>
 	  <#if !user?exists>
      <div class="middle-column-box-title-green">Interested? Want to know when we go live?</div>
		<form action="<@spring.url "/site/interested.html"/>" method="POST">
		      <table>
        		<tr><td>Add your email:</td><td><@spring.formInput "command.email"/><@regError/></td></tr>

		        <tr><td colspan='2'><input value="Let me know when i can signup!" type="submit"></td></tr>        		
		      </table>
	    </form>		
	   </#if>
	   <p>
	   Back to the <a href="<@spring.url "/site/index.html"/>"/>main page.</a>
    </div>
</body>
</html>