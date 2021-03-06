package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseProvider;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.PetProvider;
import com.arawaney.tumascotik.client.db.provider.UserProvider;
import com.arawaney.tumascotik.client.listener.ParsePetListener;
import com.arawaney.tumascotik.client.listener.ParseUserListener;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.BitMapUtil;
import com.arawaney.tumascotik.client.util.FontUtil;
import com.arawaney.tumascotik.client.util.NetworkUtil;

public class UserInfoActivity extends Activity implements ParseUserListener {

	private static final String LOG_TAG = "Tumascotik-Client-PetinfoActivity";

	ImageView userAvatar;
	TextView userCompleteName;
	TextView userEmail;
	TextView userGender;
	TextView userPhoneLocal;
	TextView userPhoneMobile;
	TextView userPhoneMobileSeparator;
	TextView userPhoneLocalSeparator;
	TextView userAddress;
	TextView username;
	TextView password;
	TextView confirmPassword;

	LinearLayout nameLayout;
	LinearLayout lastnameLayout;
	LinearLayout usernameLayout;
	LinearLayout passwordLayout;
	LinearLayout confirmPasswordLayout;

	EditText edituserName;
	EditText edituserLastName;
	EditText edituserEmail;
	EditText edituserPhoneLocal;
	EditText edituserPhoneLocalPrefix;
	EditText edituserPhoneMobile;
	EditText edituserPhoneMobilePrefix;
	EditText edituserAddress;
	EditText editUsername;
	EditText editPassword;
	EditText editConfirmPassword;

	Button buttonViewPets;

	Spinner edituserGender;

	ImageView saveEditButton;

	User user;
	User auxUser;

	private ProgressDialog progressDialog;

	ArrayAdapter<CharSequence> GenderAdapter;

	List<String> genders;

	public static final int MODE_INFO_LIST = 1;
	public static final int MODE_EDIT_LIST = 2;

