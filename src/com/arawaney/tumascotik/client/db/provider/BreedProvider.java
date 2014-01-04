package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.BreedEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Specie;

public class BreedProvider {
	private static final String LOG_TAG = "Tumascotik-Client-BreedProvider";

	public static final Uri URI_BREED = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + BreedEntity.TABLE);

	public static long insertBreed(Context context, Breed breed) {

		if (context == null || breed == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(BreedEntity.COLUMN_SYSTEM_ID, breed.getSystem_id());
			values.put(BreedEntity.COLUMN_NAME, breed.getName());
			if (breed.getSpecie() != null) {
				if (breed.getSpecie().getSystem_id() != null) {
					values.put(BreedEntity.COLUMN_SPECIE_ID, breed.getSpecie()
							.getSystem_id());

				}else {
					Log.d(LOG_TAG, "Specie object null inserting: "+ breed.getName());
				}
			}else {
				Log.d(LOG_TAG, "Specie name null inserting: "+ breed.getName());
			}
			values.put(BreedEntity.COLUMN_SPECIE_ID, breed.getSpecie()
					.getSystem_id());

			final Uri result = context.getContentResolver().insert(URI_BREED,
					values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Breed :" + breed.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Breed :" + breed.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Breed :" + breed.getName()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateBreed(Context context, Breed breed) {

		if (context == null || breed == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(BreedEntity.COLUMN_ID, breed.getId());
			values.put(BreedEntity.COLUMN_SYSTEM_ID, breed.getSystem_id());
			values.put(BreedEntity.COLUMN_NAME, breed.getName());
			if (breed.getSpecie() != null) {
				if (breed.getSpecie().getSystem_id() != null) {
					values.put(BreedEntity.COLUMN_SPECIE_ID, breed.getSpecie()
							.getSystem_id());

				}else {
					Log.d(LOG_TAG, "Specie object null inserting: "+ breed.getName());
				}
			}else {
				Log.d(LOG_TAG, "Specie name null inserting: "+ breed.getName());
			}

			String condition = BreedEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(breed.getSystem_id()) + "'";

			int row = context.getContentResolver().update(URI_BREED, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Breed :" + breed.getName()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Breed :" + breed.getName()
					+ " has not bee updated" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Breed readBreed(Context context, String breedID) {

		if (context == null)
			return null;

		String condition = BreedEntity.COLUMN_SYSTEM_ID + " = " + "'" + breedID
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_BREED,
				null, condition, null, null);

		Breed breed = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(BreedEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(BreedEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(BreedEntity.COLUMN_NAME));

					breed = new Breed();
					breed.setId(id);
					breed.setSystem_id(system_id);
					breed.setName(name);
					
					String specie_id = cursor.getString(cursor
							.getColumnIndex(BreedEntity.COLUMN_SPECIE_ID));
					
					final Specie specie = SpecieProvider.readSpecie(context, specie_id);
					 if (specie!= null) {
						 breed.setSpecie(specie);
					}else {
						Log.d(LOG_TAG, "SPecie null reading in local DB for : "+breed.getName());
					}
					

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			breed = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return breed;
	}

	public static ArrayList<Breed> readBreeds(Context context) {

		if (context == null)
			return null;

		ArrayList<Breed> breeds = new ArrayList<Breed>();

		final Cursor cursor = context.getContentResolver().query(URI_BREED,
				null, null, null, null);

		Breed breed = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(BreedEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(BreedEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(BreedEntity.COLUMN_NAME));

					breed = new Breed();
					breed.setId(id);
					breed.setSystem_id(system_id);
					breed.setName(name);
					String specie_id = cursor.getString(cursor
							.getColumnIndex(BreedEntity.COLUMN_SPECIE_ID));
					
					final Specie specie = SpecieProvider.readSpecie(context, specie_id);
					 if (specie!= null) {
						 breed.setSpecie(specie);
					}else {
						Log.d(LOG_TAG, "SPecie null reading in local DB for : "+breed.getName());
					}
					

					breeds.add(breed);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			breeds = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return breeds;
	}

	public static boolean removeBreed(Context context, long breedId) {

		try {
			String condition = BreedEntity.COLUMN_ID + " = "
					+ String.valueOf(breedId);
			int rows = context.getContentResolver().delete(URI_BREED,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Breed : " + breedId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting breed: " + e.getMessage());
		}
		return false;
	}
}
