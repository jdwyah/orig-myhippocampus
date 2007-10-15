
<#macro regError>
	<span class="error"><@spring.showErrors"<br>"/></span>
</#macro>

<#macro box class id title>
	<div class="${class}">	  
	<h2>${title}</h2>
	  <div class="right"></div>
	  <div id="${id}" class="boxContent">		
		<#nested>		
	  </div>	  
	</div>
</#macro>

<#macro urlLink topic>
	<a href="${topic.uri?default("Untitled")}">${topic.title?default("Untitled")}</a>
</#macro>
<#macro browseSearchRes res>
	<ul>
	<#list res.clickLinks as link>	
		<li><a href="<@spring.url "/site/browse/${res.user.nickname}/${link.topicTitle?url}"/>">${link.topicTitle?default("Untitled")}</a>
		</li>
	</#list>
	</ul>	
</#macro>
<#macro browseLink topic>
	<a href="<@spring.url "/site/browse/${topic.user.nickname}/${topic.title?url}"/>">${topic.title?default("Untitled")}</a>
</#macro>
<#macro userLink user>
<a href="<@spring.url "/site/browse/${user.nickname}"/>">${user.nickname}</a>
</#macro>

<#macro loginForm>

<script language="javascript" type="text/javascript"> 
  
  function doOpenID(){
      document.getElementById('openIDForm').style.display='block';
      document.getElementById('upForm').style.display='none';              
  }
 
  function doUsernamePassword(){
	  document.getElementById('openIDForm').style.display='none';
      document.getElementById('upForm').style.display='block';
      
      <#--Both this and css prop are necessary for IE to not display this in narrow width-->
      document.getElementById('remember_me').style.display='inline-block';
  }
  function isOpenID(){  
    var p = (document.getElementById('j_username2').value.indexOf('.') > -1);    
    var e = (document.getElementById('j_username2').value.indexOf('=') > -1);      	
  	return p || e;
  }
  function formvalidation(){    
  	if(isOpenID()){  
  		document.getElementById('openIDForm').submit();
  	}else{
  		document.getElementById('j_username').value = document.getElementById('j_username2').value;
  		doUsernamePassword();
  	}
  }
   
</script>

	 <form id="upForm" action="<@spring.url "/j_acegi_security_check"/>" method="POST" style="display: none">
		<fieldset>
			<legend><@spring.message "login.1.header"/></legend>
		 <p>
			 <label for="j_username"><input type='text' name='j_username' id = 'j_username'><@spring.message "login.1.user"/>
			 </label>
		 <p>
			 <label for="j_password"><input type='password' name='j_password' id = 'j_password'><@spring.message "login.1.pass"/>
			 </label>
		 <p>
		 	 <label id="remember_me" for="_acegi_security_remember_me"><input type="checkbox" name="_acegi_security_remember_me"><@spring.message "login.1.dontask"/>
			 </label>
		 <p>
		 <input name="login" value="<@spring.message "login.1.button"/>" type="submit"> <a class="link" onclick="javascript:doOpenID();">Use OpenID</a>
		</fieldset>
	 </form>	
	 <form id="openIDForm" action="<@spring.url "/site/j_acegi_openid_start"/>" method="POST" style="display: block" onSubmit="javascript:formvalidation();return false;" >
		<fieldset>
			<legend><@spring.message "login.1.header"/></legend>
			 <label for="j_username2"><input type='text' name='openid_url' id = 'j_username2' class="openid-identifier">OpenID
			 </label>		 		 
		 <p>
		 <input type="hidden" name="password"/>
		 <input name="login" value="<@spring.message "login.1.button"/>" type="submit"><a class="link" onclick="javascript:doUsernamePassword();">Use username / password</a>
		  <br>Already created an account? Sign in here.
		</fieldset>
	 </form>	
</#macro>

<#macro signupNow>
	<h2 id="signupNow">
	 	<a href="<@spring.url "/site/signupIfPossible.html"/>"><@spring.message "login.signup"/></a>
	</h2>
</#macro>

