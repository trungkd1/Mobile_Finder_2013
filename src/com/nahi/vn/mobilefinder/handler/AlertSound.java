package com.nahi.vn.mobilefinder.handler;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.nahi.vn.mobilefinder.activity.NotifyActivity;
import com.nahi.vn.mobilefinder.activity.NotifyPassLockActivity;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.service.SoundAlarmService;
import com.nahi.vn.mobilefinder.util.AppUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AlertSound.
 */
public class AlertSound {

	/** The context. */
	private Context context;
	
	/**
	 * Instantiates a new alert sound.
	 *
	 * @param context the context
	 */
	public AlertSound(Context context){
		this.context = context;
	}
	
	/**
	 * Sets the max volume.
	 *
	 */
	public void setMaxVolume() {
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int streamType = AudioManager.STREAM_SYSTEM;
		mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		mgr.setStreamSolo(streamType, false);
		mgr.setStreamMute(streamType, false);
		mgr.setStreamVolume(AudioManager.STREAM_MUSIC,
				mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
	}

	/**
	 * Sets the min volume.
	 *
	 */
	public void setMinVolume() {
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int streamType = AudioManager.STREAM_SYSTEM;
		mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		mgr.setStreamSolo(streamType, true);
		mgr.setStreamMute(streamType, true);
	}
	
	/**
	 * Start alert.
	 */
	public void startAlert(){
		AppUtil.startService(context, SoundAlarmService.class);
	}
	
	/**
	 * Stop alert.
	 */
	public void stopAlert(){
		AppUtil.stopService(context, SoundAlarmService.class);
	}
	
	/**
	 * Start alert sound screen.
	 *
	 * @param action the action
	 */
	public void startAlertSoundScreen(){
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
		AppUtil.startActionActivity(context, c, Action.ALERT.toString());
	}
}
