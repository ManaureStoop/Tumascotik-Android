package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
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
import android.widget.TextView;

import com.arawaney.tumascotik.client.ClientMainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Request;
import com.parse.Parse;
import com.parse.ParseObject;

public class SendRequest extends Activity implements ParseRequestListener {
	private static final String LOG_TAG = "Tumascotik-Client-SendRequestActivity";
	
	Button enviar;
	Button cancelar;
	TextView resumen;
	Request request;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		
		request = MainController.getREQUEST();
		
		

		setResume();
		
		loadButtons();
	}

	private void loadButtons() {
		enviar = (Button) findViewById(R.id.benviarfinal);

		enviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sendRequest();

			}

			
		});

		cancelar = (Button) findViewById(R.id.bcancfinal);
		cancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backToMainActivity();

			}

			
		});
	}
	private void sendRequest() {
		progressDialog = ProgressDialog
				.show(this, "", getResources().getString(R.string.send_request_sending));
		ParseProvider.initializeParse(getApplicationContext());
		ParseProvider.sendRequest(this,this, request);
	}
	private void setResume() {
		final String nombre = MainController.USER.getName();
		final String mascota = MainController.getPET().getName();
		final String motivo = request.getService();
	
		resumen = (TextView) findViewById(R.id.txtresumnfinal);
		String resum = nombre+mascota+motivo;
		resumen.setText(resum);
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
		Intent intent = new Intent(getApplicationContext(),
				ClientMainActivity.class);
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
}
