<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer / Original design: Gerhard Erbes - gw@actamail.com/" />
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>
  <title>MyHippocampus - Add Link</title>
</head>

<body>
  <div id="wrap">

	<!-- MIDDLE COLUMN -->
    <div id="middle-column">
	
	
	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro> 
	
	

	<#if reason?exists>
		Submission Failed<P>
		Reason: ${reason}<P>
	</#if>
									
    <form action="" method="POST">
	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-blue">Add a link: </div>
		  <p class="subheading">URL</p>					
		    <p><@spring.formInput "command.url", "size=\"80\""/><@regError/></p>
		  <p class="subheading">Description</p>					
		    <p><@spring.formInput "command.description", "size=\"80\""/><@regError/></p>
		  <p class="subheading">Notes</p>					
		    <p><@spring.formTextarea "command.notes", "rows=\"2\" cols=\"40\""/><@regError/></p>		    		    
		  <p class="subheading">Tags</p>					
		    <p><@spring.formInput "command.tags", "size=\"80\""/><@regError/></p>
      </div>
      
       <div class="middle-column-box-white">
        <div class="middle-column-box-title-blue">Submit</div>
				<input name="submit" type="submit">
       </div>
	</div>

	</form>

	</body>
</html>

 