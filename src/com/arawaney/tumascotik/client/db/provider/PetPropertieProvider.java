package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.BreedEntity;
import com.arawaney.tumascotik.client.db.PetPropertieEntity;
import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.util.CalendarUtil;

public class PetPropertieProvider {
	private static final String LOG_TAG = "Tumascotik-Client-PetPropertieProvider";

	public static final Uri URI_PETPROPERTIE = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + PetPropertieEntity.TABLE);

	public static long insertPetPropertie(Context context, PetPropertie petPropertie) {

		if (context == null || petPropertie == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(PetPropertieEntity.COLUMN_SYSTEM_ID, petPropertie.getSystem_id());
			values.put(PetPropertieEntity.COLUMN_NAME, petPropertie.getName());
			values.put(RequestEntity.COLUMN_UPDATED_AT, petPropertie
					.getUpdated_at().getTimeInMillis());


			final Uri result = context.getContentResolver().insert(URI_PETPROPERTIE,
					values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " PetPropertie :" + petPropertie.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " PetPropertie :" + petPropertie.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " PetPropertie :" + petPropertie.getName()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updatePetPropertie(Context context, PetPropertie petPropertie) {

		if (context == null || petPropertie == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(PetPropertieEntity.COLUMN_ID, petPropertie.getId());
			values.put(PetPropertieEntity.COLUMN_SYSTEM_ID, petPropertie.getSystem_id());
			values.put(PetPropertieEntity.COLUMN_NAME, petPropertie.getName());
			values.put(RequestEntity.COLUMN_UPDATED_AT, petPropertie
					.getUpdated_at().getTimeInMillis());


			String condition = PetPropertieEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(petPropertie.getSystem_id()) + "'";

			int row = context.getContentResolver().update(URI_PETPROPERTIE, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " PetPropertie :" + petPropertie.getName()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " PetPropertie :" + petPropertie.getName()
					+ " has not bee updated" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static PetPropertie readPetPropertie(Context context, String petPropertieID) {

		if (context == null)
			return null;

		String condition = PetPropertieEntity.COLUMN_SYSTEM_ID + " = " + "'" + petPropertieID
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_PETPROPERTIE,
				null, condition, null, null);

		PetPropertie petPropertie = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_NAME));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					petPropertie = new PetPropertie();
					petPropertie.setId(id);
					petPropertie.setSystem_id(system_id);
					petPropertie.setName(name);
					petPropertie.setUpdated_at(updatedAt);
					

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			petPropertie = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return petPropertie;
	}
	
	

	public static ArrayList<PetPropertie> readPetProperties(Context context) {

		if (context == null)
			return null;

		ArrayList<PetPropertie> petProperties = new ArrayList<PetPropertie>();

		final Cursor cursor = context.getContentResolver().query(URI_PETPROPERTIE,
				null, null, null, null);

		PetPropertie petPropertie = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_NAME));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					petPropertie = new PetPropertie();
					petPropertie.setId(id);
					petPropertie.setSystem_id(system_id);
					petPropertie.setName(name);
					petPropertie.setUpdated_at(updatedAt);
					petProperties.add(petPropertie);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			petProperties = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return petProperties;
	}

	public static boolean removePetPropertie(Context context, long petPropertieId) {

		try {
			String condition = PetPropertieEntity.COLUMN_ID + " = "
					+ String.valueOf(petPropertieId);
			int rows = context.getContentResolver().delete(URI_PETPROPERTIE,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "PetPropertie : " + petPropertieId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting petPropertie: " + e.getMessage());
		}
		return false;
	}
	
	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(URI_PETPROPERTIE, null,
				null, null, PetPropertieEntity.COLUMN_UPDATED_AT+" DESC");
		
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_UPDATED_AT));
					Date date = new Date(updated_at);
					Log.d(LOG_TAG, "last update "+CalendarUtil.getDateFormated(date, "dd MM yyy mm:ss"));
					
			return date;		
			}

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		
		return null;
	}
}
