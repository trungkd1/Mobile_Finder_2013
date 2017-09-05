package com.nahi.vn.mobilefinder.dialog;

import android.content.Context;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.listener.DialogListener;

public class UserGuideDialog extends BaseDialog {

	public UserGuideDialog(Context context, DialogListener listener) {
		super(context, R.style.CustomDialogTheme, listener);
		setContentView(R.layout.layout_user_guide);
	}

}
