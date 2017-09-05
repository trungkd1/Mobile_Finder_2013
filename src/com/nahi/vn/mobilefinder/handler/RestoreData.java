package com.nahi.vn.mobilefinder.handler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.entity.CallLogEntry;
import com.nahi.vn.mobilefinder.entity.ContactEntry;
import com.nahi.vn.mobilefinder.entity.SMSEntry;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.Log;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;
import com.nahi.vn.mobilefinder.worker.TimeoutCounter;
import com.nahi.vn.mobilefinder.worker.TimeoutCounter.TimeoutListener;

// TODO: Auto-generated Javadoc
/**
 * The Class RestoreData.
 */
public class RestoreData implements HttpClientListener {

	/** The context. */
	private Context context;
	
	/** The restore listener. */
	private RestoreListener restoreListener;
	
	/** The is done restore contact. */
	private boolean isDoneRestoreContact = false;
	
	/** The is done restore sms. */
	private boolean isDoneRestoreSms = false;
	
	/** The is done restore call log. */
	private boolean isDoneRestoreCallLog = false;
	
	/** The restore worker. */
	private RestoreWorker restoreWorker;
	
	/** The is restore task running. */
	private boolean isRestoreTaskRunning = false;
	
	/** The contact restore timeout. */
	private TimeoutCounter contactRestoreTimeout;
	
	/** The sms restore timeout. */
	private TimeoutCounter smsRestoreTimeout;
	
	/** The call log restore timeout. */
	private TimeoutCounter callLogRestoreTimeout;
	
	/** The Constant CHECK_RESTORE_TIMEOUT. */
	private final static int CHECK_RESTORE_TIMEOUT = 10 * 60 * 1000;//Each restore timeout is 10 minutes
	
	/** The Constant CHECK_RESTORE_INTERVAL. */
	private final static int CHECK_RESTORE_INTERVAL = 60 * 1000;
	
	/**
	 * The listener interface for receiving restore events.
	 * The class that is interested in processing a restore
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addRestoreListener<code> method. When
	 * the restore event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see RestoreEvent
	 */
	public interface RestoreListener{
		
		/**
		 * On start backup.
		 */
		public void onStartRestore();
		
		/**
		 * On finish backup.
		 */
		public void onFinishRestore();
		
		/**
		 * On backup fail.
		 */
		public void onRestoreFail();
		
		/**
		 * On cancel restore.
		 */
		public void onCancelRestore();
	}
	
	/**
	 * Instantiates a new restore data.
	 *
	 * @param context the context
	 */
	public RestoreData(Context context) {
		this.context = context;
		this.contactRestoreTimeout = new TimeoutCounter(CHECK_RESTORE_TIMEOUT, CHECK_RESTORE_INTERVAL, new TimeoutListener() {
			
			@Override
			public void onFinish() {
				if(!isDoneRestoreContact){
					resetRestoreTask();
					if(restoreListener != null){
						restoreListener.onRestoreFail();
					}
				}
				contactRestoreTimeout.cancel();
			}
		});
		this.smsRestoreTimeout = new TimeoutCounter(CHECK_RESTORE_TIMEOUT, CHECK_RESTORE_INTERVAL, new TimeoutListener() {
			
			@Override
			public void onFinish() {
				if(!isDoneRestoreSms){
					resetRestoreTask();
					if(restoreListener != null){
						restoreListener.onRestoreFail();
					}
				}
				smsRestoreTimeout.cancel();
			}
		});
		this.callLogRestoreTimeout = new TimeoutCounter(CHECK_RESTORE_TIMEOUT, CHECK_RESTORE_INTERVAL, new TimeoutListener() {
			
			@Override
			public void onFinish() {
				if(!isDoneRestoreCallLog){
					resetRestoreTask();
					if(restoreListener != null){
						restoreListener.onRestoreFail();
					}
				}
				callLogRestoreTimeout.cancel();
			}
		});
	}

