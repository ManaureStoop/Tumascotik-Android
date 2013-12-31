package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.ClientMainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.ItemListBaseAdapter;
import com.arawaney.tumascotik.client.adapter.ItemPetGridAdapter;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.GridView;

public class PetPicker extends Activity {
	private final String LOG_TAG = "Tumascotik-Client-Pet Picker";
	
	ArrayList<Pet> pets;
	GridView petGRidView;
	View addPetView;
	private LayoutInflater inflater;
	ItemPetGridAdapter adapter;
	public static int functionMode;
	
	public static final int MODE_MAKE_APPOINTMENT = 1;
	public static final int MODE_EDIT_PET = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_pet);
		
		loadViews();
		loadPets();
		loadAddButtons();
		
	}


	private void loadViews() {
		petGRidView = (GridView) findViewById(R.id.grid_pet_picker);
		inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
		addPetView = inflater.inflate(R.layout.add_pet_view, null);
	}
	
	private void loadPets() {
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
				User owner = UserProvider.readUser(getApplicationContext());
				pet.setOwner(owner);
				MainController.setPET(pet);
				PetInfoActivity.viewMode = PetInfoActivity.MODE_EDIT_LIST;
				Intent i = new Intent(PetPicker.this, PetInfoActivity.class);
				startActivity(i);
				
			}
		});
		
		petGRidView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				
				if (pets.get(position).getName() != null) {
					Log.d(LOG_TAG, "name : "+pets.get(position).getName());
			
				}
				if (pets.get(position).getComment() != null) {
					Log.d(LOG_TAG, "comment : "+pets.get(position).getComment() );
			
				}
				if (pets.get(position).getBreed() != null) {
				Log.d(LOG_TAG, "breed : "+pets.get(position).getBreed());
		
			}
			if (pets.get(position).getSpecie() != null) {
				Log.d(LOG_TAG, "specie : "+pets.get(position).getSpecie());
		
			}
			if (pets.get(position).getPet_properties() != null) {
				Log.d(LOG_TAG, "properties : "+pets.get(position).getPet_properties());
			
			}
				MainController.setPET(pets.get(position));
				if (functionMode == MODE_EDIT_PET) {
					PetInfoActivity.viewMode = PetInfoActivity.MODE_INFO_LIST;
					Intent i = new Intent(PetPicker.this, PetInfoActivity.class);
					startActivity(i);
				}else if (functionMode == MODE_MAKE_APPOINTMENT) {
					Intent i = new Intent(PetPicker.this, SetDate.class);
					startActivity(i);
				}
				
				
				
			}
		});
	}
	
	

}
