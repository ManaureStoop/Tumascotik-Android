package com.arawaney.tumascotik.client.db;

public class PetEntity {
	public static final String TABLE = "pet";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_USER_ID = "owner_id";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_BREED_ID = "breed_id";
	public static final String COLUMN_GENDER = "gender";
	public static final String COLUMN_PUPPY = "puppy";
	public static final String COLUMN_AGRESSIVE = "agressive";
	public static final String COLUMN_UPDATED_AT = "updated_at";

	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_PET = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SYSTEM_ID
			+ " TEXT, " 
			+ COLUMN_NAME 
			+ " TEXT, "
			+ COLUMN_USER_ID 
			+ " INTEGER, "
			+ COLUMN_COMMENT 
			+ " TEXT, " 
			+ COLUMN_BREED_ID 
			+ " TEXT, "
			+ COLUMN_GENDER 
			+ " INTEGER, " 
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_PUPPY 
			+ " INTEGER, " 
			+ COLUMN_AGRESSIVE
			+ " INTEGER);";
}
