package com.arawaney.tumascotik.client.dialog;

import com.arawaney.tumascotik.client.activity.Presupuesto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.arawaney.tumascotik.client.R;



@SuppressLint("ValidFragment")
public class PresupuestoAlertDialog extends DialogFragment {
	
	String motivos;
	int price;

	public PresupuestoAlertDialog(String motv, int pric){
		motivos=motv;
		price = pric;
		
    }  
	
    	
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Estimado CLiente");
	    builder.setMessage("�Desea solicitar una cita con su veterinario para realizar:  "+motivos+" por un total de "+ String.valueOf(price)+"BsF?. ");
	    builder.setIcon(R.drawable.mascotiklogodialog);
	 // Add the buttons
	    builder.setPositiveButton("Pedir Cita", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Presupuesto callingActivity = (Presupuesto) getActivity();
	    		callingActivity.MakeAppointment("Si");
	               }
	           });
	   
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
  
	            	  dialog.cancel();
	               }
	           });
	    return builder.create();

	}

	
}	
	


