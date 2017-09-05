package com.nahi.vn.mobilefinder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin;
import com.nahi.vn.mobilefinder.customui.CustomKeyboardView;
import com.nahi.vn.mobilefinder.customui.CustomKeyboardView.GoListener;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.handler.LockDevice;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;


// TODO: Auto-generated Javadoc
/**
 * The Class LockScreenActivity.
 */
public class LockScreenActivity extends Activity {

	/** The keyboard view. */
	private CustomKeyboardView keyboardView;
	
	/** The count input. */
	private int countInput = 0;
	
	/** The has window focus. */
	private boolean hasWindowFocus;
	
	/** The p intent. */
	private PendingIntent pIntent;
	
	/** The my broadcast. */
	private MyBroadcast myBroadcast;
	
	/** The am. */
	private AlarmManager am;
	
	/** The Constant STOP_LOCK_WORKER. */
	private final static int STOP_LOCK_WORKER = 9000;
	
	/** The Constant RECENT_APP_ACTIVITY. */
	private final static String RECENT_APP_ACTIVITY = "com.android.systemui.recent.RecentsActivity";
	
	/** The Constant TOGGLE_RECENTS_ACTIVITY. */
	private final static String TOGGLE_RECENTS_ACTIVITY = "com.android.systemui.recent.action.TOGGLE_RECENTS";
	
	/** The Constant ANDROID_SYSTEM_UI. */
	private final static String ANDROID_SYSTEM_UI = "com.android.systemui";
	
	/** The Constant RESTART_LOCK_SCREEN_INTENT. */
	private final static String RESTART_LOCK_SCREEN_INTENT = "RestartLockScreenIntent";
	
	/** The Constant LOCK_WORKER_SLEEP_TIME. */
	private final static long LOCK_WORKER_SLEEP_TIME = 1000L;
	
	/** The handler. */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case STOP_LOCK_WORKER:
				stopRepeatChecker();
				break;

