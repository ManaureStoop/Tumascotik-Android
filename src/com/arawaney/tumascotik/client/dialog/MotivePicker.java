package com.arawaney.tumascotik.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.activity.SetDate;
import com.arawaney.tumascotik.client.activity.SetRequestDetails;
import com.arawaney.tumascotik.client.R;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;


@SuppressLint("ValidFragment")
public class MotivePicker extends DialogFragment {
	String[] motives;
	boolean flag;
	
	
	public MotivePicker(ArrayList<String> motives, int size){
	int i = 0;
		this.motives = new String [size];
	
	for (String string : motives) {
		this.motives[i] = string;
		i++;
	}
      
    }     
    
	
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.set_date_pick_motive);
//	    builder.setIcon(R.drawable.mascotiklogodialog);
	    builder.setItems(motives, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   SetRequestDetails callingActivity = (SetRequestDetails) getActivity();
	                   callingActivity.onUserSelectValue(which);
	           }
	    });
	    return builder.create();
	}
	
 
}
