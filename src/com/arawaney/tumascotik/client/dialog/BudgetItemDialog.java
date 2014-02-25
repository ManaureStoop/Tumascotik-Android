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
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.listener.ParseBudgetListener;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetService;
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
public class BudgetItemDialog extends DialogFragment
		 {

	String userName;
	List<BudgetService> services;
	Context context;

	public BudgetItemDialog() {
	};

	public BudgetItemDialog(Context context,List<BudgetService> services) {
		
		
		this.services = services;
		this.context = context;
		
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		ArrayList<String> content = new ArrayList<String>();
		
		for (int i = 0; i < services.size(); i++) {
			String string = services.get(i).getService().getName()+"       Bs."+services.get(i).getPrice();
			content.add(string);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final CharSequence[] charSequenceItems = content.toArray(new CharSequence[content.size()]);

		builder.setTitle(context.getResources().getString(R.string.budget_dialog_title));
		builder.setItems(charSequenceItems, null);
		// Add the buttons	
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		return builder.create();
	}



}
