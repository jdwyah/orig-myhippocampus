package com.aavu.server.dao.db4o;

import java.io.File;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.google.gwt.core.client.GWT;

public class Db4oDAO {
	
	private String FILENAME = "db4.yap";

	
	
	
	private ObjectContainer db;

	
	public ObjectContainer getDb() {
		
		//
		//NOTE TO SELF The reason this isn't working 
		//is that the close() code is closing the db even in spring mode
		//
		
		if(GWT.isScript()){
			
		}
		File f = new File(FILENAME);
		System.out.println("exist "+f.exists());
		System.out.println("path "+f.getAbsolutePath());
		
		System.out.println("db "+db+" ");
		//Db4o.o
		if(db == null){
			ObjectContainer db=null;				
			System.out.println("running db from filename");
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

	public void setDb(ObjectContainer db) {
		this.db = db;
		System.out.println("setting db "+db);
	}
}
