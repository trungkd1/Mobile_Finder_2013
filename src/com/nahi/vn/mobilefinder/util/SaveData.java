/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.util - SaveData.java
 * Date create: 2:59:19 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.nahi.vn.mobilefinder.util;

import android.content.Context;
import android.content.SharedPreferences;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveData.
 */
public class SaveData {

	/** The save data. */
	private static SaveData saveData = null;	
	
	/** The is first use. */
	private static String IS_FIRST_USE = "IS_FIRST_USE";
	
	/** The is premium version. */
	private static String IS_PREMIUM = "IS_PREMIUM";
		
	/** The is backup data. */
	private static String IS_AUTO_BACKUP = "IS_AUTO_BACKUP";
	
	/** The is auto protect. */
	private static String IS_AUTO_PROTECT = "IS_AUTO_PROTECT";
	
	/** The is controlled from sim. */
	private static String IS_CONTROL_FROM_SIM = "IS_CONTROL_FROM_SIM";
	
	/** The is controlled from NAHI. */
	private static String IS_CONTROL_FROM_NAHI = "IS_CONTROL_FROM_NAHI";
	
	/** The is capture. */
	private static String IS_CAPTURE = "IS_CAPTURE";
	
	/** The is connect network. */
	private static String IS_CONNECT_NETWOK = "IS_CONNECT_NETWOK";
	
	/** The is show icon. */
	private static String IS_SHOW_ICON = "IS_SHOW_ICON";
	
	/** The is backup data. */
	private static String IS_BACKUP_DATA = "IS_BACKUP_DATA";
	
	/** The is restore data. */
	private static String IS_RESTORE_DATA = "IS_RESTORE_DATA";
	
	/** The is delete data and reset factory. */
	private static String IS_DELETE_DATA_AND_RESET = "IS_DELETE_DATA_AND_RESET";
	
	/** The is notification when sim is changed. */
	private static String IS_NOTIFICATION_SIM_CHANGED = "IS_NOTIFICATION_SIM_CHANGED";
	
	/** The is notification when sim is changed. */
	private static String IS_FOLLOWING_24h = "IS_FOLLOWING_24h";
	
	/** The is location of device. */
	private static String IS_GET_LOCATION = "IS_GET_LOCATION";
	
	/** The is times which login failed. */
	private static String TIMES_LOGIN_FAILED = "TIMES_LOGIN_FAILED";
	
	/** The setup login. */
	private static String SETUP_LOGIN = "SETUP_LOGIN"; 
	
	/** The uri sound alarm. */
	private static String URI_SOUND_ALARM = "URI_SOUND_ALARM";
	
	/** The sim number. */
	private static String SIM_NUMBER = "SIM_NUMBER";
	
	/** The name device. */
	private static String NAME_DEVICE = "NAME_DEVICE";
	
	/** The serial sim device. */
	private static String SERIAL_SIM_DEVICE = "SERIAL_SIM_DEVICE";
	
	/** The expire premium. */
	private static String EXPIRE_PREMIUM = "EXPIRE_PREMIUM";
	
	
	/** The share preference. */
	private SharedPreferences sharePreference;

	/**
	 * Instantiates a new save data.
	 * 
	 * @param context
	 *            the context
	 */
	private SaveData(Context context) {
		sharePreference = context.getSharedPreferences("finder",
				Context.MODE_PRIVATE);
	}

	/**
	 * Gets the single instance of SaveData.
	 * 
	 * @param context
	 *            the context
	 * @return single instance of SaveData
	 */
	public static SaveData getInstance(Context context) {
		if (saveData == null) {
			saveData = new SaveData(context);
		}
		return saveData;
	}
	
	
	/**
	 * Check the first use.
	 *
	 * @return true, if is first use
	 */
	public boolean isFirstUse() {
		return sharePreference.getBoolean(IS_FIRST_USE, true);
	}

	
	/**
	 * Set the first use.
	 *
	 * @param isFirstUse the new first use
	 */
	public void setFirstUse(boolean isFirstUse) {
		sharePreference.edit().putBoolean(IS_FIRST_USE, isFirstUse).commit();

	}
	
	/**
	 * Check version is premium.
	 *
	 * @return true, if is premium
	 */
	public int getPremium() {
		return sharePreference.getInt(IS_PREMIUM, 0);
	}

	
	/**
	 * Set version premium.
	 *
	 * @param values the new premium
	 */
	public void setPremium(int values) {
		sharePreference.edit().putInt(IS_PREMIUM, values).commit();

	}
	
