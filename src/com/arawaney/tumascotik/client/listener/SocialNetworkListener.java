package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.SocialNetwork;
import com.arawaney.tumascotik.client.model.User;
import com.parse.ParseObject;



public interface SocialNetworkListener {


	public void onAllSocialNetworksQueryFinished(boolean b, ArrayList<SocialNetwork> socialNetwork);
	
	public void onUpdateSocialNetworksFinished(boolean b, ArrayList<SocialNetwork> socialNetwork);


}



