package com.arawaney.tumascotik.client.control;

import java.util.Date;
import java.util.TimeZone;

import com.arawaney.tumascotik.client.util.CalendarUtil;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;

public class CalendarController {

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	static public void writeToCalendar(String petsName, Date intialDate,
			Date endDate, Context context) {
		
		// Writes Appointment on the Calendar
	
		long dateBegin =intialDate.getTime();
		long dateEnd = endDate.getTime();

		ContentResolver contentResolver = context.getContentResolver();

		String body = "Cita con Tumascotik para " + petsName;
		try {
			ContentValues eventvalue = writeEvent(dateBegin, dateEnd, body);

			Uri event = contentResolver.insert(
					CalendarContract.Events.CONTENT_URI, eventvalue);
			setupAlarm(event, eventvalue, contentResolver);

		} catch (Exception e) {
			Log.e("ERROR", "error writing to calendar", e);
		}
		;
	}

	private static ContentValues writeEvent(long dateBegin, long dateEnd, String body) {
		
		ContentValues eventvalue = new ContentValues();
		eventvalue.put(CalendarContract.Events.TITLE, body);
		eventvalue.put(CalendarContract.Events.DTSTART, dateBegin);
		eventvalue.put(CalendarContract.Events.DTEND, dateEnd);
     	eventvalue.put(CalendarContract.Events.CALENDAR_ID, 1);
		eventvalue.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone
				.getDefault().getID());
		
		return eventvalue;
	}

	private static void setupAlarm(Uri event, ContentValues eventvalue,
			ContentResolver contentResolver) {
		// Setting up Alarm 15 minutes before appointment
		eventvalue = new ContentValues();
		eventvalue.put(Reminders.MINUTES, 15);
		eventvalue.put(Reminders.EVENT_ID,
				Long.parseLong(event.getLastPathSegment()));
		eventvalue.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri = contentResolver.insert(Reminders.CONTENT_URI, eventvalue);
	}

	public static void deleteCalendar(long startQueryUTC, long endQueryUTC,
			String title, Context context) {
		// URI Builder
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
				.buildUpon();
		ContentUris.appendId(eventsUriBuilder, startQueryUTC);
		ContentUris.appendId(eventsUriBuilder, endQueryUTC);
		Uri eventsDeleteUri = eventsUriBuilder.build();

		// Delete

		try {
			Cursor c = context.getContentResolver().query(eventsDeleteUri,
					new String[] { CalendarContract.Instances.EVENT_ID }, null,
					null, null);
			c.moveToFirst();
			long eventID = c.getLong(0);
			c.close();
			Uri deleteUri = null;
			deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
			context.getContentResolver().delete(deleteUri, null, null);
		}

		catch (Exception e) {

			Log.d("DELETE", "ERROR");

		}

	}
}
