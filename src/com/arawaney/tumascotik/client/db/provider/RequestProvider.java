package com.arawaney.tumascotik.client.db.provider;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Request;

public class RequestProvider {
	private static final String LOG_TAG = "Tumascotik-Client-RequestProvider";

	public static final Uri URI_REQUEST = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + RequestEntity.TABLE);

	public static int insertRequest(Context context, Request request) {

		if (context == null || request == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(RequestEntity.COLUMN_SYSTEM_ID, request.getSystem_id());
			values.put(RequestEntity.COLUMN_START_DATE, request.getStart_date().getTimeInMillis());
			values.put(RequestEntity.COLUMN_FINISH_DATE, request.getFinisch_date().getTimeInMillis());
			values.put(RequestEntity.COLUMN_SERVICE, request.getService());
			values.put(RequestEntity.COLUMN_COMMENT, request.getComment());
			values.put(RequestEntity.COLUMN_STATUS, request.getStatus());
			values.put(RequestEntity.COLUMN_DELIVERY, request.isDelivery());
			values.put(RequestEntity.COLUMN_IS_APPOINTMENT, request.Is_appointment());
			values.put(RequestEntity.COLUMN_ACTIVE, request.isActive());
			values.put(RequestEntity.COLUMN_PET_ID, request.getPet().getId());
			

			final Uri result = context.getContentResolver().insert(URI_REQUEST,
					values);

			if (result != null) {
				int id = Integer.parseInt(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Request :" + request.getId()
							+ " has bee inserted");
					return id;
				} else
					Log.e(LOG_TAG, " Request :" + request.getId()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Request :" + request.getId() + " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateRequest(Context context, Request request) {

		if (context == null || request == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(RequestEntity.COLUMN_ID, request.getId());
			values.put(RequestEntity.COLUMN_SYSTEM_ID, request.getSystem_id());
			values.put(RequestEntity.COLUMN_START_DATE, request.getStart_date().getTimeInMillis());
			values.put(RequestEntity.COLUMN_FINISH_DATE, request.getFinisch_date().getTimeInMillis());
			values.put(RequestEntity.COLUMN_SERVICE, request.getService());
			values.put(RequestEntity.COLUMN_COMMENT, request.getComment());
			values.put(RequestEntity.COLUMN_STATUS, request.getStatus());
			values.put(RequestEntity.COLUMN_DELIVERY, request.isDelivery());
			values.put(RequestEntity.COLUMN_IS_APPOINTMENT, request.Is_appointment());
			values.put(RequestEntity.COLUMN_ACTIVE, request.isActive());
			values.put(RequestEntity.COLUMN_PET_ID, request.getPet().getId());

			String condition = RequestEntity.COLUMN_ID + " = "
					+ String.valueOf(request.getId());

			int row = context.getContentResolver().update(URI_REQUEST, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Request :" + request.getId() + " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Request :" + request.getId() + " has not bee updated");
			e.printStackTrace();
		}
		return false;
	}

	public static Request readRequest(Context context, long requestID) {
		

		if (context == null )
			return null;
		

		String condition = RequestEntity.COLUMN_ID + " = "
				+ String.valueOf(requestID);
		
		final Cursor cursor = context.getContentResolver().query(URI_REQUEST, null, condition,null, null);
		
		Request request = null;
		
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		
		try {
		if (cursor.moveToFirst()) {
			
			do {
				final long id = cursor.getLong(cursor.getColumnIndex(RequestEntity.COLUMN_ID));
				final long system_id = cursor.getLong(cursor.getColumnIndex(RequestEntity.COLUMN_SYSTEM_ID));			
				final long start_date = cursor.getInt(cursor.getColumnIndex(RequestEntity.COLUMN_START_DATE));		
				final long finish_date = cursor.getInt(cursor.getColumnIndex(RequestEntity.COLUMN_FINISH_DATE));	
				final String service = cursor.getString(cursor.getColumnIndex(RequestEntity.COLUMN_SERVICE));	
				final Integer price = cursor.getInt(cursor.getColumnIndex(RequestEntity.COLUMN_PRICE));	
				final String comment = cursor.getString(cursor.getColumnIndex(RequestEntity.COLUMN_COMMENT));	
				final String status = cursor.getString(cursor.getColumnIndex(RequestEntity.COLUMN_STATUS));	
				final Integer delivery  = cursor.getInt(cursor.getColumnIndex(RequestEntity.COLUMN_DELIVERY));		
				final Integer is_appointment = cursor.getInt(cursor.getColumnIndex(RequestEntity.COLUMN_DELIVERY));	
				final Integer active = cursor.getInt(cursor.getColumnIndex(RequestEntity.COLUMN_ACTIVE));	
				final long pet_id = cursor.getLong(cursor.getColumnIndex(RequestEntity.COLUMN_PET_ID));

				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTimeInMillis(start_date);
				
				Calendar finishCalendar = Calendar.getInstance();
				finishCalendar.setTimeInMillis(finish_date);
				
				request = new Request();
				request.setId(id);
				request.setStart_date(startCalendar);
				request.setFinisch_date(finishCalendar);
				request.setService(service);
				request.setPrice(price);
				request.setComment(comment);
				request.setStatus(status);
				request.setDelivery(delivery);
				request.setIs_appointment(is_appointment);
				request.setActive(active);
				
				
				if (pet_id != 0) {
					request.setPet(PetProvider.readPet(context, pet_id));
				} 
	
				
			} while (cursor.moveToNext());
		}

		} catch (Exception e) {
			request = null;
			Log.e(LOG_TAG, "Error : "+ e.getMessage());
		}
		finally{
			cursor.close();
		}
		return request;
	}
	
	public static boolean removeRequest(Context context, long requestId){
		
		try {
			String condition = RequestEntity.COLUMN_ID + " = " + String.valueOf(requestId);
			int rows = context.getContentResolver().delete(URI_REQUEST, condition, null);
			
			if (rows == 1) {
				Log.i(LOG_TAG,"Request : "+ requestId +"has been deleted");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting request: "+ e.getMessage());
		}
	return false;	
	}
}
