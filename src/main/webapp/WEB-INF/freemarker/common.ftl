

<#macro regError>
	<span class="error"><@spring.showErrors"<br>"/></span>
</#macro>

<#macro loginForm>

<script language="javascript" type="text/javascript"> 
  
  function doOpenID(){
      document.getElementById('openIDForm').style.display='block';
      document.getElementById('upForm').style.display='none';              
  }
 
  function doUsernamePassword(){
	  document.getElementById('openIDForm').style.display='none';
      document.getElementById('upForm').style.display='block';
      
      <#--Both this and css prop are necessary for IE to not display this in narrow width-->
      document.getElementById('remember_me').style.display='inline-block';
  }
  function formvalidation(){  
  	if(document.getElementById('j_username2').value.indexOf('.') > -1){  
  		document.getElementById('openIDForm').submit();
  	}else{
  		document.getElementById('j_username').value = document.getElementById('j_username2').value;
  		doUsernamePassword();
  	}
  }
   
</script>

	 <form id="upForm" action="/j_acegi_security_check" method="POST" style="display: none">
		<fieldset>
			<legend><@spring.message "login.1.header"/> <a class="link" onclick="javascript:doOpenID();">Use OpenID</a></legend>
		 <p>
			 <label for="j_username"><input type='text' name='j_username' id = 'j_username'><@spring.message "login.1.user"/>
			 </label>
		 <p>
			 <label for="j_password"><input type='password' name='j_password' id = 'j_password'><@spring.message "login.1.pass"/>
			 </label>
		 <p>
		 	 <label id="remember_me" for="_acegi_security_remember_me"><input type="checkbox" name="_acegi_security_remember_me"><@spring.message "login.1.dontask"/>
			 </label>
		 <p>
		 <input name="login" value="<@spring.message "login.1.button"/>" type="submit">
		</fieldset>
	 </form>	
	 <form id="openIDForm" action="/site/j_acegi_openid_start" method="POST" style="display: block">
		<fieldset>
			<legend><@spring.message "login.1.header"/> <a class="link" onclick="javascript:doUsernamePassword();">Use username / password</a></legend>
			 <label for="j_username2"><input type='text' name='j_username' id = 'j_username2' class="openid-identifier"><@spring.message "login.1.user"/>
			 </label>		 		 
		 <p>
		 <input type="hidden" name="password"/>
		 <input name="login" value="<@spring.message "login.1.button"/>" type="submit" onclick="javascript:formvalidation();return false;" >
		</fieldset>
	 </form>	
</#macro>

<#macro signupNow>
	<h2 id="signupNow">
	 	<a href="<@spring.url "/site/signupIfPossible.html"/>"><@spring.message "login.signup"/></a>
	</h2>
</#macro>

<#macro pngImage src width height>
	<#if iePre7?exists>
		<div>
			<span style="display:inline-block;width:${width}px;height:${height}px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<@spring.url "${src}"/>', sizingMethod='scale')"></span>
		</div>
	<#else>
		<!-- commonPNG -->
		<img src=${src} width=${width} height=${height} border=0/>
	</#if>
</#macro>