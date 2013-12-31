package com.arawaney.tumascotik.client.control;

import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.User;

import android.content.Context;
import android.util.Log;

public class MainController {
	private final static String LOG_TAG = "Tumascotik-Client-Main Controller";
	Context context;
	public static User USER;
	static Pet PET;
	static Request 	REQUEST;

	public static boolean Initialize(Context context) {
		Log.d(LOG_TAG, "Initialize");
		try {
			USER = UserProvider.readUser(context);

		} catch (Exception e) {

			Log.e(LOG_TAG, "Error reading user");
		}

		return isActive();

	}

	public static boolean isActive() {
		if (USER != null) {
			return true;
		}

		return false;
	}

	public static Pet getPET() {
		return PET;
	}

	public static void setPET(Pet pet) {
		PET = pet;
	}
	
	public static Request getREQUEST() {
		return REQUEST;
	}

	public static void setREQUEST(Request request) {
		REQUEST = request;
	}

}
