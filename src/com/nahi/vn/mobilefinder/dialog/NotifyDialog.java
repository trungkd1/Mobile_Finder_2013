package com.nahi.vn.mobilefinder.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.listener.DialogListener;

public class NotifyDialog extends BaseDialog {

	private TextView textTitle;
	private TextView textContent;
	private Button btnCancel;
	private Button btnAccept;
	
	public NotifyDialog(Context context, DialogListener listener) {
		super(context, R.style.CustomDialogTheme, listener);
		setContentView(R.layout.layout_user_guide);
		textTitle = (TextView)findViewById(R.id.text_title);
		textContent = (TextView)findViewById(R.id.text_content);
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnAccept = (Button)findViewById(R.id.btn_ok);
		textContent.setMovementMethod(new ScrollingMovementMethod());
	}
	
	public void setTitle(String title){
		textTitle.setText(title);
	}

	
	public void setContent(String content){
		textContent.setText(content);
	}
	
	public void hasTwoButton(boolean is2Button){
		if(is2Button){
			btnCancel.setVisibility(View.VISIBLE);
		}else{
			btnCancel.setVisibility(View.GONE);
		}
	}
	
	public void settextButton(String text){
		btnAccept.setText(text);
	}
	
}
