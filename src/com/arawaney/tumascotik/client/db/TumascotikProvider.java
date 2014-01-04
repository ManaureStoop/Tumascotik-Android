package com.arawaney.tumascotik.client.db;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class TumascotikProvider extends ContentProvider {

	private static final String LOG_TAG = "Tumascotik-Client-Provider";
	public static final String PROVIDER_NAME = "com.arawaney.tumascotik.client.db.contentprovider";

	// Identifiers

	private static final int USERS = 1;
	private static final int USER_ID = 2;
	private static final int PETS = 3;
	private static final int PET_ID = 4;
	private static final int REQUESTS = 5;
	private static final int REQUEST_ID = 6;
	private static final int SPECIES = 7;
	private static final int SPECIE_ID = 8;
	private static final int BREEDS = 9;
	private static final int BREED_ID = 10;
	private static final int PETPROPERTIES = 11;
	private static final int PETPROPERTIES_ID = 12;
	private static final int MOTIVES = 13;
	private static final int MOTIVE_ID = 14;

	// URIs

	private static final String CONTENT_USER = "content://" + PROVIDER_NAME
			+ "/" + UserEntity.TABLE;
	public static final Uri URI_USER = Uri.parse(CONTENT_USER);

	private static final String CONTENT_PET = "content://" + PROVIDER_NAME
			+ "/" + PetEntity.TABLE;
	public static final Uri URI_PET = Uri.parse(CONTENT_PET);

	private static final String CONTENT_REQUEST = "content://" + PROVIDER_NAME
			+ "/" + RequestEntity.TABLE;
	public static final Uri URI_REQUEST = Uri.parse(CONTENT_REQUEST);
	
	private static final String CONTENT_SPECIE = "content://" + PROVIDER_NAME
			+ "/" + SpecieEntity.TABLE;
	public static final Uri URI_SPECIE = Uri.parse(CONTENT_SPECIE);
	
	private static final String CONTENT_BREED = "content://" + PROVIDER_NAME
			+ "/" + BreedEntity.TABLE;
	public static final Uri URI_BREED = Uri.parse(CONTENT_BREED);
	
	private static final String CONTENT_PETPROPERTIES = "content://" + PROVIDER_NAME
			+ "/" + PetPropertieEntity.TABLE;
	public static final Uri URI_PET_PROPERTIES = Uri.parse(CONTENT_PETPROPERTIES);
	
	private static final String CONTENT_MOTIVE = "content://" + PROVIDER_NAME
			+ "/" + MotiveEntity.TABLE;
	public static final Uri URI_MOTIVE = Uri.parse(CONTENT_MOTIVE);

	// Content Types

	private static final String TYPE_USER_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + UserEntity.TABLE;
	private static final String TYPE_USER_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + UserEntity.TABLE;
	private static final String TYPE_PET_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + PetEntity.TABLE;
	private static final String TYPE_PET_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + PetEntity.TABLE;
	private static final String TYPE_REQUEST_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + RequestEntity.TABLE;
	private static final String TYPE_REQUEST_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + RequestEntity.TABLE;
	private static final String TYPE_SPECIE_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + SpecieEntity.TABLE;
	private static final String TYPE_SPECIE_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + SpecieEntity.TABLE;
	private static final String TYPE_BREED_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + BreedEntity.TABLE;
	private static final String TYPE_BREED_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + BreedEntity.TABLE;
	private static final String TYPE_PETPROPERTIES_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + PetPropertieEntity.TABLE;
	private static final String TYPE_PETPROPERTIES_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + PetPropertieEntity.TABLE;
	private static final String TYPE_MOTIVE_ITEM = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + MotiveEntity.TABLE;
	private static final String TYPE_MOTIVE_ITEMS = "android.cursor.item/vnd."
			+ PROVIDER_NAME + "." + MotiveEntity.TABLE;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		uriMatcher.addURI(PROVIDER_NAME, UserEntity.TABLE, USERS);
		uriMatcher.addURI(PROVIDER_NAME, UserEntity.TABLE + "/#", USER_ID);

		uriMatcher.addURI(PROVIDER_NAME, PetEntity.TABLE, PETS);
		uriMatcher.addURI(PROVIDER_NAME, PetEntity.TABLE + "/#", PET_ID);

		uriMatcher.addURI(PROVIDER_NAME, RequestEntity.TABLE, REQUESTS);
		uriMatcher
				.addURI(PROVIDER_NAME, RequestEntity.TABLE + "/#", REQUEST_ID);
		
		uriMatcher.addURI(PROVIDER_NAME, SpecieEntity.TABLE, SPECIES);
		uriMatcher
				.addURI(PROVIDER_NAME, SpecieEntity.TABLE + "/#", SPECIE_ID);
		
		uriMatcher.addURI(PROVIDER_NAME, BreedEntity.TABLE, BREEDS);
		uriMatcher
				.addURI(PROVIDER_NAME, BreedEntity.TABLE + "/#", BREED_ID);
		
		uriMatcher.addURI(PROVIDER_NAME, PetPropertieEntity.TABLE, PETPROPERTIES);
		uriMatcher
				.addURI(PROVIDER_NAME, PetPropertieEntity.TABLE + "/#", PETPROPERTIES_ID);
		
		uriMatcher.addURI(PROVIDER_NAME, MotiveEntity.TABLE, MOTIVES);
		uriMatcher
				.addURI(PROVIDER_NAME, MotiveEntity.TABLE + "/#", MOTIVE_ID);
	}

	public static final String DATABASE_NAME = "tumascotik_client_db";
	private SQLiteDatabase db;
	private DataBaseHelper dbHelper;
	private int dataBaseVersion = 1;

	@Override
	public boolean onCreate() {
		Log.d(LOG_TAG, "- on create");
		return createDataBaseHelper();
	}

	private boolean createDataBaseHelper() {
		Context context = getContext();

		try {
			Log.i(LOG_TAG, "Conten Provider - Database verion: "
					+ dataBaseVersion);
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
					dataBaseVersion);
			return true;

		} catch (Exception e) {
			Log.e(LOG_TAG, "Error: " + e.getMessage());
		}
		return false;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case USERS:
			return TYPE_USER_ITEMS;
		case USER_ID:
			return TYPE_USER_ITEM;
		case PETS:
			return TYPE_PET_ITEMS;
		case PET_ID:
			return TYPE_PET_ITEM;
		case REQUESTS:
			return TYPE_REQUEST_ITEMS;
		case REQUEST_ID:
			return TYPE_REQUEST_ITEM;
			
		case SPECIES:
			return TYPE_SPECIE_ITEMS;
		case SPECIE_ID:
			return TYPE_SPECIE_ITEM;
		case BREEDS:
			return TYPE_BREED_ITEMS;
		case BREED_ID:
			return TYPE_BREED_ITEM;
		case PETPROPERTIES:
			return TYPE_PETPROPERTIES_ITEMS;
		case PETPROPERTIES_ID:
			return TYPE_PETPROPERTIES_ITEM;
		case MOTIVES:
			return TYPE_MOTIVE_ITEMS;
		case MOTIVE_ID:
			return TYPE_MOTIVE_ITEM;
		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tableName = null;
		Uri target = null;

		switch (uriMatcher.match(uri)) {
		case USERS:
			tableName = UserEntity.TABLE;
			target = URI_USER;
			break;

		case PETS:
			tableName = PetEntity.TABLE;
			target = URI_PET;
			break;

		case REQUESTS:
			tableName = RequestEntity.TABLE;
			target = URI_REQUEST;
			break;

		case SPECIES:
			tableName = SpecieEntity.TABLE;
			target = URI_SPECIE;
			break;
			
		case BREEDS:
			tableName = BreedEntity.TABLE;
			target = URI_BREED;
			break;
			
		case PETPROPERTIES:
			tableName = PetPropertieEntity.TABLE;
			target = URI_PET_PROPERTIES;
			break;
			
		case MOTIVES:
			tableName = MotiveEntity.TABLE;
			target = URI_MOTIVE;
			break;
		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}

		if (tableName != null && target != null) {
			try {
				return insert(uri, values, tableName, target);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private Uri insert(Uri uri, ContentValues values, String tableName,
			Uri target) throws SQLException {
		Log.d(LOG_TAG, " - insert :" + uri);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());

		// add item
		long rowID = db.insert(tableName, "", values);

		if (rowID > 0) {
			// added successfully
			Uri itemUri = ContentUris.withAppendedId(target, rowID);
			getContext().getContentResolver().notifyChange(itemUri, null);
			return itemUri;
		}
		throw new SQLException("Failed to insert row into " + uri + " into "
				+ tableName);

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String tableName = null;
		Uri target = null;
		boolean single = false;

		switch (uriMatcher.match(uri)) {
		case USERS:
			tableName = UserEntity.TABLE;
			target = URI_USER;
			break;

		case USER_ID:
			tableName = UserEntity.TABLE;
			target = URI_USER;
			single = true;
			break;

		case PETS:
			tableName = PetEntity.TABLE;
			target = URI_PET;
			break;

		case PET_ID:
			tableName = PetEntity.TABLE;
			target = URI_PET;
			single = true;
			break;

		case REQUESTS:
			tableName = RequestEntity.TABLE;
			target = URI_REQUEST;
			break;

		case REQUEST_ID:
			tableName = RequestEntity.TABLE;
			target = URI_REQUEST;
			single = true;
			break;
			
		case SPECIES:
			tableName = SpecieEntity.TABLE;
			target = URI_SPECIE;
			break;
			
		case SPECIE_ID:
			tableName = SpecieEntity.TABLE;
			target = URI_SPECIE;
			single = true;
			break;
			
		case BREEDS:
			tableName = BreedEntity.TABLE;
			target = URI_BREED;
			break;
			
		case BREED_ID:
			tableName = BreedEntity.TABLE;
			target = URI_BREED;
			single = true;
			break;
			
		case PETPROPERTIES:
			tableName = PetPropertieEntity.TABLE;
			target = URI_PET_PROPERTIES;
			break;
			
		case PETPROPERTIES_ID:
			tableName = PetPropertieEntity.TABLE;
			target = URI_PET_PROPERTIES;
			single = true;
			break;
			
		case MOTIVES:
			tableName = MotiveEntity.TABLE;
			target = URI_MOTIVE;
			break;
			
		case MOTIVE_ID:
			tableName = MotiveEntity.TABLE;
			target = URI_MOTIVE;
			single = true;
			break;

		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}

		if (tableName != null && target != null) {

			return query(uri, tableName, single, projection, selection,
					selectionArgs, sortOrder);

		}
		return null;
	}

	private Cursor query(Uri uri, String tableName, boolean single,
			String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		Log.d(LOG_TAG, " - query :" + uri + " Table;" + tableName);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(tableName);

		if (single) {
			sqlBuilder.appendWhere("id" + "=" + uri.getPathSegments().get(1));
		}

		Cursor c = sqlBuilder.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(LOG_TAG, " - update :" + uri);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());

		int count = 0;

		switch (uriMatcher.match(uri)) {

		case USERS:
			count = db.update(UserEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case USER_ID:
			selection = UserEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(UserEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case PETS:
			count = db
					.update(PetEntity.TABLE, values, selection, selectionArgs);
			break;

		case PET_ID:
			selection = PetEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(PetEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case REQUESTS:
			count = db.update(RequestEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case REQUEST_ID:
			selection = RequestEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(RequestEntity.TABLE, values, selection,
					selectionArgs);
			break;
			
		case SPECIES:
			count = db.update(SpecieEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case SPECIE_ID:
			selection = SpecieEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(RequestEntity.TABLE, values, selection,
					selectionArgs);
			break;
			
		case BREEDS:
			count = db.update(BreedEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case BREED_ID:
			selection = BreedEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(BreedEntity.TABLE, values, selection,
					selectionArgs);
			break;
			
		case PETPROPERTIES:
			count = db.update(PetPropertieEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case PETPROPERTIES_ID:
			selection = PetPropertieEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(PetPropertieEntity.TABLE, values, selection,
					selectionArgs);
			break;
			
		case MOTIVES:
			count = db.update(MotiveEntity.TABLE, values, selection,
					selectionArgs);
			break;

		case MOTIVE_ID:
			selection = MotiveEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			count = db.update(MotiveEntity.TABLE, values, selection,
					selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsAffected = 0;
		Log.d(LOG_TAG, " - query :" + uri);

		if (dbHelper == null) {
			createDataBaseHelper();
		}

		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		dbHelper.onUpgrade(db, dataBaseVersion,
				DataBaseHelper.getVersionAvailable());
		
		String id = null;
		
		switch (uriMatcher.match(uri)) {

		case USERS:
			rowsAffected = db.delete(UserEntity.TABLE, selection,
					selectionArgs);
			break;

		case USER_ID:
			selection = UserEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(UserEntity.TABLE, selection,
					selectionArgs);
			break;

		case PETS:
			rowsAffected = db.delete(UserEntity.TABLE, selection,
					selectionArgs);
			break;

		case PET_ID:
			selection = PetEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(PetEntity.TABLE, selection,
					selectionArgs);
			break;

		case REQUESTS:
			rowsAffected = db.delete(UserEntity.TABLE, selection,
					selectionArgs);
			break;

		case REQUEST_ID:
			selection = RequestEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(RequestEntity.TABLE, selection,
					selectionArgs);
			break;
			
		case SPECIES:
			rowsAffected = db.delete(SpecieEntity.TABLE, selection,
					selectionArgs);
			break;

		case SPECIE_ID:
			selection = SpecieEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(SpecieEntity.TABLE, selection,
					selectionArgs);
			break;

			
		case BREEDS:
			rowsAffected = db.delete(BreedEntity.TABLE, selection,
					selectionArgs);
			break;

		case BREED_ID:
			selection = BreedEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(BreedEntity.TABLE, selection,
					selectionArgs);
			break;

			
		case PETPROPERTIES:
			rowsAffected = db.delete(PetPropertieEntity.TABLE, selection,
					selectionArgs);
			break;

		case PETPROPERTIES_ID:
			selection = PetPropertieEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(PetPropertieEntity.TABLE, selection,
					selectionArgs);
			break;

			
		case MOTIVES:
			rowsAffected = db.delete(MotiveEntity.TABLE, selection,
					selectionArgs);
			break;

		case MOTIVE_ID:
			selection = MotiveEntity.COLUMN_ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? "AND (" + selection
							+ ')' : "");
			rowsAffected = db.delete(MotiveEntity.TABLE, selection,
					selectionArgs);
			break;


		default:
			throw new IllegalArgumentException("Unsupported UR" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

}
