package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.arawaney.tumascotik.client.MainActivity;
import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.BudgetAdapter;
import com.arawaney.tumascotik.client.adapter.BudgetItemListBaseAdapter;
import com.arawaney.tumascotik.client.adapter.ViewRequestBaseAdapter;
import com.arawaney.tumascotik.client.backend.ParseBudgetProvider;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.dialog.BudgetItemDialog;
import com.arawaney.tumascotik.client.dialog.ListItemDialog;
import com.arawaney.tumascotik.client.listener.ParseBudgetListener;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;

public class ViewBudgets extends FragmentActivity implements ParseBudgetListener {
	ListView budgetList;
	Boolean flagclick;
	ArrayList<Budget> budgets;
	RelativeLayout zeroDataLayout;
	List<BudgetService> services;
	ProgressDialog progressDialog;
	Budget selectedBudget;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_requests);

		loadViews();

		refreshListView();

		budgetList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				services = new ArrayList<BudgetService>();
					selectedBudget = budgets.get(position);
					
					progressDialog = ProgressDialog.show(
							ViewBudgets.this, "Tumascotik",
							getResources().getString(R.string.budget_downloading_prices));
					downloadPrices();		
			}

		
		});

	}
	private void downloadPrices() {
		List<Service> services = selectedBudget.getServices();
		ParseBudgetProvider.readSavedPrices(services, selectedBudget, this);
	}
	
	private void loadViews() {
		budgetList = (ListView) findViewById(R.id.listverc);
		zeroDataLayout = (RelativeLayout) findViewById(R.id.layout_request_view_zero_data);
	}

	private ArrayList<Budget> getBudgets() {
		ArrayList<Budget> budgettList;

		budgettList = BudgetProvider.readNotInProgress(this);

		return budgettList;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshListView();
	}

	public void refreshListView() {

		budgets = getBudgets();

		if (budgets == null) {
			setEmptyDataView();
		} else {
			setListView();

		}

	}

	private void setListView() {

		budgetList.setVisibility(View.VISIBLE);
		zeroDataLayout.setVisibility(View.GONE);

		budgetList.setAdapter(new BudgetAdapter(this, budgets));

	}

	private void setEmptyDataView() {

		budgetList.setVisibility(View.GONE);
		zeroDataLayout.setVisibility(View.VISIBLE);

	}
	@Override
	public void OnBudgetInserted(boolean b, String objectId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OnAllBudgetsQueryFinished(ArrayList<Budget> budgets) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBudgetQueryFInished(Budget budget) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCanceledQueryFinished(boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBudgetRemoveFinished(Budget budget) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onOnePriceQueryFinished(BudgetService budgetService) {
		services.add(budgetService);
		
		if (services.size()== selectedBudget.getServices().size()) {
			progressDialog.dismiss();
			DialogFragment newFragment = new BudgetItemDialog(getApplicationContext(), services, selectedBudget, this);
			newFragment
					.show(getSupportFragmentManager(), "itemdetails");	
		}

		
	}
	@Override
	public void onBudgetStatusChanged(Budget budget) {
//		progressDialog = ProgressDialog.show(
//				ViewBudgets.this, "Tumascotik",
//				getResources().getString(R.string.));
		
		ParseBudgetProvider.updateBugetStatus(this, this, budget);
		
	}
	@Override
	public void OnStatusChangedFinished(boolean b, Budget budget) {
		if (b) {
			BudgetProvider.updateBudget(this, budget);
		}
		progressDialog.dismiss();
		refreshListView();
		
	}
}
