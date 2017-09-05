package com.nahi.vn.mobilefinder.handler;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin.TriggerAction;
import com.nahi.vn.mobilefinder.broadcast.LockTriggerReceiver;
import com.nahi.vn.mobilefinder.entity.ContentSender;
import com.nahi.vn.mobilefinder.entity.ContentSender.ApiType;
import com.nahi.vn.mobilefinder.entity.LocationObj;
import com.nahi.vn.mobilefinder.location.ILastLocationFinder;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.Log;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.worker.ContentSenderWorker;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationHandle.
 */
public class LocationHandle {

	/** The context. */
	private Context context;

	/** The loc manager. */
	public static LocationManager locManager;

	/** The loc listener. */
	public LocationListener locationListener;
	
	/** The tracking listener. */
	public LocationListener trackingListener;
	
	/** The is tracking. */
	private boolean isTracking = false;
	
	/** The is current internet connecting. */
	private boolean isCurrentInternetConnecting = false;

	/** The Constant WAITING_START_NETWORK. */
	private final static int WAITING_START_NETWORK = 111;
	
	/** The is get location ok. */
	private boolean isGetLocationOk;
	
	/** The timeout counter. */
	private Counter timeoutCounter;
	
	/** The Constant CHECK_REQUEST_LOCATION_TIMEOUT. */
	private final static int CHECK_REQUEST_LOCATION_TIMEOUT = 60 * 1000;
	
	/** The Constant CHECK_REQUEST_LOCATION_TIMEOUT_INTERVAL. */
	private final static int CHECK_REQUEST_LOCATION_TIMEOUT_INTERVAL = 20 * 1000;
	
