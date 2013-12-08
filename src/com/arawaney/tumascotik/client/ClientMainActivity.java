package com.arawaney.tumascotik.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.arawaney.tumascotik.client.activity.Pedircita;
import com.arawaney.tumascotik.client.activity.PetPicker;
import com.arawaney.tumascotik.client.activity.Presupuesto;
import com.arawaney.tumascotik.client.activity.SetDate;
import com.arawaney.tumascotik.client.activity.VerCitas;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.CitationDB;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.dialog.ConnectionDialog;
import com.arawaney.tumascotik.client.dialog.SelectFragmentDialog;
import com.arawaney.tumascotik.client.listener.ParseListener;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.util.NetworkUtil;
//import com.arawaney.tumascotik.client.R
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

public class ClientMainActivity extends FragmentActivity implements ParseListener {
	Date fechainicio;
	ProgressDialog progressDialog;
	SharedPreferences savedpendingappoint;
	int pendingApointments;
	
	private final String LOG_TAG = "Tumascotik-Client-Main Menu";

	// To know if the function "resetrequest" is already running. If so it can
	// be called again.
	boolean bussy;
	// To know if the refreshed was called from the logo button or was made
	// automatic
	boolean calledfromlogobuton;
	// Main Layouts
	LinearLayout main_buttons_layout;
	LinearLayout login_layout;
	// Log in views
	EditText text_username;
	EditText text_password;
	Button button_login;
	// Main menu views
	Button pedirc;
	Button vercit;
	Button pedirpres;
	// Header view
	Button logorefresh;
	// Footer views
	Button youtube;
	Button facebook;
	Button twitter;
	Button emergencia;
	
	//Navigation Drawer
    private String[] navigationMenuTitles;
   // private  mDrawerLayout;
    private ListView mDrawerList;

	MainController mainController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Removing activity title from Layout
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		bussy = false;
		calledfromlogobuton = false;

		loadViews();
		loadButtons();
	
		if (MainController.Initialize(this)) {
			setMainMenu();
			checkingInternetConnections();
			loadPets();

			
		} else {
			setLogIn();
			checkingInternetConnections();

		}



