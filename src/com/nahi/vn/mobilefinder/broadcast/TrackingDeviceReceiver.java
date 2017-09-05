package com.nahi.vn.mobilefinder.broadcast;

import java.util.ArrayList;
import java.util.List;

import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.ApiType;
import com.nahi.vn.mobilefinder.handler.LocationHandle;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.Log;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.worker.ContentSenderWorker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// TODO: Auto-generated Javadoc
/**
 * The Class TrackingDeviceReceiver.
 */
public class TrackingDeviceReceiver extends BroadcastReceiver{

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Config.INTENT_RECEIVE_LOCATION)){
        	if(SaveData.getInstance(context).isFollowing24h()){
        		LocationHandle handle = new LocationHandle(context);
        		handle.requestLocation(true);        		
        	}
        } else if(intent.getAction().equals(Config.INTENT_SEND_TRACKING_LOCATION)){
       		//Send tracking locations to server
			if(SaveData.getInstance(context).isFollowing24h() && SaveData.getInstance(context).getFinishTimesOnAnHour() > 0){
				Log.e("send server");
				// Tracking device
				String pathFile = AppUtil.createFile(context.getPackageName(), Config.LOCATION_FILE, LocationHandle.getLocationJson());
				sendLocationNAHI("", pathFile);
				
				// Send the "receive content sender" broadcast
				AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_CONTENT_SENDER);
				
				// Reset finish tracking times on an hour
				SaveData.getInstance(context).setFinishTimesOnAnHour(0);
			}
        }
	}

	/**
	 * Send location nahi.
	 *
	 * @param content the content
	 * @param pathFile the path file
	 */
	private void sendLocationNAHI(String content, String pathFile){
		List<String> listFiles = new ArrayList<String>();
		listFiles.add(pathFile);
		// Save the backup contact content senders to db
		ContentSender sender = ContentSenderWorker.createNahiRequestSender(content, listFiles, ApiType.TRACKING_DEVICE);
		if(sender != null){
			ContentSenderWorker.insertContentSenders(sender);
		}
	}
}
