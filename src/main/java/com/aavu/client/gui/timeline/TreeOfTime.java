package com.aavu.client.gui.timeline;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.widget.datepicker.DateUtil;

public class TreeOfTime {

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
	
	public TreeOfTime(int depth, int minDepth, int maxMembers){
		this.depth = depth;
		this.minDepth = minDepth;
		this.maxMembers = maxMembers;
		members = new HashSet();
		leafs = new GWTSortedMap();
	}
	
	public void add(HasDate d){
		
		//we'd previously hit the limit
		if(!leafs.isEmpty()){
			System.out.println(depth+" Add POST limit "+d);
			distributeElementToLeafs(d);

		}else{

			//hit the limit, break into subsections
			//
			if(members.size() >= maxMembers || depth <= minDepth){
				System.out.println(depth+" Add ON THE limit "+d);
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
				System.out.println(depth+" Add PRE limit "+d);
				members.add(d);
			}
		}		
	}
	
	
	private void distributeElementToLeafs(HasDate member) {
		
		int val = getSortValue(depth, member.getDate());
		
		TreeOfTime leaf = (TreeOfTime) leafs.get(new Integer(val));
		if(leaf == null){
			leaf = new TreeOfTime(depth + 1,minDepth,maxMembers);
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
			return (int)Math.floor(d.getDate() / 7.0);//week
		case 5:
			return d.getDay();
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
		
		System.out.println("DEPTH "+depth + " num "+num +" denom "+denom+" quot "+num/denom);
		
		return num / denom;
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
			return DateUtil.getMonth(key)+" "+(date.getYear()+1900)+" Week "+(key+1);
		case 5:
			String s = DateUtil.getMonth(key)+" ";
			if(key == 1)
				return "1st "+(date.getYear()+1900);
			if(key == 2)
				return "2nd "+(date.getYear()+1900);
			return key+"th "+(date.getYear()+1900);
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
	
//	private static int getSortBucketNum(int depth){
//		switch (depth) {
//		case 1:
//			return 10;//decade
//		case 2:
//			return 10;//year
//		case 3:
//			return 12;//month
//		case 4:
//			return 4;//week
//		case 5:
//			return 7;//day of week
//		case 6:
//			return 24;//hour
//		default:
//			throw new RuntimeException();
//		}
//	}

	public String toPrettyString() {
		StringBuffer sb = new StringBuffer();
		toPrettyString(sb,"");
		return sb.toString();	
	}
	private void toPrettyString(StringBuffer sb,String spacer) {		
		for (Iterator iterator = leafs.keySet().iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();
			sb.append(spacer);
			sb.append(key + "->\n");
			TreeOfTime subleaf = (TreeOfTime) leafs.get(key);
			subleaf.toPrettyString(sb,spacer+"---");
			sb.append("\n");
		}
		for (Iterator iterator = members.iterator(); iterator.hasNext();) {
			HasDate date = (HasDate) iterator.next();
			sb.append(spacer);
			sb.append("= "+date.toString());
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
