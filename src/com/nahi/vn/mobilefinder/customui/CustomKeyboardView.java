package com.nahi.vn.mobilefinder.customui;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.util.AppUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomKeyboardView.
 */
public class CustomKeyboardView extends LinearLayout {

	/** The context. */
	private Context context;
	
	/** The go listener. */
	private GoListener goListener;
	
	/** The edit text. */
	private EditText editText1;
	
	/** The edit text2. */
	private EditText editText2;
	
	/** The edit text3. */
	private EditText editText3;
	
	/** The edit text4. */
	private EditText editText4;
	
	/** The text. */
	private String text = "";
	
	/**
	 * The listener interface for receiving go events.
	 * The class that is interested in processing a go
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addGoListener<code> method. When
	 * the go event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see GoEvent
	 */
	public interface GoListener{
		
		/**
		 * On go.
		 *
		 * @param text the text
		 */
		public void onGo(String text);
		
		/**
		 * On incorrect.
		 */
		public void onIncorrect();
	}

	/**
	 * Instantiates a new custom keyboard view.
	 *
	 * @param context the context
	 */
	public CustomKeyboardView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 * Instantiates a new custom keyboard view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CustomKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	/**
	 * Inits the.
	 */
	public void init(){	
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.layout_keyboard, this);
		
		editText1 = (EditText)findViewById(R.id.edittext1);		
		
		editText2 = (EditText)findViewById(R.id.edittext2);
		
		editText3 = (EditText)findViewById(R.id.edittext3);
		
		editText4 = (EditText)findViewById(R.id.edittext4);
		
		editText4.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(editText4.getText().toString().trim())){

				}
			}
		});
		
		editText3.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
					editText4.requestFocus();
			}
		});
		
		editText2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
					editText3.requestFocus();
			}
		});
		
		editText1.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(editText1.getText().toString().trim())){
					editText2.requestFocus();
				}
				
			}
		});
		
		reset();
	}
	
	/**
	 * On click key.
	 *
	 * @param tag the tag
	 */
	public void onClickKey(String tag){
		AppUtil.vibrate(context);
		int value = Integer.parseInt(tag);

		switch(value){
		case -1:
			// Clear text
			int length = text.length();
			if(length > 0){
				switch (length) {
					case 1:
						editText1.setText("");
						editText1.requestFocus();
						break;
					case 2:
						editText2.setText("");
						editText2.requestFocus();
						break;
					case 3:
						editText3.setText("");
						editText3.requestFocus();
						break;
					case 4:
						editText4.setText("");
						editText4.requestFocus();
						break;
					default:
						break;
				}
				text = text.substring(0, length-1);
			}
			break;
		case -2:
			// Go clicked
			goListener.onGo(text);
			break;
		case -3:
			// Reset
			reset();
			break;
		default:
			if(text.length() < 4){
				if(editText1.isFocused()){
					editText1.append(tag);
					text += tag;
				}else if(editText2.isFocused()){
					editText2.append(tag);
					text +=tag;
				}else if(editText3.isFocused()){
					editText3.append(tag);
					text += tag;
				}else if(editText4.isFocused()){
					editText4.append(tag);
					text += tag;
				}				
			}
			break;
		}
	}
	
	/**
	 * On incorrect.
	 */
	public void onIncorrect(){
		goListener.onIncorrect();
	}
	
	/**
	 * Reset.
	 */
	public void reset(){
		text = "";
		editText1.setText("");
		editText2.setText("");
		editText3.setText("");
		editText4.setText("");
		editText1.requestFocus();
	}

	/**
	 * Sets the on go listener.
	 *
	 * @param goListener the new on go listener
	 */
	public void setOnGoListener(GoListener goListener){
		this.goListener = goListener;
	}
}
