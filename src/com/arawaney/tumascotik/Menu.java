package com.arawaney.tumascotik;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.arawaney.tumascotik.activity.Pedircita;
import com.arawaney.tumascotik.activity.Presupuesto;
import com.arawaney.tumascotik.activity.SetDate;
import com.arawaney.tumascotik.activity.VerCitas;
import com.arawaney.tumascotik.db.CitationDB;
import com.arawaney.tumascotik.dialog.ConnectionDialog;
import com.arawaney.tumascotik.dialog.SelectFragmentDialog;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;


public class Menu extends FragmentActivity {
	Date fechainicio;
	ProgressDialog progressDialog;
	SharedPreferences savedpendingappoint;
	int pendingApointments;
	
	// To know if the function "resetrequest" is already running. If so it can be called again.
	boolean bussy;
	// To know if the app has internet connection via wifi
	boolean wifi ;
	// To know if the refreshed was called from the logo button or was made automatic
	boolean calledfromlogobuton ;
	// To know if the app has internet connection via mobile data
	boolean mobiledata ;
	
	Button pedirc;
	Button vercit;
	Button pedirpres;
	Button youtube;
	Button facebook;
	Button logorefresh;
	Button twitter;
	Button emergencia;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Removing activity title from Layout
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		bussy = false;
		calledfromlogobuton = false;
		wifi = false;
		mobiledata= false;
		pedirc = (Button) findViewById(R.id.pdrcitamenu);
		vercit = (Button) findViewById(R.id.bvercitasmenu);
		pedirpres = (Button) findViewById(R.id.bpedirpresmenu);
		facebook = (Button) findViewById(R.id.bfacebookmenu);
	    twitter = (Button) findViewById(R.id.btwittermenu);
		youtube = (Button) findViewById(R.id.byoutubemenu);
		logorefresh = (Button) findViewById(R.id.blogorefreshmenu); 
		emergencia = (Button) findViewById(R.id.bemergenciamenu); 
		
		//reset_DataBase_SharedP();
		
