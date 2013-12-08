package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.listener.ParseListener;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.util.NetworkUtil;

public class PetInfoActivity extends Activity implements ParseListener {

	private static final String LOG_TAG = "Tumascotik-Client-PetinfoActivity";

	ImageView petAvatar;
	TextView petName;
	TextView petSpecie;
	TextView petBreed;
	TextView petGEnder;
//	TextView petProperties;
	TextView petPuppy;
	TextView petComments;

	EditText editpetName;
	Spinner editPetSpecie;
	Spinner editPetBreed;
	Spinner editPetGEnder;
//	Spinner editPetProperties;
	Spinner editPetPuppy;
	EditText editPetComments;

	Button saveEditButton;

	Pet pet;
	Pet auxPet;

	private ProgressDialog progressDialog;
	private boolean loadingSpecies = false;
	private boolean loadingBreeds = false;
//	private boolean loadingProperties = false;

	ArrayAdapter<String> SpecieAdapter;
	ArrayAdapter<String> BreedAdapter;
	ArrayAdapter<String> PropertiesAdapter;
	ArrayAdapter<CharSequence> GenderAdapter;
	ArrayAdapter<CharSequence> AgeAdapter;

	ArrayList<String> species;
	ArrayList<String> breeds;
	ArrayList<String> properties;
	List<String> genders;
	List<String> ages;

	public static final int MODE_INFO_LIST = 1;
	public static final int MODE_EDIT_LIST = 2;

