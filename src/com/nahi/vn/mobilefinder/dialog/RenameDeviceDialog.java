package com.nahi.vn.mobilefinder.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.listener.DialogListener;

// TODO: Auto-generated Javadoc
/**
 * The Class RenameDeviceDialog.
 */
public class RenameDeviceDialog extends BaseDialog{

	/** The edit_content. */
	private EditText edit_content;
	
	/** The text_title. */
	private TextView text_title;
	
	/**
	 * Instantiates a new rename device dialog.
	 *
	 * @param context the context
	 * @param listener the listener
	 */
	public RenameDeviceDialog(Context context,
			DialogListener listener) {
		super(context, R.style.CustomDialogTheme, listener);
		setContentView(R.layout.layout_user_guide);
		edit_content = (EditText)findViewById(R.id.edit_content);
		text_title = (TextView)findViewById(R.id.text_title);
		edit_content.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title){
		text_title.setText(title);
	}
	
	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent(){
		return edit_content.getText().toString();
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content){
		edit_content.setText(content);
	}
}
