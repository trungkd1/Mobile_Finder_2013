package com.nahi.vn.mobilefinder.handler;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;

import com.nahi.vn.mobilefinder.activity.NotifyActivity;
import com.nahi.vn.mobilefinder.activity.NotifyPassLockActivity;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.util.AppUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayIcon.
 */
public class DisplayIcon {

	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new display icon.
	 *
	 * @param context the context
	 */
	public DisplayIcon(Context context){
		this.context = context;
	}
	
	/**
	 * Display icon.
	 */
	public void displayIcon(){
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
		AppUtil.startActionActivity(context, c, Action.DISPLAY_ICON.toString());
	}
}
