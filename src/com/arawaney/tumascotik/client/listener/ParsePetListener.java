package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.parse.ParseObject;



public interface ParsePetListener {

	public void onSpecieQueryFinished(ArrayList<String> species);

	public void onBreedQueryFinished(ArrayList<String> breed);

	public void onPetQueryFinished(Pet pet);

	public void OnPetUpdateFinished(boolean b);

	public void onGetAllPets(ArrayList<Pet> pets);

	public void onPetInserted(String objectId);
	

}



