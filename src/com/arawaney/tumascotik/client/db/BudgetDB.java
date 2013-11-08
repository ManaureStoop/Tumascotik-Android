package com.arawaney.tumascotik.client.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class BudgetDB {

	
	public static final String KEY_TITLE = "title";
	public static final String KEY_PRICE = "price";

	
	public static final String DATABASE_NAME = "DBBudget";
	public static final String DATABASE_TABLE = "BudgetTable";
	public static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_CREATE =
		        "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE +" (" + KEY_TITLE + " TEXT, " + KEY_PRICE +" INT);";
	
	
	private Context context;
	public DatabaseHelper DBHelper;
	public SQLiteDatabase db;
	
	
	 public BudgetDB(Context ctx) 
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
				db.execSQL("DROP TABLE IF EXISTS BudgetTable");
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
	    public long insert(String title, int price) 
	    {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_TITLE, title);
	        initialValues.put(KEY_PRICE, price);

	        return db.insert(DATABASE_TABLE, null, initialValues);
	    }
	    
	  // --- delete the row in the Table ---
	    public int delete(String title) 
	    {	Log.d("DELETEDATAB","DELETEE");
	        return db.delete(DATABASE_TABLE, KEY_TITLE + "= ?", new String [] {title});
	    	
	    }
	    
	    
	  //---deletes a particular record---
	    public int deleteAll() 
	    {
	        return db.delete(DATABASE_TABLE, null, null);
	    	
	    }
	    
	    
	  //---retrieves all the records---
	    public Cursor getAll() 
	    {
	        return db.query(DATABASE_TABLE, new String[] {KEY_TITLE, KEY_PRICE}, null, null, null, null, null);
	    }
	    
	   
	    public Cursor getObject(String title)
	    {
	    	return db.query(DATABASE_TABLE, new String[] {KEY_TITLE, KEY_PRICE}, 
	    			KEY_TITLE + "= ?", new String[] {title}, null, null, null); 
	    	
	    }
	    
}
