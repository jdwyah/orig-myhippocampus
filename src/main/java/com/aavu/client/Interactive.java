package com.aavu.client;

import com.aavu.client.exception.MyUncaughtExceptionHandler;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Interactive implements EntryPoint {


	public static final String LOCAL_HOST = "http://localhost:8080/";
	public static final String REMOTE_HOST = "http://www.myhippocampus.com/";

	/**
	 * Switch between localhost for testing &
	 */
	public static String getRelativeURL(String url) {
		String realModuleBase;

		if (GWT.isScript()) {

			Logger.log("ModuleBaseURL: " + GWT.getModuleBaseURL());

			String moduleBase = GWT.getModuleBaseURL();

			// Use for Deployment to production server
			//
			realModuleBase = REMOTE_HOST;

			// Use to test compiled browser locally
			//
			if (moduleBase.indexOf("myhippocampus.com") == -1) {
				Logger.log("Testing. Using Localhost");
				realModuleBase = LOCAL_HOST;
			}


		} else {
			// realModuleBase = GWT.getModuleBaseURL();

			// This is the URL for GWT Hosted mode
			//
			realModuleBase = LOCAL_HOST;
		}

		return realModuleBase + url;
	}


	/**
	 * EntryPoint. Dispatch based on javascript dictionary that tells us what we should load.
	 * 
	 * <script language="JavaScript"> var Vars = { page: "MyMindscape" }; </script>
	 * 
	 */
	public void onModuleLoad() {
		try {
			GWT.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

			Dictionary dictionary = Dictionary.getDictionary("Vars");

			String page = dictionary.get("page");

			if (page.equals("MyMindscape")) {
				UserClientApp m = new UserClientApp();
			} else if (page.equals("AddLink")) {
				AddLink addLink = new AddLink();

			} else if (page.equals("HippocampusBrowser")) {

				String userIDStr = dictionary.get("userID");
				String username = dictionary.get("username");
				String topicIDStr = dictionary.get("topicID");
				BrowserClientApp browser = new BrowserClientApp(userIDStr, username, topicIDStr);

			} else {
				throw new Exception("Vars['page'] not set.");
			}

		} catch (Exception e) {
			Logger.log("e: " + e);

			e.printStackTrace();

			VerticalPanel panel = new VerticalPanel();

			panel.add(new Label("Error"));
			panel.add(new Label(e.getMessage()));

			RootPanel.get("loading").setVisible(false);
			RootPanel.get("slot1").add(panel);

		}

	}

}
