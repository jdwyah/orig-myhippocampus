

<#macro regError>
	<span class="error"><@spring.showErrors"<br>"/></span>
</#macro>

<#macro loginForm>
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
</#macro>

<#macro interested>
	<form action="<@spring.url "/site/interested.html"/>" method="POST">
		<fieldset>
			<legend><@spring.message "login.2.header"/></legend>				
								
				<@spring.message "login.2.addemail"/><@spring.formInput "command.email"/><@regError/>					
				<input value="<@spring.message "login.2.addemail"/>" type="submit">
		</fieldset>
	</form>		
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