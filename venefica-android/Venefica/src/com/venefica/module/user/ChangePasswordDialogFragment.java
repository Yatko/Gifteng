/**
 * 
 */
package com.venefica.module.user;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.venefica.module.main.R;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;

/**
 * @author avinash
 * Dialog fragment to change password
 */
public class ChangePasswordDialogFragment extends SherlockDialogFragment implements OnClickListener {

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnChangePasswordDialogListener {
        void onSavePassword(String oldPassword, String newPassword);
    }
	private OnChangePasswordDialogListener changePasswordDialogListener;
	/**
	 * input fields
	 */
	private EditText edtOldPassword, edtNewPassword, edtConfPassword;
	private Button btnSave;
	/**
	 * Field validator
	 */
	private InputFieldValidator vaildator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_change_password, container);
		edtOldPassword = (EditText) view.findViewById(R.id.edtActProfileOldPassword);
		edtNewPassword = (EditText) view.findViewById(R.id.edtActProfileNewPassword);
		edtConfPassword = (EditText) view.findViewById(R.id.edtActProfileNewPasswordConfirm);
		btnSave = (Button) view.findViewById(R.id.btnActProfileSave);
		btnSave.setOnClickListener(this);
        getDialog().setTitle(getResources().getString(R.string.g_label_change_password));

        // Show soft keyboard automatically
        edtOldPassword.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.changePasswordDialogListener = (OnChangePasswordDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChangePasswordDialogListener");
        }
	}

	@Override
	public void onClick(View v) {
		if (validateFields()) {
			// hide virtual keyboard
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(btnSave.getWindowToken(), 0);
			this.changePasswordDialogListener.onSavePassword(edtOldPassword
					.getText().toString().trim(), edtNewPassword.getText()
					.toString().trim());
		}
	}
	
	/**
     * Method to validate input fields for registration
     * @return result of validation
     */
	public boolean validateFields(){
    	boolean result = true;
    	StringBuffer message = new StringBuffer();
    	if(vaildator == null){
    		vaildator = new InputFieldValidator();    		
    	}    	
    	if(!vaildator.validateField(edtOldPassword, Pattern.compile(InputFieldValidator.CHAR_NUM_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_password).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.hint_user_password_pattern));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtNewPassword, Pattern.compile(InputFieldValidator.CHAR_NUM_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_new_password).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.hint_user_password_pattern));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtConfPassword, Pattern.compile(InputFieldValidator.CHAR_NUM_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_conf_password).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.hint_user_password_pattern));
    		message.append("\n");
    	}
    	if (!edtNewPassword.getText().toString().trim().equals(edtConfPassword.getText()
					.toString().trim())) {
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_new_password).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.g_hint_conf_password).toString());
    		message.append(" "+getResources().getString(R.string.g_msg_pwd_not_match).toString());
		}
    	if (!result) {
			Utility.showLongToast(getActivity(), message.toString()); 
		}
		return result;    	
    }
}