	public static int viewMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pet_info);

		pet = MainController.getPET();

		auxPet = new Pet();

		if (pet != null) {

			auxPet.setId(pet.getId());
			auxPet.setSystem_id(pet.getSystem_id());
			auxPet.setOwner(pet.getOwner());

			if(viewMode == MODE_INFO_LIST){
			getUpdatedPet();}
			else if (viewMode == MODE_EDIT_LIST) {
				downloadSpecieLists();
			} else {

			}

			loadViews();
			loadButton();
			loadListsEvents();
			refreshView();
	

		} else {
			Log.d(LOG_TAG, "Pet choosen is null");
		}

	}

	private void loadListsEvents() {
		editPetSpecie.setOnItemSelectedListener( new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!editPetSpecie.getSelectedItem().equals(pet.getSpecie())) {
					downloadBreedLists() ;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	private void downloadLists() {
		if (NetworkUtil.ConnectedToInternet(this)) {

			ParseProvider.initializeParse(this);

			progressDialog = ProgressDialog
					.show(this, "", getResources().getString(R.string.pet_info_loading_lists));

			ParseProvider.getSpecies(this);
			ParseProvider.getBreed(this, pet.getSpecie());
//			ParseProvider.getProperties(this);

			loadingSpecies = true;
			loadingBreeds = true;
//			loadingProperties = true;

		}
	}

		private void downloadBreedLists() {
			if (NetworkUtil.ConnectedToInternet(this)) {

				ParseProvider.initializeParse(this);

				progressDialog = ProgressDialog
						.show(this, "", getResources().getString(R.string.pet_info_loading_breed_lists));

				ParseProvider.getBreed(this, editPetSpecie.getSelectedItem().toString());

				loadingSpecies = false;
				loadingBreeds = true;

			}
		}
			
			private void downloadSpecieLists() {
				if (NetworkUtil.ConnectedToInternet(this)) {

					ParseProvider.initializeParse(this);

					progressDialog = ProgressDialog
							.show(this, "", getResources().getString(R.string.pet_info_loading_specie_lists));

					ParseProvider.getSpecies(this);

					loadingSpecies = true;
					loadingBreeds = false;

				}
		

	}

	private void loadViews() {
		petAvatar = (ImageView) findViewById(R.id.image_pet_info_avatar);
		petName = (TextView) findViewById(R.id.text_pet_info_name);
		petSpecie = (TextView) findViewById(R.id.text_pet_info_specie);
		petBreed = (TextView) findViewById(R.id.text_pet_info_breed);
		petGEnder = (TextView) findViewById(R.id.text_pet_info_gender);
//		petProperties = (TextView) findViewById(R.id.text_pet_info_properties);
		petPuppy = (TextView) findViewById(R.id.text_pet_info_puppy);
		petComments = (TextView) findViewById(R.id.text_pet_info_comment);

		editpetName = (EditText) findViewById(R.id.edit_text_pet_info_name);
		editPetSpecie = (Spinner) findViewById(R.id.spiner_pet_info_specie);
		editPetBreed = (Spinner) findViewById(R.id.spiner_pet_info_breed);
		editPetGEnder = (Spinner) findViewById(R.id.spiner_pet_info_gender);
//		editPetProperties = (Spinner) findViewById(R.id.spiner_pet_info_properties);
		editPetPuppy = (Spinner) findViewById(R.id.spiner_pet_info_puppy);
		editPetComments = (EditText) findViewById(R.id.edit_text_pet_info_comment);

		saveEditButton = (Button) findViewById(R.id.button_pet_info_edit_save);
		
		GenderAdapter = ArrayAdapter.createFromResource(this, R.array.Genders,
				android.R.layout.simple_spinner_item);
		genders = Arrays.asList(getResources().getStringArray(R.array.Genders));
		AgeAdapter = ArrayAdapter.createFromResource(this, R.array.Age,
				android.R.layout.simple_spinner_item);
		ages = Arrays.asList(getResources().getStringArray(R.array.Age));

	}

	private void loadButton() {
		if (saveEditButton != null) {
			saveEditButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (viewMode == MODE_INFO_LIST) {
						viewMode = MODE_EDIT_LIST;
						downloadLists();
						refreshView();
					} else if (viewMode == MODE_EDIT_LIST) {
						setSavingPetDialog();
						savePet();
						insertOrUpdatePet();
						viewMode = MODE_INFO_LIST;
						refreshView();
					}

				}

				private void savePet() {
					auxPet.setName(editpetName.getText().toString());
					auxPet.setGender(editPetGEnder.getSelectedItemPosition());
//					auxPet.setPet_properties(editPetProperties
//							.getSelectedItem().toString());
					auxPet.setBreed(editPetBreed.getSelectedItem().toString());
					auxPet.setSpecie(editPetSpecie.getSelectedItem().toString());
					auxPet.setComment(editPetComments.getText().toString());
					auxPet.setPuppy(editPetPuppy.getSelectedItemPosition());

				}

			});

		}

	}

	private void refreshView() {

		if (viewMode == MODE_INFO_LIST) {
			setInfoView();
		} else if (viewMode == MODE_EDIT_LIST) {
			setEditView();
		}

	}

	private void setSavingPetDialog() {
		progressDialog = ProgressDialog.show(this, "", "Guardando mascota");
	}

	private void setEditView() {

		petName.setVisibility(View.GONE);
		petSpecie.setVisibility(View.GONE);
		petBreed.setVisibility(View.GONE);
		petGEnder.setVisibility(View.GONE);
//		petProperties.setVisibility(View.GONE);
		petPuppy.setVisibility(View.GONE);
		petComments.setVisibility(View.GONE);

		editpetName.setVisibility(View.VISIBLE);
		editPetSpecie.setVisibility(View.VISIBLE);
		editPetBreed.setVisibility(View.VISIBLE);
		editPetGEnder.setVisibility(View.VISIBLE);
//		editPetProperties.setVisibility(View.VISIBLE);
		editPetPuppy.setVisibility(View.VISIBLE);
		editPetComments.setVisibility(View.VISIBLE);

		if (pet.getName() != null) {
			editpetName.setText(pet.getName());
		}

		if (SpecieAdapter != null) {
			
			if (editPetSpecie.getAdapter() == null) {
				editPetSpecie.setAdapter(SpecieAdapter);
			}

			if (editPetSpecie.getSelectedItemPosition() == 0) {
				if (pet.getSpecie() != null) {
					editPetSpecie
							.setSelection(species.indexOf(pet.getSpecie()));
				}
			}
		}

		if (BreedAdapter != null) {
			editPetBreed.setAdapter(BreedAdapter);
			if (pet.getBreed() != null) {
				editPetBreed.setSelection(breeds.indexOf(pet.getBreed()));
			}
		}

		if (GenderAdapter != null) {
			editPetGEnder.setAdapter(GenderAdapter);
			Integer gendervalue = pet.getGender();
			if (gendervalue > 0) {
				editPetGEnder.setSelection(genders.indexOf(genders.get(gendervalue)));
			}
		}

		if (AgeAdapter != null) {
			editPetPuppy.setAdapter(AgeAdapter);
			Integer agevalue = pet.getPuppy();
			if (agevalue > 0) {
				editPetPuppy.setSelection(ages.indexOf(ages.get(agevalue)));
			}
		}

		if (pet.getComment() != null) {
			editPetComments.setText(pet.getComment());
		}

		saveEditButton.setText(getResources().getString(
				R.string.pet_info_button_save));

	}

	private void setInfoView() {

		petName.setVisibility(View.VISIBLE);
		petSpecie.setVisibility(View.VISIBLE);
		petBreed.setVisibility(View.VISIBLE);
		petGEnder.setVisibility(View.VISIBLE);
//		petProperties.setVisibility(View.VISIBLE);
		petPuppy.setVisibility(View.VISIBLE);
		petComments.setVisibility(View.VISIBLE);

		editpetName.setVisibility(View.GONE);
		editPetSpecie.setVisibility(View.GONE);
		editPetBreed.setVisibility(View.GONE);
		editPetGEnder.setVisibility(View.GONE);
//		editPetProperties.setVisibility(View.GONE);
		editPetPuppy.setVisibility(View.GONE);
		editPetComments.setVisibility(View.GONE);

		if (pet.getName() != null) {
			petName.setText(pet.getName());
		}

		if (pet.getSpecie() != null) {
			petSpecie.setText(pet.getSpecie());

		}

		if (pet.getBreed() != null) {
			petBreed.setText(pet.getBreed());
		}
//
//		if (pet.getPet_properties() != null) {
//			petProperties.setText(pet.getPet_properties());
//		}

		if (pet.getGender() > 0) {
			petGEnder.setText(genders.get(pet.getGender()));
		} else
			petGEnder.setText(genders.get(0));

		if (pet.getPuppy() > 0) {
			petPuppy.setText(ages.get(pet.getPuppy()));
		} else
			petPuppy.setText(ages.get(0));

		if (pet.getComment() != null) {
			petComments.setText(pet.getComment());
		}

		saveEditButton.setText(getResources().getString(
				R.string.pet_info_button_edit));

	}

	private void insertOrUpdatePet() {
		if (pet.getSystem_id() != null) {
			ParseProvider.updatePet(this, auxPet);
		} else {
			ParseProvider.insertPet(this, auxPet, this);
		}
	}

	private void getUpdatedPet() {
		ParseProvider.getPet(this, pet.getSystem_id());

	}

	@Override
	public void OnLoginResponse() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpecieQueryFinished(ArrayList<String> species) {
		loadingSpecies = false;

		if (species != null) {
			this.species = species;
			this.species.add(0, "--Choose--");
			SpecieAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, this.species);
		}

		if (!loadingSpecies && !loadingBreeds) {
			progressDialog.dismiss();
			refreshView();
		}
	}

	@Override
	public void onBreedQueryFinished(ArrayList<String> breed) {
		loadingBreeds = false;

		if (breed != null) {
			this.breeds = breed;
			this.breeds.add(0, "--Choose--");
			BreedAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, this.breeds);
		}

		if (!loadingSpecies && !loadingBreeds) {
			progressDialog.dismiss();
			refreshView();
		}

	}

