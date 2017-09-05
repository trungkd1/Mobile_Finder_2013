package com.nahi.vn.mobilefinder.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.entity.CheckPremiumResponse;
import com.nahi.vn.mobilefinder.entity.PremiumResult;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashActivity.
 */
public class SplashActivity extends Activity implements OnClickListener, HttpClientListener {
	
	/** The Constant OPEN_PRIVACY_REQUEST. */
	public final static int OPEN_PRIVACY_REQUEST = 3333;
	
	/** The Constant FINISH_LOAD_SPASH_SCREEN_TIME. */
	public final static int FINISH_LOAD_SPASH_SCREEN_TIME = 1111;
	
	/** The handler. */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
				case FINISH_LOAD_SPASH_SCREEN_TIME:
					if (SaveData.getInstance(SplashActivity.this).isFirstUse()) {
						onPrivacyScreen();
					}else{
						onStartmainScreen();
					}
					break;
	
				default:
					break;
			}
		}
		
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		//Check internet connection
		AppUtil.checkInternetConnection(this);
		//Set schedule auto backup
		AppUtil.setScheduleAutoBackup(this);

		if(!TextUtils.isEmpty(ProfileData.getInstance(this).getToken()) && SaveData.getInstance(this).isInternetConnecting()){
			HttpClientHelper.getInstance().checkFinderPremium(ProfileData.getInstance(this).getToken());
			HttpClientHelper.getInstance().setListener(this);
		}
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {     
					Thread.sleep(Config.TIME_SPLASH_SCREEN);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(FINISH_LOAD_SPASH_SCREEN_TIME);
			}
		});
		th.start();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == OPEN_PRIVACY_REQUEST && resultCode == RESULT_OK){
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		return;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		SaveData.getInstance(this).setFirstUse(false);
		onStartmainScreen();
	}
	
	/**
	 * On startmain screen.
	 */
	void onStartmainScreen(){
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		finish();
	}
	
	/**
	 * On privacy screen.
	 */
	void onPrivacyScreen(){
		startActivityForResult(new Intent(SplashActivity.this, PrivacyActivity.class), OPEN_PRIVACY_REQUEST);
	}

	@Override
	public void onHttpResult(Methods methods, List<Object> data) {
		switch (methods) {
		case CHECK_FINDER_PREMIUM:
			if(data != null && data.size() > 0){
				if(data.get(0) instanceof CheckPremiumResponse){
					CheckPremiumResponse rs = (CheckPremiumResponse) data.get(0);
					PremiumResult premium = rs.getPremiumResult();
					if(!TextUtils.isEmpty(premium.getExpired())){
						SaveData.getInstance(this).setExpirePremium(AppUtil.getTimeString2long(premium.getExpired()));
						SaveData.getInstance(this).setPremium(premium.getPremium());
					}
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onStartAPI(Methods methods) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallAPIFail(Methods methods, int statusCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgressUpdate(Methods methods, int progress) {
		// TODO Auto-generated method stub
		
	}
}
