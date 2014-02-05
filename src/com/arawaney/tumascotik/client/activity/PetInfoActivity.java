package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.arawaney.tumascotik.client.backend.ParsePetProvider;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.BreedProvider;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.SpecieProvider;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Specie;
import com.arawaney.tumascotik.client.util.BitMapUtil;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.util.NetworkUtil;

public class PetInfoActivity extends Activity implements ParsePetListener {

	private static final String LOG_TAG = "Tumascotik-Client-PetinfoActivity";

	ImageView petAvatar;
	TextView petName;
	TextView petSpecie;
	TextView petBreed;
	TextView petGEnder;
	TextView petPuppy;
	TextView petAgressive;
	TextView petComments;

	EditText editpetName;
	Spinner editPetSpecie;
	Spinner editPetBreed;
	Spinner editPetGEnder;
	Spinner editPetPuppy;
	EditText editPetComments;
	Button isAgressive;

	ImageView saveEditButton;

	Pet pet;
	Pet auxPet;
	ArrayList<Breed> breeds;
	ArrayList<Specie> species;

	private ProgressDialog progressDialog;

	ArrayAdapter<String> SpecieAdapter;
	ArrayAdapter<String> BreedAdapter;
	ArrayAdapter<String> PropertiesAdapter;
	ArrayAdapter<CharSequence> GenderAdapter;
	ArrayAdapter<CharSequence> AgeAdapter;

	ArrayList<String> speciesNames;
	ArrayList<String> breedsNames;
	ArrayList<String> properties;
	List<String> genders;
	List<String> ages;

	String auxSpecie;

	private boolean isAgressiveStatus;

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

			// auxPet.setId(pet.getId());
			// auxPet.setSystem_id(pet.getSystem_id());
			// auxPet.setOwner(pet.getOwner());

			auxPet = pet;

			getLists();

