package com.nahi.vn.mobilefinder.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.activity.App;
import com.nahi.vn.mobilefinder.dialog.ContactDialog;
import com.nahi.vn.mobilefinder.dialog.ContactDialog.ContactAdapter;
import com.nahi.vn.mobilefinder.dialog.ContactDialog.ListContactListener;
import com.nahi.vn.mobilefinder.entity.CheckPremiumResponse;
import com.nahi.vn.mobilefinder.entity.ContactObj;
import com.nahi.vn.mobilefinder.entity.PremiumResult;
import com.nahi.vn.mobilefinder.handler.SyncSetting;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.listener.HttpClientListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;
import com.nahi.vn.mobilefinder.webservice.HttpClientHelper;
import com.nahi.vn.mobilefinder.webservice.NahiFinderParser.Methods;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigFragment.
 */
public class ConfigFragment extends Fragment implements OnClickListener, HttpClientListener{
	
	/** The edit number. */
	private EditText editNumber;
	
	/** The numbers. */
	String[] numbers;
	
	/** The adapter. */
	ArrayAdapter<String> adapter;
	
	/** The edit languages. */
	private EditText editLanguages;
	
	/** The edit set name device. */
	private EditText editSetNameDevice;
	
	/** The edit set account. */
	private EditText editSetAccount;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.layout_settings, container, false);
		init(view);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_set_account_NAHI:
			onLoginNAHI();
			break;
		case R.id.btn_set_phone_number:
			onNumber();
			break;
		case R.id.edit_languages:
			onSettinglanguages();
			break;
		case R.id.edit_number:
			onLoadNumber();
			break;
		}

	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		if(editSetNameDevice != null){
			SaveData.getInstance(getActivity()).setNameDevice(editSetNameDevice.getText().toString().trim());	
			if(!TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getToken())){
				HttpClientHelper.getInstance().updateDeviceName(ProfileData.getInstance(getActivity()).getToken(), SaveData.getInstance(getActivity()).getNameDevice());						
			}
		}
		//Save setting to server
		new SyncSetting(getActivity()).syncSettingServer();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//Check premium
		if(!TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getToken()) && SaveData.getInstance(getActivity()).isInternetConnecting()){
			HttpClientHelper.getInstance().checkFinderPremium(ProfileData.getInstance(getActivity()).getToken());
			HttpClientHelper.getInstance().setListener(this);
		}
		//Set last filter number to edit text
		showLastContact();
		//Set name device to edit text
		if(editSetNameDevice != null){
			SaveData.getInstance(getActivity()).setNameDevice(editSetNameDevice.getText().toString().trim());	
		}
		if(!TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getEmail())){
			editSetAccount.setText(ProfileData.getInstance(getActivity()).getEmail());
		}
		editLanguages.setText(java.util.Locale.getDefault().getDisplayName());
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Config.ACTION_CONTACT:
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
		}

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
	
	
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(final View view) {
		view.findViewById(R.id.btn_set_account_NAHI).setOnClickListener(this);
		view.findViewById(R.id.btn_set_phone_number).setOnClickListener(this);
		editSetNameDevice = (EditText) view
				.findViewById(R.id.edit_setname_device);
		editSetNameDevice.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//Disable btn login
				if(!TextUtils.isEmpty(editSetNameDevice.getText().toString().trim())){
					view.findViewById(R.id.btn_set_account_NAHI).setBackgroundResource(R.drawable.button_add_bg);
				} else {
					view.findViewById(R.id.btn_set_account_NAHI).setBackgroundResource(R.drawable.btn_add_disable);
				}
			}
		});
		//Disable btn login
		if(!TextUtils.isEmpty(editSetNameDevice.getText().toString().trim())){
			view.findViewById(R.id.btn_set_account_NAHI).setBackgroundResource(R.drawable.button_add_bg);
		} else {
			view.findViewById(R.id.btn_set_account_NAHI).setBackgroundResource(R.drawable.btn_add_disable);
		}
		editSetAccount = (EditText) view.findViewById(R.id.edit_set_account);
		editLanguages = (EditText) view.findViewById(R.id.edit_languages);
		editNumber = (EditText) view.findViewById(R.id.edit_number);
		editLanguages.setOnClickListener(this);
		editNumber.setOnClickListener(this);
		if(!TextUtils.isEmpty(SaveData.getInstance(getActivity()).getNameDevice())){
			editSetNameDevice.setText(SaveData.getInstance(getActivity()).getNameDevice());
		}
		editLanguages.setText(java.util.Locale.getDefault().getDisplayName());
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
	 * Number phone control faraway.
	 */
	private void onNumber() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		pickContactIntent
				.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, Config.ACTION_CONTACT);

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
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(editSetNameDevice != null){
			if(!TextUtils.isEmpty(editSetNameDevice.getText().toString().trim())){
				SaveData.getInstance(getActivity()).setNameDevice(
						editSetNameDevice.getText().toString().trim());
			}
		}
	}

	/**
	 * Login NAHI account to manage device from web server.
	 */
	private void onLoginNAHI() {
		if(!TextUtils.isEmpty(editSetNameDevice.getText().toString().trim())){
			AppUtil.restartFragment(getActivity(), R.id.fragment_container,
					LoginFragment.class, Config.FRAGMENT_LOGIN);
		} 
	}

	/**
	 * Select a language to display.
	 */
	private void onSettinglanguages() {
		AppUtil.startAction(getActivity(),
				android.provider.Settings.ACTION_LOCALE_SETTINGS);
	}

	@Override
	public void onHttpResult(Methods methods, List<Object> data) {
		// TODO Auto-generated method stub
		switch (methods) {
		case CHECK_FINDER_PREMIUM:
			if(data != null && data.size() > 0){
				if(data.get(0) instanceof CheckPremiumResponse){
					CheckPremiumResponse rs = (CheckPremiumResponse) data.get(0);
					PremiumResult premium = rs.getPremiumResult();
					if(!TextUtils.isEmpty(premium.getExpired())){
						SaveData.getInstance(getActivity()).setExpirePremium(AppUtil.getTimeString2long(premium.getExpired()));
						SaveData.getInstance(getActivity()).setPremium(premium.getPremium());
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
