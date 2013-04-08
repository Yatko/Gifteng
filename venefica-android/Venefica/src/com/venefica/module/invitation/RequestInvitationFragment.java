package com.venefica.module.invitation;

import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;

/**
 * @author avinash
 * Fragment class for request invitation layout
 */
public class RequestInvitationFragment extends SherlockFragment implements OnClickListener{
		
	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnRequestInvitationListener{
		public void onRequestInvitationClick(String email);
		public void onHaveInvitationClick();		
	}
	private OnRequestInvitationListener onRequestInvitationListener;
	/**
	 * Buttons
	 */
	private Button btnReqInvite, btnHaveInvite;
	private EditText edtEmail;
	private InputFieldValidator vaildator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_request_invitation, container, false);
		btnReqInvite = (Button) view.findViewById(R.id.btnActLoginRequestInvite);
		btnReqInvite.setOnClickListener(this);
		btnHaveInvite = (Button) view.findViewById(R.id.btnActLoginHaveInviteCode);
		btnHaveInvite.setOnClickListener(this);
		
		edtEmail = (EditText) view.findViewById(R.id.edtActLoginReqInvEmail);
		edtEmail.setText(Utility.getEmail(getActivity()));
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	onRequestInvitationListener = (OnRequestInvitationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRequestInvitationListener");
        }
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginRequestInvite && onRequestInvitationListener != null) {
			if (validateFields()) {
				onRequestInvitationListener.onRequestInvitationClick(edtEmail.getText().toString());
			}											
		} else if (id == R.id.btnActLoginHaveInviteCode && onRequestInvitationListener != null){
			onRequestInvitationListener.onHaveInvitationClick();
		}
	}
	
	/**
     * Method to validate input fields
     * @return result of validation
     */
    private boolean validateFields(){
    	boolean result = true;
    	StringBuffer message = new StringBuffer();
    	if(vaildator == null){
    		vaildator = new InputFieldValidator();    		
    	}
    	
    	if(!vaildator.validateField(edtEmail, Pattern.compile(InputFieldValidator.emailPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_email).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_email));
    		message.append("\n");
    	}
    	if (!result) {
			Utility.showLongToast(getActivity(), message.toString());
		}
		return result;    	
    }
}
