package com.aavu.client.widget.edit;

import com.aavu.client.widget.autocompletion.AutoCompleteTextBox;

public class AutoCompleteTextBoxWithCompleteCallback extends AutoCompleteTextBox {
	
	private CompleteListener listener;
	

	protected void complete() {
		super.complete();
		System.out.println("In TagAutoCompleteBox's complete()");
		
		if(listener != null){
			listener.completed(this.getText());
		}
		
		this.setText("");
	}
	
	public void addListener(CompleteListener listener) {
		this.listener = listener;
	}
}
