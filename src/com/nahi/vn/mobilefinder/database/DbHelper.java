/*
 *  aris-vn.com
 * 
 *  AlarmDBHelper.java
 * 
 *  Created by Thai Le.
 * 
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nahi.vn.mobilefinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class AlarmDBHelper. Alarm Provider
 */
public class DbHelper extends SQLiteOpenHelper {

    /** The Constant DATABASE_NAME. */
    public static final String DATABASE_NAME = "mobile_finder.db";

    /** The Constant DATABASE_VERSION. */
    public static final int DATABASE_VERSION = 1;

	/** The Constant TABLE_LOCATION. */
	public static final String TABLE_LOCATION = "LOCATION";

	/** The Constant TABLE_FILTER_CONTACT. */
	public static final String TABLE_FILTER_CONTACT = "FILTER_CONTACT";
	
	/** The Constant TABLE_CONTENT_SENDER. */
	public static final String TABLE_CONTENT_SENDER = "CONTENT_SENDER";
	
	/** The Constant KEY_ID. */
	public static final String KEY_ID = "_id";

	/** The Constant KEY_LATITUDE. */
	public static final String KEY_LATITUDE = "latitude";
	
	/** The Constant KEY_LONGITUDE. */
	public static final String KEY_LONGITUDE = "longitude";
	
	/** The Constant KEY_LOCATION_ADDRESS. */
	public static final String KEY_LOCATION_ADDRESS = "address";
	
	/** The Constant KEY_TIME. */
	public static final String KEY_TIME = "time";
	
	/** The Constant KEY_STATUS. */
	public static final String KEY_STATUS = "status";
	
	/** The Constant KEY_CONTACT_ID. */
	public static final String KEY_CONTACT_ID = "contact_id";
	
	/** The Constant KEY_CONTACT_NAME. */
	public static final String KEY_CONTACT_NAME = "contact_name";
	
	/** The Constant KEY_CONTACT_NUMBER. */
	public static final String KEY_CONTACT_NUMBER = "contact_number";
	
	/** The Constant KEY_SENDER_CONTENT. */
	public static final String KEY_SENDER_CONTENT = "content";
	
	/** The Constant KEY_SENDER_FILES. */
	public static final String KEY_SENDER_FILES = "files";
	
	/** The Constant KEY_SENDER_TYPE. */
	public static final String KEY_SENDER_TYPE = "type";
	
	/** The Constant KEY_SENDER_IS_SENT. */
	public static final String KEY_SENDER_IS_SENT = "is_sent";
	
	/** The Constant KEY_SENDER_API. */
	public static final String KEY_SENDER_API = "api";
	
	/** The Constant KEY_SENDER_CREATE_TIME. */
	public static final String KEY_SENDER_CREATE_TIME = "create_time";
	
	/** The Constant WHERE_PENDING_SENDER. */
	public static final String WHERE_PENDING_SENDER = KEY_SENDER_IS_SENT + "=" + 0;
	
	/** The Constant WHERE_SENDER_TYPE_IN. */
	public static final String WHERE_SENDER_TYPE_IN = KEY_SENDER_TYPE + " in ";
	
	/** The Constant WHERE_KEY_ID. */
	public static final String WHERE_KEY_ID = KEY_ID + "=";
	
	/** The Constant AND_CONDITION. */
	public static final String AND_CONDITION = " and ";
	
	/** The Constant OR_CONDITION. */
	public static final String OR_CONDITION = " or ";
	
	/** The Constant columnLocation. */
	public static final String[] columnLocation = {KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_LOCATION_ADDRESS, KEY_TIME, KEY_STATUS};
	
	/** The Constant columnContact. */
	public static final String[] columnContact = {KEY_ID, KEY_CONTACT_NAME, KEY_CONTACT_NUMBER};

	/** The Constant columnContentSender. */
	public static final String[] columnContentSender = {KEY_ID, KEY_SENDER_CONTENT, KEY_SENDER_FILES, KEY_SENDER_TYPE, KEY_SENDER_IS_SENT, KEY_SENDER_API, KEY_SENDER_CREATE_TIME};
	
	
	/** The Constant SCRIPT_CREATE_TABLE_LOCATION. */
	private static final String SCRIPT_CREATE_TABLE_LOCATION =
			"create table if not exists " + TABLE_LOCATION + " ("
					+ KEY_ID + " integer primary key autoincrement, "
					+ KEY_LATITUDE + " double, "
					+ KEY_LONGITUDE + " double, "
					+ KEY_LOCATION_ADDRESS + " text, "
					+ KEY_TIME + " integer," 
					+ KEY_STATUS + " integer);";
	
	/** The Constant SCRIPT_CREATE_TABLE_FILTER_CONTACT. */
	private static final String SCRIPT_CREATE_TABLE_FILTER_CONTACT =
			"create table if not exists " + TABLE_FILTER_CONTACT + " ("
					+ KEY_ID + " integer primary key autoincrement, "
					+ KEY_CONTACT_NAME + " text, "
					+ KEY_CONTACT_NUMBER + " text);";

	/** The Constant SCRIPT_CREATE_TABLE_CONTENT_SENDER. */
	private static final String SCRIPT_CREATE_TABLE_CONTENT_SENDER =
			"create table if not exists " + TABLE_CONTENT_SENDER + " ("
					+ KEY_ID + " integer primary key autoincrement, "
					+ KEY_SENDER_CONTENT + " text, "
					+ KEY_SENDER_FILES + " text, "
					+ KEY_SENDER_TYPE + " integer, "
					+ KEY_SENDER_IS_SENT + " integer default 0, "
					+ KEY_SENDER_API + " integer default 0, "
					+ KEY_SENDER_CREATE_TIME + " integer);";
	
    /**
     * Instantiates a new alarm db helper.
     * 
     * @param context the context
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /*
     * (non-Javadoc)
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(SCRIPT_CREATE_TABLE_LOCATION);
    	db.execSQL(SCRIPT_CREATE_TABLE_FILTER_CONTACT);
    	db.execSQL(SCRIPT_CREATE_TABLE_CONTENT_SENDER);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {

    }
}
    
    
