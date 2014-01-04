package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.PetPropertieEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.PetPropertie;
import com.arawaney.tumascotik.client.model.Breed;

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
			if (petPropertie.getBreed() != null) {
				if (petPropertie.getBreed().getSystem_id() != null) {
					values.put(PetPropertieEntity.COLUMN_BREED_ID, petPropertie.getBreed()
							.getSystem_id());

				}else {
					Log.d(LOG_TAG, "Breed object null inserting: "+ petPropertie.getName());
				}
			}else {
				Log.d(LOG_TAG, "Breed name null inserting: "+ petPropertie.getName());
			}
			values.put(PetPropertieEntity.COLUMN_BREED_ID, petPropertie.getBreed()
					.getSystem_id());

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
			if (petPropertie.getBreed() != null) {
				if (petPropertie.getBreed().getSystem_id() != null) {
					values.put(PetPropertieEntity.COLUMN_BREED_ID, petPropertie.getBreed()
							.getSystem_id());

				}else {
					Log.d(LOG_TAG, "Breed object null inserting: "+ petPropertie.getName());
				}
			}else {
				Log.d(LOG_TAG, "Breed name null inserting: "+ petPropertie.getName());
			}

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

					petPropertie = new PetPropertie();
					petPropertie.setId(id);
					petPropertie.setSystem_id(system_id);
					petPropertie.setName(name);
					
					String breed_id = cursor.getString(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_BREED_ID));
					
					final Breed breed = BreedProvider.readBreed(context, breed_id);
					 if (breed!= null) {
						 petPropertie.setBreed(breed);
					}else {
						Log.d(LOG_TAG, "Breed null reading in local DB for : "+petPropertie.getName());
					}
					

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

					petPropertie = new PetPropertie();
					petPropertie.setId(id);
					petPropertie.setSystem_id(system_id);
					petPropertie.setName(name);
					String breed_id = cursor.getString(cursor
							.getColumnIndex(PetPropertieEntity.COLUMN_BREED_ID));
					
					final Breed breed = BreedProvider.readBreed(context, breed_id);
					 if (breed!= null) {
						 petPropertie.setBreed(breed);
					}else {
						Log.d(LOG_TAG, "Breed null reading in local DB for : "+petPropertie.getName());
					}
					

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
}
