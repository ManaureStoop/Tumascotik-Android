package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Date;

import com.arawaney.tumascotik.client.adapter.ItemListBaseAdapter;
import com.arawaney.tumascotik.client.db.CitationDB;
import com.arawaney.tumascotik.client.dialog.ListItemDialog;
import com.arawaney.tumascotik.client.model.ItemDetails;

import com.arawaney.tumascotik.client.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VerCitas extends FragmentActivity {
	Button volver;
	ListView listcitas;
	Boolean flagclick;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vercitas);

		volver = (Button) findViewById(R.id.bvolverc);
		flagclick = false;

		ArrayList<ItemDetails> image_details = SetupList();

		listcitas = (ListView) findViewById(R.id.listverc);
		listcitas.setAdapter(new ItemListBaseAdapter(this, image_details));

		listcitas.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				if (!flagclick) {
					Object o = listcitas.getItemAtPosition(position);
					ItemDetails obj_itemDetails = (ItemDetails) o;
					DialogFragment newFragment = new ListItemDialog(
							obj_itemDetails.getName(), obj_itemDetails
									.getMotive(), obj_itemDetails
									.getInitialDate(), obj_itemDetails
									.getFinalDate(), getIntent()
									.getStringExtra("seleccion"));
					newFragment
							.show(getSupportFragmentManager(), "itemdetails");
				}
			}
		});
		volver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private ArrayList<ItemDetails> SetupList() {
		final int NAME_INDEX = 0;
		final int PET_INDEX = 1;
		final int MOTIVE_INDEX = 2;
		final int CELLPHONE_INDEX = 3;
		final int STARTDATE_INDEX = 4;
		final int ENDDATE_INDEX = 5;
		final int RACE_INDEX = 6;
		Date fechai;
		Date fechaf;
		int i = 1;
		ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
		Cursor cur;
		cur = null;

		CitationDB db = new CitationDB(this);
		db.open();
		// Depending on the intent the list will be built.
		if (getIntent().getStringExtra("seleccion").equals("ACEPTADAS")) {
			// Example TODO
			try {
				cur = db.getAccepted();
				cur.moveToFirst();

				do {

					ItemDetails item_details = new ItemDetails();
					item_details.setTitle(cur.getString(PET_INDEX));
					item_details.setItemDescription("Cita para : "
							+ cur.getString(MOTIVE_INDEX));

					fechai = new Date(Long.parseLong(cur
							.getString(STARTDATE_INDEX)));
					fechaf = new Date(Long.parseLong(cur
							.getString(ENDDATE_INDEX)));
					item_details.setTime("D�a : "
							+ android.text.format.DateFormat.format("EEEE, dd",
									fechai)
							+ System.getProperty("line.separator").toString()
							+ android.text.format.DateFormat.format(
									"         MMMM, yyyy", fechai)
							+ System.getProperty("line.separator").toString()
							+ "Hora : "
							+ android.text.format.DateFormat.format("h:mmaa",
									fechai)
							+ "-"
							+ android.text.format.DateFormat.format("h:mmaa",
									fechaf));
					item_details.setName(cur.getString(PET_INDEX));
					item_details.setRace(cur.getString(RACE_INDEX));
					// Have to save this data in order tu delete it from
					// database and from Parse cloud if necesary
					item_details.setMotive(cur.getString(MOTIVE_INDEX));
					item_details.setInitalDate(fechai);
					item_details.setFinalDate(fechaf);

					results.add(item_details);

				} while (cur.moveToNext());

			} catch (Exception e) {
				Log.d("ERROR DATE", e.toString());
				ItemDetails item_details = new ItemDetails();
				item_details.setRace("nohaycitas");
				item_details.setTitle(" ");
				item_details.setItemDescription("");
				item_details.setTime(" ");
				results.add(item_details);
				flagclick = true;

			}

		} else if (getIntent().getStringExtra("seleccion").equals("PENDIENTES")) {
			// Example TODO
			try {
				cur = db.getPending();
				cur.moveToFirst();

				do {

					ItemDetails item_details = new ItemDetails();
					item_details.setTitle(cur.getString(PET_INDEX));
					item_details.setItemDescription("Cita para : "
							+ cur.getString(MOTIVE_INDEX));

					fechai = new Date(Long.parseLong(cur
							.getString(STARTDATE_INDEX)));
					fechaf = new Date(Long.parseLong(cur
							.getString(ENDDATE_INDEX)));

					Log.d("FECHAQUEQUIERO!", cur.getString(STARTDATE_INDEX));

					item_details.setTime("D�a : "
							+ android.text.format.DateFormat.format("EEEE, dd",
									fechai)
							+ System.getProperty("line.separator").toString()
							+ android.text.format.DateFormat.format(
									"         MMMM, yyyy", fechai)
							+ System.getProperty("line.separator").toString()
							+ "Hora : "
							+ android.text.format.DateFormat.format("h:mmaa",
									fechai)
							+ "-"
							+ android.text.format.DateFormat.format("h:mmaa",
									fechaf));
					item_details.setName(cur.getString(PET_INDEX));
					item_details.setRace(cur.getString(RACE_INDEX));
					Log.d("RAZA", cur.getString(RACE_INDEX));
					// Have o save this data in order tu delete it from database
					// and from Parse cloud if necesary
					item_details.setMotive(cur.getString(MOTIVE_INDEX));
					item_details.setInitalDate(fechai);
					item_details.setFinalDate(fechaf);

					results.add(item_details);

				} while (cur.moveToNext());

			} catch (Exception e) {
				Log.d("ERROR DATE", e.toString());
				ItemDetails item_details = new ItemDetails();
				item_details.setTitle("  ");
				item_details.setRace("nohaycitas");
				item_details.setItemDescription("");
				item_details.setTime(" ");
				results.add(item_details);
				flagclick = true;
			}

		}
		db.close();
		cur.close();
		return results;
	}

	public void Refresh() {
		// TODO Auto-generated method stub
		super.onResume();
		ArrayList<ItemDetails> image_details = SetupList();
		listcitas = (ListView) findViewById(R.id.listverc);
		listcitas.setAdapter(new ItemListBaseAdapter(this, image_details));
	}
}
