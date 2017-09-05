package com.nahi.vn.mobilefinder.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.nahi.vn.mobilefinder.broadcast.LockTriggerReceiver;
import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin.TriggerAction;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.ApiType;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;
import com.nahi.vn.mobilefinder.worker.ContentSenderWorker;
import com.nahi.vn.mobilefinder.worker.TimeoutCounter;
import com.nahi.vn.mobilefinder.worker.TimeoutCounter.TimeoutListener;

// TODO: Auto-generated Javadoc
/**
 * The Class BackupData.
 */
public class BackupData{
	
	/** The context. */
	private Context context;
	
	/** The backup listener. */
	private BackupListener backupListener;
	
	/** The backup worker. */
	private BackupWorker backupWorker;
	
	/** The is backup task running. */
	private boolean isBackupTaskRunning = false;
	
	/** The timeout counter. */
	private TimeoutCounter timeoutCounter;
	
	/** The is done backup contact. */
	private boolean isDoneBackupContact = false;
	
	/** The is done backup sms. */
	private boolean isDoneBackupSms = false;
	
	/** The is done backup call log. */
	private boolean isDoneBackupCallLog = false;
	
	/** The Constant CHECK_BACKUP_TIMEOUT. */
	private final static int CHECK_BACKUP_TIMEOUT = 10 * 60 * 1000; //Backup timeout is 10 minutes
	
	/** The Constant CHECK_BACKUP_INTERVAL. */
	private final static int CHECK_BACKUP_INTERVAL = 60 * 1000;
	
	/**
	 * The listener interface for receiving backup events.
	 * The class that is interested in processing a backup
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addBackupListener<code> method. When
	 * the backup event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see BackupEvent
	 */
	public interface BackupListener{
		
		/**
		 * On start backup.
		 */
		public void onStartBackup();
		
		/**
		 * On finish backup.
		 */
		public void onFinishBackup();
		
		/**
		 * On backup fail.
		 */
		public void onBackupFail();
		
		/**
		 * On cancel backup.
		 */
		public void onCancelBackup();
	}
	
	
	/**
	 * Instantiates a new backup data.
	 *
	 * @param context the context
	 */
	public BackupData(Context context){
		this.context = context;
		this.timeoutCounter = new TimeoutCounter(CHECK_BACKUP_TIMEOUT, CHECK_BACKUP_INTERVAL, new TimeoutListener() {
			
			@Override
			public void onFinish() {
				if(!isDoneBackupContact || !isDoneBackupSms || !isDoneBackupCallLog){
					isBackupTaskRunning = false;
					backupWorker = null;
					if(backupListener != null){
						backupListener.onBackupFail();
					}
				}
				timeoutCounter.cancel();
			}
		});
	}
	
	/**
	 * Gets the backup listener.
	 *
	 * @return the backup listener
	 */
	public BackupListener getBackupListener() {
		return backupListener;
	}

	/**
	 * Sets the backup listener.
	 *
	 * @param backupListener the new backup listener
	 */
	public void setBackupListener(BackupListener backupListener) {
		this.backupListener = backupListener;
	}

