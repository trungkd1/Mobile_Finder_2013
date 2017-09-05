package com.nahi.vn.mobilefinder.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.handler.LocateDevice;
import com.nahi.vn.mobilefinder.handler.LockDevice;
import com.nahi.vn.mobilefinder.handler.LoginHandle;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginFragment.
 */
public class LoginFragment extends Fragment implements OnClickListener {

	/** The edit_username. */
	private EditText edit_username;
	
	/** The edit_password. */
	private EditText edit_password;
	
	/** The is start clear default home. */
	private boolean isStartClearDefaultHome = false;
	
	/** The is go to clear default home. */
	private boolean isGoToClearDefaultHome = false;
	
	/** The is start select default launch app. */
	private boolean isStartSelectDefaultLaunchApp = false;
	
	/** The is go to select default launch app. */
	private boolean isGoToSelectDefaultLaunchApp = false;
	
	/** The is login success. */
	private boolean isLoginSuccess = false;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_login, container, false);
		init(view);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		if(isLoginSuccess){
			if(isStartClearDefaultHome && isGoToClearDefaultHome){
				//Select the default launch app
				//B Select the default launcher setting to use the Finder Lock screen
				LockDevice.selectDefaultLaunchApp(getActivity(), LockDevice.SELECT_DEFAULT_LAUNCH_APP_REQUEST);
				isStartClearDefaultHome = false;
				isGoToClearDefaultHome = false;
				isStartSelectDefaultLaunchApp = true;
			}
			if(isStartSelectDefaultLaunchApp && isGoToSelectDefaultLaunchApp){
				//Show dialog ask to set password/pattern from setting
				goSetPassPatternFromSetting();
			}
		}
	}
	
	/**
	 * Go set pass pattern from setting.
	 */
	private void goSetPassPatternFromSetting(){
		AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.password_protect), 
				getActivity().getString(R.string.do_you_set_password), true, 
				new DialogListener() {
					
					@Override
					public void cancelListener(Dialog dialog) {
						dialog.dismiss();
						if(isLoginSuccess){
							getActivity().onBackPressed();
						}
					}
					
					@Override
					public void acceptListener(Dialog dialog) {
						dialog.dismiss();
						AppUtil.startAction(getActivity(), android.provider.Settings.ACTION_SECURITY_SETTINGS);
						if(isLoginSuccess){
							getActivity().onBackPressed();
						}
					}
				}).show();
		isStartSelectDefaultLaunchApp = false;
		isGoToSelectDefaultLaunchApp = false;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		if(isStartClearDefaultHome){
			isGoToClearDefaultHome = true;
		}
		if(isStartSelectDefaultLaunchApp){
			isGoToSelectDefaultLaunchApp = true;
		}
	}
	
	/**
	 * Login additional.
	 */
	private void loginAdditions(){
		// If Finder is not the default launch app, prompt user to select
		if(!LockDevice.isMyLauncherDefault(getActivity())){
			final String otherHomeName = LockDevice.getOtherHomeLaunchAppName(getActivity());
			if(!TextUtils.isEmpty(otherHomeName)){
				//Check if have other default launch home, clear the default launch
				isStartClearDefaultHome = true;
				//A You must clear the default launcher setting to use the Finder Lock screen
				AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.active_launcher), 
						getActivity().getString(R.string.clear_default_launcher), false, 
						new DialogListener() {
							
							@Override
							public void cancelListener(Dialog dialog) {
								dialog.dismiss();
							}
							
							@Override
							public void acceptListener(Dialog dialog) {
								dialog.dismiss();
								AppUtil.showInstalledAppDetails(getActivity(), otherHomeName, LockDevice.SHOW_DETAIL_HOME_APP_SETTING_REQUEST);
							}
						}).show();
			} else {
				//Select the default launch app
				//B Select the default launcher setting to use the Finder Lock screen
				isStartSelectDefaultLaunchApp = true;
				AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.active_launcher), 
						getActivity().getString(R.string.select_default_launcher), false, 
						new DialogListener() {
							
							@Override
							public void cancelListener(Dialog dialog) {
								dialog.dismiss();
							}
							
							@Override
							public void acceptListener(Dialog dialog) {
								dialog.dismiss();
								LockDevice.selectDefaultLaunchApp(getActivity(), LockDevice.SELECT_DEFAULT_LAUNCH_APP_REQUEST);
							}
						}).show();
			}
		}else{
			// launcher NAHI always default 
			//Show dialog ask to set password/pattern from setting
			goSetPassPatternFromSetting();
		}
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(isLoginSuccess){
			switch (requestCode) {
				case Config.ACTION_DEVICE_ADMIN:
					//Call the login additions
					loginAdditions();
					break;
				case LockDevice.SHOW_DETAIL_HOME_APP_SETTING_REQUEST:
					//Select the default launch app
					isStartSelectDefaultLaunchApp = true;
					//B Select the default launcher setting to use the Finder Lock screen
					AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.active_launcher), 
							getActivity().getString(R.string.select_default_launcher), false, 
							new DialogListener() {
								
								@Override
								public void cancelListener(Dialog dialog) {
									dialog.dismiss();
								}
								
								@Override
								public void acceptListener(Dialog dialog) {
									dialog.dismiss();
									LockDevice.selectDefaultLaunchApp(getActivity(), LockDevice.SELECT_DEFAULT_LAUNCH_APP_REQUEST);
								}
							}).show();
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * The Interface ListenerLogin.
	 */
	public interface ListenerLogin{
		
		/**
		 * Login success.
		 */
		public void loginSuccess();
		
		/**
		 * Login failed.
		 */
		public void loginFailed();
	}
	
	
	/** The handle. */
	private LoginHandle handle;
	
	/** The listen. */
	private ListenerLogin listen = new ListenerLogin() {
		

		@Override
		public void loginFailed() {
			isLoginSuccess = false;
			try {
				if(getActivity() != null){
					AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.login), 
							getActivity().getString(R.string.login_failed), false, 
							new DialogListener() {
								
								@Override
								public void cancelListener(Dialog dialog) {
									dialog.dismiss();
								}
								
								@Override
								public void acceptListener(Dialog dialog) {
									dialog.dismiss();
								}
							}).show();
				}
			} catch (Exception e) {
				Log.e("loginFailed error :" + e.toString());
			}
		}

		@Override
		public void loginSuccess() {
			//Run tracking device
			if(SaveData.getInstance(getActivity()).isFollowing24h()){
				LocateDevice handler = new LocateDevice(getActivity());
				handler.locateOrTrackingDevice(true);
			}
			//Active device admin
			isLoginSuccess = true;
			onActiveDeviceAdmin(getActivity());
		}
	};
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {
		view.findViewById(R.id.text_register_account).setOnClickListener(this);
		view.findViewById(R.id.text_forget_password).setOnClickListener(this);
		view.findViewById(R.id.btn_login).setOnClickListener(this);
		edit_username = (EditText)view.findViewById(R.id.edit_username);
		edit_password = (EditText)view.findViewById(R.id.edit_password);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			onLogin();
			AppUtil.hideSoftKeyboard(getActivity());
			break;
		case R.id.text_register_account:
			onRegister();
			break;

		case R.id.text_forget_password:
			onForgetPassword();
			break;
		}
	}

	/**
	 * On login.
	 */
	private void onLogin() {
		if(SaveData.getInstance(getActivity()).isInternetConnecting()){
			if(!TextUtils.isEmpty(edit_username.getText().toString()) && !TextUtils.isEmpty(edit_password.getText().toString())){
				handle = new LoginHandle(getActivity(), listen);
				handle.requestLogin(edit_username.getText().toString(), edit_password.getText().toString());
			} else {
				AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.require_username_password_title), 
						getActivity().getString(R.string.require_username_password_msg), false, 
						new DialogListener() {
							
							@Override
							public void cancelListener(Dialog dialog) {
								dialog.dismiss();
							}
							
							@Override
							public void acceptListener(Dialog dialog) {
								dialog.dismiss();
							}
						}).show();
			}
		} else {
			AppUtil.getNotifyDialog(getActivity(), getActivity().getString(R.string.no_connection_title), 
			getActivity().getString(R.string.no_connection_msg), false, 
			new DialogListener() {
				
				@Override
				public void cancelListener(Dialog dialog) {
					dialog.dismiss();
				}
				
				@Override
				public void acceptListener(Dialog dialog) {
					dialog.dismiss();
				}
			}).show();
		}
	}

	/**
	 * On register.
	 */
	private void onRegister() {
		String url = Config.URL_REGISTER_ACCOUNT;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	/**
	 * On forget password.
	 */
	private void onForgetPassword() {
		String url = Config.URL_FORGET_PASSWORD;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	
	/**
	 * On active device admin.
	 *
	 * @param context the context
	 */
	private void onActiveDeviceAdmin(Context context){
		if(context == null){
			return;
		}
		if( AppUtil.isAdminActive(context)){
			//if Addmindevice is actived then it will notifiaction
			// If Finder is not the default launch app, prompt user to select
			loginAdditions();
		}else{
			//if Addmindevice is unactQived then it will be started
			startActivityForResult(AppUtil.startActiveAdmin(context), Config.ACTION_DEVICE_ADMIN);
		}
	}
	
}
