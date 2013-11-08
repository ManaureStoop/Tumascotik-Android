package com.arawaney.tumascotik.client.dialog;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.arawaney.tumascotik.client.activity.Presupuesto;
import com.arawaney.tumascotik.client.db.BudgetDB;
import com.arawaney.tumascotik.client.R;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@SuppressLint("ValidFragment")
public class PresupuestoItemDialog extends DialogFragment {
	
	String title;
	int price;

	
	
	
	public PresupuestoItemDialog(String titl, int pric){
		title = titl;
		price = pric;
		
    }     
    
	
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(title+" de "+ String.valueOf(price));
	    builder.setIcon(R.drawable.mascotiklogodialog);
	 // Add the buttons
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.cancel();
	               }
	           });
	   
	    builder.setNegativeButton("Eliminar de lista", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {    	   
	            	   EraseDataBase();
		               Presupuesto callingActivity = (Presupuesto) getActivity();
		               callingActivity.Refresh();
	               }
	           });
	    return builder.create();
	}

	
	void EraseDataBase(){
		BudgetDB db = new BudgetDB(getActivity());
  	  	db.open();
  	  	db.delete(String.valueOf(title));
  	  	db.close();
	}
	
		}
	


