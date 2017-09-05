package com.nahi.vn.mobilefinder.broadcast;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.SenderType;
import com.nahi.vn.mobilefinder.handler.LocateDevice;
import com.nahi.vn.mobilefinder.handler.LockDevice;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.worker.ContentSenderWorker;


// TODO: Auto-generated Javadoc
/**
 * The Class ChangeSimReceiver.
 */
public class ChangeSimReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent == null){
			return;
		}
		
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			//Check has sim card after reboot 
			if(AppUtil.hasSimCard(context)){
				onChangeSim(context);
			}
			//Check if Finder lock screen is running, turn it on
			if(!TextUtils.isEmpty(SaveData.getInstance(context).getLockPassword())){
				new LockDevice(context).lockDevice(SaveData.getInstance(context).getLockPassword());
			}
			//Restart schedule auto backup
			AppUtil.setScheduleAutoBackup(context);
			//Restart tracking locations
			if(SaveData.getInstance(context).isFollowing24h()){
				LocateDevice handler = new LocateDevice(context);
				handler.locateOrTrackingDevice(true);
			}
		}
		
		if(intent.getAction().equals(Config.INTENT_RECEIVE_CONTENT_SENDER)){
			if(AppUtil.hasSimCard(context)){
				//Query from db
        		List<ContentSender> pendingSenderList = App.getDB().getPendingContentSenderByType(SenderType.SMS);
        		if(pendingSenderList.size() > 0){
        			//Execute the send content worker
        			new ContentSenderWorker(context, pendingSenderList).execute();
        		}
			}
		}
		
	}
	
	
	/**
	 * On change sim.
	 *
	 * @param context the context
	 */
	private void onChangeSim(Context context){
		String newSimSerial = AppUtil.getSerialNumber(context);
		if(!TextUtils.isEmpty(newSimSerial)){
			if(!SaveData.getInstance(context).getSeialSimOnDevice().equalsIgnoreCase(newSimSerial)
					&& SaveData.getInstance(context).isNotificationSimChanged()){
				SaveData.getInstance(context).setSeialSimOnDevice(newSimSerial);
				// Save the backup contact content senders to db
				ContentSender sms = ContentSenderWorker.createSmsSender(context.getString(R.string.device_report_change_sim, SaveData.getInstance(context).getNameDevice()));
				ContentSenderWorker.insertContentSenders(sms);
				// Send the "receive content sender" broadcast
				AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_CONTENT_SENDER);
			}
		}
	}
}