	public static int viewMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_info);

		user = MainController.getCLIENTUSER();

		if (user != null) {
			if (!newUser()) {
				getUpdatedUser();
			}

			loadViews();
			loadButton();
			refreshView();

		} else {
			Log.d(LOG_TAG, "User is null");
			finish();
		}

	}

	private void getUpdatedUser() {
		ParseProvider.getUser((ParseUserListener) this, user);

	}

	private void loadViews() {
		userAvatar = (ImageView) findViewById(R.id.image_user_info_avatar);
		userCompleteName = (TextView) findViewById(R.id.text_user_info_name);
		userEmail = (TextView) findViewById(R.id.text_user_info_email);
		userGender = (TextView) findViewById(R.id.text_user_info_gender);
		userPhoneLocal = (TextView) findViewById(R.id.text_user_info_phone_local);
		userPhoneMobile = (TextView) findViewById(R.id.text_user_info_phone_mobile);
		userPhoneLocalSeparator = (TextView) findViewById(R.id.text_user_info_phone_local_separator);
		userPhoneMobileSeparator = (TextView) findViewById(R.id.text_user_info_phone_mobile_separator);
		userAddress = (TextView) findViewById(R.id.text_user_info_address);
		username = (TextView) findViewById(R.id.TextView_user_ifo_username_title);
		password = (TextView) findViewById(R.id.TextView_user_ifo_password_title);
		confirmPassword = (TextView) findViewById(R.id.TextView_user_ifo_confirm_password_title);

		edituserName = (EditText) findViewById(R.id.edit_text_user_info_name);
		edituserLastName = (EditText) findViewById(R.id.edit_text_user_info_lastname);
		edituserEmail = (EditText) findViewById(R.id.edittext_user_info_email);
		edituserGender = (Spinner) findViewById(R.id.spiner_user_info_gender);
		edituserPhoneLocal = (EditText) findViewById(R.id.edittext_user_info_phone_local);
		edituserPhoneMobile = (EditText) findViewById(R.id.edittext_user_info_phone_mobile);
		edituserPhoneLocalPrefix = (EditText) findViewById(R.id.edittext_user_info_phone_local_prefix);
		edituserPhoneMobilePrefix = (EditText) findViewById(R.id.edittext_user_info_phone_mobile_prefix);
		edituserAddress = (EditText) findViewById(R.id.edittext_user_info_address);
		buttonViewPets = (Button) findViewById(R.id.button_pet_info_view_pets);
		editUsername = (EditText) findViewById(R.id.EditText_user_ifo_username);
		editPassword = (EditText) findViewById(R.id.EditText_user_ifo_password);
		editConfirmPassword = (EditText) findViewById(R.id.EditText_user_ifo_confirm_password);

		nameLayout = (LinearLayout) findViewById(R.id.tlayout_user_info_name);
		lastnameLayout = (LinearLayout) findViewById(R.id.tlayout_user_info_lastname);
		usernameLayout = (LinearLayout) findViewById(R.id.LinearLayout_user_info_username);
		passwordLayout = (LinearLayout) findViewById(R.id.LinearLayout_user_info_password);
		confirmPasswordLayout = (LinearLayout) findViewById(R.id.LinearLayout_user_info_confirm_password);
		saveEditButton = (ImageView) findViewById(R.id.button_user_info_edit_save);

		GenderAdapter = ArrayAdapter.createFromResource(this,
				R.array.User_Genders, android.R.layout.simple_spinner_item);
		genders = Arrays.asList(getResources().getStringArray(
				R.array.User_Genders));

		confirmPasswordLayout.setVisibility(View.GONE);
		usernameLayout.setVisibility(View.GONE);
		passwordLayout.setVisibility(View.GONE);

		if (!newUser()) {
			if (userIsClient()) {
				saveEditButton.setVisibility(View.GONE);
			}
		}

		setFonts();

	}

	private boolean userIsClient() {
		if (MainController.USER.getisAdmin() == User.IS_ADMIN) {
			if (MainController.getCLIENTUSER().getisAdmin() == User.NOT_ADMIN) {
				return true;
			}
		}
		return false;
	}

	private void setFonts() {

		userCompleteName.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_THIN));
		userEmail
				.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		userGender.setTypeface(FontUtil
				.getTypeface(this, FontUtil.ROBOTO_LIGHT));
		userPhoneLocal.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		userPhoneMobile.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		userPhoneLocalSeparator.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		userPhoneMobileSeparator.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		userAddress.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		editUsername.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		editPassword.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		editConfirmPassword.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserName.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserLastName.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserEmail.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserPhoneLocal.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserPhoneMobile.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserPhoneLocalPrefix.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserPhoneMobilePrefix.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		edituserAddress.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));
		buttonViewPets.setTypeface(FontUtil.getTypeface(this,
				FontUtil.ROBOTO_LIGHT));

	}

	private void loadButton() {
		if (saveEditButton != null) {
			saveEditButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (viewMode == MODE_INFO_LIST) {
						viewMode = MODE_EDIT_LIST;
						refreshView();
					} else if (viewMode == MODE_EDIT_LIST) {
						saveUser();
						if (newUser()) {
							if (checkFields()) {
								setSavingUserDialog();
								insertUser();
							}
						} else {
							if (userDataChanged()) {
								if (checkFields()) {
									setSavingUserDialog();
									updateUser();
								}
							} else {
								refreshView();
							}
						}
					}

				}

				private void saveUser() {
					auxUser = new User(user);
					auxUser.setName(edituserName.getText().toString());
					auxUser.setLastname(edituserLastName.getText().toString());
					auxUser.setEmail(edituserEmail.getText().toString());
					auxUser.setGender(edituserGender.getSelectedItemPosition());

					if (edituserPhoneLocalPrefix.getText().toString().trim()
							.length() != 0
							&& edituserPhoneLocal.getText().toString().trim()
									.length() != 0) {
						auxUser.setHouse_telephone(Long
								.parseLong(edituserPhoneLocalPrefix.getText()
										.toString().trim()
										+ edituserPhoneLocal.getText()
												.toString().trim()));
					}
					if (edituserPhoneMobilePrefix.getText().toString().trim()
							.length() != 0
							&& edituserPhoneMobile.getText().toString().trim()
									.length() != 0) {
						auxUser.setMobile_telephone(Long
								.parseLong(edituserPhoneMobilePrefix.getText()
										.toString().trim()
										+ edituserPhoneMobile.getText()
												.toString().trim()));
					}

					auxUser.setAddress(edituserAddress.getText().toString());

				}

				private boolean checkFields() {

					if (edituserName.getText().toString().length() == 0) {
						edituserName.requestFocus();
						Toast toast = Toast.makeText(
								UserInfoActivity.this,
								getResources().getString(
										R.string.user_info_data_missing_name),
								Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return false;
					}
					auxUser.setName(edituserName.getText().toString());

					if (edituserLastName.getText().toString().length() == 0) {
						edituserLastName.requestFocus();
						Toast toast = Toast
								.makeText(
										UserInfoActivity.this,
										getResources()
												.getString(
														R.string.user_info_data_missing_lastname),
										Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return false;
					}
					auxUser.setLastname(edituserLastName.getText().toString());

					if (newUser()) {
						if (editUsername.getText().toString().length() == 0) {
							editUsername.requestFocus();
							Toast toast = Toast
									.makeText(
											UserInfoActivity.this,
											getResources()
													.getString(
															R.string.user_info_data_missing_username),
											Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							return false;
						}
						auxUser.setUsername(editUsername.getText().toString());

						if (editPassword.getText().toString().length() == 0) {
							editPassword.requestFocus();
							Toast toast = Toast
									.makeText(
											UserInfoActivity.this,
											getResources()
													.getString(
															R.string.user_info_data_missing_password),
											Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							return false;
						}
						auxUser.setPassword(editPassword.getText().toString());

						if (editConfirmPassword.getText().toString().length() == 0) {
							editConfirmPassword.requestFocus();
							Toast toast = Toast
									.makeText(
											UserInfoActivity.this,
											getResources()
													.getString(
															R.string.user_info_data_missing_password),
											Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							return false;
						}
						if (!editPassword
								.getText()
								.toString()
								.equals(editConfirmPassword.getText()
										.toString())) {
							editPassword.requestFocus();
							Toast toast = Toast
									.makeText(
											UserInfoActivity.this,
											getResources()
													.getString(
															R.string.user_info_data_no_match_password),
											Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							return false;
						}
					}
					if (edituserAddress.getText().toString().length() == 0) {
						edituserAddress.requestFocus();
						Toast toast = Toast
								.makeText(
										UserInfoActivity.this,
										getResources()
												.getString(
														R.string.user_info_data_missing_address),
										Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return false;
					}
					auxUser.setAddress(edituserAddress.getText().toString());

					if (edituserPhoneMobilePrefix.getText().toString().trim()
							.length() == 0
							|| edituserPhoneMobile.getText().toString().trim()
									.length() == 0) {
						edituserPhoneMobile.requestFocus();
						Toast toast = Toast
								.makeText(
										UserInfoActivity.this,
										getResources()
												.getString(
														R.string.user_info_data_missing_cellphone),
										Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return false;
					}
					auxUser.setMobile_telephone((Long
							.parseLong(edituserPhoneMobilePrefix.getText()
									.toString().trim()
									+ edituserPhoneMobile.getText().toString()
											.trim())));

					return true;
				}

				private boolean userDataChanged() {
					boolean changed = false;

					if (!auxUser.getName().equals(user.getName())) {
						changed = true;
					} else if (!auxUser.getLastname()
							.equals(user.getLastname())) {
						changed = true;
					} else if (!auxUser.getEmail().equals(user.getEmail())) {
						changed = true;
					} else if (!auxUser.getHouse_telephone().equals(
							user.getHouse_telephone())) {
						changed = true;
					} else if (!auxUser.getMobile_telephone().equals(
							user.getMobile_telephone())) {
						changed = true;
					} else if (!auxUser.getAddress().equals(user.getAddress())) {
						changed = true;
					} else if (auxUser.getGender() != user.getGender()) {
						changed = true;
					}

					return changed;
				}

			});

			buttonViewPets.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(UserInfoActivity.this,
							PetPicker.class);
					PetPicker.functionMode = PetPicker.MODE_EDIT_PET;
					startActivity(i);

				}
			});

		}

	}

	private void refreshView() {

		if (viewMode == MODE_INFO_LIST) {
			setInfoView();
		} else if (viewMode == MODE_EDIT_LIST) {
			setEditView();
		}

	}

	private void setSavingUserDialog() {
		progressDialog = ProgressDialog.show(this, "", getResources()
				.getString(R.string.user_info_button_saving));
		progressDialog.setCancelable(true);
	}

	private void setEditView() {

		userEmail.setVisibility(View.GONE);
		userGender.setVisibility(View.GONE);
		userPhoneLocal.setVisibility(View.GONE);
		userPhoneMobile.setVisibility(View.GONE);
		userAddress.setVisibility(View.GONE);
		buttonViewPets.setVisibility(View.GONE);

		edituserName.setVisibility(View.VISIBLE);
		edituserLastName.setVisibility(View.VISIBLE);
		edituserEmail.setVisibility(View.VISIBLE);
		edituserGender.setVisibility(View.VISIBLE);
		edituserPhoneLocal.setVisibility(View.VISIBLE);
		edituserPhoneMobile.setVisibility(View.VISIBLE);
		edituserPhoneLocalPrefix.setVisibility(View.VISIBLE);
		edituserPhoneMobilePrefix.setVisibility(View.VISIBLE);
		edituserAddress.setVisibility(View.VISIBLE);
		userPhoneMobileSeparator.setVisibility(View.VISIBLE);
		userPhoneLocalSeparator.setVisibility(View.VISIBLE);

		if (newUser()) {
			confirmPasswordLayout.setVisibility(View.VISIBLE);
			usernameLayout.setVisibility(View.VISIBLE);
			passwordLayout.setVisibility(View.VISIBLE);
		}

		nameLayout.setVisibility(View.VISIBLE);
		lastnameLayout.setVisibility(View.VISIBLE);

		if (user.getName() != null) {
			edituserName.setText(user.getName());
		}

		if (user.getLastname() != null) {
			edituserLastName.setText(user.getLastname());
		}

		if (user.getEmail() != null) {
			edituserEmail.setText(user.getEmail());
		}

		if (GenderAdapter != null) {
			edituserGender.setAdapter(GenderAdapter);
			Integer gendervalue = user.getGender();
			if (gendervalue > 0) {
				edituserGender.setSelection(genders.indexOf(genders
						.get(gendervalue)));
			}
		}

		if (user.getAddress() != null) {
			edituserAddress.setText(user.getAddress());
		}

		if (user.getMobile_telephone() != null) {
			if (user.getMobile_telephone() != 0) {
				edituserPhoneMobile
						.setText(user
								.getMobile_telephone()
								.toString()
								.subSequence(
										3,
										user.getMobile_telephone().toString()
												.length()));
				edituserPhoneMobilePrefix.setText(user.getMobile_telephone()
						.toString().subSequence(0, 3));
			} else {
				edituserPhoneMobile.setText(" ");
				edituserPhoneMobilePrefix.setText(" ");
			}

		}
		if (user.getHouse_telephone() != null) {
			if (user.getHouse_telephone() != 0) {
				edituserPhoneLocal.setText(user
						.getHouse_telephone()
						.toString()
						.subSequence(3,
								user.getHouse_telephone().toString().length()));
				edituserPhoneLocalPrefix.setText(user.getHouse_telephone()
						.toString().subSequence(0, 3));
			} else {
				edituserPhoneLocal.setText(" ");
				edituserPhoneLocalPrefix.setText(" ");

			}
			if (newUser()) {
				usernameLayout.setVisibility(View.VISIBLE);
				passwordLayout.setVisibility(View.VISIBLE);
			}

		}
		saveEditButton.setImageResource(R.drawable.buton_check);

		String path;
		if (user.getGender() == User.GENDER_WOMAN) {
			path = "user_female";
		} else {
			path = "user_male";
		}
		userAvatar.setImageResource(BitMapUtil.getImageId(this, path));

	}

	private void setInfoView() {

		userEmail.setVisibility(View.VISIBLE);
		userGender.setVisibility(View.VISIBLE);
		userPhoneLocal.setVisibility(View.VISIBLE);
		userPhoneMobile.setVisibility(View.VISIBLE);
		userAddress.setVisibility(View.VISIBLE);

		edituserName.setVisibility(View.GONE);
		edituserLastName.setVisibility(View.GONE);
		edituserEmail.setVisibility(View.GONE);
		edituserGender.setVisibility(View.GONE);
		edituserPhoneLocal.setVisibility(View.GONE);
		edituserPhoneMobile.setVisibility(View.GONE);
		edituserPhoneLocalPrefix.setVisibility(View.GONE);
		edituserPhoneMobilePrefix.setVisibility(View.GONE);
		edituserAddress.setVisibility(View.GONE);
		userPhoneMobileSeparator.setVisibility(View.GONE);
		userPhoneLocalSeparator.setVisibility(View.GONE);

		confirmPasswordLayout.setVisibility(View.GONE);
		usernameLayout.setVisibility(View.GONE);
		passwordLayout.setVisibility(View.GONE);

		nameLayout.setVisibility(View.GONE);
		lastnameLayout.setVisibility(View.GONE);

		if (!userIsClient()) {
			buttonViewPets.setVisibility(View.GONE);
		}

		if (user.getName() != null && user.getLastname() != null) {
			userCompleteName.setText(user.getName() + " " + user.getLastname());
		}

		if (user.getEmail() != null) {
			userEmail.setText(user.getEmail().toString());

		}

		if (user.getGender() > 0) {
			userGender.setText(genders.get(user.getGender()));
		} else
			userGender.setText(genders.get(0));

		if (user.getAddress() != null) {
			userAddress.setText(user.getAddress().toString());

		}
		if (user.getMobile_telephone() != null) {
			userPhoneMobile.setText(user.getMobile_telephone().toString());

		}
		if (user.getHouse_telephone() != null) {
			userPhoneLocal.setText(user.getHouse_telephone().toString());

		}
		saveEditButton.setImageResource(R.drawable.buton_edit);

		String path;
		if (user.getGender() == User.GENDER_WOMAN) {
			path = "user_female";
		} else {
			path = "user_male";
		}
		userAvatar.setImageResource(BitMapUtil.getImageId(this, path));

	}

	private void updateUser() {
		ParseProvider.updateUser(this, auxUser);
	}

	private void insertUser() {
		ParseProvider.insertUser(this, auxUser);
	}

	private void updateClient() {
		ParseProvider.updateClient(this, auxUser);
	}

	@Override
	public void OnLoginResponse() {
		ParseProvider.updateClient(this, auxUser);

	}

	@Override
	public void onUserUpdateFinish(User user, boolean updated) {
		if (updated) {
			this.user = new User(user);
			this.user.setUpdated_at(Calendar.getInstance());
			UserProvider.updateUser(this, this.user);
			viewMode = MODE_INFO_LIST;
		} else {
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.user_info_user_not_updated),
					Toast.LENGTH_LONG).show();
		}

		refreshView();
		progressDialog.dismiss();

	}

	@Override
	public void onUserQueryFinish(User updatedUSer, boolean updated) {
		if (updated) {

			this.user = updatedUSer;
			UserProvider.updateUser(this, this.user);
			refreshView();
		}
	}

	@Override
	public void onClientsQueryFinish(ArrayList<User> users, boolean b) {
		// TODO Auto-generated method stub

	}

	private boolean newUser() {
		return (user.getSystemId() == null);
	}

	@Override
	public void onUserInsertFinish(User user, boolean inserted, String systemId) {

		if (inserted) {
			user.setSystemId(systemId);
			UserProvider.insertUser(this, user);
			viewMode = MODE_INFO_LIST;
		} else {
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.user_info_user_not_inserted),
					Toast.LENGTH_LONG).show();
		}
		progressDialog.dismiss();
		refreshView();
	}

	@Override
	public void onCLientUpdateFinish(User user, boolean b) {
		if (b) {
			this.user = new User(user);
			this.user.setUpdated_at(Calendar.getInstance());
			UserProvider.updateUser(this, user);
			viewMode = MODE_INFO_LIST;
		} else {
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.user_info_user_not_updated),
					Toast.LENGTH_LONG).show();
		}
		progressDialog.dismiss();
		refreshView();
	}

}
