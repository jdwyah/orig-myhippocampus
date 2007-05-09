<html>
<#import "/spring.ftl" as spring/>
<head>
  <title>MyHippocampus - Upgrade Account </title>
</head>

<body>



<#macro makeButton subscription>		
<#-- PEND TODO SWITCH TO ENCRYPTED BUTTONS.  NOTE the code below. Trick is that the custom var will NOT get passed, so we'll need to 
encrypt the whole button on the fly. See code stublets in com.aavu.service.paypal -->
<form action="${paypalEndpoint}" method="post">
<#--<input type="image" src="https://www.paypal.com/en_US/i/btn/x-click-but20.gif" border="0" name="submit" alt="Make payments with PayPal - it's fast, free and secure!">-->
<img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
<input type="hidden" name="cmd" value="_xclick-subscriptions">
<input type="hidden" name="business" value="${receiverEmail}">
<input type="hidden" name="item_name" value="${subscription.description}">
<input type="hidden" name="item_number" value="${subscription.id}">
<input type="hidden" name="no_shipping" value="1">
<input type="hidden" name="no_note" value="1">
<input type="hidden" name="currency_code" value="USD">
<input type="hidden" name="lc" value="US">
<input type="hidden" name="bn" value="PP-SubscriptionsBF">
<input type="hidden" name="a3" value="${subscription.price}">
<input type="hidden" name="p3" value="1">
<input type="hidden" name="t3" value="M">
<input type="hidden" name="src" value="1">
<input type="hidden" name="sra" value="1">
<input type="hidden" name="custom" value="${user.id?c}"> 	
<input type="submit" value="<@spring.message "account.upgradeto"/> ${subscription.description}">
</form>
</#macro>
	
 <div class="middle-column-box-white">

	<div class="middle-column-box-title-green">
			<@spring.message "account.current"/>
	</div>
	<@spring.message "account.description"/> ${user.subscription.description}<br>
	<@spring.message "account.maxTopics"/> ${user.subscription.maxTopics}<br>
	<@spring.message "account.terms"/> <#if user.subscription.price == 0><@spring.message "account.terms.free"/><#else>$${user.subscription.price}<@spring.message "account.permonth"/></#if><br>

	<#--Only paying users can cancel-->
	<#if user.premiumAccount>
		<div class="middle-column-box-title-green">
			<@spring.message "account.cancel"/>
		</div>

		<A HREF="${paypalEndpoint}?cmd=_subscr-find&alias=${receiverEmail?url}&custom=${user.id?c}">
		<IMG SRC="https://www.paypal.com/en_US/i/btn/cancel_subscribe_gen.gif" BORDER="0"></A>		
		
	<#else>
	
		<div class="middle-column-box-title-green">
			<@spring.message "account.upgrade"/>
		</div>
		
		<#list subscriptions as subscription>
			<@makeButton subscription/>${subscription.description} $${subscription.price} / month for up to ${subscription.maxTopics} topics.					
		</#list>
		

	</#if>

	<div class="middle-column-box-title-green">
		<@spring.message "account.about"/>
	</div>
		<@spring.message "account.about.0"/>
		<p>
		<@spring.message "account.about.1"/>
		<p>

		<@spring.message "account.about.2"/>

	<#--><div class="middle-column-box-title-green">
		<@spring.message "account.charging"/>
	</div>
		<p>
		<@spring.message "account.chargin.0"/>
	
	Why we're charging
	
We're charging for MyHippocampus for a very simple reason. We need the money ;) A lot of the Internet runs on
the idea that once you get people hooked, you'll be able to pop in advertisements and everything will turn out fine.
Unfortunately this would put us in the unenviable position of having to 'monetize' eyeballs in the
future, because we feel that this often leads to a conflicted position where we'd be more
interested in getting you to click on ads than on providing you with a great service. Instead we're looking for passionate users 
who think this service is worth it. 

Please contact us if you have any feedback or questions about this. 
	-->
</div>


