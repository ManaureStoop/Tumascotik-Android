package com.arawaney.tumascotik.client.control;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

import com.arawaney.tumascotik.client.backend.ParseBudgetProvider;
import com.arawaney.tumascotik.client.backend.ParseRequestProvider;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
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
}