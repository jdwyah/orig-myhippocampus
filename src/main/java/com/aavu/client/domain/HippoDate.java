package com.aavu.client.domain;
// Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.io.Serializable;
import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.google.gwt.user.client.rpc.IsSerializable;




/**
 * NOTE: HippoDate stores startDate as Created & endDate as lastUpdated.
 */
public class HippoDate extends MetaValue implements IsSerializable, Serializable{
	
	private transient static SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");	
		
	
	public HippoDate(){
		setPublicVisible(false);
	}	

	public HippoDate(User u, String d) {		
		super(u,d);
		setPublicVisible(false);
	}

	/**
	 * bc DB has not-null constraints on lastUpdated, we'll say that... eq == null... 
	 * hmmm, need to make sure not to fake out Hibernate.
	 * 
	 * @return
	 */
	public Date getEndDate(){
//		if(getLastUpdated().equals(getCreated())){
//			return null;
//		}
		return getLastUpdated();
	}
	public Date getStartDate(){
		return getCreated();
	}
	public boolean mustHaveUniqueName() {
		return false;
	}
	public void setEndDate(Date date){
		setLastUpdated(date);
	}
	
	public void setStartDate(Date date){
		if(getTitle() == null || getTitle().equals("")){
			setTitle(df.format(date));			
		}
		setCreated(date);
	}

}
