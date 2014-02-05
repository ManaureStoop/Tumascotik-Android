package com.arawaney.tumascotik.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.arawaney.tumascotik.client.activity.SetRequestDetails;
import com.arawaney.tumascotik.client.activity.PetInfoActivity;
import com.arawaney.tumascotik.client.activity.PetPicker;
import com.arawaney.tumascotik.client.activity.BudgetActivity;
import com.arawaney.tumascotik.client.activity.SetDate;
import com.arawaney.tumascotik.client.activity.UserInfoActivity;
import com.arawaney.tumascotik.client.activity.ViewRequests;
import com.arawaney.tumascotik.client.backend.ParseBudgetProvider;
import com.arawaney.tumascotik.client.backend.ParsePetProvider;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.CitationDB;
import com.arawaney.tumascotik.client.db.provider.BreedProvider;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.db.provider.PetPropertieProvider;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.db.provider.SpecieProvider;
import com.arawaney.tumascotik.client.dialog.ConnectionDialog;
import com.arawaney.tumascotik.client.listener.ParseBudgetListener;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.listener.ParseServiceListener;
import com.arawaney.tumascotik.client.listener.ParseUserListener;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Specie;
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
		ParsePetListener, ParseUserListener, ParseRequestListener,
		ParseServiceListener, ParseBudgetListener {
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

	// Parse Data Load variables
	boolean speciesDone;
	boolean breedsDone;
	boolean petPropertiesDone;
	boolean motivesDone;

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
				updateBudgets();
			}

		} else {
			setLogIn();
			checkingInternetConnections();

		}

		// reset_DataBase_SharedP();

	}

	private void updateBudgets() {
	ParseBudgetProvider.updateBudgets(this, this);
		
	}

	private void updateRequests() {
		ParseRequestProvider.updateRequests(this, this);

	}

	private void updatePets() {
		ParsePetProvider.updatePets(this, MainController.USER, this);

	}

	private void setLogIn() {
		login_layout.setVisibility(View.VISIBLE);
		main_buttons_layout.setVisibility(View.GONE);

	}

	private void setMainMenu() {
		login_layout.setVisibility(View.GONE);
		main_buttons_layout.setVisibility(View.VISIBLE);

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
				Intent i = new Intent(ClientMainActivity.this, BudgetActivity.class);
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
//					loadPendingObjects();
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

		text_password.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_THIN));
		text_username.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_THIN));
		button_login.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		makeRequest.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		viewRequest.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		makeBudget.setTypeface(FontUtil
				.getTypeface(this, FontUtil.ROBOTO_LIGHT));

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

//	public void refresh(int ind) {
//		// Does the Parse Query to see if accepted or rejected. 1 means accepted
//		// and goes
//		// Automatically to 4. A 2 means rejected and it gets erased
//
//		final int in = ind;
//		ParseQuery query = new ParseQuery("Citas");
//		query.whereEqualTo("fechaInicial", fechainicio);
//		query.findInBackground(new FindCallback<ParseObject>() {
//
//			@Override
//			public void done(List<ParseObject> cList, ParseException e) {
//				ParseObject object;
//				if (e == null) {
//					if (cList.size() != 0) {
//						object = cList.get(0);
//						if (object.getNumber("aceptado") == (Number) 1) {
//
//							notifyUser(true, object.getString("mascota"));
//
//							writeCalendar(object.getString("mascota"),
//									object.getDate("fechaInicial"),
//									object.getDate("fechaFinal"));
//
//							// Passes "Cita" from pending to accepted in
//							// database
//							UpdateDataBase(true, object.getDate("fechaInicial"));
//							// Puts "Cita" in Parse from 1 to 4
//							object.put("aceptado", 4);
//							object.saveInBackground();
//
//						} else if (object.getNumber("aceptado") == (Number) 2) {
//
//							notifyUser(false, object.getString("mascota"));
//
//							// Erase "Cita" from database
//							UpdateDataBase(false,
//									object.getDate("fechaInicial"));
//
//							// Delete "Cita" from calender in case it was
//							// already accepted
//							if (object.getNumber("nuevo") != (Number) 0) {
//								deleteCalendar(
//										fechainicio.getTime(),
//										object.getDate("fechaFinal").getTime(),
//										"Cita con Tumascotik para "
//												+ object.getString("mascota"));
//							}
//							// Erase "Cita" from Parse
//							object.deleteInBackground();
//
//							// // Erase "Cita" from Shared Preferences
//							resetSavedPendings(in);
//						} else if (object.getNumber("aceptado") == (Number) 0
//								|| object.getNumber("aceptado") == (Number) 4)
//							Log.d("Sigue igual", "Sigue igual");
//					} else
//						Log.d("ERROR", "No hay actividad");
//
//				} else {
//					Log.d("ERROR", "Error en refresh!");
//				}
//
//				if (calledfromlogobuton) {
//					progressDialog.dismiss();
//					calledfromlogobuton = false;
//
//				}
//			}
//		});
//
//	}

