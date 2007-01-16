package com.aavu.client.gui.ext.tabbars;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface TabHasWidgets extends HasWidgets {

	void hideDeck();

	void showDeck();

	void add(Widget w, String string, boolean b);

}
