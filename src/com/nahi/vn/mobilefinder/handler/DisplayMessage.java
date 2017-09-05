package com.nahi.vn.mobilefinder.handler;

import com.nahi.vn.mobilefinder.activity.NotifyActivity;
import com.nahi.vn.mobilefinder.activity.NotifyPassLockActivity;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.util.AppUtil;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayMessage.
 */
public class DisplayMessage {

	/** The context. */
	private Context context;
	
	/** The Constant DISPLAY_MESSAGE. */
	public final static String DISPLAY_MESSAGE = "display_message";
	
	/**
	 * Instantiates a new display message.
	 *
	 * @param context the context
	 */
	public DisplayMessage(Context context){
		this.context = context;
	}
	
	/**
	 * Display message.
	 *
	 * @param message the message
	 */
	public void displayMessage(String message){
        //Close dialogs and window shade
        Intent closeDialogs = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialogs);
        
        Class<?> c = NotifyPassLockActivity.class;
        KeyguardManager km = (KeyguardManager) context.getSystemService(
                Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            // Use the full screen activity for security.
        	c = NotifyActivity.class;
        }
        Bundle bundle = new Bundle();
		bundle.putString(DISPLAY_MESSAGE, message);
		AppUtil.startActionExtraActivity(context, c, Action.DISPLAY_MESSAGE.toString(), bundle);
	}
	
}
