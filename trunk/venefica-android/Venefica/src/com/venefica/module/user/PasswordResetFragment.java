/**
 * 
 */
package com.venefica.module.user;

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
import com.venefica.utils.Constants;

/**
 * @author avinash
 * Fragment class to show reset new password layout
 */
public class PasswordResetFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnPasswordReset{
		public void onResetPasswordButtonClick(String password, String code);
	}
	private OnPasswordReset onPasswordReset;
	
	private Button btnResetPassword;
	private EditText edtNewPassword, edtConfPassword,edtCode;

	private InputFieldValidator vaildator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_password_reset_confirm, container, false);
		btnResetPassword = (Button) view.findViewById(R.id.btnActLoginResetPasswd);
		btnResetPassword.setOnClickListener(this);
		edtNewPassword = (EditText) view.findViewById(R.id.edtActLoginNewPassword);
		edtConfPassword = (EditText) view.findViewById(R.id.edtActLoginConfirmPassword);
		edtCode = (EditText) view.findViewById(R.id.edtActLoginResetCode);
		return view;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            onPasswordReset = (OnPasswordReset) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPasswordReset");
        }
    }

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginResetPasswd && validateFields()) {
			onPasswordReset.onResetPasswordButtonClick(edtConfPassword.getText().toString(), edtCode.getText().toString().trim());
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
    	if (edtCode.getText().toString().trim().equals("")) {
    		result = false;
    		message.append(getResources().getString(R.string.g_hint_code).toString());
			message.append("- ");
			message.append(getResources().getString(R.string.g_hint_code).toString());
			message.append(" "+getResources().getString(R.string.msg_validation_county_city_area).toString());
		}
    	if (!result) {
			Utility.showLongToast(getActivity(), message.toString()); 
		}
		return result;    	
    } 
}
