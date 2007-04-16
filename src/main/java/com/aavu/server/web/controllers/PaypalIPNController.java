package com.aavu.server.web.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import sun.net.www.http.Hurryable;

import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.service.UserService;

/**
 * 
 *	1) secure/upgrade.html has a <form> which goes and pokes paypal with 
 *		an "I want to subscribe"
 *	1.5) We include the userID in the "custom" field
 *	2) Paypal sends us and IPN to myhippocampus/site/ipn when they've signed up, then IPNs when payment is approved.
 *
 *	3) On Signup IPN receipt:
 *	3.1) We send a req to Paypal saying "Really? Are we not just being spoofed"?
 *	3.2) If we get VALID, we retrieve the custom field, store the paypal_id and give the user the new subscription.
 *
 *	4) On cancel IPN receipt, we lookup the paypal_id and cancel it.
 *	
 * @author Jeff Dwyer
 *
 */
public class PaypalIPNController extends AbstractController {

	private static final Logger log = Logger.getLogger(PaypalIPNController.class);
	
	private static final String ENCODING = "UTF-8";
	
	private static final String POST_BACK_VALIDATE = "cmd=_notify-validate";
	
	private static final String PAYPAL_SUBSCR_FAILED = "subscr_failed";
	
	
	private static final String PAYPAL_SUBSCR_CANCEL = "subscr_cancel";
	private static final String PAYPAL_SUBSCR_PAYMENT = "subscr_payment";
	private static final String PAYPAL_SUBSCR_SIGNUP = "subscr_signup";
	private static final String PAYPAL_SUBSCR_EOT = "subscr_eot";
	private static final String PAYPAL_SUBSCR_MODIFY = "subscr_modify";
	
	private UserService userService;
	private String view;
	private String paypalEndpoint;
	
	public void setView(String view) {
		this.view = view;
	}


	private boolean checkPaymentStatus(String status){
//		Canceled_Reversal
//		Completed
//		Denied
//		Expired
//		Failed
//		Pending
//		Processed
//		Refunded
//		Reversed
//		Voided

		return false;
	}
		
		
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		
		log.debug("PayPalIPN received ");
		
		//read post from PayPal system and add 'cmd'
		Enumeration en = request.getParameterNames();
		
		StringBuilder postBack = new StringBuilder(POST_BACK_VALIDATE);
		while(en.hasMoreElements()){
			String paramName = (String)en.nextElement();
			String paramValue = request.getParameter(paramName);
			postBack.append("&");
			postBack.append(paramName);
			postBack.append("=");			
			postBack.append(URLEncoder.encode(paramValue, ENCODING));			
		}
		
		log.debug("PayPalPostBack "+postBack.toString());
		

		log.debug("Sending PostBack");
		
		//post back to PayPal system to validate
		//NOTE: change http: to https: in the following URL to verify using SSL (for increased security).
		//using HTTPS requires either Java 1.4 or greater, or Java Secure Socket Extension (JSSE)
		//and configured for older versions.
		URL u = new URL(paypalEndpoint);
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(postBack.toString());
		pw.close();


