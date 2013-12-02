package com.arawaney.tumascotik.client.dialog;
import com.arawaney.tumascotik.client.ClientMainActivity;
import com.arawaney.tumascotik.client.R;


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
				
				ClientMainActivity callingActivity = (ClientMainActivity) getActivity();
				callingActivity.appointmentCall(which);
				
			}
		});
		return builder.create();
	
	}

}


