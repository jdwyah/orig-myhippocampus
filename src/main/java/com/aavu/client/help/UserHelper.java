package com.aavu.client.help;

import com.aavu.client.domain.User;
import com.aavu.client.service.Manager;
import com.aavu.client.service.UserActionListener;

public class UserHelper implements UserActionListener {

	private Manager manager;
	private User user;

	public UserHelper(Manager manager, User user) {
		this.manager = manager;
		this.user = user;
	}


	public void oceanLoaded(int num_islands) {
//		if(num_islands < 1){
//			manager.newFrame();
//		}
	}

	public void islandCreated() {
		// TODO Auto-generated method stub

	}

	public void oceanLoaded() {
		// TODO Auto-generated method stub

	}

	public void topicCreated() {
		// TODO Auto-generated method stub

	}


	

	


}
