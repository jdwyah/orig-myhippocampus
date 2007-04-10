package com.aavu.client.util;

public class LorumIpsum {

	public static String getLorum(int maxL){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<maxL;i++){			
			double val = Math.random() * 26;			
			Character c = new Character((char) (((char) val) + 65));
			
			if(i == 0){
				sb.append(c);
			}else{
				sb.append(Character.toLowerCase(c.charValue()));				
			}
			if(Math.random() > .9){
				break;
			}
			if(Math.random() > .7){
				sb.append(" ");
			}
		}
		return sb.toString();
	}
}
