package com.nahi.vn.mobilefinder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.activity.PrivacyActivity;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class MainFragment.
 */
public class MainFragment extends Fragment implements OnClickListener {

	/** The btn backup. */
	private View btnBackup;
	
	/** The btn security. */
	private View btnSecurity;
	
	/** The is config. */
	private boolean isConfig;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_main, container, false);
		init(view);
		
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//Check if has already filter numbers and has already login
		isConfig = App.getDB().countTotalContact() > 0 
				&& !TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getToken())
				&& !TextUtils.isEmpty(SaveData.getInstance(getActivity()).getNameDevice());
		if(isConfig){
			btnBackup.setBackgroundResource(R.drawable.btn_menu_orange);
			btnSecurity.setBackgroundResource(R.drawable.btn_menu_orange);
		} else {
			btnBackup.setBackgroundResource(R.drawable.btn_menu_orange_disable);
			btnSecurity.setBackgroundResource(R.drawable.btn_menu_orange_disable);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
			case R.id.btn_user_guide:
				onUderGuide();
				break;
				
			case R.id.btn_backup:
				//Check if has already filter numbers and has already login
				if(isConfig){
					onBackupAndRestore();
				} 
				break;
	
			case R.id.btn_config:
				onConfig();
				break;
	
			case R.id.btn_type_protect:
				//Check if has already filter numbers and has already login
				if(isConfig){
					onProtect();
				} 
				break;
	
			case R.id.btn_options:
				AppUtil.startFragment(getActivity(), R.id.fragment_container, MainOptionFragment.class, Config.FRAGMENT_MAIN_OPTION);
				break;
		}
	}
	
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {
		//Layout main
		btnBackup = view.findViewById(R.id.btn_backup); 
		btnSecurity = view.findViewById(R.id.btn_type_protect);
		btnBackup.setOnClickListener(this);
		btnSecurity.setOnClickListener(this);
		view.findViewById(R.id.btn_config).setOnClickListener(this);
		view.findViewById(R.id.btn_user_guide).setOnClickListener(this);
		view.findViewById(R.id.btn_options).setOnClickListener(this);
		setHasOptionsMenu(true);
		if (SaveData.getInstance(getActivity()).isFirstUse()) {
			SaveData.getInstance(getActivity()).setFirstUse(false);
			String simSerial = AppUtil.getSerialNumber(getActivity());
			if(!TextUtils.isEmpty(simSerial)){
				if(!SaveData.getInstance(getActivity()).getSeialSimOnDevice().equalsIgnoreCase(simSerial)){
					SaveData.getInstance(getActivity()).setSeialSimOnDevice(simSerial);
				}
			}
		}
	}

	/**
	 * Guide to use app.
	 */
	private void onUderGuide() {
		Intent intent = new Intent(getActivity(), PrivacyActivity.class);
		intent.setAction(Config.ACTION_MAIN);
		startActivity(intent);
	}

	/**
	 * Config system app.
	 */
	private void onConfig() {
		AppUtil.startFragment(getActivity(), R.id.fragment_container, ConfigFragment.class, Config.FRAGMENT_CONFIG);
	}

	/**
	 * Backup and restore data.
	 */
	private void onBackupAndRestore() {
		AppUtil.startFragment(getActivity(), R.id.fragment_container, BackupAndRestoreFragment.class, Config.FRAGMENT_BACKUP_RESTORE);
	}

	/**
	 * Select protect type.
	 */
	private void onProtect() {
		AppUtil.startFragment(getActivity(), R.id.fragment_container, ProtectFragment.class, Config.FRAGMENT_PROTECT);
	}

}
