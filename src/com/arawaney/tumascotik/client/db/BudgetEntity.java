package com.arawaney.tumascotik.client.db;

public class BudgetEntity {
	public static final String TABLE = "budget";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_TOTAL = "total";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_DELIVERY = "delivery";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_CREATED_AT = "created_at";



	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_BUDGET = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SYSTEM_ID
			+ " TEXT, " 
			+ COLUMN_TOTAL 
			+ " INTEGER, " 
			+ COLUMN_STATUS 
			+ " INTEGER, " 
			+ COLUMN_DELIVERY 
			+ " INTEGER, " 
			+ COLUMN_ACTIVE
			+ " INTEGER, " 
		    + COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_CREATED_AT 
			+ " INTEGER, " 
			+ COLUMN_USER_ID 
			+ " TEXT);";


}
