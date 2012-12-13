package com.venefica.module.messages;

import com.venefica.module.main.R;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

/**
 * @author avinash
 * Activity for message details
 */
public class MessageDetailActivity extends Activity {
	private TextView txtSender, txtTime, txtMessageText, txtVotes, txtLikes, txtDislikes;
	private MessageData message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        message = getDemoMessage();
        
        txtSender = (TextView) findViewById(R.id.txtActMsgDetailSender);
        txtTime = (TextView) findViewById(R.id.txtActMsgDetailTime);
        
//        txtVotes = (TextView) findViewById(R.id.txtActMsgDetailVotes);
        txtLikes = (TextView) findViewById(R.id.txtActMsgDetailLikes);
        txtDislikes = (TextView) findViewById(R.id.txtActMsgDetailDisLikes);        
        txtMessageText = (TextView) findViewById(R.id.txtActMsgDetailMsgText);
        
        displayMessage(message);
       
    }
    
    /**
     * Method to show message data
     * @param message
     */
    private void displayMessage(MessageData message) {
    	txtSender.setText(message.getSenderName());
    	txtTime.setText(message.getCreationDate());
    	txtLikes.setText(message.getLikes()+"");
    	txtDislikes.setText(message.getDisLikes()+"");
    	txtMessageText.setText(message.getMessageText());
	}
	/**
     * Get Demo message
     * @return
     */
	private MessageData getDemoMessage() {
		MessageData message = new MessageData();
		message.setMessageId(1000);
		message.setDisLikes(10);
		message.setLikes(20);
		message.setCreationDate("10:30");
		message.setMessageText("Demo Message");
		message.setSenderName("Demo Sender");
		return message;
	}

	
}
