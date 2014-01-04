package com.arawaney.tumascotik.client.db;

public class PetPropertieEntity {
	public static final String TABLE = "pet";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_BREED_ID = "breed_id";
	public static final String COLUMN_NAME = "name";



	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_PETPROPERTIES = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SYSTEM_ID
			+ " TEXT, " 
			+ COLUMN_BREED_ID
			+ " TEXT, "
			+ COLUMN_NAME 
			+ " TEXT);";
}