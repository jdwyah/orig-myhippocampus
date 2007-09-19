package com.aavu.client;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.service.MindscapeManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class UserClientApp extends AbstractClientApp implements HistoryListener {



	public static final String LOCAL_HOST = "http://localhost:8080/";
	public static final String REMOTE_HOST = "http://www.myhippocampus.com/";
	public static final String MAIN_DIV = "slot1";

	private MindscapeManager manager;


	private void loadGUI(Widget widget) {
		RootPanel.get("loading").setVisible(false);
		RootPanel.get(MAIN_DIV).add(widget);

	}

	/**
	 * This is the entry point method.
	 * 
	 * -T-ODO HIGH we're running map setup TWICE. once from LoginWindow. secondly because
	 * onModuleLoad() is getting called right after the login. Why is this? It couldn't be the
	 * addition of the iframe code could it?
	 * 
	 * SOLVED. It was the IFRAME. let's make another EntryPoint that just pre-loads stuff.
	 * 
	 * NOTE the semaphore code below is inefectual even with the var static. have_initted is false
	 * both times. It looks like we're re-initting the class. this reports different objectIDs
	 * 
	 * 
	 * 
	 */
	public UserClientApp() {
		super();

	}

	// @Override
	protected void setMeUp() {
		manager = new MindscapeManager(getHippoCache());

		// static service setters.
		// hopefully replace with Spring DI
		//
		// TopicCompleter.setTopicService(hippoCache.getTopicCache());
		MetaDate.setTopicService(getHippoCache().getTopicCache());
		StdAsyncCallback.setManager(manager);

		// Window.alert("5");

		loadGUI(manager.getRootWidget());

		manager.setup("ORIG");

		String initToken = History.getToken();
		if (initToken.length() > 0) {
			onHistoryChanged(initToken);
		}

		History.addHistoryListener(this);
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
