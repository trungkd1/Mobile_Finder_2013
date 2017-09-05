package com.nahi.vn.mobilefinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.util.ContactHelperSdk5;

public class ContactListAdapter  extends CursorAdapter implements
Filterable{

	
	/** The ctx. */
	Activity ctx;

	/**
	 * Instantiates a new contact list adapter.
	 *
	 * @param context the context
	 * @param c the c
	 */
	public ContactListAdapter(Activity context, Cursor c) {
		super(context, c, true);
		ctx = context;
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		final TextView view = (TextView) inflater.inflate(
				android.R.layout.simple_dropdown_item_1line, parent, false);
		view.setText(cursor.getString(5) + "[" + cursor.getString(3) + "]");
		return view;
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView) view).setText(cursor.getString(5) + "("
				+ cursor.getString(3) + ")");
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#convertToString(android.database.Cursor)
	 */
	@Override
	public String convertToString(Cursor cursor) {
		return cursor.getString(5);
	}

	/* (non-Javadoc)
	 * @see android.widget.CursorAdapter#runQueryOnBackgroundThread(java.lang.CharSequence)
	 */
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (getFilterQueryProvider() != null) {
			return getFilterQueryProvider().runQuery(constraint);
		}
		return new ContactHelperSdk5(ctx).queryFilter(constraint);
	}
}
