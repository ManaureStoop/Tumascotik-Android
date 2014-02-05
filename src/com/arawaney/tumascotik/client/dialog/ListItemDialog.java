package com.arawaney.tumascotik.client.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import com.arawaney.tumascotik.client.R;

import com.arawaney.tumascotik.client.activity.ViewRequests;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.db.CitationDB;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.util.Log;

@SuppressLint("ValidFragment")
public class ListItemDialog extends DialogFragment implements
		ParseRequestListener {

	String petName;
	String service;
	Date initialDate;
	Date finalDate;
	Date fechainicio2;
	int status;
	Request request;

	Context context;

	public ListItemDialog() {
	};

	public ListItemDialog(Request request, Context context) {

		petName = request.getPet().getName();
		Service services =  request.getService();
		service = services.getName();
		initialDate = request.getStart_date().getTime();
		finalDate = request.getFinish_date().getTime();
		status = request.getStatus();
		this.request = request;

		this.context = context;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String connector = context.getResources().getString(
				R.string.request_view_dialog_conector);
		builder.setTitle(service + " " + connector + " " + petName);
//		builder.setIcon(R.drawable.mascotiklogodialog);
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		if (status == Request.STATUS_ACCEPTED) {
			String cancel = context.getResources().getString(
					R.string.request_view_dialog_cancel_button);
			builder.setNegativeButton(cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							cancelRequestParse();
							EraseDataBase();
							deleteCalendar(initialDate.getTime(),
									finalDate.getTime(),
									"Cita con Tumascotik para " + petName);
							ViewRequests callingActivity = (ViewRequests) getActivity();
							callingActivity.refreshListView();
						}

					});
		}
		return builder.create();
	}

	private void cancelRequestParse() {

		ParseRequestProvider.cancelRequest(context, this, request);

	}

	void UpdateParse() {

		Parse.initialize(getActivity(),
				"wf9TGHsYeTz8od557fQ9o9pRel5BNlOT9oZ4CpbH",
				"2gKGXq41TDWhacnkA1YNH07mTKEI59bA7JlORu51");
		ParseQuery query = new ParseQuery("Citas");
		query.whereEqualTo("fechaInicial", initialDate);

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
				ParseObject object;
				if (e == null) {

					if (cList.size() != 0) {

						object = cList.get(0);
						object.put("aceptado", 5);
						object.put("nuevo", 0);
						object.saveInBackground();
					}
				} else {
					Log.d("EXCEPCION PARSE", e.toString());
				}
			}
		});

	}

	void EraseDataBase() {
		CitationDB db = new CitationDB(getActivity());
		db.open();
		db.delete(String.valueOf(initialDate.getTime()));
		db.close();
	}

	private void deleteCalendar(long startQueryUTC, long endQueryUTC,
			String title) {
		// URI Builder
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
				.buildUpon();
		ContentUris.appendId(eventsUriBuilder, startQueryUTC);
		ContentUris.appendId(eventsUriBuilder, endQueryUTC);
		Uri eventsDeleteUri = eventsUriBuilder.build();

		// Delete

		ContentResolver cr = getActivity().getContentResolver();

		try {
			Cursor c = cr.query(eventsDeleteUri,
					new String[] { CalendarContract.Instances.EVENT_ID }, null,
					null, null);
			c.moveToFirst();
			long eventID = c.getLong(0);
			c.close();
			Uri deleteUri = null;
			deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
			cr.delete(deleteUri, null, null);
		}

		catch (Exception e) {

			Log.d("DELETE", "ERROR");

		}

	}

	@Override
	public void OnRequestInserted(boolean inserted, String systemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnAllRequestsQueryFinished(ArrayList<Request> requests) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestQueryFInished(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCanceledQueryFinished(boolean canceled) {
		deleteRequestFromDataBase();
	}

	private void deleteRequestFromDataBase() {
		RequestProvider.removeRequest(context, request.getSystem_id());
	}
	@Override
	public void onRequestRemoveFinished(Request request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayRequestsQueryFinished(Date[] initialScheduledDates,
			Date[] finalScheduledDates) {
		// TODO Auto-generated method stub
		
	}

}
