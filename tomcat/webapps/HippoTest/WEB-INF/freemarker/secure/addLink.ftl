<html>
<#import "/spring.ftl" as spring/>

<head>
  
  <script type="text/javascript" src="<@spring.url "/script/prototype.js"/>"></script>
  <script type="text/javascript" src="<@spring.url "/script/scriptaculous.js"/>"></script>
  
  <link rel="stylesheet" type="text/css" href="<@spring.url "/css/style.css"/>"/>
  <title><@spring.message "addLink.title"/></title>
  
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
	
		
	<#macro autocompleteMulti field name source help>

      <@spring.formInput path="${field}_field" attributes="size='80' onkeypress=\"return handleEnter(this, event)\" onClick=\"javascript:popNotice();\""/><@regError/>
	  <@spring.formHiddenInput path="${field}"/><@regError/>
	  <div id="tagline" style="display:none;"><@spring.message "${help}"/><p></div>
	  <div class="auto_complete" id="${name}_FILL"></div>			
	  
	  <div class="selected_tags" id="${name}_DISP"></div>					
	  <script type="text/javascript"> 
									
		//prevent enter from submitting the form	
		//& enter non-pre-existing tags									
		function handleEnter (field, event) {
			var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
			if (keyCode == 13) {
				//if there's an entry, let autocomplete do it. If no entry, it's a new tag, so add it
				if(!a.getCurrentEntry()){
					var t = {};
					t.innerHTML = document.getElementById('${name}_field').value
					complete(t);
				}
				return false;
			} 
			else
				return true;
		}      

		function complete(option){	
			document.getElementById('${name}').value=document.getElementById('${name}').value + ";"+option.innerHTML;
			document.getElementById('${name}_DISP').innerHTML=document.getElementById('${name}_DISP').innerHTML +" "+option.innerHTML;
			document.getElementById('${name}_field').value="";
		}

		function popNotice(){
				  	Effect.Appear('tagline',{duration:2.0});
 					Effect.Fade('tagline',{delay:4.0});
		}
											
		var a = new Ajax.Autocompleter('${name}_field','${name}_FILL','<@spring.url '${source}'/>', 
									    {paramName: "match",
									     minChars: 1,
									     updateElement: complete });
	  </script>
                       
            
	</#macro>
	

	<p>
    <form action="" method="POST">
	    <!-- Middle column full box -->
      <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "addLink.addALink"/></div>
		  <p class="subheading"><@spring.message "addLink.URL"/></p>					
		    <p><@spring.formInput "command.command_url", "size=\"80\""/><@regError/></p>
		  <p class="subheading"><@spring.message "addLink.Description"/></p>					
		    <p><@spring.formInput "command.command_description", "size=\"80\""/><@regError/></p>
		  <p class="subheading"><@spring.message "addLink.Notes"/></p>					
		    <p><@spring.formTextarea "command.command_notes", "rows=\"2\" cols=\"40\""/><@regError/></p>		    		    
		  <p class="subheading"><@spring.message "addLink.Tags"/></p>		
		    <p><@autocompleteMulti "command.command_tags", "command_tags", "/site/secure/getTopics.html", "addLink.typeInATag"/></p>
      </div>
      
      
       <div class="middle-column-box-white">
        <div class="middle-column-box-title-green"><@spring.message "addLink.submit"/></div>
				<input name="submit" type="submit" value="<@spring.message "addLink.button.submit"/>">
       </div>
	</div>

	</form>

	</body>
</html>

 