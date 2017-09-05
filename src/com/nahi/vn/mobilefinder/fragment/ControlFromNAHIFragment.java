package com.nahi.vn.mobilefinder.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.handler.LocateDevice;
import com.nahi.vn.mobilefinder.handler.SyncSetting;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class ControlFromNAHIFragment.
 */
public class ControlFromNAHIFragment extends Fragment implements OnClickListener,OnCheckedChangeListener{

	/** Toggle Control from web server. */
	private ToggleButton toggleControlFromWebserver;
	
	/** Toggle Following device on 24h. */
	private ToggleButton toggleFollowingDeviceOn24h;
	
	/** Delete all data on device. */
	private ToggleButton toogleWipeData;
	
	
	private Button btnPremium;
	
	/** The et login nahi acc. */
	private EditText etLoginNahiAcc;
	
	private View view;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_protect_from_nahi, container, false);
		init(view);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		//Save setting to server
		new SyncSetting(getActivity()).syncSettingServer();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		if(!TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getEmail())){
			etLoginNahiAcc.setText(ProfileData.getInstance(getActivity()).getEmail());
		}
		onPremium();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.toggle_control_from_webserver:
			SaveData.getInstance(getActivity()).setControlFromNAHI(isChecked);
			setProtectByNahi(isChecked);
			toggleControlFromWebserver.setEnabled(true);
			break;

		case R.id.toggle_following_device_on_24h:
			SaveData.getInstance(getActivity()).setFollowing24h(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.monitor_all_time_panel), isChecked);
			toggleFollowingDeviceOn24h.setEnabled(true);
			//Turn on/off tracking device
			if(isChecked){
				LocateDevice handler = new LocateDevice(getActivity());
				handler.locateOrTrackingDevice(true);
				//Enable view tracking
				SaveData.getInstance(getActivity()).setEnableViewTracking(true);
			} else {
				//Disable view tracking
				SaveData.getInstance(getActivity()).setEnableViewTracking(false);
			}
			//Save setting to server
			new SyncSetting(getActivity()).syncSettingServer();
			break;
			
		case R.id.toggle_wipe_data:
			SaveData.getInstance(getActivity()).setDeleteDataAndReset(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.wipe_data_panel), isChecked);
			toogleWipeData.setEnabled(true);
			break;
		}
	}

	private void setProtectByNahi(boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			SaveData saveData=SaveData.getInstance(getActivity());
			AppUtil.enableDisableView(view.findViewById(R.id.nahi_protect_panel), saveData.isControlFromNAHI());
			toggleControlFromWebserver.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.monitor_all_time_panel), saveData.isFollowing24h());
			toggleFollowingDeviceOn24h.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.wipe_data_panel), saveData.isDeleteDataAndReset());
			toogleWipeData.setEnabled(true);
		} else {
			AppUtil.enableDisableView(view.findViewById(R.id.nahi_protect_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.monitor_all_time_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.wipe_data_panel), isChecked);
		}
		
		AppUtil.enableDisableView(view.findViewById(R.id.select_account_panel), isChecked);

		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_nahi:
			onLogin();
			break;
		
		case R.id.btn_premium:
			if(!(SaveData.getInstance(getActivity()).getPremium() == 1) && !(SaveData.getInstance(getActivity()).getExpirePremium() > System.currentTimeMillis())){
				onDialog();
			}
			break;
		}
	}
	
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {		
		toggleControlFromWebserver = (ToggleButton)view.findViewById(R.id.toggle_control_from_webserver);
		toggleFollowingDeviceOn24h = (ToggleButton)view.findViewById(R.id.toggle_following_device_on_24h);
		toogleWipeData = (ToggleButton)view.findViewById(R.id.toggle_wipe_data);
		etLoginNahiAcc = (EditText) view.findViewById(R.id.login_nahi_acc);
		btnPremium = (Button)view.findViewById(R.id.btn_premium);
		
		btnPremium.setOnClickListener(this);
		view.findViewById(R.id.btn_login_nahi).setOnClickListener(this);
		toggleControlFromWebserver.setOnCheckedChangeListener(this);
		toggleFollowingDeviceOn24h.setOnCheckedChangeListener(this);
		toogleWipeData.setOnCheckedChangeListener(this);
		
		toggleControlFromWebserver.setChecked(SaveData.getInstance(getActivity()).isControlFromNAHI());
		
		
		AppUtil.enableDisableView(view.findViewById(R.id.nahi_protect_panel),
				SaveData.getInstance(getActivity()).isControlFromNAHI());
		toggleControlFromWebserver.setEnabled(true);
		AppUtil.enableDisableView(view.findViewById(R.id.monitor_all_time_panel),
				SaveData.getInstance(getActivity()).isFollowing24h());
		toggleFollowingDeviceOn24h.setEnabled(true);
		AppUtil.enableDisableView(view.findViewById(R.id.wipe_data_panel),
				SaveData.getInstance(getActivity()).isDeleteDataAndReset());
		
		onPremium();
		if((SaveData.getInstance(getActivity()).getPremium() == 1) && (SaveData.getInstance(getActivity()).getExpirePremium() > System.currentTimeMillis())){
			// Acc Premium
			toogleWipeData.setEnabled(true);
		}else{
			// Acc normal
			toogleWipeData.setEnabled(false);
			toogleWipeData.setBackgroundResource(R.drawable.switch_empty);
			toggleFollowingDeviceOn24h.setBackgroundResource(R.drawable.switch_empty);
		}
		toogleWipeData.setChecked(SaveData.getInstance(getActivity()).isDeleteDataAndReset());
		toggleFollowingDeviceOn24h.setChecked(SaveData.getInstance(getActivity()).isFollowing24h());
	
	}
	/**
	 * On login.
	 */
	private void onLogin(){
		AppUtil.startFragment(getActivity(), R.id.fragment_container, LoginFragment.class,Config.FRAGMENT_CRL_FROM_NAHI);
	}
	
	
	private void onPremium(){
		long time_expire = SaveData.getInstance(getActivity()).getExpirePremium();
		if((SaveData.getInstance(getActivity()).getPremium() == 1) && (time_expire > System.currentTimeMillis())){
			//Acc premium
			btnPremium.setText(getString(R.string.expire)+" "+AppUtil.getTimelong2String(time_expire));
		}
	}
	
	private void onDialog(){
		AppUtil.getNotifyDialog(getActivity(), getString(R.string.premium_title), getString(R.string.premium_content), getString(R.string.buy), true, new DialogListener() {
			
			@Override
			public void cancelListener(Dialog dialog) {
				dialog.dismiss();
			}
			
			@Override
			public void acceptListener(Dialog dialog) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.URL_NAHI_PREMIUM));
				startActivity(browserIntent);
				dialog.dismiss();
			}
		}).show();
	}
}
