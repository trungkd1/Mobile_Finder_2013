package com.nahi.vn.mobilefinder.service;

import java.util.ArrayList;

import com.nahi.vn.mobilefinder.handler.MailHandle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


// TODO: Auto-generated Javadoc
/**
 * The Class SendMailService.
 */
public class SendMailService extends Service {

	/** The Constant EXTRA_FILENAMES. */
	public static final String EXTRA_FILENAMES = "EXTRA_FILENAMES";
	
	/** The Constant EXTRA_BODY. */
	public static final String EXTRA_BODY = "EXTRA_BODY";
	
	/** The Constant EXTRA_ADDRESS. */
	public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try{
			if(intent.hasExtra(EXTRA_BODY) && intent.hasExtra(EXTRA_ADDRESS)){
				String body = intent.getStringExtra(EXTRA_BODY);
				String email = intent.getStringExtra(EXTRA_ADDRESS);
				ArrayList<String> listFileNames = intent.getStringArrayListExtra(EXTRA_FILENAMES);
				MailHandle.getInstance(getApplicationContext()).sendEmail(email, body, listFileNames);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
}
