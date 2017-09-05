package com.nahi.vn.mobilefinder.broadcast;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nahi.vn.mobilefinder.entity.ContentSender.SenderType;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class DeviceAdmin.
 */
public class DeviceAdmin extends DeviceAdminReceiver {

	/**
	 * The Enum TriggerAction.
	 */
	public enum TriggerAction{
		
		/** The capture image. */
		CAPTURE_IMAGE(0),
		
		/** The display icon. */
		DISPLAY_ICON(1),
		
		/** The get location. */
		GET_LOCATION(2),
		
		/** The backup. */
		BACKUP(3),
		
		/** The wipe data. */
		WIPE_DATA(4);
		
		/** The value. */
		private int value;  
		 
    	/**
	     * Instantiates a new trigger action.
	     *
	     * @param value the value
	     */
	    private TriggerAction(int value) {  
	        this.value = value;  
	    }  
	  
    	/**
	     * From value.
	     *
	     * @param value the value
	     * @return the trigger action
	     */
	    public static TriggerAction fromValue(int value) {  
	        for (TriggerAction t: TriggerAction.values()) {  
	            if (t.value == value) {  
	                return t;  
	            }  
	        }  
	        return null;  
	    }  
    	
	    /**
    	 * Merge value to string.
    	 *
    	 * @param types the types
    	 * @return the string
    	 */
    	public static String mergeValueToString(SenderType...types){
    		String text = "";
    		if(types.length > 0){
    			for(int i = 0; i < types.length; i++){
    				text += types[i].value();
    				if(types.length > 1 && i < types.length - 1){
    					text += ",";
    				}
    			}
    		}
    		return text;
    	}
	  
    	/**
	     * Value.
	     *
	     * @return the int
	     */
	    public int value() {  
	        return value;  
	    }  
	}
	
	/* (non-Javadoc)
	 * @see android.app.admin.DeviceAdminReceiver#onPasswordFailed(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		super.onPasswordFailed(context, intent);
		//Checking the auto mode is turn on
		if(SaveData.getInstance(context).isAutoProtect()){
			int failed = SaveData.getInstance(context).getTimesLoginFailed();
			//If fail over the allowed times, do the trigger
			if(failed == SaveData.getInstance(context).getSetupLogin()){
				onTrigger(context);
			} else if(failed > SaveData.getInstance(context).getSetupLogin()){
				//Reset trigger actions
				SaveData.getInstance(context).setTriggerActions(Config.TRIGGER_ACTIONS);
				
				//Send the "lock trigger" broadcast
				if(SaveData.getInstance(context).isTriggerRunning()){
					Bundle bundle = new Bundle();
					AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_LOCK_TRIGGER, bundle);
				}
			} 
			SaveData.getInstance(context).setTimesLoginFailed(failed + 1);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.admin.DeviceAdminReceiver#onPasswordSucceeded(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		super.onPasswordSucceeded(context, intent);
		SaveData.getInstance(context).setTimesLoginFailed(0);
		if(SaveData.getInstance(context).isTriggerRunning()){
			//Reset trigger actions
			SaveData.getInstance(context).setTriggerActions(Config.TRIGGER_ACTIONS);
			//Set "stop lock trigger" 
			SaveData.getInstance(context).setStopTriggerActions(true);
		}
	}

	/**
	 * On trigger.
	 *
	 * @param context the context
	 */
	public static void onTrigger(Context context){
		//Set "start lock trigger" 
		SaveData.getInstance(context).setStopTriggerActions(false);
		
		SaveData.getInstance(context).setTriggerRunning(true);
		AnalysisCommand analysisCommand = new AnalysisCommand(context);
		//Do capture
		if(SaveData.getInstance(context).isCapture()){
			analysisCommand.doTargetAction(AnalysisCommand.Action.CAPTURE_IMAGE, Boolean.toString(true));
			return;
		}
		// do display icon
		if(SaveData.getInstance(context).isShowIcon()){
			analysisCommand.doTargetAction(AnalysisCommand.Action.DISPLAY_ICON);
			return;
		}
		//Do location
		if(SaveData.getInstance(context).isLocation()){
			analysisCommand.doTargetAction(AnalysisCommand.Action.GET_LOCATION);
			return;
		}
		//Do back up
		if(SaveData.getInstance(context).isBackupData()){
			analysisCommand.doTargetAction(AnalysisCommand.Action.BACKUP);
			return;
		}
		//Do wipe data
		if(SaveData.getInstance(context).isDeleteDataAndReset()){
			analysisCommand.doTargetAction(AnalysisCommand.Action.WIPE_DATA);
			return;
		}
	}
	
}
