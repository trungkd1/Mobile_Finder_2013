/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Thai Le
 * Location: NahiMobileFinder - com.nahi.vn.mobilefinder.webservice - NahiFinderParser.java
 * Date create: 10:42 AM - Dec 16, 2013 - 2013
 * 
 * 
 */
package com.nahi.vn.mobilefinder.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.nahi.vn.mobilefinder.entity.CallLogEntry;
import com.nahi.vn.mobilefinder.entity.CheckPremiumResponse;
import com.nahi.vn.mobilefinder.entity.ContactEntry;
import com.nahi.vn.mobilefinder.entity.MobileLoginResult;
import com.nahi.vn.mobilefinder.entity.MobileProfileResult;
import com.nahi.vn.mobilefinder.entity.PremiumResult;
import com.nahi.vn.mobilefinder.entity.ResponseResult;
import com.nahi.vn.mobilefinder.entity.SMSEntry;
import com.nahi.vn.mobilefinder.entity.SessionKeyResult;
import com.nahi.vn.mobilefinder.entity.SyncSettingResult;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.Log;
import com.nahi.vn.mobilefinder.webservice.json.JsonReader;
import com.nahi.vn.mobilefinder.webservice.json.JsonToken;




// TODO: Auto-generated Javadoc
/**
 * The Class NahiParser.
 */
public class NahiFinderParser {

	/**
	 * The Enum Methods.
	 */
	public enum Methods {
		
		/** The mobile login. */
		MOBILE_LOGIN,
		
		/** The register. */
		REGISTER,
		
		/** The mobile profile. */
		MOBILE_PROFILE,
		
		/** The request session key. */
		REQUEST_SESSION_KEY,
		
		/** The check valid account. */
		CHECK_VALID_ACCOUNT,
		
		/** The register gcm. */
		REGISTER_GCM,
		
		
		/** The backup contact server. */
		BACKUP_CONTACT_SERVER,
		
		/** The backup sms server. */
		BACKUP_SMS_SERVER,
		
		/** The backup call log server. */
		BACKUP_CALL_LOG_SERVER,
		
		/** The restore contact mobile. */
		RESTORE_CONTACT_MOBILE,
		
		/** The restore sms mobile. */
		RESTORE_SMS_MOBILE,
		
		/** The restore call log mobile. */
		RESTORE_CALL_LOG_MOBILE,
		
		/** The upload picture. */
		UPLOAD_PICTURE,
		
		/** The track location. */
		TRACKING_DEVICE,
		
		/** The get location. */
		GET_LOCATION,
		
		/** The sync setting mobile. */
		SYNC_SETTING_MOBILE,
		
		/** The sync setting server. */
		SYNC_SETTING_SERVER,
		
		/** The update status command. */
		UPDATE_STATUS_COMMAND,
		
		/** The check finder premium. */
		CHECK_FINDER_PREMIUM,
		
		/** The update device name. */
		UPDATE_DEVICE_NAME
		
	}

	/** The nahi parser. */
	private static NahiFinderParser nahiParser = null;

	/**
	 * Gets the single instance of NahiParser.
	 * 
	 * @return single instance of NahiParser
	 */
	public static NahiFinderParser getInstance() {
		if (nahiParser == null) {
			nahiParser = new NahiFinderParser();
		}
		return nahiParser;
	}

