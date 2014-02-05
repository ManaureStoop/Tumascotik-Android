package com.arawaney.tumascotik.client.dialog;

import java.util.ArrayList;
import java.util.List;

import com.arawaney.tumascotik.client.activity.BudgetActivity;
import com.arawaney.tumascotik.client.activity.ViewRequests;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.BudgetDB;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Service;
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
import android.telephony.ServiceState;
import android.util.Log;

@SuppressLint("ValidFragment")
public class PresupuestoDialog extends DialogFragment {
	
	ArrayList<Service> budgetServices;
	List<String>  serviceNames;
	ArrayList<Service> selectedServices;
	ProgressDialog progressDialog;
	boolean looking ;
	Context context;

	
	public PresupuestoDialog(Context ctxt) {
		context=ctxt;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		serviceNames = new ArrayList<String>();
		selectedServices = new ArrayList<Service>();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		budgetServices = ServiceProvider.readNotRequest(context); 
	
		
		if (budgetServices == null) {
			Log.d("BudgetDialog", "No budget items");
		}
		
		for (Service service : budgetServices) {
			String name = service.getName();
			serviceNames.add(name);
		}
		
		final CharSequence[] charSequenceItems = serviceNames.toArray(new CharSequence[serviceNames.size()]);
		
	    builder.setMultiChoiceItems(charSequenceItems, null, 
	    		new DialogInterface.OnMultiChoiceClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which,
                 boolean isChecked) {
             if (isChecked) {
                 // If the user checked the item, add it to the selected items
            	 addToSelected(which);
                 Log.d("Multiplechoice", "Se elejio "+budgetServices.get(which).getName());
             } else if (selectedServices.contains(budgetServices.get(which))) {
                 // Else, if the item is already in the array, remove it 
            	 selectedServices.remove(Integer.valueOf(which));
                 Log.d("Multiplechoice", "Se quito "+budgetServices.get(which).getName());
             }
         }


     });
	    
	    // Add the buttons
	    builder.setPositiveButton(context.getResources().getString(R.string.budget_dialog_ok), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	    insertServices();
						BudgetActivity callingActivity = (BudgetActivity) getActivity();
						callingActivity.refreshView();
	      
	               }
	           });
	   
	    builder.setNegativeButton(context.getResources().getString(R.string.budget_dialog_cancel), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   dialog.cancel();   
	               }
	           });
	    return builder.create();
	}
	
	private void addToSelected(int which) {
		selectedServices.add(budgetServices.get(which));
		
	}
	
	private void insertServices() {
		Budget savedBudget = BudgetProvider.readInWorkBudget(context);
		
		if (savedBudget == null) {
			savedBudget = new Budget();
			savedBudget.setActive(Budget.ACTIVE);
			savedBudget.setDelivery(Budget.IS_DELIVERY);
			savedBudget.setStatus(Budget.STATUS_WORKING);
			savedBudget.setUserId(MainController.USER.getSystemId());
			long id = BudgetProvider.insertBudget(context, savedBudget);
			savedBudget.setId(id);
		}
		for (Service service : selectedServices) {
			savedBudget.addService(service);
		}
		
		BudgetProvider.updateBudget(context, savedBudget);
		
	}
	
}
