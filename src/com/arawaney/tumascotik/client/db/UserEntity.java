package com.arawaney.tumascotik.client.db;

public class UserEntity {
	public static final String TABLE = "user";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SYSTEM_ID = "system_id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LASTNAME = "lastname";
	public static final String COLUMN_CEDULA = "cedula";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_GENDER = "gender";
	public static final String COLUMN_TELEPHONE_MOBILE = "mobile_telephone";
	public static final String COLUMN_TELEPHONE_HOUSE = "house_telephone";
	public static final String COLUMN_ADMIN = "admin";
	public static final String COLUMN_UPDATED_AT = "updated_at";

	public static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
			+ TABLE 
			+ " (" 
			+ COLUMN_ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COLUMN_SYSTEM_ID
			+ " TEXT, " 
			+ COLUMN_USERNAME
			+ " TEXT, " 
			+ COLUMN_PASSWORD 
			+ " TEXT, " 
			+ COLUMN_NAME 
			+ " TEXT, "
			+ COLUMN_LASTNAME 
			+ " TEXT, " 
			+ COLUMN_CEDULA 
			+ " INTEGER, "
			+ COLUMN_GENDER 
			+ " INTEGER, " 
			+ COLUMN_ADDRESS 
			+ " TEXT, " 
			+ COLUMN_EMAIL 
			+ " TEXT, " 
			+ COLUMN_TELEPHONE_MOBILE
			+ " TEXT, " 
			+ COLUMN_TELEPHONE_HOUSE
			+ " TEXT, " 
			+ COLUMN_UPDATED_AT 
			+ " INTEGER, " 
			+ COLUMN_ADMIN 
			+ " INTEGER);";

}
