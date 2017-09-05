package com.nahi.vn.mobilefinder.handler;

import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.entity.MobileLoginResult;
import com.nahi.vn.mobilefinder.entity.SessionKeyResult;
import com.nahi.vn.mobilefinder.fragment.LoginFragment.ListenerLogin;
import com.nahi.vn.mobilefinder.gcm.GcmHandler;
import com.nahi.vn.mobilefinder.gcm.GcmHandler.IGCM;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;
import com.nahi.vn.mobilefinder.worker.TimeoutCounter;
import com.nahi.vn.mobilefinder.worker.TimeoutCounter.TimeoutListener;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginHandle.
 */
public class LoginHandle implements HttpClientListener {

	/** The activity. */
	private FragmentActivity activity;
	
	/** The email. */
	private String email;
	
	/** The password. */
	private String password;
	
	/** The device id. */
	private String deviceId;
	
	/** The package name. */
	private String packageName;
	
	/** The listen. */
	private ListenerLogin listen;
	
	/** The progress dialog. */
	private ProgressDialog progressDialog;
	
	/** The timeout counter. */
	private TimeoutCounter timeoutCounter;
	
	/** The is login sucess. */
	private boolean isLoginSucess = false;
	
	/** The Constant CHECK_LOGIN_TIMEOUT. */
	private final static int CHECK_LOGIN_TIMEOUT = 60 * 1000;
	
	/** The Constant CHECK_LOGIN_TIMEOUT_INTERVAL. */
	private final static int CHECK_LOGIN_TIMEOUT_INTERVAL = 30 * 1000;
	
	/**
	 * Instantiates a new login handle.
	 *
	 * @param activity the activity
	 * @param listen the listen
	 */
	public LoginHandle(FragmentActivity activity,ListenerLogin listen){
		this.activity = activity;
		this.listen = listen;
		this.progressDialog = new ProgressDialog(this.activity);
		this.progressDialog.setTitle(this.activity.getString(R.string.login_title));
		this.progressDialog.setMessage(this.activity.getString(R.string.loading_msg));
		this.progressDialog.setCancelable(false);
		this.timeoutCounter = new TimeoutCounter(CHECK_LOGIN_TIMEOUT, CHECK_LOGIN_TIMEOUT_INTERVAL, new TimeoutListener() {
			
			@Override
			public void onFinish() {
				if(!isLoginSucess){
					progressDialog.dismiss();
					LoginHandle.this.listen.loginFailed();
				}
				timeoutCounter.cancel();
			}
		});
	}
	
	/**
	 * Request login.
	 *
	 * @param email the email
	 * @param password the password
	 */
	public void requestLogin(String email,String password){
		if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
			this.email = email;
			this.password = password;
			this.deviceId = AppUtil.getDeviceID() + email;
			this.packageName = activity.getPackageName();
			HttpClientHelper.getInstance().requestSessionKey();
			HttpClientHelper.getInstance().setListener(this);
			this.progressDialog.show();
			this.timeoutCounter.start();
		}else{
			AppUtil.getNotifyDialog(activity, activity.getString(R.string.login), activity.getString(R.string.intput_email_password), false, new DialogListener() {
				
				@Override
				public void cancelListener(Dialog dialog) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
				
				@Override
				public void acceptListener(Dialog dialog) {
					dialog.dismiss();
				}
				
			});
		}	
	}
	
	/* (non-Javadoc)
	 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onHttpResult(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, java.util.List)
	 */
	@Override
	public void onHttpResult(Methods methods, List<Object> data) {
		switch (methods) {
			case REQUEST_SESSION_KEY:
				if(data != null && data.size() > 0){
					SessionKeyResult rs = (SessionKeyResult) data.get(0);
					String sessionKey = rs.getSessionKey();
					HttpClientHelper.getInstance().mobileLogin(email, password, deviceId, sessionKey, packageName);
				}
				break;
			case MOBILE_LOGIN:
			
				if(data != null && data.size() > 0){
					final MobileLoginResult rs = (MobileLoginResult) data.get(0);
					ProfileData.getInstance(activity).setToken(rs.getToken());
					GcmHandler.getInstance(activity).registerGcm(rs.getToken());
					GcmHandler.getInstance(activity).setGcmCallBack(new IGCM() {
						
						@Override
						public void onRegisterFail() {
							progressDialog.dismiss();
							listen.loginFailed();
						}
						
						@Override
						public void onFinishNahiServerRegister(String regId) {
							isLoginSucess = true;
							progressDialog.dismiss();
							ProfileData.getInstance(activity).setGCMId(regId);
							listen.loginSuccess();
							ProfileData.getInstance(activity).setEmail(email);
						}
						
						@Override
						public void onFinishGoogleRegister(String regId) {
							progressDialog.dismiss();
						}

					});
				}else{
					progressDialog.dismiss();
					listen.loginFailed();
				}
				break;
			default:
				break;
		}
	}

	/* (non-Javadoc)
	 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onStartAPI(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods)
	 */
	@Override
	public void onStartAPI(Methods methods) {
		switch (methods) {
			case REQUEST_SESSION_KEY:

				break;
	
			default:
				break;
		}
	}

	/* (non-Javadoc)
	 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onCallAPIFail(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, int)
	 */
	@Override
	public void onCallAPIFail(Methods methods, int statusCode) {

	}

	/* (non-Javadoc)
	 * @see com.nahi.vn.mobilefinder.listener.HttpClientListener#onProgressUpdate(com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods, int)
	 */
	@Override
	public void onProgressUpdate(Methods methods, int progress) {
	}
	
}
