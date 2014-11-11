package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.arawaney.tumascotik.client.db.PetEntity;
import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.db.SocialNetworkEntity;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.SocialNetwork;
import com.arawaney.tumascotik.client.util.CalendarUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SocialNetworkProvider {

	private static final String LOG_TAG = "Tumascotik-Client-SocialNetworkProvider";

	public static final Uri URI_SOCIAL_NETWORK = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + SocialNetworkEntity.TABLE);

	public static int insertSocialNetwork(Context context, SocialNetwork socialNetwork) {

		if (context == null || socialNetwork == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(SocialNetworkEntity.COLUMN_SYSTEM_ID, socialNetwork.getSystemId());
			values.put(SocialNetworkEntity.COLUMN_NAME, socialNetwork.getName());
			values.put(SocialNetworkEntity.COLUMN_FIELD, socialNetwork.getField());
			values.put(SocialNetworkEntity.COLUMN_ENABLED, socialNetwork.getEnabled());
			if (socialNetwork.getUpdated_at() != null) {
				values.put(RequestEntity.COLUMN_UPDATED_AT, socialNetwork
						.getUpdated_at().getTimeInMillis());

			}
			final Uri result = context.getContentResolver().insert(URI_SOCIAL_NETWORK,
					values);

			if (result != null) {
				int id = Integer.parseInt(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " SocialNetwork :" + socialNetwork.getName()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " SocialNetwork :" + socialNetwork.getName()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " SocialNetwork :" + socialNetwork.getName() + " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateSocialNetwork(Context context, SocialNetwork socialNetwork) {

		if (context == null || socialNetwork == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(SocialNetworkEntity.COLUMN_SYSTEM_ID, socialNetwork.getSystemId());
			values.put(SocialNetworkEntity.COLUMN_NAME, socialNetwork.getName());
			values.put(SocialNetworkEntity.COLUMN_FIELD, socialNetwork.getField());
			values.put(RequestEntity.COLUMN_UPDATED_AT, socialNetwork.getUpdated_at()
					.getTimeInMillis());
			values.put(SocialNetworkEntity.COLUMN_ENABLED, socialNetwork.getEnabled());

			String condition = SocialNetworkEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ String.valueOf(socialNetwork.getSystemId() + "'");

			int row = context.getContentResolver().update(URI_SOCIAL_NETWORK, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " SocialNetwork :" + socialNetwork.getName() + " has bee updated");
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static ArrayList<SocialNetwork> readSocialNetworks(Context context) {

		if (context == null)
			return null;

		ArrayList<SocialNetwork> socialNetworks = new ArrayList<SocialNetwork>();
		
		final Cursor cursor = context.getContentResolver().query(URI_SOCIAL_NETWORK,
				null, null, null, null);

		SocialNetwork socialNetwork = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					
					final String system_id = cursor.getString(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_NAME));
					final String field = cursor.getString(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_FIELD));					
					final Integer enabled = cursor.getInt(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					socialNetwork = new SocialNetwork();
					socialNetwork.setSystemId(system_id);				
					socialNetwork.setName(name);
					socialNetwork.setField(field);
					socialNetwork.setEnabled(enabled);
					socialNetwork.setUpdated_at(updatedAt);
					
					Log.d(LOG_TAG, name+" "+field);

					socialNetworks.add(socialNetwork);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			socialNetworks = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return socialNetworks;
	}

	public static Date getLastUpdate(Context context) {


		final Cursor cursor = context.getContentResolver().query(URI_SOCIAL_NETWORK,
				null, null, null, SocialNetworkEntity.COLUMN_UPDATED_AT + " DESC");

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				final long updated_at = cursor.getLong(cursor
						.getColumnIndex(SocialNetworkEntity.COLUMN_UPDATED_AT));
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

	public static SocialNetwork readSocialNetwork(Context context, String systemId) {

		if (context == null)
			return null;

		String condition = SocialNetworkEntity.COLUMN_SYSTEM_ID + " = " + "'" + systemId
				+ "'";

		final Cursor cursor = context.getContentResolver().query(URI_SOCIAL_NETWORK,
				null, condition, null, null);

		SocialNetwork socialNetwork = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final String system_id = cursor.getString(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_SYSTEM_ID));
					final String name = cursor.getString(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_NAME));
					final String field = cursor.getString(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_FIELD));					
					final Integer enabled = cursor.getInt(cursor
							.getColumnIndex(SocialNetworkEntity.COLUMN_ENABLED));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					socialNetwork = new SocialNetwork();
					socialNetwork.setSystemId(system_id);				
					socialNetwork.setName(name);
					socialNetwork.setField(field);
					socialNetwork.setEnabled(enabled);
					socialNetwork.setUpdated_at(updatedAt);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			socialNetwork = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return socialNetwork;
	}

}
