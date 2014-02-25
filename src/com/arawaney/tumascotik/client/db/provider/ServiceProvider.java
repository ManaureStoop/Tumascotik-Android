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
import com.arawaney.tumascotik.client.db.ServiceEntity;
import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.util.CalendarUtil;

public class ServiceProvider {
	private static final String LOG_TAG = "Tumascotik-Client-MotiveProvider";

	public static final Uri URI_SERVICE = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + ServiceEntity.TABLE);

	public static long insertMotive(Context context, Service motive) {

		if (context == null || motive == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(ServiceEntity.COLUMN_SYSTEM_ID, motive.getSystem_id());
			values.put(ServiceEntity.COLUMN_NAME, motive.getName());
			values.put(ServiceEntity.COLUMN_NEED_REQUEST,
					motive.getNeedsRequest());
			values.put(ServiceEntity.COLUMN_DURATION, motive.getDuration());
			values.put(RequestEntity.COLUMN_UPDATED_AT, motive
					.getUpdated_at().getTimeInMillis());

			final Uri result = context.getContentResolver().insert(URI_SERVICE,
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
			Log.e(LOG_TAG, " Motive :" + motive.getName()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateMotive(Context context, Service motive) {

		if (context == null || motive == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(ServiceEntity.COLUMN_ID, motive.getId());
			values.put(ServiceEntity.COLUMN_SYSTEM_ID, motive.getSystem_id());
			values.put(ServiceEntity.COLUMN_NAME, motive.getName());
			values.put(ServiceEntity.COLUMN_NEED_REQUEST,
					motive.getNeedsRequest());
			values.put(ServiceEntity.COLUMN_UPDATED_AT, motive.getUpdated_at()
					.getTimeInMillis());
			values.put(ServiceEntity.COLUMN_DURATION, motive.getDuration());

			String condition = ServiceEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(motive.getSystem_id()) + "'";

			int row = context.getContentResolver().update(URI_SERVICE, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Motive :" + motive.getName()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Motive :" + motive.getName()
					+ " has not bee updated" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static Service readMotive(Context context, String motiveID) {

		if (context == null)
			return null;

		String condition = ServiceEntity.COLUMN_SYSTEM_ID + " = " + "'"
				+ motiveID + "'";

		final Cursor cursor = context.getContentResolver().query(URI_SERVICE,
				null, condition, null, null);

		Service motive = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(ServiceEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NAME));
					final int needRequest = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NEED_REQUEST));
					final int duration = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_DURATION));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(ServiceEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					motive = new Service();
					motive.setId(id);
					motive.setSystem_id(system_id);
					motive.setNeedsRequest(needRequest);
					motive.setName(name);
					motive.setDuration(duration);
					motive.setUpdated_at(updatedAt);

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

	public static ArrayList<Service> readMotives(Context context) {

		if (context == null)
			return null;

		ArrayList<Service> motives = new ArrayList<Service>();

		final Cursor cursor = context.getContentResolver().query(URI_SERVICE,
				null, null, null, null);

		Service motive = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(ServiceEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NAME));
					final int needRequest = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NEED_REQUEST));
					final int duration = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_DURATION));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					motive = new Service();
					motive.setId(id);
					motive.setSystem_id(system_id);
					motive.setNeedsRequest(needRequest);
					motive.setName(name);
					motive.setDuration(duration);
					motive.setUpdated_at(updatedAt);

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
	
	public static ArrayList<Service> readRequestMotives(Context context) {

		if (context == null)
			return null;

		ArrayList<Service> motives = new ArrayList<Service>();
		
		String condition = ServiceEntity.COLUMN_NEED_REQUEST + " = " + "'"
				+ Service.NEED_REQUEST + "'";


		final Cursor cursor = context.getContentResolver().query(URI_SERVICE,
				null, condition, null, null);

		Service motive = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(ServiceEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NAME));
					final int needRequest = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NEED_REQUEST));
					final int duration = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_DURATION));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					motive = new Service();
					motive.setId(id);
					motive.setSystem_id(system_id);
					motive.setNeedsRequest(needRequest);
					motive.setName(name);
					motive.setDuration(duration);
					motive.setUpdated_at(updatedAt);

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
	
	public static ArrayList<Service> readNotRequest(Context context) {

		if (context == null)
			return null;

		ArrayList<Service> motives = new ArrayList<Service>();
		
		String condition = ServiceEntity.COLUMN_NEED_REQUEST + " = " + "'"
				+ Service.NOT_NEED_REQUEST + "'";


		final Cursor cursor = context.getContentResolver().query(URI_SERVICE,
				null, condition, null, null);

		Service motive = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(ServiceEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NAME));
					final int needRequest = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_NEED_REQUEST));
					final int duration = cursor.getInt(cursor
							.getColumnIndex(ServiceEntity.COLUMN_DURATION));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					motive = new Service();
					motive.setId(id);
					motive.setSystem_id(system_id);
					motive.setNeedsRequest(needRequest);
					motive.setName(name);
					motive.setDuration(duration);
					motive.setUpdated_at(updatedAt);

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
			String condition = ServiceEntity.COLUMN_ID + " = "
					+ String.valueOf(motiveId);
			int rows = context.getContentResolver().delete(URI_SERVICE,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Motive : " + motiveId + "has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting motive: " + e.getMessage());
		}
		return false;
	}

	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(URI_SERVICE, null,
				null, null, ServiceEntity.COLUMN_UPDATED_AT+" DESC");
		
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(ServiceEntity.COLUMN_UPDATED_AT));
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
