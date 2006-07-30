package com.aavu.client.widget;

import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;

public class AutoCompleteTextBoxMultipleCompletes extends AutoCompleteTextBox {


	
	//eek
	//what about topic names with spaces??
	public static final String SEPARATOR = ";";

	//Override//	grr java 1.5 
	public void process(){

		String baseText = this.getText();
		
		int liof = baseText.lastIndexOf(SEPARATOR);

		//reset for when there's no separator
		if(liof == -1){
			liof = 0;
		}else{
			//skip the separator char
			liof++;
		}
		
		String text = baseText.substring(liof);
		System.out.println("Base: |"+baseText+"|");
		System.out.println("Text: |"+text+"|");
		matches = new String[] {};
		if (text.length() > 0) {
			items.getCompletionItems(text, this);
		} else {
			onMatch(text);
		}

	}

	//add selected item to textbox
	//Override //grr java 1.5 
	protected void complete(){
		if(choices.getItemCount() > 0 && choices.getSelectedIndex() != -1){
			
			
			//don't overwrite previously entered text
			String curText = this.getText();
			int liof = curText.lastIndexOf(SEPARATOR);
			
			//set to put nothing in root
			if(liof == -1){
				liof = 0;
			}else{
				//keep SEPARATOR in the window
				liof++;
			}
			String root = curText.substring(0,liof);
			
			this.setText(root+choices.getItemText(choices.getSelectedIndex()));
		}

		choices.clear();
		choicesPopup.hide();
	}



}


