<html>
	<head>
	<title>ERROR</title>
		<meta HTTP-EQUIV="content-type" CONTENT="text/html; charset=ISO-8859-1">
	        <META HTTP-EQUIV="Expires" CONTENT="-1">  
			<link rel="stylesheet" type="text/css" href="/css/pems-style.css">
	        <link rel="stylesheet" type="text/css" href="/css/pems-mmsstatus.css">
	        <link rel="stylesheet" type="text/css" href="/css/pems-orderform.css">
			<script src="/js/utils.js" language="JavaScript" type="text/javascript"></script>
			<span class="sectionTitle">Error Occurred !!! </span><BR>
	
	</head>
	
	<body>
	   <center>
		<table width=800  cellpadding=2 cellspacing=0 class="table">	   
			<tr class="tableTitle">
				<th colspan=4 class="tableTitle" >A problem encounterd while processing your Order</th>
			</tr>
	      	
		  	<tr class="rowEven1">
	<#if exception?exists>
				<td class=orderFormLabel>Exception: </td>
				<td class=orderForm>
					${exception}
					<#list exception.stackTrace as st>
						${st}
					</#list>
				</td>
	<#else>
				<td class=orderFormLabel>
					No Error Message found !!!
				</td>
	</#if>
			</tr>

		</table>
	  </center>		
	</body>
</html>
