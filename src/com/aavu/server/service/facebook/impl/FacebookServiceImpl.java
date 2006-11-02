package com.aavu.server.service.facebook.impl;

import com.aavu.server.service.facebook.FacebookService;



import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

public class FacebookServiceImpl implements FacebookService {
	
	private static final String server = "http://api.facebook.com/restserver.php";
	private static final String apiKey = "d1144ae411b79109d46c6d752cd4d222";
	private static final String secret = "6fff87d9b04d26f7c64e65a04c832828";
	
	
	
//	API Key:  	d1144ae411b79109d46c6d752cd4d222
//	Secret: 	6fff87d9b04d26f7c64e65a04c832828
//	Base URL: 	http://www.myhippocampus.com
//	Callback URL: 	http://www.myhippocampus.com/site/facebook.html
//	IP Addresses: 	192.168.2.4,208.109.97.168
//	Support Email: 	jdwyah@myhippocampus.com
	
	
	//auth token a8511ce57cee09d7e99487e08c71346f
	
	public void doSomething() throws MalformedURLException{
		
		String sessionKey = getSessionKey();
		
		
		FacebookRestClient client = new FacebookRestClient(
		server,
		sessionKey,
		apiKey,
		secret);
		
		
		
		
		//client.callMethod("", );
		
	}
	
	

	private String getSessionKey() {

		
		
		return null;
	}
	
	
//	<div class="apinote"><h2><a href="http://www.myhippocampus.com">MyHippocampus</a></h2>Login to Facebook to enjoy the full
//	functionality of <a href="http://www.myhippocampus.com">MyHippocampus</a>.
//	If you don't want this to happen, go to the normal <a href="http://www.facebook.com/login.php">Facebook login</a> page.<p>
//	Starting next week, if you are already logged into Facebook, you won't have to login again.</p></div>
//	<form method="post" action="https://api.facebook.com/login.php" onsubmit="quicklogin();">
//
//	<input type="hidden" id="challenge" name="challenge" value="81ee10263abe7da482f42996496cc87e" />
//	<input type="hidden" id="md5pass" name="md5pass" value="" />
//	<input type="hidden" id="next" name="next" value="" />
//	<div id="loginform"><div class="form_row clearfix">
//	<label for="email" id="label_email">Email:</label>
//	<input type="text" class="inputtext" id="email" name="email" value="jeffrey.m.dwyer.02@alum.dartmouth.org" />
//	</div><div class="form_row clearfix"><label for="email" id="label_email">Password:</label>
//	<input type="password" class="inputpassword" id="pass" name="pass" value=""  /></div><div id="buttons">
//	<input type="hidden" id="api_key" name="api_key" value="d1144ae411b79109d46c6d752cd4d222" />
//	<input type="submit" value="Login" name="login" id="login" onclick="this.disabled=true; this.form.submit();" class="inputsubmit"/>
//	<input type="button" class="inputbutton" onclick="goURI('https://register.facebook.com/r.php');" id="register" name="register" value="Register" /></div>
//	<p class="reset_password">If you have forgotten your password, <b><a href="http://www.facebook.com/reset.php">click here</a></b> to reset it.</p></div>
//	<div class="apinote" style="margin-bottom: 10px; padding-top:10px;"><p><strong>Security Note:</strong> After login, you should never provide your password to an outside application.  Facebook does not provide your contact information to <a href="http://www.myhippocampus.com">MyHippocampus</a>.</p></div></form>
}