<#macro pngImage src width height>
	<#if iePre7?exists>
		<div>
			<span style="display:inline-block;width:${width}px;height:${height}px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<@spring.url "${src}"/>', sizingMethod='scale')"></span>
		</div>
	<#else>
		<!-- commonPNG -->
		<img src=<@spring.url "${src}"/> width=${width} height=${height} border=0/>
	</#if>
</#macro>


<#macro terms>
	<@terms_formatted "", ""/>
</#macro>

<#macro terms_formatted p="<p>",br="<br>">
${p}Thank you for using MyHippocampus. By using MyHippocampus's products, services or web sites ("MyHippocampus"), you agree to the following terms and conditions, and any policies, guidelines or amendments thereto that may be presented to you from time to time (collectively, the "Terms of Service"). We may update the Terms of Service in the future, and you will be able to find the most current version of this agreement at http://www.myhippocampus.com/site/terms.html.
${p}
1. USE OF SERVICES
${p}
MyHippocampus LLC., its subsidiaries and affiliated companies ("MyHippocampus") offer MyHippocampus to you, provided that you are of legal age to form a binding contract and are not a person barred from receiving services under the laws of the United States. In order to access certain services, you may be required to provide current, accurate identification, contact, and other information as part of the registration process and/or continued use of MyHippocampus. You are responsible for maintaining the confidentiality of your account password, and are responsible for all activities that occur under your account. You agree to immediately notify MyHippocampus of any unauthorized use of your password or account or any other breach of security. MyHippocampus cannot and will not be liable for any loss or damage arising from your failure to provide us with accurate information or to keep your password secure.
${p}
2. APPROPRIATE CONDUCT
${p}
You understand that all information, data, text, software, music, sound, photographs, graphics, video, messages or other materials ("Content") are the sole responsibility of the person from which such Content originated. MyHippocampus reserves the right, but shall have no obligation, to pre-screen, refuse or move any Content available via MyHippocampus. You understand that by using MyHippocampus you may be exposed to Content that is offensive, indecent or objectionable, and that you use MyHippocampus at your own risk. In addition, there are commercially available services and software to limit access to material that you may find objectionable.
${p}
You agree that you are responsible for your own conduct and any Content that you create, transmit or display while using MyHippocampus and for any consequences thereof. You agree to use MyHippocampus only for purposes that are legal, proper and in accordance with the Terms of Service and any applicable policies or guidelines. You agree that you will not engage in any activity that interferes with or disrupts MyHippocampus or servers or networks connected to MyHippocampus.
${p}
In addition to this agreement, your use of some specific MyHippocampus is governed by the policies or guidelines that are presented to you when you sign up for or access those services and which are specifically incorporated into this Terms of Service.
${p}
3. MyHippocampus PRIVACY POLICY
${p}
By using MyHippocampus, you acknowledge and agree that MyHippocampus may access, preserve, and disclose your account information and any Content associated with that account if required to do so by law or in a good faith belief that such access preservation or disclosure is reasonably necessary to: (a) satisfy any applicable law, regulation, legal process or enforceable governmental request, (b) enforce this Terms of Service, including investigation of potential violations hereof, (c) detect, prevent, or otherwise address fraud, security or technical issues (including, without limitation, the filtering of spam), or (d) protect against imminent harm to the rights, property or safety of MyHippocampus, its users or the public as required or permitted by law.
${p}
You understand that the technical processing and transmission of MyHippocampus, including your Content, may involve (a) transmissions over various networks; and (b) changes to conform and adapt to technical requirements of connecting networks or devices.
${p}
4. PROPRIETARY RIGHTS
${p}
MyHippocampus's Rights
${p}
You acknowledge and agree that MyHippocampus and any necessary software used in connection with MyHippocampus ("Software") contain proprietary and confidential information that is protected by applicable intellectual property and other laws and treaties. You further acknowledge and agree that Content contained in sponsor advertisements or presented to you through MyHippocampus is protected by copyrights, trademarks, service marks, patents or other proprietary rights and laws. Except as expressly authorized by MyHippocampus or other proper third party rights holders, you agree not to modify, rent, lease, loan, sell, distribute or create derivative works based on MyHippocampus, Content or Software, in whole or in part.
${p}
Subject to the Terms of Service, MyHippocampus grants you a personal, non-transferable and non-exclusive right and license to use the object code of its Software; provided that you do not (and do not allow any third party to) copy, modify, create a derivative work of, reverse engineer, reverse assemble or otherwise attempt to discover any source code, sell, assign, sublicense, grant a security interest in or otherwise transfer any right in the Software, unless such activity is expressly permitted or required by law. You agree not to modify the Software in any manner or form, or to use modified versions of the Software, including (without limitation) for the purpose of obtaining unauthorized access to MyHippocampus. You agree not to access MyHippocampus by any means other than through the interface that is provided by MyHippocampus for use in accessing MyHippocampus.
${p}
Your Rights
${p}
MyHippocampus claims no ownership or control over any Content submitted, posted or displayed by you on or through MyHippocampus. You or a third party licensor, as appropriate, retain all patent, trademark and copyright to any Content you submit, post or display on or through MyHippocampus and you are responsible for protecting those rights, as appropriate. By submitting, posting or displaying Content on or through MyHippocampus which are intended to be available to the general public, you grant MyHippocampus a worldwide, non-exclusive, royalty-free license to reproduce, adapt and publish such Content on MyHippocampus solely for the purpose of displaying, distributing and promoting MyHippocampus. This license terminates when such Content is removed from the MyHippocampus service to which you originally submitted. MyHippocampus reserves the right to syndicate Content submitted, posted or displayed by you on or through MyHippocampus and use that Content in connection with any of the services offered by MyHippocampus. MyHippocampus furthermore reserves the right to refuse to accept, post, display or transmit any Content in its sole discretion.
${p}
5. SOFTWARE AND AUTOMATIC UPDATES
${p}
Your use of any Software provided by MyHippocampus will be governed by this Terms of Service and any additional terms and conditions of the end user license agreement accompanying such Software. MyHippocampus may automatically check your version of the Software and may automatically download upgrades to the Software to update, enhance and further develop MyHippocampus, including providing bug fixes, patches, enhanced functions, missing plug-ins and new versions.
${p}
6. POLICIES REGARDING COPYRIGHT AND TRADEMARKS
${p}
It is our policy to respond to notices of alleged infringement that comply with the United States' Digital Millennium Copyright Act or other applicable law. 
${p}
Any use of MyHippocampus's trade names, trademarks, service marks, logos, domain names, and other distinctive brand features must be in compliance with this Terms of Service.
${p}
7. GENERAL PRACTICES REGARDING USE AND STORAGE
${p}
You agree that MyHippocampus has no responsibility or liability for the deletion or failure to store any Content and other communications maintained or transmitted by MyHippocampus. You acknowledge that MyHippocampus has set no fixed upper limit on the number of transmissions you may send or receive through MyHippocampus or the amount of storage space used; however, we retain the right, at our sole discretion, to create limits at any time with or without notice.
${p}
Upon the termination of your use of MyHippocampus, including upon receipt of a certificate or other legal document confirming your death, MyHippocampus retains the right to close your account so that you will no longer be able to retrieve content contained in that account.
${p}
8. NO RESALE OF SERVICE
${p}
You agree not to reproduce, duplicate, copy, sell, trade, resell or exploit for any commercial purposes, any portion of MyHippocampus, use of MyHippocampus, or access to MyHippocampus.
${p}
9. MODIFICATIONS TO SERVICE
${p}
MyHippocampus reserves the right at any time and from time to time to modify or discontinue, temporarily or permanently, MyHippocampus (or any part thereof) with or without notice. You agree that MyHippocampus shall not be liable to you or to any third party for any modification, suspension or discontinuance of MyHippocampus.
${p}
10. TERMINATION
${p}
You may discontinue your use of MyHippocampus at any time. You agree that MyHippocampus may at any time and for any reason, including a period of account inactivity, terminate your access to MyHippocampus, terminate the Terms of Service, or suspend or terminate your account. In the event of termination, your account will be disabled and you may not be granted access to MyHippocampus, your account or any files or other content contained in your account. Sections 10 (Termination), 13 (Indemnity), 14 (Disclaimer of Warranties), 15 (Limitations of Liability), 16 (Exclusions and Limitations) and 19 (including choice of law, severability and statute of limitations), of the Terms of Service, shall survive expiration or termination.
${p}
11. ADVERTISEMENTS
${p}
Some MyHippocampus features may be supported by advertising revenue and may display advertisements and promotions on the service. The manner, mode and extent of advertising by MyHippocampus on its services are subject to change. You agree that MyHippocampus shall not be responsible or liable for any loss or damage of any sort incurred by you as a result of any such dealings or as the result of the presence of such advertisers on MyHippocampus.
${p}
12. LINKS
${p}
MyHippocampus may provide, or third parties may provide, links to other World Wide Web sites or resources. Because MyHippocampus has no control over such sites and resources, you acknowledge and agree that MyHippocampus is not responsible for the availability of such external sites or resources, and does not endorse and is not responsible or liable for any Content, advertising, products, or other materials on or available from such sites or resources. You further acknowledge and agree that MyHippocampus shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with use of or reliance on any such Content, goods or services available on or through any such site or resource.
${p}
13. INDEMNITY
${p}
You agree to hold harmless and indemnify MyHippocampus, and its subsidiaries, affiliates, officers, agents, and employees, advertisers or partners, from and against any third party claim arising from or in any way related to your use of MyHippocampus, violation of this Terms of Service or any other actions connected with use of MyHippocampus, including any liability or expense arising from all claims, losses, damages (actual and consequential), suits, judgments, litigation costs and attorneys' fees, of every kind and nature. In such a case, MyHippocampus will provide you with written notice of such claim, suit or action.
${p}
14. DISCLAIMER OF WARRANTIES
${p}
YOU EXPRESSLY UNDERSTAND AND AGREE THAT:
${p}
${br}a. YOUR USE OF MyHippocampus IS AT YOUR SOLE RISK. MyHippocampus ARE PROVIDED ON AN "AS IS" AND "AS AVAILABLE" BASIS. TO THE MAXIMUM EXTENT PERMITTED BY LAW, MyHippocampus EXPRESSLY DISCLAIMS ALL WARRANTIES AND CONDITIONS OF ANY KIND, WHETHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT.
${br}b. MyHippocampus DOES NOT WARRANT THAT (i) MyHippocampus WILL MEET YOUR REQUIREMENTS, (ii) MyHippocampus WILL BE UNINTERRUPTED, TIMELY, SECURE, OR ERROR-FREE, (iii) THE RESULTS THAT MAY BE OBTAINED FROM THE USE OF MyHippocampus WILL BE ACCURATE OR RELIABLE, (iv) THE QUALITY OF ANY PRODUCTS, SERVICES, INFORMATION, OR OTHER MATERIAL PURCHASED OR OBTAINED BY YOU THROUGH MyHippocampus WILL MEET YOUR EXPECTATIONS, AND (V) ANY ERRORS IN THE SOFTWARE WILL BE CORRECTED.
${br}c. ANY MATERIAL DOWNLOADED OR OTHERWISE OBTAINED THROUGH THE USE OF MyHippocampus IS DONE AT YOUR OWN DISCRETION AND RISK AND THAT YOU WILL BE SOLELY RESPONSIBLE FOR ANY DAMAGE TO YOUR COMPUTER SYSTEM OR OTHER DEVICE OR LOSS OF DATA THAT RESULTS FROM THE DOWNLOAD OF ANY SUCH MATERIAL.
${br}d. NO ADVICE OR INFORMATION, WHETHER ORAL OR WRITTEN, OBTAINED BY YOU FROM MyHippocampus OR THROUGH OR FROM MyHippocampus SHALL CREATE ANY WARRANTY NOT EXPRESSLY STATED IN THE TERMS OF SERVICE. 
${p}
15. LIMITATION OF LIABILITY
${p}
YOU EXPRESSLY UNDERSTAND AND AGREE THAT MyHippocampus SHALL NOT BE LIABLE TO YOU FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL OR EXEMPLARY DAMAGES, INCLUDING BUT NOT LIMITED TO, DAMAGES FOR LOSS OF PROFITS, GOODWILL, USE, DATA OR OTHER INTANGIBLE LOSSES (EVEN IF MyHippocampus HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES) RESULTING FROM: (i) THE USE OR THE INABILITY TO USE MyHippocampus; (ii) THE COST OF PROCUREMENT OF SUBSTITUTE GOODS AND SERVICES RESULTING FROM ANY GOODS, DATA, INFORMATION OR SERVICES PURCHASED OR OBTAINED OR MESSAGES RECEIVED OR TRANSACTIONS ENTERED INTO THROUGH OR FROM MyHippocampus; (iii) UNAUTHORIZED ACCESS TO OR ALTERATION OF YOUR TRANSMISSIONS OR DATA; (iv) STATEMENTS OR CONDUCT OF ANY THIRD PARTY ON MyHippocampus; OR (v) ANY OTHER MATTER RELATING TO MyHippocampus.
${p}
16. EXCLUSIONS AND LIMITATIONS
${p}
NOTHING IN THIS AGREEMENT IS INTENDED TO EXCLUDE OR LIMIT ANY CONDITION, WARRANTY, RIGHT OR LIABILITY WHICH MAY NOT BE LAWFULLY EXCLUDED OR LIMITED. SOME JURISDICTIONS DO NOT ALLOW THE EXCLUSION OF CERTAIN WARRANTIES OR CONDITIONS OR THE LIMITATION OR EXCLUSION OF LIABILITY FOR LOSS OR DAMAGE CAUSED BY NEGLIGENCE, BREACH OF CONTRACT OR BREACH OF IMPLIED TERMS, OR INCIDENTAL OR CONSEQUENTIAL DAMAGES. ACCORDINGLY, ONLY THE ABOVE LIMITATIONS IN SECTIONS 14 AND 15 WHICH ARE LAWFUL IN YOUR JURISDICTION WILL APPLY TO YOU AND OUR LIABILITY WILL BE LIMITED TO THE MAXIMUM EXTENT PERMITTED BY LAW.
${p}
17. NO THIRD PARTY BENEFICIARIES
${p}
You agree that, except as otherwise expressly provided in this Terms of Service, there shall be no third party beneficiaries to the Terms of Service.
${p}
18. NOTICE
${p}
You agree that MyHippocampus may provide you with notices, including those regarding changes to the Terms of Service, by email, regular mail, or postings on MyHippocampus.
${p}
19. GENERAL INFORMATION
${p}
Entire Agreement. The Terms of Service (including any policies, guidelines or amendments that may be presented to your form time to time) constitute the entire agreement between you and MyHippocampus and govern your use of MyHippocampus, superceding any prior agreements between you and MyHippocampus for the use of MyHippocampus. You also may be subject to additional terms and conditions that may apply when you use or purchase certain other MyHippocampus, affiliate services, third-party content or third-party software.
${p}
Choice of Law and Forum. The Terms of Service and the relationship between you and MyHippocampus shall be governed by the laws of the State of Delaware without regard to its conflict of law provisions. You and MyHippocampus agree to submit to the personal and exclusive jurisdiction of the courts located within the Delaware.
${p}
Waiver and Severability of Terms. The failure of MyHippocampus to exercise or enforce any right or provision of the Terms of Service shall not constitute a waiver of such right or provision. If any provision of the Terms of Service is found by a court of competent jurisdiction to be invalid, the parties nevertheless agree that the court should endeavor to give effect to the parties' intentions as reflected in the provision, and the other provisions of the Terms of Service remain in full force and effect.
${p}
Statute of Limitations. You agree that regardless of any statute or law to the contrary, any claim or cause of action arising out of or related to use of MyHippocampus or the Terms of Service must be filed within one (1) year after such claim or cause of action arose or be forever barred.
${p}
The section headings in the Terms of Service are for convenience only and have no legal or contractual effect.
${p}		 
</#macro>