package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.BudgetItemListBaseAdapter;
import com.arawaney.tumascotik.client.adapter.ViewRequestBaseAdapter;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.dialog.ListItemDialog;
import com.arawaney.tumascotik.client.model.Request;

public class ViewRequests extends FragmentActivity {
	ListView requestList;
	Boolean flagclick;
	ArrayList<Request> requests;
	RelativeLayout zeroDataLayout;

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
							getApplicationContext());
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
}
