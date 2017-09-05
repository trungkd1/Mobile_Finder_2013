/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Thai Le
 * Location: NahiMobileFinder - com.nahi.vn.mobilefinder.webservice - HttpClientHelper.java
 * Date create: 10:42 AM - Dec 16, 2013 - 2013
 * 
 * 
 */
package com.nahi.vn.mobilefinder.webservice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;


// TODO: Auto-generated Javadoc
/**
 * The Class HttpClientHelper.
 */
public class HttpClientHelper {

	/**
	 * The Enum HttpMethod.
	 */
	public enum HttpMethod{
		
		/** The get. */
		GET,
		
		/** The post. */
		POST,
		
		/** The upload file. */
		UPLOAD_FILE
	}
	
	/** The http client helper. */
	private volatile static HttpClientHelper httpClientHelper;

	/** The time out get data (mili seconds). */
	public static int TIME_OUT_GET_DATA = 25000;

	/** The time out connection(mili seconds). */
	public static int TIME_OUT_CONNECTION = 20000;

	/** The listener. */
	private HttpClientListener httpClientListener;

	/**
	 * Gets the single instance of HttpClientHelper.
	 * 
	 * @return single instance of HttpClientHelper
	 */
	public static HttpClientHelper getInstance() {
		if (httpClientHelper == null) {
			synchronized (HttpClientHelper.class) {
				if (httpClientHelper == null) {
					httpClientHelper = new HttpClientHelper();
				}
			}
		}
		return httpClientHelper;
	}

