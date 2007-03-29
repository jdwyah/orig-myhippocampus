package com.aavu.client.help;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HelpPicker extends Composite {

	public VerticalPanel fiveP = new VerticalPanel();
	public VerticalPanel questionP = new VerticalPanel();
	public SimplePanel answerP = new SimplePanel();
	
	public HelpPicker(){
		
		HorizontalPanel topP = new HorizontalPanel();
		topP.add(fiveP);
		topP.add(questionP);
		
				
		HelpLabel who1 = new HelpLabel(ConstHolder.myConstants.help_who_1(),answerP,new Label(ConstHolder.myConstants.help_who_1_A()));
		HelpLabel who2 = new HelpLabel(ConstHolder.myConstants.help_who_2(),answerP,new Label(ConstHolder.myConstants.help_who_2_A()));		
		HelpLabel who3 = new HelpLabel(ConstHolder.myConstants.help_who_3(),answerP,new Label(ConstHolder.myConstants.help_who_3_A()));		
		HelpLabel who = new HelpLabel(ConstHolder.myConstants.help_who(),questionP,answerP);
		who.add(who1);
		who.add(who2);
		who.add(who3);
		
		
		HelpLabel what1 = new HelpLabel(ConstHolder.myConstants.help_what_1(),answerP,new Label(ConstHolder.myConstants.help_what_1_A()));
		HelpLabel what2 = new HelpLabel(ConstHolder.myConstants.help_what_2(),answerP,new Label(ConstHolder.myConstants.help_what_2_A()));		
		HelpLabel what3 = new HelpLabel(ConstHolder.myConstants.help_what_3(),answerP,new Label(ConstHolder.myConstants.help_what_3_A()));
		HelpLabel what = new HelpLabel(ConstHolder.myConstants.help_what(),questionP,answerP);
		what.add(what1);
		what.add(what2);
		what.add(what3);
		
		
		HelpLabel how1 = new HelpLabel(ConstHolder.myConstants.help_how_1(),answerP,new Label(ConstHolder.myConstants.help_how_1_A()));
		HelpLabel how2 = new HelpLabel(ConstHolder.myConstants.help_how_2(),answerP,new Label(ConstHolder.myConstants.help_how_2_A()));		
		HelpLabel how3 = new HelpLabel(ConstHolder.myConstants.help_how_3(),answerP,new Label(ConstHolder.myConstants.help_how_3_A()));
		HelpLabel how = new HelpLabel(ConstHolder.myConstants.help_how(),questionP,answerP);
		how.add(how1);
		how.add(how2);
		how.add(how3);
		
		
		HelpLabel why1 = new HelpLabel(ConstHolder.myConstants.help_why_1(),answerP,new Label(ConstHolder.myConstants.help_why_1_A()));
		HelpLabel why2 = new HelpLabel(ConstHolder.myConstants.help_why_2(),answerP,new Label(ConstHolder.myConstants.help_why_2_A()));		
		HelpLabel why3 = new HelpLabel(ConstHolder.myConstants.help_why_3(),answerP,new Label(ConstHolder.myConstants.help_why_3_A()));
		HelpLabel why = new HelpLabel(ConstHolder.myConstants.help_why(),questionP,answerP);
		why.add(why1);
		why.add(why2);
		why.add(why3);
		
		HelpLabel where1 = new HelpLabel(ConstHolder.myConstants.help_where_1(),answerP,new Label(ConstHolder.myConstants.help_where_1_A()));
		HelpLabel where2 = new HelpLabel(ConstHolder.myConstants.help_where_2(),answerP,new Label(ConstHolder.myConstants.help_where_2_A()));		
		HelpLabel where3 = new HelpLabel(ConstHolder.myConstants.help_where_3(),answerP,new Label(ConstHolder.myConstants.help_where_3_A()));
		HelpLabel where = new HelpLabel(ConstHolder.myConstants.help_where(),questionP,answerP);
		where.add(where1);
		where.add(where2);
		where.add(where3);
		
		
		fiveP.add(what);		
		fiveP.add(how);		
		fiveP.add(where);
		fiveP.add(who);
		fiveP.add(why);
		
		VerticalPanel mainP = new VerticalPanel();
		mainP.add(topP);
		mainP.add(answerP);
		
		fiveP.addStyleName("H-Help5Qs");		
		mainP.addStyleName("H-HelpPicker");
		
		initWidget(mainP);
	}

	private class HelpLabel extends Label implements ClickListener, MouseListener {
		
		private Panel target;
		private Panel clearTarget;
		private List widgets;

		public HelpLabel(String label,Panel target,Panel clearTarget){
			super(label);
			this.target = target;						
			this.clearTarget = clearTarget;
			this.widgets = new ArrayList();			
			addClickListener(this);
			addMouseListener(this);
		}
		public HelpLabel(String label, Panel target, Widget w) {
			this(label, target,null);
			add(w);
		}
		public void add(Widget w){
			widgets.add(w);
		}
		public void onClick(Widget sender) {
			target.clear();
			if(clearTarget != null){
				clearTarget.clear();
			}
			for (Iterator iter = widgets.iterator(); iter.hasNext();) {
				Widget element = (Widget) iter.next();
				target.add(element);
			}
		}
		
		public void onMouseEnter(Widget sender) {
			addStyleName("H-Help-Hover");
		}
		public void onMouseLeave(Widget sender) {
			removeStyleName("H-Help-Hover");			
		}
		public void onMouseMove(Widget sender, int x, int y) {}
		public void onMouseUp(Widget sender, int x, int y) {}
		public void onMouseDown(Widget sender, int x, int y) {}
	}
	
}
