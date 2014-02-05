package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.arawaney.tumascotik.client.ClientMainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.ServiceProvider;
import com.arawaney.tumascotik.client.dialog.MotivePicker;
import com.arawaney.tumascotik.client.dialog.TimePicker;
import com.arawaney.tumascotik.client.listener.ParseServiceListener;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.util.NetworkUtil;

@SuppressLint("NewApi")
public class SetRequestDetails extends FragmentActivity {
	private static final String LOG_TAG = "Tumascotik-Client-SetRequesDetailsActivity";
	Button setComment;
	EditText comments;
	Button motive;
	TextView motiveText;
	Button isDelivery;
	ImageView foward;
	ImageView cancel;
	Request request;

	boolean isDeliveryStatus;

	ArrayAdapter<String> MotivesAdapter;
	ArrayList<String> motives;
	ArrayList<Service> services;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setrequestdetails);

		loadViews();
		getMotiveLists();
		loadButtons();

	}

	@SuppressLint("NewApi")
	private void loadButtons() {
		foward = (ImageView) findViewById(R.id.bsigpedirc);

		foward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Checkfields()) {
					saveRequest();
					Intent i = new Intent(SetRequestDetails.this,
							SetDate.class);
					startActivity(i);
				}

			}

			private void saveRequest() {
				
				setRequest();

				Service service = new Service();
				for (Service servic : services) {
					if (servic.getName()
							.equals(motiveText.getText().toString())) {
						service = servic;
					}
				}

				request.setService(service);

				if (comments.getText() != null) {
					if (!comments.getText().toString().isEmpty()) {
						request.setComment(comments.getText().toString());
					}
				}

				if (isDeliveryStatus) {
					request.setDelivery(Request.IS_DELIVERY);
				} else
					request.setDelivery(Request.IS__NOT_DELIVERY);

				MainController.setREQUEST(request);

			}
		});

		cancel = (ImageView) findViewById(R.id.bcancpedirc);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ClientMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});
		motive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new MotivePicker(motives, motives
						.size());
				newFragment.show(getSupportFragmentManager(), "motives");

			}
		});

		setComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comments.setVisibility(View.VISIBLE);

			}
		});

		isDelivery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isDeliveryStatus) {
					isDeliveryStatus = false;
					isDelivery.setBackground(getResources().getDrawable(
							R.drawable.ic_yellow_button_pressed));
					isDelivery
							.setText(R.string.set_request_details_isDeivery_title_negative);
				} else {
					isDeliveryStatus = true;
					isDelivery.setBackground(getResources().getDrawable(
							R.drawable.ic_yellow_button));
					isDelivery
							.setText(R.string.set_request_details_isDeivery_title_afirmative);
				}

			}
		});
	}

	public void onUserSelectValue(int index) {
		motiveText.setText(motives.get(index));
	}

	private void loadViews() {
		setComment = (Button) findViewById(R.id.button_setrequestdetails_comment);
		comments = (EditText) findViewById(R.id.editText_setrequestdetails_comments);
		motive = (Button) findViewById(R.id.button_set_requestdetails_motive);
		motiveText = (TextView) findViewById(R.id.text_motive);
		isDelivery = (Button) findViewById(R.id.toggleButton_setrequestdetails_isdelivetry);
		comments.setVisibility(View.GONE);
		isDeliveryStatus = false;
		setFonts();
	}

	private void setFonts() {
		comments.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		motive.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		motiveText.setTypeface(FontUtil
				.getTypeface(this, FontUtil.ROBOTO_LIGHT));

	}

	private void getMotiveLists() {
		motives = new ArrayList<String>();
		services = ServiceProvider.readRequestMotives(this);
		if (services != null) {
			if (!services.isEmpty()) {
				for (Service service : services) {
					motives.add(service.getName());
				}
			} else {
				Log.d(LOG_TAG, "no motives found");
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	// Checks if every field is filled by the user. If there is one blank-field
	// the user will be notified
	// when clicking next
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
		if (motive.getText().toString()
				.equals(getResources().getString(R.string.general_choose))) {
			conteo++;
			messagePiece2 = messagePiece2
					+ getResources().getString(
							R.string.set_request_details_motive);
			ready = false;
		}

		if (!ready) {
			if (conteo > 1) {
				Toast toast = Toast.makeText(SetRequestDetails.this,
						messagePiece4 + " " + messagePiece2 + " "
								+ messagePiece5, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else {
				Toast toast = Toast.makeText(SetRequestDetails.this,
						messagePiece1 + " " + messagePiece2 + " "
								+ messagePiece3, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		return ready;

	}
	
	private void setRequest() {
		request = new Request();
		request.setPet(MainController.getPET());
		request.setStatus(Request.STATUS_PENDING);
		request.setActive(Request.ACTIVE);
	}

}
