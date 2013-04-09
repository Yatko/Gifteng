/**
 * 
 */
package com.venefica.module.invitation;

import java.util.Calendar;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;
import com.venefica.module.user.UserDto;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.services.ImageDto;

/**
 * @author avinash
 * Fragment class to show join/user registration fragment
 */
public class JoinGiftingFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnJoinGiftingFragmentListener{
		public void onJoinButtonClick(String password, UserDto userDto);
	}
	private OnJoinGiftingFragmentListener joinGiftingFragmentListener;
	
	private Button btnJoin;
	private EditText edtFName, edtLName, edtEmail, edtPassword;
	/*
	 * validator instance
	 */
	private InputFieldValidator vaildator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_join_user, container, false);
		btnJoin = (Button) view.findViewById(R.id.btnActInvitationJoin);
		btnJoin.setOnClickListener(this);
		
		edtFName = (EditText) view.findViewById(R.id.edtActInvitationJoinFName);
		edtLName = (EditText) view.findViewById(R.id.edtActInvitationJoinLName);
		edtEmail = (EditText) view.findViewById(R.id.edtActInvitationJoinEmail);
		edtPassword = (EditText) view.findViewById(R.id.edtActInvitationJoinPassword);
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.joinGiftingFragmentListener = (OnJoinGiftingFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnJoinGiftingFragmentListener");
        }
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActInvitationJoin && this.joinGiftingFragmentListener != null) {
			if (validateFields()) {
				UserDto userDto = new UserDto();
				userDto.setFirstName(edtFName.getText().toString());
				userDto.setLastName(edtLName.getText().toString());
				userDto.setEmail(edtEmail.getText().toString());
				
				userDto.setName(/*edtEmail.getText().toString()*/"");
				userDto.setDateOfBirth(Calendar.getInstance().getTime());
				userDto.setJoinedAt(Calendar.getInstance().getTime());
				userDto.setPhoneNumber("");
				userDto.setZipCode("");
				userDto.setCity("");
				userDto.setCounty("");
				userDto.setArea("");
				userDto.setAvatar(new ImageDto(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));
				
				// hide virtual keyboard
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				joinGiftingFragmentListener.onJoinButtonClick(edtPassword.getText().toString(), userDto);
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
    	if(!vaildator.validateField(edtFName, Pattern.compile(InputFieldValidator.userNamePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_fname).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtLName, Pattern.compile(InputFieldValidator.userNamePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_lname).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtEmail, Pattern.compile(InputFieldValidator.emailPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_email).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_email));
    		message.append("\n");
    	} 
    	if(!vaildator.validateField(edtPassword, Pattern.compile(InputFieldValidator.charNumPatternRegx))
    			|| edtPassword.getText().toString().length() < 6){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_password).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.hint_user_password_pattern));
    		message.append("\n");
    	}
    	if (!result) {
			Utility.showLongToast(getActivity(), message.toString());
		}
		return result;    	
    }
}
