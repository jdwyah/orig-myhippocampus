<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<#import "/spring.ftl" as spring/>
<#import "mmsutil.ftl" as mms/>

<html>
	<head>
		<title>Select the dates</title>
		
		<script src="http://pems.medicalmetrx.com/js/utils.js" language="JavaScript" type="text/javascript"></script>
		<script src="http://pems.medicalmetrx.com/js/calendar.js" language="JavaScript" type="text/javascript"></script>
		<script src="http://pems.medicalmetrx.com/js/calendar-en.js" language="JavaScript" type="text/javascript"></script>
		<script src="http://pems.medicalmetrx.com/js/calendar-setup.js" language="JavaScript" type="text/javascript"></script>
	
	</head>


	<#assign imagedir = "/pems_static/images">

	<#macro regInput path class="class='input'" size="size='37'">
			<#assign attribs = class+" "+size />
		    <@spring.formInput path, attribs/>
			<@regError/>    
	</#macro>
	<#macro regError>
		<font color="#FF0000"><@spring.showErrors"<br>"/></font>
	</#macro>

	<body>

<form method="post" action="idtfSelect.html">


	<@spring.formRadioButtons "command.style", styleMap, "" /><@regError/>

<fieldset>
<ul>

    <li>    			
            Starting Model # <@mms.formInputInt path="command.startModelNo" /><@regError/>
    </li>

    <li>
        	Ending Model # <@mms.formInputInt path="command.endModelNo" /><@regError/>
    </li>

</ul>
    <input title="Clicking this button will search using model numbers" class="button" type="submit" name="modelno" value="Search ModelNo">
</fieldset>

<fieldset>
<ul>

    <li>    			
            Start Date: <@regInput path="command.startDate" /><@regError/>
             <button title='Click here for a calendar' type='button' id='f_trigger_b' class=tinyButton><img width=15 height=14 src='${imagedir}/calendar.gif'></button>
									<script type='text/javascript'>
						          	<!--
							            Calendar.setup({
							              inputField     :    'startDate',          // id of the input field
							              ifFormat       :    '%Y-%m-%d',           // format of the input field
							              showsTime      :    false,                // will not display a time selector
							              button         :    'f_trigger_b',        // trigger for the calendar (button ID)
							              singleClick    :    true,                // double-click mode
							              weekNumbers    :    false,                // no week numbers 
							              step           :    1                     // show all years in drop-down boxes (instead of every other year as default)
							            });
									// -->
	            					</script>
    </li>

    <li>

        	End Date: <@regInput path="command.endDate" /><@regError/>
        	 <button title='Click here for a calendar' type='button' id='f_trigger_b' class=tinyButton><img width=15 height=14 src='${imagedir}/calendar.gif'></button>
									<script type='text/javascript'>
						          	<!--
							            Calendar.setup({
							              inputField     :    'endDate',          // id of the input field
							              ifFormat       :    '%Y-%m-%d',           // format of the input field
							              showsTime      :    false,                // will not display a time selector
							              button         :    'f_trigger_b',        // trigger for the calendar (button ID)
							              singleClick    :    true,                // double-click mode
							              weekNumbers    :    false,                // no week numbers 
							              step           :    1                     // show all years in drop-down boxes (instead of every other year as default)
							            });
									// -->
	            					</script>
    </li>

</ul>

    <input title="Clicking this button will search between these dates" class="button" type="submit" name="date" value="Search Date">
</fieldset>

    
</form>
<p><a href="../j_acegi_logout">Logout</a>
	</body>
</html>

 