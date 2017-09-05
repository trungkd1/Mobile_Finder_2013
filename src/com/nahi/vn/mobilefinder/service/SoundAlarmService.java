package com.nahi.vn.mobilefinder.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.handler.AlertSound;
import com.nahi.vn.mobilefinder.util.SaveData;


// TODO: Auto-generated Javadoc
/**
 * The Class SoundAlarmService.
 */
public class SoundAlarmService extends Service {
	
	/** The context. */
	private Context context;
	
	/** The sound alarm. */
	public static SoundAlarmService soundAlarm;
	
	/** The mp. */
	private MediaPlayer mp;
	
	/** The raw id. */
	private int rawId = R.raw.police;
	
	/** The alert sound. */
	private AlertSound alertSound;
	
	/** The current volumn. */
	private int currentVolumn;

	/** The aud mgr. */
	private AudioManager audMgr;	
	
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		context = getApplicationContext();
		alertSound = new AlertSound(context);
		audMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		currentVolumn = audMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		mp = new MediaPlayer();
		preparePlay(false);
		super.onCreate();
	}	
	
	/**
	 * Prepare play.
	 *
	 * @param isPlayDefault the is play default
	 */
	private void preparePlay(boolean isPlayDefault){
		try{
			if(isPlayDefault){
				//Default sound
				AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawId);
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			} else {
				//Check for using the default or selected sound
				if(!TextUtils.isEmpty(SaveData.getInstance(context).getUriSoundAlarm())){
					//Selected sound
					mp.setDataSource(SaveData.getInstance(context).getUriSoundAlarm());
				} else {
					//Default sound
					AssetFileDescriptor afd = context.getResources().openRawResourceFd(rawId);
					mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				}
			}
			mp.setLooping(true);
			mp.prepare();
		} catch(Exception ex){
			mp.reset();
			preparePlay(true);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startPlay();
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * Start play.
	 */
	private void startPlay() {
		try{
			alertSound.setMaxVolume();
			if(!mp.isPlaying()){
				mp.start();
			}
		} catch(Exception ex){
			mp.reset();
			preparePlay(true);
			if(!mp.isPlaying()){
				mp.start();
			}
		}
		//Send sms, email, nahi message to inform start alert sound here
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(mp != null){
			mp.stop();
			mp.release();
		}
		//Return the normal volume
		if(audMgr != null){
			audMgr.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_PLAY_SOUND);
		}
		//Send sms, email, nahi message to inform stop alert sound here
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
