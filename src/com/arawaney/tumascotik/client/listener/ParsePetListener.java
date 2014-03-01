package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Specie;
import com.arawaney.tumascotik.client.model.User;
import com.parse.ParseObject;



public interface ParsePetListener {

	public void onAllSpeciesQueryFinished(boolean b, ArrayList<Specie> species);

	public void onBreedQueryFinished(ArrayList<String> breed);

	public void onPetQueryFinished(Pet pet);

	public void OnPetUpdateFinished(boolean b);

	public void onGetAllPets(ArrayList<Pet> pets);

	public void onPetInserted(String objectId, boolean b);

	public void onAllBreedsQueryFinished(boolean b, ArrayList<Breed> species);

	public void onAllPetPropertiesFinished(boolean b,
			ArrayList<PetPropertie> petProperties);
	
	public void onUpdateBreedsQueryFinished(boolean b, ArrayList<Breed> species);

	public void onUpdatePetPropertiesFinished(boolean b,
			ArrayList<PetPropertie> petProperties);
	
	public void onUpdateSpeciesQueryFinished(boolean b, ArrayList<Specie> species);

	public void onPetRemoveFinished(boolean b);
	

}



