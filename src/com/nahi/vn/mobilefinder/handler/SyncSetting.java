package com.nahi.vn.mobilefinder.handler;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.entity.ContactObj;
import com.nahi.vn.mobilefinder.entity.SyncSettingResult;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;

// TODO: Auto-generated Javadoc
/**
 * The Class SyncSetting.
 */
public class SyncSetting {
	
	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new alert sound.
	 *
	 * @param context the context
	 */
	public SyncSetting(Context context){
		this.context = context;
	}
	
	/**
	 * Sync setting server.
	 */
	public void syncSettingServer(){
		ArrayList<ContactObj> contacts = App.getDB().getAllContacts();
		String regPhones = "";
		if(contacts.size() > 0){
			List<String> numbers = new ArrayList<String>();
			for(ContactObj co : contacts){
				numbers.add(co.number);
			}
			regPhones = AppUtil.mergeArrToString(numbers, Config.SPLIT_PATTERN_COMMA);
		}
		if(!TextUtils.isEmpty(ProfileData.getInstance(context).getToken())
				&& !TextUtils.isEmpty(regPhones)
				&& !TextUtils.isEmpty(ProfileData.getInstance(context).getEmail())){
			HttpClientHelper.getInstance().syncSettingServer(ProfileData.getInstance(context).getToken(), regPhones, 
					ProfileData.getInstance(context).getEmail(), 
					SaveData.getInstance(context).isControlFromNAHI() ? "1" : "0",
					SaveData.getInstance(context).getEnableViewTracking() ? "1" : "0");
		}
	}
	
	/**
	 * Sync setting mobile.
	 */
	public void syncSettingMobile(){
		if(!TextUtils.isEmpty(ProfileData.getInstance(context).getToken())){
			HttpClientHelper.getInstance().syncSettingMobile(ProfileData.getInstance(context).getToken());
		}
		HttpClientHelper.getInstance().setListener(new HttpClientListener() {
			
			@Override
			public void onStartAPI(Methods methods) {
			}
			
			@Override
			public void onProgressUpdate(Methods methods, int progress) {
			}
			
			@Override
			public void onHttpResult(Methods methods, List<Object> data) {
				if(methods == Methods.SYNC_SETTING_MOBILE){
					if(data != null && data.size() > 0){
						if(data.get(0) instanceof SyncSettingResult){
							SyncSettingResult rs = (SyncSettingResult) data.get(0);
							
							ArrayList<ContactObj> dbContacts = App.getDB().getAllContacts();
							if(rs.getRegPhone().size() > 0){
								//Add new filter numbers to db if not existing
								for(String number : rs.getRegPhone()){
									App.getDB().insertContact(new ContactObj("", number));
								}
								//Delete numbers
								for(ContactObj c : dbContacts){
									boolean isRemoveNumber = true;
									for(String number : rs.getRegPhone()){
										if(AppUtil.isMatchPhone(context, c.number, number)){
											isRemoveNumber = false;
											break;
										}
									}
									//Delete numbers that don't have on the server list
									if(isRemoveNumber){
										App.getDB().deleteContact(c.number);
									}
								}
							} else {
								//Delete all numbers on db
								if(dbContacts.size() > 0){
									App.getDB().deleteAllContacts();
								}
							}
							ProfileData.getInstance(context).setEmail(rs.getLoginUser());
							SaveData.getInstance(context).setControlFromNAHI(rs.isAllowNahiRemote());
						}
					}
				}
			}
			
			@Override
			public void onCallAPIFail(Methods methods, int statusCode) {
			}
		});
	}
}
