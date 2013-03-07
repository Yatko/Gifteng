package com.venefica.module.messages;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.venefica.module.main.R;

/**
 * @author avinash
 * Activity for messages
 */
public class MessageListActivity extends Activity {
	/**
	 * List to show messages
	 */
	private ListView listViewMessages;
	/**
	 * List adapter
	 */
	private MessageListAdapter messageListAdapter;
	/**
	 * Message list
	 */
	private ArrayList<MessageData> messages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);
		listViewMessages = (ListView) findViewById(R.id.listActMessageList);
		messages = getDemoMessages();
        
		messageListAdapter = new MessageListAdapter(this, messages);
		listViewMessages.setAdapter(messageListAdapter);
		listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
				startActivity(intent);
			}
		});
	}
	/**
	 * Method to get demo data
	 * @return
	 */
	private ArrayList<MessageData> getDemoMessages() {
		ArrayList<MessageData> messages = new ArrayList<MessageData>();
		MessageData message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		message.setUnread(false);
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		message = new MessageData();
		message.setCreationDate("18:10");
		message.setMessageText("Demo message");
		message.setSenderName("Sender");
		messages.add(message);
		return messages;
	}
}
