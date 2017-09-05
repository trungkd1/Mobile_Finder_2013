package com.nahi.vn.mobilefinder.handler;

import com.nahi.vn.mobilefinder.util.AppUtil;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class WipeData.
 */
public class WipeData {
	
	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new wipe data.
	 *
	 * @param context the context
	 */
	public WipeData(Context context){
		this.context = context;
	}
	
	/**
	 * Wipe data.
	 */
	public void wipeData(){
		if(AppUtil.isAdminActive(context)){
			AppUtil.wipeData(context);
		}
	}
	
}
