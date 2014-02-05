package com.arawaney.tumascotik.client.db;

public class ServiceBudgetEntity {
	public static final String TABLE = "serviceBudget";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SERVICE_SYSTEM_ID = "service_system_id";
	public static final String COLUMN_BUDGET_ID = "budget_id";




	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_SERVICE_BUDGET = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SERVICE_SYSTEM_ID
			+ " TEXT, "
			+ COLUMN_BUDGET_ID 
			+ " INTEGER);";


}
