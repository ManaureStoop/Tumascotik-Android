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

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.ViewRequestBaseAdapter;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.dialog.ListItemDialog;
import com.arawaney.tumascotik.client.model.Request;

public class ViewRequests extends FragmentActivity {
	ListView requestList;
	Boolean flagclick;
	ArrayList<Request> requests ;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_requests);

		flagclick = false;

		requests = getRequests();

		requestList = (ListView) findViewById(R.id.listverc);
		requestList.setAdapter(new ViewRequestBaseAdapter(this, requests));

		requestList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				if (!flagclick) {
					Request request = requests.get(position);
					DialogFragment newFragment = new ListItemDialog(request, getApplicationContext());
					newFragment
							.show(getSupportFragmentManager(), "itemdetails");
				}
			}
		});
	
	}

	private ArrayList<Request> getRequests() {
		ArrayList<Request> requestList ;
		
		requestList = RequestProvider.readRequests(this);

		return requestList;
	}


	public void Refresh() {
		// TODO Auto-generated method stub
		super.onResume();
		ArrayList<Request> image_details = getRequests();
		requestList = (ListView) findViewById(R.id.listverc);
		requestList.setAdapter(new ViewRequestBaseAdapter(this, image_details));
	}
}
