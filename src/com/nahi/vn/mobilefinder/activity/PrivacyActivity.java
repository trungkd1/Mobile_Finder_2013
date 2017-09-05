package com.nahi.vn.mobilefinder.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.R;

// TODO: Auto-generated Javadoc
/**
 * The Class PrivacyActivity.
 */
public class PrivacyActivity extends Activity implements OnClickListener{

	/** The tv content. */
	private TextView tvContent; 
	
	private TextView tvTitle;
	
	/** The btn accept. */
	private Button btnAccept; 
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.layout_privacy);
        android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        getWindow().setAttributes(params);

		tvContent = (TextView) findViewById(R.id.text_content);
		tvTitle = (TextView)findViewById(R.id.text_title);
		tvContent.setMovementMethod(new ScrollingMovementMethod());
		btnAccept = (Button)findViewById(R.id.btn_accept);
		btnAccept.setOnClickListener(this);
		
		tvTitle.setText(R.string.user_guide);
		findViewById(R.id.webview_container).setVisibility(View.VISIBLE);
		WebView webview = (WebView)findViewById(R.id.webview);
		webview.loadUrl(getString(R.string.url_help));
	}
	

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_accept){
			setResult(RESULT_OK);
			finish();
		}
	}
}
