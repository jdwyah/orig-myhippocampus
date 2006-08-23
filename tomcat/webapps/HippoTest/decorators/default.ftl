<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    ${head}
</head>

<body onload="${page.properties["body.onload"]?default("")}">

<a name="top"></a>
<div id="container">
    <div id="intro">
        
        <div id="content">
            <#include "/messages.ftl"/>
            ${body}
        </div>
    </div>

    <div id="supportingText">
        <div id="underground">
          <#if page.getProperty("page.underground")?exists>
              ${page.getProperty("page.underground")}
          </#if>
        </div>    
    </div>	  
</div>

</body>
</html>
