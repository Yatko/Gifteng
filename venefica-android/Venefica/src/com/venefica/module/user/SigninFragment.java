package com.venefica.module.user;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Fragment to show signin layout
 */
public class SigninFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnSigninListener{
		public void onSigninButtonClick(boolean rememberUser, String userId, String password);
		public void onForgotPasswordButtonClick();
	}
	private OnSigninListener onSigninListener;
	
	private Button btnForgotPassword, btnLogin;
	private EditText edtLogin, edtPassword;
	
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Field validator
	 */
	private InputFieldValidator validator;
	/**
	 * Checkbox remember me
	 */
	private CheckBox chkRemember;
	/**
	 * Shared prefs
	 */
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Check if session is valid
		prefs = getActivity().getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_sign_in, container, false);
		btnForgotPassword = (Button) view.findViewById(R.id.btnActLoginLogin);
		btnForgotPassword.setOnClickListener(this);
		btnLogin = (Button) view.findViewById(R.id.btnActLoginForgotPassword);
		btnLogin.setOnClickListener(this);
		edtLogin = (EditText) view.findViewById(R.id.edtActLoginEmailPhone);
		edtPassword = (EditText) view.findViewById(R.id.edtActLoginPassword);
		chkRemember = (CheckBox) view.findViewById(R.id.chkActLoginRememberMe);
		return view;
	}
	
	@Override
	public void onStart() {		
		super.onStart();
		if((System.currentTimeMillis() - prefs.getLong(Constants.PREFERENCES_SESSION_IN_TIME, 0)) < Constants.SESSION_TIME_OUT 
				&& !prefs.getString(Constants.PREFERENCES_AUTH_TOKEN, "").equals("")){	

			((VeneficaApplication)getActivity().getApplication()).setAuthToken(prefs.getString(Constants.PREFERENCES_AUTH_TOKEN, ""));
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong(Constants.PREFERENCES_SESSION_IN_TIME, System.currentTimeMillis());
			editor.commit();
		}else if(prefs.getString(Constants.PREF_KEY_LOGIN_TYPE, "").equals(Constants.PREF_VAL_LOGIN_VENEFICA)){
			edtLogin.setText(prefs.getString(Constants.PREF_KEY_LOGIN, ""));
			edtPassword.setText(prefs.getString(Constants.PREF_KEY_PASSWORD, ""));
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.onSigninListener = (OnSigninListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSigninListener");
        }
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginLogin && onSigninListener != null) {
			if(validator == null){
				validator = new InputFieldValidator();
			}
			//Empty field check
			if (edtLogin.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
				Utility.showShortToast(getActivity(), getResources().getString(R.string.msg_empty_user_password)
						+" "+getResources().getString(R.string.hint_user_password_pattern));
			}else if(!validateInput()){
				//Validate fields
				Utility.showShortToast(getActivity(), getResources().getString(R.string.hint_user_password_pattern));
			}else{
				onSigninListener.onSigninButtonClick(chkRemember.isChecked()
						, edtLogin.getText().toString(), edtPassword.getText().toString());
			}
		} else if (id == R.id.btnActLoginForgotPassword && onSigninListener != null){
			onSigninListener.onForgotPasswordButtonClick();
		}
	}
	
	/**
	 * Method to validate id and password
	 * @return validation result
	 */
	private boolean validateInput(){
		if((validator.validateField(edtLogin, Pattern.compile(InputFieldValidator.phonePatternRegx)) 
				|| (validator.validateField(edtLogin, Pattern.compile(InputFieldValidator.emailPatternRegx)))
				&& validator.validateField(edtPassword, Pattern.compile(InputFieldValidator.charNumPatternRegx)))){
			return true;
		}
		return false;
	}
}
