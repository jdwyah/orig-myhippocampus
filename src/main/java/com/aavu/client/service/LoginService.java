package com.aavu.client.service;

import org.gwm.client.GInternalFrame;

import com.aavu.client.gui.LoginWindow;
import com.aavu.client.strings.Consts;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginService {

	
	
	/**
	 * Note: LoginWindow has a semaphore to prevent multiple instances
	 * Will bring up a dialog that on success will call loginSuccess()
	 * @param callback 
	 * @param frame 
	 * @param myConstants 
	 * 
	 */
	public static void doLogin(GInternalFrame frame, LoginListener listener) {
		LoginWindow lw = new LoginWindow(frame,listener);
	}
	
	
	
}
