package com.arawaney.tumascotik.client.db.provider;

import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.db.UserEntity;
import com.arawaney.tumascotik.client.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class UserProvider {
	
	private static final String LOG_TAG = "Tumascotik-Client-UserProvider";

	public static final Uri URI_USER = Uri.parse("conten://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + UserEntity.TABLE);

	public static int insertUser(Context context, User user) {

		if (context == null || user == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(UserEntity.COLUMN_ID, user.getId());
			values.put(UserEntity.COLUMN_SYSTEM_ID, user.getSystemId());
			values.put(UserEntity.COLUMN_NAME, user.getName());
			values.put(UserEntity.COLUMN_LASTNAME, user.getLastname());
			values.put(UserEntity.COLUMN_USERNAME, user.getUsername());
			values.put(UserEntity.COLUMN_PASSWORD, user.getPassword());
			values.put(UserEntity.COLUMN_GENDER, user.getGender());
			values.put(UserEntity.COLUMN_ADDRESS, user.getAddress());
			values.put(UserEntity.COLUMN_EMAIL, user.getEmail());
			values.put(UserEntity.COLUMN_CEDULA, user.getCedula());
			values.put(UserEntity.COLUMN_TELEPHONE_HOUSE,
					user.getHouse_telephone());
			values.put(UserEntity.COLUMN_TELEPHONE_MOBILE,
					user.getMobile_telephone());
			values.put(UserEntity.COLUMN_ADMIN, user.getisAdmin());

			final Uri result = context.getContentResolver().insert(URI_USER,
					values);

			if (result != null) {
				int id = Integer.parseInt(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " User :" + user.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " User :" + user.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " User :" + user.getName() + " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateUser(Context context, User user) {

		if (context == null || user == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(UserEntity.COLUMN_ID, user.getId());
			values.put(UserEntity.COLUMN_SYSTEM_ID, user.getSystemId());
			values.put(UserEntity.COLUMN_NAME, user.getName());
			values.put(UserEntity.COLUMN_LASTNAME, user.getLastname());
			values.put(UserEntity.COLUMN_USERNAME, user.getUsername());
			values.put(UserEntity.COLUMN_PASSWORD, user.getPassword());
			values.put(UserEntity.COLUMN_GENDER, user.getGender());
			values.put(UserEntity.COLUMN_ADDRESS, user.getAddress());
			values.put(UserEntity.COLUMN_EMAIL, user.getEmail());
			values.put(UserEntity.COLUMN_CEDULA, user.getCedula());
			values.put(UserEntity.COLUMN_TELEPHONE_HOUSE,
					user.getHouse_telephone());
			values.put(UserEntity.COLUMN_TELEPHONE_MOBILE,
					user.getMobile_telephone());
			values.put(UserEntity.COLUMN_ADMIN, user.getisAdmin());

			String condition = UserEntity.COLUMN_ID + " = "
					+ String.valueOf(user.getId());

			int row = context.getContentResolver().update(URI_USER, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " User :" + user.getName() + " has bee updated");
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static User readUser(Context context) {

	
	if (context == null )
		return null;
	
	final Cursor cursor = context.getContentResolver().query(URI_USER, null, null,null, null);
	
	User user = null;
	
	if (cursor.getCount() == 0) {
		cursor.close();
		return null;
	}
	
	try {
	if (cursor.moveToFirst()) {
		
		do {
			final long id = cursor.getLong(cursor.getColumnIndex(UserEntity.COLUMN_ID));
			final long system_id = cursor.getLong(cursor.getColumnIndex(UserEntity.COLUMN_SYSTEM_ID));
			final String username = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_USERNAME));
			final String password = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_PASSWORD));
			final String name = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_NAME));
			final String lastname = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_LASTNAME));
			final Integer cedula = cursor.getInt(cursor.getColumnIndex(UserEntity.COLUMN_CEDULA));
			final String address = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_ADDRESS));
			final String email = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_EMAIL));
			final String gender = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_GENDER));
			final Integer mobile_telephone = cursor.getInt(cursor.getColumnIndex(UserEntity.COLUMN_TELEPHONE_MOBILE));
			final String house_telephone = cursor.getString(cursor.getColumnIndex(UserEntity.COLUMN_TELEPHONE_HOUSE));
			final Integer admin = cursor.getInt(cursor.getColumnIndex(UserEntity.COLUMN_ADMIN));
			
			user = new User();
			user.setId(id);
			user.setSystemId(system_id);
			user.setUsername(username);
			user.setPassword(password);
			user.setName(name);
			user.setLastname(lastname);
			user.setCedula(cedula);
			user.setAddress(address);
			user.setEmail(email);
			user.setGender(gender);
			user.setMobile_telephone(mobile_telephone);
			user.setHouse_telephone(house_telephone);
			user.setAdmin(admin);
			
		} while (cursor.moveToNext());
	}

	} catch (Exception e) {
		user = null;
		Log.e(LOG_TAG, "Error : "+ e.getMessage());
	}
	finally{
		cursor.close();
	}
	return user;
}

}
