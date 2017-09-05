package com.nahi.vn.mobilefinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.broadcast.DeviceAdmin.TriggerAction;
import com.nahi.vn.mobilefinder.broadcast.LockTriggerReceiver;
import com.nahi.vn.mobilefinder.handler.AlertSound;
import com.nahi.vn.mobilefinder.handler.AnalysisCommand.Action;
import com.nahi.vn.mobilefinder.handler.DisplayMessage;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class AlertSoundActivity.
 */
public class NotifyActivity extends Activity implements OnClickListener{

	/** The alert sound. */
	private AlertSound alertSound;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        
		setContentView(R.layout.layout_alert_sound);
		init();
	}
	
	/**
	 * Inits the.
	 */
	private void init(){
		findViewById(R.id.dismiss_alert).setOnClickListener(this);
		checkIntent(getIntent());
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dismiss_alert:
			//Finish activity and stop alert sound
			if(alertSound != null){
				alertSound.stopAlert();
			}
			finish();
			break;

		default:
			break;
		}
	}
	
	/**
	 * Check intent.
	 *
	 * @param intent the intent
	 */
	private void checkIntent(Intent intent){
		String action = intent.getAction();
		if(action == null){
			return;
		}
		if(intent.getAction().equals(Action.DISPLAY_MESSAGE.toString())){
			if(intent.getExtras().containsKey(DisplayMessage.DISPLAY_MESSAGE)){
				TextView tvDisplayMsg = (TextView) findViewById(R.id.text_message);
				tvDisplayMsg.setText(intent.getExtras().getString(DisplayMessage.DISPLAY_MESSAGE));
				//Visible message, button
				showView(R.id.layout_alert, true);
				showView(R.id.text_message, true);
				showView(R.id.dismiss_alert, true);
				//Change button text to OK
				Button btOK = (Button) findViewById(R.id.dismiss_alert);
				btOK.setText(getString(R.string.ok));
			}
		} else if(intent.getAction().equals(Action.DISPLAY_ICON.toString())){
			//Visible lock icon
			showView(R.id.layout_lock, true);
			//Send the "lock trigger" broadcast
			if(SaveData.getInstance(this).isTriggerRunning()){
				Bundle bundle = new Bundle();
				bundle.putInt(LockTriggerReceiver.KEY_ACTION_DONE, TriggerAction.DISPLAY_ICON.value());
				AppUtil.sendBroadcast(this, Config.INTENT_RECEIVE_LOCK_TRIGGER, bundle);
			}
		} else if(intent.getAction().equals(Action.ALERT.toString())){
			//start alert
			alertSound = new AlertSound(this);
			alertSound.startAlert();
			//Visible lock icon
			showView(R.id.layout_alert, true);
			showView(R.id.alert_image, true);
			showView(R.id.dismiss_alert, true);
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
	
	/**
	 * Show view.
	 *
	 * @param view the view
	 * @param isVisible the is visible
	 */
	private void showView(int view, boolean isVisible){
		if(isVisible){
			findViewById(view).setVisibility(View.VISIBLE);
		} else {
			findViewById(view).setVisibility(View.GONE);
		}
	}
}
