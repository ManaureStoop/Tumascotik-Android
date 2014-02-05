package com.arawaney.tumascotik.client.dialog;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.arawaney.tumascotik.client.activity.BudgetActivity;
import com.arawaney.tumascotik.client.db.BudgetDB;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.R;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@SuppressLint("ValidFragment")
public class PresupuestoItemDialog extends DialogFragment {
	
	BudgetService budgetService;
	Context context;  
	long budgetid;

	public PresupuestoItemDialog(BudgetService budgetService, Context context,  long budgetid){
		this.budgetService = budgetService;
		this.context = context;  
		this.budgetid = budgetid;
    }     
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	

	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(budgetService.getService().getName()+" : Bs."+budgetService.getPrice());
	    builder.setPositiveButton(getResources().getString(R.string.budget_dialog_ok), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.cancel();
	               }
	           });
	   
	    builder.setNegativeButton(getResources().getString(R.string.budget_dialog_delete_from_list), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {    	   
	            	   BudgetProvider.removeBudgetService(context, budgetid, budgetService.getService().getSystem_id());
		               BudgetActivity callingActivity = (BudgetActivity) getActivity();
		               callingActivity.refreshView();
	               }
	           });
	    return builder.create();
	}
	
		}
	


