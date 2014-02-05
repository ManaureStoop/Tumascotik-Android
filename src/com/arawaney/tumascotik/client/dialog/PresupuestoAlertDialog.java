package com.arawaney.tumascotik.client.dialog;

import com.arawaney.tumascotik.client.activity.BudgetActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.arawaney.tumascotik.client.R;



@SuppressLint("ValidFragment")
public class PresupuestoAlertDialog extends DialogFragment {
	
	int price;

	public PresupuestoAlertDialog(int price){
		this.price = price;
		
    }  
	
    	
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(getResources().getString(R.string.budget_dialog_send_title));
	    builder.setMessage(getResources().getString(R.string.budget_dialog_send_body)+price+"?");
	    builder.setPositiveButton(getResources().getString(R.string.budget_dialog_send), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	BudgetActivity callingActivity = (BudgetActivity) getActivity();
	    		callingActivity.sendBudget();
	               }
	           });
	   
	    builder.setNegativeButton(getResources().getString(R.string.budget_dialog_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
  
	            	  dialog.cancel();
	               }
	           });
	    return builder.create();

	}

	
}	
	


