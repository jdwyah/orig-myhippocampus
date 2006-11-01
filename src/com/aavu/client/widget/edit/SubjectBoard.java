package com.aavu.client.widget.edit;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.subjects.SubjectInfo;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class SubjectBoard extends Composite{

	private Manager manager;

	private ChooserPanel chooserPanel;
	private InfoPanel infoPanel;	
	private SelectedPanel selectedPanel;
	private Label editMe;
	private SubjectServicePicker subjectTypeList;

	private TextBox titleBox; 

	public SubjectBoard(final Manager manager, TextBox titleBox) {	
		this.manager = manager;
		this.titleBox = titleBox;

		HorizontalPanel mainPanel = new HorizontalPanel();
		
		chooserPanel  = new ChooserPanel();
		infoPanel = new InfoPanel();
		
		
		selectedPanel = new SelectedPanel();

		editMe = new Label(Manager.myConstants.subject_edit());
		editMe.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				chooseSubject();
			}});
		editMe.setVisible(false);
		
		subjectTypeList = new SubjectServicePicker();
		subjectTypeList.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				chooseSubject();
				
			}});

		mainPanel.add(new HeaderLabel(Manager.myConstants.subject()));
		mainPanel.add(subjectTypeList);
		mainPanel.add(selectedPanel);
		mainPanel.add(editMe);
		mainPanel.add(chooserPanel);
		mainPanel.add(infoPanel);


		initWidget(mainPanel);
	}
	
	public void setSubject(Subject subject){		
		selectedPanel.setSubject(subject);
		editMe.setVisible(true);
		chooserPanel.setVisible(false);
		infoPanel.setVisible(false);
	}
	public void chooseSubject(){		
		editMe.setVisible(false);
	
		Subject s = subjectTypeList.getSelectedService();
		manager.getSubjectService().lookup(s,titleBox.getText(),new StdAsyncCallback("SubjectService"){
			public void onSuccess(Object result) {
				super.onSuccess(result);

				System.out.println("result: "+result);

				List subjectList = (List) result;
				chooserPanel.setList(subjectList);
				
				chooserPanel.setVisible(true);
				infoPanel.setVisible(true);
			}
		});

	}
	

	/**
	 * Panel responsible for giving the user the options in the subject list
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class ChooserPanel extends VerticalPanel{
		public ChooserPanel(){
			setStyleName("H-SubjectChooser");
		}

		public void setList(List subjectList) {
			clear();
			infoPanel.clear();
			
			add(new Label(Manager.myConstants.subject_choose()));
			
			int i = 0;
			for (Iterator iter = subjectList.iterator(); iter.hasNext();) {
				if (i > 2)
					break;
				Subject subj = (Subject) iter.next();
				add(new ChooserLabel(subj));
				i++;
			}
		}



	}
	/**
	 * Panel to show additional information about the Subject
	 * 
	 * @author Jeff Dwyer
	 */
	private class InfoPanel extends VerticalPanel{
		public InfoPanel(){
			setStyleName("H-SubjectInfoPanel");
		}

		public void setSubject(Subject subject) {
			clear();			
			for (Iterator iter = subject.getInfos().iterator(); iter.hasNext();) {
				SubjectInfo info = (SubjectInfo) iter.next();
				add(new Label(info.getType()+" "+info.getValue()));				
			}			
		}
	}
	
	private class SelectedPanel extends VerticalPanel{
		
		private Label lab = new Label(Manager.myConstants.subject_none());
		
		public SelectedPanel(){
			setStyleName("H-SubjectSelectedPanel");
			add(lab);			
		}
		

		public void setSubject(Subject subject) {
			lab.setText(subject.getName());
		}
		
	}
	
	/**
	 * Label for "Did you mean:" 
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class ChooserLabel extends Label implements MouseListener {
		private Subject subject;

		public ChooserLabel(Subject subj){
			super(subj.getName());
			this.subject = subj;	

			addMouseListener(this);
		}

		public void onMouseEnter(Widget sender) {
			infoPanel.setSubject(subject);
			addStyleName("H-Selected");
		}
		public void onMouseLeave(Widget sender) {
			removeStyleName("H-Selected");
		}
		
		public void onMouseDown(Widget sender, int x, int y) {}
		
		public void onMouseMove(Widget sender, int x, int y) {}
		public void onMouseUp(Widget sender, int x, int y) {
			setSubject(subject);
		}		
	}

	private class SubjectServicePicker extends ListBox {

		String none = "(None)";
		String book = "Book";
		String country = "Country";

		public SubjectServicePicker(){
			super();		
			addItem(none);
			addItem(book);
			addItem(country);					
		}

		public Subject getSelectedService() {

			String sel = getItemText(getSelectedIndex());

			if(sel.equals(none)){
				return null;		
			}else if (sel.equals(book)){
				return new AmazonBook();
			}else if (sel.equals(country)){
				return new HippoCountry();
			}

			return null;
		}


	}

}
