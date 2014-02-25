package com.arawaney.tumascotik.client.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.datatype.Duration;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.BreedProvider;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.listener.ParseServiceListener;
import com.arawaney.tumascotik.client.listener.ParseUserListener;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Specie;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseProvider {
	private static final String LOG_TAG = "Tumascotik-Client-ParseProvider";

	private static final String EMAIL_TAG = "email";
	private static final String EMAILVERIFIED_TAG = "emailVerified";
	private static final String IDENTIFICATION_TAG = "identification";
	private static final String LAST_NAME_TAG = "lastName";
	private static final String ADMIN_TAG = "admin";
	private static final String GENDER_TAG = "gender";
	private static final String REFERENCE_ADDRESS_TAG = "referenceAddress";
	private static final String ADDRESS_TAG = "address";
	private static final String TELEPHONE_HOME_TAG = "telephoneHome";
	private static final String TELEPHONE_MOBILE_TAG = "telephoneMobile";
	private static final String NAME_TAG = "name";
	private static final String USERNAME_TAG = "userame";
	private static final String PASSWORD_TAG = "password";
	private static final String IS_REQUEST_TAG = "isRequest";
	private static final String DURATION_TAG = "duration";
	private static final String UPDATED_AT_TAG = "updatedAt";
	
	private static final String USER_TABLE = "_User";
	private static final String SERVICE_TABLE = "Service";
	
	private static final String SEX_MALE = "M";
	private static final String SEX_FEMALE = "F";

	public static void logIn(String username, final String password,
			final Context context, final ParseUserListener listener) {
		final ProgressDialog progressDialog = ProgressDialog.show(context, "",
				"Realizando Login...");
		ParseUser.logInInBackground(username, password, new LogInCallback() {

			public void done(ParseUser user, ParseException e) {

				if (user != null) {
					ParseProvider.setUser(context, user, password);

				} else {
					Log.e(LOG_TAG, "Error by login :" + e.getMessage());
				}
				progressDialog.dismiss();
				listener.OnLoginResponse();
			}

		});
	}

	public static void initializeParse(Context context) {
		Parse.initialize(context, "mLQoFv0KQAi0WYL1JGSSKGRjQ77DgZST0qX47TJe",
				"X0GMTJKHoSDMjA7N9leHfaYNfptL4e8fPhsGMDVN");
	}

	protected static void setUser(Context context, ParseUser user,
			String password) {
		User dbuser = new User();

		dbuser.setSystemId(user.getObjectId().toString());
		dbuser.setUsername(user.getUsername());
		dbuser.setPassword(password);
		dbuser.setName(user.get(NAME_TAG).toString());
		dbuser.setLastname(user.get(LAST_NAME_TAG).toString());
		dbuser.setCedula(user.getInt(IDENTIFICATION_TAG));
		dbuser.setAddress(user.get(ADDRESS_TAG).toString());
		dbuser.setEmail(user.getEmail());
		
		String gender = user.getString(GENDER_TAG);
		
		if (gender.equals(SEX_MALE)) {
			dbuser.setGender(User.GENDER_MAN);
		}else if (gender.equals(SEX_FEMALE)) {
			dbuser.setGender(User.GENDER_WOMAN);
		}
		
		dbuser.setMobile_telephone(user.getLong(TELEPHONE_MOBILE_TAG));
		dbuser.setHouse_telephone(user.getLong(TELEPHONE_HOME_TAG));
		dbuser.setAdmin(user.getBoolean(ADMIN_TAG) ? 1 : 0);
		Calendar updated_at = Calendar.getInstance();
		dbuser.setUpdated_at(updated_at);

		UserProvider.insertUser(context, dbuser);

	}

	public static void getUser(final ParseUserListener parseListener,
			final User user) {

		String systemId = user.getSystemId();
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(USER_TABLE);
		query.whereEqualTo("objectId", systemId);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException e) {
				if (e == null) {
					ParseObject parseUSer = arg0.get(0);

					User updatedUser = user;

					updatedUser.setName(parseUSer.get(NAME_TAG).toString());
					updatedUser.setLastname(parseUSer.get(LAST_NAME_TAG)
							.toString());
					updatedUser.setCedula(parseUSer.getInt(IDENTIFICATION_TAG));
					updatedUser.setAddress(parseUSer.get(ADDRESS_TAG)
							.toString());
					updatedUser.setEmail(parseUSer.getString(EMAIL_TAG));
					
					String gender = parseUSer.getString(GENDER_TAG);
					
					if (gender.equals(SEX_MALE)) {
						updatedUser.setGender(User.GENDER_MAN);
					}else if (gender.equals(SEX_FEMALE)) {
						updatedUser.setGender(User.GENDER_WOMAN);
					}
					
					updatedUser.setMobile_telephone(parseUSer
							.getLong(TELEPHONE_MOBILE_TAG));
					updatedUser.setHouse_telephone(parseUSer
							.getLong(TELEPHONE_HOME_TAG));
					updatedUser.setAdmin(parseUSer.getBoolean(ADMIN_TAG) ? 1
							: 0);

					parseListener.onUserQueryFinish(updatedUser, true);
				} else {
					Log.e(LOG_TAG, "No user " + e.getMessage());
					parseListener.onUserQueryFinish(null, false);

				}

			}

		});
	}

	public static void updateUser(final ParseUserListener listener,
			final User user) {

		ParseUser parsedUser = ParseUser.getCurrentUser();

		if (parsedUser != null) {

			parsedUser.put(NAME_TAG, user.getName());
			parsedUser.put(LAST_NAME_TAG, user.getLastname());
			parsedUser.put(EMAIL_TAG, user.getEmail());
			
			int gender = user.getGender();
			if (gender == User.GENDER_MAN) {
				parsedUser.put(GENDER_TAG, SEX_MALE);
			}else if (gender == User.GENDER_MAN) {
				parsedUser.put(GENDER_TAG, SEX_FEMALE);
			}
			
			parsedUser.put(GENDER_TAG, user.getGender());
			parsedUser.put(TELEPHONE_MOBILE_TAG, user.getMobile_telephone());
			parsedUser.put(TELEPHONE_HOME_TAG, user.getHouse_telephone());
			parsedUser.put(ADDRESS_TAG, user.getAddress());

			parsedUser.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e != null) {
						listener.onUserUpdateFinish(user, false);
					} else {
						listener.onUserUpdateFinish(user, true);
					}

				}
			});

		} else {
			Log.d(LOG_TAG, "currentuser null");
			listener.onUserUpdateFinish(user, false);
		}

	}

	public static void getCurrentUser(Context context) {

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			ParseUser.logInInBackground(MainController.USER.getUsername(),
					MainController.USER.getPassword(), new LogInCallback() {

						public void done(ParseUser user, ParseException e) {

							if (user != null) {

							} else {
								Log.e(LOG_TAG,
										"Error by getcurrentuser :"
												+ e.getMessage());
							}
						}

					});
		}

	}

	public static void getAllMotives(final ParseServiceListener listener) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(SERVICE_TABLE);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				final ArrayList<Service> motives;
				if (e == null) {
					motives = new ArrayList<Service>();
					for (ParseObject object : cList) {
						Service motive = new Service();
						motive.setSystem_id(object.getObjectId());
						motive.setName(object.getString(NAME_TAG));
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						motive.setUpdated_at(Updated_At);
						boolean needsRequest = object
								.getBoolean(IS_REQUEST_TAG);
						if (needsRequest) {
							motive.setNeedsRequest(Service.NEED_REQUEST);
						} else
							motive.setNeedsRequest(Service.NOT_NEED_REQUEST);
						
						if (object.getInt(DURATION_TAG) != 0) {
							motive.setDuration(object.getInt(DURATION_TAG));
						}

						motives.add(motive);
					}
					listener.onAllMotivesQueryFinished(true, motives);

				} else {
					motives = null;
					Log.d(LOG_TAG, " Query Error: " + e.getMessage());
					listener.onAllMotivesQueryFinished(false, motives);
				}

			}

		});

	}

	public static void updateAllMotives(Context context, final ParseServiceListener listener) {

		Date lastUpdate = ServiceProvider.getLastUpdate(context);
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(SERVICE_TABLE);
		if (lastUpdate != null) {
			Log.d(LOG_TAG, CalendarUtil.getDateFormated(lastUpdate, "hh:mm dd MM yyyy"));
			query.whereGreaterThan(UPDATED_AT_TAG, lastUpdate);
		}
		else{
			Log.d(LOG_TAG, "nuuuuull");
		}

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {

				if (e == null) {
					ArrayList<Service> motives = new ArrayList<Service>();
					for (ParseObject object : cList) {
						Service motive = new Service();
						motive.setSystem_id(object.getObjectId());
						motive.setName(object.getString(NAME_TAG));
						Calendar Updated_At = Calendar.getInstance();
						Updated_At.setTimeInMillis(object.getUpdatedAt()
								.getTime());
						motive.setUpdated_at(Updated_At);
						boolean needsRequest = object
								.getBoolean(IS_REQUEST_TAG);
						if (needsRequest) {
							motive.setNeedsRequest(Service.NEED_REQUEST);
						} else
							motive.setNeedsRequest(Service.NOT_NEED_REQUEST);
						
						if (object.getInt(DURATION_TAG) != 0) {
							motive.setDuration(object.getInt(DURATION_TAG));
						}
						Log.d(LOG_TAG,"HOOOLA"+" "+ motive.getName()+" "+motive.getSystem_id()+" "+CalendarUtil.getDateFormated(motive.getUpdated_at(), "hh:mm dd MM yyyy"));
						motives.add(motive);
					}

					listener.onUpdateMotivesFinished(true, motives);

				} else {
					listener.onUpdateMotivesFinished(false, null);
					Log.d(LOG_TAG,
							" Query error updating PetProperties: "
									+ e.getMessage());
				}

			}

		});


	}

}
