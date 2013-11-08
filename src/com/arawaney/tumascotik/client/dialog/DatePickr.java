package com.arawaney.tumascotik.client.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


public class DatePickr extends DialogFragment {
	public final static String EXTRA_MESSAGE = "com.example.pickerapplication.MESSAGE";
	Handler hDate;
   
	
	public DatePickr(Handler arg_h){
        hDate = arg_h;      
    }     
    

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of TimePickerDialog and return it
		DatePickerDialog dpdlog = new DatePickerDialog(getActivity(), callback, year, month, day);
		return dpdlog;
		}

	
	private OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day){
			// Do something with the time chosen by the user'
			Message msg = new Message();
			Bundle data = new Bundle();
			int[] date = new int[3];
			date[0] = year;
			date[1] = month;
			date[2] = day;
			
			data.putIntArray(EXTRA_MESSAGE, date);
			msg.setData(data);
			hDate.sendMessage(msg);
		}
	};

}
