package com.arawaney.tumascotik.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.arawaney.tumascotik.client.activity.BudgetActivity;
import com.arawaney.tumascotik.client.activity.PetPicker;
import com.arawaney.tumascotik.client.activity.SetRequestDetails;
import com.arawaney.tumascotik.client.activity.UserInfoActivity;
import com.arawaney.tumascotik.client.activity.ViewRequests;
import com.arawaney.tumascotik.client.backend.ParseBudgetProvider;
import com.arawaney.tumascotik.client.backend.ParsePetProvider;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.control.BackEndDataUpdater;
import com.arawaney.tumascotik.client.control.CalendarController;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.BreedProvider;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.PetPropertieProvider;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
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
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.Specie;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.sidebarmenu.ActivityBase;
import com.arawaney.tumascotik.client.sidebarmenu.SlidingMenuBuilderConcrete;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.util.NetworkUtil;

//import com.arawaney.tumascotik.client.R

public class MainActivity extends ActivityBase implements
		ParsePetListener, ParseUserListener, ParseRequestListener,
		ParseServiceListener, ParseBudgetListener {
	Date fechainicio;
	ProgressDialog progressDialog;
	SharedPreferences savedpendingappoint;
	int pendingApointments;
	ActionBar actionBar;

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
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		bussy = false;
		calledfromlogobuton = false;

		loadViews();
		loadButtons();

		if (MainController.Initialize(this)) {

			setMainMenu();
			if (checkingInternetConnections()) {
				loadActionBar();
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

	private void loadActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		View view = getLayoutInflater().inflate(R.layout.view_actionbar_main, null);
		ImageView menu = (ImageView)view.findViewById(R.id.imageView_actionbar_menu);     
		ImageView syncRequest = (ImageView)view.findViewById(R.id.imageView_actionbar_sync);   
		syncRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateRequests();
				updateBudgets();
			}
		});
		menu.setOnClickListener(new OnClickListener()
		{
		        @Override
		        public void onClick(View v)
		        {
		        	toggleMenu();
		        }
		});
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		actionBar.setCustomView(view, params);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

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

	private void openMyProfile(int viewMode) {
		UserInfoActivity.viewMode = viewMode;
		Intent i = new Intent(MainActivity.this, UserInfoActivity.class);
		startActivity(i);

	}

	private void openPetPicker(int functionMode) {
		PetPicker.functionMode = functionMode;
		Intent i = new Intent(MainActivity.this, PetPicker.class);
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

				Intent vercitasIntent = new Intent(MainActivity.this,
						ViewRequests.class);

				startActivity(vercitasIntent);
			}
		});
		makeBudget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						BudgetActivity.class);
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

				startActivity(getOpenFacebookIntent(MainActivity.this));
			}
		});

		twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(getOpenTwitterIntent(MainActivity.this));
			}
		});
		logorefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (pendingApointments != 0) {
					progressDialog = ProgressDialog.show(
							MainActivity.this, "Tumascotik",
							"Actualizando citas...");
					calledfromlogobuton = true;
					// loadPendingObjects();
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
						MainActivity.this, MainActivity.this);

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

	@Override
	protected void onResume() {
		super.onResume();
		if (mainController.isActive()) {
			checkingInternetConnections();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void notifyUser(boolean vetAccepted, Request request) {
		String toasttext;
		String notificationtitle;
		String notificationcontent;
		Class tapclass;

		String petName = request.getPet().getName();

		if (vetAccepted) {
			toasttext = getResources().getString(
					R.string.request_notification_toast_accepted)
					+ " " + petName;
			notificationtitle = getResources().getString(
					R.string.request_notification_accepted_title)
					+ " " + petName;
			notificationcontent = getResources().getString(
					R.string.request_notification_accepted_content);
			tapclass = ViewRequests.class;

		} else {
			toasttext = getResources().getString(
					R.string.request_notification_toast_rejected)
					+ " " + petName;
			notificationtitle = getResources().getString(
					R.string.request_notification_rejected_title)
					+ " " + petName;
			notificationcontent = getResources().getString(
					R.string.request_notification_rejected_content);
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
//		Toast toast = Toast.makeText(MainActivity.this, toasttext,
//				Toast.LENGTH_LONG);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
		// Generating Status Bar notification
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(notificationtitle)
				.setContentText(notificationcontent)
				.setSound(
						Uri.parse("android.resource://" + this.getPackageName()
								+ "/" + R.raw.bark));
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
			loadActionBar();
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

		progressDialog = ProgressDialog.show(MainActivity.this,
				"Tumascotik",
				getResources()
						.getString(R.string.main_load_backend_data_dialog));

		loadSpecies();
		loadBreeds();
		loadPetProperties();
		loadMotives();

	}

	private void loadMotives() {
		ParseProvider.getAllMotives(this);
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
	public Class<?> setSlidingMenu() {
		// Each activity can have it's own sliding menu controlling builder
		// class.
		return SlidingMenuBuilderConcrete.class;
	}

	@Override
	public boolean enableHomeIconActionSlidingMenu() {
		return true;
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
	public void onPetInserted(String objectId, boolean b) {
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

					Request savedRequest = RequestProvider.readRequest(this,
							request.getSystem_id());
					if (savedRequest == null) {
						RequestProvider.insertRequest(this, request);
					} else {
						if (requestAccepted(request, savedRequest)) {
							notifyUser(true, savedRequest);
							Date startDate = savedRequest.getStart_date()
									.getTime();
							Date finishDate = savedRequest.getFinish_date()
									.getTime();

							CalendarController.writeToCalendar(savedRequest
									.getPet().getName(), startDate, finishDate,
									this);

						} else if (requestCanceled(request, savedRequest)) {
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
		return savedRequest.getStatus() != Request.STATUS_CANCELED
				&& request.getStatus() == Request.STATUS_CANCELED;
	}

	private boolean requestAccepted(Request request, Request savedRequest) {
		return savedRequest.getStatus() == Request.STATUS_PENDING
				&& request.getStatus() == Request.STATUS_ACCEPTED;
	}

	@Override
	public void onRequestQueryFInished(Request request) {
		if (request != null) {

			Request savedRequest = RequestProvider.readRequest(this,
					request.getSystem_id());

			if (requestAccepted(request, savedRequest)) {
				notifyUser(true, savedRequest);

				Date startDate = savedRequest.getStart_date().getTime();
				Date finishDate = savedRequest.getFinish_date().getTime();

				CalendarController.writeToCalendar(savedRequest.getPet()
						.getName(), startDate, finishDate, this);

			} else if (requestCanceled(request, savedRequest)) {
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
	public void onAllSpeciesQueryFinished(boolean querySucces,
			ArrayList<Specie> species) {
		if (querySucces) {
			for (Specie specie : species) {
				SpecieProvider.insertSpecie(this, specie);
			}
		}
		speciesDone = true;

		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
			setUpBackendUpdateServiceAlarm();
		}
	}

	@Override
	public void onAllBreedsQueryFinished(boolean querySucces,
			ArrayList<Breed> breeds) {
		if (querySucces) {
			for (Breed breed : breeds) {
				BreedProvider.insertBreed(this, breed);
			}
		}
		breedsDone = true;

		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
			setUpBackendUpdateServiceAlarm();
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
			setUpBackendUpdateServiceAlarm();
		}

	}

	@Override
	public void onMotivesQueryFinished(ArrayList<String> motives) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAllMotivesQueryFinished(boolean querySucces,
			ArrayList<Service> motives) {
		if (querySucces) {
			for (Service motive : motives) {
				ServiceProvider.insertMotive(this, motive);
			}
		}
		motivesDone = true;

		if (breedsDone && speciesDone && motivesDone && petPropertiesDone) {
			progressDialog.dismiss();
			setUpBackendUpdateServiceAlarm();
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

	@Override
	public void onUpdateMotivesFinished(boolean b, ArrayList<Service> motives) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateBreedsQueryFinished(boolean b, ArrayList<Breed> species) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdatePetPropertiesFinished(boolean b,
			ArrayList<PetPropertie> petProperties) {

	}

	@Override
	public void onUpdateSpeciesQueryFinished(boolean b,
			ArrayList<Specie> species) {

	}

	void setUpBackendUpdateServiceAlarm() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);

		Intent intent = new Intent(this, BackEndDataUpdater.class);

		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		// Set alarm for 1 week
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000
				* 60 * 60 * 24 * 7, pintent);
		
		startService(new Intent(getBaseContext(), BackEndDataUpdater.class));

	}

	@Override
	public void onPetRemoveFinished(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
