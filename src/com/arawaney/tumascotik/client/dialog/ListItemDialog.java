package com.arawaney.tumascotik.client.dialog;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.User;

@SuppressLint("ValidFragment")
public class ListItemDialog extends DialogFragment {

	String petName;
	String service;
	Date fechainicio2;
	int status;
	Request request;
	ParseRequestListener listener;

	Context context;

	public ListItemDialog() {
	};

	public ListItemDialog(Request request, Context context,
			ParseRequestListener listener) {

		petName = request.getPet().getName();
		Service services = request.getService();
		service = services.getName();
		status = request.getStatus();
		this.request = request;
		this.listener = listener;

		this.context = context;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String connector = context.getResources().getString(
				R.string.request_view_dialog_conector);
		builder.setTitle(service + " " + connector + " " + petName);
		// builder.setIcon(R.drawable.mascotiklogodialog);
		// Add the buttons
		builder.setPositiveButton(context.getResources().getString(
				R.string.budget_request_dialog__back), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		if (MainController.USER.getisAdmin() == User.IS_ADMIN) {
			if (request.getStatus() == Request.STATUS_PENDING) {
				builder.setNeutralButton(
						context.getResources().getString(
								R.string.request_view_dialog_accept_button),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								request.setStatus(Request.STATUS_ACCEPTED);
								listener.acceptRequest(request);
							}
						});

				String cancel = context.getResources().getString(
						R.string.request_view_dialog_cancel_button);
				builder.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								request.setStatus(Request.STATUS_CANCELED);
								listener.cancelRequest(request);
							}

						});
			} else if (status == Request.STATUS_ACCEPTED) {
				String cancel = context.getResources().getString(
						R.string.request_view_dialog_cancel_button);
				builder.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								request.setStatus(Request.STATUS_CANCELED);
								listener.cancelRequest(request);
							}

						});

			}
		} else if (MainController.USER.getisAdmin() == User.NOT_ADMIN) {
			if (status == Request.STATUS_ACCEPTED) {
				String cancel = context.getResources().getString(
						R.string.request_view_dialog_cancel_button);
				builder.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								request.setStatus(Request.STATUS_CANCELED);
								listener.cancelRequest(request);
							}

						});

			}
		}

		return builder.create();
	}

}
