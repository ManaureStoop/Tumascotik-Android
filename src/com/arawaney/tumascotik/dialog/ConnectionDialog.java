package com.arawaney.tumascotik.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConnectionDialog extends DialogFragment {
    
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.mascotiklogodialog);
        builder.setTitle("Alerta!");
        builder.setMessage("No se ha detectado ninguna conexi�n  a internet en su equipo. Para realizar citas o conectarse a las redes sociales necesitar� de internet. ")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
