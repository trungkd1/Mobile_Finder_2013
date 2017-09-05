package com.nahi.vn.mobilefinder.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nahi.vn.mobilefinder.R;
import com.nahi.vn.mobilefinder.entity.ContactObj;

// TODO: Auto-generated Javadoc
/**
 * The Class ContactDialog.
 */
public class ContactDialog extends Dialog{

	/** The lv contact. */
	private ListView lvContact;
	
	/**
	 * The listener interface for receiving listContact events.
	 * The class that is interested in processing a listContact
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addListContactListener<code> method. When
	 * the listContact event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ListContactEvent
	 */
	public interface ListContactListener{
		
		/**
		 * On item click.
		 *
		 * @param contactAdapter the contact adapter
		 * @param view the view
		 * @param position the position
		 * @param id the id
		 */
		public void onItemClick(ContactAdapter contactAdapter, View view, int position, long id);
	}
	
	/**
	 * Instantiates a new contact dialog.
	 *
	 * @param context the context
	 * @param theme the theme
	 */
	public ContactDialog(Context context, int theme) {
		super(context,theme);
	}

	/**
	 * Instantiates a new contact dialog.
	 *
	 * @param context the context
	 * @param contacts the contacts
	 * @param listContactListener the list contact listener
	 */
	public ContactDialog(Context context, ArrayList<ContactObj> contacts, final ListContactListener listContactListener) {
		super(context, R.style.CustomDialogTheme2);
		setContentView(R.layout.layout_contact_picker);
		
        android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 9/10;
        getWindow().setAttributes(params);
        
		lvContact = (ListView) findViewById(R.id.lvContact);
		final ContactAdapter contactAdapter = new ContactAdapter(context, R.layout.row_contact, contacts);
		lvContact.setAdapter(contactAdapter);
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				listContactListener.onItemClick(contactAdapter, view, position, id);
				ContactDialog.this.dismiss();
			}
		});
	}
	
	/**
	 * The Class ContactAdapter.
	 */
	public class ContactAdapter extends ArrayAdapter<ContactObj>{

		/**
		 * Instantiates a new contact adapter.
		 *
		 * @param context the context
		 * @param resource the resource
		 * @param objects the objects
		 */
		public ContactAdapter(Context context, int resource,
				List<ContactObj> objects) {
			super(context, resource, objects);
		}
		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactHolderView holder;
			if(convertView == null){
				convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row_contact, null);
				holder = new ContactHolderView();
				holder.number = (TextView) convertView.findViewById(R.id.number);
				convertView.setTag(holder);
			} else {
				holder = (ContactHolderView) convertView.getTag();
			}
			holder.number.setText(getItem(position).number);
			return convertView;
		}
	}

	/**
	 * The Class ContactHolderView.
	 */
	static class ContactHolderView{
		
		/** The number. */
		public TextView number;
	}
}
