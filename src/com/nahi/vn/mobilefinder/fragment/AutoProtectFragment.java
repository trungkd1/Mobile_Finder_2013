package com.nahi.vn.mobilefinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class AutoProtectFragment.
 */
public class AutoProtectFragment extends Fragment implements OnCheckedChangeListener{

	/** Check auto protect. */
	private ToggleButton checkAutoProtect;
	
	/** Check capture. */
	private ToggleButton checkCapture;
	
	/** Check location. */
	private ToggleButton checkLocate;
	
	/** Check display  the unable to access icon. */
	private ToggleButton checkDisplayUnaccessIcon;
	
	/** Check backup data. */
	private ToggleButton checkBackupData;
	
	/** Check delete data. */
	private ToggleButton checkDeleteData ;
	
	/** Set times allow the failed login. */
	private EditText editTimesFailed;

	
	private View view;
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_auto_protect_setup, container, false);
		init(view);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.toggle_auto_protect:
			SaveData.getInstance(getActivity()).setAutoProtect(isChecked);
			setAutoUpdate(isChecked);
			checkAutoProtect.setEnabled(true);
			break;

		case R.id.toggle_capture:
			SaveData.getInstance(getActivity()).setCapture(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.capture_panel), isChecked);
			checkCapture.setEnabled(true);
			break;
			
		case R.id.toggle_locate:
			SaveData.getInstance(getActivity()).setLocation(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.getLocation_panel), isChecked);
			checkLocate.setEnabled(true);
			break;
		
		case R.id.toggle_display_icon:
			SaveData.getInstance(getActivity()).setShowIcon(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.displayIcon_panel), isChecked);
			checkDisplayUnaccessIcon.setEnabled(true);
			break;
			
		case R.id.toggle_backup_data:
			SaveData.getInstance(getActivity()).setBackupData(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.backup_panel), isChecked);	
			checkBackupData.setEnabled(true);
			break;
			
		case R.id.toggle_wipe_data:
			SaveData.getInstance(getActivity()).setDeleteDataAndReset(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.wipedata_panel), isChecked);
			checkDeleteData.setEnabled(true);
			break;
		}
	}

	
	@Override
	public void onResume() {
		super.onResume();
		editTimesFailed.clearFocus();
	}
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {		
		checkAutoProtect = (ToggleButton)view.findViewById(R.id.toggle_auto_protect);
		checkCapture = (ToggleButton)view.findViewById(R.id.toggle_capture);
		checkLocate = (ToggleButton)view.findViewById(R.id.toggle_locate);
		checkDisplayUnaccessIcon = (ToggleButton)view.findViewById(R.id.toggle_display_icon);
		checkBackupData = (ToggleButton)view.findViewById(R.id.toggle_backup_data);
		checkDeleteData = (ToggleButton)view.findViewById(R.id.toggle_wipe_data);
		editTimesFailed = (EditText)view.findViewById(R.id.edit_limit);
		
		checkAutoProtect.setChecked(SaveData.getInstance(getActivity()).isAutoProtect());
		checkCapture.setChecked(SaveData.getInstance(getActivity()).isCapture());
		checkLocate.setChecked(SaveData.getInstance(getActivity()).isLocation());
		checkDisplayUnaccessIcon.setChecked(SaveData.getInstance(getActivity()).isShowIcon());
		checkBackupData.setChecked(SaveData.getInstance(getActivity()).isBackupData());
		
		editTimesFailed.setText(""+SaveData.getInstance(getActivity()).getSetupLogin());
		
		checkAutoProtect.setOnCheckedChangeListener(this);
		checkCapture.setOnCheckedChangeListener(this);
		checkLocate.setOnCheckedChangeListener(this);
		checkDisplayUnaccessIcon.setOnCheckedChangeListener(this);
		checkBackupData.setOnCheckedChangeListener(this);
		checkDeleteData.setOnCheckedChangeListener(this);
		// set enable for views base on the configuration
		//set for auto protect
		AppUtil.enableDisableView(view.findViewById(R.id.autoprotect_panel), 
				SaveData.getInstance(getActivity()).isAutoProtect());
		//enable checkAutoProtect
		checkAutoProtect.setEnabled(true);
		//set for capture
		AppUtil.enableDisableView(view.findViewById(R.id.capture_panel), 
				SaveData.getInstance(getActivity()).isCapture());
		//enable checkCapture
		checkCapture.setEnabled(true);
		//set for get location
		AppUtil.enableDisableView(view.findViewById(R.id.getLocation_panel), 
				SaveData.getInstance(getActivity()).isLocation());
		//enable checkLocate
		checkLocate.setEnabled(true);
		//set for get display icon
		AppUtil.enableDisableView(view.findViewById(R.id.displayIcon_panel), 
						SaveData.getInstance(getActivity()).isShowIcon());
		//enable checkDisplayUnaccessIcon
		checkDisplayUnaccessIcon.setEnabled(true);
		//set for  backup
		AppUtil.enableDisableView(view.findViewById(R.id.backup_panel), 
				SaveData.getInstance(getActivity()).isBackupData());
		//enable checkBackupData
		checkBackupData.setEnabled(true);
		checkDeleteData.setChecked(SaveData.getInstance(getActivity()).isDeleteDataAndReset());
		//set for  wipe data
		AppUtil.enableDisableView(view.findViewById(R.id.wipedata_panel), 
				SaveData.getInstance(getActivity()).isDeleteDataAndReset());			
		
		if((SaveData.getInstance(getActivity()).getPremium() == 1) && (SaveData.getInstance(getActivity()).getExpirePremium() > System.currentTimeMillis())){
			// Acc Premium
			checkDeleteData.setEnabled(true);
		}else{
			// Acc normal
			checkDeleteData.setEnabled(false);
			checkDeleteData.setBackgroundResource(R.drawable.switch_empty);
		}
		
		editTimesFailed.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(editTimesFailed.getText().toString())){
					int i = Integer.parseInt(editTimesFailed.getText().toString());
					if(i > 0)
						SaveData.getInstance(getActivity()).setSetupLogin(i);
				}
			}
		});
		
		
	}
	/**
	 * Check the times login failed.
	 *
	 * @param times the times
	 * @return true, if successful
	 */
	boolean onWrongLogin(int times){
		return false;
	}
	
	/**
	 * Capture when login failed!.
	 */
	void onCapture(){
		
	}
	
	/**
	 * Turn network such as 3G/wifi on.
	 */
	void onConnectNetwork(){
		
	}
	
	/**
	 * Display icon when device be harmful.
	 */
	void onDisplayicon(){
		
	}
	
	/**
	 * Backing up data includes contact and sms from device.
	 */
	void onBackupData(){
		
	}
	
	/**
	 * Delete data and reset factory.
	 */
	void onDeleteDataAndReset(){
		
	}
	private void setAutoUpdate(boolean isChecked){
		if (isChecked) {
			SaveData saveData=SaveData.getInstance(getActivity());
			AppUtil.enableDisableView(view.findViewById(R.id.autoprotect_panel), saveData.isAutoProtect());
			checkAutoProtect.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.capture_panel), saveData.isCapture());
			checkCapture.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.getLocation_panel), saveData.isLocation());
			checkLocate.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.displayIcon_panel), saveData.isShowIcon());
			checkDisplayUnaccessIcon.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.backup_panel), saveData.isBackupData());
			checkBackupData.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.wipedata_panel), saveData.isDeleteDataAndReset());
			checkDeleteData.setEnabled(true);
		} else {
			AppUtil.enableDisableView(view.findViewById(R.id.autoprotect_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.capture_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.getLocation_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.displayIcon_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.backup_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.wipedata_panel), isChecked);
		}
		AppUtil.enableDisableView(view.findViewById(R.id.login_panel), isChecked);
		
		
	}


	
}
