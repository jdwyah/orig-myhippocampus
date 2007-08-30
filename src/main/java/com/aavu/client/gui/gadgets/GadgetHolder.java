package com.aavu.client.gui.gadgets;

public interface GadgetHolder {

	boolean isVisible();

	void setVisible(boolean b);

	void setTop(int i);

	void setLeft(int i);

	void fireSizeChanged();

}
