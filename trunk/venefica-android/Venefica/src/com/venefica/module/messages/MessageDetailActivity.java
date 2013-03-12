package com.venefica.module.messages;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.utils.Utility;
import com.venefica.services.MessageDto;

/**
 * @author avinash
 * Activity for message details
 */
public class MessageDetailActivity extends VeneficaActivity implements OnClickListener{
	/**
	 * text view to display message details
	 */
	private TextView txtSender, txtTime, txtMessageText;
	/**
	 * reply edit box and buttons
	 */
	private EditText edtReply;
	private ImageButton imgBtnReply, imgBtnSend;
	/**
	 * reply control visibility flag
	 */
	private boolean isReplyControlVisible = false;
	/**
	 * Message to send
	 */
	private MessageDto message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_transperent_black));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   
        setContentView(R.layout.activity_message_detail);
//        message = new MessageDto();getIntent().getExtras().getInt("act_mode");
        
        txtSender = (TextView) findViewById(R.id.txtActMsgDetailSender);
        txtSender.setText(getIntent().getExtras().getString("sender"));
        
        txtTime = (TextView) findViewById(R.id.txtActMsgDetailTime);
        txtTime.setText(getIntent().getExtras().getString("time"));
        
        txtMessageText = (TextView) findViewById(R.id.txtActMsgDetailMsgText);
        txtMessageText.setText(getIntent().getExtras().getString("message_text"));
        
        edtReply = (EditText) findViewById(R.id.edtActMsgDetailReplyText);
        imgBtnReply = (ImageButton)findViewById(R.id.imgBtnActMsgDetailReply);
        imgBtnReply.setOnClickListener(this);
        imgBtnSend = (ImageButton)findViewById(R.id.imgBtnActMsgDetailSend);
        imgBtnSend.setOnClickListener(this);
        //hide reply button if current user is sender/owner
        if (getIntent().getExtras().getBoolean("is_owner")) {
        	imgBtnReply.setVisibility(View.GONE);
		} else {
			imgBtnReply.setVisibility(View.VISIBLE);
		}
        //hide reply view by default
        setReplyViewVisibility(View.GONE);
//        displayMessage(message);
       
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
    	txtMessageText.setText(message.getText());
	}

    /**
     * Set reply edit box and send button visibility
     * @param visibility
     */
    private void setReplyViewVisibility(int visibility){
    	if (visibility == View.VISIBLE) {
    		isReplyControlVisible = true;
		}else {
			isReplyControlVisible = false;
		}
    	edtReply.setVisibility(visibility);
    	imgBtnSend.setVisibility(visibility);
    }
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgBtnActMsgDetailReply) {
			//show reply controls
			setReplyViewVisibility(View.VISIBLE);			
		} else if (view.getId() == R.id.imgBtnActMsgDetailSend){

		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
    	if (itemId == android.R.id.home) {
    		finish();
    	}
    	return true;
	}
	@Override
	public void onBackPressed() {
		if (isReplyControlVisible) {
			setReplyViewVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}		
	}
}
