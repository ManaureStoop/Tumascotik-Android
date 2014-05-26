package com.arawaney.tumascotik.client.listener;

import java.util.ArrayList;

import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetService;

public interface ParseBudgetListener {

	void OnBudgetInserted(boolean b, String objectId);

	void OnAllBudgetsQueryFinished(ArrayList<Budget> budgets);

	void onBudgetQueryFInished(Budget budget);

	void onCanceledQueryFinished(boolean b);

	void onBudgetRemoveFinished(Budget budget);

	void onOnePriceQueryFinished(BudgetService budgetService);
	
	void onBudgetStatusChanged(Budget budget);

	void OnStatusChangedFinished(boolean b, Budget budget);
	

}