			default:
				break;
			}
		}
		
	};
	
	/** The window closer runnable. */
	private Runnable windowCloserRunnable = new Runnable() {
	    @Override
	    public void run() {
	    	checkAndCloseRecentApps();
	    }
	};
	
	/**
	 * The Class MyBroadcast.
	 */
	private class MyBroadcast extends BroadcastReceiver{

		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			String str = ((ActivityManager.RunningTaskInfo)((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName();
			if(!hasWindowFocus && !str.equalsIgnoreCase(Config.LOCK_ACTIVITY_NAME) 
	    		  && !str.equalsIgnoreCase(Config.NOTIFY_ACTIVITY_NAME)
	    		  && !str.equalsIgnoreCase(Config.CAPTURE_ACTIVITY_NAME)
	    		  && !str.equalsIgnoreCase(Config.NOTIFY_PASS_LOCK_ACTIVITY_NAME)
	    		  && !str.equalsIgnoreCase(Config.CAPTURE_PASS_LOCK_ACTIVITY_NAME)
	    		  && !str.contains(Config.NAHI_KIDS_APP_PACKAGE_NAME)
	    		  && !str.equalsIgnoreCase(Config.NAHI_KIDS_MAIN_ACTIVITY_NAME)){
				//Clock recent apps
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1){
					if (!hasWindowFocus) {
						checkAndCloseRecentApps();
					}
				}
				//Re-start lock screen
				new LockDevice(getApplicationContext()).lockDevice(SaveData.getInstance(getApplicationContext()).getLockPassword());
			} 
		}
		
	}
	
	/**
	 * Start repeat checker.
	 */
	private void startRepeatChecker(){
    	am.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(), LOCK_WORKER_SLEEP_TIME, pIntent);
	}
	
	/**
	 * Stop repeat checker.
	 */
	private void stopRepeatChecker(){
		am.cancel(pIntent);
	}
	
	/**
	 * Check and close recent apps.
	 */
	private void checkAndCloseRecentApps(){
        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn != null && cn.getClassName().equals(RECENT_APP_ACTIVITY)) {
            toggleRecentApps();
        }
	}
	
	/**
	 * Toggle recent apps.
	 */
	private void toggleRecentApps() {
	    Intent closeRecents = new Intent(TOGGLE_RECENTS_ACTIVITY);
	    closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
	    ComponentName recents = new ComponentName(ANDROID_SYSTEM_UI, RECENT_APP_ACTIVITY);
	    closeRecents.setComponent(recents);
	    this.startActivity(closeRecents);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.activity_lock_screen);
		init();
		
		startRepeatChecker();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		//Stop lock checker
		stopRepeatChecker();
		//Cancel am
		am.cancel(pIntent);
		//Un-register broadcast
		unregisterReceiver(myBroadcast);
		//Disable home
		LockDevice.disableHome(getApplicationContext());
		//Return result ok
		setResult(RESULT_OK);
		super.onDestroy();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onWindowFocusChanged(boolean)
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		this.hasWindowFocus = hasFocus;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1){
			if (!hasFocus) {
		        handler.postDelayed(windowCloserRunnable, 0);
			}
		}
	}
	
	/**
	 * On click key.
	 *
	 * @param v the v
	 */
	public void onClickKey(View v){
		keyboardView.onClickKey(v.getTag().toString());
	}

	/**
	 * Inits the.
	 */
	public void init() {
		View emptyView = (View)findViewById(R.id.keyboard_empty_view);
		emptyView.setVisibility(View.VISIBLE);

		keyboardView = (CustomKeyboardView)findViewById(R.id.keyboard_view);
		keyboardView.setOnGoListener(new GoListener() {			
			@Override
			public void onGo(String text) {
				if(!TextUtils.isEmpty(SaveData.getInstance(LockScreenActivity.this).getLockPassword())){
					countInput++;
					if(countInput <= SaveData.getInstance(LockScreenActivity.this).getSetupLogin()){
						if(TextUtils.isEmpty(text) || !SaveData.getInstance(LockScreenActivity.this).getLockPassword().equals(text)){
							keyboardView.onIncorrect();
							if(countInput > SaveData.getInstance(LockScreenActivity.this).getSetupLogin()){
								countInput = 0;
								//Trigger here
								DeviceAdmin.onTrigger(LockScreenActivity.this);
							} 
						}
						else {
							if(SaveData.getInstance(LockScreenActivity.this).isTriggerRunning()){
								//Reset trigger actions
								SaveData.getInstance(LockScreenActivity.this).setTriggerActions(Config.TRIGGER_ACTIONS);
								//Set "stop lock trigger" 
								SaveData.getInstance(LockScreenActivity.this).setStopTriggerActions(true);
							}
							//Pass the password, turn off the Lock screen
							SaveData.getInstance(LockScreenActivity.this).setLockPassword("");
							countInput = 0;
							handler.sendEmptyMessage(STOP_LOCK_WORKER);
							finish();
						}
					}
					else{		
						countInput = 0;
						keyboardView.onIncorrect();		
						//Trigger here
						DeviceAdmin.onTrigger(LockScreenActivity.this);
					}
				}
				else{
					//Password is empty, press any key to dismiss
					handler.sendEmptyMessage(STOP_LOCK_WORKER);
					finish();
				}
			}

			@Override
			public void onIncorrect() {
				AppUtil.getNotifyDialog(LockScreenActivity.this, 
					getString(R.string.incorrect_password_title), 
					getString(R.string.incorrect_password_msg), false, 
					new DialogListener() {
						
						@Override
						public void cancelListener(Dialog dialog) {
							dialog.dismiss();
						}
						
						@Override
						public void acceptListener(Dialog dialog) {
							keyboardView.reset();
							dialog.dismiss();
						}
					}).show();
			}
		});
		checkIntent(getIntent());
		
		am = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
		pIntent = PendingIntent.getBroadcast( this, 0, new Intent(RESTART_LOCK_SCREEN_INTENT),
				 0 );
		myBroadcast = new MyBroadcast();
		registerReceiver(myBroadcast, new IntentFilter(RESTART_LOCK_SCREEN_INTENT));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		keyboardView.onClickKey("-1");
		return;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyLongPress(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_HOME
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_HOME
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onAttachedToWindow()
	 */
	@Override
	public void onAttachedToWindow() {
		if(Build.VERSION.SDK_INT < 14){
			this.getWindow().setType(
					WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		}
		super.onAttachedToWindow();
	}

	/**
	 * Check intent.
	 *
	 * @param intent the intent
	 */
	private void checkIntent(Intent intent){
		String action = intent.getAction();
		if(TextUtils.isEmpty(action)){
			//If don't include password, finish the lock screen
			return;
		}
		if(intent.getAction().equals(Action.LOCK_DEVICE.toString())){
			if(intent.getExtras().containsKey(LockDevice.NEW_PASS_CODE)){
				SaveData.getInstance(this).setLockPassword(intent.getExtras().getString(LockDevice.NEW_PASS_CODE));
			} 
		} 
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		checkIntent(intent);
		super.onNewIntent(intent);
	}

}
