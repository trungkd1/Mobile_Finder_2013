/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.dialog - SynDialogFragment.java
 * Date create: 2:55:22 PM - Nov 21, 2013 - 2013
 * 
 * 
 */

package com.nahi.vn.mobilefinder.dialog;


import com.nahi.vn.mobilefinder.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class SynDialogFragment. This class is used to show loading dialog to
 * show users that this app is processing some long time task.
 * 
 * @author TamLe
 */
public class SynDialogFragment extends DialogFragment {

    /** The Constant TAG. */
    public static final String TAG = "SynDialogFragment";

    /** The message. */
    private String message;

    /** The layout. */
    private View layout;

    /** The waiting dismiss. */
    private static boolean waitingDismiss = false;

    /** The instance. */
    private static SynDialogFragment instance;
    
    /**
     * Gets the single instance of SynDialogFragment.
     *
     * @param message the message
     * @return single instance of SynDialogFragment
     */
    public static SynDialogFragment getInstance(String message) {
    	if(instance == null){
    		instance = new SynDialogFragment(message);
    	}
        return instance;
    }

    /**
     * Gets the single instance of SynDialogFragment.
     *
     * @return single instance of SynDialogFragment
     */
    public static SynDialogFragment getInstance() {
    	if(instance == null){
    		instance = new SynDialogFragment();
    	}
        return instance;
    }
    
    /**
     * Instantiates a new syn dialog fragment.
     *
     * @param message the message
     */
    private SynDialogFragment(String message) {
        this.message = message;
    }

    /**
     * Instantiates a new syn dialog fragment.
     */
    private SynDialogFragment() {
        super();
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Panel);
        setCancelable(false);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.syn_layout, container, false);
        if (message != null) {
            layout.findViewById(R.id.message).setVisibility(View.VISIBLE);
            ((TextView)layout.findViewById(R.id.message)).setText(message);
        }
        return layout;
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#show(android.support.v4.app.
     * FragmentManager, java.lang.String)
     */
    @Override
    public void show(FragmentManager arg0, String arg1) {
        waitingDismiss = false;
        try {
            super.show(arg0, arg1);
        } catch (IllegalStateException e) {
        }
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#dismiss()
     */
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (IllegalStateException e) {
            waitingDismiss = true;
        } catch (NullPointerException e) {
            waitingDismiss = true;
        }
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();
        if (waitingDismiss) {
            dismiss();
            waitingDismiss = false;
        }
    }

    /**
     * Dismiss dialog for activity.
     * 
     * @param activity the activity
     */
    public void dismiss(FragmentActivity activity) {
        if (activity != null) {
            DialogFragment progressDiag = ((DialogFragment)(activity.getSupportFragmentManager()
                    .findFragmentByTag(SynDialogFragment.TAG)));
            if (progressDiag != null) {
                progressDiag.dismiss();
            } else {
                waitingDismiss = true;
            }
        }

    }

    /**
     * Show dialog with a default message for activity.
     * 
     * @param activity the activity
     */
    public void show(FragmentActivity activity) {
        instance.show(activity.getSupportFragmentManager(), SynDialogFragment.TAG);
    }


}
