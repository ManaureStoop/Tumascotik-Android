package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.MotiveEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Motive;

public class MotiveProvider {
	private static final String LOG_TAG = "Tumascotik-Client-MotiveProvider";

	public static final Uri URI_MOTIVE = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + MotiveEntity.TABLE);

	public static long insertMotive(Context context, Motive motive) {

		if (context == null || motive == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(MotiveEntity.COLUMN_SYSTEM_ID, motive.getSystem_id());
			values.put(MotiveEntity.COLUMN_NAME, motive.getName());

			final Uri result = context.getContentResolver().insert(URI_MOTIVE,
					values);

			if (result != null) {
				long id = Long.parseLong(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Motive :" + motive.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Motive :" + motive.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Motive :" + motive.getName() + " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateMotive(Context context, Motive motive) {

		if (context == null || motive == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(MotiveEntity.COLUMN_ID, motive.getId());
			values.put(MotiveEntity.COLUMN_SYSTEM_ID, motive.getSystem_id());
			values.put(MotiveEntity.COLUMN_NAME, motive.getName());

			String condition = MotiveEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(motive.getSystem_id()) + "'";

			int row = context.getContentResolver().update(URI_MOTIVE, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Motive :" + motive.getName() + " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Motive :" + motive.getName() + " has not bee updated"
					+ e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Motive readMotive(Context context, String motiveID) {

		if (context == null)
			return null;

		String condition = MotiveEntity.COLUMN_SYSTEM_ID + " = " + "'" + motiveID
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_MOTIVE, null,
				condition, null, null);

		Motive motive = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(MotiveEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(MotiveEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(MotiveEntity.COLUMN_NAME));

					motive = new Motive();
					motive.setId(id);
					motive.setSystem_id(system_id);
					motive.setName(name);
					

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			motive = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return motive;
	}

	public static ArrayList<Motive> readMotives(Context context) {

		if (context == null)
			return null;

		ArrayList<Motive> motives = new ArrayList<Motive>();

		final Cursor cursor = context.getContentResolver().query(URI_MOTIVE, null,
				null, null, null);

		Motive motive = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(MotiveEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(MotiveEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(MotiveEntity.COLUMN_NAME));


					motive = new Motive();
					motive.setId(id);
					motive.setSystem_id(system_id);
					motive.setName(name);

					motives.add(motive);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			motives = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return motives;
	}

	public static boolean removeMotive(Context context, long motiveId) {

		try {
			String condition = MotiveEntity.COLUMN_ID + " = "
					+ String.valueOf(motiveId);
			int rows = context.getContentResolver().delete(URI_MOTIVE, condition,
					null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Motive : " + motiveId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting motive: " + e.getMessage());
		}
		return false;
	}
}
