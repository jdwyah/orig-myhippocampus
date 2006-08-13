<#macro printLookup array element>
    	<#if element?exists && element?string == "-1">
    		[ None Provided ]
    	<#elseif element?exists && element?string == "0">
            [ Unknown ]
        <#else>
	    	${array[element?c]?upper_case}
    	</#if>
</#macro>
<#macro strip str>
${str?replace('\\D', '','r')?string}
</#macro>
<#macro split str idx>
<#assign l = str?split(" ")/>
<#if l?size gte idx>
${l[idx]?default("")}
</#if>
</#macro>
<#macro asNumber num="">
<#if num?has_content>${num?c}<#else></#if>
</#macro>
<#macro printDate date="">
		<#if date?has_content>
			${date?date?string("MMddyyyy")}
		<#else>
			
		</#if>
    </#macro>
<#macro tab>|</#macro>
<#macro newLine>
</#macro>
<#list command.orders as order>
<#if !order.orderIdtfBilling?exists>
<#else>
<@compress single_line=true>
${order.patient.patientId?c}<@tab/>
${order.patient.lastName?upper_case}<@tab/>
${order.patient.firstName?upper_case}<@tab/>
<@printLookup array=command.sexOptions element=order.patient.sex.sexId /><@tab/>
${order.orderIdtfBilling.patientAddress?upper_case}<@tab/>
<@tab/><#-- address 2 -->
${order.orderIdtfBilling.patientCity}<@tab/>
${order.orderIdtfBilling.patientState?upper_case}<@tab/>
${order.orderIdtfBilling.patientZip?upper_case}<@tab/>
<@strip order.orderIdtfBilling.patientTel/><@tab/>
<@strip order.patient.idtfKeyClearText?default("")/><@tab/>
<@printDate order.patient.dateOfBirth /><@tab/>
<#if order.orderIdtfBilling.responsibleParty>
<@split order.orderIdtfBilling.rpName?upper_case, 1/><@tab/><#-- last -->
<@split order.orderIdtfBilling.rpName?upper_case, 0/><@tab/><#-- first -->
${order.orderIdtfBilling.rpAddress?upper_case}<@tab/>
<@tab/><#-- address 2 -->
${order.orderIdtfBilling.rpCity?upper_case}<@tab/>
${order.orderIdtfBilling.rpState?upper_case}<@tab/>
${order.orderIdtfBilling.rpZip?upper_case}<@tab/>
<#else>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
</#if>
<#if order.orderIdtfBilling.priInsMedicare>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<#else>
${order.orderIdtfBilling.rpRelationship?default("")?upper_case}<@tab/><#-- realtionship-->
<@asNumber order.orderIdtfBilling.priInsMnemonics/><@tab/><#-- mnemonic-->
${order.orderIdtfBilling.priInsSubsNoClearText?default("")?upper_case}<@tab/><#--? policy number-->
${order.orderIdtfBilling.priInsGroup?upper_case}<@tab/>
${order.orderIdtfBilling.priInsName?upper_case}<@tab/>
${order.orderIdtfBilling.priInsAddress?upper_case}<@tab/>
${order.orderIdtfBilling.priInsCity?upper_case}<@tab/>
${order.orderIdtfBilling.priInsState?upper_case}<@tab/>
${order.orderIdtfBilling.priInsZip?upper_case}<@tab/>
</#if>
<#if order.orderIdtfBilling.secIns>
<@asNumber order.orderIdtfBilling.secInsMnemonics/><@tab/> <#-- mnemonic-->
${order.orderIdtfBilling.secInsSubsNoClearText?default("")?upper_case}<@tab/><#--? policy number-->
${order.orderIdtfBilling.secInsGroup?upper_case}<@tab/>
${order.orderIdtfBilling.secInsName?upper_case}<@tab/>
${order.orderIdtfBilling.secInsAddress?upper_case}<@tab/>
${order.orderIdtfBilling.secInsCity?upper_case}<@tab/>
${order.orderIdtfBilling.secInsState?upper_case}<@tab/>
${order.orderIdtfBilling.secInsZip?upper_case}<@tab/>
<#else>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
<@tab/>
</#if>
<@printDate order.study.finished/><@tab/>
G0288<@tab/>
${order.orderIdtfBilling.icd9Codes.code?upper_case}<@tab/>
${order.physician.lastName?upper_case}, ${order.physician.firstName?upper_case}<@tab/><#-- lookup -->
${order.physician.upin?default("")?upper_case}<@tab/>
${order.physician.state?upper_case}<@tab/>
Medical Metrx<@tab/></@compress><@newLine/>
</#if>
</#list>
