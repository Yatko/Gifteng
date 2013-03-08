package com.venefica.module.messages;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.services.MessageDto;

/**
 * @author avinash
 * Activity for message details
 */
public class MessageDetailActivity extends Activity {
	private TextView txtSender, txtTime, txtMessageText, txtVotes, txtLikes, txtDislikes;
	private MessageDto message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
//        message = getDemoMessage();
        
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
    private void displayMessage(MessageDto message) {
    	txtSender.setText(message.getFromFullName());
    	if (message.getCreatedAt() != null) {
    		txtTime.setText(Utility.convertDateTimeToString(message.getCreatedAt()));
		}    	
//    	txtLikes.setText(message.getLikes()+"");
//    	txtDislikes.setText(message.getDisLikes()+"");
    	txtMessageText.setText(message.getText());
	}
	
	
}
