package com.nahi.vn.mobilefinder.handler;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nahi.vn.mobilefinder.activity.CaptureActivity;
import com.nahi.vn.mobilefinder.activity.CapturePassLockActivity;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.util.AppUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class CaptureImage.
 */
public class CaptureImage  {

	/** The context. */
	private Context context;
	
	/** The Constant IS_LOCK_RUNNING. */
	public final static String IS_LOCK_RUNNING = "is_lock_running";
	
	/**
	 * Instantiates a new capture image.
	 *
	 * @param context the context
	 */
	public CaptureImage(Context context){
		this.context = context;
	}
	
	/**
	 * Start activity capture.
	 *
	 * @param isLockRunning the is lock running
	 */
	public void startCapture(boolean isLockRunning){
        //Close dialogs and window shade
        Intent closeDialogs = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialogs);
        
        Class<?> c = CapturePassLockActivity.class;
        KeyguardManager km = (KeyguardManager) context.getSystemService(
                Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            // Use the full screen activity for security.
        	c = CaptureActivity.class;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_LOCK_RUNNING, isLockRunning);
		AppUtil.startActionExtraActivity(context, c, Action.CAPTURE_IMAGE.toString(), bundle);
	}

}
