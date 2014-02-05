package com.arawaney.tumascotik.client.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arawaney.tumascotik.client.R;
import com.arawaney.tumascotik.client.adapter.BudgetItemListBaseAdapter;
import com.arawaney.tumascotik.client.backend.ParseBudgetProvider;
import com.arawaney.tumascotik.client.db.BudgetDB;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.db.provider.RequestProvider;
import com.arawaney.tumascotik.client.dialog.PresupuestoAlertDialog;
import com.arawaney.tumascotik.client.dialog.PresupuestoDeleteallDialog;
import com.arawaney.tumascotik.client.dialog.PresupuestoDialog;
import com.arawaney.tumascotik.client.dialog.PresupuestoItemDialog;
import com.arawaney.tumascotik.client.listener.ParseBudgetListener;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetItemDetails;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Request;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.util.FontUtil;

public class BudgetActivity extends FragmentActivity implements
		ParseBudgetListener {
	private static final String LOG_TAG = "Tumascotik-Client-BudgetActivity";

	ImageView add;
	ImageView sendBudget;
	ImageView deleteall;
	ListView budgetsLists;
	RelativeLayout zeroDataLayout;
	TextView totalprice;
	Boolean flagclick;
	ProgressDialog progressDialog;
	TextView zeroDataText;

	

	int total;

	Budget budget;
	List<Service> services;
	ArrayList<BudgetService> budgetServices;
	int totalServices;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);

		loadViews();

		initializeTotalPrice();

		loadCLickEvents();

		refreshView();
	}

	private void initializeTotalPrice() {
		total = 0;
		totalprice.setText("Bs " + String.valueOf(total));
	}

	private void loadCLickEvents() {

		budgetsLists.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
			
					Object object = budgetsLists.getItemAtPosition(position);
					BudgetService objectBudgetService = (BudgetService) object;
					DialogFragment newFragment = new PresupuestoItemDialog(
							objectBudgetService, getApplicationContext(),
							budget.getId());
					newFragment.show(getSupportFragmentManager(), "presupitem");
				}
			
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment newFragment = new PresupuestoDialog(
						BudgetActivity.this);
				newFragment.show(getSupportFragmentManager(), "presup");

			}
		});
		sendBudget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new PresupuestoAlertDialog(total);
				newFragment.show(getSupportFragmentManager(), "presualert");

			}
		});
		deleteall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new PresupuestoDeleteallDialog();
				newFragment.show(getSupportFragmentManager(), "presudelete");

			}
		});
	}

	public void refreshView() {
		budget = null;
		budget = getSavedBudget();

		budgetServices = new ArrayList<BudgetService>();
		total = 0;

		if (budget == null) {
			setEmptyDataView();
		} else if (budget.getServices() == null) {
			setEmptyDataView();
		} else {
			setListView();

		}

	}

	private void setListView() {
		deleteall.setClickable(true);
		sendBudget.setClickable(true);

		budgetsLists.setVisibility(View.VISIBLE);
		zeroDataLayout.setVisibility(View.GONE);

		totalServices = budget.getServices().size();

		updatePrices();

	}

	private void setUpdatingPricesDialog() {
		progressDialog = ProgressDialog.show(this, "", getResources()
				.getString(R.string.budget_updating_prices));
	}

	private void updatePrices() {
		setUpdatingPricesDialog();
		List<Service> services = budget.getServices();
		ParseBudgetProvider.readPrices(services, this);
	}

	private void setEmptyDataView() {
		deleteall.setClickable(false);
		sendBudget.setClickable(false);

		budgetsLists.setVisibility(View.GONE);
		zeroDataLayout.setVisibility(View.VISIBLE);

	}

	private Budget getSavedBudget() {
		Budget budget = BudgetProvider.readInWorkBudget(this);

		return budget;
	}

	private void loadViews() {

		add = (ImageView) findViewById(R.id.bagregpresp);
		sendBudget = (ImageView) findViewById(R.id.bhacerpedidopresp);
		deleteall = (ImageView) findViewById(R.id.bdeleteallpresp);
		totalprice = (TextView) findViewById(R.id.txttotal);
		budgetsLists = (ListView) findViewById(R.id.listview_budgets);
		zeroDataLayout = (RelativeLayout) findViewById(R.id.layout_budget_zero_data);
		zeroDataText = (TextView) findViewById(R.id.textView_budget_zero_data);
		
		totalprice.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));
	    zeroDataText.setTypeface(FontUtil.getTypeface(this, FontUtil.ROBOTO_LIGHT));

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void DeleteAll() {

		BudgetProvider.removeBudget(this, budget.getId());
		refreshView();
		total = 0;
		totalprice.setText("Bs.0");
	}

	@Override
	public void OnBudgetInserted(boolean b, String objectId) {

		if (b) {
			budget.setSystem_id(objectId);
			BudgetProvider.updateBudget(this, budget);
			refreshView();
		} else {
			budget.setStatus(Budget.STATUS_WORKING);
		}
		
		dismissDialog();
     
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

		budgetServices.add(budgetService);
		total = total + budgetService.getPrice();
		totalprice.setText("Bs." + String.valueOf(total));

		totalServices--;
		if (totalServices == 0) {
			dismissDialog();
			budgetsLists.setAdapter(new BudgetItemListBaseAdapter(this,
					budgetServices));
		}

	}

	private void dismissDialog() {
		progressDialog.dismiss();
	}

	public void sendBudget() {
		budget.setStatus(Budget.STATUS_IN_PROCESS);
		budget.setTotal(total);
		setSendingOrderDialog();
		ParseBudgetProvider.sendBudget(this, this, budget);

	}

	private void setSendingOrderDialog() {
		progressDialog = ProgressDialog.show(this, "", getResources()
				.getString(R.string.budget_updating_prices));

	}

}
