package com.arawaney.tumascotik.client.db;

public class ServiceEntity {
	public static final String TABLE = "service";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NEED_REQUEST = "need_request";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_DURATION = "duration";



	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_SERVICE= "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SYSTEM_ID
			+ " TEXT, " 
			+ COLUMN_NEED_REQUEST
			+ " INTEGER, "
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, "
			+ COLUMN_DURATION 
			+ " INTEGER, "
			+ COLUMN_NAME 
			+ " TEXT);";
}
