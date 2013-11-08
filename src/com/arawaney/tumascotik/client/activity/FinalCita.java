package com.arawaney.tumascotik.client.activity;

import java.util.Date;
import java.util.GregorianCalendar;
import com.arawaney.tumascotik.client.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import com.arawaney.tumascotik.client.db.CitationDB;
import com.parse.ParseObject;

public class FinalCita  extends Activity {
	ParseObject Objeto;
	Button enviar;
	Button cancelar;
	Button anterior;
	Date fechainicio;
	Date fechafinal;
	TextView resumen;
	int index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);	
	
		SharedPreferences set = getSharedPreferences("TUMASC", 0);
		index = set.getInt("index",0);
    Objeto  = new ParseObject("Citas");
	
	
	final int año = 	getIntent().getIntExtra("ano", 0) ;
	final int mes = 	getIntent().getIntExtra("mes", 0) ;
	final int dia = 	getIntent().getIntExtra("dia", 0) ; 
	final int horainicial = getIntent().getIntExtra("horai", 0);
	final int horafinal = 	getIntent().getIntExtra("horaf", 0);
	final int minutoinicial=getIntent().getIntExtra("minutoi", 0);
	final int minutofinal = getIntent().getIntExtra("minutof", 0);
	final int preciocaro = getIntent().getIntExtra("preciocaro", 0);
	
	final String nombre = 	getIntent().getStringExtra("nombre");
	final String mascota = 	getIntent().getStringExtra("mascota") ;
	final String motivo = 	getIntent().getStringExtra("motivo") ;
	final String telefonof = getIntent().getStringExtra("telf");
	final String direccion = 	getIntent().getStringExtra("direccion");
	final String ptoref = 	getIntent().getStringExtra("ptoref") ;
	final String raza = 	getIntent().getStringExtra("raza") ;
	final String especie = 	getIntent().getStringExtra("especie") ;
	final String telefonom = getIntent().getStringExtra("telm");
	

	fechainicio = new GregorianCalendar(año,mes,dia,horainicial,minutoinicial).getTime();
	fechafinal = new GregorianCalendar(año,mes,dia,horafinal,minutofinal).getTime();
    
	resumen = (TextView) findViewById(R.id.txtresumnfinal);
	String resum = "Estimado "+ nombre+" la cita de "+ mascota +" para "+motivo+" se pautara para el 0"+dia+"/0"+mes+" desde las "+horainicial+":"+minutoinicial+" hasta las "+horafinal+":"+minutofinal+". Por favor espere la respuesta de su veterinario.";
	resumen.setText(resum);
	enviar = (Button)findViewById(R.id.benviarfinal);

	enviar.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d("ENVIO","ENVÍO");
			// Uploading data to parse
			Objeto.put("nombre", nombre );
			Objeto.put("direccion", direccion );
			Objeto.put("puntoReferencia",ptoref);
			Objeto.put("telefonoFijo",telefonof);
			Objeto.put("telefonoMovil",telefonom);
			Objeto.put("mascota", mascota);
			Objeto.put("raza",raza);
			Objeto.put("especie",especie);
			Objeto.put("motivo",motivo);  	
			Objeto.put("fechaInicial",fechainicio); 
			Objeto.put("fechaFinal",fechafinal); 
			Objeto.put("aceptado",0);
			Objeto.put("nuevo",0); 
			Objeto.put("precio",preciocaro);
			Objeto.saveInBackground();
			
			
			// Saving pending data on the cellphone so it wont get erased when the aplication closes
			SharedPreferences reqst = getSharedPreferences("TUMASC", 0);
			SharedPreferences.Editor editor = reqst.edit();
			editor.putInt("año"+index, año);
			editor.putInt("mes"+index, mes);
			editor.putInt("dia"+index, dia);
			editor.putInt("horai"+index, horainicial);
			editor.putInt("minutoi"+index,minutoinicial);
			index++;
			editor.putInt("index",index);
			editor.commit();
			
			Savedatabase(nombre,mascota,motivo,telefonom,especie);
			
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);        
			finish();
		}
	});
	
	
	anterior = (Button) findViewById(R.id.bantfinal);
	anterior.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d("ANTERIOR","ECHO PA ATRAS");
			 Intent returnIntent = new Intent();
			 setResult(RESULT_OK,returnIntent);     
			 finish();
		}
	});
	cancelar = (Button) findViewById(R.id.bcancfinal);
	cancelar.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d("CANCELADO","CANC");
			Intent returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);        
			finish();
			
		}
	});
	}
	
void Savedatabase( String nombre, String mascota, String motivo,String telefono, String especie){
	CitationDB db = new CitationDB(this);
	  db.open();
	  db.insert(nombre, mascota, motivo, telefono, String.valueOf(fechainicio.getTime()), String.valueOf(fechafinal.getTime()),especie);
	  db.close();
}
}

