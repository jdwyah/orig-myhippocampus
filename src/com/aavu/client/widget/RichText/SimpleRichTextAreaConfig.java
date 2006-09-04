/*
 * Created on 2006/07/26 22:49:50 David <davidge7@gmail.com>
 */
package com.aavu.client.widget.RichText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SimpleRichTextAreaConfig implements RichTextAreaConfig {
	public static final RichTextAreaConfig DEFAULT = getInstance("default");
	public static final RichTextAreaConfig SILVER = getInstance("silver");
	public static final RichTextAreaConfig BLUE = getInstance("blue");
	
	private String toolBarBackgroundColor;
	//mouse over
	private String toolButtonOverBackgroundColor;
	private String toolButtonOverBorderColor;
	//mouse down
	private String toolButtonDownBackgroundColor;
	private String toolButtonDownBorderColor;
	//normal (state off or stateless button)
	private String toolButtonBackgroundColor;
	private String toolButtonBorderColor;
	//normal (state on)
	private String toolButtonOnBackgroundColor;
	private String toolButtonOnBorderColor;
	//drop down panel
	private String dropDownPanelBackgroundColor;
	private String dropDownPanelBorderColor;
	//drop down menu item
	private String menuItemMouseOverBorderColor;
	private String menuItemMouseOverBackgroundColor;	
	
	private List fonts = new ArrayList();
	private String[][] fontSizes = new String[][]{{"1", "Small"},{"2","normal"}};
	private Set customToolButtons = new HashSet();

	//toolBars
	private String[][] toolBars;
	
	public SimpleRichTextAreaConfig(){
	}
	
	public List getFonts() {
		return fonts;
	}

	public void setFonts(List fonts) {
		this.fonts = fonts;
	}
	public void addFont(String font){
		if(this.fonts == null){
			this.fonts = new ArrayList();
		}
		this.fonts.add(font);
	}
	public void removeFont(String font){
		if(this.fonts == null){
			this.fonts = new ArrayList();
		}else{
			this.fonts.remove(font);
		}
	}

	public String getToolBarBackgroundColor() {
		return toolBarBackgroundColor;
	}

	public void setToolBarBackgroundColor(String toolBarBackgroundColor) {
		this.toolBarBackgroundColor = toolBarBackgroundColor;
	}

	public String getToolButtonDownBackgroundColor() {
		return toolButtonDownBackgroundColor;
	}

	public void setToolButtonDownBackgroundColor(
			String toolButtonDownBackgroundColor) {
		this.toolButtonDownBackgroundColor = toolButtonDownBackgroundColor;
	}

	public String getToolButtonDownBorderColor() {
		return toolButtonDownBorderColor;
	}

	public void setToolButtonDownBorderColor(String toolButtonDownBorderColor) {
		this.toolButtonDownBorderColor = toolButtonDownBorderColor;
	}

	public String getToolButtonOverBackgroundColor() {
		return toolButtonOverBackgroundColor;
	}

	public void setToolButtonOverBackgroundColor(
			String toolButtonOverBackgroundColor) {
		this.toolButtonOverBackgroundColor = toolButtonOverBackgroundColor;
	}

	public String getToolButtonOverBorderColor() {
		return toolButtonOverBorderColor;
	}

	public void setToolButtonOverBorderColor(String toolButtonOverBorderColor) {
		this.toolButtonOverBorderColor = toolButtonOverBorderColor;
	}
	
	public String getToolButtonBackgroundColor() {
		return toolButtonBackgroundColor;
	}

	public void setToolButtonBackgroundColor(String toolButtonBackgroundColor) {
		this.toolButtonBackgroundColor = toolButtonBackgroundColor;
	}

	public String getToolButtonBorderColor() {
		return toolButtonBorderColor;
	}

	public void setToolButtonBorderColor(String toolButtonBorderColor) {
		this.toolButtonBorderColor = toolButtonBorderColor;
	}

	public String getToolButtonOnBackgroundColor() {
		return toolButtonOnBackgroundColor;
	}

	public void setToolButtonOnBackgroundColor(String toolButtonOnBackgroundColor) {
		this.toolButtonOnBackgroundColor = toolButtonOnBackgroundColor;
	}

	public String getToolButtonOnBorderColor() {
		return toolButtonOnBorderColor;
	}

	public void setToolButtonOnBorderColor(String toolButtonOnBorderColor) {
		this.toolButtonOnBorderColor = toolButtonOnBorderColor;
	}

	public String getDropDownPanelBackgroundColor() {
		return dropDownPanelBackgroundColor;
	}

	public void setDropDownPanelBackgroundColor(String dropDownPanelBackgroundColor) {
		this.dropDownPanelBackgroundColor = dropDownPanelBackgroundColor;
	}

	public String getDropDownPanelBorderColor() {
		return dropDownPanelBorderColor;
	}

	public void setDropDownPanelBorderColor(String dropDownPanelBorderColor) {
		this.dropDownPanelBorderColor = dropDownPanelBorderColor;
	}

	public String getMenuItemMouseOverBackgroundColor() {
		return menuItemMouseOverBackgroundColor;
	}

	public void setMenuItemMouseOverBackgroundColor(
			String menuItemMouseOverBackgroundColor) {
		this.menuItemMouseOverBackgroundColor = menuItemMouseOverBackgroundColor;
	}

	public String getMenuItemMouseOverBorderColor() {
		return menuItemMouseOverBorderColor;
	}

	public void setMenuItemMouseOverBorderColor(String menuItemMouseOverBorderColor) {
		this.menuItemMouseOverBorderColor = menuItemMouseOverBorderColor;
	}
	public void setFontSizes(String[][] fontSizes){
		this.fontSizes = fontSizes;
	}
	public String[][] getFontSizes() {
		return fontSizes;
	}

	public void addCustomToolButton(ToolButton tb){
		customToolButtons.add(tb);
	}
	public void removeCustomToolButton(ToolButton tb){
		customToolButtons.remove(tb);
	}
	public Set getCustomToolButtons(){
		return customToolButtons;
	}
	public ToolButton getCustomToolButton(String id){
		for(Iterator it = customToolButtons.iterator(); it.hasNext(); ){
			ToolButton btn = (ToolButton) it.next();
			if(btn.getId().equalsIgnoreCase(id)){
				return btn;
			}
		}
		return null;
	}
	
	public String[][] getToolBars() {
		return toolBars;
	}

	public void setToolBars(String[][] toolBars) {
		this.toolBars = toolBars;
	}

	public static RichTextAreaConfig getInstance(String id){
		SimpleRichTextAreaConfig trac = new SimpleRichTextAreaConfig();
		if(id.equals("default")){
			trac.toolBarBackgroundColor = "#EFEFDE";
			//mouse over
			trac.toolButtonOverBackgroundColor = "#DFF1FF";
			trac.toolButtonOverBorderColor = "#316AC5";
			//mouse down
			trac.toolButtonDownBackgroundColor = "#C1D2EE";
			trac.toolButtonDownBorderColor = "#316AC5";
			//normal (state off or stateless button)
			trac.toolButtonBackgroundColor = "#EFEFDE";
			trac.toolButtonBorderColor = "#EFEFDE";
			//normal (state on)
			trac.toolButtonOnBackgroundColor = "#C1D2EE";
			trac.toolButtonOnBorderColor = "#316AC5";
			//drop down panel
			trac.dropDownPanelBackgroundColor = "#EEEEEE";
			trac.dropDownPanelBorderColor = "#BCBCBC";
			//drop down menu item
			trac.menuItemMouseOverBorderColor = "#7D7D7D";
			trac.menuItemMouseOverBackgroundColor = "#DDDDDD";
			//font sizes
			trac.fontSizes = new String[][]{
					{"1", "Small"},
					{"2", "Normal"},
					{"4", "Large"},
					{"6", "Huge"},
				};
			trac.fonts.clear();
			//trac.fonts.add("Normal");
			trac.fonts.add("Times New Roman");
			trac.fonts.add("Arial");
			trac.fonts.add("Courier New");
			trac.fonts.add("Georgia");
			trac.fonts.add("Trebuchet");
			trac.fonts.add("Verdana");

			//toolbars
			trac.toolBars = new String[][]{
					{"bold", "italic", "underline", "-", "font", "font-size", "-", 
						"color", "background", "-", "link", "-", 
						"numbered-list", "bulleted-list", "-", 
						"indent-more", "indent-less", "-", 
						"align-left", "align-center","align-right", "-",  
						"remove-formatting"}, 
				};
			
		}else if(id.equals("silver")){
			trac.toolBarBackgroundColor = "#DDDDE9";
			//mouse over
			trac.toolButtonOverBackgroundColor = "#FFE3AF";
			trac.toolButtonOverBorderColor = "#000000";
			//mouse down
			trac.toolButtonDownBackgroundColor = "#FEA662";
			trac.toolButtonDownBorderColor = "#000000";
			//normal (state off or stateless button)
			trac.toolButtonBackgroundColor = "#DDDDE9";
			trac.toolButtonBorderColor = "#DDDDE9";
			//normal (state on)
			trac.toolButtonOnBackgroundColor = "#FBA763";
			trac.toolButtonOnBorderColor = "#000000";
			//drop down panel
			trac.dropDownPanelBackgroundColor = "#FDFAFF";
			trac.dropDownPanelBorderColor = "#000000";
			//drop down menu item
			trac.menuItemMouseOverBorderColor = "#000000";
			trac.menuItemMouseOverBackgroundColor = "#DDDDE9";
			//font sizes
			trac.fontSizes = new String[][]{
					{"1", "Small"},
					{"2", "Normal"},
					{"4", "Large"},
					{"6", "Huge"},
				};
			trac.fonts.clear();
			//trac.fonts.add("Normal");
			trac.fonts.add("Times New Roman");
			trac.fonts.add("Arial");
			trac.fonts.add("Courier New");
			trac.fonts.add("Georgia");
			trac.fonts.add("Trebuchet");
			trac.fonts.add("Verdana");

			//toolbars
			trac.toolBars = new String[][]{
					{"bold", "italic", "underline", "-", "font", "font-size", "-", 
						"color", "background", "-", "link", "-", 
						"numbered-list", "bulleted-list", "-", 
						"indent-more", "indent-less", "-", 
						"align-left", "align-center","align-right", "-",  
						"remove-formatting"}, 
				};
			
		}else if(id.equals("blue")){
			trac.toolBarBackgroundColor = "#E0ECFF";
			//mouse over
			trac.toolButtonOverBackgroundColor = "#C3D9FF";
			trac.toolButtonOverBorderColor = "#6600FF";
			//mouse down
			trac.toolButtonDownBackgroundColor = "#C3D9FF";
			trac.toolButtonDownBorderColor = "#6600FF";
			//normal (state off or stateless button)
			trac.toolButtonBackgroundColor = "#E0ECFF";
			trac.toolButtonBorderColor = "E0ECFF";
			//normal (state on)
			trac.toolButtonOnBackgroundColor = "#C3D9FF";
			trac.toolButtonOnBorderColor = "#6600FF";
			//drop down panel
			trac.dropDownPanelBackgroundColor = "#E0ECFF";
			trac.dropDownPanelBorderColor = "#330099";
			//drop down menu item
			trac.menuItemMouseOverBorderColor = "#C3D9FF";
			trac.menuItemMouseOverBackgroundColor = "#5570CC";
			//font sizes
			trac.fontSizes = new String[][]{
					{"1", "Small"},
					{"2", "Normal"},
					{"4", "Large"},
					{"6", "Huge"},
				};
			trac.fonts.clear();
			//trac.fonts.add("Normal");
			trac.fonts.add("Times New Roman");
			trac.fonts.add("Arial");
			trac.fonts.add("Courier New");
			trac.fonts.add("Georgia");
			trac.fonts.add("Trebuchet");
			trac.fonts.add("Verdana");

			//toolbars
			trac.toolBars = new String[][]{
					{"bold", "italic", "underline", "-", "font", "font-size", "-", 
						"color", "background", "-", "link", "-", 
						"numbered-list", "bulleted-list", "-", 
						"indent-more", "indent-less", "-", 
						"align-left", "align-center","align-right", "-",  
						"remove-formatting"}, 
				};
			
		}

		return trac;
	}

}
