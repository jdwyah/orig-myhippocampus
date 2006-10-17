<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<#import "/spring.ftl" as spring/>

<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
  <meta name="description" content="Personal HD" />
  <meta name="keywords" content="personal,knowledge,management" />
  <meta name="author" content="Jeff Dwyer / Original design: Gerhard Erbes - gw@actamail.com/" />
  
  <script type="text/javascript" src="<@spring.url "/script/prototype.js"/>"></script>
  <script type="text/javascript" src="<@spring.url "/script/scriptaculous.js"/>"></script>
  
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>
  <title><@spring.message "addLink.title"/></title>
  
	<style>  div.auto_complete {
            width: 350px;
            background: #fff;
          }
          div.auto_complete ul {
            border:1px solid #888;
            margin:0;
            padding:0;
            width:100%;
            list-style-type:none;
          }
          div.auto_complete ul li {
            margin:0;
            padding:3px;
          }
          div.auto_complete ul li.selected { 
            background-color: #ffb; 
          }
          div.auto_complete ul strong.highlight { 
            color: #800; 
            margin:0;
            padding:0;
          }
	</style>
</head>

<body>
  <div id="wrap">

	<!-- MIDDLE COLUMN -->
    <div id="middle-column">
	
	
	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro> 
		
	<#macro autocomplete field name source help>

      <@spring.formInput path="${field}" attributes="size='80' onClick=\"javascript:popNotice();\""/><@regError/>
	  <div id="tagline" style="display:none;"><@spring.message "${help}"/></div>
	  <div class="auto_complete" id="${name}_FILL"></div>								
	  <script type="text/javascript"> 
									
		function ajaxFailure(t){
			alert('Error ' + t.status + ' -- ' + t.statusText);
		}

		function popNotice(){
				  	Effect.Appear('tagline',{duration:2.0});
 					Effect.Fade('tagline',{delay:4.0});
		}
											
		var a = new Ajax.Autocompleter('${name}','${name}_FILL','<@spring.url '${source}'/>', 
									    {paramName: "match",
									     minChars: 1});
	  </script>
      
      
	</#macro>
	

	<p>
    <form action="" method="POST">
	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-blue"><@spring.message "addLink.addALink"/></div>
		  <p class="subheading"><@spring.message "addLink.URL"/></p>					
		    <p><@spring.formInput "command.url", "size=\"80\""/><@regError/></p>
		  <p class="subheading"><@spring.message "addLink.Description"/></p>					
		    <p><@spring.formInput "command.description", "size=\"80\""/><@regError/></p>
		  <p class="subheading"><@spring.message "addLink.Notes"/></p>					
		    <p><@spring.formTextarea "command.notes", "rows=\"2\" cols=\"40\""/><@regError/></p>		    		    
		  <p class="subheading"><@spring.message "addLink.Tags"/></p>		
		    <p><@autocomplete "command.tags", "tags", "/site/secure/getTopics.html", "addLink.typeInATag"/></p>
      </div>
      
      
       <div class="middle-column-box-white">
        <div class="middle-column-box-title-blue"><@spring.message "addLink.submit"/></div>
				<input name="submit" type="submit" value="<@spring.message "addLink.button.submit"/>">
       </div>
	</div>

	</form>

	</body>
</html>

 