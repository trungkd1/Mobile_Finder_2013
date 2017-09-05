package com.nahi.vn.mobilefinder.activity;

import android.content.Context;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.database.SQLiteAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class App.
 */
public class App extends android.app.Application {
	
	public static String APP_NAME;
	/** The context. */
	private static Context context;

	/** The db. */
	private static SQLiteAdapter db;

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		db = new SQLiteAdapter(context);
		APP_NAME = getString(R.string.app_name);
	}
	

	/**
	 * Gets the db.
	 *
	 * @return the db
	 */
	public static SQLiteAdapter getDB(){
		if(!db.isReady()){
			db = new SQLiteAdapter(context);			
		}
		return db;
	}

	/**
	 * Close.
	 */
	public static void close(){
		db.close();
	}	
}
