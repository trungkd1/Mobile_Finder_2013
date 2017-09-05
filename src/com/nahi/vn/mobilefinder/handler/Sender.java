package com.nahi.vn.mobilefinder.handler;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.nahi.vn.mobilefinder.service.SendMailService;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public class Sender {

	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new message.
	 *
	 * @param context the context
	 */
	public Sender(Context context) {
		this.context = context;
	}

	/**
	 * On send sms.
	 *
	 * @param address the address
	 * @param body the body
	 */
	public void onSendSMS(String address, String body) {
		SmsManager smsManager =     SmsManager.getDefault();
		smsManager.sendTextMessage(address, null, body, null, null);
	}

	
	/**
	 * On send email.
	 *
	 * @param address the address
	 * @param body the body
	 * @param listFiles the list files
	 */
	public void onSendEmail(String address,String body, ArrayList<String> listFiles) {
		if(listFiles.size() > 0){
			Intent intent = new Intent(context, SendMailService.class);
			intent.putExtra(SendMailService.EXTRA_BODY, body);
			intent.putExtra(SendMailService.EXTRA_ADDRESS, address);
			intent.putStringArrayListExtra(SendMailService.EXTRA_FILENAMES,
					listFiles);
			context.startService(intent);
		}
	}

	
	/**
	 * On send to nahi server.
	 *
	 * @param address the address
	 * @param body the body
	 * @param listFiles the list files
	 */
	public void onSendToNAHIServer(String address,String body, ArrayList<String> listFiles){
		Intent intent = new Intent(context, SendMailService.class);
		intent.putExtra(SendMailService.EXTRA_BODY, body);
		intent.putExtra(SendMailService.EXTRA_ADDRESS, address);
		intent.putStringArrayListExtra(SendMailService.EXTRA_FILENAMES,
				listFiles);
		context.startService(intent);
        
	}

}
