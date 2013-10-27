package com.arawaney.tumascotik.dialog;
import com.arawaney.tumascotik.Menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class SelectFragmentDialog extends DialogFragment{
	
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.mascotiklogodialog);
		builder.setTitle(R.string.verCitas);
		builder.setItems(R.array.Citas, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Menu callingActivity = (Menu) getActivity();
				callingActivity.appointmentCall(which);
				
			}
		});
		return builder.create();
	
	}

}


