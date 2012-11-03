package com.venefica.module.messages;

import com.venefica.activity.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author avinash
 * View for Message list Item
 */
public class MessageListItemView extends LinearLayout {
	private MessageData message;
	private TextView textSender;
	private TextView textTime;
	private TextView  textDetails;
	private Context context;
	/**
	 * Constructor to use view in java code
	 * @param context
	 */
	public MessageListItemView(Context context, MessageData message) {
		super(context);
		this.context = context;
		this.message = message;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_message_list_item, this, false);
		textSender = (TextView) view.findViewById(R.id.txtMessageLItemSender);
		textSender.setText(message.getSenderName());
		if(!message.isUnread()){
			textSender.setTypeface(null,Typeface.ITALIC);
		}
		textTime = (TextView) view.findViewById(R.id.txtMessageLItemTime);
		textTime.setText(message.getCreationDate());
		textDetails = (TextView) view.findViewById(R.id.txtMessageLItemDetails);
		textDetails.setText(message.getMessageText());
		this.addView(view);
	}
	/**
	 * @return the message object
	 */
	public MessageData getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(MessageData message) {
		this.message = message;
		textSender.setText(message.getSenderName());
		if (!message.isUnread()) {
			textSender.setTypeface(null, Typeface.NORMAL);
		}
		textTime.setText(message.getCreationDate());
		textDetails.setText(message.getMessageText());
		this.invalidate();
	}
}
