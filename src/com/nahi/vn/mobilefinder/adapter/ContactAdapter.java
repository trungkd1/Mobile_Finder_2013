package com.nahi.vn.mobilefinder.adapter;

import com.nahi.vn.mobilefinder.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactAdapter extends CursorAdapter implements SectionIndexer {
	/** The m indexer. */
	private AlphabetIndexer mIndexer;
	
	/** The m inflater. */
	private LayoutInflater mInflater;

	private TextView searchText;
	
	
	/**
	 * Instantiates a new contact adapter.
	 *
	 * @param context the context
	 * @param c the c
	 */
	public ContactAdapter(Context context, Cursor c,TextView searchText) {
		super(context, c, true);
		mIndexer = new AlphabetIndexer(c, 1, " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		mInflater = LayoutInflater.from(context);
		this.searchText = searchText;
	}

	/**
	 * Show new string.
	 *
	 * @param textView the text view
	 */
	public void showNewString(TextView textView){
		String search = searchText.getText().toString().toLowerCase().trim();
		String temp = textView.getText().toString().toLowerCase();
		int start = temp.indexOf(search);
		int end = start + search.length();

		if(TextUtils.isEmpty(search) || start == -1){
			return;
		}

		SpannableString wordtoSpan = new SpannableString(textView.getText());
		wordtoSpan.setSpan(new BackgroundColorSpan(0xFFADE039), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(wordtoSpan);
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		TextView text2 = (TextView) view.findViewById(android.R.id.text2);

		text1.setTextColor(Color.BLACK);
		text2.setTextColor(context.getResources().getColor(R.color.gray));

		text1.setText(cursor.getString(1), TextView.BufferType.SPANNABLE);
		text2.setText(cursor.getString(2) + " ["
				+ getStringByType(cursor.getInt(3)) + "]", 
				TextView.BufferType.SPANNABLE);
		showNewString(text1);
		showNewString(text2);
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mInflater.inflate(android.R.layout.simple_list_item_2,
				null);
		bindView(v, context, cursor);
		return v;
	}

	/* (non-Javadoc)
	 * @see android.widget.SectionIndexer#getPositionForSection(int)
	 */
	public int getPositionForSection(int section) {
		return mIndexer.getPositionForSection(section);
	}

	/* (non-Javadoc)
	 * @see android.widget.SectionIndexer#getSectionForPosition(int)
	 */
	public int getSectionForPosition(int position) {
		return mIndexer.getPositionForSection(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.SectionIndexer#getSections()
	 */
	public Object[] getSections() {
		return mIndexer.getSections();
	}
	
	
	/**
	 * Gets the string by type.
	 *
	 * @param type the type
	 * @return the string by type
	 */
	private static String getStringByType(int type) {
		String numberType;
		switch (type) {
		case Phone.TYPE_MOBILE:
			numberType = "Mobile";
			break;
		case Phone.TYPE_HOME:
			numberType = "Home";
			break;
		case Phone.TYPE_WORK:
			numberType = "Work";
			break;
		default:
			numberType = "Other";
			break;
		}
		return numberType;
	}
}