//	@Override
//	public void onPropertiesQueryFinished(ArrayList<String> properties) {
//		loadingProperties = false;
//
//		if (properties != null) {
//			this.properties = properties;
//			this.properties.add(0, "--Choose--");
//			PropertiesAdapter = new ArrayAdapter<String>(this,
//					android.R.layout.simple_spinner_item, this.properties);
//		}
//
//		if (!loadingSpecies && !loadingBreeds && !loadingProperties) {
//			progressDialog.dismiss();
//			refreshView();
//		}
//	}

	@Override
	public void onPetQueryFinished(Pet pet) {
		if (this.pet != null) {
			if (pet != null) {
				pet.setId(this.pet.getId());
				pet.setSystem_id(this.pet.getSystem_id());
				this.pet = pet;
				PetProvider.updatePet(this, this.pet);
				refreshView();
			}
		}
	}

	@Override
	public void OnPetUpdateFinished(boolean b) {
		if (b) {
			pet = auxPet;
			PetProvider.updatePet(this, pet);

		}
		refreshView();
		progressDialog.dismiss();

	}

	@Override
	public void onGetAllPets(ArrayList<Pet> pets) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPetInserted(String objectId) {

		auxPet.setSystem_id(objectId);

		pet = auxPet;

		long localId = PetProvider.insertPet(this, pet);
		pet.setId(localId);
		refreshView();
		progressDialog.dismiss();

	}

}
