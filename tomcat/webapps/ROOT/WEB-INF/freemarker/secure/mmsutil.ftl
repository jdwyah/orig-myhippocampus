<#macro booleanFormCheck path attributes="">
	<@spring.bind path/>
    <input type="checkbox" id="${spring.status.expression}" name="${spring.status.expression}" value="true"        
        <#if spring.stringStatusValue == "true">checked="checked"</#if> ${attributes}
    <@spring.closeTag/>
	<#assign hiddenName = "_"+spring.status.expression/>
	<input type="hidden" name="${hiddenName}">
</#macro>
<#macro oneRadioButton path value attributes="">
    <@spring.bind path/>
    <input type="radio" id="${spring.status.expression}" name="${spring.status.expression}" value="${value}"
        <#if spring.stringStatusValue == value>checked="checked"</#if> ${attributes}/>
</#macro>
<#macro checkSelected value>
    <#if spring.stringStatusValue?is_number && spring.stringStatusValue == value?number>checked</#if>
    <#if spring.stringStatusValue?is_string && spring.stringStatusValue == value>checked</#if>
</#macro>
<#macro mmsFormRadioButtons path options separator attributes="">
    <@spring.bind path/>
    <#list options?keys as value>
    <input type="radio" id="${spring.status.expression}" name="${spring.status.expression}" value="${value}"
        <@checkSelected value/> ${attributes}
    <@spring.closeTag/>
    ${options[value]}${separator}
    </#list>
</#macro>
<#macro formInputInt path attributes="" fieldType="text" >
    <@spring.bind path/>
    <input type="${fieldType}" id="${spring.status.expression}" name="${spring.status.expression}" value="<#if fieldType!="password">${spring.status.value?default(0)?c}</#if>" ${attributes}
    <@spring.closeTag/>
</#macro>
<#macro formHiddenInputInt path attributes="" >
    <@formInputInt path, attributes, "hidden"/>
</#macro>
