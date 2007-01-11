/*
 * Copyright 2007 Jeff Dwyer <jdwyah AT gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aavu.client.util;

/**
 * Specify the number of decimals you'd like. No checking for infinite || NaN values.
 *  
 * @author Jeff Dwyer
 * @version $Revision: 0.0 $
 */
public class DecimalFormatSimple {

	private int decimals;

	public DecimalFormatSimple(int decimals){
		this.decimals = decimals;	
	}
	
	public String format(double d){
				
		if(Double.toString(d).indexOf('E') != -1){
			return Double.toString(d);
		}
		
		Double mantissa = new Double(d % 1);		
		
		if(mantissa.doubleValue() == 0){
			return ((int)d)+"";
		}
		
		if(d < 0){
			if((int)Math.ceil(d) == 0){				
				return "-."+mantissa.toString().substring(3,(3+decimals < mantissa.toString().length())? 3+decimals : mantissa.toString().length());
			}else{
				return (int)Math.ceil(d)+"."+mantissa.toString().substring(3,3+decimals);
			}
		}
		else{
			if((int)Math.floor(d) == 0){
				return "."+mantissa.toString().substring(2,2+decimals);
			}else{
				return (int)Math.floor(d)+"."+mantissa.toString().substring(2,2+decimals);
			}
		}
	}	
}
