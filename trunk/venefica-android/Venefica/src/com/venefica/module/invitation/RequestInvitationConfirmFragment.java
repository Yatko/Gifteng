/**
 * 
 */
package com.venefica.module.invitation;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.services.InvitationDto.UserType;

/**
 * @author avinash
 *  Fragment class for confirm request invitation layout
 */
public class RequestInvitationConfirmFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash 
	 * Listener to communicate with activity
	 */
	public interface OnRequestInvitationConfirmClickListener {
		public void onConfirmRequest(String zipCode, String source, UserType useType);
	}

	/**
	 * listener object
	 */
	private OnRequestInvitationConfirmClickListener listener;

	/**
	 * Button
	 */
	private Button btnConfirmRequest;
	private EditText edtZip;
	private Spinner spinSource;
	private RadioGroup radioGroup;
	/*
	 * validator instance
	 */
	private InputFieldValidator vaildator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//set layout for fragment
		View view = inflater.inflate(R.layout.view_confirm_invitation_request,
				container, false);
		btnConfirmRequest = (Button) view.findViewById(R.id.btnActLoginConfirmReq);
		btnConfirmRequest.setOnClickListener(this);
		
		spinSource = (Spinner) view.findViewById(R.id.spinActInvitationKnowUs);
		edtZip = (EditText) view.findViewById(R.id.edtActInvitationZipCode);
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupActInvitation);
		return view;
	}

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	listener = (OnRequestInvitationConfirmClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRequestInvitationConfirmClickListener");
        }
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginConfirmReq && this.listener != null) {
			if (validateFields()) {
				UserType useType = null ;
				int selectedRadioFilter = radioGroup.getCheckedRadioButtonId();
				switch (selectedRadioFilter) {
				  case R.id.radioActInvitationGiving :
					  useType = UserType.GIVER;
       	              break;
				  case R.id.radioActInvitationReceiving :
					  useType = UserType.RECEIVER;
					  break;
				}
				// hide virtual keyboard
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				this.listener.onConfirmRequest(edtZip.getText().toString()
						, spinSource.getSelectedItem().toString()
						, useType);
			}			
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
    	
    	if(!vaildator.validateField(edtZip, Pattern.compile(InputFieldValidator.zipCodePatternRegx)) || edtZip.getText().toString().length() < 6){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_zip).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_zipcode));
    		message.append("\n");
    	}
    	if (spinSource.getSelectedItemPosition() == 0) {
    		result = false;    		
    		message.append(getResources().getString(R.string.g_msg_select_source));
    		message.append("\n");
		}
    	if (!result) {
			Utility.showLongToast(getActivity(), message.toString());
		}
		return result;    	
    }
}
