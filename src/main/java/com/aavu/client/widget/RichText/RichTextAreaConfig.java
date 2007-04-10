/*
 * Created on 2006/07/27 18:41:15 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import java.util.List;
import java.util.Set;

public interface RichTextAreaConfig {
	/**
	 * valid fonts list.
	 * @return
	 */
	public List getFonts();
	
	/**
	 * return all valid font-sizes.
	 * for example: {{"1","small"},{"2","normal"},{"3","large"}}
	 */
	public String[][] getFontSizes();
	/**
	 * Toolbars & toolButtons
	 * 
	 * @return
	 */
	public String[][] getToolBars();
	
	/**
	 * background color of toolbar
	 * @return
	 */
	public String getToolBarBackgroundColor();
	/**
	 * the background color of tool buttons when mouse over it
	 * @return String 
	 */
	public String getToolButtonOverBackgroundColor();
	/**
	 * the border color of tool buttons when mouse over it
	 * @return String 
	 */
	public String getToolButtonOverBorderColor();
	
	
	//mouse down
	/**
	 * the background color of tool buttons when mouse down 
	 * @return String 
	 */
	public String getToolButtonDownBackgroundColor();
	/**
	 * the border color of tool buttons when mouse down
	 * @return String 
	 */
	public String getToolButtonDownBorderColor();
	//normal (state off or stateless button)
	/**
	 * the background color of tool buttons 
	 * @return String 
	 */
	public String getToolButtonBackgroundColor();
	/**
	 * the border color of tool buttons
	 * @return String 
	 */
	public String getToolButtonBorderColor();
	//normal (state on)
	/**
	 * the background color of tool buttons when a tool button's state is on
	 * @return String 
	 */
	public String getToolButtonOnBackgroundColor();
	/**
	 * the border color of tool buttons when a tool button's state is on
	 * @return String 
	 */
	public String getToolButtonOnBorderColor();
	
	/**
	 * All custom defined tool buttons.
	 * @return Set
	 */
	public Set getCustomToolButtons();

	/**
	 * find one custom defined tool button by id
	 * @param id  Id of the custom defined tool button
	 * @return custom defined ToolButton
	 */
	public ToolButton getCustomToolButton(String id);
	
	
	/**
	 * background color of drop down panel.
	 * @return
	 */
	public String getDropDownPanelBackgroundColor();
	/**
	 * border color of drop down panel
	 * @return
	 */
	public String getDropDownPanelBorderColor();
	
	/**
	 * background color of drop down menu ietms when mouse over it.
	 * @return
	 */
	public String getMenuItemMouseOverBackgroundColor();
	/**
	 * border color of drop down menu items when mouse over it.
	 * @return
	 */
	public String getMenuItemMouseOverBorderColor();
}