	/**
	 * Gets the restore listener.
	 *
	 * @return the restore listener
	 */
	public RestoreListener getRestoreListener() {
		return restoreListener;
	}

	/**
	 * Sets the restore listener.
	 *
	 * @param restoreListener the new restore listener
	 */
	public void setRestoreListener(RestoreListener restoreListener) {
		this.restoreListener = restoreListener;
	}

	/**
	 * Restore data.
	 */
	public void restoreData(){
		if(restoreWorker == null){
			restoreWorker = new RestoreWorker();
		}
		String token = ProfileData.getInstance(context).getToken();
		if(!TextUtils.isEmpty(token)){
			HttpClientHelper.getInstance().restoreContactMobile(token);
			HttpClientHelper.getInstance().setListener(this);
			if(restoreListener != null){
				restoreListener.onStartRestore();
			}
			contactRestoreTimeout.start();
		}
	}
	
	/**
	 * On restore contact.
	 *
	 * @param list the list
	 */
	private void onRestoreContact(List<Object> list) {
		for(int i = 0 ; i < list.size() ; i ++){
			ContactEntry contact = (ContactEntry) list.get(i);
		    ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

		    ops.add(ContentProviderOperation.newInsert(
		    ContactsContract.RawContacts.CONTENT_URI)
		        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
		        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
		        .build());

		    //------------------------------------------------------ Names
		    
		    if(!TextUtils.isEmpty(contact.getName())){
		        ops.add(ContentProviderOperation.newInsert(
		        ContactsContract.Data.CONTENT_URI)
		            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		            .withValue(ContactsContract.Data.MIMETYPE,
		        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
		            .withValue(
		        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName()).build());
		    }

		    //------------------------------------------------------ Mobile Number    
		    
		    if(!TextUtils.isEmpty(contact.getNumber())){			    	
		        ops.add(ContentProviderOperation.
		        newInsert(ContactsContract.Data.CONTENT_URI)
		            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		            .withValue(ContactsContract.Data.MIMETYPE,
		        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
		            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		            .build());
		    }
		    
		    //------------------------------------------------------ Mobile Type       
		    
		    if(!TextUtils.isEmpty(contact.getType())){		
		        ops.add(ContentProviderOperation.
		        newInsert(ContactsContract.Data.CONTENT_URI)
		            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		            .withValue(ContactsContract.Data.MIMETYPE,
		        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, contact.getType())
		            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
		        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		            .build());
		    }
		
		    // Asking the Contact provider to create a new contact           
		    Cursor cursor = null;
		    try {
		    	cursor = cursorContact(context, contact.getNumber(),contact.getName());
		    	if(cursor.getCount() == 0){
		    		 context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		    	}else{
		    		Log.e("Exist Phone number");
		    	}
		       
		    } catch(Exception ex){
		    	Log.e("Restore error :" + ex.toString());
		    } finally{
		    	if(cursor != null){
		    		cursor.close();
		    	}
		    }
		
		}
	}
	
	/**
	 * Cursor contact.
	 *
	 * @param context the context
	 * @param number the number
	 * @param name the name
	 * @return the cursor
	 */
	private Cursor cursorContact(Context context,String number,String name) {
		Cursor cursor = null;
		if(TextUtils.isEmpty(number) && !TextUtils.isEmpty(name)){
			cursor = context. getContentResolver().query(Phone.CONTENT_URI,
					Config.PHONE_CONTACT, Phone.DISPLAY_NAME+" = ?", new String[]{name},null);
		}else if(!TextUtils.isEmpty(number) && TextUtils.isEmpty(name)){
			cursor = context. getContentResolver().query(Phone.CONTENT_URI,
					Config.PHONE_CONTACT, 
					Phone.NUMBER+" = ?", new String[]{number},	null);
		}else  if(!TextUtils.isEmpty(number) && !TextUtils.isEmpty(name)){
			cursor = context. getContentResolver().query(Phone.CONTENT_URI,
					Config.PHONE_CONTACT, 
					Phone.NUMBER+" = ? " +" AND "+Phone.DISPLAY_NAME+" = ?", new String[]{number,name},	null);
		}
		
		return cursor;
	}

	/**
	 * On restore call log.
	 *
	 * @param list the list
	 */
	private void onRestoreCallLog(List<Object> list) {
		for(int i = 0 ; i < list.size(); i ++){
			CallLogEntry callLog = (CallLogEntry) list.get(i);
			
			if(!TextUtils.isEmpty(callLog.getDate()) && !TextUtils.isEmpty(callLog.getNumber())){
				Cursor cursor = null;
				try{
					cursor = context.getContentResolver().query(Calls.CONTENT_URI,
							null, Config.DATE+" = ? and "+Config.CALL_LOG_NUMBER+" = ?", new String[]{callLog.getDate(),callLog.getNumber()}, null);
					if (cursor==null||(cursor!=null&&cursor.getCount()==0)) {
						//CallLog not exist. Insert into db
						ContentValues values = new ContentValues();
					    values.put(Config.CALL_LOG_NUMBER, callLog.getNumber());
					    values.put(Config.DATE, callLog.getDate());
					    values.put(Config.CALL_LOG_DURATION, callLog.getDuration());
					    values.put(Config.TYPE, callLog.getType());
				    	values.put(Config.CALL_LOG_NAME, callLog.getName());
					    context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
					} else {
						Log.e("Calllog from contact: "+callLog.getNumber()+" at the time: "+callLog.getDate() +" exist");
					}
				} catch(Exception e){
					Log.e(e.toString());
				} finally{
					if(cursor != null){
						cursor.close();
					}
				}
			}
		}
	}

	/**
	 * On restore message.
	 *
	 * @param list the list
	 */
	private void onRestoreMessage(List<Object> list) {
		for(int i = 0 ; i < list.size() ; i ++){
			SMSEntry sms = (SMSEntry) list.get(i);
			
			if(!TextUtils.isEmpty(sms.getmDate()) && !TextUtils.isEmpty(sms.getmNumber())){
				Cursor cursor = null;
				try{
					cursor = context.getContentResolver().query(Uri.parse("content://sms"),
							null, Config.DATE+" = ? and "+Config.SMS_ADDRESS+" = ?", new String[]{sms.getmDate(),sms.getmNumber()}, null);
					if (cursor==null||(cursor!=null&&cursor.getCount()==0)) {
						ContentValues values = new ContentValues();
						values.put(Config.SMS_PERSON, sms.mName);
						values.put(Config.SMS_ADDRESS, sms.mNumber);
						values.put(Config.SMS_BODY, sms.mBody);
						values.put(Config.TYPE, sms.mType);
						values.put(Config.DATE, sms.mDate);
						values.put(Config.SMS_READ, sms.mRead);
						context.getContentResolver().insert(Uri.parse("content://sms"),
								values);
					} else {
						Log.e("Message from contact: "+sms.getmNumber()+" at the time: "+sms.getmDate() +" exist");
					}
				} catch(Exception e){
					Log.e(e.toString());
				} finally{
					if(cursor != null){
						cursor.close();
					}
				}
			}
		}
	}

	/**
	 * Cancel restore data task.
	 */
	public void cancelRestoreDataTask(){
		if(!restoreWorker.isCancelled()){
			restoreWorker.cancel(true);
		}
	}
	
	/**
	 * Reset restore task.
	 */
	private void resetRestoreTask(){
		isRestoreTaskRunning = false;
		isDoneRestoreContact = false;
		isDoneRestoreSms = false;
		isDoneRestoreCallLog = false;
		restoreWorker = null;
	}
	
	/* (non-Javadoc)
	 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onHttpResult(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, java.util.List)
	 */
	@Override
	public void onHttpResult(Methods methods, List<Object> data) {
		switch (methods) {
			case RESTORE_CONTACT_MOBILE:
				if(data != null){
					if(!isRestoreTaskRunning && restoreWorker != null){
						restoreWorker.setMethodId(Methods.RESTORE_CONTACT_MOBILE);
						restoreWorker.execute(data);
					}
				} else {
					isDoneRestoreContact = true;
				}
				break;
			case RESTORE_CALL_LOG_MOBILE:
				if(data != null){
					if(isRestoreTaskRunning && restoreWorker != null){
						restoreWorker = null;
						restoreWorker = new RestoreWorker();
						restoreWorker.setMethodId(Methods.RESTORE_CALL_LOG_MOBILE);
						restoreWorker.execute(data);
					}
				} else {
					isDoneRestoreCallLog = true;
				}
				break;
			case RESTORE_SMS_MOBILE:
				if(data != null){
					if(isRestoreTaskRunning && restoreWorker != null){
						restoreWorker = null;
						restoreWorker = new RestoreWorker();
						restoreWorker.setMethodId(Methods.RESTORE_SMS_MOBILE);
						restoreWorker.execute(data);
					}
				} else {
					isDoneRestoreSms = true;
				}
				break;
			default:
				break;
		}
		if(methods == Methods.RESTORE_CONTACT_MOBILE || methods == Methods.RESTORE_CALL_LOG_MOBILE || methods == Methods.RESTORE_SMS_MOBILE){
			if(isDoneRestoreContact && isDoneRestoreSms && isDoneRestoreCallLog){
				resetRestoreTask();
				if(restoreListener != null){
					restoreListener.onFinishRestore();
				}
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
		resetRestoreTask();
		if(restoreListener != null){
			restoreListener.onRestoreFail();
		}
	}

	/* (non-Javadoc)
	 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onProgressUpdate(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, int)
	 */
	@Override
	public void onProgressUpdate(Methods methods, int progress) {
		
	}
	
	/**
	 * The Class RestoreWorker.
	 */
	private class RestoreWorker extends AsyncTask<List<Object>, Void, Void>{

		/** The method id. */
		private Methods methodId;
		
		/**
		 * Sets the method id.
		 *
		 * @param methodId the new method id
		 */
		public void setMethodId(Methods methodId) {
			this.methodId = methodId;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(List<Object>... params) {
			switch (methodId) {
				case RESTORE_CONTACT_MOBILE:
					onRestoreContact(params[0]);
					break;
				case RESTORE_CALL_LOG_MOBILE:
					onRestoreCallLog(params[0]);
					break;
				case RESTORE_SMS_MOBILE:
					onRestoreMessage(params[0]);
					break;
				default:
					break;
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isRestoreTaskRunning = true;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			switch (methodId) {
				case RESTORE_CONTACT_MOBILE:
					isDoneRestoreContact = true;
					break;
				case RESTORE_SMS_MOBILE:
					isDoneRestoreSms = true;
					break;
				case RESTORE_CALL_LOG_MOBILE:
					isDoneRestoreCallLog = true;
					break;
				default:
					break;
			}
			
			if(!isDoneRestoreSms){
				HttpClientHelper.getInstance().restoreSmsMobile(ProfileData.getInstance(context).getToken());
				smsRestoreTimeout.start();
				return;
			}
			if(!isDoneRestoreCallLog){
				HttpClientHelper.getInstance().restoreCallLogMobile(ProfileData.getInstance(context).getToken());
				callLogRestoreTimeout.start();
				return;
			}
			if(isDoneRestoreContact && isDoneRestoreSms && isDoneRestoreCallLog){
				resetRestoreTask();
				if(restoreListener != null){
					restoreListener.onFinishRestore();
				}
			}
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if(isCancelled()){
				resetRestoreTask();
				if(restoreListener != null){
					restoreListener.onCancelRestore();
				}
			}
		}
	}
}
