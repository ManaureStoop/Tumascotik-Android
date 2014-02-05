package com.arawaney.tumascotik.client.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.dialog.DatePickr;
import com.arawaney.tumascotik.client.dialog.TimePicker;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.util.CalendarUtil;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SetDate extends FragmentActivity implements ParseRequestListener {
	private static final String LOG_TAG = "Tumascotik-Client-SetDateActivity";
	int numberOfScheduledAppointments;
	int numberOfCreatedTimeBlocks;
	Date initialDate;
	Button dateButton;
	ImageView forward;
	ImageView cancelar;
	Button timeButton;
	TextView pickedTime;
	TextView pickedDate;
	TextView alerta;
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	String[] timeBlocks;
	int[] expensiveBlocks;
	Date[] initialScheduledDates;
	Date[] finalScheduledDates;
	Date dayInitialDate;
	Date dayFinalDate;
	int[] minutei;
	int[] minutef;
	int[] houri;
	int[] hourf;
	int ind;
	int duration;
	int minDuration;
	
	ProgressDialog progressDialog;
	

	Request request;

	Handler hDate = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			int[] date = msg.getData().getIntArray(DatePickr.EXTRA_MESSAGE);
			year = date[0];
			month = date[1];
			day = date[2];

			Calendar pickedCalendar = Calendar.getInstance();
			pickedCalendar.set(year, month, day);

			pickedDate.setText(CalendarUtil.getDateFormated(pickedCalendar,
					"dd/MM/yyyy"));

			dayInitialDate = new GregorianCalendar(year, month, day, 0, 0)
					.getTime();
			dayFinalDate = new GregorianCalendar(year, month, day, 23, 59)
					.getTime();

			getScheduledTimeBlocks();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setdate);

		ParseProvider.initializeParse(this);

		loadRequest();

		loadViews();

		loadButtons();

	}



	private void loadRequest() {
		request = MainController.getREQUEST();
		
	}



	private void loadButtons() {
		dateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDatePickerDialog(v);

			}
		});

		timeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				writehours();
				DialogFragment newFragment = new TimePicker(timeBlocks,
						expensiveBlocks, numberOfCreatedTimeBlocks);
				newFragment.show(getSupportFragmentManager(), "hora");

			}
		});
		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Checkfields()) {
					Intent i = new Intent(SetDate.this, SendRequest.class);
					startActivity(i);
				}

			}
		});
		cancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);
				finish();

			}
		});
	}

	private void loadViews() {
		dateButton = (Button) findViewById(R.id.bfechasetd);
		forward = (ImageView) findViewById(R.id.bsigsetd);
		cancelar = (ImageView) findViewById(R.id.bcancsetd);
		timeButton = (Button) findViewById(R.id.bhorasetd);
		timeButton.setEnabled(false);
		timeButton.setClickable(false);
		pickedTime = (TextView) findViewById(R.id.txthorasetd);
		pickedDate = (TextView) findViewById(R.id.txtfechasetd);
		alerta = (TextView) findViewById(R.id.txtalertasetd);
		alerta.setVisibility(View.GONE);

		setFonts();

	}

	private void setFonts() {
		dateButton.setTypeface(FontUtil
				.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		timeButton.setTypeface(FontUtil
				.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		pickedTime
				.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		pickedDate
				.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		alerta.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));

	}

	public void getScheduledTimeBlocks() {

		 progressDialog = ProgressDialog.show(
				SetDate.this,
				"",
				getResources().getString(
						R.string.set_date_dialog_loading_timeblocks));

		timeBlocks = new String[100];
		expensiveBlocks = new int[100];
		minutei = new int[100];
		minutef = new int[100];
		houri = new int[100];
		hourf = new int[100];

		ParseRequestProvider.geRequestByDay(dayInitialDate, dayFinalDate, this);
	}
	
	private void enableTimeButton() {
		timeButton.setClickable(true);
		timeButton.setEnabled(true);
		pickedTime.setText(R.string.set_date_time_text);
	}

	public void writehours() {
		int i;
		int j = 0;

		long min;
		boolean isBlockBetween = false;
		boolean appointmentAtFirstBLock = false;
		boolean blockCreated = false;
		boolean block = false;

		// Setting Hour Limitations
		Date cursorDate = new GregorianCalendar(year, month, day, 0, 0)
				.getTime();
		Date cursorDateEn = new GregorianCalendar(year, month, day, 0, 0)
				.getTime();
		Date beginningWorkingDay = new GregorianCalendar(year, month, day, 7,
				30).getTime();
		Date endWorkingDay = new GregorianCalendar(year, month, day, 18, 0)
				.getTime();
		Date maximumDate = new GregorianCalendar(year, month, day, 23, 30)
				.getTime();
		Date midDay = new GregorianCalendar(year, month, day, 12, 0).getTime();
		Date afterLunch = new GregorianCalendar(year, month, day, 13, 30)
				.getTime();

		// Possible end of hour-block
		Date auxFinalDate;

		// While the times doesnt exceeds the last hour-block
		while (cursorDate.before(maximumDate)) {
			
			loadDuration();
			
			auxFinalDate = new Date(cursorDate.getTime() + duration);// Added service duration
			
			min = minDuration; // minimum block is 1 hour
			blockCreated = false;
			block = false;
			// If there is an activity or appointment already in the first hour
			// block
			// the list will start at the end of this hour-block
			for (i = 0; i < numberOfScheduledAppointments; i++) {

				if (auxFinalDate.after(finalScheduledDates[i])
						&& cursorDate.before(finalScheduledDates[i])) {
					Log.d(LOG_TAG, "Test true raro");
					appointmentAtFirstBLock = true;
					break;
				}
			}

			if (appointmentAtFirstBLock) {
				cursorDateEn = finalScheduledDates[i];
				appointmentAtFirstBLock = false;
				Log.d(LOG_TAG + " ALGORITH ",
						"There is a time block at the beginning of the day");
			}
			// If the activity or appointment starts inside the lunch block
			// the hour block will start at the end of the lunch block
			/*
			 * else if(trans.after(despuesdalmuerzo)&&
			 * fechabase.before(despuesdalmuerzo)){ fechabasen =
			 * despuesdalmuerzo; Log.d("AJA02", "AQUI02"); }
			 */

			else {

				// Check if any activity gets in the way
				for (i = 0; i < numberOfScheduledAppointments; i++) {
					if ((initialScheduledDates[i].before(auxFinalDate))
							&& (initialScheduledDates[i].after(cursorDate) || initialScheduledDates[i]
									.equals(cursorDate))) {
						Log.d(LOG_TAG + " ALGORITH ",
								"A previews Time Block gets in the way");
						isBlockBetween = true;
						// If difference between block start and next activity
						// is more than one hour
						if (initialScheduledDates[i].getTime()
								- cursorDate.getTime() >= (minDuration)) {
							Log.d(LOG_TAG + " ALGORITH ",
									"Time between begginings more than minimum");
							min = initialScheduledDates[i].getTime()
									- cursorDate.getTime();
							GregorianCalendar cal1 = new GregorianCalendar();
							cal1.setTime(cursorDate);
							GregorianCalendar cal2 = new GregorianCalendar();
							cal2.setTime(initialScheduledDates[i]);

							// If the hour block is outside the normal
							// workschedule it muss notify the user
							if ((cursorDate.after(endWorkingDay) || cursorDate
									.equals(endWorkingDay))
									|| (cursorDate.before(beginningWorkingDay))
									|| ((cursorDate.before(afterLunch)) && (cursorDate
											.after(midDay) || cursorDate
											.equals(midDay)))) {
								expensiveBlocks[j] = 1;
							} else {
								expensiveBlocks[j] = 0;
							}

							timeBlocks[j] = android.text.format.DateFormat
									.format("h:mmaa", cursorDate)
									+ " - "
									+ android.text.format.DateFormat.format(
											"h:mmaa", initialScheduledDates[i]);

							minutei[j] = cal1.get(Calendar.MINUTE);
							minutef[j] = cal2.get(Calendar.MINUTE);
							houri[j] = cal1.get(Calendar.HOUR_OF_DAY);
							hourf[j] = cal2.get(Calendar.HOUR_OF_DAY);

							Log.d(LOG_TAG + " ALGORITH ", "TimeBlock created: "
									+ timeBlocks[j]);
							j++;
							blockCreated = true;
						} else {
							block = true;
						}
						cursorDateEn = finalScheduledDates[i];
					}

				}
				/*
				 * // Check if the Lunch time gets in the way // If block goes
				 * in between the lunch time if(trans.after(mediodia)&&
				 * (fechabase.before(mediodia)||fechabase.equals(mediodia))){
				 * flag = true; Log.d("AJA06", "AQUI06"); // If difference is
				 * more than one hour if(mediodia.getTime() -
				 * fechabase.getTime() >= (60*60*1000)){ Log.d("AJA07",
				 * "AQUI07"); // If there was an activity on the way and the new
				 * block was created if (searmo){ if (mediodia.getTime() -
				 * fechabase.getTime() < min){ j--; GregorianCalendar cal1 = new
				 * GregorianCalendar(); cal1.setTime(fechabase);
				 * GregorianCalendar cal2 = new GregorianCalendar();
				 * cal2.setTime(mediodia);
				 * 
				 * bloques[j] =
				 * "                     "+String.valueOf(cal1.get(Calendar
				 * .HOUR_OF_DAY
				 * ))+":"+String.valueOf(cal1.get(Calendar.MINUTE))+" - "+
				 * String
				 * .valueOf(cal2.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf
				 * (cal2.get(Calendar.MINUTE)); minutoi[j]
				 * =cal1.get(Calendar.MINUTE) ; minutof[j]=
				 * cal2.get(Calendar.MINUTE); horai[j]=
				 * cal1.get(Calendar.HOUR_OF_DAY); horaf[j]=
				 * cal2.get(Calendar.HOUR_OF_DAY); Log.d("AJA08", bloques[j]);
				 * j++; } } else { // If there wasnt an activity on the way
				 * if(!block){ GregorianCalendar cal1 = new GregorianCalendar();
				 * cal1.setTime(fechabase); GregorianCalendar cal2 = new
				 * GregorianCalendar(); cal2.setTime(mediodia);
				 * 
				 * bloques[j] =
				 * "                     "+String.valueOf(cal1.get(Calendar
				 * .HOUR_OF_DAY
				 * ))+":"+String.valueOf(cal1.get(Calendar.MINUTE))+" - "+
				 * String
				 * .valueOf(cal2.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf
				 * (cal2.get(Calendar.MINUTE)); minutoi[j]
				 * =cal1.get(Calendar.MINUTE) ; minutof[j]=
				 * cal2.get(Calendar.MINUTE); horai[j]=
				 * cal1.get(Calendar.HOUR_OF_DAY); horaf[j]=
				 * cal2.get(Calendar.HOUR_OF_DAY); Log.d("AJA09", bloques[j]);
				 * j++;} } } else{ if (searmo) j--;
				 * 
				 * }
				 * 
				 * fechabasen = despuesdalmuerzo; }
				 */
				// Check if the Last hour-block possible time gets in the way

				if ((auxFinalDate.after(maximumDate) && (cursorDate
						.before(maximumDate) || cursorDate.equals(maximumDate)))) {
					isBlockBetween = true;
					// // If difference is more than one hour
					if (maximumDate.getTime() - cursorDate.getTime() >= (minDuration)) {
						if (blockCreated) {
							if (maximumDate.getTime() - cursorDate.getTime() < min) {
								j--;
								GregorianCalendar cal1 = new GregorianCalendar();
								cal1.setTime(cursorDate);
								GregorianCalendar cal2 = new GregorianCalendar();
								cal2.setTime(maximumDate);

								// If the hour block is outside the normal
								// workschedule it muss notify the user
								if ((cursorDate.after(endWorkingDay) || cursorDate
										.equals(endWorkingDay))
										|| (cursorDate
												.before(beginningWorkingDay))
										|| ((cursorDate.before(afterLunch)) && (cursorDate
												.after(midDay) || cursorDate
												.equals(midDay)))) {
									expensiveBlocks[j] = 1;
								} else {
									expensiveBlocks[j] = 0;
								}
								timeBlocks[j] = android.text.format.DateFormat
										.format("h:mmaa", cursorDate)
										+ " - "
										+ android.text.format.DateFormat
												.format("h:mmaa", maximumDate);

								minutei[j] = cal1.get(Calendar.MINUTE);
								minutef[j] = cal2.get(Calendar.MINUTE);
								houri[j] = cal1.get(Calendar.HOUR_OF_DAY);
								hourf[j] = cal2.get(Calendar.HOUR_OF_DAY);
								Log.d("AJA09", timeBlocks[j]);
								j++;
							}
						} else {
							if (!block) {
								GregorianCalendar cal1 = new GregorianCalendar();
								cal1.setTime(cursorDate);
								GregorianCalendar cal2 = new GregorianCalendar();
								cal2.setTime(maximumDate);

								// If the hour block is outside the normal
								// workschedule it muss notify the user
								if ((cursorDate.after(endWorkingDay) || cursorDate
										.equals(endWorkingDay))
										|| (cursorDate
												.before(beginningWorkingDay))
										|| ((cursorDate.before(afterLunch)) && (cursorDate
												.after(midDay) || cursorDate
												.equals(midDay)))) {
									expensiveBlocks[j] = 1;
								} else {
									expensiveBlocks[j] = 0;
								}
								timeBlocks[j] = android.text.format.DateFormat
										.format("h:mmaa", cursorDate)
										+ " - "
										+ android.text.format.DateFormat
												.format("h:mmaa", maximumDate);

								minutei[j] = cal1.get(Calendar.MINUTE);
								minutef[j] = cal2.get(Calendar.MINUTE);
								houri[j] = cal1.get(Calendar.HOUR_OF_DAY);
								hourf[j] = cal2.get(Calendar.HOUR_OF_DAY);
								Log.d("AJA10", timeBlocks[j]);
								j++;
							}
						}

					} else {
						if (blockCreated) {
							j--;
						}
					}
					cursorDateEn = maximumDate;
				}
				// IF nothing interfiers in the new block.
				if (!isBlockBetween) {
					GregorianCalendar cal1 = new GregorianCalendar();
					cal1.setTime(cursorDate);
					GregorianCalendar cal2 = new GregorianCalendar();
					cal2.setTime(auxFinalDate);

					// If the hour block is outside the normal workschedule it
					// muss notify the user
					if ((cursorDate.after(endWorkingDay) || cursorDate
							.equals(endWorkingDay))
							|| (cursorDate.before(beginningWorkingDay))
							|| ((cursorDate.before(afterLunch)) && (cursorDate
									.after(midDay) || cursorDate.equals(midDay)))) {
						expensiveBlocks[j] = 1;
					} else {
						expensiveBlocks[j] = 0;
					}
					timeBlocks[j] = android.text.format.DateFormat.format(
							"h:mmaa", cursorDate)
							+ " - "
							+ android.text.format.DateFormat.format("h:mmaa",
									auxFinalDate);

					minutei[j] = cal1.get(Calendar.MINUTE);
					minutef[j] = cal2.get(Calendar.MINUTE);
					houri[j] = cal1.get(Calendar.HOUR_OF_DAY);
					hourf[j] = cal2.get(Calendar.HOUR_OF_DAY);

					Log.d("AJA11", timeBlocks[j]);
					j++;
					cursorDateEn = auxFinalDate;
				}
			}

			cursorDate = cursorDateEn;
			isBlockBetween = false;
		}

		numberOfCreatedTimeBlocks = j;

	}

	private void loadDuration() {
		request = MainController.getREQUEST();
		int durationInHours = request.getService().getDuration();
		
		duration = durationInHours* 60 *60 * 1000;
		minDuration = ((durationInHours* 60 *60 * 1000)*3)/4;
		
	}

	public void onUserSelectValue(int index) {
		pickedTime.setText(timeBlocks[index]);
		ind = index;
		setRequestDates();
		if (expensiveBlocks[index] == 1)
			alerta.setVisibility(View.VISIBLE);
		else
			alerta.setVisibility(View.GONE);
	}

	private void setRequestDates() {

		Calendar start_date = Calendar.getInstance();
		Calendar finish_date = Calendar.getInstance();

		start_date.set(year, month, day, houri[ind], minutei[ind], 0);
		finish_date.set(year, month, day, hourf[ind], minutef[ind], 0);

		request.setStart_date(start_date);
		request.setFinish_date(finish_date);

		MainController.setREQUEST(request);

	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickr(hDate);
		newFragment.show(getSupportFragmentManager(), "datePicker");

	}

	@Override
	// Function to reset button properties whenever the programm goes on pause.
	protected void onPause() {
		super.onPause();
	}

	boolean Checkfields() {

		boolean ready = true;

		String messagePiece1 = getResources().getString(
				R.string.set_date_pick_checkfields_message1);
		String messagePiece2 = "";
		String messagePiece3 = getResources().getString(
				R.string.set_date_pick_checkfields_message3);
		String messagePiece4 = getResources().getString(
				R.string.set_date_pick_checkfields_message4);
		String messagePiece5 = getResources().getString(
				R.string.set_date_pick_checkfields_message5);

		int conteo = 0;
		if (pickedDate.getText().equals(
				getResources().getString(R.string.set_date_date_text))) {
			conteo++;
			messagePiece2 = messagePiece2
					+ getResources().getString(R.string.set_date_date_text)
					+ ",";
			ready = false;
		}
		if (pickedTime.getText()
				.equals(getResources().getString(
						R.string.set_date_time_datefirst_text))
				|| pickedTime.getText().equals(
						getResources().getString(R.string.set_date_time_text))) {
			conteo++;
			messagePiece2 = messagePiece2
					+ getResources().getString(R.string.set_date_time_text)
					+ ",";
			ready = false;
		}
		if (!ready) {
			if (conteo > 1) {
				Toast toast = Toast.makeText(SetDate.this, messagePiece4 + " "
						+ messagePiece2 + " " + messagePiece5,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else {
				Toast toast = Toast.makeText(SetDate.this, messagePiece1 + " "
						+ messagePiece2 + " " + messagePiece3,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		return ready;
	}

	@Override
	public void OnRequestInserted(boolean inserted, String systemId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnAllRequestsQueryFinished(ArrayList<Request> requests) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestQueryFInished(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCanceledQueryFinished(boolean canceled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestRemoveFinished(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDayRequestsQueryFinished(Date[] initialScheduledDates,
			Date[] finalScheduledDates) {
if (initialScheduledDates != null && finalScheduledDates != null) {
	this.initialScheduledDates = initialScheduledDates;
	this.finalScheduledDates = finalScheduledDates;


	numberOfScheduledAppointments = initialScheduledDates.length;
}else{
	numberOfScheduledAppointments = 0;
}
	
		progressDialog.dismiss();
		enableTimeButton();
	}
}
