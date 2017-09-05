/*
 * *  aris-vn.com
 *  NetworkUtil.java
 *
 *  Created by Thai Le.
 * 
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nahi.vn.mobilefinder.broadcast;


import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.SenderType;
import com.nahi.vn.mobilefinder.handler.BackupData;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.worker.ContentSenderWorker;

// TODO: Auto-generated Javadoc
/**
 * The Class NetworkUtil. This class is used to listen the network state change
 * and save it into shared_preference file
 * 
 * @author TamLe
 */
public class NetworkUtil extends BroadcastReceiver{

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
        	//Check internet connection
        	AppUtil.checkInternetConnection(context);
	        	
        	//Check if has internet, query pending sender from db and then send email and message to nahi server
        	if(SaveData.getInstance(context).isInternetConnecting()){
        		sendPendingContent(context, SenderType.EMAIL, SenderType.NAHI_MESSAGE);
        		
        		//Checking if has auto backup data, do backup contact, sms and call log
        		if(SaveData.getInstance(context).isAutoBackup()){
        			new BackupData(context).backupDataTask(true);
        		}
        	}
        } else if(intent.getAction().equals(Config.INTENT_RECEIVE_CONTENT_SENDER)){
    		if(SaveData.getInstance(context).isInternetConnecting()){
	    		//Catch and send a content sender 
	    		sendPendingContent(context, SenderType.NAHI_MESSAGE);
    		}
        } else if(intent.getAction().equals(Config.INTENT_RECEIVE_AUTO_BACKUP_SENDER)){
        	if(SaveData.getInstance(context).isInternetConnecting() && SaveData.getInstance(context).isAutoBackup()){
        		//Occur when receive auto backup broadcast
	    		sendPendingContent(context, SenderType.AUTO_BACKUP);
        	}
        } else if(intent.getAction().equals(Config.INTENT_SCHEDULE_AUTO_BACKUP)){
        	//Schedule to auto backup data, do backup contact, sms and call log
    		if(SaveData.getInstance(context).isInternetConnecting() && SaveData.getInstance(context).isAutoBackup()){
    			new BackupData(context).backupDataTask(true);
    		}
        }
    }
    
    /**
     * Send pending content.
     *
     * @param context the context
     * @param senderTypes the sender types
     */
    private void sendPendingContent(Context context, SenderType...senderTypes){
		//Query from db
		List<ContentSender> pendingSenderList = App.getDB().getPendingContentSenderByType(senderTypes);
		if(pendingSenderList.size() > 0){
			//Execute the send content worker
			new ContentSenderWorker(context, pendingSenderList).execute();
		}
    }
}
