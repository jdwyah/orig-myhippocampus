<html>
<#if inviter?exists>
Congratulations, ${inviter.username} has sent you an invitation to MyHippocampus. 
MyHippocampus is a website that let's you collect & visualize your knowledge in a whole new way.
<#else>
Congratulations, your number has been called and you can now sign up to use MyHippocampus. 
MyHippocampus is a website that let's you collect & visualize your knowledge in a whole new way.
</#if>
<p>
Your secret key is ${randomkey}	
<p>
You can user this key to signup for an account. Go to http://www.myhippocampus.com/site/signup.html?secretkey=${randomkey} to signup or just
<a href="http://www.myhippocampus.com/site/signup.html?secretkey=${randomkey}&email=${email?url}">click here.</a>
<p>
If you want to learn more about the site, just go to <a href="http://www.myhippocampus.com/">myhippocampus.com</a> and take a look at the screencasts.
<p>
Thanks for your time,
<p>
-Jeff Dwyer
Founder, MyHippocampus
http://www.myhippocampus.com
</html>