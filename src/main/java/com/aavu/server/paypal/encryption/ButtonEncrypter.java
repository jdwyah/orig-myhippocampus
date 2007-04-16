package com.aavu.server.paypal.encryption;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoInfrastructureException;

public class ButtonEncrypter {
	
	private static final Logger log = Logger.getLogger(ButtonEncrypter.class);
	
	private String certPath;
	private String keyPath;
	private String paypalCertPath;
	private String keyPass;

	public String getButton(User user, Subscription subscription) throws HippoInfrastructureException {

		String cmdText = "";
		
		ClientSideEncrypter client_side = new ClientSideEncrypter( keyPath, certPath, paypalCertPath, keyPass );

		String result;
		try {
			result = client_side.getButtonEncryptionValue( cmdText, keyPath, certPath, paypalCertPath, keyPass );
		} catch (Exception e) {
			log.error(e);
			throw new HippoInfrastructureException(e);
		} 
		
		return result;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public void setPaypalCertPath(String paypalCertPath) {
		this.paypalCertPath = paypalCertPath;
	}
}
