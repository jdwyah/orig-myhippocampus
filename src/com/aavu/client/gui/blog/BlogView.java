package com.aavu.client.gui.blog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.explorer.FTICachingExplorerPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A way to see things that have been recently updated
 * 
 * @author Jeff Dwyer
 *
 */
public class BlogView extends FTICachingExplorerPanel {
	private static final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	private VerticalPanel mainPanel = new VerticalPanel();

	public BlogView(Manager manager, Map defaultMap) {
		super(manager,defaultMap);	
		
		initWidget(mainPanel);
		
	}

	public Widget getWidget() {
		return this;
	}

	public void draw(List ftis) {
		mainPanel.clear();

		for (Iterator iterator = ftis.iterator(); iterator.hasNext();) {
			DatedTopicIdentifier fti = (DatedTopicIdentifier) iterator.next();

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(new TopicLink(fti));
			hp.add(new Label(df.format(fti.getCreated())));
			mainPanel.add(hp);
		}

	}

	
	
}
