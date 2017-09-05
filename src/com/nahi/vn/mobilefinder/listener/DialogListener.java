package com.nahi.vn.mobilefinder.listener;

import android.app.Dialog;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving dialog events.
 * The class that is interested in processing a dialog
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDialogListener<code> method. When
 * the dialog event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DialogEvent
 */
public interface DialogListener {

	/**
	 * Accept listenr.
	 *
	 * @param dialog the dialog
	 */
	public void acceptListener(Dialog dialog);
	
	/**
	 * Cancel listenr.
	 *
	 * @param dialog the dialog
	 */
	public void cancelListener(Dialog dialog);
}
