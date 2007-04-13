<html>
<#import "/spring.ftl" as spring/>
<head>
  <title><@spring.message "contact.title"/></title>
</head>

<body>
	
	<form method="post" action="http://localhost:8080/site/ipn">

<input type="hidden" name="custom" value="7">	
<input type="hidden" name="mc_gross" value="0.01">
<input type="hidden" name="address_status" value="unconfirmed">
<input type="hidden" name="payer_id" value="QGVDAFGZ9XHLJ">
<input type="hidden" name="tax" value="0.00">
<input type="hidden" name="payment_date" value="13:50:26 Mar 11, 2004 PST">
<input type="hidden" name="address_street" value="1840 Embarcadero Road">
<input type="hidden" name="payment_status" value="Pending">
<input type="hidden" name="address_zip" value="94303">
<input type="hidden" name="first_name" value="Patrick">
<input type="hidden" name="address_name" value="Patrick Breitenbach">
<input type="hidden" name="notify_version" value="1.6">

<input type="hidden" name="payer_status" value="unverified">
<input type="hidden" name="business" value="pb-pdn@paypal.com">
<input type="hidden" name="address_country" value="United States">
<input type="hidden" name="address_city" value="Palo Alto">
<input type="hidden" name="quantity" value="1">
<input type="hidden" name="verify_sign" value="AkU-lzGsIkV0gazwa9nDVpmsx9X0AMF3KqbmhBuM8UTVNO5CFNAptk78">
<input type="hidden" name="payer_email" value="pb-test@paypal.com">
<input type="hidden" name="txn_id" value="4MX09190KB7728256">
<input type="hidden" name="payment_type" value="instant">
<input type="hidden" name="last_name" value="Breitenbach">
<input type="hidden" name="address_state" value="CA">
<input type="hidden" name="receiver_email" value="pb-pdn@paypal.com">
<input type="hidden" name="receiver_id" value="WAT63H8628SRN">
<input type="hidden" name="pending_reason" value="verify">
<input type="hidden" name="txn_type" value="web_accept">
<input type="hidden" name="item_name" value="Test">
<input type="hidden" name="mc_currency" value="USD">
<input type="hidden" name="item_number" value="">
<input type="hidden" name="payment_gross" value="0.01">
<input type="submit" value="Test IPN">
</form>

</body>
</html>