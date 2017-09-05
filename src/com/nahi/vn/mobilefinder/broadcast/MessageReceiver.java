package com.nahi.vn.mobilefinder.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.entity.ContactObj;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.CommandType;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReceiver.
 */
public class MessageReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras == null) {
			return;
		}

		Object[] pdus = (Object[]) extras.get("pdus");
		for (int i = 0; i < pdus.length; i++) {
			SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String body = SMessage.getMessageBody().toString().trim();
			String number = SMessage.getOriginatingAddress();
			
			//Just filter the sms that has phone number
			if (TextUtils.isEmpty(number)) {
				return;
			}
			
			//Checking the control from sms messages mode is turn on
			if(SaveData.getInstance(context).isControlFromSim()){
				//Filter for the phone number of sms sender in the filter remote number list
				ContactObj contact = App.getDB().getContactObjByNumber(number);
				if(contact != null){
					AnalysisCommand analysisCommand = new AnalysisCommand(context);
					if(analysisCommand.doAction(body, CommandType.SMS)){
						//if is an action, abort sms to the os 
						abortBroadcast();
					}
				}
			}
		}
	}

}
