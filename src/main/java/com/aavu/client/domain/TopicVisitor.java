package com.aavu.client.domain;


/**
 * Visitor pattern to avoid instanceof not working for CGLib enhanced objs.
 * Note, this problem only effect server side work, since our HibernateSerializer
 * cloning fixes this for the client. 
 * 
 * see http://www.hibernate.org/280.html .
 * 
 * MetaLocation, Text, etc
 * 
 * @author Jeff Dwyer
 *
 */
public interface TopicVisitor {

	void visit(MetaSeeAlso meta);
	void visit(Meta meta);
	void visit(Topic topic);	

}
