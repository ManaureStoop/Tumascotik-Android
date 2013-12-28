package com.arawaney.tumascotik.client.activity;



import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.arawaney.tumascotik.client.dialog.DatePickr;
import com.arawaney.tumascotik.client.dialog.TimePicker;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.arawaney.tumascotik.client.R;


public class SetDate extends FragmentActivity {
	int size;
	int size2;
	Date fechainicio;
	Button fecha;
	Button siguiente;
	Button cancelar;
	Button hora;
	TextView elijahora;
	TextView tDate;
	TextView alerta;
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	String[] bloques;
	int[] preciocaros;
	Date[]fechasinicio;
	Date[]fechasfinal;
	Date fechadiainicio;
	Date fechadiafinal;
	int[] minutoi;
	int[] minutof;
	int[] horai;
	int[] horaf;
	int ind;
	
	Handler hDate = new Handler() {
		@Override
		public void handleMessage(Message msg){
		
		int[] date = msg.getData().getIntArray(DatePickr.EXTRA_MESSAGE);
		year = date[0];
		month = date[1];
		day = date[2];
		
		String y = String.valueOf(year);
		String mo = String.valueOf(month + 1);
		String d = String.valueOf(day);
		
		tDate.setText(d + " / " + mo + " / " + y);
		fechadiainicio = new GregorianCalendar(year,month,day,0, 0).getTime();
		fechadiafinal  = new GregorianCalendar(year,month,day,23,59).getTime();
		
		sethours();
		hora.setClickable(true);
		hora.setEnabled(true);
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setdate);
		Parse.initialize(this, "wf9TGHsYeTz8od557fQ9o9pRel5BNlOT9oZ4CpbH",
			    "2gKGXq41TDWhacnkA1YNH07mTKEI59bA7JlORu51");
		
		fecha = (Button) findViewById(R.id.bfechasetd);
		siguiente = (Button) findViewById(R.id.bsigsetd);
		hora = (Button) findViewById(R.id.bhorasetd);
		hora.setEnabled(false);
		hora.setClickable(false);
		elijahora =(TextView) findViewById(R.id.txthorasetd);
		elijahora.setText(R.string.main_porfelija);
		tDate = (TextView) findViewById(R.id.txtfechasetd);
		alerta = (TextView) findViewById(R.id.txtalertasetd);
		alerta.setGravity(Gravity.CENTER);
	
		fecha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				showDatePickerDialog(v);	
				
			}
		});
	
		 hora.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    writehours(); 
				DialogFragment newFragment = new TimePicker(bloques,preciocaros,size2);
				newFragment.show(getSupportFragmentManager(), "hora");
				
			}
		});
	siguiente.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				  if(Checkfields())	 {
					Intent i = new Intent(SetDate.this,Pedircita.class);
					  i.putExtra("horai", horai[ind]);
					  i.putExtra("horaf", horaf[ind]);
					  i.putExtra("minutoi",minutoi[ind]);
					  i.putExtra("minutof", minutof[ind]);
					  i.putExtra("preciocaro", preciocaros[ind]);

					  i.putExtra("ano",year);
					  i.putExtra("mes",month);
					  i.putExtra("dia",day);
					  i.putExtra("motivos", getIntent().getStringExtra("motivos"));
					  startActivityForResult(i, 2);
					  }

				}
			});
	cancelar = (Button) findViewById(R.id.bcancsetd);
	cancelar.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);        
			finish();
			
		}
	});

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 2) {

		     if(resultCode == RESULT_OK){      
		           // It means client pressed Anterior button. We dont do anything  
		     }
		     if (resultCode == RESULT_CANCELED) {    
		    	 //It means client pressed cancelar or a request was made. we want to go menu activity.
		    	 SetDate.this.finish();
		     }
		  }
		}//onActivityResult

	public void sethours() {
		
		final ProgressDialog progressDialog = ProgressDialog.show(SetDate.this, "", "Cargando disponibilidad...");
		
		ParseQuery query = new ParseQuery("Citas");
		query.whereGreaterThan("fechaInicial", fechadiainicio);
		query.whereLessThan("fechaInicial",fechadiafinal);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {
			    
		    	if (e == null) {
		           int i;
		           ParseObject parseobject;
		           size = cList.size();
		           bloques = new String[20];
		           preciocaros = new int[20];
		           minutoi = new int[20];
		           minutof= new int[20];
		           horai= new int[20];
		           horaf= new int[20];
		           
		           
		           if (size!= 0){
		        	 fechasinicio = new Date [size];
		        	 fechasfinal = new Date [size];
			           for(i=0;i<size;i++){
			        	   parseobject = cList.get(i);
			        	   fechasinicio[i] = parseobject.getDate("fechaInicial");
			        	   fechasfinal[i] = parseobject.getDate("fechaFinal");
			        	  
			           }
		           }
		           for(i=0;i<size;i++){
		        	   Log.d("EPAAA", String.valueOf(fechasinicio[i].toString()));
		        	   Log.d("EPAAA", String.valueOf(fechasfinal[i].toString()));
		        	  
		           }
		            progressDialog.dismiss(); 
			       	hora.setClickable(true);
			    	hora.setEnabled(true );
			    	elijahora.setText(R.string.main_elijhora);
		        } else {
		           Log.d("ERRORRRR", "Error: " + e.getMessage());
		        }
		    }
		   
		});
	   	
	    
}


	
	public void writehours() {
		int i;
		int j = 0;
	
		long min;
		boolean flag = false;
		boolean flag2= false;
		boolean searmo = false;
		boolean block = false;
		//Setting Hour Limitations
		Date fechabase = new GregorianCalendar(year,month,day,0,0).getTime();
		Date fechabasen = new GregorianCalendar(year,month,day,0,0).getTime();
		Date iniciohorario = new GregorianCalendar(year,month,day,7,30).getTime();
		Date finhorario = new GregorianCalendar(year,month,day,18,0).getTime();
	    Date fechatope = new GregorianCalendar(year,month,day,23,30).getTime();		
	    Date mediodia =  new GregorianCalendar(year,month,day,12,0).getTime();
	    Date despuesdalmuerzo =  new GregorianCalendar(year,month,day,13,30).getTime();
	   //Possible end of hour-block
	    Date trans;
	    
	    // While the times doesnt exceeds the last hour-block
	    while(fechabase.before(fechatope)){	
	    	trans = new Date(fechabase.getTime()+(90*60*1000));//Added 1.5 Hours
	        min = 60*60*1000;
	        searmo = false;
	        block = false;
	     // If there is an activity or appointment already in the first hour block
	     //the list will start  at the end of this hour-block
	        for(i=0;i<size;i++){
	        	
	        	if(trans.after(fechasfinal[i])&& fechabase.before(fechasfinal[i])){
		    		flag2=true;
		    		break;
		    	}
	        }
	        
	        if(flag2){
	        	fechabasen = fechasfinal[i];
	        	flag2 = false;
	        	Log.d("AJA01", "AQUI01");
	        }
		  // If the activity or appointment starts inside the lunch block 
		  //the hour block will start  at the end of the lunch block
	      /*else if(trans.after(despuesdalmuerzo)&& fechabase.before(despuesdalmuerzo)){
	    		fechabasen = despuesdalmuerzo;	
	    		Log.d("AJA02", "AQUI02");
	    	}*/
	       
	    	else{
	        
	        	
	        	//Check if any activity gets in the  way
	    	  for(i=0;i<size;i++){
	        	 if ((fechasinicio[i].before(trans))&&(fechasinicio[i].after(fechabase)||fechasinicio[i].equals(fechabase))){
	        		 Log.d("AJA03", "AQUI03");
	        		 flag = true;
	        		// If difference between block start and next activity is more than one hour
	        		if(fechasinicio[i].getTime() - fechabase.getTime() >= (60*60*1000)){ 
	        			    Log.d("AJA04", "AQUI04");
	        			    min = fechasinicio[i].getTime() - fechabase.getTime();
		        			GregorianCalendar cal1 = new GregorianCalendar();
		        			cal1.setTime(fechabase);
		        			GregorianCalendar cal2 = new GregorianCalendar();
		        			cal2.setTime(fechasinicio[i]);
		        			//If the hour block is outside  the normal workschedule it muss notify the user
		        			if((fechabase.after(finhorario)||fechabase.equals(finhorario))||(fechabase.before(iniciohorario))||((fechabase.before(despuesdalmuerzo))&&(fechabase.after(mediodia)||fechabase.equals(mediodia)))){
		        				preciocaros[j]= 1;
		        			}
		        			else{
		        				preciocaros[j]= 0;
		        			}
	        				bloques[j] =android.text.format.DateFormat.format("h:mmaa", fechabase)+" - "+android.text.format.DateFormat.format("h:mmaa", fechasinicio[i]);
   
	    	        		   minutoi[j] =cal1.get(Calendar.MINUTE) ;
	    			           minutof[j]= cal2.get(Calendar.MINUTE);
	    			           horai[j]= cal1.get(Calendar.HOUR_OF_DAY);
	    			           horaf[j]= cal2.get(Calendar.HOUR_OF_DAY);
	
		        			Log.d("AJA05", bloques[j]);
		        			j++;
		        			searmo = true;
		        		}
	        		else{
	        		block = true;
	        		}
	        		fechabasen = fechasfinal[i];	
	        	 }
	        		   
	        				    		
	    	  }
	    	  /*
	    	 // Check if the Lunch time gets in the way
	    	// If block goes in between the lunch time
	    	  if(trans.after(mediodia)&& (fechabase.before(mediodia)||fechabase.equals(mediodia))){ 
	        			flag = true;
	        			Log.d("AJA06", "AQUI06");
	        		   // If difference is more than one hour
	        			if(mediodia.getTime() - fechabase.getTime() >= (60*60*1000)){
	        				Log.d("AJA07", "AQUI07");
	        		   // If there was an activity on the way and the new block was created 
	        			if (searmo){
	        				if (mediodia.getTime() - fechabase.getTime() < min){
	        					j--;
	        					GregorianCalendar cal1 = new GregorianCalendar();
		    	        		cal1.setTime(fechabase);
		    	        		GregorianCalendar cal2 = new GregorianCalendar();
		    	        		cal2.setTime(mediodia);
		    	        			
		    	        		bloques[j] = "                     "+String.valueOf(cal1.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(cal1.get(Calendar.MINUTE))+" - "+ String.valueOf(cal2.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(cal2.get(Calendar.MINUTE));
		    			           minutoi[j] =cal1.get(Calendar.MINUTE) ;
		    			           minutof[j]= cal2.get(Calendar.MINUTE);
		    			           horai[j]= cal1.get(Calendar.HOUR_OF_DAY);
		    			           horaf[j]= cal2.get(Calendar.HOUR_OF_DAY);       			
		    	        		Log.d("AJA08", bloques[j]);
		    	        		j++;
	        				}
	        			  }
	        			  else {
	        				// If there wasnt an activity on the way
	        				  if(!block){
	        				  GregorianCalendar cal1 = new GregorianCalendar();
		    	        		cal1.setTime(fechabase);
		    	        		GregorianCalendar cal2 = new GregorianCalendar();
		    	        		cal2.setTime(mediodia);
		    	        			
		    	        		bloques[j] = "                     "+String.valueOf(cal1.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(cal1.get(Calendar.MINUTE))+" - "+ String.valueOf(cal2.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(cal2.get(Calendar.MINUTE));
		    			           minutoi[j] =cal1.get(Calendar.MINUTE) ;
		    			           minutof[j]= cal2.get(Calendar.MINUTE);
		    			           horai[j]= cal1.get(Calendar.HOUR_OF_DAY);
		    			           horaf[j]= cal2.get(Calendar.HOUR_OF_DAY);       			
		    	        		Log.d("AJA09", bloques[j]);
		    	        		j++;}
	        			  }
	        			  }
	        			else{
	          				if (searmo)
	          					j--;
	          				
	          			}
	          			
	        			fechabasen = despuesdalmuerzo;	
	        		}
	        		*/
	    	  // Check if the Last hour-block possible time gets in the way
	
	    	  if((trans.after(fechatope)&& (fechabase.before(fechatope)||fechabase.equals(fechatope)))){ 
      			flag = true;
      		//// If difference is more than one hour
      			if(fechatope.getTime() - fechabase.getTime() >= (60*60*1000)){
      				 if (searmo){
	      				if (fechatope.getTime() - fechabase.getTime() < min){
	      					j--;
	      					GregorianCalendar cal1 = new GregorianCalendar();
		    	        		cal1.setTime(fechabase);
		    	        		GregorianCalendar cal2 = new GregorianCalendar();
		    	        		cal2.setTime(fechatope);
		    	        		
			        			//If the hour block is outside  the normal workschedule it muss notify the user
		    	       			if((fechabase.after(finhorario)||fechabase.equals(finhorario))||(fechabase.before(iniciohorario))||((fechabase.before(despuesdalmuerzo))&&(fechabase.after(mediodia)||fechabase.equals(mediodia)))){
			        				preciocaros[j]= 1;
			        			}
			        			else{
			        				preciocaros[j]= 0;
			        			}
		        				bloques[j] =android.text.format.DateFormat.format("h:mmaa", fechabase)+" - "+android.text.format.DateFormat.format("h:mmaa", fechatope);

		    			           
		    	        		
		    	        		   minutoi[j] =cal1.get(Calendar.MINUTE) ;
		    			           minutof[j]= cal2.get(Calendar.MINUTE);
		    			           horai[j]= cal1.get(Calendar.HOUR_OF_DAY);
		    			           horaf[j]= cal2.get(Calendar.HOUR_OF_DAY);       			
		    	        		Log.d("AJA09", bloques[j]);
		    	        		j++;
	      				}
      				 }
      				 else{
      					 if(!block){
      					GregorianCalendar cal1 = new GregorianCalendar();
    	        		cal1.setTime(fechabase);
    	        		GregorianCalendar cal2 = new GregorianCalendar();
    	        		cal2.setTime(fechatope);
    	        			
	        			//If the hour block is outside  the normal workschedule it muss notify the user
    	       			if((fechabase.after(finhorario)||fechabase.equals(finhorario))||(fechabase.before(iniciohorario))||((fechabase.before(despuesdalmuerzo))&&(fechabase.after(mediodia)||fechabase.equals(mediodia)))){
	        				preciocaros[j]= 1;
	        			}
	        			else{
	        				preciocaros[j]= 0;
	        			}
        				bloques[j] =android.text.format.DateFormat.format("h:mmaa", fechabase)+" - "+android.text.format.DateFormat.format("h:mmaa", fechatope);
 
    	        		
    	        		   minutoi[j] =cal1.get(Calendar.MINUTE) ;
    			           minutof[j]= cal2.get(Calendar.MINUTE);
    			           horai[j]= cal1.get(Calendar.HOUR_OF_DAY);
    			           horaf[j]= cal2.get(Calendar.HOUR_OF_DAY);       			
    	        		Log.d("AJA10", bloques[j]);
    	        		j++;
      				 }
      				 	}
      				
      			}
      			else{
      				if (searmo){
      					j--;
      				}
      			}
      			fechabasen = fechatope;	
      		}
	        // IF nothing interfiers in the new block.		
	        if (!flag){
	        		GregorianCalendar cal1 = new GregorianCalendar();
	        		cal1.setTime(fechabase);
	        		GregorianCalendar cal2 = new GregorianCalendar();
	        		cal2.setTime(trans);
	        			
        			//If the hour block is outside  the normal workschedule it muss notify the user
	       			if((fechabase.after(finhorario)||fechabase.equals(finhorario))||(fechabase.before(iniciohorario))||((fechabase.before(despuesdalmuerzo))&&(fechabase.after(mediodia)||fechabase.equals(mediodia)))){        				
        				preciocaros[j]= 1;
        			}
        			else{
        				preciocaros[j]= 0;
        			}
    				bloques[j] =android.text.format.DateFormat.format("h:mmaa", fechabase)+" - "+android.text.format.DateFormat.format("h:mmaa", trans);

			           minutoi[j] =cal1.get(Calendar.MINUTE) ;
			           minutof[j]= cal2.get(Calendar.MINUTE);
			           horai[j]= cal1.get(Calendar.HOUR_OF_DAY);
			           horaf[j]= cal2.get(Calendar.HOUR_OF_DAY);       			
	        		
			           Log.d("AJA11", bloques[j]);
	        		j++;
	        		fechabasen = trans;
	        }
	    }
	      
	      fechabase = fechabasen;
	      flag = false;
	    }
	    
		
	    size2 = j;
		
	}
	
	public void onUserSelectValue(int index) {
		elijahora.setText(bloques[index]);
		ind = index;
		if(preciocaros[index]==1)
			alerta.setText(R.string.Alerta_Precios);
		else
			alerta.setText("");
		}
	//Function to show the Date Picker
	public void showDatePickerDialog(View v) {
	DialogFragment newFragment = new DatePickr(hDate);
    newFragment.show(getSupportFragmentManager(), "datePicker");
 
    }
	
	@Override
	
	//Function to reset button properties whenever the programm goes on pause.
	protected void onPause() {
		super.onPause();
	}
	boolean Checkfields(){
		String opciona1="Disculpe, pero el campo ";
		String opcion2="";
		String opciona3=" se encuentra incompleto.";
		String opcionb1="Disculpe, pero los campos ";
		String opcionb3=" se encuentran incompletos.";
		boolean ready = true;
		int conteo = 0;
		if(tDate.getText().equals("Elija Fecha" )){
			conteo++;
			opcion2 = opcion2+" Fecha,";
			ready = false;
			Log.d("Entroo1o","");
		}
		if(elijahora.getText().equals("Por favor elija primero la fecha")){
			conteo++;
			opcion2 = opcion2+" Hora,";
			ready = false;
			Log.d("Entroo2o","");
		}
		if (!ready){
			 if (conteo > 1){
				 Toast toast = Toast.makeText(SetDate.this, opcionb1+opcion2+opcionb3 , 15000);
		    	 toast.setGravity(Gravity.CENTER, 0, 0);    	
		    	 toast.show();}
			 else {
				 Toast toast = Toast.makeText(SetDate.this, opciona1+opcion2+opciona3 , 15000);
		    	 toast.setGravity(Gravity.CENTER, 0, 0);    	
		    	 toast.show();
			 }
		}
		return ready;
	}
}
