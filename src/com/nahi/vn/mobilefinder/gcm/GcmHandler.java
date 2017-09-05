/*
 * 
 */
package com.nahi.vn.mobilefinder.gcm;


import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gcm.GCMRegistrar;
import com.nahi.vn.mobilefinder.entity.MobileProfileResult;
import com.nahi.vn.mobilefinder.entity.ResponseResult;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;


// TODO: Auto-generated Javadoc
/**
 * The Class GcmHandler.
 */
public class GcmHandler {

    // Google project id
    /** The Constant SENDER_ID. */
	public static final String SENDER_ID = "226635272431";
	
	/** The context. */
	private Context context;
	
	/** The instance. */
	private static GcmHandler instance;
	
	/** The gcm call back. */
	private IGCM gcmCallBack;
	
	/**
	 * The Interface IGCM.
	 */
	public interface IGCM{
		
		/**
		 * On finish google register.
		 *
		 * @param regId the reg id
		 */
		public void onFinishGoogleRegister(String regId);
		
		/**
		 * On finish nahi server register.
		 *
		 * @param regId the reg id
		 */
		public void onFinishNahiServerRegister(String regId);
		
		/**
		 * On register fail.
		 */
		public void onRegisterFail();
	}
	
	/**
	 * Instantiates a new gcm handler.
	 *
	 * @param context the context
	 */
	private GcmHandler(Context context) {
		this.context = context;
	}
	
	/**
	 * Gets the single instance of GcmHandler.
	 *
	 * @param context the context
	 * @return single instance of GcmHandler
	 */
	public static GcmHandler getInstance(Context context){
		if (instance == null) {
			instance = new GcmHandler(context);
		}
		return instance;
	}

	/**
	 * Gets the gcm call back.
	 *
	 * @return the gcm call back
	 */
	public IGCM getGcmCallBack() {
		return gcmCallBack;
	}

	/**
	 * Sets the gcm call back.
	 *
	 * @param gcmCallBack the new gcm call back
	 */
	public void setGcmCallBack(IGCM gcmCallBack) {
		this.gcmCallBack = gcmCallBack;
	}

	/**
	 * Register.
	 *
	 * @param nahiToken the nahi token
	 */
	public void registerGcm(final String nahiToken){
		// Check if Internet present
		if (!SaveData.getInstance(context).isInternetConnecting()) {
			// stop executing code by return
			return;
		}
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(context);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(context);
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(context);
		
		// Check if regid already presents
		if (TextUtils.isEmpty(regId)) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(context, SENDER_ID);
		} else {
			// if device is not already registered on GCM
			registerGcmNahiServer(nahiToken, regId);
		}		
	}
	
	/**
	 * Register.
	 *
	 * @param token the token
	 * @param regId the reg id
	 */
	public void registerGcmNahiServer(String token, final String regId) {
    	//Register gcm to Nahi server
        HttpClientHelper.getInstance().registerGcm(token, regId, SaveData.getInstance(context).getNameDevice());
        HttpClientHelper.getInstance().setListener(new HttpClientListener() {
			
			@Override
			public void onStartAPI(Methods methods) {
			}
			
			@Override
			public void onProgressUpdate(Methods methods, int progress) {
			}
			
			@Override
			public void onHttpResult(Methods methods, List<Object> data) {
				if(methods == Methods.REGISTER_GCM){
					if(data != null && data.size() > 0){
						if(data.get(0) instanceof ResponseResult){
							ResponseResult rs = (ResponseResult)data.get(0);
							if(rs.getStatus() == ResponseResult.STATUS_OK){
								GCMRegistrar.setRegisteredOnServer(context, true);
								if(gcmCallBack != null){
									gcmCallBack.onFinishNahiServerRegister(regId);
								}
							} else{
								if(gcmCallBack != null){
									gcmCallBack.onRegisterFail();
								}
							}
						} else{
							if(gcmCallBack != null){
								gcmCallBack.onRegisterFail();
							}
						}
					} else{
						if(gcmCallBack != null){
							gcmCallBack.onRegisterFail();
						}
					}
				} else if (methods == Methods.MOBILE_PROFILE){
					if(data != null && data.size() > 0){
						if(data.get(0) instanceof MobileProfileResult){
							MobileProfileResult rs = (MobileProfileResult) data.get(0);
							ProfileData.getInstance(context).setEmail(rs.getEmail());
							ProfileData.getInstance(context).setCellphone(rs.getCellPhone());
						}
					}
				}
			}
			
			@Override
			public void onCallAPIFail(Methods methods, int statusCode) {
				if(gcmCallBack != null){
					gcmCallBack.onRegisterFail();
				}
			}
		});
    }
	
	/**
	 * Gcm destroy.
	 */
	public void destroyGcm(){
		GCMRegistrar.onDestroy(context.getApplicationContext());
	}
}
