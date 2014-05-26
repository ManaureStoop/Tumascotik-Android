package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.arawaney.tumascotik.client.db.PetEntity;
import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.db.UserEntity;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class UserProvider {

	private static final String LOG_TAG = "Tumascotik-Client-UserProvider";

	public static final Uri URI_USER = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + UserEntity.TABLE);

	public static int insertUser(Context context, User user) {

		if (context == null || user == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(UserEntity.COLUMN_SYSTEM_ID, user.getSystemId());
			values.put(UserEntity.COLUMN_NAME, user.getName());
			values.put(UserEntity.COLUMN_LASTNAME, user.getLastname());
			values.put(UserEntity.COLUMN_USERNAME, user.getUsername());
			values.put(UserEntity.COLUMN_PASSWORD, user.getPassword());
			values.put(UserEntity.COLUMN_GENDER, user.getGender());
			values.put(UserEntity.COLUMN_ADDRESS, user.getAddress());
			values.put(UserEntity.COLUMN_EMAIL, user.getEmail());
			values.put(UserEntity.COLUMN_CEDULA, user.getCedula());
			values.put(UserEntity.COLUMN_IS_CURRENT_USER, user.getIsCurrentUser());
			values.put(UserEntity.COLUMN_TELEPHONE_HOUSE,
					user.getHouse_telephone());
			values.put(UserEntity.COLUMN_TELEPHONE_MOBILE,
					user.getMobile_telephone());
			values.put(UserEntity.COLUMN_ADMIN, user.getisAdmin());
			if (user.getUpdated_at() != null) {
				values.put(RequestEntity.COLUMN_UPDATED_AT, user
						.getUpdated_at().getTimeInMillis());

			}
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
			values.put(UserEntity.COLUMN_IS_CURRENT_USER, user.getIsCurrentUser());
			values.put(UserEntity.COLUMN_EMAIL, user.getEmail());
			values.put(UserEntity.COLUMN_CEDULA, user.getCedula());
			values.put(UserEntity.COLUMN_TELEPHONE_HOUSE,
					user.getHouse_telephone());
			values.put(UserEntity.COLUMN_TELEPHONE_MOBILE,
					user.getMobile_telephone());
			values.put(RequestEntity.COLUMN_UPDATED_AT, user.getUpdated_at()
					.getTimeInMillis());
			values.put(UserEntity.COLUMN_ADMIN, user.getisAdmin());

			String condition = UserEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(user.getSystemId() + "'");

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

	public static User readMainUser(Context context) {

		if (context == null)
			return null;
	
		String condition = UserEntity.COLUMN_IS_CURRENT_USER + " = " + User.IS_CURRENT_USER;
		
		final Cursor cursor = context.getContentResolver().query(URI_USER,
				null, condition, null, null);

		User user = null;

		if (cursor.getCount() == 0) {
			Log.d(LOG_TAG, "No user found as current user");
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(UserEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_SYSTEM_ID));
					final String username = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_USERNAME));
					final String password = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_PASSWORD));
					final String name = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_NAME));
					final String lastname = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_LASTNAME));
					final Integer cedula = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_CEDULA));
					final String address = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_ADDRESS));
					final String email = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_EMAIL));
					final Integer gender = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_GENDER));
					final Long mobile_telephone = cursor
							.getLong(cursor
									.getColumnIndex(UserEntity.COLUMN_TELEPHONE_MOBILE));
					final Long house_telephone = cursor.getLong(cursor
							.getColumnIndex(UserEntity.COLUMN_TELEPHONE_HOUSE));
					final Integer admin = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_ADMIN));
					final Integer isCurrentUser = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_IS_CURRENT_USER));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					user = new User();
					user.setId(id);
					user.setSystemId(system_id);
					user.setUsername(username);
					user.setPassword(password);
					user.setIsCurrentUser(isCurrentUser);
					user.setName(name);
					user.setLastname(lastname);
					user.setCedula(cedula);
					user.setAddress(address);
					user.setEmail(email);
					user.setGender(gender);
					user.setMobile_telephone(mobile_telephone);
					user.setHouse_telephone(house_telephone);
					user.setAdmin(admin);
					user.setUpdated_at(updatedAt);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			user = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return user;
	}

	public static ArrayList<User> readUsers(Context context) {

		if (context == null)
			return null;

		ArrayList<User> users = new ArrayList<User>();

		String condition = UserEntity.COLUMN_ADMIN + " = " + 0;

		final Cursor cursor = context.getContentResolver().query(URI_USER,
				null, condition, null, null);

		User user = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(UserEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_SYSTEM_ID));
					final String username = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_USERNAME));
					final String password = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_PASSWORD));
					final String name = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_NAME));
					final String lastname = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_LASTNAME));
					final Integer cedula = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_CEDULA));
					final String address = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_ADDRESS));
					final String email = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_EMAIL));
					final Integer gender = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_GENDER));
					final Long mobile_telephone = cursor
							.getLong(cursor
									.getColumnIndex(UserEntity.COLUMN_TELEPHONE_MOBILE));
					final Long house_telephone = cursor.getLong(cursor
							.getColumnIndex(UserEntity.COLUMN_TELEPHONE_HOUSE));
					final Integer admin = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_ADMIN));
					final Integer isCurrentUser = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_IS_CURRENT_USER));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					user = new User();
					user.setId(id);
					user.setSystemId(system_id);
					user.setUsername(username);
					user.setPassword(password);
					user.setIsCurrentUser(isCurrentUser);
					user.setName(name);
					user.setLastname(lastname);
					user.setCedula(cedula);
					user.setAddress(address);
					user.setEmail(email);
					user.setGender(gender);
					user.setMobile_telephone(mobile_telephone);
					user.setHouse_telephone(house_telephone);
					user.setAdmin(admin);
					user.setUpdated_at(updatedAt);

					users.add(user);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			users = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return users;
	}

	public static Date getLastUpdate(Context context) {

		String condition = UserEntity.COLUMN_ADMIN + " = " + 0;

		final Cursor cursor = context.getContentResolver().query(URI_USER,
				null, condition, null, UserEntity.COLUMN_UPDATED_AT + " DESC");

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				final long updated_at = cursor.getLong(cursor
						.getColumnIndex(UserEntity.COLUMN_UPDATED_AT));
				Date date = new Date(updated_at);
				Log.d(LOG_TAG,
						"last update "
								+ CalendarUtil.getDateFormated(date,
										"dd MM yyy mm:ss"));

				return date;
			}

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}

		return null;
	}

	public static User readUser(Context context, String systemId) {

		if (context == null)
			return null;

		String condition = UserEntity.COLUMN_SYSTEM_ID + " = " + "'" + systemId
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_USER,
				null, condition, null, null);

		User user = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(UserEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_SYSTEM_ID));
					final String username = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_USERNAME));
					final String password = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_PASSWORD));
					final String name = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_NAME));
					final String lastname = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_LASTNAME));
					final Integer cedula = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_CEDULA));
					final String address = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_ADDRESS));
					final String email = cursor.getString(cursor
							.getColumnIndex(UserEntity.COLUMN_EMAIL));
					final Integer gender = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_GENDER));
					final Long mobile_telephone = cursor
							.getLong(cursor
									.getColumnIndex(UserEntity.COLUMN_TELEPHONE_MOBILE));
					final Long house_telephone = cursor.getLong(cursor
							.getColumnIndex(UserEntity.COLUMN_TELEPHONE_HOUSE));
					final Integer admin = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_ADMIN));
					final Integer isCurrentUser = cursor.getInt(cursor
							.getColumnIndex(UserEntity.COLUMN_IS_CURRENT_USER));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					user = new User();
					user.setId(id);
					user.setSystemId(system_id);
					user.setUsername(username);
					user.setPassword(password);
					user.setIsCurrentUser(isCurrentUser);
					user.setName(name);
					user.setLastname(lastname);
					user.setCedula(cedula);
					user.setAddress(address);
					user.setEmail(email);
					user.setGender(gender);
					user.setMobile_telephone(mobile_telephone);
					user.setHouse_telephone(house_telephone);
					user.setAdmin(admin);
					user.setUpdated_at(updatedAt);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			user = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return user;
	}

}
