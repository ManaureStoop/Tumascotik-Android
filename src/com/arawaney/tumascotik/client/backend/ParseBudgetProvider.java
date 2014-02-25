package com.arawaney.tumascotik.client.backend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.arawaney.tumascotik.client.control.MainController;
import com.arawaney.tumascotik.client.db.provider.BudgetProvider;
import com.arawaney.tumascotik.client.listener.ParseBudgetListener;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ParseBudgetProvider {
	private static final String LOG_TAG = "Tumascotik-Client-ParseBudgetProvider";

	private static final String DELIVERY_TAG = "delivery";
	private static final String UPDATED_AT_TAG = "updatedAt";
	private static final String CREATED_AT_TAG = "createdAt";
	private static final String ACTIVE_TAG = "active";
	private static final String USER_ID_TAG = "userId";
	private static final String STATUS_ID_TAG = "statusId";
	private static final String BUDGET_ID_TAG = "budgetId";
	private static final String SERVICE_ID_TAG = "serviceId";
	private static final String TOTAL_TAG = "total";
	private static final String STATUS_NUMBER_TAG = "statusNumber";
	private static final String PRICE_TAG = "price";

	private static final String BUDGET_TABLE = "Budget";
	private static final String SERVICE_BUDGET_TABLE = "Service_Budget";
	private static final String STATUS_TABLE = "Status";
	private static final String PRICE_TABLE = "Price";


	public static void sendBudget(Context context,
			final ParseBudgetListener listener, final Budget budget) {
		final ParseObject parseBudget = new ParseObject(BUDGET_TABLE);

		parseBudget.put(USER_ID_TAG, budget.getUserId());

		if (budget.isDelivery() == Budget.IS_DELIVERY) {
			parseBudget.put(DELIVERY_TAG, true);
		} else
			parseBudget.put(DELIVERY_TAG, false);
		if (budget.isActive() == Budget.ACTIVE) {
			parseBudget.put(ACTIVE_TAG, true);
		} else
			parseBudget.put(ACTIVE_TAG, false);
		
		parseBudget.put(TOTAL_TAG,	budget.getTotal());

		parseBudget.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					listener.OnBudgetInserted(true, parseBudget.getObjectId());
					saveNewBudgetStatus();
					saveBudgetService();

				} else {
					Log.d(LOG_TAG, "Error saving budget: " + e.getMessage());
					listener.OnBudgetInserted(false, parseBudget.getObjectId());
				}
			}

			private void saveNewBudgetStatus() {

				int statusNUm = budget.getStatus();
				ParseQuery<ParseObject> innerQuery = new ParseQuery<ParseObject>(
						STATUS_TABLE);
				innerQuery.whereEqualTo(STATUS_NUMBER_TAG, statusNUm);
				innerQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> status, ParseException e) {
						if (e == null) {
							parseBudget.put(STATUS_ID_TAG, status.get(0)
									.getObjectId());
							parseBudget.saveInBackground();

						} else {
							Log.e(LOG_TAG,
									"No status matches inserting new user "
											+ e.getMessage());

						}

					}
				});

			}

			private void saveBudgetService() {
				for (Service service : budget.getServices()) {

					final ParseObject parseBudgetService = new ParseObject(
							SERVICE_BUDGET_TABLE);
					parseBudgetService.put(SERVICE_ID_TAG,
							service.getSystem_id());
					parseBudgetService.put(BUDGET_ID_TAG,
							parseBudget.getObjectId());

					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							PRICE_TABLE);
					query.whereEqualTo(SERVICE_ID_TAG, service.getSystem_id());

					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> prices,
								ParseException e) {
							if (e == null) {
								if (!prices.isEmpty()) {
									parseBudgetService.put(PRICE_TAG, prices
											.get(0).get(PRICE_TAG));
								}

							} else {
								e.printStackTrace();
							}

							parseBudgetService.saveInBackground();

						}
					});

				}
			}

		});
	}

	public static void updateBudgets(Context context,
			final ParseBudgetListener listener) {
		Date updateAt = BudgetProvider.getLastUpdate(context);
		User user = MainController.USER;
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				BUDGET_TABLE);
		query.whereEqualTo(USER_ID_TAG, user.getSystemId());
		if (updateAt != null) {
			query.whereGreaterThan(UPDATED_AT_TAG, updateAt);
		}
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> cList, ParseException e) {

				if (e == null) {
					ArrayList<Budget> budgets = new ArrayList<Budget>();

					for (ParseObject object : cList) {
						Budget budget = readBudgetFromCursor(object,
								listener);
						budget.setSystem_id(object.getObjectId());
						budgets.add(budget);
					}

					listener.OnAllBudgetsQueryFinished(budgets);

				} else {
					listener.OnAllBudgetsQueryFinished(null);
					Log.d(LOG_TAG,
							" Query error getting Budgets"
									 + ": " + e.getMessage());
				}

			}

			private Budget readBudgetFromCursor(ParseObject parsedBudget,
				 final ParseBudgetListener listener) {
				final Budget budget = new Budget();

				if (parsedBudget.getBoolean(ACTIVE_TAG)) {
					budget.setActive(Budget.ACTIVE);
				} else
					budget.setActive(Budget.INACTIVE);

				if (parsedBudget.getBoolean(DELIVERY_TAG)) {
					budget.setDelivery(Budget.IS_DELIVERY);
				} else
					budget.setDelivery(Budget.IS__NOT_DELIVERY);

			

				Calendar updateCalendar = Calendar.getInstance();

				updateCalendar.setTimeInMillis(parsedBudget.getUpdatedAt()
						.getTime());
				
				Calendar createCalendar = Calendar.getInstance();

				createCalendar.setTimeInMillis(parsedBudget.getCreatedAt()
						.getTime());


				budget.setUpdated_at(updateCalendar);
				
				budget.setCreatedAt(createCalendar);
				
				budget.setTotal(parsedBudget.getInt(TOTAL_TAG));

				budget.setUserId(MainController.USER.getSystemId());

				budget.setSystem_id(parsedBudget.getObjectId());

				String statusId = parsedBudget.getString(STATUS_ID_TAG);

				ParseQuery<ParseObject> statusQuery = new ParseQuery<ParseObject>(
						STATUS_TABLE);
				statusQuery.whereEqualTo("objectId", statusId);

				statusQuery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> status, ParseException e) {
						if (e == null) {
							budget.setStatus(status.get(0).getInt(
									STATUS_NUMBER_TAG));
							listener.onBudgetQueryFInished(budget);
						} else {
							Log.d(LOG_TAG,
									"Error getting status from budget: "
											+ budget.getSystem_id() + " "
											+ e.getMessage());
						}
					}
				});
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						SERVICE_BUDGET_TABLE);
				query.whereEqualTo(BUDGET_ID_TAG, parsedBudget.getObjectId());
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> serviceBudgets,
							ParseException e) {
						if (e == null) {
							if (serviceBudgets.size() > 0) {
								for (ParseObject parseObject : serviceBudgets) {
									Service service = new Service();
									service.setSystem_id(parseObject.getString(SERVICE_ID_TAG));
									budget.addService(service);
								}
							} else {
								
							}
							listener.onBudgetQueryFInished(budget);

						} else
							Log.d(LOG_TAG,
									"Error getting Service_Budgets from budget: "
											+ budget.getSystem_id() + " "
											+ e.getMessage());

					}
				});

				return budget;
			}

		});

	}

