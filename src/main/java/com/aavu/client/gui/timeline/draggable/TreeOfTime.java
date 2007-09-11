package com.aavu.client.gui.timeline.draggable;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.gui.timeline.HasDate;
import com.aavu.client.widget.datepicker.DateConstants;
import com.aavu.client.widget.datepicker.DateUtil;
import com.google.gwt.i18n.client.DateTimeFormat;

public class TreeOfTime {

	//private static final DateTimeFormat df = DateTimeFormat.getFormat("EE, MMM dd");
	private static final SimpleDateFormat df = new SimpleDateFormat("EE, MMM dd");


	public static final double HOURS_IN_MONTH = 24.0*31.0;
	public static final double HOURS_IN_WEEK = 24.0*7.0;
	public static final double MIN_PER_DAY = 24.0*60.0;
	public static final double SEC_PER_HOUR = 60.0*60.0;

	private Set members;
	private Map leafs;

	private int depth;

	private int maxMembers;
	private int minDepth;
	private int maxDepth;

	public TreeOfTime(int depth, int minDepth, int maxDepth,int maxMembers){
		this.depth = depth;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.maxMembers = maxMembers;
		members = new HashSet();
		leafs = new GWTSortedMap();
	}

	public void add(HasDate d){

		//we'd previously hit the limit
		if(!leafs.isEmpty()){
			//System.out.println("TreeOfTime."+depth+" Add POST limit "+d);
			distributeElementToLeafs(d);

		}else{

			//hit the limit, break into subsections
			//
			if(depth <= maxDepth
					&&
					(members.size() >= maxMembers 
							|| 
							depth <= minDepth)){
				//System.out.println("TreeOfTime."+depth+" Add ON THE limit "+d);
				for (Iterator iterator = members.iterator(); iterator.hasNext();) {
					HasDate member = (HasDate) iterator.next();
					distributeElementToLeafs(member);
				}
				members.clear();

				distributeElementToLeafs(d);
			}
			//no limit yet
			//
			else{
				//System.out.println("TreeOfTime."+depth+" Add PRE limit "+d);
				members.add(d);
			}
		}		
	}


	private void distributeElementToLeafs(HasDate member) {

		int val = getSortValue(depth, member.getStartDate());

		TreeOfTime leaf = (TreeOfTime) leafs.get(new Integer(val));
		if(leaf == null){
			leaf = new TreeOfTime(depth + 1,minDepth,maxDepth,maxMembers);
			leafs.put(new Integer(val), leaf);
		}

		leaf.add(member);
	}

	private static int getSortValue(int depth,Date d){
		switch (depth) {
		case 1:
			return d.getYear()  - d.getYear() % 10;//decade
		case 2:
			return d.getYear();//year
		case 3:
			return d.getMonth();//month
		case 4:
			return (int)Math.floor((d.getDate()-1) / 7.0);//week
		case 5:			
			//return 1-7 of this week
			int rtn = d.getDate() % 7;
			rtn = 0 == rtn ? 7 : rtn;
			//System.out.println("get sort d5 "+d.getDate()+" "+(d.getDate() % 7)+" "+rtn);
			return rtn;
		case 6:
			return d.getHours();
		case 7:
			return 15 * (d.getMinutes() / 15);
		case 8:
			return d.getMinutes();		
		default:
			System.out.println("ACK depth "+depth);
		return d.getSeconds();
		}
	}

	/**
	 * getTime() % interval / interval approach held many thorns, I believe because sorting out the interval 
	 * was actually much more difficult than it appears & leap years etc rose their heads. 
	 * 
	 * This approach basically just approximate a percent for an approrpiate loevel of detail.
	 * 
	 * @param depth
	 * @param d
	 * @return
	 */
	public static double getPctAtDepth(int depth,Date d){


		double num = d.getSeconds();
		double denom = 60;

		switch (depth) {
		case 1:				
			System.out.println("YEAR Mod "+d.getYear() % 10);
			num = (12*(d.getYear() % 10) + d.getMonth());
			denom =  (10.0 * 12.0);//decade
			break;
		case 2:			
			num = 30*d.getMonth() + d.getDate(); 
			denom = 365.0;//year
			break;
		case 3:			
			num = (d.getDate()-1)*24 + d.getHours();
			denom = HOURS_IN_MONTH;//month
			break;
		case 4:						 
			num = ((d.getDate() % 7.0)*24) + d.getHours();
			//num = (getSortValue(depth, d) * 24) + d.getHours();
			//num = (d.getDay()-1)*24 + d.getHours();
			denom = HOURS_IN_WEEK;//week
			break;
		case 5:
			num = (d.getHours()-1)*60 + d.getMinutes();
			denom = MIN_PER_DAY;//day
			break;
		case 6:			
			num = (d.getMinutes()-1)*60 +d.getSeconds();
			denom = SEC_PER_HOUR;//hour
			break;
		case 7:
			num = (((d.getMinutes()%15)))*60 +d.getSeconds();
			denom = SEC_PER_HOUR;//hour
			break;
		case 8:
			num = d.getSeconds();
			denom = 60.0;
			break;
		default:			
			return (d.getSeconds() / 60.0);
		}

		//System.out.println("TreeOfTime. DEPTH "+depth + " num "+num +" denom "+denom+" quot "+num/denom);

		return num / denom;
	}
	private static String getDayStr(int key){
		int mod = key % 10;
		if(mod == 1)
			return key+"st";
		if(mod == 2)
			return key+"nd";
		if(mod == 3)
			return key+"nd";
		return key+"th";
	}

