package com.nahi.vn.mobilefinder.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.handler.BackupData;
import com.nahi.vn.mobilefinder.handler.BackupData.BackupListener;
import com.nahi.vn.mobilefinder.handler.RestoreData;
import com.nahi.vn.mobilefinder.handler.RestoreData.RestoreListener;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.Log;
import com.nahi.vn.mobilefinder.util.ProfileData;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class BackupAndRestoreFragment.
 */
public class BackupAndRestoreFragment extends Fragment implements OnClickListener,OnCheckedChangeListener{

	/** The progress dialog. */
	private ProgressDialog progressDialog;
	
	/** The image auto backup. */
	private ImageView imageAutoBackup;

	/** The text auto backup. */
	private TextView textAutoBackup;

	/** The btn auto backup. */
	private View btnAutoBackup;
	
	/** The backup. */
	private BackupData backup;
	
	/** The restore. */
	private RestoreData restore;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_backup_restore, container, false);
		init(view);
		return view;
	}
	

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_backup:
			onBackupData();
			break;
		case R.id.btn_restore:
			onRestoreData();
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SaveData.getInstance(getActivity()).setAutoBackup(isChecked);
		onInitAutoBackup(isChecked);
	}
	
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {
		btnAutoBackup = (View)view.findViewById(R.id.btn_auto_back_up);
		imageAutoBackup = (ImageView)view.findViewById(R.id.image_auto_back_up);
		textAutoBackup = (TextView)view.findViewById(R.id.text_auto_back_up);
		
		view.findViewById(R.id.btn_backup).setOnClickListener(this);
		view.findViewById(R.id.btn_restore).setOnClickListener(this);
		ToggleButton checkAutoBackup = (ToggleButton)view.findViewById(R.id.toggle_auto_backup);
		checkAutoBackup.setChecked(SaveData.getInstance(getActivity()).isAutoBackup());
		checkAutoBackup.setOnCheckedChangeListener(this);
		onInitAutoBackup(SaveData.getInstance(getActivity()).isAutoBackup());
		
		initBackup();
		initRestore();
	}
	
	/**
	 * On init auto backup.
	 *
	 * @param isChecked the is checked
	 */
	private void onInitAutoBackup(boolean isChecked){
		imageAutoBackup.setImageResource(isChecked ? R.drawable.ic_wifi : R.drawable.ic_wifi_highlighted) ;
		AppUtil.setCheck(btnAutoBackup,textAutoBackup,isChecked);
	}
	
	/**
	 * Inits the progress dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param listener the listener
	 */
	private void initProgressDialog(String title, String message, DialogInterface.OnClickListener listener){
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
	}
	
	/**
	 * Inits the backup.
	 */
	private void initBackup(){
		backup = new BackupData(getActivity());
		backup.setBackupListener(new BackupListener() {
			
			@Override
			public void onStartBackup() {
				progressDialog.show();
			}
			
			@Override
			public void onFinishBackup() {
				try{
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
						progressDialog = null;
					}
					onShowNotifyDialog(getActivity().getString(R.string.backup_data),getActivity().getString(R.string.backup_success),false);
				} catch(Exception e){
					Log.e("error :" + e.toString());
				}
			}
			
			@Override
			public void onBackupFail() {
				try{
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
						progressDialog = null;
					}
					onShowNotifyDialog(getActivity().getString(R.string.backup_data),getActivity().getString(R.string.backup_fails),false);
				} catch(Exception e){
					Log.e("error :" + e.toString());
				}
			}

			@Override
			public void onCancelBackup() {
				try{
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
						progressDialog = null;
					}
				} catch(Exception e){
					Log.e("error :" + e.toString());
				}
			}
		});
	}
	
	/**
	 * Inits the restore.
	 */
	private void initRestore(){
		restore = new RestoreData(getActivity());
		restore.setRestoreListener(new RestoreListener() {
			
			@Override
			public void onStartRestore() {
				progressDialog.show();
			}
			
			@Override
			public void onRestoreFail() {
				try{
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
						progressDialog = null;
					}
					onShowNotifyDialog(getActivity().getString(R.string.restore_data),getActivity().getString(R.string.restore_fails),false);
				} catch(Exception e){
					Log.e("error 1:" + e.toString());
				}
			}
			
			@Override
			public void onFinishRestore() {
				try{
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
						progressDialog = null;
					}
					onShowNotifyDialog(getActivity().getString(R.string.restore_data),getActivity().getString(R.string.restore_success),false);
				} catch(Exception e){
					Log.e("error 2:" + e.toString());
				}
			}

			@Override
			public void onCancelRestore() {
				
			}
		});
	}
	
	/**
	 * Backing up data includes contact and sms from device.
	 */
	private void onBackupData(){
		if(!TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getToken()) && !TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getGCMId())){
			if(SaveData.getInstance(getActivity()).isInternetConnecting()){
				initProgressDialog(getString(R.string.backup_data_progress_title), getString(R.string.backup_data_progress_msg),
										new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												backup.cancelBackupDataTask();
											}
									});
				backup.backupDataTask(false);
			}
			else{
				//Show dialog inform don't have internet connection
				AppUtil.showCheckInternetConnectionDialog(getActivity());
			}
		}
		else{
			//If nahi login token or reg id is empty, redirect to the login screen
			AppUtil.restartFragment(getActivity(), R.id.fragment_container, LoginFragment.class, Config.FRAGMENT_LOGIN);
		}
	}
	

	/**
	 * Restoring data includes contact and sms from device.
	 */
	private void onRestoreData(){
		if(!TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getToken()) && !TextUtils.isEmpty(ProfileData.getInstance(getActivity()).getGCMId())){
			if(SaveData.getInstance(getActivity()).isInternetConnecting()){
				initProgressDialog(getString(R.string.restore_data_progress_title), getString(R.string.restore_data_progress_msg),
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								restore.cancelRestoreDataTask();
							}
					});
				restore.restoreData();
			}
			else{
				//Show dialog inform don't have internet connection
				AppUtil.showCheckInternetConnectionDialog(getActivity());
			}
		}
		else{
			//If nahi login token or reg id is empty, redirect to the login screen
			AppUtil.restartFragment(getActivity(), R.id.fragment_container, LoginFragment.class, Config.FRAGMENT_LOGIN);
		}
	}
	
	
	
	/**
	 * On show notify dialog.
	 *
	 * @param title the title
	 * @param content the content
	 * @param hasTwoButtons the has two buttons
	 */
	private void onShowNotifyDialog(String title,String content ,final boolean hasTwoButtons){
		AppUtil.getNotifyDialog(getActivity(), 
			     title, content, hasTwoButtons, 
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
