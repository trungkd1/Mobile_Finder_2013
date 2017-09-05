package com.nahi.vn.mobilefinder.adapter;

import java.util.ArrayList;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.entity.SMSEntry;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMSLogsAdapter  extends BaseAdapter {
	/** The m inflater. */
	private LayoutInflater mInflater;
	
	/** The data. */
	private ArrayList<SMSEntry> data;

	
	private Context context;
	
	/**
	 * Instantiates a new sMS logs adapter.
	 *
	 * @param context the context
	 * @param data the data
	 */
	public SMSLogsAdapter(Context context, ArrayList<SMSEntry> data) {
		mInflater = LayoutInflater.from(context);
		this.data = data;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return data.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					android.R.layout.simple_list_item_2, null);
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView
					.findViewById(android.R.id.text1);
			holder.text2 = (TextView) convertView
					.findViewById(android.R.id.text2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SMSEntry e = data.get(position);
		holder.text1.setTextColor(Color.BLACK);
		holder.text2.setTextColor(context.getResources().getColor(R.color.gray));
		holder.text1.setText(formatLine1(e));
		holder.text2.setText(e.mBody);
		return convertView;
	}

	/**
	 * Format line1.
	 *
	 * @param e the e
	 * @return the char sequence
	 */
	private CharSequence formatLine1(SMSEntry e) {
		if (TextUtils.isEmpty(e.mName))
			return e.mNumber;
		return String.format("%s [%s]", e.mName, e.mNumber);
	}

	/**
	 * The Class ViewHolder.
	 */
	private class ViewHolder {
		
		/** The text1. */
		TextView text1;
		
		/** The text2. */
		TextView text2;
	}
}
