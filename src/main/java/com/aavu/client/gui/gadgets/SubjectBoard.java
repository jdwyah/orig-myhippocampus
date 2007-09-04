package com.aavu.client.gui.gadgets;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.subjects.SubjectInfo;
import com.aavu.client.domain.subjects.WikiSubject;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ext.ObjectListBox;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class SubjectBoard extends Composite {

	private Manager manager;

	private ChooserPanel chooserPanel;
	private InfoPanel infoPanel;
	private SelectedPanel selectedPanel;
	private Label editMe;
	private SubjectServicePicker subjectTypeList;

	private EditableLabelExtension titleBox;

	private Subject selectedSubject;

	private Topic topic;

	private TagBoard tagBoard;

	private SaveNeededListener saveNeeded;

	public SubjectBoard(final Manager manager, EditableLabelExtension titleBox2, TagBoard tagBoard,
			SaveNeededListener saveNeeded) {
		this.manager = manager;
		this.titleBox = titleBox2;
		this.tagBoard = tagBoard;
		this.saveNeeded = saveNeeded;

		HorizontalPanel mainPanel = new HorizontalPanel();

		chooserPanel = new ChooserPanel();
		infoPanel = new InfoPanel();


		selectedPanel = new SelectedPanel();

		editMe = new Label(ConstHolder.myConstants.subject_edit());
		editMe.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				chooseSubject();
			}
		});
		editMe.setVisible(false);

		subjectTypeList = new SubjectServicePicker();
		subjectTypeList.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				chooseSubject();

			}
		});

		mainPanel.add(new HeaderLabel(ConstHolder.myConstants.subject()));
		mainPanel.add(subjectTypeList);
		mainPanel.add(selectedPanel);
		mainPanel.add(editMe);
		mainPanel.add(chooserPanel);
		mainPanel.add(infoPanel);


		initWidget(mainPanel);
	}

	public void load(Topic topic) {

		this.topic = topic;
		setSubject(topic.getSubject());
		subjectTypeList.setSubject(topic.getSubject());
	}

	private void setSubject(Subject subject) {

		// TODO remove old TAG
		// tagBoard.removeTag(selectedSubject.getTagName())

		selectedSubject = subject;
		selectedPanel.setSubject(subject);

		editMe.setVisible(true);
		chooserPanel.setVisible(false);
		infoPanel.setVisible(false);

	}

	private void setNewSubject(Subject subject) {
		setSubject(subject);
		if (subject != null) {
			// TODO
			// tagBoard.tagTopic(subject.getTagName());
		}
		saveNeeded.onChange(this);
	}

	public Subject getSelectedSubject() {
		return selectedSubject;
	}

	private void chooseSubject() {
		editMe.setVisible(false);

		Subject s = subjectTypeList.getSelectedService();
		if (s != null) {
			manager.getSubjectService().lookup(s, titleBox.getText(),
					new StdAsyncCallback("SubjectService") {
						public void onSuccess(Object result) {
							super.onSuccess(result);

							System.out.println("result: " + result);

							List subjectList = (List) result;
							chooserPanel.setList(subjectList);

							chooserPanel.setVisible(true);
							infoPanel.setVisible(true);
						}
					});
		}

	}


	/**
	 * Panel responsible for giving the user the options in the subject list
	 * 
	 * @author Jeff Dwyer
	 * 
	 */
	private class ChooserPanel extends VerticalPanel {
		public ChooserPanel() {
			setStyleName("H-SubjectChooser");
		}

		// List <Subject>
		public void setList(List subjectList) {
			clear();
			infoPanel.clear();

			if (subjectList.size() > 0) {
				add(new Label(ConstHolder.myConstants.subject_choose()));
			} else {
				add(new Label(ConstHolder.myConstants.subject_no_matches()));
			}

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
	private class InfoPanel extends VerticalPanel {
		public InfoPanel() {
			setStyleName("H-SubjectInfoPanel");
		}

		public void setSubject(Subject subject) {
			clear();
			for (Iterator iter = subject.getInfos().iterator(); iter.hasNext();) {
				SubjectInfo info = (SubjectInfo) iter.next();
				add(new Label(info.getType() + " " + info.getValue()));
			}
		}
	}

	private class SelectedPanel extends VerticalPanel {

		private Label lab = new Label(ConstHolder.myConstants.subject_none());

		public SelectedPanel() {
			setStyleName("H-SubjectSelectedPanel");
			add(lab);
		}


		public void setSubject(Subject subject) {
			if (subject == null) {
				lab.setText(ConstHolder.myConstants.subject_none());
			} else {
				lab.setText(subject.getName());
			}
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

		public ChooserLabel(Subject subj) {
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

		public void onMouseDown(Widget sender, int x, int y) {
		}

		public void onMouseMove(Widget sender, int x, int y) {
		}

		public void onMouseUp(Widget sender, int x, int y) {
			setNewSubject(subject);
		}
	}

	private class SubjectServicePicker extends ObjectListBox {

		public SubjectServicePicker() {
			super();
			addItem(ConstHolder.myConstants.subject_none(), null);
			addItem(ConstHolder.myConstants.book(), new AmazonBook());
			addItem(ConstHolder.myConstants.country(), new HippoCountry());
			addItem(ConstHolder.myConstants.wiki(), new WikiSubject());
		}


		public void setSubject(Subject subject) {
			setSelectedObjectToType(subject);
		}

		public Subject getSelectedService() {
			return (Subject) getSelectedObject();
		}
	}



}