		// Getting Shared Preferences of data saved in App
		savedpendingappoint = getSharedPreferences("TUMASC", 0);
		getNumberOfPendingAppointments(savedpendingappoint);

		
		pedirc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(Menu.this, SetDate.class);
				startActivity(i);
			}
		});

		
		vercit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showAppointmentFragmentDialog(v);
			}
		});
		pedirpres.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Menu.this, Presupuesto.class);
				startActivity(i);
			}
		});
		youtube.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=XIMprgXfrCo")));
			}
		});
		
		
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(getOpenFacebookIntent(Menu.this));
			}
		});
		
		twitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(getOpenTwitterIntent(Menu.this));
			}
		});
		logorefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (pendingApointments!=0){
				progressDialog = ProgressDialog.show(Menu.this, "Tumascotik", "Actualizando citas...");
				calledfromlogobuton = true;
				loadPendingObjects();}
				
			}
		});
		emergencia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent callIntent = new Intent(Intent.ACTION_CALL);
				 callIntent.setData(Uri.parse("tel:04142564227"));
				 startActivity(callIntent);
				
			}
		});
	}

	private void getNumberOfPendingAppointments(SharedPreferences settings) {
		//Get number off Pending Appointments 
		pendingApointments = settings.getInt("index",0);
		Log.d("INDICE EN SHARED = ", String.valueOf(pendingApointments));
	}

	private void reset_DataBase_SharedP() {
		//Chages the Data Base deleting certain object and resets the number of pending Appointments		
		CitationDB db = new CitationDB(this);
		db.open();
		db.delete("1367955000000");
		db.updateINIDATE(1);
		db.close();
		SharedPreferences reqst = getSharedPreferences("TUMASC", 0);
		SharedPreferences.Editor editor = reqst.edit();
		editor.putInt("index",0);
		editor.commit();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, (android.view.Menu) menu);
		return true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		checkingInternetConnections();
	}
	
	public void resetSavedPendings (int ind) {
	int i;
	int j;
	
	waitTurnToModify();
	
	//taking turn to modify
	bussy = true;
	
	SharedPreferences.Editor editor = savedpendingappoint.edit();
	for(i=ind;i<(pendingApointments-1);i++){
		j = i+1;
		editor.putInt("a�o"+i,savedpendingappoint.getInt("a�o"+j,0) );
		editor.putInt("mes"+i,savedpendingappoint.getInt("mes"+j,0) );
		editor.putInt("dia"+i,savedpendingappoint.getInt("dia"+j,0) );
		editor.putInt("horai"+i,savedpendingappoint.getInt("horai"+j,0) );
		editor.putInt("minutoi"+i,savedpendingappoint.getInt("minutoi"+j,0) );
	}
	pendingApointments--;
	editor.putInt("index",pendingApointments);
	editor.commit();
	bussy = false;
	}

	private void waitTurnToModify() {
		//wait if other process is reseting pending appointments
		if(bussy){
		while (bussy){
			
		}}
	}
	
	
	public void refresh(int ind){
	//Does the Parse Query to see if accepted or rejected. 1 means accepted and goes
	//Automatically to 4. A 2 means rejected and it gets erased
	
		final int in = ind;
		ParseQuery query = new ParseQuery("Citas");
		query.whereEqualTo("fechaInicial", fechainicio);
		query.findInBackground(new FindCallback() {
	    public void done(List<ParseObject> cList, ParseException e) {
	    	ParseObject object; 
	    	if (e == null) {
		    	if (cList.size() != 0){
		    		object = cList.get(0); 
		    		if (object.getNumber("aceptado") == (Number)1){
		    	    	 
		    			 notifyUser(true,object.getString("mascota"));
		    			
		    			 writeCalendar(object.getString("mascota"), object.getDate("fechaInicial"), object.getDate("fechaFinal"));
		    			
		    			 // Passes "Cita" from pending to accepted in database
		    			 UpdateDataBase(true,object.getDate("fechaInicial"));
		    	    	 // Puts "Cita" in Parse from 1 to 4
		    			 object.put("aceptado", 4);
		    	    	 object.saveInBackground();
		    	    	 
		    	    	  }
		    	      else if (object.getNumber("aceptado")== (Number)2) {
		    	    	 
		    	    	  notifyUser(false,object.getString("mascota"));
		    	    	
		    	    	  // Erase "Cita" from database
		    	    	  UpdateDataBase(false,object.getDate("fechaInicial"));
		    	    	  
		    	    	  // Delete "Cita" from calender in case it was already accepted
		    	    	  if (object.getNumber("nuevo") != (Number)0){
		    	    		  deleteCalendar(fechainicio.getTime(),object.getDate("fechaFinal").getTime(),"Cita con Tumascotik para "+object.getString("mascota"));  
		    	    	  }
		    	    	  // Erase "Cita" from Parse
		    	    	 object.deleteInBackground();
		    	    	
		    	    	 //// Erase "Cita" from Shared Preferences
		    	    	 resetSavedPendings(in);}
		    	      else if (object.getNumber("aceptado") == (Number)0||object.getNumber("aceptado") == (Number)4)
		    	    	  Log.d("Sigue igual", "Sigue igual");
		    		}
	    		else
	    			 Log.d("ERROR", "No hay actividad");
		    	
		    	
		    	
	    	} else {
	           Log.d("ERROR", "Error en refresh!");
	        }
	        
	    	if ( calledfromlogobuton){
				progressDialog.dismiss();
				calledfromlogobuton = false;
				
			}
	    }
	   
	});




}
	public void loadPendingObjects(){
		//Loads every object pending to see if accepted or rejected
		int index;
		pendingApointments = savedpendingappoint.getInt("index",0);
		for(index=0;index<pendingApointments;index++){
			fechainicio = new GregorianCalendar(savedpendingappoint.getInt("a�o"+index,0),savedpendingappoint.getInt("mes"+index,0),savedpendingappoint.getInt("dia"+index,0),savedpendingappoint.getInt("horai"+index,0),savedpendingappoint.getInt("minutoi"+index,0)).getTime();
			Log.d("fechaINICIO", fechainicio.toString());
			refresh (index);		
	}
	
}

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)

	public void notifyUser(boolean vet_accepted,String petsname){
	String toasttext;
	String notificationtitle;
	String notificationcontent;
	Class tapclass;
	if (vet_accepted){
		 toasttext = "Cita para "+ petsname + " con Tumascotik Aceptada!";
		 notificationtitle = "Cita para "+petsname+" aceptada";
		 notificationcontent = "La cita fue agregada en su Agenda";  
		 tapclass = VerCitas.class;
		 
	}
	else{
		 toasttext = "Su veterinario no puede atender a "+petsname;
		 notificationtitle = "Cita para "+petsname+" rechazada";
		 notificationcontent = "Disculpe, por favor elija una nueva cita";
		 tapclass = Pedircita.class;
		
	}
	
	showToastStatusBarNotif(toasttext, notificationtitle,notificationcontent, tapclass);  	
	
}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showToastStatusBarNotif(String toasttext,
		String notificationtitle, String notificationcontent, Class tapclass) {
	// Generating Toast notification
	  Toast toast = Toast.makeText(Menu.this,  toasttext, 8000);
	  toast.setGravity(Gravity.CENTER, 0, 0);
	  toast.show();
	// Generating Status Bar notification
	  	 NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(notificationtitle)
			        .setContentText(notificationcontent)
			        .setSound(Uri.parse("android.resource://com.example.tumascotikmenu/"+R.raw.bark));
 //Add event to
	  	Intent resultIntent = new Intent(this, tapclass);
	  	resultIntent.putExtra("seleccion","ACEPTADAS");
	  	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	  	stackBuilder.addParentStack(Pedircita.class);
	  	stackBuilder.addNextIntent(resultIntent);
	  	PendingIntent resultPendingIntent =
	  	        stackBuilder.getPendingIntent(
	  	            0,
	  	            PendingIntent.FLAG_UPDATE_CURRENT
	  	        );
	  	mBuilder.setContentIntent(resultPendingIntent);
	  	 NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				
		mNotificationManager.notify(0, mBuilder.build());
}
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void writeCalendar(String mascota, Date fechai, Date fechaf){
	//Writes Appointment on the Calendar
		String datebegin = String.valueOf(fechai.getTime());	
		String dateend = String.valueOf(fechaf.getTime());
		String es = "Cita con Tumascotik para "+mascota;		
		try {
			ContentValues eventvalue = new ContentValues();
			writeEvent(datebegin, dateend, es, eventvalue);
			Uri event = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eventvalue);
			setupAlarm(event,eventvalue);
			
		} catch (Exception e) {
			Log.e("ERROR","error writing to calendar", e);
		};
		}

	private void writeEvent(String datebegin, String dateend, String es,
			ContentValues eventvalue) {
		eventvalue.put(CalendarContract.Events.TITLE, es);
		eventvalue.put(CalendarContract.Events.DTSTART, datebegin);
		eventvalue.put(CalendarContract.Events.DTEND, dateend);
		eventvalue.put(CalendarContract.Events.CALENDAR_ID, 1);
		eventvalue.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
	}

	private void setupAlarm(Uri event,ContentValues eventvalue) {
		// Setting up Alarm 15 minutes before appointment
		eventvalue = new ContentValues();
		eventvalue.put(Reminders.MINUTES, 15);
		eventvalue.put(Reminders.EVENT_ID,  Long.parseLong(event.getLastPathSegment()));
		eventvalue.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri =  getContentResolver().insert(Reminders.CONTENT_URI, eventvalue);
	}

	void UpdateDataBase(boolean accepted, Date fechai){
	CitationDB db = new CitationDB(this);
	  db.open();
	if(accepted){
		Log.d("UPDATEDATA","ACEPTAR");
		db.update(String.valueOf(fechai.getTime()), 1);}
	else{
		Log.d("UPDATEDATA","BORRAR");
		db.delete(String.valueOf(fechai.getTime()));}
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

		ContentResolver cr =this.getContentResolver();

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

	public void showAppointmentFragmentDialog(View v){
	
	DialogFragment newFragment = new SelectFragmentDialog();
	newFragment.show(getFragmentManager(), "appointment");
	
}
	public void appointmentCall(int selection){
		String select;
		if (selection == 0)
			select = "ACEPTADAS";
		else 
			select = "PENDIENTES";
		
		Intent vercitasIntent = new Intent(Menu.this, VerCitas.class);
		vercitasIntent.putExtra("seleccion", select);
		startActivity(vercitasIntent);
		
	}

	public static Intent getOpenFacebookIntent(Context context) {

	   try {
	    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
	    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100000796583747"));
	   } catch (Exception e) {
	    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tumascotik"));
	   }
	}
	
	public static Intent getOpenTwitterIntent(Context context) {
	Intent intent;
	try {
		intent = new Intent(Intent.ACTION_VIEW,
		    Uri.parse("twitter://user?user_id=289100088"));
		

		}catch (Exception e) {
		  intent = new Intent(Intent.ACTION_VIEW,
		         Uri.parse("https://twitter.com/#!/[tumascotik]")); 
		} 
     return intent;
	}
	public void checkingInternetConnections(){
	ConnectivityManager conma = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	wifi = conma.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting(); 
	mobiledata = conma.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting(); 
	if(wifi || mobiledata){
		Parse.initialize(this, "wf9TGHsYeTz8od557fQ9o9pRel5BNlOT9oZ4CpbH",
			    "2gKGXq41TDWhacnkA1YNH07mTKEI59bA7JlORu51");
		loadPendingObjects();
        pedirc.setEnabled(true);
        twitter.setEnabled(true);
        facebook.setEnabled(true);
        youtube.setEnabled(true);
        logorefresh.setEnabled(true);
        pedirpres.setEnabled(true);
        
		
	}
		
	else{
        pedirc.setEnabled(false);
        twitter.setEnabled(false);
        facebook.setEnabled(false);
        youtube.setEnabled(false);
        logorefresh.setEnabled(false);
        pedirpres.setEnabled(false);
        DialogFragment newFragment = new ConnectionDialog();
        newFragment.show(getFragmentManager(), "connections");
		
	}
		
}
}
