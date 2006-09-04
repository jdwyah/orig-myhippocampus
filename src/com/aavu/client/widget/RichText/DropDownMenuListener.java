/*
 * Created on 2006/07/29 19:03:40 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

public interface DropDownMenuListener {
	/**
	 * fired when user click one menu item, 
	 * @param row the index of the clicked item
	 */
	public void onClick(int row);
	/**
	 * fired when user click one menu item, 
	 * @param clickedItemValue the value of the clicked item
	 */
	public void onClick(String clickedItemValue);
	/**
	 * the event will fired when this menu set to visible
	 * if wanna checked some menu, write code in this event.
	 */
	public void onShow();

}
