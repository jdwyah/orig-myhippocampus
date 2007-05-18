package com.aavu.client.gui.timeline;

import java.util.Date;

import junit.framework.TestCase;

public class TreeOfTimeTest extends TestCase {
	public void testTree(){

		TreeOfTime tree = new TreeOfTime(1,0,10);

		tree.add(new DateW(1995,5,17));
		tree.add(new DateW(1995,5,16));
		tree.add(new DateW(1995,5,15));
		tree.add(new DateW(1995,5,13));
		tree.add(new DateW(1995,5,12));
		tree.add(new DateW(1995,5,11));
		tree.add(new DateW(1995,5,7));
		tree.add(new DateW(1995,5,6));
		tree.add(new DateW(1995,5,5));
		tree.add(new DateW(1995,5,4,10,5));
		tree.add(new DateW(1995,5,4,11,4));
		tree.add(new DateW(1995,5,4,12,6));//
		tree.add(new DateW(1995,5,4,13,7));
		tree.add(new DateW(1995,5,4,14,8));
		tree.add(new DateW(1995,5,4,15,9));
		tree.add(new DateW(1995,5,4,16,10));
		tree.add(new DateW(1995,5,4,17,11));
		tree.add(new DateW(1995,5,4,17,12));
		tree.add(new DateW(1995,5,4,17,13));
		tree.add(new DateW(1995,5,4,13,14));
		tree.add(new DateW(1995,5,4,12,15));		
		tree.add(new DateW(1995,5,4));
		tree.add(new DateW(1995,5,3));
		tree.add(new DateW(1995,5,2));
		tree.add(new DateW(1995,5,1));


//		tree.add(new DateW(1990,5,4));
//		tree.add(new DateW(1990,5,3));
//		tree.add(new DateW(1990,5,2));
//		tree.add(new DateW(1990,5,1));
//		tree.add(new DateW(1990,5,4));
//		tree.add(new DateW(1990,5,3));
//		tree.add(new DateW(1990,5,2));
//		tree.add(new DateW(1990,5,1));


//		tree.add(new DateW(1989,5,4));
//		tree.add(new DateW(1989,5,3));
//		tree.add(new DateW(1990,5,2));
//		tree.add(new DateW(1990,5,1));

//		tree.add(new DateW(1989,5,4,13,10,23));
//		tree.add(new DateW(1989,5,4,13,10,22));
//		tree.add(new DateW(1989,5,4,13,12,21));
//		tree.add(new DateW(1989,5,4,13,13,13));
//		tree.add(new DateW(1989,5,4,13,14,27));
//		tree.add(new DateW(1989,5,4,13,15,22));
//		tree.add(new DateW(1989,5,4,13,16,43));
//		tree.add(new DateW(1989,5,4,13,17,25));
//		tree.add(new DateW(1989,5,4,13,12,55));
//		tree.add(new DateW(1989,5,4,13,13,33));
//		tree.add(new DateW(1989,5,4,13,14,38));
//		tree.add(new DateW(1989,5,4,13,15,13));
//		tree.add(new DateW(1989,5,4,13,16,05));
//		tree.add(new DateW(1989,5,4,13,17,53));
//		tree.add(new DateW(1989,5,3));
//		tree.add(new DateW(1990,5,2));
//		tree.add(new DateW(1990,5,1));

		System.out.println(tree.toPrettyString());

	}
	
	public void testMinDepthTree(){

		TreeOfTime tree = new TreeOfTime(1,2,10);

		tree.add(new DateW(1995,5,17));
		tree.add(new DateW(1995,5,16));
		tree.add(new DateW(1995,5,15));
		tree.add(new DateW(1995,5,1));
		
		System.out.println(tree.toPrettyString());
		
		tree.visit(new Visitor(){
			public void found(Object object, int depth, int key) {
				System.out.println("yooooooooooooooo depth "+depth);
				assertEquals("Depth Should be 2",depth, 2);
			}});
		
		tree = new TreeOfTime(1,4,10);

		tree.add(new DateW(1995,5,17));
		tree.add(new DateW(1995,5,16));
		tree.add(new DateW(1995,5,15));
		tree.add(new DateW(1995,5,1));
		
		
		tree.visit(new Visitor(){
			public void found(Object object, int depth, int key) {
				assertEquals("Depth Should be 4",depth, 4);
			}});
		
		
	}
	

	private class DateW implements HasDate {

		private Date date;

		public DateW(int i, int j, int k) {
			this.date = new Date(i,j,k);
		}

		public DateW(int i, int j, int k, int l, int m) {
			this.date = new Date(i,j,k,l,m);
		}

		public Date getDate() {
			return date;
		}

		@Override
		public String toString() {
			return date.toString();
		}

	}
	public void testPct(){

		Date d = new Date(1970,0,1,1,1,1);			

		System.out.println(d.getYear());
		System.out.println(d.getMonth());
		System.out.println(d.getDate());

		assertEquals(.0,TreeOfTime.getPctAtDepth(1, d));			
		assertEquals(1.0/365,TreeOfTime.getPctAtDepth(2, d));
		assertEquals(1.0/TreeOfTime.HOURS_IN_MONTH,TreeOfTime.getPctAtDepth(3, d));
		assertEquals(25.0/TreeOfTime.HOURS_IN_WEEK,TreeOfTime.getPctAtDepth(4, d));			
		assertEquals(1.0/TreeOfTime.MIN_PER_DAY,TreeOfTime.getPctAtDepth(5, d));
		assertEquals(1.0/TreeOfTime.SEC_PER_HOUR,TreeOfTime.getPctAtDepth(6, d));
		assertEquals(61.0/TreeOfTime.SEC_PER_HOUR,TreeOfTime.getPctAtDepth(7, d));
		assertEquals(1.0/60,TreeOfTime.getPctAtDepth(8, d));

		d = new Date(1981,2,8,4,20,1);			

		System.out.println(d.getYear());
		System.out.println(d.getMonth());
		System.out.println(d.getDate());

		//1981 + 2 months / 120 months / decade
		assertEquals(14.0/120,TreeOfTime.getPctAtDepth(1, d));		
		//30*2 + 8
		assertEquals(68.0/365,TreeOfTime.getPctAtDepth(2, d));

		//7days * 24 + 4
		assertEquals(172.0/TreeOfTime.HOURS_IN_MONTH,TreeOfTime.getPctAtDepth(3, d));

		//1days(of week) * 24 + 4		
		assertEquals(28.0/TreeOfTime.HOURS_IN_WEEK,TreeOfTime.getPctAtDepth(4, d));
		//3*60+20
		assertEquals(200.0/TreeOfTime.MIN_PER_DAY,TreeOfTime.getPctAtDepth(5, d));

		//19*60 + 1
		assertEquals(1141.0/TreeOfTime.SEC_PER_HOUR,TreeOfTime.getPctAtDepth(6, d));
		
		assertEquals(301.0/TreeOfTime.SEC_PER_HOUR,TreeOfTime.getPctAtDepth(7, d));
		
		assertEquals(1.0/60,TreeOfTime.getPctAtDepth(8, d));

	}
}
