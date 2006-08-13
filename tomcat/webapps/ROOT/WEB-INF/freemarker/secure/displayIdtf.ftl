<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<#import "/spring.ftl" as spring/>

<html>
	<head>
		<title>Here are all studies</title>
		<link rel="stylesheet" type="text/css" href="http://pems.medicalmetrx.com/css/pems-style.css">
        <link rel="stylesheet" type="text/css" href="http://pems.medicalmetrx.com/css/pems-mmsstatus.css">
        <link rel="stylesheet" type="text/css" href="http://pems.medicalmetrx.com/css/pems-orderform.css">		
	</head>
<body>


<#macro printLookup array element>
    	<#if element?exists && element?string == "-1">
    		[ None Provided ]
    	<#elseif element?exists && element?string == "0">
            [ Unknown ]
        <#else>
	    	${array[element?c]}
    	</#if>
</#macro>
<#macro printDate date="">
		<#if date?has_content>
			${date?date?string.short}
		<#else>
			undef
		</#if>
    </#macro>


<#if command.orders?size == 0>
No orders found
</#if>

<table>
<tr><td>

<#macro tab></td><td></#macro>
<#macro newLineE>
</td></tr><tr class="rowEven"><td>
</#macro>
<#macro newLineO>
</td></tr><tr class="rowEven1"><td>
</#macro>
<#macro tstart>
<table><tr><td>
</#macro>


<#macro bigTable>
<table class="table"><tr><td>
<#nested>
</td></tr></table>
</#macro>

<#macro smTable title>
<table class="subTable">
<tr class="tableTitle">
<th class="tableTitle">${title}</th>
</tr>
<tr class="rowEven"><td>
<#nested>
</td></tr></table>
</#macro>


<#list command.orders as order>
<@bigTable>
<#if !order.orderIdtfBilling?exists>
<!--<@smTable "No IDTF">
</@smTable>-->
<#else>
<@smTable "Patient">
ID: ${order.patient.patientId}<@tab/>
Last: ${order.patient.lastName}<@tab/>
First: ${order.patient.firstName}<@tab/>
<@printLookup array=command.sexOptions element=order.patient.sex.sexId /><@newLineO/>
Address: ${order.orderIdtfBilling.patientAddress}<@tab/>
City: ${order.orderIdtfBilling.patientCity}<@tab/>
State: ${order.orderIdtfBilling.patientState}<@tab/>
Zip: ${order.orderIdtfBilling.patientZip}<@tab/>
Tel: ${order.orderIdtfBilling.patientTel}<@newLineE/>
Key: ${order.patient.idtfKeyClearText?default("undef")}<@tab/>
DOB: <@printDate order.patient.dateOfBirth /><@newLineO/>
</@smTable>
<@smTable "Responsible Party">
<#if order.orderIdtfBilling.responsibleParty>
Name: ${order.orderIdtfBilling.rpName}<@tab/>
Address: ${order.orderIdtfBilling.rpAddress}<@tab/>
City: ${order.orderIdtfBilling.rpCity}<@tab/>
State: ${order.orderIdtfBilling.rpState}<@tab/>
Zip: ${order.orderIdtfBilling.rpZip}<@newLineO/>
<#else>
No Repsonsible Party<@newLineO/>
</#if>
</@smTable>
<@smTable "Primary Insurance">
<#if order.orderIdtfBilling.priInsMedicare>
Medicare<@newLineO/>
<#else>
Relationship: ${order.orderIdtfBilling.rpRelationship?default("undef")}<@tab/><#-- realtionship-->
Mnemonic: ${order.orderIdtfBilling.priInsMnemonics?default("undef")}<@tab/><#-- mnemonic-->
Policy: ${order.orderIdtfBilling.priInsSubsNoClearText}<@tab/>
Group: ${order.orderIdtfBilling.priInsGroup}<@newLineO/>
Name: ${order.orderIdtfBilling.priInsName}<@tab/>
Address: ${order.orderIdtfBilling.priInsAddress}<@tab/>
City: ${order.orderIdtfBilling.priInsCity}<@newLineE/>
State: ${order.orderIdtfBilling.priInsState}<@tab/>
Zip: ${order.orderIdtfBilling.priInsZip}<@newLineO/>
</#if>
</@smTable>

<@smTable "Secondary Insurance">
<#if order.orderIdtfBilling.secIns>
Mnemonic: ${order.orderIdtfBilling.secInsMnemonics?default("undef")}<@tab/><#-- mnemonic-->
Policy: ${order.orderIdtfBilling.secInsSubsNoClearText}<@tab/>
Group: ${order.orderIdtfBilling.secInsGroup}<@newLineO/>
Name: ${order.orderIdtfBilling.secInsName}<@tab/>
Address: ${order.orderIdtfBilling.secInsAddress}<@newLineE/>
City: ${order.orderIdtfBilling.secInsCity}<@tab/>
State: ${order.orderIdtfBilling.secInsState}<@tab/>
Zip: ${order.orderIdtfBilling.secInsZip}<@newLineO/>
<#else>
No Secondary Insurance<@newLineO/>
</#if>
</@smTable>

<@smTable "Order Info">
Service Date: <@printDate order.study.finished/><@newLineO/>
CPT: G0288<@newLineE/><#-- replace -->
ICD9: ${order.orderIdtfBilling.icd9Codes.code}<@newLineO/>
Physician: ${order.physician.firstName} ${order.physician.lastName}<@newLineE/>
UPIN: ${order.physician.upin?default("undef")}<@newLineO/>
State: ${order.physician.state}<@newLineE/>
Site: Medical Metrx<@newLineO/>
</@smTable>
</#if>
</@bigTable>
</#list>

</td></tr>
</table>



	</body>
</html>

 