	/**
	 * On backup contact.
	 *
	 * @param isAutoBackup the is auto backup
	 */
	private void onBackupContact(boolean isAutoBackup) {
		Cursor cursor = cursorContact(context);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonData = new JSONObject();
		try {
			if (cursor.moveToFirst()) {
				do {
					JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate(Config.CONTACT_NAME, cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)));
						jsonObject.accumulate(Config.CONTACT_NUMBER, cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
						jsonObject.accumulate(Config.CONTACT_TYPE, cursor.getString(cursor.getColumnIndex(Phone.TYPE)));
						jsonArray.put(jsonObject);
				} while (cursor.moveToNext());
			}
			jsonData.accumulate(Config.DATA, jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cursor.close();
		
		String pathFile = AppUtil.createFile(context.getPackageName(), Config.BACKUP_CONTACT_FILE, jsonData.toString());

		sendBackupRequest(pathFile, ApiType.BACKUP_CONTACT_SERVER, isAutoBackup);
	}

	/**
	 * On backup call log.
	 *
	 * @param isAutoBackup the is auto backup
	 */
	private void onBackupCallLog(boolean isAutoBackup) {
		Cursor cursor = cursorCallLogs(context);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonData = new JSONObject();
		try {
			if (cursor.moveToFirst()) {
				do {
					JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate(Config.CALL_LOG_NAME, cursor.getString(cursor.getColumnIndex(Config.CALL_LOG_NAME)));
						jsonObject.accumulate(Config.CALL_LOG_NUMBER, cursor.getString(cursor.getColumnIndex(Config.CALL_LOG_NUMBER)));
						jsonObject.accumulate(Config.CALL_LOG_DURATION, cursor.getString(cursor.getColumnIndex(Config.CALL_LOG_DURATION)));
						jsonObject.accumulate(Config.TYPE, cursor.getInt(cursor.getColumnIndex(Config.TYPE)));
					    jsonObject.accumulate(Config.DATE, cursor.getLong(cursor.getColumnIndex(Config.DATE)));
					    jsonArray.put(jsonObject);
				} while (cursor.moveToNext());
			}
			jsonData.accumulate(Config.DATA, jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cursor.close();
		
		String pathFile = AppUtil.createFile(context.getPackageName(), Config.BACKUP_CALL_LOG_FILE,jsonData.toString());
		
		sendBackupRequest(pathFile, ApiType.BACKUP_CALL_LOG_SERVER, isAutoBackup);
	}

	/**
	 * On backup message.
	 *
	 * @param isAutoBackup the is auto backup
	 */
	private void onBackupMessage(boolean isAutoBackup) {
		Cursor cursor = cursorMessage(context);
		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			if (cursor.moveToFirst()) {
				do {
					JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate(Config.SMS_PERSON, cursor.getString(cursor.getColumnIndex(Config.SMS_PERSON)));
						jsonObject.accumulate(Config.SMS_ADDRESS, cursor.getString(cursor.getColumnIndex(Config.SMS_ADDRESS)));
					    jsonObject.accumulate(Config.SMS_BODY, cursor.getString(cursor.getColumnIndex(Config.SMS_BODY)));
					    jsonObject.accumulate(Config.TYPE, cursor.getInt(cursor.getColumnIndex(Config.TYPE)));
					    jsonObject.accumulate(Config.DATE, cursor.getLong(cursor.getColumnIndex(Config.DATE)));
					    jsonObject.accumulate(Config.SMS_READ, cursor.getString(cursor.getColumnIndex(Config.SMS_READ)));
					    jsonArray.put(jsonObject);
				} while (cursor.moveToNext());
			}
			jsonData.accumulate(Config.DATA, jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cursor.close();
		
		String pathFile = AppUtil.createFile(context.getPackageName(), Config.BACKUP_SMS_FILE, jsonData.toString());

		sendBackupRequest(pathFile, ApiType.BACKUP_SMS_SERVER, isAutoBackup);
	}
	
	/**
	 * Send backup request.
	 *
	 * @param pathFile the path file
	 * @param api the api
	 * @param isAutoBackup the is auto backup
	 */
	private void sendBackupRequest(String pathFile, ApiType api, boolean isAutoBackup){
		List<String> listFiles = new ArrayList<String>();
		listFiles.add(pathFile);
		// Save the backup contact content senders to db
		ContentSender sender = null;
		if(isAutoBackup){
			sender = ContentSenderWorker.createAutoBackupRequestSender("", listFiles, api);
		}
		else{
			sender = ContentSenderWorker.createNahiRequestSender("", listFiles, api);
		}
		if(sender != null){
			ContentSenderWorker.insertContentSenders(sender);
		}
	}
	
	
	/**
	 * Backup data.
	 *
	 * @param isAutoBackup the is auto backup
	 */
	private void backupData(boolean isAutoBackup){
		onBackupContact(isAutoBackup);
		onBackupCallLog(isAutoBackup);
		onBackupMessage(isAutoBackup);
		if(isAutoBackup){
			// Send the "auto backup receive content sender" broadcast
			AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_AUTO_BACKUP_SENDER);	
		}
		else{
			// Send the "receive content sender" broadcast
			AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_CONTENT_SENDER);			
		}
	}
	
	/**
	 * Backup data task.
	 *
	 * @param isAutoBackup the is auto backup
	 */
	public void backupDataTask(boolean isAutoBackup){
		if(backupWorker == null){
			backupWorker = new BackupWorker();
		}
		if(!isBackupTaskRunning && backupWorker != null){
			backupWorker.execute();
		}
	}
	
	/**
	 * Cancel backup data task.
	 */
	public void cancelBackupDataTask(){
		if(!backupWorker.isCancelled()){
			backupWorker.cancel(true);
		}
	}
	
	/**
	 * Cursor message.
	 *
	 * @param context the context
	 * @return the cursor
	 */
	private static Cursor cursorMessage(Context context){
		Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"),
				Config.SMS, Config.SMS_ADDRESS + " IS NOT NULL " + " AND " + Config.SMS_ADDRESS + " <> '' ", null, Config.DATE + " DESC");
		return cursor;
	}
	
	/**
	 * Cursor call logs.
	 *
	 * @param context the context
	 * @return the cursor
	 */
	private static Cursor cursorCallLogs(Context context) {
		Cursor cursor = context.getContentResolver().query(Calls.CONTENT_URI,
				Config.CALL_LOGS, null, null, Config.DATE + " DESC");
		return cursor; 
	}
	
	/**
	 * Cursor contact.
	 *
	 * @param context the context
	 * @return the cursor
	 */
	private static Cursor cursorContact(Context context) {
		Cursor cursor = context. getContentResolver().query(Phone.CONTENT_URI,
				Config.PHONE_CONTACT, 
				null, null,	Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		return cursor;
	}

	/**
	 * The Class BackupWorker.
	 */
	private class BackupWorker extends AsyncTask<Void, Void, Void> implements HttpClientListener{

		/* (non-Javadoc)
		 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onHttpResult(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, java.util.List)
		 */
		@Override
		public void onHttpResult(Methods methods, List<Object> data) {
			switch (methods) {
				case BACKUP_CONTACT_SERVER:
					isDoneBackupContact = true;
					break;
				case BACKUP_SMS_SERVER:
					isDoneBackupSms = true;
					break;
				case BACKUP_CALL_LOG_SERVER:
					isDoneBackupCallLog = true;
					break;
				default:
					break;
			}
			if(isDoneBackupContact && isDoneBackupSms && isDoneBackupCallLog){
				isBackupTaskRunning = false;
				backupWorker = null;
				if(SaveData.getInstance(context).isAutoProtect()){
					//Send the "lock trigger" broadcast
					if(SaveData.getInstance(context).isTriggerRunning()){
						Bundle bundle = new Bundle();
						bundle.putInt(LockTriggerReceiver.KEY_ACTION_DONE, TriggerAction.BACKUP.value());
						AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_LOCK_TRIGGER, bundle);
					}
				}
				if(backupListener != null){
					backupListener.onFinishBackup();
				}
			}
		}

		/* (non-Javadoc)
		 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onStartAPI(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods)
		 */
		@Override
		public void onStartAPI(Methods methods) {
		}

		/* (non-Javadoc)
		 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onCallAPIFail(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, int)
		 */
		@Override
		public void onCallAPIFail(Methods methods, int statusCode) {
			isBackupTaskRunning = false;
			backupWorker = null;
			if(backupListener != null){
				backupListener.onBackupFail();
			}
		}

		/* (non-Javadoc)
		 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onProgressUpdate(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, int)
		 */
		@Override
		public void onProgressUpdate(Methods methods, int progress) {
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(backupListener != null){
				backupListener.onStartBackup();
			}
			HttpClientHelper.getInstance().setListener(this);
			isBackupTaskRunning = true;
			timeoutCounter.start();
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... params) {
			backupData(false);
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if(isCancelled()){
				isBackupTaskRunning = false;
				backupWorker = null;
				if(backupListener != null){
					backupListener.onCancelBackup();
				}
			}
		}
		
	}
}
