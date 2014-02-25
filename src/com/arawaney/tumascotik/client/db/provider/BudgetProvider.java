package com.arawaney.tumascotik.client.db.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.arawaney.tumascotik.client.db.PetEntity;
import com.arawaney.tumascotik.client.db.BudgetEntity;
import com.arawaney.tumascotik.client.db.ServiceBudgetEntity;
import com.arawaney.tumascotik.client.db.TumascotikProvider;
import com.arawaney.tumascotik.client.model.BudgetService;
import com.arawaney.tumascotik.client.model.Pet;
import com.arawaney.tumascotik.client.model.Budget;
import com.arawaney.tumascotik.client.model.Service;
import com.arawaney.tumascotik.client.model.User;
import com.arawaney.tumascotik.client.util.CalendarUtil;

public class BudgetProvider {
	private static final String LOG_TAG = "Tumascotik-Client-BudgetProvider";

	public static final Uri URI_BUDGET = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/" + BudgetEntity.TABLE);
	public static final Uri URI_SERVICE_BUDGET = Uri.parse("content://"
			+ TumascotikProvider.PROVIDER_NAME + "/"
			+ ServiceBudgetEntity.TABLE);

	public static int insertBudget(Context context, Budget budget) {

		if (context == null || budget == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(BudgetEntity.COLUMN_SYSTEM_ID, budget.getSystem_id());

			if (budget.getUpdated_at() != null) {
				values.put(BudgetEntity.COLUMN_UPDATED_AT, budget
						.getUpdated_at().getTimeInMillis());
			}
			if (budget.getCreatedAt() != null) {
				values.put(BudgetEntity.COLUMN_CREATED_AT, budget
						.getCreatedAt().getTimeInMillis());
			}
			values.put(BudgetEntity.COLUMN_STATUS, budget.getStatus());
			values.put(BudgetEntity.COLUMN_DELIVERY, budget.isDelivery());
			values.put(BudgetEntity.COLUMN_ACTIVE, budget.isActive());
			values.put(BudgetEntity.COLUMN_USER_ID, budget.getUserId());
			values.put(BudgetEntity.COLUMN_TOTAL, budget.getTotal());

			final Uri result = context.getContentResolver().insert(URI_BUDGET,
					values);

			if (result != null) {
				int id = Integer.parseInt(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Budget :" + budget.getId()
							+ " has bee inserted");
					if (budget.getServices() != null) {
						for (Service service : budget.getServices()) {
							insertServiceBudget(context, id,
									service.getSystem_id());
						}
					}

					return id;
				} else
					Log.e(LOG_TAG, " Budget :" + budget.getId()
							+ " has not bee inserted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Budget :" + budget.getId()
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static int insertServiceBudget(Context context, long budgetId,
			String serviceId) {

		if (context == null || budgetId < 0 || serviceId == null)
			return -1;

		try {
			ContentValues values = new ContentValues();
			values.put(ServiceBudgetEntity.COLUMN_BUDGET_ID, budgetId);
			values.put(ServiceBudgetEntity.COLUMN_SERVICE_SYSTEM_ID, serviceId);

			final Uri result = context.getContentResolver().insert(
					URI_SERVICE_BUDGET, values);

			if (result != null) {
				int id = Integer.parseInt(result.getPathSegments().get(1));
				if (id > 0) {
					Log.i(LOG_TAG, " Budget/Service :" + budgetId + "/"
							+ serviceId + " has bee inserted");
					return id;
				} else
					Log.i(LOG_TAG, " Budget/Service :" + budgetId + "/"
							+ serviceId + " has not bee inserted");

			}
		} catch (Exception e) {
			Log.i(LOG_TAG, " Budget/Service :" + budgetId + "/" + serviceId
					+ " has not bee inserted");
			e.printStackTrace();
		}
		return -1;

	}

	public static boolean updateBudget(Context context, Budget budget) {

		if (context == null || budget == null)
			return false;

		try {
			ContentValues values = new ContentValues();
			values.put(BudgetEntity.COLUMN_ID, budget.getId());
			values.put(BudgetEntity.COLUMN_SYSTEM_ID, budget.getSystem_id());

			if (budget.getUpdated_at() != null) {
				values.put(BudgetEntity.COLUMN_UPDATED_AT, budget
						.getUpdated_at().getTimeInMillis());
			}
			if (budget.getCreatedAt() != null) {
				values.put(BudgetEntity.COLUMN_CREATED_AT, budget
						.getCreatedAt().getTimeInMillis());
				Log.d(LOG_TAG, CalendarUtil.getDateFormated(budget
						.getCreatedAt(), "dd/MM/yyyy"));

			}
			
			values.put(BudgetEntity.COLUMN_DELIVERY, budget.isDelivery());
			values.put(BudgetEntity.COLUMN_ACTIVE, budget.isActive());

			if (budget.getStatus() != 0) {
				values.put(BudgetEntity.COLUMN_STATUS, budget.getStatus());
			}
			if (budget.getTotal() != null) {
				values.put(BudgetEntity.COLUMN_TOTAL, budget.getTotal());
			}
			String condition = BudgetEntity.COLUMN_ID + " = " + "'"
					+ budget.getId() + "'";

			int row = context.getContentResolver().update(URI_BUDGET, values,
					condition, null);

			if (row == 1) {
				Log.i(LOG_TAG, " Budget :" + budget.getId()
						+ " has bee updated");

				if (budget.getServices() != null) {
					for (Service service : budget.getServices()) {
						if (!serviceBudgetExists(context, budget.getId(),service.getSystem_id())) {
							insertServiceBudget(context, budget.getId(),
									service.getSystem_id());
						}
						
					}
				}

				return true;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, " Budget :" + budget.getId()
					+ " has not bee updated");
			e.printStackTrace();
		}
		return false;
	}

	private static boolean serviceBudgetExists(Context context, long budgetId,
			String serviceSystemId) {
		if (context == null || serviceSystemId == null|| budgetId <0)
			return false;

		String condition = ServiceBudgetEntity.COLUMN_BUDGET_ID + " = "
				+ "'" + budgetId + "'";
		condition = condition +" AND "+ServiceBudgetEntity.COLUMN_SERVICE_SYSTEM_ID + " = "
				+ "'" + serviceSystemId + "'";

		final Cursor cursor = context.getContentResolver().query(
				URI_SERVICE_BUDGET, null, condition, null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return false;
		}
		else{
			return true;
		}
		
				}

	public static Budget readBudget(Context context, String budgetSystemID) {

		if (context == null)
			return null;

		String condition = BudgetEntity.COLUMN_SYSTEM_ID + " = " + "'"
				+ String.valueOf(budgetSystemID) + "'";

		final Cursor cursor = context.getContentResolver().query(URI_BUDGET,
				null, condition, null, null);

		Budget budget = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_SYSTEM_ID));
					final Integer total = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_TOTAL));
					final int status = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ACTIVE));
					final String userId = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_USER_ID));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_UPDATED_AT));
					final long created_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_CREATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);
					Calendar createdAt = Calendar.getInstance();
					createdAt.setTimeInMillis(created_at);

					budget = new Budget();
					budget.setId(id);
					budget.setSystem_id(system_id);
					budget.setTotal(total);
					budget.setStatus(status);
					budget.setDelivery(delivery);
					budget.setActive(active);
					budget.setUpdated_at(updatedAt);
					budget.setCreatedAt(createdAt);
					budget.setUserId(userId);

					List<Service> services = readServiceByBudget(context,
							id);
					if (services != null) {
						budget.setServices(services);
					}

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			budget = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return budget;
	}

	public static boolean removeBudget(Context context, long budgetId) {

		try {
			String condition = BudgetEntity.COLUMN_ID + " = " + "'"
					+ String.valueOf(budgetId) + "'";
			int rows = context.getContentResolver().delete(URI_BUDGET,
					condition, null);

			if (rows == 1) {
				Log.i(LOG_TAG, "Budget : " + budgetId + "has been deleted");
				removeServiceBudgetbyBudget(context, budgetId);
				return true;
			} else {
				Log.i(LOG_TAG, "Budget : " + budgetId + "has not been deleted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting budget: " + e.getMessage());
		}
		return false;
	}

	private static void removeServiceBudgetbyBudget(Context context, long budgetId) {

		try {
			String condition = ServiceBudgetEntity.COLUMN_BUDGET_ID + " = " + "'"
					+ String.valueOf(budgetId) + "'";
			int rows = context.getContentResolver().delete(URI_SERVICE_BUDGET,
					condition, null);

			if (rows > 0) {
				Log.i(LOG_TAG, "Budget : " + budgetId + "ServiceBUdgets: "+rows+ "have been deleted");
	
			} else {
				Log.i(LOG_TAG, "Budget : " + budgetId + "has not been deleted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting budget: " + e.getMessage());
		}
	}

	public static ArrayList<Budget> readBudgets(Context context) {
		if (context == null)
			return null;

		ArrayList<Budget> budgets = new ArrayList<Budget>();

		final Cursor cursor = context.getContentResolver().query(URI_BUDGET,
				null, null, null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_SYSTEM_ID));
					final Integer total = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_TOTAL));
					final int status = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ACTIVE));
					final String userId = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_USER_ID));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_UPDATED_AT));
					final long created_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_CREATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);
					Calendar createdAt = Calendar.getInstance();
					createdAt.setTimeInMillis(created_at);

					Budget budget = new Budget();
					budget.setId(id);
					budget.setSystem_id(system_id);
					budget.setTotal(total);
					budget.setStatus(status);
					budget.setDelivery(delivery);
					budget.setActive(active);
					budget.setUpdated_at(updatedAt);
					budget.setCreatedAt(createdAt);
					budget.setUserId(userId);

					List<Service> services = readServiceByBudget(context,
							id);
					if (services != null) {
						budget.setServices(services);
					}

					budgets.add(budget);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			budgets = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return budgets;
	}

	public static ArrayList<Service> readServiceByBudget(Context context,
			long budgetId) {
		if (context == null || budgetId < 0)
			return null;

		ArrayList<Service> services = new ArrayList<Service>();

		String condition = ServiceBudgetEntity.COLUMN_BUDGET_ID + " = "
				+ "'" + budgetId + "'";

		final Cursor cursor = context.getContentResolver().query(
				URI_SERVICE_BUDGET, null, condition, null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final String serviceSystemId = cursor
							.getString(cursor
									.getColumnIndex(ServiceBudgetEntity.COLUMN_SERVICE_SYSTEM_ID));

					Service service = ServiceProvider.readMotive(context,
							serviceSystemId);

					if (service != null) {
						services.add(service);
					}

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			services = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return services;
	}

	public static Date getLastUpdate(Context context) {
		final Cursor cursor = context.getContentResolver().query(URI_BUDGET,
				null, null, null, BudgetEntity.COLUMN_UPDATED_AT + " DESC");

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				final long updated_at = cursor.getLong(cursor
						.getColumnIndex(BudgetEntity.COLUMN_UPDATED_AT));
				Date date = new Date(updated_at);
				Log.d(LOG_TAG,
						"last update "
								+ CalendarUtil.getDateFormated(date,
										"dd MM yyy mm:ss"));

				return date;
			}

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}

		return null;
	}

	public static Budget readInWorkBudget(Context context) {

		if (context == null)
			return null;

		String condition = BudgetEntity.COLUMN_STATUS + " = " + "'"
				+ String.valueOf(Budget.STATUS_WORKING) + "'";

		final Cursor cursor = context.getContentResolver().query(URI_BUDGET,
				null, condition, null, null);

		Budget budget = null;

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {
					final long id = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_SYSTEM_ID));
					final Integer total = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_TOTAL));
					final int status = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ACTIVE));
					final String userId = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_USER_ID));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_UPDATED_AT));
					final long created_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_CREATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);
					Calendar createdAt = Calendar.getInstance();
					createdAt.setTimeInMillis(created_at);

					budget = new Budget();
					budget.setId(id);
					budget.setSystem_id(system_id);
					budget.setTotal(total);
					budget.setStatus(status);
					budget.setDelivery(delivery);
					budget.setActive(active);
					budget.setUpdated_at(updatedAt);
					budget.setCreatedAt(createdAt);
					budget.setUserId(userId);

					List<Service> services = readServiceByBudget(context,
							id);
					if (services != null) {
						budget.setServices(services);
					}

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			budget = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return budget;
	}
	
	public static ArrayList<Budget> readNotInProgress(Context context) {
		if (context == null)
			return null;

		ArrayList<Budget> budgets = new ArrayList<Budget>();
		
		String condition = BudgetEntity.COLUMN_STATUS + " <> " + "'"
				+ String.valueOf(Budget.STATUS_WORKING) + "'";
		
		final Cursor cursor = context.getContentResolver().query(URI_BUDGET,
				null, condition, null, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		try {
			if (cursor.moveToFirst()) {

				do {

					final long id = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ID));
					final String system_id = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_SYSTEM_ID));
					final Integer total = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_TOTAL));
					final int status = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_STATUS));
					final Integer delivery = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_DELIVERY));
					final Integer active = cursor.getInt(cursor
							.getColumnIndex(BudgetEntity.COLUMN_ACTIVE));
					final String userId = cursor.getString(cursor
							.getColumnIndex(BudgetEntity.COLUMN_USER_ID));
					final long updated_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_UPDATED_AT));
					final long created_at = cursor.getLong(cursor
							.getColumnIndex(BudgetEntity.COLUMN_CREATED_AT));

					Calendar updatedAt = Calendar.getInstance();
					updatedAt.setTimeInMillis(updated_at);
					Calendar createdAt = Calendar.getInstance();
					createdAt.setTimeInMillis(created_at);

					Budget budget = new Budget();
					budget.setId(id);
					budget.setSystem_id(system_id);
					budget.setTotal(total);
					budget.setStatus(status);
					budget.setDelivery(delivery);
					budget.setActive(active);
					budget.setUpdated_at(updatedAt);
					budget.setCreatedAt(createdAt);
					Log.d(LOG_TAG, CalendarUtil.getDateFormated(createdAt, "dd/MM/yyyy"));
					budget.setUserId(userId);

					List<Service> services = readServiceByBudget(context,
							id);
					if (services != null) {
						budget.setServices(services);
					}

					budgets.add(budget);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			budgets = null;
			Log.e(LOG_TAG, "Error : " + e.getMessage());
		} finally {
			cursor.close();
		}
		return budgets;
	}
	public static void removeBudgetService(Context context, long budgetId,
			String serviceId) {

		if (context == null || budgetId < 0 || serviceId == null)
			return;
		
		



		try {
			
			String condition = ServiceBudgetEntity.COLUMN_BUDGET_ID + " = "
					+ "'" + budgetId + "'";
			condition = condition +" AND "+ServiceBudgetEntity.COLUMN_SERVICE_SYSTEM_ID + " = "
					+ "'" + serviceId + "'";
		
			int rows = context.getContentResolver().delete(URI_SERVICE_BUDGET,
					condition, null);

			if (rows > 0) {
				Log.i(LOG_TAG, "Budget : " + budgetId + "ServiceBUdgets: "+rows+ "have been deleted");
	
			} else {
				Log.i(LOG_TAG, "Budget : " + budgetId + "has not been deleted");

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error deleting budget: " + e.getMessage());
		}
	



		
	}
}
