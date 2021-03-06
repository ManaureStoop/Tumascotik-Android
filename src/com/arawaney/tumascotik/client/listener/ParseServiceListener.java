package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.parse.ParseObject;



public interface ParseServiceListener {

	public void onMotivesQueryFinished(ArrayList<String> motives);

	public void onAllMotivesQueryFinished(boolean b, ArrayList<Service> motives);
	
	public void onUpdateMotivesFinished(boolean b, ArrayList<Service> motives);


}



