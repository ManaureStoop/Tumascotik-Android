package com.arawaney.tumascotik.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.activity.Presupuesto;
import com.arawaney.tumascotik.client.db.BudgetDB;
import com.arawaney.tumascotik.client.R;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

@SuppressLint("ValidFragment")
public class PresupuestoDialog extends DialogFragment {
	
	private ArrayList mSelectedItems;
	ProgressDialog progressDialog;
	boolean looking ;
	Context thiscontxt;

	
	public PresupuestoDialog(Context ctxt) {
		thiscontxt=ctxt;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Parse.initialize(getActivity(), "wf9TGHsYeTz8od557fQ9o9pRel5BNlOT9oZ4CpbH",
			    "2gKGXq41TDWhacnkA1YNH07mTKEI59bA7JlORu51");
	    looking = false;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Elija ");
	    builder.setIcon(R.drawable.mascotiklogodialog);
	 // Where we track the selected items
	    mSelectedItems = new ArrayList();  
	    builder.setMultiChoiceItems(R.array.Presupuestos, null,
                new DialogInterface.OnMultiChoiceClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which,
                 boolean isChecked) {
             if (isChecked) {
                 // If the user checked the item, add it to the selected items
                 mSelectedItems.add(which);
                 Log.d("Multiplechoice", "Se elejio"+String.valueOf(which));
             } else if (mSelectedItems.contains(which)) {
                 // Else, if the item is already in the array, remove it 
                 mSelectedItems.remove(Integer.valueOf(which));
                 Log.d("Multiplechoice", "Se quito"+String.valueOf(which));
             }
         }
     });
	    
	    // Add the buttons
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   CreateDatabase();

	               }
	           });
	   
	    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   dialog.cancel();
		        
	               }
	           });
	    return builder.create();
	}
	void CreateDatabase(){
		int nselected = mSelectedItems.size(); 
		if (nselected!=0){
			Log.d("Entro a create", "Hay "+String.valueOf(nselected));
			looking = true;
			progressDialog = ProgressDialog.show(getActivity(), "Tumascotik", "Descargando precios...");
			int i = 0;
			String[] Presupuestos = getResources().getStringArray(R.array.Presupuestos);
			String title;
			final String lastitem = Presupuestos[(Integer) mSelectedItems.get(nselected-1)];
			Log.d("Entro a create", "Ultimo: "+lastitem);
			for (i=0; i<=(nselected-1);i++){
				title = Presupuestos[(Integer) mSelectedItems.get(i)];
				Log.d("Entro a create", "titulo: "+title);
				ParseQuery query = new ParseQuery("Presupuestos");
				query.whereEqualTo("Peticion", title);
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> cList, ParseException e) {
				    	ParseObject object; 
				    	if (e == null) {
					    	if (cList.size() != 0){
					    		object = cList.get(0);
					    		Log.d("Encontro en create",object.getString("Peticion")+" "+object.getInt("Precio"));
					    		BudgetDB db = new BudgetDB(thiscontxt);
					    		db.open();
					    		db.insert(object.getString("Peticion"), object.getInt("Precio"));
					    		db.close();
								  if (object.getString("Peticion").equals(lastitem)){	
									    Log.d("IGUALES", "match!");
									    progressDialog.dismiss();
								    	looking = false;
						            	Presupuesto callingActivity = (Presupuesto) thiscontxt;
							            callingActivity.Refresh();
								    	}
					    	}
				    		else
				    			 Log.d("ERRORRRR", "No hay actividad");
	
				    	} else {
				           Log.d("ERRORRRR", "Error en createdatabase");
				        }
	
					    	}
				});

			}
		}
  }
}
