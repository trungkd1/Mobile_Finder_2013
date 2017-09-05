package com.nahi.vn.mobilefinder.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.entity.ContactObj;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.ApiType;
import com.nahi.vn.mobilefinder.entity.ContentSender.SenderType;
import com.nahi.vn.mobilefinder.entity.LocationObj;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLiteAdapter.
 */
public class SQLiteAdapter {

	/** The sq lite helper. */
	private DbHelper dbHelper;
	
	/** The sq lite database. */
	private SQLiteDatabase sqLiteDatabase;
	
	/** The context. */
	private Context context;

	/**
	 * Instantiates a new sQ lite adapter.
	 *
	 * @param context the context
	 */
	public SQLiteAdapter(Context context) {
		this.context = context;
		open();
	}

	/**
	 * Check Database is open or close.
	 * 
	 * @return true, if is ready
	 */
	public boolean isReady() {
		if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Open Database when start app.
	 *
	 * @return the sQ lite adapter
	 * @throws SQLException the sQL exception
	 */
	public SQLiteAdapter open() throws android.database.SQLException {
		dbHelper = new DbHelper(context);
		sqLiteDatabase = dbHelper.getWritableDatabase();
		return this;
	}
	

	/**
	 * Close Database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Insert location.
	 *
	 * @param obj the obj
	 * @return the long
	 */
	public long insertLocation(LocationObj obj){
		int count = countTotalLocation();
		if(count >= Config.MAX_LOCATION_RESULT){
			deleteAllLocation();
		}
		long i = sqLiteDatabase.insert(DbHelper.TABLE_LOCATION, null, getLocationValues(obj));
		return i;
	}
	
	/**
	 * Insert contact.
	 *
	 * @param obj the obj
	 * @return the long
	 */
	public long insertContact(ContactObj obj){
		if(getContactObjByNumber(obj.number) != null){
			return ContactObj.EXISTED;
		}
		else{
			return sqLiteDatabase.insert(DbHelper.TABLE_FILTER_CONTACT, null, getContactValues(obj));
		}
	}
	
	/**
	 * Update contact.
	 *
	 * @param obj the obj
	 * @return the long
	 */
	public long updateContact(ContactObj obj){
		return sqLiteDatabase.update(DbHelper.TABLE_FILTER_CONTACT, getContactValues(obj), DbHelper.WHERE_KEY_ID + obj.id, null);
	}
	
	/**
	 * Delete all contacts.
	 *
	 * @return the int
	 */
	public int deleteAllContacts(){
		return sqLiteDatabase.delete(DbHelper.TABLE_FILTER_CONTACT, null, null);
	}
	
	
	/**
	 * Delete contact.
	 *
	 * @param number the number
	 * @return the int
	 */
	public int deleteContact(String number){
		return sqLiteDatabase.delete(DbHelper.TABLE_FILTER_CONTACT, DbHelper.KEY_CONTACT_NUMBER+" = ? ", new String[]{number});
	}
	
	/**
	 * Gets the all locations.
	 *
	 * @return the all locations
	 */
	public ArrayList<LocationObj> getAllLocations(){
		ArrayList<LocationObj> list = new ArrayList<LocationObj>();
		Cursor cursor = sqLiteDatabase.query(DbHelper.TABLE_LOCATION, DbHelper.columnLocation,
				null, null, null, null, null, String.valueOf(Config.MAX_LOCATION_RESULT));
		if(cursor != null){
			while(cursor.moveToNext()){				
				list.add(getLocationObj(cursor));	
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * Sum total location.
	 *
	 * @return the int
	 */
	public int countTotalLocation(){
		String countQuery = "SELECT count(" + DbHelper.KEY_ID + ") FROM " + DbHelper.TABLE_LOCATION;
		Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
		int sum = 0;
		if (cursor != null) {
		    try {
		        if (cursor.moveToFirst()) {
		            sum = cursor.getInt(0);
		        }
		    } finally {
		        cursor.close();
		    }
		}
		return sum;
	}
	
	/**
	 * Delete latest location.
	 *
	 * @return the int
	 */
	public int deleteLatestLocation(){
		String whereLastestLocation = DbHelper.KEY_ID + " IN (" + "SELECT " + DbHelper.KEY_ID 
				+ " FROM " + DbHelper.TABLE_LOCATION + " ORDER BY " + DbHelper.KEY_ID + " ASC LIMIT 1)";
		return sqLiteDatabase.delete(DbHelper.TABLE_LOCATION, whereLastestLocation, null);
	}
	
	/**
	 * Delete all location.
	 *
	 * @return the int
	 */
	public int deleteAllLocation(){
		return sqLiteDatabase.delete(DbHelper.TABLE_LOCATION, null, null);
	}
	
	/**
	 * Count total contact.
	 *
	 * @return the int
	 */
	public int countTotalContact(){
		String countQuery = "SELECT count(" + DbHelper.KEY_ID + ") FROM " + DbHelper.TABLE_FILTER_CONTACT;
		Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
		int sum = 0;
		if (cursor != null) {
		    try {
		        if (cursor.moveToFirst()) {
		            sum = cursor.getInt(0);
		        }
		    } finally {
		        cursor.close();
		    }
		}
		return sum;
	}
	
	/**
	 * Gets the all contacts.
	 *
	 * @return the all contacts
	 */
	public ArrayList<ContactObj> getAllContacts(){
		ArrayList<ContactObj> list = new ArrayList<ContactObj>();
		Cursor cursor = sqLiteDatabase.query(DbHelper.TABLE_FILTER_CONTACT, DbHelper.columnContact,
				null, null, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()){				
				list.add(getContactObj(cursor));	
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * Insert content sender.
	 *
	 * @param sender the sender
	 * @return the long
	 */
	public long insertContentSender(ContentSender sender){
		return sqLiteDatabase.insert(DbHelper.TABLE_CONTENT_SENDER, null, getContentSenderValues(sender));
	}
	
	/**
	 * Update content sender.
	 *
	 * @param sender the sender
	 * @return the long
	 */
	public long updateContentSender(ContentSender sender){
		return sqLiteDatabase.update(DbHelper.TABLE_CONTENT_SENDER, getContentSenderValues(sender), DbHelper.WHERE_KEY_ID + sender.getId(), null);
	}
	
	/**
	 * Gets the all content sender.
	 *
	 * @return the all content sender
	 */
	public ArrayList<ContentSender> getAllContentSender(){
		ArrayList<ContentSender> list = new ArrayList<ContentSender>();
		Cursor cursor = sqLiteDatabase.query(DbHelper.TABLE_CONTENT_SENDER, DbHelper.columnContentSender,
				null, null, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()){				
				list.add(getContentSenderObj(cursor));	
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * Gets the pending content sender.
	 *
	 * @return the pending content sender
	 */
	public ArrayList<ContentSender> getPendingContentSender(){
		ArrayList<ContentSender> list = new ArrayList<ContentSender>();
		Cursor cursor = sqLiteDatabase.query(DbHelper.TABLE_CONTENT_SENDER, DbHelper.columnContentSender,
				DbHelper.WHERE_PENDING_SENDER, null, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()){				
				list.add(getContentSenderObj(cursor));	
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * Gets the pending content sender by type.
	 *
	 * @param types the types
	 * @return the pending content sender by type
	 */
	public ArrayList<ContentSender> getPendingContentSenderByType(SenderType...types){
		String whereType = "(" + SenderType.mergeValueToString(types) + ")";
		ArrayList<ContentSender> list = new ArrayList<ContentSender>();
		Cursor cursor = sqLiteDatabase.query(DbHelper.TABLE_CONTENT_SENDER, DbHelper.columnContentSender,
				DbHelper.WHERE_PENDING_SENDER + DbHelper.AND_CONDITION + DbHelper.WHERE_SENDER_TYPE_IN + whereType, null, null, null, null);
		if(cursor != null){
			while(cursor.moveToNext()){				
				list.add(getContentSenderObj(cursor));	
			}
		}
		cursor.close();
		return list;
	}
	
	/**
	 * Gets the location values.
	 *
	 * @param obj the obj
	 * @return the location values
	 */
	private ContentValues getLocationValues(LocationObj obj) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DbHelper.KEY_LATITUDE, obj.getLatitude());
		contentValues.put(DbHelper.KEY_LONGITUDE, obj.getLongitude());
		contentValues.put(DbHelper.KEY_TIME, obj.getTime());
		contentValues.put(DbHelper.KEY_LOCATION_ADDRESS, obj.getAddress());
		contentValues.put(DbHelper.KEY_STATUS, obj.getStatus());
		return contentValues;
	}
	
	/**
	 * Gets the contact obj by number.
	 *
	 * @param number the number
	 * @return the contact obj by number
	 */
	public ContactObj getContactObjByNumber(String number){
		if(TextUtils.isEmpty(number)){
			return null;
		}
		ContactObj obj = null;
		ArrayList<ContactObj> list = getAllContacts();
		for(ContactObj temp: list){
			if(AppUtil.isMatchPhone(context, number, temp.number)){
				obj = temp;
				break;
			}
		}
		return obj;
	}
	
	/**
	 * Gets the last contact obj.
	 *
	 * @return the last contact obj
	 */
	public ContactObj getLastContactObj(){
		ContactObj obj = null;
		Cursor cursor = sqLiteDatabase.query(DbHelper.TABLE_FILTER_CONTACT, DbHelper.columnContact,
				null, null, null, null, DbHelper.KEY_ID + " DESC", String.valueOf(1));
		if(cursor != null){
			while(cursor.moveToNext()){				
				obj = getContactObj(cursor);	
			}
		}
		cursor.close();
		return obj;
	}
	
	/**
	 * Gets the contact values.
	 *
	 * @param obj the obj
	 * @return the contact values
	 */
	private ContentValues getContactValues(ContactObj obj){
		ContentValues contentValues = new ContentValues();
		contentValues.put(DbHelper.KEY_CONTACT_NAME, obj.name);
		contentValues.put(DbHelper.KEY_CONTACT_NUMBER, obj.number);
		return contentValues;
	}

	/**
	 * Gets the contact obj.
	 *
	 * @param cursor the cursor
	 * @return the contact obj
	 */
	private ContactObj getContactObj(Cursor cursor){
		ContactObj obj = new ContactObj(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
		return obj;
	}
	
	/**
	 * Gets the location obj.
	 *
	 * @param cursor the cursor
	 * @return the location obj
	 */
	private LocationObj getLocationObj(Cursor cursor){
		LocationObj obj = new LocationObj(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_ID)), 
				cursor.getDouble(cursor.getColumnIndex(DbHelper.KEY_LATITUDE)), 
				cursor.getDouble(cursor.getColumnIndex(DbHelper.KEY_LONGITUDE)),
				cursor.getLong(cursor.getColumnIndex(DbHelper.KEY_TIME)),
				cursor.getString(cursor.getColumnIndex(DbHelper.KEY_LOCATION_ADDRESS)),
				cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_STATUS)));
		return obj;
	}
	
	/**
	 * Gets the content sender values.
	 *
	 * @param sender the sender
	 * @return the content sender values
	 */
	private ContentValues getContentSenderValues(ContentSender sender){
		ContentValues contentValues = new ContentValues();
		contentValues.put(DbHelper.KEY_SENDER_CONTENT,  sender.getContent());
		contentValues.put(DbHelper.KEY_SENDER_FILES,  sender.getFiles());
		contentValues.put(DbHelper.KEY_SENDER_TYPE,  sender.getType().value());
		contentValues.put(DbHelper.KEY_SENDER_IS_SENT,  sender.isSent() ? 1 : 0);
		contentValues.put(DbHelper.KEY_SENDER_API,  sender.getApi().value());
		contentValues.put(DbHelper.KEY_SENDER_CREATE_TIME,  sender.getCreateTime());
		return contentValues;
	}
	
	/**
	 * Gets the content sender obj.
	 *
	 * @param cursor the cursor
	 * @return the content sender obj
	 */
	private ContentSender getContentSenderObj(Cursor cursor){
		ContentSender obj = new ContentSender(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_ID)), 
				cursor.getString(cursor.getColumnIndex(DbHelper.KEY_SENDER_CONTENT)), 
				cursor.getString(cursor.getColumnIndex(DbHelper.KEY_SENDER_FILES)), 
				SenderType.fromValue(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_SENDER_TYPE))), 
				cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_SENDER_IS_SENT)) == 1 ? true : false, 
				ApiType.fromValue(cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_SENDER_API))),
				cursor.getLong(cursor.getColumnIndex(DbHelper.KEY_SENDER_CREATE_TIME)));
		return obj;
	}
}
