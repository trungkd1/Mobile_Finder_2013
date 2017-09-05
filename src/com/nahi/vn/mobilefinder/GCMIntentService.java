package com.nahi.vn.mobilefinder;

import java.text.DecimalFormat;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gcm.GCMBaseIntentService;
import com.nahi.vn.mobilefinder.gcm.GcmHandler;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.CommandType;
import com.nahi.vn.mobilefinder.handler.LocateDevice;
import com.nahi.vn.mobilefinder.location.ILastLocationFinder;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class GcmIntentService.
 */
public class GCMIntentService extends GCMBaseIntentService {

    /** The current lat. */
    private double currentLat = 0;

    /** The current lng. */
    private double currentLng = 0;
    
    /** The cmd id. */
    private String cmdId;
    
    /** The context. */
    private Context context;
    
    /**
     * Instantiates a new gcm intent service.
     */
    public GCMIntentService() {
        super(GcmHandler.SENDER_ID);
    }

    /**
     * Method called on device registered.
     *
     * @param context the context
     * @param registrationId the registration id
     */
    @Override
    protected void onRegistered(final Context context, String registrationId) {
    	//Return callback after finish google gcm register
    	if(GcmHandler.getInstance(context).getGcmCallBack() != null){
    		GcmHandler.getInstance(context).getGcmCallBack().onFinishGoogleRegister(registrationId);
    	}
    	//Register gcm to Nahi server
    	GcmHandler.getInstance(context).registerGcmNahiServer(ProfileData.getInstance(context).getToken(), registrationId);
    }

    /**
     * Method called on device un registred.
     *
     * @param context the context
     * @param registrationId the registration id
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    }

    /**
     * Method called on Receiving a new message.
     *
     * @param context the context
     * @param intent the intent
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
    	this.context = context;
    	//Checking the control from nahi server mode is turn on
    	if(SaveData.getInstance(context).isControlFromNAHI()){
            if(intent.getExtras() != null){
            	AnalysisCommand analysisCommand = new AnalysisCommand(context);
            	if(intent.hasExtra(Config.NAHI_GCM_COMMAND_CODE)
            			&& intent.hasExtra(Config.NAHI_GCM_COMMAND_MESSAGE)
            			&& intent.hasExtra(Config.NAHI_GCM_COMMAND_ID)){
            		String code = intent.getExtras().getString(Config.NAHI_GCM_COMMAND_CODE);
            		String message = intent.getExtras().getString(Config.NAHI_GCM_COMMAND_MESSAGE);
            		String cmdId = intent.getExtras().getString(Config.NAHI_GCM_COMMAND_ID);
            		this.cmdId = cmdId;
            		//If command have been done, send the status to inform server
        			if(analysisCommand.doAction(code, message, CommandType.GCM)){
        				startGetCurrentLocation();
        			}
            	} else if(intent.hasExtra(Config.NAHI_GCM_COMMAND_CODE)
            			&& intent.hasExtra(Config.NAHI_GCM_COMMAND_ID)){
            		String code = intent.getExtras().getString(Config.NAHI_GCM_COMMAND_CODE);
            		String cmdId = intent.getExtras().getString(Config.NAHI_GCM_COMMAND_ID);
            		this.cmdId = cmdId;
            		//If command have been done, send the status to inform server
            		if(analysisCommand.doAction(code, CommandType.GCM)){
            			startGetCurrentLocation();
            		}
            	}
            }
    	}
    }
    
    /**
     * Start get current location.
     */
    private void startGetCurrentLocation(){
    	new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				//Turn on GPS
				AppUtil.turnGps(context, true);
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(Config.TIME_START_NETWORK);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				ILastLocationFinder finder =  LocateDevice.getLastLocationFinder(context);
				Location loc = finder.getLastBestLocation(ILastLocationFinder.MAX_DISTANCE, System.currentTimeMillis()- ILastLocationFinder.MAX_TIME);
				if(loc != null){
					//Get last best location ok
					processLocate(loc);
				} else {
					//No last best location
					//Send update status command without lat, lng
					updateStatusCommand("");
				}
				//Stop get location service
				stopGetCurrentLocation();
			}
			
		}.execute();
    }
    
    /**
     * Stop get current location.
     */
    private void stopGetCurrentLocation(){
		//Turn off GPS
		AppUtil.turnGps(context, false);
    }
    
    /**
     * Process locate.
     *
     * @param location the location
     */
    private void processLocate(Location location){
		//Get lat, lng of current location
		DecimalFormat decim = (DecimalFormat) DecimalFormat.getInstance(Locale.ENGLISH);
		decim.applyPattern(Config.LOCATION_DECIMAL_FORMAT);
		currentLat = Double.parseDouble(decim.format(location
				.getLatitude()));
		currentLng = Double.parseDouble(decim.format(location
				.getLongitude()));
		if (currentLat != 0 && currentLng != 0) {
			//Send the status to inform server
			String data = "{\"lat\":\"" + currentLat + "\",\"long\":\"" + currentLng + "\"}";
			updateStatusCommand(data);
		}
    }
    
    /**
     * Update status command.
     *
     * @param data the data
     */
    private void updateStatusCommand(String data){
		//Send the status to inform server
		HttpClientHelper.getInstance().updateStatusCommand(ProfileData.getInstance(context).getToken(), cmdId, data);
    }
    
    /* (non-Javadoc)
     * @see com.google.android.gcm.GCMBaseIntentService#onDeletedMessages(android.content.Context, int)
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
    }

    /* (non-Javadoc)
     * @see com.google.android.gcm.GCMBaseIntentService#onError(android.content.Context, java.lang.String)
     */
    @Override
    public void onError(Context context, String errorId) {
    	//Return callback after fail to register google gcm
    	if(GcmHandler.getInstance(context).getGcmCallBack() != null){
    		GcmHandler.getInstance(context).getGcmCallBack().onRegisterFail();
    	}
    }

    /* (non-Javadoc)
     * @see com.google.android.gcm.GCMBaseIntentService#onRecoverableError(android.content.Context, java.lang.String)
     */
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        return super.onRecoverableError(context, errorId);
    }

}
