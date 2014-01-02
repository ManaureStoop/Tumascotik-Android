package com.arawaney.tumascotik.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.arawaney.tumascotik.client.activity.SetRequestDetails;
import com.arawaney.tumascotik.client.activity.PetInfoActivity;
import com.arawaney.tumascotik.client.activity.PetPicker;
import com.arawaney.tumascotik.client.activity.Budget;
import com.arawaney.tumascotik.client.activity.SetDate;
import com.arawaney.tumascotik.client.activity.UserInfoActivity;
import com.arawaney.tumascotik.client.activity.ViewRequests;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.CitationDB;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.dialog.ConnectionDialog;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.listener.ParseUserListener;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.util.NetworkUtil;
//import com.arawaney.tumascotik.client.R
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

public class ClientMainActivity extends FragmentActivity implements
		ParsePetListener, ParseUserListener, ParseRequestListener {
	Date fechainicio;
	ProgressDialog progressDialog;
	SharedPreferences savedpendingappoint;
	int pendingApointments;

	private final String LOG_TAG = "Tumascotik-Client-Main Menu";

	// To know if the function "reset request" is already running. If so it can
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
	TextView button_login;
	// Main menu views
	TextView makeRequest;
	TextView viewRequest;
	TextView makeBudget;
	// Header view
	Button logorefresh;
	// Footer views
	ImageView youtube;
	ImageView facebook;
    ImageView twitter;
    ImageView emergencia;

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
			if (checkingInternetConnections()) {
				ParseProvider.getCurrentUser(this);
				updatePets();
				updateRequests();
			}

		} else {
			setLogIn();
			checkingInternetConnections();

		}

		// reset_DataBase_SharedP();

	}

	private void updateRequests() {
		ParseProvider.getRequests(this, this);
		
	}

	private void updatePets() {
		ParseProvider.getPets(this, MainController.USER);

	}

	private void setLogIn() {
		login_layout.setVisibility(View.VISIBLE);
		main_buttons_layout.setVisibility(View.GONE);

	}

	private void setMainMenu() {
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
			openPetPicker(PetPicker.MODE_EDIT_PET);
			return true;
		case R.id.menu_profile:
			openMyProfile(UserInfoActivity.MODE_INFO_LIST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openMyProfile(int viewMode) {
		UserInfoActivity.viewMode = viewMode;
		Intent i = new Intent(ClientMainActivity.this, UserInfoActivity.class);
		startActivity(i);

	}

	private void openPetPicker(int functionMode) {
		PetPicker.functionMode = functionMode;
		Intent i = new Intent(ClientMainActivity.this, PetPicker.class);
		startActivity(i);

	}

	private void loadButtons() {
		makeRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (userDataIsComplete()) {
					openPetPicker(PetPicker.MODE_MAKE_APPOINTMENT);
				} else {
					openMyProfile(UserInfoActivity.MODE_EDIT_LIST);
				}

			}

			private boolean userDataIsComplete() {
				if (MainController.USER.getAddress() != null
						&& MainController.USER.getMobile_telephone() != null) {
					return true;
				} else
					return false;
			}
		});

		viewRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent vercitasIntent = new Intent(ClientMainActivity.this,
						ViewRequests.class);
				
				startActivity(vercitasIntent);
			}
		});
		makeBudget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ClientMainActivity.this,
						Budget.class);
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
					progressDialog = ProgressDialog.show(
							ClientMainActivity.this, "Tumascotik",
							"Actualizando citas...");
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

				ParseProvider.logIn(username, password,
						ClientMainActivity.this, ClientMainActivity.this);

			}
		});
	}

	private void loadViews() {
		main_buttons_layout = (LinearLayout) findViewById(R.id.main_buttons_menu);
		login_layout = (LinearLayout) findViewById(R.id.log_in_main_menu);		
		
		text_password = (EditText) findViewById(R.id.text_login_passw);
		text_username = (EditText) findViewById(R.id.text_login_username);
		button_login = (TextView) findViewById(R.id.button_login);
		makeRequest = (TextView) findViewById(R.id.pdrcitamenu);
		viewRequest = (TextView) findViewById(R.id.bvercitasmenu);
		makeBudget = (TextView) findViewById(R.id.bpedirpresmenu);
		facebook = (ImageView) findViewById(R.id.bfacebookmenu);
		twitter = (ImageView) findViewById(R.id.btwittermenu);
		youtube = (ImageView) findViewById(R.id.byoutubemenu);
		logorefresh = (Button) findViewById(R.id.blogorefreshmenu);
		emergencia = (ImageView) findViewById(R.id.bemergenciamenu);
		
		setFonts();

	}

	private void setFonts() {
		
		text_password.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		text_username.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		button_login.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		makeRequest.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		viewRequest.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		makeBudget.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		
	}

	private void getNumberOfPendingAppointments(SharedPreferences settings) {
		// Get number off Pending Appointments
		pendingApointments = settings.getInt("index", 0);
		Log.d("INDICE EN SHARED = ", String.valueOf(pendingApointments));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_menu, (android.view.Menu) menu);
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
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
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
			tapclass = ViewRequests.class;

		} else {
			toasttext = "Su veterinario no puede atender a " + petsname;
			notificationtitle = "Cita para " + petsname + " rechazada";
			notificationcontent = "Disculpe, por favor elija una nueva cita";
			tapclass = SetRequestDetails.class;

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
		stackBuilder.addParentStack(SetRequestDetails.class);
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

	public boolean checkingInternetConnections() {

		if (NetworkUtil.ConnectedToInternet(this)) {
			ParseProvider.initializeParse(this);
			// loadPendingObjects();
			makeRequest.setEnabled(true);
			twitter.setEnabled(true);
			facebook.setEnabled(true);
			youtube.setEnabled(true);
			logorefresh.setEnabled(true);
			makeBudget.setEnabled(true);
			text_password.setEnabled(true);
			text_username.setEnabled(true);
			button_login.setEnabled(true);
			return true;

		}

		else {
			makeRequest.setEnabled(false);
			twitter.setEnabled(false);
			facebook.setEnabled(false);
			youtube.setEnabled(false);
			logorefresh.setEnabled(false);
			makeBudget.setEnabled(false);
			text_password.setEnabled(false);
			text_username.setEnabled(false);
			button_login.setEnabled(false);
			DialogFragment newFragment = new ConnectionDialog();
			newFragment.show(getFragmentManager(), "connections");
			return false;

		}

	}

	@Override
	public void OnLoginResponse() {
		mainController.Initialize(this);
		if (mainController.isActive()) {
			setMainMenu();
			updatePets();
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

	@Override
	public void onPetQueryFinished(Pet pet) {
		if (pet != null) {
			Pet savedPet = PetProvider.readPet(this, pet.getSystem_id());
			pet.setId(savedPet.getId());
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
				Pet savedPet = PetProvider.readPet(this, pet.getSystem_id());
				if (savedPet == null) {
					PetProvider.insertPet(this, pet);
				} else{
					pet.setId(savedPet.getId());
					PetProvider.updatePet(this, pet);
				}
					
			}
		}
	}

	@Override
	public void onPetInserted(String objectId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserUpdateFinish(User updatedUSer, boolean updated) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserQueryFinish(User updatedUSer, boolean updated) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnRequestInserted(boolean inserted, String systemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAllRequestsQueryFinished(ArrayList<Request> requests) {
		if (requests != null ) {
			if (!requests.isEmpty()) {
				for (Request request : requests) {

					Log.d(LOG_TAG, "1Request COmment: " + request.getComment());
					Log.d(LOG_TAG,
							"1Request isDelivery : " + request.isDelivery());
					Log.d(LOG_TAG,
							"1Request StartDAte : "
									+ CalendarUtil.getDateFormated(
											request.getStart_date(),
											"dd/MM/yyyy hh:mm"));
					
					Log.d(LOG_TAG, "1Request : " + request.getSystem_id());
					Request savedRequest = RequestProvider.readRequest(this,
							request.getSystem_id());
					if (savedRequest == null) {
						RequestProvider.insertRequest(this, request);
					} else {
						request.setId(savedRequest.getId());
						RequestProvider.updateRequest(this, request);
					}

				}
				
			}
			
		}
	}

	@Override
	public void onRequestQueryFInished(Request request) {
		if (request != null) {
			Log.d(LOG_TAG, "2Request Status "+request.getStatus());
			Log.d(LOG_TAG, "2Request 2Service : "+request.getService());
			Log.d(LOG_TAG, "1Request COmment: " + request.getComment());
			Log.d(LOG_TAG,
					"1Request isDelivery : " + request.isDelivery());
			Log.d(LOG_TAG,
					"1Request StartDAte : "
							+ CalendarUtil.getDateFormated(
									request.getStart_date(),
									"dd/MM/yyyy hh:mm"));
			
			Request savedRequest = RequestProvider.readRequest(this, request.getSystem_id());
			request.setId(savedRequest.getId());
			RequestProvider.updateRequest(this, request);
		}
		
	}

	@Override
	public void onCanceledQueryFinished(boolean canceled) {
		// TODO Auto-generated method stub
		
	}

}
