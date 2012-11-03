package com.venefica.module.messages;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author avinash
 * Adapter for message list
 */
public class MessageListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MessageData> messages;
	/**
	 * Constructor for view
	 */
	public MessageListAdapter(Context context, ArrayList<MessageData> messages) {
		this.context = context;
		this.messages = messages;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return messages.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new MessageListItemView(context, messages.get(position));
		}else{
			((MessageListItemView)convertView).setMessage(messages.get(position));
		}
		return convertView;
	}
}
