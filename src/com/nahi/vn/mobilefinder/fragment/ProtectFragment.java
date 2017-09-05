package com.nahi.vn.mobilefinder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nahi.vn.mobilefinder.handler.SyncSetting;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;
import com.nahi.vn.mobilefinder.util.SaveData;

// TODO: Auto-generated Javadoc
/**
 * The Class ProtectFragment.
 */
public class ProtectFragment extends Fragment implements OnCheckedChangeListener,OnClickListener{

	/** The toggle auto protect. */
	private ToggleButton toggleAutoProtect;
	
	/** The toggle control by sim. */
	private ToggleButton toggleControlBySim;
	
	/** The toggle control from web. */
	private ToggleButton toggleControlFromNAHI;

	/** The btn auto protect. */
	private View btnAutoProtect;

	/** The btn protect by sim. */
	private View btnProtectBySim;

	/** The btn protect from nahi. */
	private View btnProtectFromNahi;

	/** The image auto protect. */
	private ImageView imageAutoProtect;

	/** The image protect by sim. */
	private ImageView imageProtectBySim;

	/** The image protect from nahi. */
	private ImageView imageProtectFromNahi;

	/** The text auto protect. */
	private TextView textAutoProtect;

	/** The text protect by sim. */
	private TextView textProtectBySim;

	/** The text protect from nahi. */
	private TextView textProtectFromNahi;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_protect_setup, container, false);
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
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		switch (buttonView.getId()) {
		case R.id.toggle_auto_protect:
			SaveData.getInstance(getActivity()).setAutoProtect(isChecked);
			onInitAutoProtect(isChecked);
			break;
		case R.id.toggle_control_by_sim:
			SaveData.getInstance(getActivity()).setControlFromSim(isChecked);
			onInitProtectBySim(isChecked);
			break;
			
		case R.id.toggle_control_from_nahi:
			SaveData.getInstance(getActivity()).setControlFromNAHI(isChecked);
			onInitProtectFromNahi(isChecked);
			break;
		
		}
	}
	
	
	/**
	 * init.
	 *
	 * @param view the view
	 */
	private void init(View view) {		

		btnAutoProtect = (View)view.findViewById(R.id.btn_auto_protect);
		btnProtectBySim = (View)view.findViewById(R.id.btn_protect_by_sim);
		btnProtectFromNahi = (View)view.findViewById(R.id.btn_protect_from_nahi);
		
		imageAutoProtect = (ImageView)view.findViewById(R.id.ic_auto_protect);
		imageProtectBySim = (ImageView)view.findViewById(R.id.ic_protect_by_sim);
		imageProtectFromNahi = (ImageView)view.findViewById(R.id.ic_protect_from_nahi);
		
		textAutoProtect = (TextView)view.findViewById(R.id.text_auto_protect);
		textProtectBySim = (TextView)view.findViewById(R.id.text_protect_by_sim);
		textProtectFromNahi = (TextView)view.findViewById(R.id.text_protect_from_nahi);
		
		toggleAutoProtect = (ToggleButton)view.findViewById(R.id.toggle_auto_protect);
		toggleControlBySim = (ToggleButton)view.findViewById(R.id.toggle_control_by_sim);
		toggleControlFromNAHI = (ToggleButton)view.findViewById(R.id.toggle_control_from_nahi);
		
		view.findViewById(R.id.btn_auto_protect).setOnClickListener(this);
		view.findViewById(R.id.btn_protect_by_sim).setOnClickListener(this);
		view.findViewById(R.id.btn_protect_from_nahi).setOnClickListener(this);
		
		
		toggleAutoProtect.post(new Runnable() {
			
			@Override
			public void run() {
				toggleAutoProtect.setOnCheckedChangeListener(ProtectFragment.this);
				toggleAutoProtect.setChecked(SaveData.getInstance(getActivity()).isAutoProtect());
			}
		});
		
		toggleControlBySim.post(new Runnable() {
			
			@Override
			public void run() {
				toggleControlBySim.setOnCheckedChangeListener(ProtectFragment.this);
				toggleControlBySim.setChecked(SaveData.getInstance(getActivity()).isControlFromSim());
			}
		});
		
		toggleControlFromNAHI.post(new Runnable() {
			
			@Override
			public void run() {
				toggleControlFromNAHI.setOnCheckedChangeListener(ProtectFragment.this);
				toggleControlFromNAHI.setChecked(SaveData.getInstance(getActivity()).isControlFromNAHI());
			}
		});
		
	
		onInitAutoProtect(SaveData.getInstance(getActivity()).isAutoProtect());
		onInitProtectBySim(SaveData.getInstance(getActivity()).isControlFromSim());
		onInitProtectFromNahi(SaveData.getInstance(getActivity()).isControlFromNAHI());
	}
	
	/**
	 * On init auto protect.
	 *
	 * @param isChecked the is checked
	 */
	private void onInitAutoProtect(boolean isChecked){
		imageAutoProtect.setImageResource(isChecked ? R.drawable.ic_auto_protect : R.drawable.ic_auto_protect_highlighted) ;
		AppUtil.setCheck(btnAutoProtect,textAutoProtect,isChecked);
	}
	
	/**
	 * On init protect by sim.
	 *
	 * @param isChecked the is checked
	 */
	private void onInitProtectBySim(boolean isChecked){
		imageProtectBySim.setImageResource(isChecked ? R.drawable.ic_sim_protect : R.drawable.ic_sim_protect_highlighted) ;
		AppUtil.setCheck(btnProtectBySim,textProtectBySim,isChecked);
	}
	
	/**
	 * On init protect from nahi.
	 *
	 * @param isChecked the is checked
	 */
	private void onInitProtectFromNahi(boolean isChecked){
		imageProtectFromNahi.setImageResource(isChecked ? R.drawable.ic_nahi_protect : R.drawable.ic_nahi_protect_highlighted) ;
		AppUtil.setCheck(btnProtectFromNahi,textProtectFromNahi,isChecked);
	}
	
	/**
	 * When login wrong pass, system will auto turn protect on.
	 * 
	 */
	void onAutoProtect(){
		AppUtil.startFragment(getActivity(), R.id.fragment_container, AutoProtectFragment.class, Config.FRAGMENT_AUTO_PROTECT);
	}
	
	
	/**
	 * Control device from sim.
	 */
	void onControlFromSim(){
		AppUtil.startFragment(getActivity(), R.id.fragment_container, ControlFromSimFragment.class, Config.FRAGMENT_CRL_BY_SIM);
	}
	
	
	/**
	 * Control device from web server of NAHI.
	 */
	void onControlFromNAHI(){
		AppUtil.startFragment(getActivity(), R.id.fragment_container, ControlFromNAHIFragment.class,Config.FRAGMENT_CRL_FROM_NAHI);
	}


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_auto_protect:
			onAutoProtect();
			break;

		case R.id.btn_protect_by_sim:
			onControlFromSim();
			break;
			
		case R.id.btn_protect_from_nahi:
			onControlFromNAHI();
			break;
		}
	}

}
