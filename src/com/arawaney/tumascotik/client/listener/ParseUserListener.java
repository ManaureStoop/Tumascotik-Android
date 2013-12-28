package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.parse.ParseObject;



public interface ParseUserListener {
	
	public void OnLoginResponse();
	
	public void onUserQueryFinish(User updatedUSer,boolean updated);
	
	public void onUserUpdateFinish(User updatedUSer,boolean updated);
	

}



