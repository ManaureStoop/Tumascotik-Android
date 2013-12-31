package com.arawaney.tumascotik.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;


public class Datosmascota  extends Activity  {

	EditText mascota;
	Button siguiente;
	Button cancelar;
	Button anterior;
	Spinner motivos;
	Spinner razas;
	Spinner especie;
	
	EditText editEscribir;
	ArrayAdapter<CharSequence> RazaAdapter;
	ArrayAdapter<CharSequence> RazaAdapter1;
	ArrayAdapter<CharSequence> MotivosAdapter;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
			
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_datosmascota);

			
			mascota = (EditText) findViewById(R.id.etxtmascotadatmasc);
	    	siguiente = (Button) findViewById(R.id.bsigpedirc);
	        motivos = (Spinner) findViewById(R.id.spinmotivodatmasc);
	        razas = (Spinner) findViewById(R.id.spinrazadatmasc);
	        especie = (Spinner) findViewById(R.id.spinespeciedatmasc);
	        
	
	        RazaAdapter1 = ArrayAdapter.createFromResource(Datosmascota .this, R.array.Trans, android.R.layout.simple_spinner_item);   
	        razas.setAdapter(RazaAdapter1);
	        
			if(getIntent().getStringExtra("motivos")!=null){
				
				MotivosAdapter = ArrayAdapter.createFromResource(Datosmascota.this,R.array.Presupuesto, android.R.layout.simple_spinner_item);   
				motivos.setAdapter(MotivosAdapter);
				motivos.setEnabled(false);
			}

	        
	        siguiente = (Button) findViewById(R.id.bsigdatmasc);
	        siguiente.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					 if (Checkfields()){
					 Intent i = new Intent(Datosmascota.this, SendRequest.class);
			
					  i.putExtra("nombre", getIntent().getStringExtra("nombre"));
					  i.putExtra("direccion", getIntent().getStringExtra("direccion"));
					  i.putExtra("ptoref", getIntent().getStringExtra("ptoref"));
					  i.putExtra("telf", getIntent().getStringExtra("telf"));
					  i.putExtra("telm", getIntent().getStringExtra("telm"));
					  i.putExtra("horai", getIntent().getIntExtra("horai", 0));
					  i.putExtra("horaf", getIntent().getIntExtra("horaf", 0));
					  i.putExtra("minutoi",getIntent().getIntExtra("minutoi", 0));
					  i.putExtra("minutof", getIntent().getIntExtra("minutof", 0));
					  i.putExtra("preciocaro", getIntent().getIntExtra("preciocaro", 0));
					  i.putExtra("ano",getIntent().getIntExtra("ano", 0));
					  i.putExtra("mes",getIntent().getIntExtra("mes", 0));
					  i.putExtra("dia",getIntent().getIntExtra("dia", 0));
					  
					  if(motivos.getSelectedItem().toString().equals("Presupuesto"))
						  i.putExtra("motivo",getIntent().getStringExtra("motivos"));
						  
					  else
						  i.putExtra("motivo", motivos.getSelectedItem().toString());
					  
					  i.putExtra("raza", razas.getSelectedItem().toString());
					  i.putExtra("especie", especie.getSelectedItem().toString());
					  i.putExtra("mascota", mascota.getText().toString());
					  
						  
					  startActivityForResult(i, 1);
					 }
					  
				}
			});
		
			cancelar = (Button) findViewById(R.id.bcancdatmasc);
			cancelar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					setResult(RESULT_CANCELED, returnIntent);        
					finish();
					
				}
			});
			anterior = (Button) findViewById(R.id.bantdatmasc);
			anterior.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					setResult(RESULT_OK, returnIntent);        
					finish();
					
				}
			});
		 especie.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String selected = arg0.getSelectedItem().toString();
				Log.d("LOGCLICK",selected);
				
				
				if(selected.equals("--Elija--")) {
					RazaAdapter = ArrayAdapter.createFromResource(Datosmascota.this, R.array.Trans, android.R.layout.simple_spinner_item);   
		        }       
		         else if(selected.equals("Perro")){		                
		               RazaAdapter = ArrayAdapter.createFromResource(Datosmascota.this, R.array.Perros, android.R.layout.simple_spinner_item);   
		              
		           }    
		           else if(selected.equals("Gato")){
		               RazaAdapter = ArrayAdapter.createFromResource(Datosmascota.this, R.array.Gatos, android.R.layout.simple_spinner_item);   
		               
		           }
		           else if(selected.equals("Otro")){
		               RazaAdapter = ArrayAdapter.createFromResource(Datosmascota.this, R.array.Otros, android.R.layout.simple_spinner_item);   
		               
		           }
				razas.setAdapter(RazaAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		}


		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_menu, menu);
			return true;
		}

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {

			  if (requestCode == 1) {

			     if(resultCode == RESULT_OK){      
			    	// It means client pressed Anterior button. We dont do anything      
			     }
			     if (resultCode == RESULT_CANCELED) {    
			    	 //It means client pressed cancelar or a request was made. we want to go menu activity.
			    	 Intent returnIntent = new Intent();
						setResult(RESULT_CANCELED, returnIntent);        
						finish();
			     }
			  }
			}//onActivityResult

		// Checks if every field is filled by the user. If there is one blank-field the user will be notified
		// when clicking next
		boolean Checkfields(){
			String opciona1="Disculpe, pero el campo ";
			String opcion2="";
			String opciona3=" se encuentra incompleto.";
			String opcionb1="Disculpe, pero los campos ";
			String opcionb3=" se encuentran incompletos.";
			boolean ready = true;
			int conteo = 0;
			
			if(mascota.getText().length()==0){
				conteo++;
				opcion2 = opcion2+" Nombre de Mascota,";
				ready = false;
			}
			if(motivos.getSelectedItem().toString().equals("--Elija--")){
				conteo++;
				opcion2 = opcion2+" Motivo,";
				ready = false;
			}
			if(razas.getSelectedItem().toString().equals("--Elija--")){
				conteo++;
				opcion2 = opcion2+" Raza,";
				ready = false;
			}
			if(especie.getSelectedItem().toString().equals("--Elija--")){
				conteo++;
				opcion2 = opcion2+" Especie,";
				ready = false;
			}
			if (!ready){
				 if (conteo > 1){
					 Toast toast = Toast.makeText(Datosmascota.this, opcionb1+opcion2+opcionb3 , 15000);
			    	 toast.setGravity(Gravity.CENTER, 0, 0);    	
			    	 toast.show();}
				 else {
					 Toast toast = Toast.makeText(Datosmascota.this, opciona1+opcion2+opciona3 , 15000);
			    	 toast.setGravity(Gravity.CENTER, 0, 0);    	
			    	 toast.show();
				 }
			}
			return ready;
		}

	


}
