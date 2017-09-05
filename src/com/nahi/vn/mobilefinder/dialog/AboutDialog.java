package com.nahi.vn.mobilefinder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nahi.vn.mobilefinder.R;

// TODO: Auto-generated Javadoc
/**
 * The Class AboutDialog.
 */
public class AboutDialog extends Dialog {

	/** The listener. */
	private DialogListener listener;
	
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
		 * Dismiss listener.
		 *
		 * @param dialog the dialog
		 */
		public void dismissListener(Dialog dialog);
		
	}
	
	/**
	 * Instantiates a new about dialog.
	 *
	 * @param context the context
	 * @param theme the theme
	 */
	public AboutDialog(Context context, int theme) {
		super(context, R.style.CustomDialogTheme);
	}
	
	
	/**
	 * Instantiates a new about dialog.
	 *
	 * @param context the context
	 * @param listener the listener
	 */
	public AboutDialog(Context context, DialogListener listener) {
		super(context, R.style.CustomDialogTheme);
		this.listener = listener;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_about);
		Button btnOk = (Button) findViewById(R.id.btn_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.dismissListener(AboutDialog.this);
				}
			}

		});
	}
	
}
