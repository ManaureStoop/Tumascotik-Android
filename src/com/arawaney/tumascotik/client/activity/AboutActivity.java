package com.arawaney.tumascotik.client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.util.FontUtil;

public class AboutActivity extends Activity {
	private final String LOG_TAG = "Tumascotik Client Picker";

	TextView title;
	TextView version;
	TextView developed;
	TextView contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		loadViews();
		setFonts();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void loadViews() {
		version = (TextView) findViewById(R.id.about_activity_version);
		title = (TextView) findViewById(R.id.about_activity_title);
		developed = (TextView) findViewById(R.id.about_activity_developed);
		contact = (TextView) findViewById(R.id.about_activity_contact);

	}
	
	private void setFonts() {
		version.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		title.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		developed.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		contact.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
	}

}