//	public static void cancelBudget(Context context,
//			final ParseBudgetListener listener, Budget budget) {
//
//		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
//				BUDGET_TABLE);
//		query.whereEqualTo("objectId", budget.getSystem_id());
//		query.findInBackground(new FindCallback<ParseObject>() {
//
//			@Override
//			public void done(List<ParseObject> parsedBudget, ParseException e) {
//				if (e == null) {
//					parsedBudget.get(0).put(STATUS_ID_TAG,
//							Budget.STATUS_CANCELED);
//					parsedBudget.get(0).saveInBackground();
//					listener.onCanceledQueryFinished(true);
//				} else {
//					Log.d(LOG_TAG,
//							"Error getting budget to cancel : "
//									+ e.getMessage());
//					listener.onCanceledQueryFinished(false);
//
//				}
//			}
//		});
//
//	}

	public static void removeBudget(final Budget budget,
			final ParseBudgetListener listener) {

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
				BUDGET_TABLE);
		query.whereEqualTo("objectId", budget.getSystem_id());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> parsedBudget, ParseException e) {
				if (e == null) {
					try {

						parsedBudget.get(0).delete();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							SERVICE_BUDGET_TABLE);
					query.whereMatches(BUDGET_ID_TAG, budget.getSystem_id());
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> list,
								ParseException arg1) {
							try {
								list.get(0).delete();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});
					Log.d(LOG_TAG, "HOOLA");
					listener.onBudgetRemoveFinished(budget);

				} else {
					Log.d(LOG_TAG,
							"Error getting budget to delete : "
									+ e.getMessage());

				}
			}
		});

	}

	public static void readPrices(List<Service> services,
			final ParseBudgetListener listener) {

		for (final Service service : services) {

			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					PRICE_TABLE);
			query.whereEqualTo(SERVICE_ID_TAG, service.getSystem_id());

			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> prices,
						ParseException e) {
					if (e == null) {
						if (!prices.isEmpty()) {
							BudgetService budgetService = new BudgetService();
							budgetService.setService(service);
							budgetService.setPrice(prices.get(0).getInt(PRICE_TAG));
							listener.onOnePriceQueryFinished(budgetService);
						}

					} else {
						e.printStackTrace();
					}

				}
			});

		}
		
	}
	
	public static void readSavedPrices(List<Service> services,Budget budget, 
			final ParseBudgetListener listener) {

		for (final Service service : services) {

			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
					SERVICE_BUDGET_TABLE);
			query.whereEqualTo(SERVICE_ID_TAG, service.getSystem_id());
			query.whereEqualTo(BUDGET_ID_TAG, budget.getSystem_id());

			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> prices,
						ParseException e) {
					if (e == null) {
						if (!prices.isEmpty()) {
							BudgetService budgetService = new BudgetService();
							budgetService.setService(service);
							budgetService.setPrice(prices.get(0).getInt(PRICE_TAG));
							listener.onOnePriceQueryFinished(budgetService);
						}

					} else {
						e.printStackTrace();
					}

				}
			});

		}
		
	}

}
