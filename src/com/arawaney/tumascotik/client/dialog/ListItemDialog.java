package com.arawaney.tumascotik.client.dialog;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import com.arawaney.tumascotik.client.R;


import com.arawaney.tumascotik.client.activity.VerCitas;
import com.arawaney.tumascotik.client.db.CitationDB;
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
public class ListItemDialog extends DialogFragment {
	
	String nombre;
	String motivo;
	Date fechainicio;
	Date fechafinal;
	Date fechainicio2;
	String stats;
	
	public ListItemDialog(){};
	
	public ListItemDialog(String name, String motive, Date initialdate, Date finaldate, String status ){
		nombre = name;
		motivo =motive;
		fechainicio=initialdate;
		fechafinal = finaldate;
        stats = status;
    }     
    
	
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(motivo+" para "+nombre);
	    builder.setIcon(R.drawable.mascotiklogodialog);
	 // Add the buttons
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.cancel();
	               }
	           });
	    if (stats.equals("ACEPTADAS")){
	    builder.setNegativeButton("Cancelar Cita", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   EraseSharedPref();
	            	   UpdateParse();
	            	   EraseDataBase();
	            	   deleteCalendar(fechainicio.getTime(),fechafinal.getTime(),"Cita con Tumascotik para "+nombre);
		               VerCitas callingActivity = (VerCitas) getActivity();
		               callingActivity.Refresh();
	               }
	           });}
	    return builder.create();
	}

	void EraseSharedPref(){
		int i;
		int j;
		int index = 0;
		int ind = 0;
		//Loads every object pending 
		SharedPreferences settings = getActivity().getSharedPreferences("TUMASC", 0);
		index = settings.getInt("index",0);
		
		for(i=0;i<index;i++){
			fechainicio2 = new GregorianCalendar(settings.getInt("a�o"+i,0),settings.getInt("mes"+i,0),settings.getInt("dia"+i,0),settings.getInt("horai"+i,0),settings.getInt("minutoi"+i,0)).getTime();
			Log.d("FECHA1",fechainicio.toString());
			Log.d("FECHA2",fechainicio2.toString());
			if (fechainicio2.compareTo(fechainicio) == 0){
			 ind = i;
		
		SharedPreferences reqst = getActivity().getSharedPreferences("TUMASC", 0);
		SharedPreferences.Editor editor = reqst.edit();
		for(i=ind;i<(index-1);i++){
			j = i+1;
			editor.putInt("a�o"+i,reqst.getInt("a�o"+j,0) );
			editor.putInt("mes"+i,reqst.getInt("mes"+j,0) );
			editor.putInt("dia"+i,reqst.getInt("dia"+j,0) );
			editor.putInt("horai"+i,reqst.getInt("horai"+j,0) );
			editor.putInt("minutoi"+i,reqst.getInt("minutoi"+j,0) );
		}
		index--;
		editor.putInt("index",index);
		editor.commit();
		}
		}
		}
	

    
	void UpdateParse(){

		Parse.initialize(getActivity(), "wf9TGHsYeTz8od557fQ9o9pRel5BNlOT9oZ4CpbH",
			    "2gKGXq41TDWhacnkA1YNH07mTKEI59bA7JlORu51");
		ParseQuery query = new ParseQuery("Citas");
		query.whereEqualTo("fechaInicial", fechainicio);

		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
		    	ParseObject object; 
		    	if (e == null) {
		    		
			    	if (cList.size() != 0){
			    		
			    		 object = cList.get(0);
		    			 object.put("aceptado", 5);
		    			 object.put("nuevo", 0);
		    	    	 object.saveInBackground();
			    }
			    	}else{
			    		Log.d("EXCEPCION PARSE", e.toString());
			    	}
	 }});
		
	}
	void EraseDataBase(){
		CitationDB db = new CitationDB(getActivity());
  	  	db.open();
  	  	db.delete(String.valueOf(fechainicio.getTime()));
  	  	db.close();
	}
	private void deleteCalendar(long startQueryUTC, long endQueryUTC, String title){
		// URI Builder
		Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
		            .buildUpon();
		ContentUris.appendId(eventsUriBuilder, startQueryUTC);
		ContentUris.appendId(eventsUriBuilder, endQueryUTC);
		Uri eventsDeleteUri = eventsUriBuilder.build();

		// Delete

		ContentResolver cr = getActivity().getContentResolver();

		try {
			Cursor c = cr.query(eventsDeleteUri, new String[] {CalendarContract.Instances.EVENT_ID}, null, null, null);
			c.moveToFirst();
			long eventID = c.getLong(0);
			c.close();
			Uri deleteUri = null;
			deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
			cr.delete(deleteUri, null, null);
		}

		catch(Exception e){

		Log.d("DELETE", "ERROR");

		}


		}
	
}

