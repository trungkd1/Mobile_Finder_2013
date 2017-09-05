/*
 * 
 */
package com.nahi.vn.mobilefinder.broadcast;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin.TriggerAction;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class LockTriggerReceiver.
 */
public class LockTriggerReceiver extends BroadcastReceiver{

	/** The Constant KEY_ACTION_DONE. */
	public final static String KEY_ACTION_DONE = "key_action_done";
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(Config.INTENT_RECEIVE_LOCK_TRIGGER)){
			boolean isStopTriggerActions = SaveData.getInstance(context).isStopTriggerActions();
			if(!isStopTriggerActions){
				String actionsText = SaveData.getInstance(context).getTriggerActions(); 
				//Update the action have just done to actions list
				if(intent.hasExtra(KEY_ACTION_DONE)){
					int doneActionValue = intent.getIntExtra(KEY_ACTION_DONE, -1);
					if(doneActionValue > -1 && doneActionValue <= 4){
						StringBuilder sb = new StringBuilder(actionsText);
						sb.setCharAt(doneActionValue, '1');
						actionsText = sb.toString();
						SaveData.getInstance(context).setTriggerActions(actionsText);
					}
				}
				//Get actions list that done or not
				HashMap<TriggerAction, Boolean> actions = new HashMap<DeviceAdmin.TriggerAction, Boolean>();
				for(int i = 0; i < actionsText.length(); i++){
					Character act =  actionsText.charAt(i);
					if(act == '0'){
						actions.put(TriggerAction.fromValue(i), false);
					} else if(act == '1'){
						actions.put(TriggerAction.fromValue(i), true);
					}
				}
				
				//Doing actions that not done yet
				AnalysisCommand analysisCommand = new AnalysisCommand(context);
				
				for(int i = 0; i < actions.size(); i++){
					TriggerAction key = TriggerAction.fromValue(i);
				    boolean val = actions.get(key);
					if(!val){
						switch (key) {
							case CAPTURE_IMAGE:
								if(SaveData.getInstance(context).isCapture() && SaveData.getInstance(context).isTriggerRunning()){
									analysisCommand.doTargetAction(AnalysisCommand.Action.CAPTURE_IMAGE, Boolean.toString(true));
									return;
								}
								break;
							case DISPLAY_ICON:
								if(SaveData.getInstance(context).isShowIcon() && SaveData.getInstance(context).isTriggerRunning()){
									analysisCommand.doTargetAction(AnalysisCommand.Action.DISPLAY_ICON);
									return;
								}
								break;
							case GET_LOCATION:
								if(SaveData.getInstance(context).isLocation() && SaveData.getInstance(context).isTriggerRunning()){
									analysisCommand.doTargetAction(AnalysisCommand.Action.GET_LOCATION);
									if(SaveData.getInstance(context).isBackupData() && SaveData.getInstance(context).isTriggerRunning()){
										analysisCommand.doTargetAction(AnalysisCommand.Action.BACKUP);
									}
									return;
								}
								break;
							case BACKUP:
								if(SaveData.getInstance(context).isBackupData() && SaveData.getInstance(context).isTriggerRunning()){
									analysisCommand.doTargetAction(AnalysisCommand.Action.BACKUP);
									return;
								}
								break;
							case WIPE_DATA:
								if(SaveData.getInstance(context).isDeleteDataAndReset() && SaveData.getInstance(context).isTriggerRunning()){
									analysisCommand.doTargetAction(AnalysisCommand.Action.WIPE_DATA);
									return;
								}
								break;
							default:
								break;
						}
					}
				}
			}
		}
		
	}

}