	/**
	 * Gets the expire premium.
	 *
	 * @return the expire premium
	 */
	public long getExpirePremium(){
		return sharePreference.getLong(EXPIRE_PREMIUM, 0);
	}
	
	
	/**
	 * Sets the expire premium.
	 *
	 * @param time the new expire premium
	 */
	public void setExpirePremium(long time){
		sharePreference.edit().putLong(EXPIRE_PREMIUM, time).commit();
	}
	
	/**
	 * Check auto backup data.
	 *
	 * @return true, if is auto backup
	 */
	public boolean isAutoBackup() {
		return sharePreference.getBoolean(IS_AUTO_BACKUP, true);
	}

	
	/**
	 * Set auto backup data.
	 *
	 * @param isAutoBackup the new auto backup
	 */
	public void setAutoBackup(boolean isAutoBackup) {
		sharePreference.edit().putBoolean(IS_AUTO_BACKUP, isAutoBackup).commit();

	}
	
	/**
	 * Check auto protect.
	 *
	 * @return true, if is auto protect
	 */
	public boolean isAutoProtect() {
		return sharePreference.getBoolean(IS_AUTO_PROTECT, true);
	}

	
	/**
	 * Set auto protect.
	 *
	 * @param isAutoProtect the new auto protect
	 */
	public void setAutoProtect(boolean isAutoProtect) {
		sharePreference.edit().putBoolean(IS_AUTO_PROTECT, isAutoProtect).commit();

	}
	
	/**
	 * Check control from sim.
	 *
	 * @return true, if is control from sim
	 */
	public boolean isControlFromSim() {
		return sharePreference.getBoolean(IS_CONTROL_FROM_SIM, true);
	}

	/**
	 * Set control from sim.
	 *
	 * @param isControlFromSim the new control from sim
	 */
	public void setControlFromSim(boolean isControlFromSim) {
		sharePreference.edit().putBoolean(IS_CONTROL_FROM_SIM, isControlFromSim).commit();

	}
	
	/**
	 * Check control from NAHI.
	 *
	 * @return true, if is control from nahi
	 */
	public boolean isControlFromNAHI() {
		return sharePreference.getBoolean(IS_CONTROL_FROM_NAHI, true);
	}

	/**
	 * Set control from NAHI.
	 *
	 * @param isControlFromNAHI the new control from nahi
	 */
	public void setControlFromNAHI(boolean isControlFromNAHI) {
		sharePreference.edit().putBoolean(IS_CONTROL_FROM_NAHI, isControlFromNAHI).commit();

	}
	
	/**
	 * Check capture when login failed!.
	 *
	 * @return true, if is capture
	 */
	public boolean isCapture() {
		return sharePreference.getBoolean(IS_CAPTURE, true);
	}

	/**
	 * Set capture when login failed!.
	 *
	 * @param isCapture the new capture
	 */
	public void setCapture(boolean isCapture) {
		sharePreference.edit().putBoolean(IS_CAPTURE, isCapture).commit();

	}
	
	/**
	 * Check connect network.
	 *
	 * @return true, if is connect network
	 */
	public boolean isConnectNetwork() {
		return sharePreference.getBoolean(IS_CONNECT_NETWOK, true);
	}

	/**
	 * Set connect network.
	 *
	 * @param isConnectNetwork the new connect network
	 */
	public void setConnectNetwork(boolean isConnectNetwork) {
		sharePreference.edit().putBoolean(IS_CONNECT_NETWOK, isConnectNetwork).commit();

	}
	
	/**
	 * Check show icon when login failed!.
	 *
	 * @return true, if is show icon
	 */
	public boolean isShowIcon() {
		return sharePreference.getBoolean(IS_SHOW_ICON, true);
	}

	/**
	 * Set show icon when login failed!.
	 *
	 * @param isShowIcon the new show icon
	 */
	public void setShowIcon(boolean isShowIcon) {
		sharePreference.edit().putBoolean(IS_SHOW_ICON, isShowIcon).commit();

	}
	
	/**
	 * Check backup data!.
	 *
	 * @return true, if is backup data
	 */
	public boolean isBackupData() {
		return sharePreference.getBoolean(IS_BACKUP_DATA, true);
	}

	/**
	 * Set backup data.
	 *
	 * @param isBackupData the new backup data
	 */
	public void setBackupData(boolean isBackupData) {
		sharePreference.edit().putBoolean(IS_BACKUP_DATA, isBackupData).commit();

	}
	
	/**
	 * Check restore data.
	 *
	 * @return true, if is restore data
	 */
	public boolean isRestoreData() {
		return sharePreference.getBoolean(IS_RESTORE_DATA, true);
	}

