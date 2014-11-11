package com.arawaney.tumascotik.client.db;

public class SocialNetworkEntity {
	public static final String TABLE = "social_network";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_FIELD = "field";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_ENABLED = "enabled";
	
	

	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_SOCIAL_NETWORK = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_SYSTEM_ID
			+ " TEXT PRIMARY KEY, " 
			+ COLUMN_NAME 
			+ " TEXT, "
			+ COLUMN_FIELD
			+ " TEXT, "
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_ENABLED
			+ " INTEGER);";

}
