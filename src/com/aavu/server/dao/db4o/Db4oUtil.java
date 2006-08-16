package com.aavu.server.dao.db4o;

import org.apache.log4j.Logger;

import com.db4o.ObjectSet;

public class Db4oUtil {
	private static final Logger log = Logger.getLogger(Db4oUtil.class);

	public static Object getUniqueRes(ObjectSet set) {
		if(set == null){
			log.warn("Null set return");
			throw new RuntimeException("Null");
		}
		if(set.size() != 1){
			log.warn("No unique result");
			throw new RuntimeException("Null");
		}
		return set.get(0);
	}

}