<#--http://www.stellarwebsolutions.com/en/articles/paypal_button_encryption_php.php
http://www.paypaldev.org/topic.asp?TOPIC_ID=10285&SearchTerms=userid,custom
http://www.pdncommunity.com/pdn/board/message?board.id=basicpayments&message.id=3092&query.id=19293#M3092
	<p>Paypal</p>
		
		<form action="${paypalEndpoint}" method="post">
			<input type="hidden" name="cmd" value="_s-xclick">
			<input type="hidden" name="custom" value="${user.id?c}"> 
			<input type="submit" value="Upgrade to Basic Account">
			<img alt="" border="0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
			<input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----
			">
		</form>
	
	
	<p>Sandbox</p>
	<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
		<input type="hidden" name="cmd" value="_s-xclick">
		<input type="hidden" name="custom" value="${user.id?c}"> 		
		<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/x-click-but20.gif" border="0" name="submit" alt="Make payments with PayPal - it's fast, free and secure!">
		<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
		<input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----MIIH+wYJKoZIhvcNAQcEoIIH7DCCB+gCAQExggE6MIIBNgIBADCBnjCBmDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExETAPBgNVBAcTCFNhbiBKb3NlMRUwEwYDVQQKEwxQYXlQYWwsIEluYy4xFjAUBgNVBAsUDXNhbmRib3hfY2VydHMxFDASBgNVBAMUC3NhbmRib3hfYXBpMRwwGgYJKoZIhvcNAQkBFg1yZUBwYXlwYWwuY29tAgEAMA0GCSqGSIb3DQEBAQUABIGAWEX3fJWZGx3Emg8SLsZCFfoSkJ+OyJ6iSiqjSl0CgOJzEJqGIG2nfk8ha+uy4CLy6mWI16Sdo8y4fBAZKXD22krgox/4otkHkkrKsgIbxkgO0rVl3O4+yRIXkfWYHgRPHvWFoXT4/ZE1lxJYU9NEfg/HQ7QiGiH/QmRQmBejXuQxCzAJBgUrDgMCGgUAMIIBRQYJKoZIhvcNAQcBMBQGCCqGSIb3DQMHBAhFbHs9HJLWoICCASD/hxsB3RNUd5aOyqKvpkKhqTCvuxrbUH93S28dU5E+FOGUXQuemM2T2mS9A+SS1kkfjmdDPo8cNxdCGPR67H/E4myGx4vujewTM2hpi73GenvT886rYfYaWGS436sjjcH8GSLhu5tpvccMekUP+4pPnOUXVxY8VqvDy2GEKVcRSG9A00WijnndN/+yuNFT6wUqSMoi6owJ5dH40o6YKWuH177SyqA3r6pEmRMMBwbBB21Nn2sGCL1sON5jfmvBH20z8y9wFkAbJbSG3ImUctD/jAdyqEKmGuM+b7fyrEDC72b1rnhYSOR764V9chrWneILqnPSlcMeyuH3SeEXZ/7Eu1ZaiUfP9/iPRU5H3sY+CF3HmPOnijYzJzxjDqN9QOCgggOlMIIDoTCCAwqgAwIBAgIBADANBgkqhkiG9w0BAQUFADCBmDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExETAPBgNVBAcTCFNhbiBKb3NlMRUwEwYDVQQKEwxQYXlQYWwsIEluYy4xFjAUBgNVBAsUDXNhbmRib3hfY2VydHMxFDASBgNVBAMUC3NhbmRib3hfYXBpMRwwGgYJKoZIhvcNAQkBFg1yZUBwYXlwYWwuY29tMB4XDTA0MDQxOTA3MDI1NFoXDTM1MDQxOTA3MDI1NFowgZgxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMREwDwYDVQQHEwhTYW4gSm9zZTEVMBMGA1UEChMMUGF5UGFsLCBJbmMuMRYwFAYDVQQLFA1zYW5kYm94X2NlcnRzMRQwEgYDVQQDFAtzYW5kYm94X2FwaTEcMBoGCSqGSIb3DQEJARYNcmVAcGF5cGFsLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAt5bjv/0N0qN3TiBL+1+L/EjpO1jeqPaJC1fDi+cC6t6tTbQ55Od4poT8xjSzNH5S48iHdZh0C7EqfE1MPCc2coJqCSpDqxmOrO+9QXsjHWAnx6sb6foHHpsPm7WgQyUmDsNwTWT3OGR398ERmBzzcoL5owf3zBSpRP0NlTWonPMCAwEAAaOB+DCB9TAdBgNVHQ4EFgQUgy4i2asqiC1rp5Ms81Dx8nfVqdIwgcUGA1UdIwSBvTCBuoAUgy4i2asqiC1rp5Ms81Dx8nfVqdKhgZ6kgZswgZgxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMREwDwYDVQQHEwhTYW4gSm9zZTEVMBMGA1UEChMMUGF5UGFsLCBJbmMuMRYwFAYDVQQLFA1zYW5kYm94X2NlcnRzMRQwEgYDVQQDFAtzYW5kYm94X2FwaTEcMBoGCSqGSIb3DQEJARYNcmVAcGF5cGFsLmNvbYIBADAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4GBAFc288DYGX+GX2+WP/dwdXwficf+rlG+0V9GBPJZYKZJQ069W/ZRkUuWFQ+Opd2yhPpneGezmw3aU222CGrdKhOrBJRRcpoO3FjHHmXWkqgbQqDWdG7S+/l8n1QfDPp+jpULOrcnGEUY41ImjZJTylbJQ1b5PBBjGiP0PpK48cdFMYIBpDCCAaACAQEwgZ4wgZgxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMREwDwYDVQQHEwhTYW4gSm9zZTEVMBMGA1UEChMMUGF5UGFsLCBJbmMuMRYwFAYDVQQLFA1zYW5kYm94X2NlcnRzMRQwEgYDVQQDFAtzYW5kYm94X2FwaTEcMBoGCSqGSIb3DQEJARYNcmVAcGF5cGFsLmNvbQIBADAJBgUrDgMCGgUAoF0wGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHATAcBgkqhkiG9w0BCQUxDxcNMDcwNDE2MTQwOTU2WjAjBgkqhkiG9w0BCQQxFgQUvIfwWFYknhfUCPNU2PNYhMtKKwwwDQYJKoZIhvcNAQEBBQAEgYCV9v61etRM7zbzdNN1N0JHBskVR5SLKKmnVL0Lv4PD6Z17mtO/jet9BuMwHfyMYvwtS5ZRveC2cfFvH0J1d2e9SMgY74rWv9dYsa1lh1ShoBU45AAaoUApNn0a+PaOmPwCKz1+1kkSZWV08Gv5YBifp9w9AfskUq3LnQRnzf49wA==-----END PKCS7-----
		">
	</form>-->

</body>
</html>