		log.debug("PostBackSent");
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(uc.getInputStream()));
		String res = in.readLine();
		in.close();
		
		log.debug("Return From "+paypalEndpoint);
				
		//assign posted variables to local variables
		String itemName = request.getParameter("item_name");
		String itemNumber = request.getParameter("item_number");
		String paymentStatus = request.getParameter("payment_status");
		String paymentAmount = request.getParameter("mc_gross");
		String paymentCurrency = request.getParameter("mc_currency");
		String txnId = request.getParameter("txn_id");
		String txnType = request.getParameter("txn_type");
		String receiverEmail = request.getParameter("receiver_email");
		String payerEmail = request.getParameter("payer_email");
		
		//unique paypal ID. store this in user account to associate 
		//this paypal account with our user. Only accept cancel reqs
		//from when this user matches. Otherwise 1 user could cancel another
		//user's accounts.
		String payerID = request.getParameter("payer_id");
		
		log.debug("ItemName "+itemName+"\n"+
				"itemNumber "+itemNumber+"\n"+
				"paymentStatus "+paymentStatus+"\n"+
				"paymentAmount "+paymentAmount+"\n"+
				"paymentCurrency "+paymentCurrency+"\n"+
				"txnId "+txnId+"\n"+
				"txnType "+txnType+"\n"+
				"receiverEmail "+receiverEmail+"\n"+
				"payerEmail "+payerEmail+"\n"+
				"payerID "+payerID+"\n"+
				"custom: "+request.getParameter("custom"));
		
		long hippoUserID = -1;
		String custom =  request.getParameter("custom");	
		if(custom != null){
			try{			
				hippoUserID = Long.parseLong(custom);
			}catch (NumberFormatException e) {
				log.warn(e.getMessage());
			}
		}//else custom will be null on CANCEL reqs
		
		String messageRtn = "";
		
		//check notification validation
		if(res.equals("VERIFIED")) {
			//check that paymentStatus=Completed
			//check that txnId has not been previously processed
			//check that receiverEmail is your Primary PayPal email
			//check that paymentAmount/paymentCurrency are correct
			//process payment
			
			log.debug("VERIFIED");
			log.debug("ItemName "+itemName+"\n"+
					"itemNumber "+itemNumber+"\n"+
					"paymentStatus "+paymentStatus+"\n"+
					"paymentAmount "+paymentAmount+"\n"+
					"paymentCurrency "+paymentCurrency+"\n"+
					"txnId "+txnId+"\n"+
					"txnType "+txnType+"\n"+
					"receiverEmail "+receiverEmail+"\n"+
					"payerEmail "+payerEmail+"\n");
			
			long subscriptionID = Long.parseLong(itemNumber);
			
			
			processIPN(txnType,hippoUserID,subscriptionID,payerID,payerEmail);
									
			messageRtn = "Verified";
		}
		else if(res.equals("INVALID")) {
			//log for investigation
			log.warn("PAYPAL INVALID ");
			log.warn("ItemName "+itemName+"\n"+
					"itemNumber "+itemNumber+"\n"+
					"paymentStatus "+paymentStatus+"\n"+
					"paymentAmount "+paymentAmount+"\n"+
					"paymentCurrency "+paymentCurrency+"\n"+
					"txnId "+txnId+"\n"+
					"txnType "+txnType+"\n"+
					"receiverEmail "+receiverEmail+"\n"+
					"payerEmail "+payerEmail+"\n");

			messageRtn = "Invalid";
		}
		
		else {
			//error
			log.error("PAYPAL neither INVALID nor COMPLETED");
			log.warn("ItemName "+itemName+"\n"+
					"itemNumber "+itemNumber+"\n"+
					"paymentStatus "+paymentStatus+"\n"+
					"paymentAmount "+paymentAmount+"\n"+
					"paymentCurrency "+paymentCurrency+"\n"+
					"txnId "+txnId+"\n"+
					"txnType "+txnType+"\n"+
					"receiverEmail "+receiverEmail+"\n"+
					"payerEmail "+payerEmail+"\n");
			
			messageRtn = "Error";
		}
		
		
		
		return new ModelAndView(view,"message",messageRtn);
	}


	/**
	 * 
	 * see http://www.paypaldev.org/topic.asp?TOPIC_ID=4941
	 * 
	 * We generally recommend procssing subscr_signup and subscr_eot and disregarding all other subsription-related IPNs if possible.
	 * 
	 * but what about cancel? For now let's cancel...
	 * 
	 * @param txn_type
	 * @param subscriptionID
	 * @param subscriptionID 
	 * @param paypalID
	 * @param payerEmail
	 * @throws HippoBusinessException 
	 * @throws HippoInfrastructureException 
	 */
	private void processIPN(String txn_type,long hippoUserID,long subscriptionID, String paypalID, String payerEmail) throws HippoBusinessException, HippoInfrastructureException {
		
		
		if(txn_type.equals(PAYPAL_SUBSCR_SIGNUP)){			
			log.info("IPN Signup "+paypalID+" "+hippoUserID);
			
			if(hippoUserID == -1){
				throw new HippoInfrastructureException("No custom variable in Paypal return.");
			}
			userService.subscriptionNewSignup(hippoUserID,paypalID,subscriptionID,payerEmail);			
		}
		else if(txn_type.equals(PAYPAL_SUBSCR_EOT)){
			log.warn("IPN EndOfTerm "+paypalID+" "+hippoUserID);

			userService.subscriptionCancel(paypalID);

		}
		else if(txn_type.equals(PAYPAL_SUBSCR_FAILED)){
			log.warn("IPN Failed "+paypalID+" "+hippoUserID);
		}
		else if(txn_type.equals(PAYPAL_SUBSCR_MODIFY)){
			log.info("IPN Modify "+paypalID+" "+hippoUserID);
		}
		else if(txn_type.equals(PAYPAL_SUBSCR_PAYMENT)){
			log.info("IPN Paid "+paypalID+" "+hippoUserID);
			if(hippoUserID == -1){
				throw new HippoInfrastructureException("No custom variable in Paypal return.");
			}
			userService.subscriptionRecordPayment(hippoUserID,paypalID);
		}
		else if(txn_type.equals(PAYPAL_SUBSCR_CANCEL)){
			log.info("IPN CANCEL "+paypalID+" "+hippoUserID);
			
			userService.subscriptionCancel(paypalID);
		}else{
			log.error("txn_type: Invalid "+paypalID);
			throw new HippoBusinessException("Invalid txn_type "+txn_type);
		}
		
		log.info("IPN Process Complete "+paypalID+" "+hippoUserID+" "+txn_type);
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void setPaypalEndpoint(String paypalEndpoint) {
		this.paypalEndpoint = paypalEndpoint;
	}
	
	
//	try {
//		url = new URL(PAYPAL_VERIFY_URL);
//		con = (HttpsURLConnection) url.openConnection(); 
//		} catch (MalformedURLException e1) {
//		e1.printStackTrace();
//		} catch (IOException e1) {
//		e1.printStackTrace();
//		}
//
////		 set up url connection to post information and
////		 retrieve information back
//		try {
//		con.setRequestMethod("POST");
//		} catch (ProtocolException e2) {
//		e2.printStackTrace();
//		}
//		con.setDoInput( true );
//		con.setDoOutput( true );
//		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//
////		 add url form parameters
//		DataOutputStream ostream = null;
//		try {
//		ostream = new DataOutputStream(con.getOutputStream());
//		System.out.println( "adding post parameters: " + sb.toString() );
//		ostream.writeBytes( sb.toString() );
//		} catch(IOException ioe) {
//		ioe.printStackTrace(); 
//		} finally {
//		if(ostream != null) {
//		try {
//		ostream.flush();
//		ostream.close();
//		} catch(IOException ioe) {
//		ioe.printStackTrace();
//		}
//		}
//		}
//
//		StringBuffer buf = null;
//		try {
//		Object contents = con.getContent();
//		InputStream is = (InputStream) contents;
//		buf = new StringBuffer();
//		int c;
//		while( ( c = is.read() ) != -1 ) {
//		buf.append( (char) c );
//		}
//		} catch (IOException e) {
//		e.printStackTrace();
//		} finally {
//		con.disconnect();
//		}
//		return buf.toString();
//		} 



}