	/** The handler. */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case WAITING_START_NETWORK:
				if(isTracking){
					locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							Config.TIME_TRACKING, 0, trackingListener);
					locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							Config.TIME_TRACKING, 0, trackingListener);
				} 
				else{
					locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							Config.TIME_TRACKING, 0, locationListener);
					locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							Config.TIME_TRACKING, 0, locationListener);
				}
				break;

			default:
				break;
			}
		}
		
	};
	
	/**
	 * Instantiates a new location handle.
	 *
	 * @param context the context
	 */
	public LocationHandle(Context context) {
		this.context = context;
		locManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		trackingListener = new MyTrackingListener();
	}

	/**
	 * Request location.
	 *
	 * @param isTracking the is tracking
	 */
	public void requestLocation(boolean isTracking) {
		this.isTracking = isTracking;
		this.isGetLocationOk = false;
		this.isCurrentInternetConnecting = SaveData.getInstance(context).isInternetConnecting();
		this.checkRequestLocationTimeOut();
		if(AppUtil.hasGPSDevice(context)){
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					onConnect();
					try {
						Thread.sleep(Config.TIME_START_NETWORK);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(WAITING_START_NETWORK);
				}
			});
			th.start();
		} else {
			Log.e("GPS provider is not available");
		}
	}

	/**
	 * Check request location time out.
	 */
	private void checkRequestLocationTimeOut(){
		timeoutCounter = new Counter(CHECK_REQUEST_LOCATION_TIMEOUT, CHECK_REQUEST_LOCATION_TIMEOUT_INTERVAL);
		timeoutCounter.start();
	}
	
	/**
	 * The Class Counter.
	 */
	public class Counter extends CountDownTimer {

		/**
		 * Instantiates a new counter.
		 *
		 * @param millisInFuture the millis in future
		 * @param countDownInterval the count down interval
		 */
		public Counter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		/**
		 * On finish.
		 */
		@Override
		public void onFinish() {
			if(!isGetLocationOk){
				ILastLocationFinder finder =  LocateDevice.getLastLocationFinder(context);
				Location loc = finder.getLastBestLocation(ILastLocationFinder.MAX_DISTANCE, System.currentTimeMillis()- ILastLocationFinder.MAX_TIME);
				if(loc != null){
					processLocateOrTracking(loc);
				} else {
					//Send the "lock trigger" broadcast
					if(SaveData.getInstance(context).isTriggerRunning()){
						Bundle bundle = new Bundle();
						bundle.putInt(LockTriggerReceiver.KEY_ACTION_DONE, TriggerAction.GET_LOCATION.value());
						AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_LOCK_TRIGGER, bundle);
					}
				}
			}
			if(timeoutCounter != null){
				timeoutCounter.cancel();
			}
		}

		/**
		 * On tick.
		 *
		 * @param millisUntilFinished the millis until finished
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			
		}
		
	}
	/**
	 * Stop send.
	 */
	public void stopSend() {
		if(this.isTracking){
			if (locManager != null && trackingListener != null) {
				locManager.removeUpdates(trackingListener);
			}
		}
		else{
			if (locManager != null && locationListener != null) {
				locManager.removeUpdates(locationListener);
			}
		}
	}

	/**
	 * Process locate or tracking.
	 *
	 * @param location the location
	 */
	private void processLocateOrTracking(Location location){
		DecimalFormat decim = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
		decim.applyPattern(Config.LOCATION_DECIMAL_FORMAT);
		
		final double lat = Double.parseDouble(decim.format(location
				.getLatitude()));
		final double lng = Double.parseDouble(decim.format(location
				.getLongitude()));
		this.isGetLocationOk = true;
		
		if (lat != 0 && lng != 0) {
			LocationObj obj = new LocationObj();
			String address = getAddressFromLatLng(lat, lng);
			obj.setLatitude(lat);
			obj.setLongitude(lng);
			obj.setAddress(address);
			obj.setTime(System.currentTimeMillis());
			obj.setStatus(Config.STATUS_IN_PROGRESS);
			onSaveDB(obj);
			
			Log.e(SaveData.getInstance(context).getFinishTimesOnAnHour() + "/" + "lat,lng :" + lat + "," + lng);
			
			if(!isTracking){
				//Send sms, email only is get location
				//Get current location
				sendLocationSMS(context.getString(R.string.get_location_msg, lat, lng, address));
				//Send lat lng of location
				sendLocationNAHI(lat + Config.SPLIT_PATTERN + lng + Config.SPLIT_PATTERN + address, "");
				
				//Send the "lock trigger" broadcast
				if(SaveData.getInstance(context).isTriggerRunning()){
					Bundle bundle = new Bundle();
					bundle.putInt(LockTriggerReceiver.KEY_ACTION_DONE, TriggerAction.GET_LOCATION.value());
					AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_LOCK_TRIGGER, bundle);
				}
				
				// Send the "receive content sender" broadcast
				AppUtil.sendBroadcast(context, Config.INTENT_RECEIVE_CONTENT_SENDER);
			}
		}
		
		onDisconnect();
		stopSend();
	}
	
	/**
	 * The listener interface for receiving myTracking events.
	 * The class that is interested in processing a myTracking
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyTrackingListener<code> method. When
	 * the myTracking event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyTrackingEvent
	 */
	public class MyTrackingListener implements LocationListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onLocationChanged(android.location
		 * .Location)
		 */
		/**
		 * On location changed.
		 *
		 * @param location the location
		 */
		@Override
		public void onLocationChanged(final Location location) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					processLocateOrTracking(location);
					return null;
				}
			}.execute();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onProviderDisabled(java.lang.String
		 * )
		 */
		/**
		 * On provider disabled.
		 *
		 * @param provider the provider
		 */
		@Override
		public void onProviderDisabled(String provider) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onProviderEnabled(java.lang.String)
		 */
		/**
		 * On provider enabled.
		 *
		 * @param provider the provider
		 */
		@Override
		public void onProviderEnabled(String provider) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onStatusChanged(java.lang.String,
		 * int, android.os.Bundle)
		 */
		/**
		 * On status changed.
		 *
		 * @param provider the provider
		 * @param status the status
		 * @param extras the extras
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}
	
	/**
	 * The listener interface for receiving myLocation events. The class that is
	 * interested in processing a myLocation event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addMyLocationListener<code> method. When
	 * the myLocation event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see MyLocationEvent
	 */
	public class MyLocationListener implements LocationListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onLocationChanged(android.location
		 * .Location)
		 */
		/**
		 * On location changed.
		 *
		 * @param location the location
		 */
		@Override
		public void onLocationChanged(final Location location) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					processLocateOrTracking(location);
					return null;
				}
			}.execute();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onProviderDisabled(java.lang.String
		 * )
		 */
		/**
		 * On provider disabled.
		 *
		 * @param provider the provider
		 */
		@Override
		public void onProviderDisabled(String provider) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onProviderEnabled(java.lang.String)
		 */
		/**
		 * On provider enabled.
		 *
		 * @param provider the provider
		 */
		@Override
		public void onProviderEnabled(String provider) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.location.LocationListener#onStatusChanged(java.lang.String,
		 * int, android.os.Bundle)
		 */
		/**
		 * On status changed.
		 *
		 * @param provider the provider
		 * @param status the status
		 * @param extras the extras
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}

	/**
	 * On connect.
	 */
	private void onConnect() {
		AppUtil.turnGps(context, true);
		if(!isCurrentInternetConnecting){
			AppUtil.turn3gConnection(context, true);			
			AppUtil.turnWifi(context, true);
		}
	}

	/**
	 * On disconnect.
	 */
	private void onDisconnect() {
		AppUtil.turnGps(context, false);
		if(!isCurrentInternetConnecting){
			AppUtil.turn3gConnection(context, false);
			AppUtil.turnWifi(context, false);			
		}
	}

	/**
	 * On save db.
	 * 
	 * @param obj
	 *            the obj
	 */
	private void onSaveDB(LocationObj obj) {
		if(App.getDB().insertLocation(obj) > 0){
			SaveData.getInstance(context).setFinishTimesOnAnHour(SaveData.getInstance(context).getFinishTimesOnAnHour() + 1);
		}
	}
	
	/**
	 * Send location sms.
	 *
	 * @param content the content
	 */
	private void sendLocationSMS(String content){
		if (AppUtil.hasSimCard(context)) {
			if((SaveData.getInstance(context).getPremium() == 1) && (SaveData.getInstance(context).getExpirePremium() > System.currentTimeMillis())){
				ContentSender sms = ContentSenderWorker
						.createSmsSender(content);
				ContentSenderWorker.insertContentSenders(sms);
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
		ContentSender sender = null;
		if(isTracking){
			sender = ContentSenderWorker.createNahiRequestSender(content, listFiles, ApiType.TRACKING_DEVICE);
		} else{
			sender = ContentSenderWorker.createNahiRequestSender(content, listFiles, ApiType.GET_LOCATION);
		}
		if(sender != null){
			ContentSenderWorker.insertContentSenders(sender);
		}
	}
	
	/**
	 * Gets the location json.
	 *
	 * @return the location json
	 */
	public static String getLocationJson(){
		ArrayList<LocationObj> objs = App.getDB().getAllLocations();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonData = new JSONObject();
		try {
			for(LocationObj obj : objs){
				JSONObject jsonObject = new JSONObject();
	        	jsonObject.accumulate("lat", obj.getLatitude());
	        	jsonObject.accumulate("long", obj.getLongitude());
	        	jsonObject.accumulate("address", obj.getAddress());
	        	jsonObject.accumulate("time", obj.getTime());
	        	jsonArray.put(jsonObject);
			}
			jsonData.accumulate(Config.DATA, jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonData.toString();
	}
	
	/**
	 * Gets the address from lat lng.
	 *
	 * @param lat the lat
	 * @param lng the lng
	 * @return the address from lat lng
	 */
	private String getAddressFromLatLng(double lat, double lng){
		String address = "";
		int count = 0;
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		List<Address> addresses = null;
		do {
			try {
				addresses = geocoder.getFromLocation(lat, lng, Config.MAX_GET_ADDRESS_RESULT);
				if(addresses.size() > 0){
					Address returnedAddress = addresses.get(0);
					StringBuilder strReturnedAddress = new StringBuilder();
					for(int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
						strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
					}
					address = strReturnedAddress.toString().trim();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				count++;
			}
		} while(TextUtils.isEmpty(address) && count < Config.MAX_GET_ADDRESS_TRY_TIMES);
		return address;
	}
	
}