//	public void loadPendingObjects() {
//		// Loads every object pending to see if accepted or rejected
//		int index;
//		pendingApointments = savedpendingappoint.getInt("index", 0);
//		for (index = 0; index < pendingApointments; index++) {
//			fechainicio = new GregorianCalendar(savedpendingappoint.getInt(
//					"a�o" + index, 0), savedpendingappoint.getInt(
//					"mes" + index, 0), savedpendingappoint.getInt(
//					"dia" + index, 0), savedpendingappoint.getInt("horai"
//					+ index, 0), savedpendingappoint.getInt("minutoi" + index,
//					0)).getTime();
//			Log.d("fechaINICIO", fechainicio.toString());
//			refresh(index);
//		}
//
//	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	
	public void notifyUser(boolean vetAccepted, Request request) {
		String toasttext;
		String notificationtitle;
		String notificationcontent;
		Class tapclass;
		
		String petName = request.getPet().getName();
		
		if (vetAccepted) {
			toasttext = getResources().getString(R.string.request_notification_toast_accepted)+" "+petName;
			notificationtitle = getResources().getString(R.string.request_notification_accepted_title)+" "+petName;
			notificationcontent = getResources().getString(R.string.request_notification_accepted_content);
			tapclass = ViewRequests.class;

		} else {
			toasttext = getResources().getString(R.string.request_notification_toast_rejected)+" "+petName;
			notificationtitle = getResources().getString(R.string.request_notification_rejected_title)+" "+petName;
			notificationcontent = getResources().getString(R.string.request_notification_rejected_content);
			PetPicker.functionMode = PetPicker.MODE_MAKE_APPOINTMENT;
			tapclass = PetPicker.class;

		}

		showToastStatusBarNotif(toasttext, notificationtitle,
				notificationcontent, tapclass);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showToastStatusBarNotif(String toasttext,
			String notificationtitle, String notificationcontent, Class tapclass) {
		// Generating Toast notification
		Toast toast = Toast.makeText(ClientMainActivity.this, toasttext, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		// Generating Status Bar notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(notificationtitle)
				.setContentText(notificationcontent)
				.setSound(
						Uri.parse("android.resource://"+this.getPackageName()+"/"
								+ R.raw.bark));
		// Add event to
		Intent resultIntent = new Intent(this, tapclass);
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
			loadBackEndData();
			updatePets();
		} else {
			text_username.setText("Error haciendo login");
		}

	}

	private void loadBackEndData() {
		speciesDone = false;
		breedsDone = false;
		petPropertiesDone = false;
		motivesDone = false;
		
		progressDialog = ProgressDialog.show(
				ClientMainActivity.this, "Tumascotik",
				getResources().getString(R.string.main_load_backend_data_dialog));
		
		loadSpecies();
		loadBreeds();
		loadPetProperties();
		loadMotives();

	}

	private void loadMotives() {
		ParseProvider.getAllMotives(this, this);
	}

	private void loadPetProperties() {
		ParsePetProvider.getAllPetProperties(this);

	}

	private void loadBreeds() {
		ParsePetProvider.getAllBreeds(this);

	}

	private void loadSpecies() {
		ParsePetProvider.getAllSpecies(this);

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
		if (requests != null) {
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
						if (requestAccepted(request, savedRequest)) {
							notifyUser(true, savedRequest);
							
						}else if (requestCanceled(request, savedRequest)){
							notifyUser(false, savedRequest);
							savedRequest.setStatus(Request.STATUS_CANCELED);
							RequestProvider.updateRequest(this, savedRequest);
						}
					
						request.setId(savedRequest.getId());
						RequestProvider.updateRequest(this, request);
					}

				}

			}

		}
	}

	private boolean requestCanceled(Request request, Request savedRequest) {
		return savedRequest.getStatus()!= Request.STATUS_CANCELED && request.getStatus() == Request.STATUS_CANCELED;
	}

	private boolean requestAccepted(Request request, Request savedRequest) {
		return savedRequest.getStatus()== Request.STATUS_PENDING && request.getStatus() == Request.STATUS_ACCEPTED;
	}

	@Override
	public void onRequestQueryFInished(Request request) {
		if (request != null) {
			Log.d(LOG_TAG, "2Request Status " + request.getStatus());
			Log.d(LOG_TAG, "2Request 2Service : " + request.getService().getSystem_id());
			Log.d(LOG_TAG, "1Request COmment: " + request.getComment());
			Log.d(LOG_TAG, "1Request isDelivery : " + request.isDelivery());
			Log.d(LOG_TAG,
					"1Request StartDAte : "
							+ CalendarUtil.getDateFormated(
									request.getStart_date(), "dd/MM/yyyy hh:mm"));

			Request savedRequest = RequestProvider.readRequest(this,
					request.getSystem_id());
			
			if (requestAccepted(request, savedRequest)) {
				notifyUser(true, savedRequest);
				
			}else if (requestCanceled(request, savedRequest)){
				notifyUser(false, savedRequest);
				savedRequest.setStatus(Request.STATUS_CANCELED);
				RequestProvider.updateRequest(this, savedRequest);
			}
			
			request.setId(savedRequest.getId());
			RequestProvider.updateRequest(this, request);
		}

	}

	@Override
	public void onCanceledQueryFinished(boolean canceled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllSpeciesQueryFinished(boolean querySucces, ArrayList<Specie> species) {
		if (querySucces) {
			for (Specie specie : species) {
				SpecieProvider.insertSpecie(this, specie);
			}
		}
		speciesDone = true;

		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onAllBreedsQueryFinished(boolean querySucces, ArrayList<Breed> breeds) {
		if (querySucces) {
			for (Breed breed : breeds) {
				BreedProvider.insertBreed(this, breed);
			}
		}
		breedsDone = true;
		
		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
		}

	}

	@Override
	public void onAllPetPropertiesFinished(boolean querySucces,
			ArrayList<PetPropertie> petProperties) {
		if (querySucces) {
			for (PetPropertie petPropertie : petProperties) {
				PetPropertieProvider.insertPetPropertie(this, petPropertie);
			}
		}
		
		petPropertiesDone = true;
		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
		}

	}

	@Override
	public void onMotivesQueryFinished(ArrayList<String> motives) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllMotivesQueryFinished(boolean querySucces, ArrayList<Service> motives) {
		if (querySucces) {
			for (Service motive : motives) {
				ServiceProvider.insertMotive(this, motive);
			}
		}
		motivesDone = true;
		
		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
		}


	}

	@Override
	public void onRequestRemoveFinished(Request request) {
		RequestProvider.removeRequest(this, request.getSystem_id());
	}

	@Override
	public void onDayRequestsQueryFinished(Date[] initialScheduledDates,
			Date[] finalScheduledDates) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnBudgetInserted(boolean b, String objectId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAllBudgetsQueryFinished(ArrayList<Budget> budgets) {
		if (budgets != null) {
			if (!budgets.isEmpty()) {
				for (Budget budget : budgets) {

					Log.d(LOG_TAG,
							"1Request isDelivery : " + budget.isDelivery());
					
					Log.d(LOG_TAG, "1Request : " + budget.getSystem_id());
					
					Budget savedBudget = BudgetProvider.readBudget(this,
							budget.getSystem_id());
						
					if (savedBudget == null) {
						BudgetProvider.insertBudget(this, budget);
					} else {
						budget.setId(savedBudget.getId());
						BudgetProvider.updateBudget(this, budget);	
					}

				}

			}

		}
	}

	@Override
	public void onBudgetQueryFInished(Budget budget) {
		if (budget != null) {
			Log.d(LOG_TAG, "2Request Status " + budget.getStatus());
			Log.d(LOG_TAG, "1Request isDelivery : " + budget.isDelivery());

			Budget savedBudget = BudgetProvider.readBudget(this,
					budget.getSystem_id());
			
			budget.setId(savedBudget.getId());
			BudgetProvider.updateBudget(this, budget);
		}

	}

	@Override
	public void onBudgetRemoveFinished(Budget budget) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOnePriceQueryFinished(BudgetService budgetService) {
		// TODO Auto-generated method stub
		
	}

}