		// reset_DataBase_SharedP();

	}

	private void loadPets() {
		ParseProvider.getPets(this, MainController.USER);
		
	}

	private void setLogIn() {
		login_layout.setVisibility(View.VISIBLE);
		main_buttons_layout.setVisibility(View.GONE);

	}

	private void setMainMenu() {
		Log.d(LOG_TAG, "set main menu");

		login_layout.setVisibility(View.GONE);
		main_buttons_layout.setVisibility(View.VISIBLE);

		// Getting Shared Preferences of data saved in App
		savedpendingappoint = getSharedPreferences("TUMASC", 0);
		getNumberOfPendingAppointments(savedpendingappoint);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_mypets:
	            openPetPicker();
	            return true;
	        case R.id.menu_profile:
	        	openMyProfile();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void openMyProfile() {
		
		
	}

	private void openPetPicker() {
		Intent i = new Intent(ClientMainActivity.this, PetPicker.class);
		startActivity(i);
		
	}

	private void loadButtons() {
		pedirc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(ClientMainActivity.this, SetDate.class);
				startActivity(i);
			}
		});

		vercit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showAppointmentFragmentDialog(v);
			}
		});
		pedirpres.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ClientMainActivity.this, Presupuesto.class);
				startActivity(i);
			}
		});
		youtube.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.youtube.com/watch?v=XIMprgXfrCo")));
			}
		});

		facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(getOpenFacebookIntent(ClientMainActivity.this));
			}
		});

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(getOpenTwitterIntent(ClientMainActivity.this));
			}
		});
		logorefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (pendingApointments != 0) {
					progressDialog = ProgressDialog.show(ClientMainActivity.this,
							"Tumascotik", "Actualizando citas...");
					calledfromlogobuton = true;
					loadPendingObjects();
				}

			}
		});
		emergencia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:04142564227"));
				startActivity(callIntent);

			}
		});

		button_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String username = text_username.getText().toString();
				String password = text_password.getText().toString();

				ParseProvider.logIn(username, password, ClientMainActivity.this, ClientMainActivity.this);
				

			}
		});
	}

	private void loadViews() {
		main_buttons_layout = (LinearLayout) findViewById(R.id.main_menu_buttons);
		login_layout = (LinearLayout) findViewById(R.id.log_in_menu);
		text_password = (EditText) findViewById(R.id.text_login_passw);
		text_username = (EditText) findViewById(R.id.text_login_username);
		button_login = (Button) findViewById(R.id.button_login);
		pedirc = (Button) findViewById(R.id.pdrcitamenu);
		vercit = (Button) findViewById(R.id.bvercitasmenu);
		pedirpres = (Button) findViewById(R.id.bpedirpresmenu);
		facebook = (Button) findViewById(R.id.bfacebookmenu);
		twitter = (Button) findViewById(R.id.btwittermenu);
		youtube = (Button) findViewById(R.id.byoutubemenu);
		logorefresh = (Button) findViewById(R.id.blogorefreshmenu);
		emergencia = (Button) findViewById(R.id.bemergenciamenu);

	}

	private void getNumberOfPendingAppointments(SharedPreferences settings) {
		// Get number off Pending Appointments
		pendingApointments = settings.getInt("index", 0);
		Log.d("INDICE EN SHARED = ", String.valueOf(pendingApointments));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_menu,
				(android.view.Menu) menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mainController.isActive()) {
			checkingInternetConnections();
		}
	}

	public void resetSavedPendings(int ind) {
		int i;
		int j;

		waitTurnToModify();

		// taking turn to modify
		bussy = true;

		SharedPreferences.Editor editor = savedpendingappoint.edit();
		for (i = ind; i < (pendingApointments - 1); i++) {
			j = i + 1;
			editor.putInt("año" + i, savedpendingappoint.getInt("año" + j, 0));
			editor.putInt("mes" + i, savedpendingappoint.getInt("mes" + j, 0));
			editor.putInt("dia" + i, savedpendingappoint.getInt("dia" + j, 0));
			editor.putInt("horai" + i,
					savedpendingappoint.getInt("horai" + j, 0));
			editor.putInt("minutoi" + i,
					savedpendingappoint.getInt("minutoi" + j, 0));
		}
		pendingApointments--;
		editor.putInt("index", pendingApointments);
		editor.commit();
		bussy = false;
	}

	private void waitTurnToModify() {
		// wait if other process is reseting pending appointments
		if (bussy) {
			while (bussy) {

			}
		}
	}

	public void refresh(int ind) {
		// Does the Parse Query to see if accepted or rejected. 1 means accepted
		// and goes
		// Automatically to 4. A 2 means rejected and it gets erased

		final int in = ind;
		ParseQuery query = new ParseQuery("Citas");
		query.whereEqualTo("fechaInicial", fechainicio);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> cList, ParseException e) {
				ParseObject object;
				if (e == null) {
					if (cList.size() != 0) {
						object = cList.get(0);
						if (object.getNumber("aceptado") == (Number) 1) {

							notifyUser(true, object.getString("mascota"));

							writeCalendar(object.getString("mascota"),
									object.getDate("fechaInicial"),
									object.getDate("fechaFinal"));

							// Passes "Cita" from pending to accepted in
							// database
							UpdateDataBase(true, object.getDate("fechaInicial"));
							// Puts "Cita" in Parse from 1 to 4
							object.put("aceptado", 4);
							object.saveInBackground();

						} else if (object.getNumber("aceptado") == (Number) 2) {

							notifyUser(false, object.getString("mascota"));

							// Erase "Cita" from database
							UpdateDataBase(false,
									object.getDate("fechaInicial"));

							// Delete "Cita" from calender in case it was
							// already accepted
							if (object.getNumber("nuevo") != (Number) 0) {
								deleteCalendar(
										fechainicio.getTime(),
										object.getDate("fechaFinal").getTime(),
										"Cita con Tumascotik para "
												+ object.getString("mascota"));
							}
							// Erase "Cita" from Parse
							object.deleteInBackground();

							// // Erase "Cita" from Shared Preferences
							resetSavedPendings(in);
						} else if (object.getNumber("aceptado") == (Number) 0
								|| object.getNumber("aceptado") == (Number) 4)
							Log.d("Sigue igual", "Sigue igual");
					} else
						Log.d("ERROR", "No hay actividad");

				} else {
					Log.d("ERROR", "Error en refresh!");
				}

				if (calledfromlogobuton) {
					progressDialog.dismiss();
					calledfromlogobuton = false;

				}
			}

		});

	}

	public void loadPendingObjects() {
		// Loads every object pending to see if accepted or rejected
		int index;
		pendingApointments = savedpendingappoint.getInt("index", 0);
		for (index = 0; index < pendingApointments; index++) {
			fechainicio = new GregorianCalendar(savedpendingappoint.getInt(
					"a�o" + index, 0), savedpendingappoint.getInt(
					"mes" + index, 0), savedpendingappoint.getInt(
					"dia" + index, 0), savedpendingappoint.getInt("horai"
					+ index, 0), savedpendingappoint.getInt("minutoi" + index,
					0)).getTime();
			Log.d("fechaINICIO", fechainicio.toString());
			refresh(index);
		}

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void notifyUser(boolean vet_accepted, String petsname) {
		String toasttext;
		String notificationtitle;
		String notificationcontent;
		Class tapclass;
		if (vet_accepted) {
			toasttext = "Cita para " + petsname + " con Tumascotik Aceptada!";
			notificationtitle = "Cita para " + petsname + " aceptada";
			notificationcontent = "La cita fue agregada en su Agenda";
			tapclass = VerCitas.class;

		} else {
			toasttext = "Su veterinario no puede atender a " + petsname;
			notificationtitle = "Cita para " + petsname + " rechazada";
			notificationcontent = "Disculpe, por favor elija una nueva cita";
			tapclass = Pedircita.class;

		}

		showToastStatusBarNotif(toasttext, notificationtitle,
				notificationcontent, tapclass);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showToastStatusBarNotif(String toasttext,
			String notificationtitle, String notificationcontent, Class tapclass) {
		// Generating Toast notification
		Toast toast = Toast.makeText(ClientMainActivity.this, toasttext, 8000);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// Generating Status Bar notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(notificationtitle)
				.setContentText(notificationcontent)
				.setSound(
						Uri.parse("android.resource://com.example.tumascotikmenu/"
								+ R.raw.bark));
		// Add event to
		Intent resultIntent = new Intent(this, tapclass);
		resultIntent.putExtra("seleccion", "ACEPTADAS");
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Pedircita.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(0, mBuilder.build());
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void writeCalendar(String mascota, Date fechai, Date fechaf) {
		// Writes Appointment on the Calendar
		String datebegin = String.valueOf(fechai.getTime());
		String dateend = String.valueOf(fechaf.getTime());
		String es = "Cita con Tumascotik para " + mascota;
		try {
			ContentValues eventvalue = new ContentValues();
			writeEvent(datebegin, dateend, es, eventvalue);
			Uri event = getContentResolver().insert(
					CalendarContract.Events.CONTENT_URI, eventvalue);
			setupAlarm(event, eventvalue);

		} catch (Exception e) {
			Log.e("ERROR", "error writing to calendar", e);
		}
		;
	}

	private void writeEvent(String datebegin, String dateend, String es,
			ContentValues eventvalue) {
		eventvalue.put(CalendarContract.Events.TITLE, es);
		eventvalue.put(CalendarContract.Events.DTSTART, datebegin);
		eventvalue.put(CalendarContract.Events.DTEND, dateend);
		eventvalue.put(CalendarContract.Events.CALENDAR_ID, 1);
		eventvalue.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone
				.getDefault().getID());
	}

	private void setupAlarm(Uri event, ContentValues eventvalue) {
		// Setting up Alarm 15 minutes before appointment
		eventvalue = new ContentValues();
		eventvalue.put(Reminders.MINUTES, 15);
		eventvalue.put(Reminders.EVENT_ID,
				Long.parseLong(event.getLastPathSegment()));
		eventvalue.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri = getContentResolver()
				.insert(Reminders.CONTENT_URI, eventvalue);
	}

	void UpdateDataBase(boolean accepted, Date fechai) {
		CitationDB db = new CitationDB(this);
		db.open();
		if (accepted) {
			Log.d("UPDATEDATA", "ACEPTAR");
			db.update(String.valueOf(fechai.getTime()), 1);
		} else {
			Log.d("UPDATEDATA", "BORRAR");
			db.delete(String.valueOf(fechai.getTime()));
		}
		db.close();
	}

	private void deleteCalendar(long startQueryUTC, long endQueryUTC,
			String title) {
		// URI Builder
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
				.buildUpon();
		ContentUris.appendId(eventsUriBuilder, startQueryUTC);
		ContentUris.appendId(eventsUriBuilder, endQueryUTC);
		Uri eventsDeleteUri = eventsUriBuilder.build();

		// Delete

		ContentResolver cr = this.getContentResolver();

		try {
			Cursor c = cr.query(eventsDeleteUri,
					new String[] { CalendarContract.Instances.EVENT_ID }, null,
					null, null);
			c.moveToFirst();
			long eventID = c.getLong(0);
			c.close();
			Uri deleteUri = null;
			deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
			cr.delete(deleteUri, null, null);
		}

		catch (Exception e) {

			Log.d("DELETE", "ERROR");

		}

	}

	public void showAppointmentFragmentDialog(View v) {

		DialogFragment newFragment = new SelectFragmentDialog();
		newFragment.show(getFragmentManager(), "appointment");

	}

	public void appointmentCall(int selection) {
		String select;
		if (selection == 0)
			select = "ACEPTADAS";
		else
			select = "PENDIENTES";

		Intent vercitasIntent = new Intent(ClientMainActivity.this, VerCitas.class);
		vercitasIntent.putExtra("seleccion", select);
		startActivity(vercitasIntent);

	}

	public static Intent getOpenFacebookIntent(Context context) {

		try {
			context.getPackageManager()
					.getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("fb://profile/100000796583747"));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://www.facebook.com/tumascotik"));
		}
	}

	public static Intent getOpenTwitterIntent(Context context) {
		Intent intent;
		try {
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("twitter://user?user_id=289100088"));

		} catch (Exception e) {
			intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://twitter.com/#!/[tumascotik]"));
		}
		return intent;
	}

	public void checkingInternetConnections() {

		if (NetworkUtil.ConnectedToInternet(this)) {
			ParseProvider.initializeParse(this);
			// loadPendingObjects();
			pedirc.setEnabled(true);
			twitter.setEnabled(true);
			facebook.setEnabled(true);
			youtube.setEnabled(true);
			logorefresh.setEnabled(true);
			pedirpres.setEnabled(true);
			text_password.setEnabled(true);
			text_username.setEnabled(true);
			button_login.setEnabled(true);

		}

		else {
			pedirc.setEnabled(false);
			twitter.setEnabled(false);
			facebook.setEnabled(false);
			youtube.setEnabled(false);
			logorefresh.setEnabled(false);
			pedirpres.setEnabled(false);
			text_password.setEnabled(false);
			text_username.setEnabled(false);
			button_login.setEnabled(false);
			DialogFragment newFragment = new ConnectionDialog();
			newFragment.show(getFragmentManager(), "connections");

		}

	}

	@Override
	public void OnLoginResponse() {
	mainController.Initialize(this);
		if (mainController.isActive()) {
			setMainMenu();
		} else {
			text_username.setText("Error haciendo login");
		}
		
	}

	@Override
	public void onSpecieQueryFinished(ArrayList<String> species) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBreedQueryFinished(ArrayList<String> breed) {		
	}

//	@Override
//	public void onPropertiesQueryFinished(ArrayList<String> properties) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void onPetQueryFinished(Pet pet) {
		if (pet != null) {
	PetProvider.updatePet(this, pet);
}		
	}

	@Override
	public void OnPetUpdateFinished(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetAllPets(ArrayList<Pet> pets) {
if (pets != null) {
	for (Pet pet : pets) {
		Log.d(LOG_TAG, pet.getName());
		
		if(PetProvider.readPet(this, pet.getSystem_id())== null){
			PetProvider.insertPet(this, pet);
		}else
			PetProvider.updatePet(this, pet);
	}
}		
	}

	@Override
	public void onPetInserted(String objectId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUSerUpdateFinish() {
		// TODO Auto-generated method stub
		
	}
}
