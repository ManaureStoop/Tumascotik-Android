package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.PetPropertieEntity;
import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.SpecieEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Specie;
import com.arawaney.tumascotik.client.util.CalendarUtil;

public class SpecieProvider {
	private static final String LOG_TAG = "Tumascotik-Client-SpecieProvider";

	public static final Uri URI_SPECIE = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + SpecieEntity.TABLE);

	public static long insertSpecie(Context context, Specie specie) {

		if (context == null || specie == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(SpecieEntity.COLUMN_SYSTEM_ID, specie.getSystem_id());
			values.put(SpecieEntity.COLUMN_NAME, specie.getName());
			values.put(RequestEntity.COLUMN_UPDATED_AT, specie
					.getUpdated_at().getTimeInMillis());

			final Uri result = context.getContentResolver().insert(URI_SPECIE,
					values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Specie :" + specie.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Specie :" + specie.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Specie :" + specie.getName() + " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateSpecie(Context context, Specie specie) {

		if (context == null || specie == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(SpecieEntity.COLUMN_ID, specie.getId());
			values.put(SpecieEntity.COLUMN_SYSTEM_ID, specie.getSystem_id());
			values.put(SpecieEntity.COLUMN_NAME, specie.getName());
			values.put(RequestEntity.COLUMN_UPDATED_AT, specie
					.getUpdated_at().getTimeInMillis());

			String condition = SpecieEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(specie.getSystem_id()) + "'";

			int row = context.getContentResolver().update(URI_SPECIE, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Specie :" + specie.getName() + " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Specie :" + specie.getName() + " has not bee updated"
					+ e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Specie readSpecie(Context context, String specieID) {

		if (context == null)
			return null;

		String condition = SpecieEntity.COLUMN_SYSTEM_ID + " = " + "'" + specieID
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_SPECIE, null,
				condition, null, null);

		Specie specie = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(SpecieEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(SpecieEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SpecieEntity.COLUMN_NAME));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					specie = new Specie();
					specie.setId(id);
					specie.setSystem_id(system_id);
					specie.setName(name);
					specie.setUpdated_at(updatedAt);
					

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			specie = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return specie;
	}

	public static ArrayList<Specie> readSpecies(Context context) {

		if (context == null)
			return null;

		ArrayList<Specie> species = new ArrayList<Specie>();

		final Cursor cursor = context.getContentResolver().query(URI_SPECIE, null,
				null, null, null);

		Specie specie = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(SpecieEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(SpecieEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SpecieEntity.COLUMN_NAME));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);


					specie = new Specie();
					specie.setId(id);
					specie.setSystem_id(system_id);
					specie.setName(name);
					specie.setUpdated_at(updatedAt);
					species.add(specie);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			species = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return species;
	}

	public static boolean removeSpecie(Context context, long specieId) {

		try {
			String condition = SpecieEntity.COLUMN_ID + " = "
					+ String.valueOf(specieId);
			int rows = context.getContentResolver().delete(URI_SPECIE, condition,
					null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Specie : " + specieId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting specie: " + e.getMessage());
		}
		return false;
	}

	public static Date getLastUpdate(Context context) {

		final Cursor cursor = context.getContentResolver().query(URI_SPECIE, null,
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
