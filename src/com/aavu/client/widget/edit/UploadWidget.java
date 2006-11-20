package com.aavu.client.widget.edit;

import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadWidget extends Composite {

	private ProgressBar progressBar;
	private Manager manager;
	private FileUpload upload; 

	public UploadWidget(final Manager manager,String path){
		this.manager = manager;
		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		form.setAction(path);

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		
		// Create a FileUpload widget.
		upload = new FileUpload();
		upload.setName("file");
		panel.add(upload);

		HorizontalPanel uploads = new HorizontalPanel();
		
		// Add a 'submit' button.
		uploads.add(new Button(manager.myConstants.upload_submit(), new ClickListener() {
			public void onClick(Widget sender) {
				form.submit();
			}
		}));
		
		progressBar = new ProgressBar(20
				,ProgressBar.SHOW_TEXT);
		initProgressBar();
		uploads.add(progressBar);
		
		panel.add(uploads);

		// Add an event handler to the form.
		form.addFormHandler(new FormHandler() {
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				// When the form submission is successfully completed, this event is
				// fired. Assuming the service returned a response of type text/plain,
				// we can get the result text here (see the FormPanel documentation for
				// further explanation).
				progressBar.setProgress(100);
				progressBar.setText(manager.myConstants.upload_complete());
				Window.alert(event.getResults());
				
				Timer t = new Timer() {
					public void run() {
						initProgressBar();					
					}
				};
				t.schedule(2000);
			}

			public void onSubmit(FormSubmitEvent event) {
				if (upload.getFilename().length() == 0) {
					Window.alert(manager.myConstants.upload_select_file());
					event.setCancelled(true);
					return;
				}
				progressBar.setVisible(true);
				Timer t = new Timer() {
					public void run() {
						int progress = progressBar.getProgress()+4;
						if (progress>100) cancel();
						progressBar.setProgress(progress);
					}
				};
				t.scheduleRepeating(1000);
			}
		});

		initWidget(form);
	}
	
	private void initProgressBar(){
		progressBar.setProgress(0);
		progressBar.setText(manager.myConstants.upload());
		progressBar.setVisible(false);
	}
}
