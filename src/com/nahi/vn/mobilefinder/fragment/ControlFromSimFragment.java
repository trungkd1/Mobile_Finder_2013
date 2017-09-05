/*
 * 
 */
package com.nahi.vn.mobilefinder.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.dialog.ContactDialog;
import com.nahi.vn.mobilefinder.dialog.ContactDialog.ContactAdapter;
import com.nahi.vn.mobilefinder.dialog.ContactDialog.ListContactListener;
import com.nahi.vn.mobilefinder.entity.ContactObj;
import com.nahi.vn.mobilefinder.handler.SyncSetting;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class ControlFromSimFragment.
 */
public class ControlFromSimFragment  extends Fragment implements OnClickListener,OnCheckedChangeListener{

	/** Toggle button control from sim. */
	private ToggleButton toggleControlBySim;
	
	/** Toggle button back up data. */
	private ToggleButton toggleNotifyChangedSim;
	
	/** The numbers. */
	private String[] numbers;
	
	/** The edit number. */
	private EditText editNumber;
	
	/** The Constant ACTION_RINGTONE. */
	private final static int ACTION_RINGTONE = 22;
	
	/** The Constant aCTION_CONTACT. */
	private final static int ACTION_CONTACT = 21;
	
	private View view;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_protect_by_sim, container, false);
		init(view);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//Set last filter number to edit text
		showLastContact();
	}
	
	/**
	 * Show last contact.
	 *
	 * @return true, if successful
	 */
	private boolean showLastContact(){
		ContactObj lastContact = App.getDB().getLastContactObj();
		if(lastContact != null){
			if(editNumber != null){
				editNumber.setText(lastContact.number);
			}
			return true;
		} else {
			return false;
		}
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
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.toggle_control_by_sim:
			SaveData.getInstance(getActivity()).setControlFromSim(isChecked);
			setProtectBySim(isChecked);
			toggleControlBySim.setEnabled(true);
			break;
		case R.id.toggle_notify_changed_sim:
			SaveData.getInstance(getActivity()).setNotificationSimChanged(isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.change_sim_panel), isChecked);
			toggleNotifyChangedSim.setEnabled(true);
			break;
		}
	}
	

	private void setProtectBySim(boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			SaveData saveData=SaveData.getInstance(getActivity());
			AppUtil.enableDisableView(view.findViewById(R.id.sim_protect_panel), saveData.isControlFromSim());
			toggleControlBySim.setEnabled(true);
			AppUtil.enableDisableView(view.findViewById(R.id.change_sim_panel), saveData.isNotificationSimChanged());
			toggleNotifyChangedSim.setEnabled(true);
		} else {
			AppUtil.enableDisableView(view.findViewById(R.id.sim_protect_panel), isChecked);
			AppUtil.enableDisableView(view.findViewById(R.id.change_sim_panel), isChecked);
		}
		AppUtil.enableDisableView(view.findViewById(R.id.select_phones_panel), isChecked);
		AppUtil.enableDisableView(view.findViewById(R.id.select_sound_panel), isChecked);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_set_phone_number:
			onNumber();
			break;
			
		case R.id.btn_select_alarm_sound:
			onSoundAlert();
			break;
			
		case R.id.edit_number:
			onLoadNumber();
			break;
		}
	}
	
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {				
		toggleControlBySim = (ToggleButton)view.findViewById(R.id.toggle_control_by_sim);
		toggleNotifyChangedSim = (ToggleButton)view.findViewById(R.id.toggle_notify_changed_sim);
		editNumber = (EditText) view.findViewById(R.id.edit_number);
		
		editNumber.setOnClickListener(this);
		view.findViewById(R.id.btn_set_phone_number).setOnClickListener(this);
		view.findViewById(R.id.btn_select_alarm_sound).setOnClickListener(this);
		toggleControlBySim.setOnCheckedChangeListener(this);
		toggleNotifyChangedSim.setOnCheckedChangeListener(this);
		
		toggleControlBySim.setChecked(SaveData.getInstance(getActivity()).isControlFromSim());
		toggleNotifyChangedSim.setChecked(SaveData.getInstance(getActivity()).isNotificationSimChanged());
		
		AppUtil.enableDisableView(view.findViewById(R.id.sim_protect_panel),
				SaveData.getInstance(getActivity()).isControlFromSim());
		toggleControlBySim.setEnabled(true);
		AppUtil.enableDisableView(view.findViewById(R.id.change_sim_panel), 
				SaveData.getInstance(getActivity()).isNotificationSimChanged());
		toggleNotifyChangedSim.setEnabled(true);
		
	}


	
	/**
	 * Select sound alert.
	 */
	void onSoundAlert(){
		 Intent tmpIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		 if(!TextUtils.isEmpty(SaveData.getInstance(getActivity()).getUriSoundAlarm())){
			 tmpIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(SaveData.getInstance(getActivity()).getUriSoundAlarm()));
		 }
	     startActivityForResult(tmpIntent, ACTION_RINGTONE);	
	 }
	
	/**
	 * Number phone control faraway.
	 */
	private void onNumber(){
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		pickContactIntent
				.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, Config.ACTION_CONTACT);
		
	}

	/**
	 * On load number.
	 */
	private void onLoadNumber() {
		ArrayList<ContactObj> objs = App.getDB().getAllContacts();
		if (objs.size() > 0) {
			numbers = new String[objs.size()];
			for (int i = 0; i < objs.size(); i++) {
				numbers[i] = objs.get(i).number;
			}
			openNewDialog(objs);
		}

	}
	
	/**
	 * Open new dialog.
	 *
	 * @param contacts the contacts
	 */
	private void openNewDialog(ArrayList<ContactObj> contacts) {
		ContactDialog dialog = new ContactDialog(getActivity(), contacts, new ListContactListener() {
			
			@Override
			public void onItemClick(ContactAdapter contactAdapter, View view,
					int position, long id) {
				onShowNotifyDialog(getActivity().getString(R.string.remove_number), String.format(getString(R.string.do_you_want_to_remove_number), contactAdapter.getItem(position).number),true, contactAdapter.getItem(position).number);		
			}
		});
		dialog.show();
	}
	
	
	/**
	 * On show notify dialog.
	 *
	 * @param title the title
	 * @param content the content
	 * @param hasTwoButtons the has two buttons
	 * @param number the number
	 */
	private void onShowNotifyDialog(String title,String content, final boolean hasTwoButtons, final String number){
		AppUtil.getNotifyDialog(getActivity(), 
			     title, content, hasTwoButtons, 
			     new DialogListener() {
			      
			      @Override
			      public void cancelListener(Dialog dialog) {
			       dialog.dismiss();
			      }
			      
			      @Override
			      public void acceptListener(Dialog dialog) {
			       if(hasTwoButtons){
			    	   int result = App.getDB().deleteContact(number);
			    	   if(result > 0){
			    		   if(!showLastContact()){
			    			   if(editNumber != null){
			    				   editNumber.setText("");
			    			   }
			    		   }
			    	   }
			       }
			       dialog.dismiss();
			      }
			     }).show();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK ){
			switch (requestCode) {
			case ACTION_CONTACT:
				if (data != null) {
					String number = AppUtil
							.getNumber(getActivity(), data.getData());
					if(!TextUtils.isEmpty(number) && android.util.Patterns.PHONE.matcher(number).matches()){
						long result = App.getDB().insertContact(new ContactObj("", number));
						if(editNumber != null && !TextUtils.isEmpty(number) && result > 0){
							editNumber.setText(number);
						}
					}
				}
				break;
				
			case ACTION_RINGTONE:
				if(data.hasExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)){
					Uri uri  = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
					if(uri != null){
						SaveData.getInstance(getActivity()).setUriSoundAlarm(uri.toString());
					} else {
						SaveData.getInstance(getActivity()).setUriSoundAlarm("");
					}
				}
			
				break;
			}
			
		}
		
	}

}
