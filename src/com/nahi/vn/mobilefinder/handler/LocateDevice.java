/*
 * 
 */
package com.nahi.vn.mobilefinder.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.nahi.vn.mobilefinder.broadcast.GetLocationReceiver;
import com.nahi.vn.mobilefinder.broadcast.TrackingDeviceReceiver;
import com.nahi.vn.mobilefinder.location.GingerbreadLastLocationFinder;
import com.nahi.vn.mobilefinder.location.ILastLocationFinder;
import com.nahi.vn.mobilefinder.location.LegacyLastLocationFinder;
import com.nahi.vn.mobilefinder.util.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class LocateDevice.
 */
public class LocateDevice {
	
	/** The context. */
	private Context context;
	
	/** The Constant IS_TRACKING. */
	public final static String IS_TRACKING = "is_tracking";
	
	/**
	 * Instantiates a new locate device.
	 *
	 * @param context the context
	 */
	public LocateDevice(Context context) {
		this.context = context;
	}
	
	/**
	 * Locate or tracking device.
	 *
	 * @param isTracking the is tracking
	 * @param pendingTime the pending time
	 */
	public void locateOrTrackingDevice(boolean isTracking, long pendingTime){
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    if(isTracking){
	    	PendingIntent pi = this.getPendingIntent(TrackingDeviceReceiver.class, Config.INTENT_RECEIVE_LOCATION);
	    	//Tracking device 24/24
	    	am.setRepeating(AlarmManager.RTC_WAKEUP, 
	 	            System.currentTimeMillis() + pendingTime, 
	 	            Config.TIME_TRACKING, //each 1' or 10' to tracking location
	 	            pi);
	    	//Send tracking locations
	    	PendingIntent sendTrackingPi = this.getPendingIntent(TrackingDeviceReceiver.class, Config.INTENT_SEND_TRACKING_LOCATION);
	    	am.setRepeating(AlarmManager.RTC_WAKEUP, 
	 	            System.currentTimeMillis() + Config.TIME_TRACKING_SEND_SERVER, 
	 	            Config.TIME_TRACKING_SEND_SERVER, //each 10' or 1 hour to send tracking locations
	 	            sendTrackingPi);
	    }
	    else{
	    	PendingIntent pi = this.getPendingIntent(GetLocationReceiver.class, Config.INTENT_RECEIVE_LOCATION);
	    	am.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(), pi);		
	    }
	}
	
	/**
	 * Locate or tracking device.
	 *
	 * @param isTracking the is tracking
	 */
	public void locateOrTrackingDevice(boolean isTracking){
	    locateOrTrackingDevice(isTracking, 0);
	}
	
	/**
	 * Gets the pending intent.
	 *
	 * @param c the c
	 * @param action the action
	 * @return the pending intent
	 */
	private PendingIntent getPendingIntent(Class<?> c, String action){
	    PendingIntent pi = PendingIntent.getBroadcast(context, 0, getReceiveIntent(c, action), PendingIntent.FLAG_CANCEL_CURRENT);
	    return pi;
	}
	
	/**
	 * Gets the receive intent.
	 *
	 * @param c the c
	 * @param action the action
	 * @return the receive intent
	 */
	private Intent getReceiveIntent(Class<?> c, String action){
		Intent intent = new Intent(context, c);
	    intent.setAction(action);
	    return intent;
	}
	
	/**
	 * Stop tracking device24_24.
	 *
	 */
	public void stopTrackingDevice24_24(){
		if(isAlreadyTracking()){
			PendingIntent sender = this.getPendingIntent(TrackingDeviceReceiver.class, Config.INTENT_RECEIVE_LOCATION);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(sender);
		}
	}
	
	/**
	 * Checks if is already tracking.
	 *
	 * @return true, if is already tracking
	 */
	private boolean isAlreadyTracking(){
		return PendingIntent.getBroadcast(context, 0, getReceiveIntent(TrackingDeviceReceiver.class, Config.INTENT_RECEIVE_LOCATION), PendingIntent.FLAG_NO_CREATE) != null;
	}
	
	/**
	 * Gets the last location finder.
	 *
	 * @param context the context
	 * @return the last location finder
	 */
	public static ILastLocationFinder getLastLocationFinder(Context context) {
	    return ILastLocationFinder.SUPPORTS_GINGERBREAD ? new GingerbreadLastLocationFinder(context) : new LegacyLastLocationFinder(context);
	}
	
}
