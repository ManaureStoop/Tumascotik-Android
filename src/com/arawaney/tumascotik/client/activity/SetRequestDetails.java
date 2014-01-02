package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;

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
import com.arawaney.tumascotik.client.dialog.MotivePicker;
import com.arawaney.tumascotik.client.dialog.TimePicker;
import com.arawaney.tumascotik.client.listener.ParseServiceListener;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.util.NetworkUtil;

public class SetRequestDetails extends FragmentActivity implements ParseServiceListener {
	private static final String LOG_TAG = "Tumascotik-Client-SetRequesDetailsActivity";

	EditText comments;
	Button motive;
	TextView motiveText;
	ToggleButton isDelivery;
	ImageView siguiente;
	ImageView cancelar;
	
	private ProgressDialog progressDialog;
	
	ArrayAdapter<String> MotivesAdapter;
	ArrayList<String> motives;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setrequestdetails);

		loadViews();
		downloadMotitveLists();
		loadButtons();

	}

	private void loadButtons() {
		siguiente = (ImageView) findViewById(R.id.bsigpedirc);

		siguiente.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Checkfields()) {
					saveRequest();
					Intent i = new Intent(SetRequestDetails.this,
							SendRequest.class);
					startActivity(i);
				}

			}

			private void saveRequest() {
				Request request = MainController.getREQUEST();
				
				request.setService(motiveText.getText().toString());
				
				
				if (comments.getText()!=null) {
					if (!comments.getText().toString().isEmpty()) {
						request.setComment(comments.getText().toString());
					}	
				}
				
				if (isDelivery.isChecked()) {
					request.setDelivery(Request.IS_DELIVERY);
				}else
					request.setDelivery(Request.IS__NOT_DELIVERY);
				
				MainController.setREQUEST(request);
				
			}
		});

		cancelar = (ImageView) findViewById(R.id.bcancpedirc);
		cancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});
		motive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new MotivePicker(motives, motives.size());
				newFragment.show(getSupportFragmentManager(), "motives");
				
			}
		});
	}
	
	public void onUserSelectValue(int index) {
		motiveText.setText(motives.get(index));
	}

	private void loadViews() {
		comments = (EditText) findViewById(R.id.editText_setrequestdetails_comments);
		motive = (Button) findViewById(R.id.button_set_requestdetails_motive);
		motiveText = (TextView) findViewById(R.id.text_motive);
		isDelivery = (ToggleButton) findViewById(R.id.toggleButton_setrequestdetails_isdelivetry);
		setFonts();
	}
	
	private void setFonts() {
		comments.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_THIN));
		motive.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		motiveText.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		
	}

	private void downloadMotitveLists() {
		if (NetworkUtil.ConnectedToInternet(this)) {

			ParseProvider.initializeParse(this);

			progressDialog = ProgressDialog
					.show(this, "", getResources().getString(R.string.set_request_details_loading_motive_lists));

			ParseProvider.getMotives(this, this);


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
		if (motive.getText().toString().equals(getResources().getString(R.string.general_choose))) {
			conteo++;
			messagePiece2 = messagePiece2
					+ getResources().getString(R.string.set_request_details_motive)
					;
			ready = false;
		}

		if (!ready) {
			if (conteo > 1) {
				Toast toast = Toast.makeText(SetRequestDetails.this, messagePiece4
						+ " "+ messagePiece2 + " "+ messagePiece5, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else {
				Toast toast = Toast.makeText(SetRequestDetails.this, messagePiece1
						+ " "+messagePiece2 +" "+ messagePiece3, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		return ready;
	
}
	
	@Override
	public void onMotivesQueryFinished(ArrayList<String> motives) {

		if (motives != null) {
			this.motives = motives;
			this.motives.add(0, getResources().getString(R.string.general_choose));
			MotivesAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, this.motives);
		
			progressDialog.dismiss();
		}
}
}
