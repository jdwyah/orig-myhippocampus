package com.aavu.client;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.User;
import com.aavu.client.service.BrowserManager;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class BrowserClientApp extends AbstractClientApp implements HistoryListener {

	public static final String MAIN_DIV = "slot1";


	private Manager manager;


	private String userIDStr;

	private String topicIDStr;


	private String username;



	public BrowserClientApp(String userIDStr, String username, String topicIDStr) {
		try {

			System.out.println("new Browser " + userIDStr + " " + username + " " + topicIDStr);

			this.userIDStr = userIDStr;
			this.username = username;
			this.topicIDStr = topicIDStr;

			initServices();

			setMeUp();


		} catch (Exception e) {
			error(e);
		}

	}



	// @Override
	protected void setMeUp() {


		User u = new User();
		u.setId(Long.parseLong(userIDStr));
		u.setUsername(username);

		manager = new BrowserManager(getHippoCache(), u);

		MetaDate.setTopicService(getHippoCache().getTopicCache());
		StdAsyncCallback.setManager(manager);

		loadGUI(manager.getRootWidget());

		manager.setup("Original");


		// Location loc = WindowUtils.getLocation();
		//
		// System.out.println("Query " + loc.getQueryString());
		// System.out.println("href " + loc.getHref());
		//
		// for (Iterator iterator = loc.getParameterMap().keySet().iterator(); iterator.hasNext();)
		// {
		// String key = (String) iterator.next();
		// System.out.println("Key " + key + " Value " + loc.getParameterMap().get(key));
		// }



		String initToken = History.getToken();

		System.out.println("Browser Startup token: " + initToken);

		if (initToken != null) {
			System.out.println("Len " + initToken.length());
		}
		System.out.println("TopicID " + topicIDStr);


		if (initToken.length() > 0) {
			System.out.println("Do history change");
			onHistoryChanged(initToken);
		} else {
			System.out.println("goto " + topicIDStr);
			manager.gotoTopic(topicIDStr);
		}



		History.addHistoryListener(this);
	}


	private void loadGUI(Widget widget) {
		RootPanel.get("loading").setVisible(false);
		RootPanel.get(MAIN_DIV).add(widget);
	}

	public void onHistoryChanged(String historyToken) {
		// This method is called whenever the application's history changes. Set
		// the label to reflect the current history token.
		System.out.println("history changed to " + historyToken);

		// manager == null if we open directly to the page with a #link
		//
		if (historyToken != EMPTY && manager != null) {
			manager.gotoTopic(historyToken);

			// was giving us an endless loop..
			// /* huh... seems to be fine in IE, but FF fires a reload and the
			// request fails.
			// * change to "-1"
			// *
			// * also if we open topic 193. then close the window. then open
			// 193, it will think we've
			// * already opened it.
			// */
			// History.newItem(EMPTY);
		}
	}
}
