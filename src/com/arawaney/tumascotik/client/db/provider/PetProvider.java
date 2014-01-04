package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.PetEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Breed;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;

public class PetProvider {
	private static final String LOG_TAG = "Tumascotik-Client-PetProvider";

	public static final Uri URI_PET = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + PetEntity.TABLE);

	public static long insertPet(Context context, Pet pet) {

		if (context == null || pet == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(PetEntity.COLUMN_SYSTEM_ID, pet.getSystem_id());
			values.put(PetEntity.COLUMN_NAME, pet.getName());
			values.put(PetEntity.COLUMN_USER_ID, pet.getOwner().getId());
			values.put(PetEntity.COLUMN_COMMENT, pet.getComment());
			values.put(PetEntity.COLUMN_GENDER, pet.getGender());

			if (pet.getBreed() != null) {
				if (pet.getBreed().getSystem_id() != null) {
					values.put(PetEntity.COLUMN_BREED_ID, pet.getBreed()
							.getSystem_id());
				} else {
					Log.d(LOG_TAG,
							"Breed Name null inserting pet: " + pet.getName());
				}

			} else {
				Log.d(LOG_TAG, "Breed null inserting pet: " + pet.getName());
			}

			values.put(PetEntity.COLUMN_PUPPY, pet.getPuppy());

			final Uri result = context.getContentResolver().insert(URI_PET,
					values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Pet :" + pet.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Pet :" + pet.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Pet :" + pet.getName() + " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updatePet(Context context, Pet pet) {

		if (context == null || pet == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(PetEntity.COLUMN_ID, pet.getId());
			values.put(PetEntity.COLUMN_SYSTEM_ID, pet.getSystem_id());
			values.put(PetEntity.COLUMN_NAME, pet.getName());
			values.put(PetEntity.COLUMN_COMMENT, pet.getComment());
			values.put(PetEntity.COLUMN_GENDER, pet.getGender());
			values.put(PetEntity.COLUMN_PUPPY, pet.getPuppy());

			if (pet.getBreed() != null) {
				if (pet.getBreed().getSystem_id() != null) {
					values.put(PetEntity.COLUMN_BREED_ID, pet.getBreed()
							.getSystem_id());
				} else {
					Log.d(LOG_TAG,
							"Breed Name null updating pet: " + pet.getName());
				}

			} else {
				Log.d(LOG_TAG, "Breed null updating pet: " + pet.getName());
			}

			if (pet.getOwner() != null)
				values.put(PetEntity.COLUMN_USER_ID, pet.getOwner().getId());

			String condition = PetEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(pet.getSystem_id()) + "'";

			int row = context.getContentResolver().update(URI_PET, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Pet :" + pet.getName() + " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Pet :" + pet.getName() + " has not bee updated"
					+ e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Pet readPet(Context context, String petID) {

		if (context == null)
			return null;

		String condition = PetEntity.COLUMN_SYSTEM_ID + " = " + "'" + petID
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_PET, null,
				condition, null, null);

		Pet pet = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(PetEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_NAME));

					final String comment = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_COMMENT));
					final int gender = cursor.getInt(cursor
							.getColumnIndex(PetEntity.COLUMN_GENDER));
					final Integer puppy = cursor.getInt(cursor
							.getColumnIndex(PetEntity.COLUMN_PUPPY));
					;

					pet = new Pet();
					pet.setId(id);
					pet.setSystem_id(system_id);
					pet.setName(name);
					pet.setComment(comment);
					pet.setGender(gender);
					pet.setPuppy(puppy);
					pet.setOwner(UserProvider.readUser(context));

					String breed_id = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_BREED_ID));
					;
					final Breed breed = BreedProvider.readBreed(context,
							breed_id);
					if (breed != null) {
						pet.setBreed(breed);
					}

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			pet = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return pet;
	}

	public static ArrayList<Pet> readPets(Context context) {

		if (context == null)
			return null;

		ArrayList<Pet> pets = new ArrayList<Pet>();

		final Cursor cursor = context.getContentResolver().query(URI_PET, null,
				null, null, null);

		Pet pet = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(PetEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_NAME));
					final String comment = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_COMMENT));
					final int gender = cursor.getInt(cursor
							.getColumnIndex(PetEntity.COLUMN_GENDER));
					final Integer puppy = cursor.getInt(cursor
							.getColumnIndex(PetEntity.COLUMN_PUPPY));
					;

					pet = new Pet();
					pet.setId(id);
					pet.setSystem_id(system_id);
					pet.setName(name);
					pet.setComment(comment);
					pet.setGender(gender);
					pet.setPuppy(puppy);
					pet.setOwner(UserProvider.readUser(context));

					String breed_id = cursor.getString(cursor
							.getColumnIndex(PetEntity.COLUMN_BREED_ID));
					
					final Breed breed = BreedProvider.readBreed(context,
							breed_id);
					if (breed != null) {
						pet.setBreed(breed);
					}

					pets.add(pet);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			pets = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return pets;
	}

	public static boolean removePet(Context context, long petId) {

		try {
			String condition = PetEntity.COLUMN_ID + " = "
					+ String.valueOf(petId);
			int rows = context.getContentResolver().delete(URI_PET, condition,
					null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Pet : " + petId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting pet: " + e.getMessage());
		}
		return false;
	}
}