			loadViews();
			loadButtons();
			loadListsEvents();
			refreshView();

		} else {
			Log.d(LOG_TAG, "Pet choosen is null");
		}

	}

	private void getLists() {
		speciesNames = new ArrayList<String>();
		breedsNames = new ArrayList<String>();

		species = SpecieProvider.readSpecies(this);
		if (species != null) {
			for (Specie specie : species) {
				speciesNames.add(specie.getName());
			}
		}

		breeds = BreedProvider.readBreedBySpecie(this, pet.getBreed()
				.getSpecie().getSystem_id());

		if (breeds != null) {
			for (Breed breed : breeds) {
				breedsNames.add(breed.getName());

			}
		}
	}

	private void loadListsEvents() {
		editPetSpecie.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!editPetSpecie.getSelectedItem().equals(auxSpecie)) {
					getBreedLists(editPetSpecie.getSelectedItem().toString());
					auxSpecie = editPetSpecie.getSelectedItem().toString();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void getBreedLists(String specieName) {
		Specie specieSelected = new Specie();
		for (Specie specie : species) {
			if (specie.getName().equals(specieName)) {
				specieSelected = specie;
			}
		}

		breeds = BreedProvider.readBreedBySpecie(this,
				specieSelected.getSystem_id());
		breedsNames = new ArrayList<String>();
		if (breeds != null) {
			for (Breed breed : breeds) {
				breedsNames.add(breed.getName());
			}
		}
		BreedAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, breedsNames);
		editPetBreed.setAdapter(BreedAdapter);
	}

	private void loadViews() {
		petAvatar = (ImageView) findViewById(R.id.image_pet_info_avatar);
		petName = (TextView) findViewById(R.id.text_pet_info_name);
		petSpecie = (TextView) findViewById(R.id.text_pet_info_specie);
		petBreed = (TextView) findViewById(R.id.text_pet_info_breed);
		petGEnder = (TextView) findViewById(R.id.text_pet_info_gender);
		petPuppy = (TextView) findViewById(R.id.text_pet_info_puppy);
		petComments = (TextView) findViewById(R.id.text_pet_info_comment);
		petAgressive = (TextView) findViewById(R.id.text_pet_info_isagressive);

		editpetName = (EditText) findViewById(R.id.edit_text_pet_info_name);
		editPetSpecie = (Spinner) findViewById(R.id.spiner_pet_info_specie);
		editPetBreed = (Spinner) findViewById(R.id.spiner_pet_info_breed);
		editPetGEnder = (Spinner) findViewById(R.id.spiner_pet_info_gender);
		editPetPuppy = (Spinner) findViewById(R.id.spiner_pet_info_puppy);
		editPetComments = (EditText) findViewById(R.id.edit_text_pet_info_comment);
		isAgressive = (Button) findViewById(R.id.Button_pet_info_isagressive);

		isAgressiveStatus = false;

		saveEditButton = (ImageView) findViewById(R.id.button_pet_info_edit_save);

		GenderAdapter = ArrayAdapter.createFromResource(this, R.array.Genders,
				android.R.layout.simple_spinner_item);
		genders = Arrays.asList(getResources().getStringArray(R.array.Genders));

		AgeAdapter = ArrayAdapter.createFromResource(this, R.array.Age,
				android.R.layout.simple_spinner_item);
		ages = Arrays.asList(getResources().getStringArray(R.array.Age));

		SpecieAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, speciesNames);
		BreedAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, breedsNames);
		setFonts();

	}

	private void setFonts() {

		petName.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		petSpecie
				.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		petBreed.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		petGEnder
				.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		petPuppy.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		petAgressive.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		isAgressive.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		petComments.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		editpetName.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		editPetComments.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));

	}

	private void loadButtons() {
		if (saveEditButton != null) {
			saveEditButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (viewMode == MODE_INFO_LIST) {

						viewMode = MODE_EDIT_LIST;
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

					Breed breed = getBreedFromList();
					if (breed != null) {
						auxPet.setBreed(breed);
					}

					auxPet.setComment(editPetComments.getText().toString());
					auxPet.setPuppy(editPetPuppy.getSelectedItemPosition());
					if (isAgressiveStatus) {
						auxPet.setAgressive(Pet.AGRESSIVE);
					} else {
						auxPet.setAgressive(Pet.NOT_AGRESSIVE);
					}

				}

			});

		}

		isAgressive.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (isAgressiveStatus) {
					isAgressiveStatus = false;
					isAgressive.setBackground(getResources().getDrawable(
							R.drawable.ic_yellow_button_pressed));
					isAgressive.setText(R.string.pet_info_title_agressive_no);
				} else {
					isAgressiveStatus = true;
					isAgressive.setBackground(getResources().getDrawable(
							R.drawable.ic_yellow_button));
					isAgressive.setText(R.string.pet_info_title_agressive_yes);
				}

			}
		});

	}

	private Breed getBreedFromList() {

		for (Breed breed : breeds) {
			if (breed.getName().equals(
					editPetBreed.getSelectedItem().toString())) {
				return breed;
			}
		}
		return null;

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

	@SuppressLint("NewApi")
	private void setEditView() {

		petName.setVisibility(View.GONE);
		petSpecie.setVisibility(View.GONE);
		petBreed.setVisibility(View.GONE);
		petGEnder.setVisibility(View.GONE);
		petPuppy.setVisibility(View.GONE);
		petComments.setVisibility(View.GONE);
		petAgressive.setVisibility(View.GONE);

		editpetName.setVisibility(View.VISIBLE);
		editPetSpecie.setVisibility(View.VISIBLE);
		editPetBreed.setVisibility(View.VISIBLE);
		editPetGEnder.setVisibility(View.VISIBLE);
		editPetPuppy.setVisibility(View.VISIBLE);
		editPetComments.setVisibility(View.VISIBLE);
		isAgressive.setVisibility(View.VISIBLE);

		if (pet.getName() != null) {
			editpetName.setText(pet.getName());
		}

		if (SpecieAdapter != null) {

			if (editPetSpecie.getAdapter() == null) {
				editPetSpecie.setAdapter(SpecieAdapter);
			}

			if (editPetSpecie.getSelectedItemPosition() == 0) {
				if (pet.getBreed().getSpecie() != null) {
					editPetSpecie.setSelection(speciesNames.indexOf(pet
							.getBreed().getSpecie().getName()));
					auxSpecie = (String) editPetSpecie.getSelectedItem();
				}
			}
		}

		if (BreedAdapter != null) {
			editPetBreed.setAdapter(BreedAdapter);
			if (pet.getBreed() != null) {
				editPetBreed.setSelection(breedsNames.indexOf(pet.getBreed()
						.getName()));
			}
		}

		if (GenderAdapter != null) {
			editPetGEnder.setAdapter(GenderAdapter);
			Integer gendervalue = pet.getGender();
			if (gendervalue > 0) {
				editPetGEnder.setSelection(genders.indexOf(genders
						.get(gendervalue)));
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

		if (pet.getAgressive() != null) {
			if (pet.getAgressive() == Pet.AGRESSIVE) {
				isAgressive.setText(getResources().getString(
						R.string.pet_info_title_agressive_yes));
				isAgressive.setBackground(getResources().getDrawable(
						R.drawable.ic_yellow_button));
			} else if (pet.getAgressive() == Pet.NOT_AGRESSIVE) {
				isAgressive.setText(getResources().getString(
						R.string.pet_info_title_agressive_no));
				isAgressive.setBackground(getResources().getDrawable(
						R.drawable.ic_yellow_button_pressed));
			}

		}

		String path = (String) editPetSpecie.getSelectedItem();
		if (path == null) {
			path = "other";
		}
		petAvatar.setImageResource(BitMapUtil.getImageId(this, path));

		saveEditButton.setImageResource(R.drawable.buton_check);

	}

	private void setInfoView() {

		petName.setVisibility(View.VISIBLE);
		petSpecie.setVisibility(View.VISIBLE);
		petBreed.setVisibility(View.VISIBLE);
		petGEnder.setVisibility(View.VISIBLE);
		petPuppy.setVisibility(View.VISIBLE);
		petComments.setVisibility(View.VISIBLE);
		petAgressive.setVisibility(View.VISIBLE);

		editpetName.setVisibility(View.GONE);
		editPetSpecie.setVisibility(View.GONE);
		editPetBreed.setVisibility(View.GONE);
		editPetGEnder.setVisibility(View.GONE);
		editPetPuppy.setVisibility(View.GONE);
		editPetComments.setVisibility(View.GONE);
		isAgressive.setVisibility(View.GONE);

		if (pet.getName() != null) {
			petName.setText(pet.getName());
		}

		if (pet.getBreed().getSpecie() != null) {
			petSpecie.setText(pet.getBreed().getSpecie().getName());

		}

		if (pet.getBreed() != null) {
			petBreed.setText(pet.getBreed().getName());
		}

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

		if (pet.getAgressive() != null) {
			if (pet.getAgressive() == Pet.AGRESSIVE) {
				petAgressive.setText(getResources().getString(
						R.string.pet_info_title_agressive_yes));
			} else if (pet.getAgressive() == Pet.NOT_AGRESSIVE) {
				petAgressive.setText(getResources().getString(
						R.string.pet_info_title_agressive_no));
			}

		}

		String path = (String) petSpecie.getText();

		if (path == null) {
			path = "other";
		}
		petAvatar.setImageResource(BitMapUtil.getImageId(this, path));

		saveEditButton.setImageResource(R.drawable.buton_edit);

	}

	private void insertOrUpdatePet() {
		if (pet.getSystem_id() != null) {
			ParsePetProvider.updatePet(this, auxPet);
		} else {
			ParsePetProvider.insertPet(this, auxPet, this);
		}
	}

	@Override
	public void onPetQueryFinished(Pet pet) {
		if (this.pet != null) {
			if (pet != null) {
				pet.setId(this.pet.getId());
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

		} else {
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.user_info_user_not_updated),
					Toast.LENGTH_LONG).show();
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

	@Override
	public void onAllSpeciesQueryFinished(boolean b, ArrayList<Specie> species) {
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
	public void onBreedQueryFinished(ArrayList<String> breed) {
		// TODO Auto-generated method stub

	}

}
