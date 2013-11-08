package com.arawaney.tumascotik.client.dialog;

import com.arawaney.tumascotik.client.activity.Presupuesto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.arawaney.tumascotik.client.R;


public class PresupuestoDeleteallDialog extends DialogFragment  {
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Estimado CLiente");
	    builder.setMessage("El contenido de la lista de presupuestos se borrara");
	    builder.setIcon(R.drawable.mascotiklogodialog);
	 // Add the buttons
	    builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Presupuesto callingActivity = (Presupuesto) getActivity();
	    		callingActivity.DeleteAll();
	               }
	           });
	   
	    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
  
	            	  dialog.cancel();
	               }
	           });
	    return builder.create();

	}

}