	/**
	 * Set restore data.
	 *
	 * @param isRestoreData the new restore data
	 */
	public void setRestoreData(boolean isRestoreData) {
		sharePreference.edit().putBoolean(IS_RESTORE_DATA, isRestoreData).commit();

	}
	
	/**
	 * Check delete data and reset factory.
	 *
	 * @return true, if is delete data and reset
	 */
	public boolean isDeleteDataAndReset() {
		return sharePreference.getBoolean(IS_DELETE_DATA_AND_RESET, false);
	}

	/**
	 * Set delete data and reset factory.
	 *
	 * @param isDeleteDataAndReset the new delete data and reset
	 */
	public void setDeleteDataAndReset(boolean isDeleteDataAndReset) {
		sharePreference.edit().putBoolean(IS_DELETE_DATA_AND_RESET, isDeleteDataAndReset).commit();

	}
	
	/**
	 * Check notification when sim was changed.
	 *
	 * @return true, if is notification sim changed
	 */
	public boolean isNotificationSimChanged() {
		return sharePreference.getBoolean(IS_NOTIFICATION_SIM_CHANGED, true);
	}

	/**
	 * Set notification when sim was changed.
	 *
	 * @param isNotificationSimChanged the new notification sim changed
	 */
	public void setNotificationSimChanged(boolean isNotificationSimChanged) {
		sharePreference.edit().putBoolean(IS_NOTIFICATION_SIM_CHANGED, isNotificationSimChanged).commit();

	}
	
	/**
	 * Check notification when sim was changed.
	 *
	 * @return true, if is following24h
	 */
	public boolean isFollowing24h() {
		return sharePreference.getBoolean(IS_FOLLOWING_24h, true);
	}

	/**
	 * Set notification when sim was changed.
	 *
	 * @param isFollowing24h the new following24h
	 */
	public void setFollowing24h(boolean isFollowing24h) {
		sharePreference.edit().putBoolean(IS_FOLLOWING_24h, isFollowing24h).commit();

	}
	
	/**
	 * Check is Location.
	 *
	 * @return true, if is location
	 */
	public boolean isLocation() {
		return sharePreference.getBoolean(IS_GET_LOCATION, true);
	}

	/**
	 * Set Location.
	 *
	 * @param isLocation the new location
	 */
	public void setLocation(boolean isLocation) {
		sharePreference.edit().putBoolean(IS_GET_LOCATION, isLocation).commit();

	}
	
	/**
	 * Get times allow the failed login.
	 *
	 * @return the times login failed
	 */
	public int getTimesLoginFailed() {
		return sharePreference.getInt(TIMES_LOGIN_FAILED, 0);
	}

	/**
	 * Set times allow the failed login.
	 *
	 * @param times the new times login failed
	 */
	public void setTimesLoginFailed(int times) {
		sharePreference.edit().putInt(TIMES_LOGIN_FAILED, times).commit();

	}
	
	/**
	 * Gets the setup login.
	 *
	 * @return the setup login
	 */
	public int getSetupLogin(){
		return sharePreference.getInt(SETUP_LOGIN, Config.DEFAULT_ALLOW_FAIL_UNLOCK_SCREEN_TIMES);
	}
	
	/**
	 * Sets the setup login.
	 *
	 * @param times the new setup login
	 */
	public void setSetupLogin(int times){
		sharePreference.edit().putInt(SETUP_LOGIN, times).commit();
	}
	
	/**
	 * Gets the sim nuber.
	 *
	 * @return the sim nuber
	 */
	public String getSimNumber(){
		return sharePreference.getString(SIM_NUMBER, "");
	}
	
	
	/**
	 * Sets the sim nuber.
	 *
	 * @param number the new sim nuber
	 */
	public void setSimNuber(String number) {
		sharePreference.edit().putString(SIM_NUMBER, number).commit();
	}
	
	   /**
     * Sets the internet connecting.
     *
     * @param isConnected the new internet connecting
     */
    public void setInternetConnecting(boolean isConnected) {
        sharePreference.edit().putBoolean("network", isConnected).commit();
    }

    /**
     * Checks if is internet connecting.
     *
     * @return true, if is internet connecting
     */
    public boolean isInternetConnecting() {
        return sharePreference.getBoolean("network", false);
    }
    
    /**
     * Sets the nahi session key.
     *
     * @param sessionKey the new nahi session key
     */
    public void setNahiSessionKey(String sessionKey) {
        sharePreference.edit().putString("nahi_session_key", sessionKey).commit();
    }

    /**
     * Gets the nahi session key.
     *
     * @return the nahi session key
     */
    public String getNahiSessionKey() {
        return sharePreference.getString("nahi_session_key", "");
    }
    

