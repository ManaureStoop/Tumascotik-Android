package com.arawaney.tumascotik.client.control;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.backend.ParseBudgetProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.listener.ParseRequestListener;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.util.CalendarUtil;

public class Updater {

	final static float expirationPeriod = 6; // Six months

	public static void requestExpiredUpdater(Context context) {
		Calendar today = Calendar.getInstance();

		ArrayList<Request> requests = RequestProvider.readRequests(context);
		if (requests != null) {
			for (Request request : requests) {

				int diffYear = today.get(Calendar.YEAR)
						- request.getFinish_date().get(Calendar.YEAR);
				int diffMonth = diffYear * 12 + today.get(Calendar.MONTH)
						- request.getFinish_date().get(Calendar.MONTH);

				if (diffMonth >= expirationPeriod) {
					ParseRequestProvider.deactivateRequest(context, request);
					RequestProvider.removeRequest(context,
							request.getSystem_id());

				}
			}
		}

	}

	public static void budgetExpiredUpdater(Context context) {
		Calendar today = Calendar.getInstance();

		ArrayList<Budget> budgets = BudgetProvider.readBudgets(context);
		if (budgets != null) {
			for (Budget budget : budgets) {
				int diffYear = today.get(Calendar.YEAR)
						- budget.getCreatedAt().get(Calendar.YEAR);
				int diffMonth = diffYear * 12 + today.get(Calendar.MONTH)
						- budget.getCreatedAt().get(Calendar.MONTH);

				if (diffMonth >= expirationPeriod) {
					ParseBudgetProvider.deactivateBudget(context, budget);
					BudgetProvider.removeBudget(context, budget.getId());

				}
			}
		}

	}

	public static void updateOldRequests(Context context, ParseRequestListener listener, FragmentManager fm) {
		Calendar today = Calendar.getInstance();

		ArrayList<Request> requests = RequestProvider.readPassedRequests(
				context, today);
		if (requests != null) {
			for (Request request : requests) {

				Log.d("TEST REQUEST PASSED", request.getPet().getName() + " "
						+ request.getService().getName());
				Updater updater = new Updater();
				OldRequestDialogFragment dialog = updater.new OldRequestDialogFragment(request, context, listener);
				dialog.show(fm, "NoticeDialogFragment");

			}
		}

	}

	public class OldRequestDialogFragment extends DialogFragment {
		Request request;
		Context context;
		ParseRequestListener listener;
		
		public OldRequestDialogFragment(Request request, Context context, ParseRequestListener listener) {
			this.request = request;
			this.context = context;
			this.listener = listener;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			String message = context.getResources().getString(
					R.string.request_view_passed_date1)
					+ request.getService().getName()
					+ context.getResources().getString(
							R.string.request_view_passed_date2)
					+ request.getPet().getName()
					+ context.getResources().getString(
							R.string.request_view_passed_date3);
			builder.setMessage(message)
					.setPositiveButton(R.string.request_view_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									request.setStatus(Request.STATUS_ATTENDED);
									ParseRequestProvider.changeRequestStatus(context, listener, request);
									RequestProvider.updateRequest(context, request);
									dialog.dismiss();
								}
							})
					.setNegativeButton(R.string.request_view_no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									request.setStatus(Request.STATUS_CANCELED);
									ParseRequestProvider.changeRequestStatus(context, listener, request);
									RequestProvider.updateRequest(context, request);
									dialog.dismiss();
								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
}