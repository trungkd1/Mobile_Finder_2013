package com.nahi.vn.mobilefinder.util;

import android.content.Context;
import android.content.SharedPreferences;

// TODO: Auto-generated Javadoc
/**
 * The Class ProfileData.
 */
public class ProfileData {

	/** The token. */
	private static String TOKEN = "TOKEN";
	
	/** The email. */
	private static String EMAIL = "EMAIL";
	
	/** The gcm id. */
	private static String GCM_ID = "GCM_ID";
	
	/** The cell phone. */
	private static String CELL_PHONE = "CELL_PHONE";
	
	/** The is premium. */
	private static String IS_PREMIUM = "IS_PREMIUM";
	
	/** The share preference. */
	private SharedPreferences sharePreference;

	/** The data. */
	private static ProfileData data;
	
	/**
	 * Instantiates a new save data.
	 * 
	 * @param context
	 *            the context
	 */
	private ProfileData(Context context) {
		sharePreference = context.getSharedPreferences("finder",
				Context.MODE_PRIVATE);
	}

	
	/**
	 * Gets the single instance of ProfileData.
	 *
	 * @param context the context
	 * @return single instance of ProfileData
	 */
	public static ProfileData getInstance(Context context) {
		if (data == null) {
			data = new ProfileData(context);
		}
		return data;
	}
	
	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token){
		sharePreference.edit().putString(TOKEN, token).commit();
	}
	
	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken(){
		 return sharePreference.getString(TOKEN, "");
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email){
		sharePreference.edit().putString(EMAIL, email).commit();
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail(){
		return sharePreference.getString(EMAIL, "");
	}
	
	/**
	 * Sets the gCM id.
	 *
	 * @param id the new gCM id
	 */
	public void setGCMId(String id){
		sharePreference.edit().putString(GCM_ID, id).commit();
	}
	
	/**
	 * Gets the gCM id.
	 *
	 * @return the gCM id
	 */
	public String getGCMId(){
		 return sharePreference.getString(GCM_ID, "");
	}
	
	/**
	 * Sets the cellphone.
	 *
	 * @param number the new cellphone
	 */
	public void setCellphone(String number){
		sharePreference.edit().putString(CELL_PHONE, number).commit();
	}
	
	/**
	 * Gets the cellphone.
	 *
	 * @return the cellphone
	 */
	public String getCellphone(){
		 return sharePreference.getString(CELL_PHONE, "");
	}
	
	/**
	 * Sets the premium.
	 *
	 * @param isPremium the new premium
	 */
	public void setPremium(boolean isPremium){
		sharePreference.edit().putBoolean(IS_PREMIUM, isPremium).commit();
	}
	
	/**
	 * Checks if is premium.
	 *
	 * @return true, if is premium
	 */
	public boolean isPremium(){
		 return sharePreference.getBoolean(IS_PREMIUM, false);
	}
}
