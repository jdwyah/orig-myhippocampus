package com.aavu.client.domain.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Ok, here's how this works. We don't want to serialize the whole topic, send it to the server and
 * then hope/pray/hack that things get saved right with respect to all of the lazy loading /
 * persistent set / CGLIB etc munging that we did on the way to the client.
 * 
 * Instead we implement our logic in commands.
 * 
 * These nuggets have everything they need to affect the changes. We'll run them here on the client,
 * to update our local state, but then we'll serialize them and send them over to the server where
 * they will be hydrated, run and saved.
 * 
 * Contract for the server is that it will hypdrate the 3 transient topics (if necessary) with the
 * id's in the three variables.
 * 
 * Again, this seems like a bunch of rigamorole, but it makes the object graph
 * connecting/unconnecting a non-issue, which is really nice. It's also very easy on the network
 * connection.
 * 
 * @param testTopic
 * @param command
 * @param callback
 */
public abstract class AbstractCommand implements IsSerializable {

	/**
	 * @gwt.typeArgs <java.lang.Long>
	 */
	private List topicIDs = new ArrayList();
	private transient List topics = new ArrayList();

	private String data;

	public AbstractCommand() {
	}

	public AbstractCommand(Topic topic) {
		this(topic, null, null);
	}

	public AbstractCommand(Topic topic, Topic topic1) {
		this(topic, topic1, null);
	}


	public AbstractCommand(Topic topic, Topic topic1, Topic topic2) {
		Integer i = new Integer(3);
		if (topic != null) {
			topicIDs.add(new Long(topic.getId()));
			topics.add(topic);
		}
		if (topic1 != null) {
			topicIDs.add(new Long(topic1.getId()));
			topics.add(topic1);
		}
		if (topic2 != null) {
			topicIDs.add(new Long(topic2.getId()));
			topics.add(topic2);
		}
	}

	public AbstractCommand(List _topics) {
		init(_topics);
	}

	protected void init(List _topics) {
		this.topics = _topics;
		setTopicIDsFromTopics(_topics);
	}

	protected void setTopicIDsFromTopics(List _topics) {
		for (Iterator iter = _topics.iterator(); iter.hasNext();) {
			Topic topic = (Topic) iter.next();
			topicIDs.add(new Long(topic.getId()));
			topics = _topics;
		}
	}

	public abstract void executeCommand() throws HippoException;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


	public Topic getTopic(int i) {
		if (topics.size() > i) {
			return (Topic) topics.get(i);
		} else {
			return null;
		}
	}

	public void setTopic(int i, Topic t) {
		// System.out.println("AbstractCommand topics.size "+topics.size()+" setting "+i);
		if (topics.size() > i) {
			topics.set(i, t);
		} else if (i == topics.size()) {
			topics.add(t);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	protected long getTopicID(int i) {
		if (topicIDs.size() > i) {
			return ((Long) topicIDs.get(i)).longValue();
		} else {
			return -1;
		}
	}



	public List getTopicIDs() {
		return topicIDs;
	}

	public List getTopics() {
		return topics;
	}

	public Set getDeleteSet() {
		return new HashSet();
	}

	public void setTopics(List topics) {
		this.topics = topics;
	}

	public boolean updatesTitle() {
		return false;
	}

	public Set getAffectedTopics() {
		return new HashSet();
	}


	/**
	 * s -> e inclusive s, exclusive e TODO java 1.5 makes this unec
	 */
	protected List subList(List l, int s, int e) {
		List rtn = new ArrayList();
		for (int i = s; i < e; i++) {
			rtn.add(l.get(i));
		}
		return rtn;
	}


}
