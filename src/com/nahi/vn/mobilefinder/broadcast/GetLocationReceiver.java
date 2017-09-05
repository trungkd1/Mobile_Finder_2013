package com.nahi.vn.mobilefinder.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nahi.vn.mobilefinder.handler.LocationHandle;
import com.nahi.vn.mobilefinder.util.Config;

public class GetLocationReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Config.INTENT_RECEIVE_LOCATION)){
    		LocationHandle handle = new LocationHandle(context);
    		handle.requestLocation(false);        		
        }
	}

}
