package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.MainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.ItemUserGridAdapter;
import com.arawaney.tumascotik.client.adapter.ViewRequestBaseAdapter;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class ClientPicker extends Activity {
	private final String LOG_TAG = "Tumascotik Client Picker";

	ArrayList<User> users;
	ListView userGRidView;
	View addUserView;
	private LayoutInflater inflater;
	ItemUserGridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picker);

		loadViews();
		loadUsers();
		loadAddButtons();

	}

	@Override
	protected void onResume() {
		super.onResume();
		users = UserProvider.readUsers(this);
		if (users == null) {
			users = new ArrayList<User>();
		}
		if (userGRidView != null) {
			adapter = new ItemUserGridAdapter(this, users);
			adapter.setFooterView(addUserView);
			userGRidView.setAdapter(adapter);
		}

	}

	private void loadViews() {
		userGRidView = (ListView) findViewById(R.id.grid_picker);
		inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
		addUserView = inflater.inflate(R.layout.add_view, null);

	}

	private void loadUsers() {
		users = UserProvider.readUsers(this);
		if (users == null) {
			users = new ArrayList<User>();
		}
		adapter = new ItemUserGridAdapter(this, users);
		adapter.setFooterView(addUserView);
		userGRidView.setAdapter(adapter);

	}

	private void loadAddButtons() {
		addUserView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(LOG_TAG, "Add new user!!");
				User user = new User();
				user.setAdmin(User.NOT_ADMIN); 
				MainController.setCLIENTUSER(user);
				UserInfoActivity.viewMode = UserInfoActivity.MODE_EDIT_LIST;
				Intent i = new Intent(ClientPicker.this, UserInfoActivity.class);
				startActivity(i);

			}
		});

		userGRidView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				MainController.setCLIENTUSER(users.get(position));
				UserInfoActivity.viewMode = UserInfoActivity.MODE_INFO_LIST;
				Intent i = new Intent(ClientPicker.this, UserInfoActivity.class);
				startActivity(i);
			}
		});
	}
}
