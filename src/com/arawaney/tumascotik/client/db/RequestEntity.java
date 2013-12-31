package com.arawaney.tumascotik.client.db;

public class RequestEntity {
	public static final String TABLE = "request";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_FINISH_DATE = "finisch_date";
	public static final String COLUMN_SERVICE = "service";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_DELIVERY = "delivery";
	public static final String COLUMN_IS_APPOINTMENT = "is_appointment";
	public static final String COLUMN_ACTIVE = "active";
	public static final String COLUMN_PET_ID = "pet_id";


	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_REQUEST = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SYSTEM_ID
			+ " TEXT, " 
			+ COLUMN_START_DATE
			+ " INTEGER, " 
			+ COLUMN_FINISH_DATE 
			+ " INTEGER, " 
			+ COLUMN_SERVICE 
			+ " TEXT, "
			+ COLUMN_PRICE 
			+ " INTEGER, " 
			+ COLUMN_COMMENT 
			+ " TEXT, "
			+ COLUMN_IS_APPOINTMENT 
			+ " INTEGER, " 
			+ COLUMN_STATUS 
			+ " INTEGER, " 
			+ COLUMN_DELIVERY 
			+ " INTEGER, " 
			+ COLUMN_ACTIVE
			+ " INTEGER, " 
			+ COLUMN_PET_ID 
			+ " TEXT);";


}