	/**
	 * Parses the.
	 *
	 * @param content the content
	 * @param methodId the method id
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Object> parse(InputStream content, Methods methodId)
			throws IOException {
		JsonReader reader = null;
		if (content == null){
			return null;
		}
		try {
			reader = new JsonReader(new InputStreamReader(content, HTTP.UTF_8));
			reader.setLenient(true);
		} catch (UnsupportedEncodingException e) {
			Log.e("parse error:" + e.toString());
		}
		switch (methodId) {
			case RESTORE_CONTACT_MOBILE: 
				return parseRestoreContactMobile(reader);
			case RESTORE_SMS_MOBILE: 
				return parseRestoreSmsMobile(reader);
			case RESTORE_CALL_LOG_MOBILE: 
				return parseRestoreCallLogMobile(reader);
			case CHECK_FINDER_PREMIUM: 
				return parsecheckFinderPremium(reader);
			default:
				return null;
		}
	}
	/**
	 * Parsecheck finder premium.
	 *
	 * @param reader the reader
	 * @return the list
	 */
	private List<Object> parsecheckFinderPremium(JsonReader reader) {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<Object>();
		ResponseResult responseResult = new ResponseResult();
		PremiumResult premiumResult=null;
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (reader.peek() == JsonToken.NULL) {
					reader.skipValue();
					continue;
				}
				if (name.equals("status")) {
					responseResult.setStatus(Integer.parseInt(reader
							.nextString()));
				} else if (name.equals("message")) {
					responseResult.setMessage(reader.nextString());
				} else if (name.equals("result")) {
					premiumResult=readPremium(reader);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} catch (Exception e) {
			android.util.Log.d("Tamle", e.toString());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				android.util.Log.d("Tamle", e.toString());
			}
		}
		CheckPremiumResponse checkPremiumResponse=new CheckPremiumResponse();
		checkPremiumResponse.setResponseResult(responseResult);
		if (premiumResult!=null) {
			checkPremiumResponse.setPremiumResult(premiumResult);
		}
		list.add(checkPremiumResponse);
		return list;
	}

	/**
	 * Read premium.
	 *
	 * @param reader the reader
	 * @return the premium result
	 */
	private PremiumResult readPremium(JsonReader reader) {
		// TODO Auto-generated method stub
		PremiumResult premiumResult=new PremiumResult();
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (reader.peek() == JsonToken.NULL) {
					reader.skipValue();
					continue;
				}
				if (name.equals("premium")) {
					premiumResult.setPremium(reader
							.nextInt());
				} else if (name.equals("expired")) {
					premiumResult.setExpired(reader.nextString());
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		} catch (Exception e) {
			android.util.Log.d("Tamle", e.toString());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				android.util.Log.d("Tamle", e.toString());
			}
		}
		return premiumResult;
	}

	/**
	 * Parses the.
	 *
	 * @param result the result
	 * @param methodId the method id
	 * @return the list
	 */
	public List<Object> parse(String result, Methods methodId) {
		Log.e("parse :" + result + " - methodId :" + methodId);
		JSONObject json;
		try {
			json = new JSONObject(result);
			JSONObject rs = null;
			if(json.has("result")){
				rs = json.getJSONObject("result");
			}
			switch (methodId) {
				case MOBILE_LOGIN:
					return parseMobileLogin(rs);
				case REGISTER:
					return parseRegister(rs);
				case MOBILE_PROFILE:
					return parseMobileProfile(json);
				case REQUEST_SESSION_KEY:
					return parseRequestSessionKey(rs);
				case CHECK_VALID_ACCOUNT:
					return parseCheckValidAccount(rs);
				case REGISTER_GCM:
					return parseRegisterGcm(json);
				case BACKUP_CONTACT_SERVER:
					return parseBackupContactServer(json);
				case BACKUP_SMS_SERVER:
					return parseBackupSmsServer(json);
				case BACKUP_CALL_LOG_SERVER: 
					return parseBackupCallLogServer(json);
				case UPLOAD_PICTURE: 
					return parseUploadPicture(json);
				case TRACKING_DEVICE:
					return parseTrackingDevice(json);
				case GET_LOCATION:
					return parseGetLocation(json);
				case SYNC_SETTING_MOBILE:
					return parseSyncSettingMobile(rs);
				case SYNC_SETTING_SERVER:
					return parseSyncSettingServer(json);
				case UPDATE_STATUS_COMMAND:
					return parseUpdateStatusCommand(json);
				case UPDATE_DEVICE_NAME:
					return parseUpdateDeviceName(json);
				default:
					return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses the request session key.
	 *
	 * @param result the result
	 * @return the list
	 */
	private List<Object> parseRequestSessionKey(JSONObject result){
		List<Object> list = new ArrayList<Object>();
		try {
			if(result.has("session_key") && result.has("session_key")){
				list.add(new SessionKeyResult(result.getString("session_key"), result.getString("expired_time")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the mobile login.
	 *
	 * @param result the result
	 * @return the list
	 */
	private List<Object> parseMobileLogin(JSONObject result){
		List<Object> list = new ArrayList<Object>();
		try {
			if(result.has("id") && result.has("token") && result.has("expired")){
				list.add(new MobileLoginResult(result.getInt("id"), result.getString("token"), result.getString("expired")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the mobile profile.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseMobileProfile(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("result")){
				JSONObject result = json.getJSONObject("result");
				MobileProfileResult profile = new MobileProfileResult();
				profile.setId(result.optInt("id"));
				profile.setFullName(result.optString("fullname"));
				profile.setEmail(result.optString("email"));
				profile.setCellPhone(result.optString("cellphone"));
				profile.setDob(result.optString("dob"));
				profile.setGender(result.optString("gender"));
				profile.setImage(result.optString("image"));
				profile.setKidId(result.optInt("kid_id"));
				profile.setKidFullname(result.optString("kid_fullname"));
				profile.setKidEmail(result.optString("kid_email"));
				profile.setKidCellphone(result.optString("kid_cellphone"));
				profile.setKidDob(result.optString("kid_dob"));
				profile.setKidGender(result.optString("kid_gender"));
				profile.setKidImage(result.optString("kid_image"));
				list.add(profile);
			} else {
				if(json.has("status") && json.has("message")){
					list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
				}
			}
		} catch (Exception e) {
			Log.e("parseMobileProfile  error:" + e.toString());
		}
		return list;
	}
	
	/**
	 * Parses the register.
	 *
	 * @param result the result
	 * @return the list
	 */
	private List<Object> parseRegister(JSONObject result){
		List<Object> list = new ArrayList<Object>();
		try {
			if(result.has("id") && result.has("token") && result.has("expired")){
				list.add(new MobileLoginResult(result.getInt("id"), result.getString("token"), result.getString("expired")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the check valid account.
	 *
	 * @param result the result
	 * @return the list
	 */
	private List<Object> parseCheckValidAccount(JSONObject result){
		List<Object> list = new ArrayList<Object>();
		try {
			if(result.has("customer_id")){
				list.add(result.getInt("customer_id"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the sync setting mobile.
	 *
	 * @param result the result
	 * @return the list
	 */
	private List<Object> parseSyncSettingMobile(JSONObject result){
		List<Object> list = new ArrayList<Object>();
		try {
			SyncSettingResult setting = new SyncSettingResult();
			setting.setRegPhone(result.optString("rep_phone"));
			setting.setLoginUser(result.optString("login_user"));
			setting.setAllowNahiRemote(result.optInt("nahi_admin") == 1 ? true : false);
			list.add(setting);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the sync setting server.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseSyncSettingServer(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the register gcm.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseRegisterGcm(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the backup contact server.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseBackupContactServer(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the backup sms server.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseBackupSmsServer(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the backup call log server.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseBackupCallLogServer(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the restore contact server.
	 *
	 * @param reader the reader
	 * @return the list
	 */
	private List<Object> parseRestoreContactMobile(JsonReader reader){
		List<Object> list = new ArrayList<Object>();
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (reader.peek() == JsonToken.NULL) {
					reader.skipValue();
					continue;
				}
				if (name.equals(Config.DATA)) {
					reader.beginArray();
					while (reader.hasNext()) {
						ContactEntry contact = readContactResult(reader);
						if(contact != null){
							list.add(contact);
						}
					}
					reader.endArray();
				}
			}
			reader.endObject();
		} catch (IOException e) {
			Log.e("parseRestoreContactMobile error:" + e.toString());
		}
		return list;
	}
	
	/**
	 * Read contact result.
	 *
	 * @param input the input
	 * @return the contact entry
	 */
	private ContactEntry readContactResult(JsonReader input) {
		boolean objectNull = true;
		ContactEntry contact = new ContactEntry();
		try {
			input.beginObject();
			while (input.hasNext()) {
				String name = input.nextName();
				if (input.peek() == JsonToken.NULL) {
					input.skipValue();
					continue;
				}
				if (name.equals(Config.CONTACT_NAME)) {
					objectNull = false;
					contact.setName(input.nextString());
				} else if (name.equals(Config.CONTACT_NUMBER)) {
					objectNull = false;
					contact.setNumber(input.nextString());
				} else if (name.equals(Config.CONTACT_TYPE)) {
					objectNull = false;
					contact.setType(input.nextString());
				} else {
					input.skipValue();
				}
			}
			if(objectNull){
				return null;
			}
			input.endObject();
		} catch (Exception e) {
			Log.e(e.toString());
		}
		return contact;
	}
	
	/**
	 * Parses the restore sms mobile.
	 *
	 * @param reader the reader
	 * @return the list
	 */
	private List<Object> parseRestoreSmsMobile(JsonReader reader){
		List<Object> list = new ArrayList<Object>();
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (reader.peek() == JsonToken.NULL) {
					reader.skipValue();
					continue;
				}
				if (name.equals(Config.DATA)) {
					reader.beginArray();
					while (reader.hasNext()) {
						SMSEntry sms = readSMSResult(reader);
						if(sms != null){
							list.add(sms);
						}
					}
					reader.endArray();
				}
			}
			reader.endObject();
		} catch (IOException e) {
			Log.e("parseRestoreSmsMobile error:" + e.toString());
		}
		return list;
	}
	
	/**
	 * Read sms result.
	 *
	 * @param input the input
	 * @return the sMS entry
	 */
	private SMSEntry readSMSResult(JsonReader input) {
		boolean objectNull = true;
		SMSEntry sms = new SMSEntry();
		try {
			input.beginObject();
			while (input.hasNext()) {
				String name = input.nextName();
				if (input.peek() == JsonToken.NULL) {
					input.skipValue();
					continue;
				}
				if (name.equals(Config.SMS_BODY)) {
					objectNull = false;
					sms.mBody = input.nextString();
				} else if (name.equals(Config.DATE)) {
					objectNull = false;
					sms.mDate = input.nextString();
				} else if (name.equals(Config.TYPE)) {
					objectNull = false;
					sms.mType = input.nextString();
				} else if (name.equals(Config.SMS_PERSON)) {
					objectNull = false;
					sms.mName = input.nextString();
				} else if (name.equals(Config.SMS_ADDRESS)) {
					objectNull = false;
					sms.mNumber = input.nextString();
				} else if (name.equals(Config.SMS_READ)) {
					objectNull = false;
					sms.mRead = input.nextString();
				} else {
					input.skipValue();
				}
			}
			if(objectNull){
				return null;
			}
			input.endObject();
		} catch (Exception e) {
			Log.e(e.toString());
		}
		return sms;
	}
	
	/**
	 * Parses the restore call log mobile.
	 *
	 * @param reader the reader
	 * @return the list
	 */
	private List<Object> parseRestoreCallLogMobile(JsonReader reader){
		List<Object> list = new ArrayList<Object>();
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (reader.peek() == JsonToken.NULL) {
					reader.skipValue();
					continue;
				}
				if (name.equals(Config.DATA)) {
					reader.beginArray();
					while (reader.hasNext()) {
						CallLogEntry callLog = readCallLogResult(reader);
						if(callLog != null){
							list.add(callLog);
						}
					}
					reader.endArray();
				}
			}
			reader.endObject();
		} catch (IOException e) {
			Log.e("parseRestoreCallLogMobile error:" + e.toString());
		}
		return list;
	}
	
	/**
	 * Read call log result.
	 *
	 * @param input the input
	 * @return the call log entry
	 */
	private CallLogEntry readCallLogResult(JsonReader input) {
		boolean objectNull = true;
		CallLogEntry callLog = new CallLogEntry();
		try {
			input.beginObject();
			while (input.hasNext()) {
				String name = input.nextName();
				if (input.peek() == JsonToken.NULL) {
					input.skipValue();
					continue;
				}
				if (name.equals(Config.CALL_LOG_NUMBER)) {
					objectNull = false;
					callLog.setNumber(input.nextString());
				} else if (name.equals(Config.DATE)) {
					objectNull = false;
					callLog.setDate(input.nextString());
				} else if (name.equals(Config.CALL_LOG_DURATION)) {
					objectNull = false;
					callLog.setDuration(input.nextString());
				} else if (name.equals(Config.TYPE)) {
					objectNull = false;
					callLog.setType(input.nextString());
				} else if (name.equals(Config.CALL_LOG_NAME)) {
					objectNull = false;
					callLog.setName(input.nextString());
				} else {
					input.skipValue();
				}
			}
			if(objectNull){
				return null;
			}
			input.endObject();
		} catch (Exception e) {
			Log.e(e.toString());
		}
		return callLog;
	}
	
	/**
	 * Parses the upload picture.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseUploadPicture(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the tracking device.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseTrackingDevice(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the get location.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseGetLocation(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the update status command.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseUpdateStatusCommand(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Parses the update device name.
	 *
	 * @param json the json
	 * @return the list
	 */
	private List<Object> parseUpdateDeviceName(JSONObject json){
		List<Object> list = new ArrayList<Object>();
		try {
			if(json.has("status") && json.has("message")){
				list.add(new ResponseResult(json.optInt("status"), json.optString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
