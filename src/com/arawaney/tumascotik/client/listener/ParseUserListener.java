package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.parse.ParseObject;



public interface ParseUserListener {
	
	public void OnLoginResponse();
	
	public void onUserQueryFinish(User updatedUSer,boolean updated);
	
	public void onUserInsertFinish(User updatedUSer,boolean updated, String systemId);
	
	public void onUserUpdateFinish(User updatedUSer,boolean updated);

	public void onClientsQueryFinish(ArrayList<User> users, boolean b);

	public void onCLientUpdateFinish(User user, boolean b);
	

}