	public static String getLabelForDepth(int depth,int key, Date date){


		switch (depth) {
		case 1:
			return "The "+key+"0's";
		case 2:
			return "Year "+(key+1900);
		case 3:
			return DateUtil.getMonth(key)+" "+(date.getYear()+1900);
		case 4:
			if(4 == key){
				return DateUtil.getMonth(date.getMonth())+" "+(date.getYear()+1900)+" 28th-31st";
			}else{
				return DateUtil.getMonth(date.getMonth())+" "+(date.getYear()+1900)+" "+getDayStr((7*key+1))+"-"+getDayStr((7*(key+1)));
			}
		case 5:
			String s = DateUtil.getMonth(date.getMonth())+" ";			
			return s+getDayStr(date.getDate())+" "+(date.getYear()+1900);
		case 6:
			return df.format(date)+" "+key+" O'Clock";			
		case 7:
			return df.format(date)+" "+date.getHours()+":"+key;
		case 8:
			return df.format(date)+" "+":"+key;	
		default:			
			return "PicoSeconds";
		}
	}
	public static String getShortLabelForDepth(int depth,int key, Date date){


		switch (depth) {
		case 1:
			return "The "+key+"0's";
		case 2:
			return ""+(key+1900);
		case 3:
			return DateUtil.getMonth(key);
		case 4:
			if(4 == key){
				return "28th-31st";
			}else{
				return getDayStr((7*key+1))+"-"+getDayStr((7*(key+1)));
			}
		case 5:
						
			return getDayStr(date.getDate());
		case 6:
			return ""+key;			
		case 7:
			return date.getHours()+":"+key;
		case 8:
			return ":"+key;	
		default:			
			return "PicoSeconds";
		}
	}

//	private static int getSortBucketNum(int depth){
//	switch (depth) {
//	case 1:
//	return 10;//decade
//	case 2:
//	return 10;//year
//	case 3:
//	return 12;//month
//	case 4:
//	return 4;//week
//	case 5:
//	return 7;//day of week
//	case 6:
//	return 24;//hour
//	default:
//	throw new RuntimeException();
//	}
//	}



	public String toPrettyString() {
		StringBuffer sb = new StringBuffer();
		toPrettyString(sb,"",new Integer(0));
		return sb.toString();	
	}
	private void toPrettyString(StringBuffer sb,String spacer,Integer yourkey) {		
		for (Iterator iterator = leafs.keySet().iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();		
			sb.append(spacer);
			sb.append(key + "->\n");
			TreeOfTime subleaf = (TreeOfTime) leafs.get(key);
			subleaf.toPrettyString(sb,spacer+"---",key);
			sb.append("\n");
		}
		for (Iterator iterator = members.iterator(); iterator.hasNext();) {
			HasDate date = (HasDate) iterator.next();			
			sb.append(spacer);
			sb.append("= "+date.toString()+" "+getLabelForDepth(depth - 1, yourkey.intValue(), date.getStartDate())+" depth "+depth+" key "+yourkey.intValue());
			sb.append("\n");
		}

	}

	public int getDepth() {
		return depth;
	}

	public void visit(Visitor visitor) {
		visit(visitor, new Integer(0));
	}
	private void visit(Visitor visitor, Integer key) {
		for (Iterator iterator = leafs.keySet().iterator(); iterator.hasNext();) {
			Integer nextKey = (Integer) iterator.next();

			TreeOfTime subleaf = (TreeOfTime) leafs.get(nextKey);
			subleaf.visit(visitor,nextKey);			
		}

		//members should be clear if we have leafs 
		for (Iterator iterator = members.iterator(); iterator.hasNext();) {
			HasDate date = (HasDate) iterator.next();

			//return our parents depth, since that's what the key 
			//refers to.
			visitor.found(date,depth - 1,key.intValue());
		}
	}

}
