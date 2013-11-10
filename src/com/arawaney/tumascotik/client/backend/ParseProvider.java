package com.arawaney.tumascotik.client.backend;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.arawaney.tumascotik.client.activity.SetDate;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.listener.ParseListener;
import com.arawaney.tumascotik.client.model.User;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ParseProvider {
	private static final String LOG_TAG = "Tumascotik-Client-ParseProvider";

	public static void logIn(String username, final String password,
			final Context context, final ParseListener listener) {
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
		dbuser.setName(user.get("Name").toString());
		dbuser.setLastname(user.get("LastName").toString());
		dbuser.setCedula(user.getInt("Cedula"));
		dbuser.setAddress(user.get("Address").toString());
		dbuser.setEmail(user.getEmail());
		dbuser.setGender(user.get("Gender").toString());
		dbuser.setMobile_telephone(user.getInt("Telephone_mobile"));
		dbuser.setHouse_telephone(user.getInt("Telephone_House"));
		dbuser.setAdmin(user.getBoolean("Admin") ? 1 : 0);

		UserProvider.insertUser(context, dbuser);

	}

}
