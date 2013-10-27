package com.arawaney.tumascotik.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CitationDB {

	
	public static final String KEY_NAME = "name";
	public static final String KEY_PET = "pet";
	public static final String KEY_REASON = "reason";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_INITIAL_DATE = "initialDate";
	public static final String KEY_FINAL_DATE = "finalDate";
	public static final String KEY_ACCEPTED = "accepted";
	public static final String KEY_RACE = "race";
	
	public static final String DATABASE_NAME = "DBCalendar";
	public static final String DATABASE_TABLE = "CitationTable";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_CREATE =
		        "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE +" (" + KEY_NAME + " TEXT, " + KEY_PET + " TEXT, " + KEY_REASON + " TEXT, " +
		        KEY_PHONE + " TEXT, " + KEY_INITIAL_DATE + " TEXT, " + KEY_FINAL_DATE + " TEXT, "+ KEY_RACE + " TEXT, " + KEY_ACCEPTED + " INT);";
	
	
	private Context context;
	public DatabaseHelper DBHelper;
	public SQLiteDatabase db;
	
	
	 public CitationDB(Context ctx) 
	    {
	        this.context = ctx;
	        DBHelper = new DatabaseHelper(context);
	        
	    }
	 
	 private static class DatabaseHelper extends SQLiteOpenHelper 
	    {
		 
	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);	  
	        }

			@Override
			public void onCreate(SQLiteDatabase db) {
				db.execSQL(DATABASE_CREATE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS CitationTable");
	            onCreate(db);
			}
	    }
	 
	 
	//---opens the database---
	    public SQLiteDatabase open() throws SQLException 
	    {
	        db = DBHelper.getWritableDatabase();
	        return db;
	    }
	    
	 //---closes the database---    
	    public void close() 
	    {
	    	db.close();
	        DBHelper.close();
	    }
	    
	 // --- insert a row in the Table ---
	    public long insert(String name, String pet, String reason, String phone, String initialDate, String finalDate, String race) 
	    {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_NAME, name);
	        initialValues.put(KEY_PET, pet);
	        initialValues.put(KEY_REASON, reason);
	        initialValues.put(KEY_PHONE, phone);
	        initialValues.put(KEY_INITIAL_DATE, initialDate);
	        initialValues.put(KEY_FINAL_DATE, finalDate);
	        initialValues.put(KEY_ACCEPTED, 0);
	        initialValues.put(KEY_RACE, race);
	        return db.insert(DATABASE_TABLE, null, initialValues);
	    }
	    
	  // --- delete the row in the Table ---
	    public int delete(String initialDate) 
	    {	Log.d("DELETEDATAB","DELETEE");
	        return db.delete(DATABASE_TABLE, KEY_INITIAL_DATE + "= ?", new String [] {initialDate});
	    	
	    }
	    
	    
	  //---deletes a particular record---
	    public int deleteAll() 
	    {
	        return db.delete(DATABASE_TABLE, null, null);
	    	
	    }
	    
	    
	  //---retrieves all the records---
	    public Cursor getAll() 
	    {
	        return db.query(DATABASE_TABLE, new String[] {KEY_NAME, KEY_PET, KEY_REASON, KEY_PHONE, KEY_INITIAL_DATE, KEY_FINAL_DATE, 
	        		KEY_ACCEPTED, KEY_RACE}, null, null, null, null, null);
	    }
	    
	    public Cursor getPending()
	    {
	    	return db.query(DATABASE_TABLE, new String[] {KEY_NAME, KEY_PET, KEY_REASON, KEY_PHONE, KEY_INITIAL_DATE, KEY_FINAL_DATE, KEY_RACE}, 
	        		KEY_ACCEPTED + "= ?", new String[] {String.valueOf(0)}, null, null, null); 
	    	
	    }
	    
	    public Cursor getAccepted()
	    {
	    	return db.query(DATABASE_TABLE, new String[] {KEY_NAME, KEY_PET, KEY_REASON, KEY_PHONE, KEY_INITIAL_DATE, KEY_FINAL_DATE, KEY_RACE}, 
	        		KEY_ACCEPTED + "= ?", new String[] {String.valueOf(1)}, null, null, null); 
	    	
	    }
	    public Cursor getObject(String InitDate)
	    {
	    	return db.query(DATABASE_TABLE, new String[] {KEY_NAME, KEY_PET, KEY_REASON, KEY_PHONE, KEY_INITIAL_DATE, KEY_FINAL_DATE, KEY_RACE}, 
	    			KEY_INITIAL_DATE + "= ?", new String[] {InitDate}, null, null, null); 
	    	
	    }
	    
	  //---updates a record---
	    public long update(String initialDate, Integer accepted) 
	    {   Log.d("UPDATEDATAB","UPDATE");
	        ContentValues args = new ContentValues();
	        args.put(KEY_ACCEPTED, accepted);
	        return db.update(DATABASE_TABLE, args, KEY_INITIAL_DATE + "= ?" , new String[] {initialDate});
	    }
	
	    public long updateINIDATE(Integer accepted) 
	    {
	    	ContentValues args = new ContentValues();
	    	args.put(KEY_ACCEPTED, accepted);
		     return db.update(DATABASE_TABLE, args, KEY_PET + "= ?" , new String[] {"Sofi"});
	    }
	    
}
