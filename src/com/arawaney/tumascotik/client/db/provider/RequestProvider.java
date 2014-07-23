package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.Attributes.Name;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.PetEntity;
import com.arawaney.tumascotik.client.db.RequestEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;

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
			values.put(RequestEntity.COLUMN_START_DATE, request.getStart_date()
					.getTimeInMillis());
			values.put(RequestEntity.COLUMN_FINISH_DATE, request
					.getFinish_date().getTimeInMillis());
			if (request.getUpdated_at() != null) {
				values.put(RequestEntity.COLUMN_UPDATED_AT, request
						.getUpdated_at().getTimeInMillis());
			}
			
			values.put(RequestEntity.COLUMN_SERVICE_ID, request.getService().getSystem_id()
					);
			values.put(RequestEntity.COLUMN_COMMENT, request.getComment());
			values.put(RequestEntity.COLUMN_STATUS, request.getStatus());
			values.put(RequestEntity.COLUMN_DELIVERY, request.isDelivery());
			values.put(RequestEntity.COLUMN_ACTIVE, request.isActive());
			values.put(RequestEntity.COLUMN_PET_ID, request.getPet()
					.getSystem_id());

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
			Log.e(LOG_TAG, " Request :" + request.getId()
					+ " has not bee inserted");
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
			values.put(RequestEntity.COLUMN_START_DATE, request.getStart_date()
					.getTimeInMillis());
			Log.d(LOG_TAG, request.getSystem_id()+" "+request.getStart_date()
					.getTimeInMillis());
			values.put(RequestEntity.COLUMN_FINISH_DATE, request
					.getFinish_date().getTimeInMillis());
			values.put(RequestEntity.COLUMN_UPDATED_AT, request
					.getUpdated_at().getTimeInMillis());
	
			values.put(RequestEntity.COLUMN_COMMENT, request.getComment());

			values.put(RequestEntity.COLUMN_DELIVERY, request.isDelivery());
			values.put(RequestEntity.COLUMN_ACTIVE, request.isActive());
			values.put(RequestEntity.COLUMN_PET_ID, request.getPet()
					.getSystem_id());

			if (request.getService().getSystem_id() != null) {
				values.put(RequestEntity.COLUMN_SERVICE_ID, request.getService().getSystem_id());
			}
			if (request.getStatus() != 0) {
				values.put(RequestEntity.COLUMN_STATUS, request.getStatus());
			}
			if (request.getPrice() != null) {
				values.put(RequestEntity.COLUMN_PRICE, request.getPrice());
			}
			String condition = RequestEntity.COLUMN_SYSTEM_ID + " = " + "'"
					+ request.getSystem_id() + "'";

			int row = context.getContentResolver().update(URI_REQUEST, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Request :" + request.getId()
						+ " has bee updated");
				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Request :" + request.getId()
					+ " has not bee updated");
			e.printStackTrace();
		}
		return false;
	}

	public static Request readRequest(Context context, String requestSystemID) {

		if (context == null)
			return null;

		String condition = RequestEntity.COLUMN_SYSTEM_ID + " = " + "'"
				+ String.valueOf(requestSystemID) + "'";

		final Cursor cursor = context.getContentResolver().query(URI_REQUEST,
				null, condition, null, null);

		Request request = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_SYSTEM_ID));
					final long start_date = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_START_DATE));
					final long finish_date = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_FINISH_DATE));
					final String serviceId = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_SERVICE_ID));
					final Integer price = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_PRICE));
					final String comment = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_COMMENT));
					final int status = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_ACTIVE));
					final String pet_id = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_PET_ID));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTimeInMillis(start_date);

					Calendar finishCalendar = Calendar.getInstance();
					finishCalendar.setTimeInMillis(finish_date);
									
					Service service = ServiceProvider.readMotive(context, serviceId);

					request = new Request();
					request.setId(id);
					request.setSystem_id(system_id);
					request.setStart_date(startCalendar);
					request.setFinish_date(finishCalendar);
					request.setService(service);
					request.setPrice(price);
					request.setComment(comment);
					request.setStatus(status);
					request.setDelivery(delivery);
					request.setActive(active);
					request.setUpdated_at(updatedAt);

					Log.d("TEST 1", pet_id+" "+ system_id);
					if (pet_id != null) {
						request.setPet(PetProvider.readPet(context, pet_id));
					}

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			request = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return request;
	}

	public static boolean removeRequest(Context context, String requestId) {

		try {
			String condition = RequestEntity.COLUMN_SYSTEM_ID + " = "+ "'"
					+ String.valueOf(requestId)+ "'";
			int rows = context.getContentResolver().delete(URI_REQUEST,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Request : " + requestId + "has been deleted");
				return true;
			}else {
				Log.i(LOG_TAG, "Request : " + requestId + "has not been deleted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting request: " + e.getMessage());
		}
		return false;
	}

	public static ArrayList<Request> readRequests(Context context) {
		if (context == null)
			return null;

		ArrayList<Request> requests = new ArrayList<Request>();

		final Cursor cursor = context.getContentResolver().query(URI_REQUEST,
				null, null, null, null);


		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_SYSTEM_ID));
					final long start_date = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_START_DATE));
					final long finish_date = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_FINISH_DATE));
					final String serviceId = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_SERVICE_ID));
					final Integer price = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_PRICE));
					final String comment = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_COMMENT));
					final int status = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_ACTIVE));
					final String pet_id = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_PET_ID));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTimeInMillis(start_date);

					Calendar finishCalendar = Calendar.getInstance();
					finishCalendar.setTimeInMillis(finish_date);
					
					Service service = ServiceProvider.readMotive(context, serviceId);

					Request request = new Request();
					request.setId(id);
					request.setSystem_id(system_id);
					request.setStart_date(startCalendar);
					request.setFinish_date(finishCalendar);
					request.setService(service);
					request.setPrice(price);
					request.setComment(comment);
					request.setStatus(status);
					request.setDelivery(delivery);
					request.setActive(active);
					request.setUpdated_at(updatedAt);


					if (pet_id != null) {
						Log.d(LOG_TAG, "Reading pet: "+pet_id);
						request.setPet(PetProvider.readPet(context, pet_id));
					}

					requests.add(request);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			requests = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return requests;
	}
	
	public static ArrayList<Request> readPassedRequests(Context context, Calendar today) {
		if (context == null)
			return null;

		ArrayList<Request> requests = new ArrayList<Request>();

		String condition = "("+RequestEntity.COLUMN_STATUS + " = "+ "'"
				+ Request.STATUS_ACCEPTED+ "'"+" OR "+ RequestEntity.COLUMN_STATUS + " = "+ "'"
				+ Request.STATUS_PENDING+ "'"+")"+" AND "+ RequestEntity.COLUMN_FINISH_DATE + " < "+ "'"
				+ today.getTimeInMillis()+ "'";
		
		final Cursor cursor = context.getContentResolver().query(URI_REQUEST,
				null, condition, null, null);


		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_SYSTEM_ID));
					final long start_date = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_START_DATE));
					final long finish_date = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_FINISH_DATE));
					final String serviceId = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_SERVICE_ID));
					final Integer price = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_PRICE));
					final String comment = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_COMMENT));
					final int status = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_ACTIVE));
					final String pet_id = cursor.getString(cursor
							.getColumnIndex(RequestEntity.COLUMN_PET_ID));
					final long updated_at = cursor.getInt(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
					
					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);

					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTimeInMillis(start_date);

					Calendar finishCalendar = Calendar.getInstance();
					finishCalendar.setTimeInMillis(finish_date);
					
					Service service = ServiceProvider.readMotive(context, serviceId);

					Request request = new Request();
					request.setId(id);
					request.setSystem_id(system_id);
					request.setStart_date(startCalendar);
					request.setFinish_date(finishCalendar);
					request.setService(service);
					request.setPrice(price);
					request.setComment(comment);
					request.setStatus(status);
					request.setDelivery(delivery);
					request.setActive(active);
					request.setUpdated_at(updatedAt);


					if (pet_id != null) {
						Log.d(LOG_TAG, "Reading pet: "+pet_id);
						request.setPet(PetProvider.readPet(context, pet_id));
					}

					requests.add(request);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			requests = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return requests;
	}

	
	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(URI_REQUEST, null,
				null, null, PetEntity.COLUMN_UPDATED_AT+" DESC");
		
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(RequestEntity.COLUMN_UPDATED_AT));
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

	public static void removeRequestsByPet(Context context,
			String petSystemId) {

		try {
			String condition = RequestEntity.COLUMN_PET_ID + " = "+ "'"
					+ String.valueOf(petSystemId)+ "'";
			int rows = context.getContentResolver().delete(URI_REQUEST,
					condition, null);

			if (rows >= 1) {
				Log.i(LOG_TAG, rows +" Requests : with pet id " + petSystemId + "have been deleted");
				
			}else {
				Log.i(LOG_TAG, "Requests : with pet id " + petSystemId + "have not been deleted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting request: " + e.getMessage());
		}
		
	}
}
