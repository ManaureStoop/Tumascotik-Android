package com.arawaney.tumascotik.client.control;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.arawaney.tumascotik.client.MainActivity;
import com.arawaney.tumascotik.client.backend.ParsePetProvider;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.db.provider.BreedProvider;
import com.arawaney.tumascotik.client.db.provider.PetPropertieProvider;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.db.provider.SpecieProvider;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseServiceListener;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Specie;

public class BackEndDataUpdater extends Service implements ParsePetListener,
		ParseServiceListener {
	Context context;

	private void updateAllData() {
		
		updateMotives();
		updateBreeds();
		updatePetProperties();
		updateSpecies();
		updatePets();
		

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		updateAllData();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		this.context = getApplicationContext();
		updateAllData();
		super.onCreate();
	}

	private void updateMotives() {
		ParseProvider.updateAllMotives(context, this);
	}

	private void updatePetProperties() {
		ParsePetProvider.updateAllPetProperties(context, this);

	}

	private void updateBreeds() {
		ParsePetProvider.updateAllBreeds(context, this);

	}

	private void updateSpecies() {
		ParsePetProvider.updateAllSpecies(context, this);

	}

	@Override
	public void onMotivesQueryFinished(ArrayList<String> motives) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllSpeciesQueryFinished(boolean b, ArrayList<Specie> species) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBreedQueryFinished(ArrayList<String> breed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPetQueryFinished(Pet pet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnPetUpdateFinished(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetAllPets(ArrayList<Pet> pets) {
		if (pets != null) {
			for (Pet pet : pets) {
				Pet savedPet = PetProvider.readPet(this, pet.getSystem_id());
				if (savedPet == null) {
					PetProvider.insertPet(this, pet);
				} else {
					pet.setId(savedPet.getId());
					PetProvider.updatePet(this, pet);
				}

			}
		}
	}

	@Override
	public void onPetInserted(String objectId, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllBreedsQueryFinished(boolean b, ArrayList<Breed> species) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllPetPropertiesFinished(boolean b,
			ArrayList<PetPropertie> petProperties) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateBreedsQueryFinished(boolean b, ArrayList<Breed> breeds) {
		if (b) {
			for (Breed breed : breeds) {
				Breed breedSaved = BreedProvider.readBreed(context,
						breed.getSystem_id());
				if (breedSaved == null) {
					BreedProvider.insertBreed(context, breed);
				} else {
					breed.setId(breedSaved.getId());
					BreedProvider.updateBreed(context, breed);
				}
			}

		}

	}

	@Override
	public void onUpdatePetPropertiesFinished(boolean b,
			ArrayList<PetPropertie> petProperties) {
		if (b) {
			for (PetPropertie petPropertie : petProperties) {
				PetPropertie petPropertieSaved = PetPropertieProvider
						.readPetPropertie(context, petPropertie.getSystem_id());
				if (petPropertieSaved == null) {
					PetPropertieProvider.insertPetPropertie(context,
							petPropertie);
				} else {
					petPropertie.setId(petPropertieSaved.getId());
					PetPropertieProvider.updatePetPropertie(context,
							petPropertie);
				}
			}

		}

	}

	@Override
	public void onUpdateSpeciesQueryFinished(boolean b,
			ArrayList<Specie> species) {
		if (b) {
			for (Specie specie : species) {
				Specie specieSaved = SpecieProvider.readSpecie(context,
						specie.getSystem_id());
				if (specieSaved == null) {
					SpecieProvider.insertSpecie(context, specie);
				} else {
					specie.setId(specieSaved.getId());
					SpecieProvider.updateSpecie(context, specie);
				}
			}

		}
	}

	@Override
	public void onAllMotivesQueryFinished(boolean b,
			ArrayList<com.arawaney.tumascotik.client.model.Service> motives) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateMotivesFinished(boolean b,
			ArrayList<com.arawaney.tumascotik.client.model.Service> motives) {
		if (b) {
			for (com.arawaney.tumascotik.client.model.Service service : motives) {
				com.arawaney.tumascotik.client.model.Service serviceSaved = ServiceProvider
						.readMotive(context, service.getSystem_id());
				if (serviceSaved == null) {
					ServiceProvider.insertMotive(context, service);
				} else {
					service.setId(serviceSaved.getId());
					ServiceProvider.updateMotive(context, service);
				}
			}

		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void updatePets() {
		ParsePetProvider.updatePets(this, MainController.USER, this);

	}

	@Override
	public void onPetRemoveFinished(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
