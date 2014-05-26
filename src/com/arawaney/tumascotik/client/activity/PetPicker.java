package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.MainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.ViewRequestBaseAdapter;
import com.arawaney.tumascotik.client.adapter.ItemPetGridAdapter;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class PetPicker extends Activity {
	private final String LOG_TAG = "Tumascotik-Client-Pet Picker";

	ArrayList<Pet> pets;
	ListView petGRidView;
	View addPetView;
	private LayoutInflater inflater;
	ItemPetGridAdapter adapter;
	public static int functionMode;

	public static final int MODE_MAKE_APPOINTMENT = 1;
	public static final int MODE_EDIT_PET = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picker);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
Log.d(LOG_TAG, "METRICS:"+metrics.densityDpi);
		loadViews();
		loadPets();
		loadAddButtons();

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPets();
	}

	private void loadViews() {
		petGRidView = (ListView) findViewById(R.id.grid_picker);
		inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
		addPetView = inflater.inflate(R.layout.add_view, null);

	}

	private boolean userIsClient() {
		if (MainController.USER.getisAdmin() == User.IS_ADMIN) {
			if (MainController.getCLIENTUSER().getisAdmin() == User.NOT_ADMIN) {
				return true;
			}
		}
		return false;
	}

	private void loadPets() {

		if (userIsClient()) {
			pets = PetProvider.readPetsByUser(this, MainController
					.getCLIENTUSER().getSystemId());
		} else
			pets = PetProvider.readPets(this);

		if (pets == null) {
			pets = new ArrayList<Pet>();
		}

		adapter = new ItemPetGridAdapter(this, pets);
		adapter.setFooterView(addPetView);
		petGRidView.setAdapter(adapter);

	}

	private void loadAddButtons() {
		addPetView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(LOG_TAG, "Add new pet!!");
				Pet pet = new Pet();
				User owner;
				if (userIsClient()) {
					owner = MainController.getCLIENTUSER();
				} else {
					owner = MainController.USER;
				}
				pet.setOwner(owner);
				MainController.setPET(pet);
				PetInfoActivity.viewMode = PetInfoActivity.MODE_EDIT_LIST;
				Intent i = new Intent(PetPicker.this, PetInfoActivity.class);
				startActivity(i);

			}
		});

		petGRidView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				MainController.setPET(pets.get(position));
				if (functionMode == MODE_EDIT_PET) {
					PetInfoActivity.viewMode = PetInfoActivity.MODE_INFO_LIST;
					Intent i = new Intent(PetPicker.this, PetInfoActivity.class);
					startActivity(i);
				} else if (functionMode == MODE_MAKE_APPOINTMENT) {
					if (!petDataComplete(MainController.getPET())) {
						PetInfoActivity.viewMode = PetInfoActivity.MODE_EDIT_LIST;
						Intent i = new Intent(PetPicker.this,
								PetInfoActivity.class);
						startActivity(i);
					} else {
						Intent i = new Intent(PetPicker.this,
								SetRequestDetails.class);
						startActivity(i);
					}
				}

			}
		});
	}

	boolean petDataComplete(Pet pet) {

		boolean dataComplete = true;

		if (pet.getName() == null) {

			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getName().isEmpty()) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getBreed() == null) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getBreed().getPetPropertie() == null) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getBreed().getSpecie() == null) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getBreed().getSpecie().getName() == null) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getBreed().getPetPropertie().getName() == null) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		if (pet.getAgressive() == null) {
			dataComplete = false;
			notifyDataIncomplete();
			return dataComplete;
		}
		return dataComplete;
	}

	private void notifyDataIncomplete() {
		String toastText = getResources().getString(
				R.string.pet_info_pet_data_incomplete);
		Toast toast = Toast.makeText(PetPicker.this, toastText,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

}
