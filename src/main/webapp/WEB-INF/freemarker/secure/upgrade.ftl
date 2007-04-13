<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus - Upgrade Account </title>
</head>

<body>

	
 <div class="middle-column-box-white">

	<div class="middle-column-box-title-green">
		Current Subscription:
	</div>
	Description: ${user.subscription.description}<br>
	Maximum Topics: ${user.subscription.maxTopics}<br>
	Terms: $${user.subscription.price} / month<br>


	<#if user.premiumAccount>
		<div class="middle-column-box-title-green">
			Cancel Subscription:
		</div>

		<A HREF="https://www.paypal.com/cgi-bin/webscr?cmd=_subscr-find&alias=jdwyah%40myhippocampus%2ecom">
		<IMG SRC="https://www.paypal.com/en_US/i/btn/cancel_subscribe_gen.gif" BORDER="0">
		
	<#else>
	
		<div class="middle-column-box-title-green">
			Upgrade to Basic Account:
		</div>


<#--http://www.paypaldev.org/topic.asp?TOPIC_ID=10285&SearchTerms=userid,custom-->
		<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
			<input type="hidden" name="cmd" value="_s-xclick">
			<input type="hidden" name="custom" value="${user.id}"> 
<#--			<input type="image" src="http://www.myhippocampus.com/site/img/basicAccount.png" border="0" name="submit" alt="Make payments with PayPal - it's fast, free and secure!">-->
			<input type="submit" value="Upgrade to Basic Account">
			<img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
			<input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----
			">
		</form>
	
	
	
	</#if>
</A>

</div>
<#--
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-but20.gif" border="0" name="submit" alt="Make payments with PayPal - it's fast, free and secure!">
<img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
<input type="hidden" name="cmd" value="_xclick-subscriptions">
<input type="hidden" name="business" value="jdwyah@myhippocampus.com">
<input type="hidden" name="item_name" value="foo">
<input type="hidden" name="item_number" value="2">
<input type="hidden" name="no_shipping" value="1">
<input type="hidden" name="no_note" value="1">
<input type="hidden" name="currency_code" value="USD">
<input type="hidden" name="lc" value="US">
<input type="hidden" name="bn" value="PP-SubscriptionsBF">
<input type="hidden" name="a3" value="4.95">
<input type="hidden" name="p3" value="1">
<input type="hidden" name="t3" value="M">
<input type="hidden" name="src" value="1">
<input type="hidden" name="sra" value="1">
</form>-->


</body>
</html>