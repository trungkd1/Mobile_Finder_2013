package com.nahi.vn.mobilefinder.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.dialog.AboutDialog;
import com.nahi.vn.mobilefinder.dialog.NotifyDialog;
import com.nahi.vn.mobilefinder.listener.DialogListener;
import com.nahi.vn.mobilefinder.util.AppUtil;
import com.nahi.vn.mobilefinder.util.Config;

// TODO: Auto-generated Javadoc
/**
 * The Class MainOptionFragment.
 */
public class MainOptionFragment extends Fragment implements OnClickListener{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_main_option, container, false);
		init(view);
		
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_feed_back:
				onFeedback();
				break;
			case R.id.btn_share:
				onShare();
				break;
			case R.id.btn_update:
				onUpdate();
				break;
			case R.id.btn_about:
				onAbout();
				break;
			case R.id.btn_uninstall:
				onUninstall();
				break;
			default:
				break;
		}
	}

	/**
	 * Inits the.
	 *
	 * @param view the view
	 */
	private void init(View view) {
		//Layout option
		view.findViewById(R.id.btn_feed_back).setOnClickListener(this);
		view.findViewById(R.id.btn_share).setOnClickListener(this);
		view.findViewById(R.id.btn_update).setOnClickListener(this);
		view.findViewById(R.id.btn_about).setOnClickListener(this);
		view.findViewById(R.id.btn_uninstall).setOnClickListener(this);
		new NotifyDialog(getActivity(), new DialogListener() {

			@Override
			public void acceptListener(Dialog dialog) {
				dialog.dismiss();
			}

			@Override
			public void cancelListener(Dialog dialog) {
				dialog.dismiss();
			}
			
		});
		setHasOptionsMenu(false);
	}
	
	/**
	 * About information of producer.
	 */
	private void onAbout() {
		AboutDialog aboutDialog = new AboutDialog(getActivity(), new AboutDialog.DialogListener() {
			
			@Override
			public void dismissListener(Dialog dialog) {
				dialog.dismiss();
			}

		});
		aboutDialog.show();
	}

	/**
	 * Feedback on google play.
	 */
	private void onFeedback() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id="
				+ getActivity().getApplication().getPackageName()));
		startActivity(intent);
	}

	
	/**
	 * Share app.
	 */
	private void onShare() {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(shareIntent, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				String packageName = resolveInfo.activityInfo.packageName;
				Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
				targetedShareIntent.setType("text/plain");
				targetedShareIntent.putExtra(Intent.EXTRA_TEXT, Config.APP_NAME);
				targetedShareIntent.setPackage(packageName);
				targetedShareIntents.add(targetedShareIntent);
			}
			Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), getString(R.string.share));
			for (Intent intent : targetedShareIntents) {
				if (intent.getPackage().contains(Config.SKYPE_PACKAGE_NAME)) {
					//Remove skype from chooser
					targetedShareIntents.remove(intent);
					break;
				}
			}
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[] {}));
	
			startActivity(chooserIntent);
		}
	}

	/**
	 * Check update.
	 */
	private void onUpdate() {
		onFeedback();
	}

	/**
	 * Remove app.
	 */
	private void onUninstall() {
		if(AppUtil.isAdminActive(getActivity())){
			AppUtil.removeActiveAdmin(getActivity());
		}
		
		Uri uri = Uri.parse("package:" + getActivity().getApplication().getPackageName());
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		startActivityForResult(intent, Config.REQUEST_CODE_UNINSTALL_FREE_APP);
	}
}
