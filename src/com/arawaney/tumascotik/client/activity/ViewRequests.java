package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.BudgetItemListBaseAdapter;
import com.arawaney.tumascotik.client.adapter.ViewRequestBaseAdapter;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.control.CalendarController;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.dialog.ListItemDialog;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Request;

public class ViewRequests extends FragmentActivity implements ParseRequestListener{
	ListView requestList;
	Boolean flagclick;
	ArrayList<Request> requests;
	RelativeLayout zeroDataLayout;
	ProgressDialog progressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_requests);

		flagclick = false;

		loadViews();

		refreshListView();

		requestList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				if (!flagclick) {
					Request request = requests.get(position);
					DialogFragment newFragment = new ListItemDialog(request,
							getApplicationContext(),ViewRequests.this );
					newFragment
							.show(getSupportFragmentManager(), "itemdetails");
				}
			}
		});

	}

	private void loadViews() {
		requestList = (ListView) findViewById(R.id.listverc);
		zeroDataLayout = (RelativeLayout) findViewById(R.id.layout_request_view_zero_data);
	}

	private ArrayList<Request> getRequests() {
		ArrayList<Request> requestList;

		requestList = RequestProvider.readRequests(this);

		return requestList;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshListView();
	}

	public void refreshListView() {

		requests = getRequests();

		if (requests == null) {
			setEmptyDataView();
		} else {
			setListView();

		}

	}

	private void setListView() {

		requestList.setVisibility(View.VISIBLE);
		zeroDataLayout.setVisibility(View.GONE);

		requestList.setAdapter(new ViewRequestBaseAdapter(this, requests));

	}

	private void setEmptyDataView() {

		requestList.setVisibility(View.GONE);
		zeroDataLayout.setVisibility(View.VISIBLE);

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
	public void onCanceledQueryFinished(boolean canceled, Request request) {
		if (canceled) {
			Date initialDate = request.getStart_date().getTime();
			Date finalDate = request.getFinish_date().getTime();
			String petName = request.getPet().getName();
			
			CalendarController.deleteCalendar(initialDate.getTime(),
					finalDate.getTime(),
					"Cita con Tumascotik para " + petName, this);
			
			RequestProvider.updateRequest(this, request);
			
			
		}
	progressDialog.dismiss();
	refreshListView();
		
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
		progressDialog = ProgressDialog.show(
				this, "Tumascotik",
				getResources().getString(R.string.request_view_process_cancelling));
		 progressDialog.setCancelable(true);
		ParseRequestProvider.changeRequestStatus(this, this, request);
		
		
	}

	@Override
	public void acceptRequest(Request request) {
		progressDialog = ProgressDialog.show(
				this, "Tumascotik",
				getResources().getString(R.string.request_view_process_accepting));
		 progressDialog.setCancelable(true);
		ParseRequestProvider.changeRequestStatus(this,this,request);
		
	}

	@Override
	public void onRequestAccept(Request request, boolean b) {
		if (b) {
			
			Date startDate = request.getStart_date()
					.getTime();
			Date finishDate = request.getFinish_date()
					.getTime();

			CalendarController.writeToCalendar(request
					.getPet().getName(), startDate, finishDate,
					this);
			RequestProvider.updateRequest(this, request);
		}
		progressDialog.dismiss();
		refreshListView();
	}

	@Override
	public void onOnePriceQueryFinished(int price) {
		// TODO Auto-generated method stub
		
	}
	
}