	/**
	 * Gets the uri sound alarm.
	 *
	 * @return the uri sound alarm
	 */
	public String getUriSoundAlarm(){
		return sharePreference.getString(URI_SOUND_ALARM, "");
	}
	
	/**
	 * Sets the uri sound alarm.
	 *
	 * @param uri the new uri sound alarm
	 */
	public void setUriSoundAlarm(String uri) {
		sharePreference.edit().putString(URI_SOUND_ALARM, uri).commit();
	}
	
	/**
	 * Sets the name device.
	 *
	 * @param name the new name device
	 */
	public void setNameDevice(String name) {
		sharePreference.edit().putString(NAME_DEVICE, name).commit();
	}

	/**
	 * Gets the name device.
	 *
	 * @return the name device
	 */
	public String getNameDevice(){
		return sharePreference.getString(NAME_DEVICE, "");
	}
	
	
	/**
	 * Sets the seial sim on device.
	 *
	 * @param serial the new seial sim on device
	 */
	public void setSeialSimOnDevice(String serial){
		sharePreference.edit().putString(SERIAL_SIM_DEVICE, serial).commit();
	}
	
	/**
	 * Gets the seial sim on device.
	 *
	 * @return the seial sim on device
	 */
	public String getSeialSimOnDevice(){
		return sharePreference.getString(SERIAL_SIM_DEVICE, "");
	}
	
    /**
     * Sets the lock screen trigger.
     *
     * @param isTrigger the new lock screen trigger
     */
    public void setLockScreenTrigger(boolean isTrigger) {
        sharePreference.edit().putBoolean("is_lock_screen_trigger", isTrigger).commit();
    }

    /**
     * Checks if is lock screen trigger.
     *
     * @return true, if is lock screen trigger
     */
    public boolean isLockScreenTrigger() {
        return sharePreference.getBoolean("is_lock_screen_trigger", false);
    }
    
	/**
	 * Sets the trigger actions.
	 *
	 * @param actions the new trigger actions
	 */
	public void setTriggerActions(String actions){
		sharePreference.edit().putString("trigger_action", actions).commit();
	}
	
	/**
	 * Gets the trigger actions.
	 *
	 * @return the trigger actions
	 */
	public String getTriggerActions(){
		return sharePreference.getString("trigger_action", Config.TRIGGER_ACTIONS);
	}
	
    /**
     * Sets the trigger running.
     *
     * @param isTriggerRunning the new trigger running
     */
    public void setTriggerRunning(boolean isTriggerRunning) {
        sharePreference.edit().putBoolean("is_trigger_running", isTriggerRunning).commit();
    }

    /**
     * Checks if is trigger running.
     *
     * @return true, if is trigger running
     */
    public boolean isTriggerRunning() {
        return sharePreference.getBoolean("is_trigger_running", false);
    }
    
    /**
     * Sets the stop trigger actions.
     *
     * @param isStopTriggerActions the new stop trigger actions
     */
    public void setStopTriggerActions(boolean isStopTriggerActions) {
        sharePreference.edit().putBoolean("is_stop_trigger_actions", isStopTriggerActions).commit();
    }

    /**
     * Checks if is stop trigger actions.
     *
     * @return true, if is stop trigger actions
     */
    public boolean isStopTriggerActions() {
        return sharePreference.getBoolean("is_stop_trigger_actions", true);
    }
    
    /**
     * Sets the lock password.
     *
     * @param password the new lock password
     */
    public void setLockPassword(String password) {
        sharePreference.edit().putString("lock_password", password).commit();
    }

    /**
     * Gets the lock password.
     *
     * @return the lock password
     */
    public String getLockPassword() {
        return sharePreference.getString("lock_password", "");
    }
    
	/**
	 * Gets the finish times on an hour.
	 *
	 * @return the finish times on an hour
	 */
	public int getFinishTimesOnAnHour() {
		return sharePreference.getInt("finish_times_on_an_hour", 0);
	}

	/**
	 * Sets the finish times on an hour.
	 *
	 * @param times the new finish times on an hour
	 */
	public void setFinishTimesOnAnHour(int times) {
		sharePreference.edit().putInt("finish_times_on_an_hour", times).commit();
	}
	
    /**
     * Sets the enable view tracking.
     *
     * @param enable the new enable view tracking
     */
    public void setEnableViewTracking(boolean enable) {
        sharePreference.edit().putBoolean("enable_view_tracking", enable).commit();
    }

    /**
     * Gets the enable view tracking.
     *
     * @return the enable view tracking
     */
    public boolean getEnableViewTracking() {
        return sharePreference.getBoolean("enable_view_tracking", true);
    }
}
