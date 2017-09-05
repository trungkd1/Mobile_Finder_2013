/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.dialog - BaseDialog.java
 * Date create: 2:51:26 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package com.nahi.vn.mobilefinder.dialog;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.listener.DialogListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseDialog.
 */
public class BaseDialog extends Dialog {

	/** The listener. */
	protected DialogListener listener;

	/**
	 * Instantiates a new base dialog.
	 *
	 * @param context the context
	 * @param theme the theme
	 * @param listener the listener
	 */
	public BaseDialog(Context context, int theme, DialogListener listener) {
		super(context,theme);
		this.listener = listener;
	}

	/**
	 * Instantiates a new base dialog.
	 *
	 * @param context the context
	 * @param theme the theme
	 */
	public BaseDialog(Context context, int theme) {
		super(context,theme);
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (listener != null) {
			Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
			Button btn_ok = (Button) findViewById(R.id.btn_ok);
			btn_cancel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					listener.cancelListener(BaseDialog.this);
				}

			});

			btn_ok.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					listener.acceptListener(BaseDialog.this);
				}

			});
		}
	}

}
