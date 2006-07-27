package com.aavu.server.dao.db4o;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class Db4oDAO  {
	
	private static final String FILENAME = "db4.yac";
		
	private static ObjectContainer db;

	
	public ObjectContainer getDb() {
		if(db == null){
			ObjectContainer db=null;				
			
			db=Db4o.openFile(FILENAME);
			return db;
		}else{
			return db;
		}
		
	}

	public void closeDB(){
		if(db != null){
			db.close();
		}
		db = null;
	}
}