	/**
	 * The Class HttpTask.
	 */
	private class HttpTask extends AsyncTask<Methods, Integer, List<Object>> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (httpClientListener != null && methodId != null) {
				httpClientListener.onHttpResult(methodId, result);
			}
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (httpClientListener != null && methodId != null) {
				if (values != null && values.length > 0) {
					httpClientListener.onProgressUpdate(methodId, values[0]);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (httpClientListener != null && methodId != null) {
				httpClientListener.onStartAPI(methodId);
			}
		}

		/** The method id. */
		private Methods methodId;

		/** The http method. */
		private HttpMethod httpMethod;
		
		/** The url. */
		private String url;
		
		/** The params. */
		private List<NameValuePair> params;
		
		/**
		 * Instantiates a new http task.
		 *
		 * @param method the method
		 * @param methodId the method id
		 * @param url the url
		 * @param params the params
		 */
		public HttpTask(HttpMethod method, Methods methodId, String url, List<NameValuePair> params){
			this.httpMethod = method;
			this.methodId = methodId;
			this.url = url;
			this.params = params;
			
			/*
			 * In some cases we need to add some info to request's header and
			 * send it to server team for some reasons(EX: count how many
			 * request to server) a_ttpUriRequest.addHeader(name, value);
			 * a_ttpUriRequest.addHeader(name, value);
			 * a_ttpUriRequest.addHeader(name, value);
			 * a_ttpUriRequest.addHeader(name, value);
			 * a_ttpUriRequest.addHeader(name, value);
			 */
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<Object> doInBackground(Methods... params) {
			if(methodId == null){
				return null;
			}
			return perform(methodId);
		}

		/**
		 * Perform.
		 * 
		 * @param methodId
		 *            the method id
		 * @return the list
		 */
		private List<Object> perform(Methods methodId) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpConnectionParams.setTcpNoDelay(httpClient.getParams(), true);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					TIME_OUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpClient.getParams(),
					TIME_OUT_GET_DATA);
			List<Object> result = null;
			try {
				HttpResponse response = null;
				switch (httpMethod) {
					case GET:
						if(params.size() > 0){
							String paramString = URLEncodedUtils.format(params, "UTF-8");
							url += "?" + paramString;
						}
						HttpUriRequest httpUriRequest = new HttpGet(url);
						response = httpClient.execute(httpUriRequest);
						break;
					case POST:
			            HttpPost httpPost = new HttpPost(url);
			            if(params.size() > 0){
			            	httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			            }
			            response = httpClient.execute(httpPost);
						break;
					case UPLOAD_FILE:
						MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
						for(NameValuePair nvp : params){
							if(nvp.getName().equalsIgnoreCase("file") || nvp.getName().equalsIgnoreCase("json")
									|| nvp.getName().equalsIgnoreCase("file1") 
									|| nvp.getName().equalsIgnoreCase("file2")){
								File file = new File(nvp.getValue());
								FileBody fileBody = new FileBody(file, "application/octect-stream");
								entity.addPart(nvp.getName(), fileBody);
							}
							else{
								entity.addPart(nvp.getName(), new StringBody(nvp.getValue()));
							}
						}
						HttpPost httppost = new HttpPost(url);
						httppost.setEntity(entity);
						response = httpClient.execute(httppost);
						break;
					default:
						break;
				}
				
				if(response != null){
					final int statusCode = response.getStatusLine().getStatusCode();
					// check status code is OK or not
					if (statusCode != HttpStatus.SC_OK) {
						if (httpClientListener != null) {
							httpClientListener.onCallAPIFail(methodId, statusCode);
						}
						return result;
					}
					
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						try {
							switch (methodId) {
								case RESTORE_CONTACT_MOBILE: 
								case RESTORE_SMS_MOBILE: 
								case RESTORE_CALL_LOG_MOBILE:
								case CHECK_FINDER_PREMIUM:
									result = NahiFinderParser.getInstance().parse(entity.getContent(), methodId);
									break;
								default:
									result = NahiFinderParser.getInstance().parse(EntityUtils.toString(entity), methodId);
									break;
							}
						} catch (Exception e) {
							return result;
						}
					}
				}
			} catch (Exception e) {
				return result;
			} finally{
				httpClient.getConnectionManager().shutdown();
			}
			return result;
		}
	}
	
    /**
	 * Gets the listener.
	 * 
	 * @return the listener
	 */
	public HttpClientListener getListener() {
		return httpClientListener;
	}

	/**
	 * Sets the listener.
	 * 
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(HttpClientListener listener) {
		this.httpClientListener = listener;
	}

	/**
	 * Request session key.
	 *
	 */
	public void requestSessionKey(){
 		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 		nameValuePairs.add(new BasicNameValuePair("api_key", Config.NAHI_API_KEY));
		new HttpTask(HttpMethod.POST, Methods.REQUEST_SESSION_KEY, Config.URL_REQUEST_SESSION_KEY, nameValuePairs).execute();
	}
	
	/**
	 * Mobile login.
	 *
	 * @param email the email
	 * @param password the password
	 * @param deviceId the device id
	 * @param sessionKey the session key
	 * @param packageName the package name
	 */
	public void mobileLogin(String email, String password, String deviceId, String sessionKey, String packageName){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));
		nameValuePairs.add(new BasicNameValuePair("session_key", sessionKey));
		nameValuePairs.add(new BasicNameValuePair("package", packageName));
		new HttpTask(HttpMethod.POST, Methods.MOBILE_LOGIN, Config.URL_MOBILE_LOGIN, nameValuePairs).execute();
	}
	
	/**
	 * Mobile profile.
	 *
	 * @param token the token
	 */
	public void mobileProfile(String token){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		new HttpTask(HttpMethod.POST, Methods.MOBILE_PROFILE, Config.URL_MOBILE_PROFILE, nameValuePairs).execute();
	}
	
	/**
	 * Register.
	 *
	 * @param sessionKey the session key
	 * @param email the email
	 * @param password the password
	 * @param deviceId the device id
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param dob the dob
	 * @param cellPhone the cell phone
	 * @param packageName the package name
	 * @param gender the gender
	 */
	public void register(String sessionKey, String email, String password, String deviceId, String firstName, String lastName, String dob, String cellPhone, String packageName, String gender){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_key", sessionKey));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("deviceid", deviceId));
		nameValuePairs.add(new BasicNameValuePair("first_name", firstName));
		nameValuePairs.add(new BasicNameValuePair("last_name", lastName));
		nameValuePairs.add(new BasicNameValuePair("dob", dob));
		nameValuePairs.add(new BasicNameValuePair("cellphone", cellPhone));
		nameValuePairs.add(new BasicNameValuePair("package", packageName));
		nameValuePairs.add(new BasicNameValuePair("gender", gender));
		new HttpTask(HttpMethod.POST, Methods.REGISTER, Config.URL_REGISTER, nameValuePairs).execute();
	}
	
	/**
	 * Check valid account.
	 *
	 * @param token the token
	 * @param email the email
	 */
	public void checkValidAccount(String token, String email){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		new HttpTask(HttpMethod.POST, Methods.CHECK_VALID_ACCOUNT, Config.URL_CHECK_VALID_ACCOUNT, nameValuePairs).execute();
	}
	
	/**
	 * Sync setting mobile.
	 *
	 * @param token the token
	 */
	public void syncSettingMobile(String token){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		new HttpTask(HttpMethod.POST, Methods.SYNC_SETTING_MOBILE, Config.URL_SYNC_SETTING_MOBILE, nameValuePairs).execute();
	}
	
	/**
	 * Sync setting server.
	 *
	 * @param token the token
	 * @param regPhones the reg phones
	 * @param loginUser the login user
	 * @param allowNahiRemote the allow nahi remote
	 * @param enableViewTracking the enable view tracking
	 */
	public void syncSettingServer(String token, String regPhones, String loginUser, String allowNahiRemote, String enableViewTracking){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("rep_phone", regPhones));
		nameValuePairs.add(new BasicNameValuePair("login_user", loginUser));
		nameValuePairs.add(new BasicNameValuePair("nahi_admin", allowNahiRemote));
		nameValuePairs.add(new BasicNameValuePair("enable_view_tracking", enableViewTracking));
		new HttpTask(HttpMethod.POST, Methods.SYNC_SETTING_SERVER, Config.URL_SYNC_SETTING_SERVER, nameValuePairs).execute();
	}
	
	/**
	 * Register gcm.
	 *
	 * @param token the token
	 * @param regId the reg id
	 * @param deviceName the device name
	 */
	public void registerGcm(String token, String regId, String deviceName){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("reg_id", regId));
		nameValuePairs.add(new BasicNameValuePair("device_name", deviceName));
		nameValuePairs.add(new BasicNameValuePair("site", Config.SITE_MOBILE_FINDER));
		new HttpTask(HttpMethod.POST, Methods.REGISTER_GCM, Config.URL_REGISTER_GCM, nameValuePairs).execute();
	}
	
	/**
	 * Backup contact server.
	 *
	 * @param token the token
	 * @param filePath the file path
	 */
	public void backupContactServer(String token, String filePath){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("json", filePath));
		nameValuePairs.add(new BasicNameValuePair("type", Config.NAHI_CONTACT_TYPE));
		new HttpTask(HttpMethod.UPLOAD_FILE, Methods.BACKUP_CONTACT_SERVER, Config.URL_BACKUP_CONTACT_SERVER, nameValuePairs).execute();
	}
	
	/**
	 * Backup sms server.
	 *
	 * @param token the token
	 * @param filePath the file path
	 */
	public void backupSmsServer(String token, String filePath){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("json", filePath));
		nameValuePairs.add(new BasicNameValuePair("type", Config.NAHI_SMS_TYPE));
		new HttpTask(HttpMethod.UPLOAD_FILE, Methods.BACKUP_SMS_SERVER, Config.URL_BACKUP_SMS_SERVER, nameValuePairs).execute();
	}
	
	/**
	 * Backup call log server.
	 *
	 * @param token the token
	 * @param filePath the file path
	 */
	public void backupCallLogServer(String token, String filePath){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("json", filePath));
		nameValuePairs.add(new BasicNameValuePair("type", Config.NAHI_CALL_LOG_TYPE));
		new HttpTask(HttpMethod.UPLOAD_FILE, Methods.BACKUP_CALL_LOG_SERVER, Config.URL_BACKUP_CALL_LOG_SERVER, nameValuePairs).execute();
	}
	
	/**
	 * Restore contact server.
	 *
	 * @param token the token
	 */
	public void restoreContactMobile(String token){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("type", Config.NAHI_CONTACT_TYPE));
		new HttpTask(HttpMethod.POST, Methods.RESTORE_CONTACT_MOBILE, Config.URL_RESTORE_CONTACT_MOBILE, nameValuePairs).execute();
	}
	
	/**
	 * Restore sms mobile.
	 *
	 * @param token the token
	 */
	public void restoreSmsMobile(String token){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("type", Config.NAHI_SMS_TYPE));
		new HttpTask(HttpMethod.POST, Methods.RESTORE_SMS_MOBILE, Config.URL_RESTORE_SMS_MOBILE, nameValuePairs).execute();
	}
	
	/**
	 * Restore call log mobile.
	 *
	 * @param token the token
	 */
	public void restoreCallLogMobile(String token){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("type", Config.NAHI_CALL_LOG_TYPE));
		new HttpTask(HttpMethod.POST, Methods.RESTORE_CALL_LOG_MOBILE, Config.URL_RESTORE_CALL_LOG_MOBILE, nameValuePairs).execute();
	}
	
	/**
	 * Upload picture.
	 *
	 * @param token the token
	 * @param fileList the file list
	 */
	public void uploadPicture(String token, List<String> fileList){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		if(fileList.size() == 1){
			nameValuePairs.add(new BasicNameValuePair("file1", fileList.get(0)));
		} else if(fileList.size() == 2){
			nameValuePairs.add(new BasicNameValuePair("file1", fileList.get(0)));
			nameValuePairs.add(new BasicNameValuePair("file2", fileList.get(1)));
		}
		new HttpTask(HttpMethod.UPLOAD_FILE, Methods.UPLOAD_PICTURE, Config.URL_UPLOAD_PICTURE, nameValuePairs).execute();
	}
	
	/**
	 * Tracking device.
	 *
	 * @param token the token
	 * @param filePath the file path
	 */
	public void trackingDevice(String token, String filePath){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("json", filePath));
		new HttpTask(HttpMethod.UPLOAD_FILE, Methods.TRACKING_DEVICE, Config.URL_UPLOAD_TRACKING, nameValuePairs).execute();
	}
	
	/**
	 * Gets the location.
	 *
	 * @param token the token
	 * @param lat the lat
	 * @param lng the lng
	 * @param address the address
	 * @return the location
	 */
	public void getLocation(String token, String lat, String lng, String address){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("long", lng));
		nameValuePairs.add(new BasicNameValuePair("address", address));
		new HttpTask(HttpMethod.POST, Methods.GET_LOCATION, Config.URL_UPLOAD_LOCATION, nameValuePairs).execute();
	}
	
	/**
	 * Update status command.
	 *
	 * @param token the token
	 * @param cmdId the cmd id
	 * @param data the data
	 */
	public void updateStatusCommand(String token, String cmdId, String data){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("site", Config.SITE_MOBILE_FINDER));
		nameValuePairs.add(new BasicNameValuePair("cmd_id", cmdId));
		if(!TextUtils.isEmpty(data)){
			nameValuePairs.add(new BasicNameValuePair("data", data));
		}
		new HttpTask(HttpMethod.POST, Methods.UPDATE_STATUS_COMMAND, Config.URL_UPDATE_STATUS_COMMAND, nameValuePairs).execute();
	}
	
	/**
	 * Check finder premium.
	 *
	 * @param token the token
	 */
	public void checkFinderPremium(String token){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("site", Config.SITE_MOBILE_FINDER));
		new HttpTask(HttpMethod.POST, Methods.CHECK_FINDER_PREMIUM, Config.URL_CHECK_FINDER_PREMIUM, nameValuePairs).execute();
	}
	
	/**
	 * Update device name.
	 *
	 * @param token the token
	 * @param deviceName the device name
	 */
	public void updateDeviceName(String token, String deviceName){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("device_name", deviceName));
		nameValuePairs.add(new BasicNameValuePair("site", Config.SITE_MOBILE_FINDER));
		new HttpTask(HttpMethod.POST, Methods.UPDATE_DEVICE_NAME, Config.URL_UPDATE_DEVICE_NAME, nameValuePairs).execute();
	}
}
