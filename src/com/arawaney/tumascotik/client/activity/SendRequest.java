package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arawaney.tumascotik.client.MainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.util.CalendarUtil;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.parse.Parse;
import com.parse.ParseObject;

public class SendRequest extends Activity implements ParseRequestListener {
	private static final String LOG_TAG = "Tumascotik-Client-SendRequestActivity";

	ImageView send;
	ImageView cancel;
	TextView textPetName;
	TextView textMotiveTitle;
	TextView textMotive;
	TextView textDate;
	TextView textTime;
	TextView textPrice;
	TextView textDuration;
	TextView textDelivery;
	Request request;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_request);

		request = MainController.getREQUEST();
		loadViews();
		setFonts();
		setResume();
		loadButtons();
	}

	private void loadViews() {
		textPetName = (TextView) findViewById(R.id.txtresumen_title_petname);
		textMotive = (TextView) findViewById(R.id.txtresumen_motive);
		textDate = (TextView) findViewById(R.id.txtresumen_date);
		textTime = (TextView) findViewById(R.id.txtresumen_time);
		textPrice = (TextView) findViewById(R.id.txtresumen_price);
		textDuration = (TextView) findViewById(R.id.txtresumen_duration);
		textDelivery = (TextView) findViewById(R.id.txtresumen_delivery);
		textMotiveTitle = (TextView) findViewById(R.id.txtresumen_motive_title);
	}

	private void loadButtons() {
		send = (ImageView) findViewById(R.id.benviarfinal);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sendRequest();

			}

		});

		cancel = (ImageView) findViewById(R.id.bcancfinal);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backToMainActivity();

			}

		});
	}

	private void sendRequest() {
		progressDialog = ProgressDialog.show(this, "", getResources()
				.getString(R.string.send_request_sending));
		progressDialog.setCancelable(true);
		ParseProvider.initializeParse(getApplicationContext());
		ParseRequestProvider.sendRequest(this, this, request);
	}

	private void setResume() {

		final String petName = MainController.getPET().getName();
		final String date = CalendarUtil.getDateFormated(
				request.getStart_date(), "dd , MM yyyy");
		final String time = CalendarUtil.getDateFormated(
				request.getStart_date(), "hh:mm a");
		final String motive = request.getService().getName();
		final int price;
		if (request.getPrice() != null) {
			price = request.getPrice();
		} else
			price = 0;

		final String hourSufix = getResources().getString(
				R.string.send_request_details_hour_sufix);

		final float duration = getRequestDuration();

		textPetName.setText(petName);
		textMotive.setText(motive);
		textDate.setText(getResources().getString(
				R.string.send_request_details_text_date)
				+ " " + date);
		textTime.setText(getResources().getString(
				R.string.send_request_details_text_time)
				+ " " + time);
		if (price == 0) {
			textPrice.setText(getResources().getString(
					R.string.send_request_details_text_price)
					+ ("N/A"));

		} else
			textPrice.setText(getResources().getString(
					R.string.send_request_details_text_price)
					+ price);
		textDuration.setText(getResources().getString(
				R.string.send_request_details_text_duration)
				+ " " + duration + " " + hourSufix);

		if (request.isDelivery() == Request.IS_DELIVERY) {
			textDelivery.setText(getResources().getString(
					R.string.send_request_details_text_delivery_yes));
		} else {
			textDelivery.setText(getResources().getString(
					R.string.send_request_details_text_delvery_no));

		}

	}

	private float getRequestDuration() {
		float hours = 0;
		int finishTime = request.getFinish_date().get(Calendar.HOUR_OF_DAY);
		int startTime = request.getStart_date().get(Calendar.HOUR_OF_DAY);

		hours = finishTime - startTime;
		return hours;
	}

	private void setFonts() {
		textMotiveTitle.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		textPetName.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		textMotive.setTypeface(FontUtil
				.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		textDate.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		textTime.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		textPrice
				.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		textDuration.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		textDelivery.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
	}

	void SaveRequest(String systemId) {
		request.setSystem_id(systemId);
		RequestProvider.insertRequest(this, request);
	}

	@Override
	public void OnRequestInserted(boolean inserted, String systemId) {
		progressDialog.dismiss();
		if (inserted) {
			SaveRequest(systemId);
		}
		backToMainActivity();

	}

	private void backToMainActivity() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
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
	public void onCanceledQueryFinished(boolean canceled, Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestRemoveFinished(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDayRequestsQueryFinished(Date[] initialScheduledDates,
			Date[] finalScheduledDates) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelRequest(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptRequest(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestAccept(Request request, boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOnePriceQueryFinished(int price) {
		if (price != 0) {
			request.setPrice(price);
		}

	}
}
