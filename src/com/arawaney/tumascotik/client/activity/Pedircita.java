package com.arawaney.tumascotik.client.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.arawaney.tumascotik.client.R;


public class Pedircita  extends Activity  {

	EditText nombre;
	EditText direccion;
	EditText ptoref;
	EditText telf;
	EditText telm;
	EditText editEscribir;
	Button siguiente;
	Button cancelar;
	Button anterior;	
	


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE); 
			
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_pedircita);

			
			nombre = (EditText) findViewById(R.id.etxtnombrepedirc);
			direccion = (EditText) findViewById(R.id.etxtdireccionpedirc);
			ptoref = (EditText) findViewById(R.id.etxtptoreferenciapedirc);
			telf = (EditText) findViewById(R.id.etxttlffpedirc);
			telm = (EditText) findViewById(R.id.etxttlfmpedirc);
	    	siguiente = (Button) findViewById(R.id.bsigpedirc);
	       
	        
	        siguiente.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					 if (Checkfields()){
					 Intent i = new Intent(Pedircita.this, Datosmascota.class);
			

					  i.putExtra("horai", getIntent().getIntExtra("horai", 0));
					  i.putExtra("horaf", getIntent().getIntExtra("horaf", 0));
					  i.putExtra("minutoi",getIntent().getIntExtra("minutoi", 0));
					  i.putExtra("minutof", getIntent().getIntExtra("minutof", 0));
					  i.putExtra("preciocaro", getIntent().getIntExtra("preciocaro", 0));
					  i.putExtra("ano",getIntent().getIntExtra("ano", 0));
					  i.putExtra("mes",getIntent().getIntExtra("mes", 0));
					  i.putExtra("dia",getIntent().getIntExtra("dia", 0));
					  i.putExtra("motivos", getIntent().getStringExtra("motivos"));
					  
					  i.putExtra("nombre", nombre.getText().toString());
					  i.putExtra("direccion", direccion.getText().toString());
					  i.putExtra("ptoref", ptoref.getText().toString());
					  i.putExtra("telm", telm.getText().toString());
					  i.putExtra("telf", telf.getText().toString());
						  
					  startActivityForResult(i, 1);
					 }
					  
				}
			});
		
			cancelar = (Button) findViewById(R.id.bcancpedirc);
			cancelar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Pedircita.this.finish();
					
				}
			});
			anterior = (Button) findViewById(R.id.bantpedirc);
			anterior.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					setResult(RESULT_OK, returnIntent);        
					finish();
					
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
			
			if(nombre.getText().length()==0){
				conteo++;
				opcion2 = opcion2+" Nombre,";
				ready = false;
			}
			if(direccion.getText().length()==0){
				conteo++;
				opcion2 = opcion2+" Direccion,";
				ready = false;
			}
			if(ptoref.getText().length()==0){
				conteo++;
				opcion2 = opcion2+" Punto de Referencia,";
				ready = false;
			}
			if(telf.getText().length()==0){
				conteo++;
				opcion2 = opcion2+" Telefono Fijo,";
				ready = false;
			}
			if(telm.getText().length()==0){
				conteo++;
				opcion2 = opcion2+" Telefono M�vil,";
				ready = false;
			}
			
			if (!ready){
				 if (conteo > 1){
					 Toast toast = Toast.makeText(Pedircita.this, opcionb1+opcion2+opcionb3 , 15000);
			    	 toast.setGravity(Gravity.CENTER, 0, 0);    	
			    	 toast.show();}
				 else {
					 Toast toast = Toast.makeText(Pedircita.this, opciona1+opcion2+opciona3 , 15000);
			    	 toast.setGravity(Gravity.CENTER, 0, 0);    	
			    	 toast.show();
				 }
			}
			return ready;
		}

	


}
