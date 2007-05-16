

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
  }
  
   
</script>

	 <form id="upForm" action="/j_acegi_security_check" method="POST" style="display: none">
		<fieldset>
			<legend><@spring.message "login.1.header"/> <a class="link" onclick="javascript:doOpenID();">Use OpenID</a></legend>
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
	 <form id="openIDForm" action="/site/j_acegi_openid_start" method="POST" style="display: block">
		<fieldset>
			<legend><@spring.message "login.1.header"/> <a class="link" onclick="javascript:doUsernamePassword();">Use username / password</a></legend>
			 <label for="j_username2"><input type='text' name='j_username' id = 'j_username2' class="openid-identifier"><@spring.message "login.1.user"/>
			 </label>		 		 
		 <p>
		 <input type="hidden" name="password"/>
		 <input name="login" value="<@spring.message "login.1.button"/>" type="submit">
		</fieldset>
	 </form>	
</#macro>

<#macro signupNow>
	<h2>
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