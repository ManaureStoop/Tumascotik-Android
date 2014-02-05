package com.arawaney.tumascotik.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.activity.SetDate;
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
public class TimePicker extends DialogFragment {
	String[] hours;
	int[] preciocaro;
	boolean flag;
	
	
	public TimePicker(String[] horas,int[] precioscaros, int size){
		int i;
		hours = new String [size];
		preciocaro = new int [size];
		for(i=0;i<size;i++){
			preciocaro[i]=precioscaros[i];
			if(preciocaro[i]==1)
			hours[i]=horas[i]+" *";
			else
			hours[i]=horas[i];
		}
      
    }     
    
	
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.set_date_pick_time_block);
//	    builder.setIcon(R.drawable.mascotiklogodialog);
	    builder.setItems(hours, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	  SetDate callingActivity = (SetDate) getActivity();
	                   callingActivity.onUserSelectValue(which);
	           }
	    });
	    return builder.create();
	}
	
 
}
