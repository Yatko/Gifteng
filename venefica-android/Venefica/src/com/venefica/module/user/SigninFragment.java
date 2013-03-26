package com.venefica.module.user;

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
		public void onSigninButtonClick();
		public void onForgotPasswordButtonClick();
	}
	private OnSigninListener onSigninListener;
	
	private Button btnForgotPassword, btnLogin;
	private EditText edtId, edtPassword;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_sign_in, container, false);
		btnForgotPassword = (Button) view.findViewById(R.id.btnActLoginLogin);
		btnForgotPassword.setOnClickListener(this);
		btnLogin = (Button) view.findViewById(R.id.btnActLoginForgotPassword);
		btnLogin.setOnClickListener(this);
		edtId = (EditText) view.findViewById(R.id.edtActLoginEmailPhone);
		edtPassword = (EditText) view.findViewById(R.id.edtActLoginPassword);
		return view;
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
			onSigninListener.onSigninButtonClick();
		} else if (id == R.id.btnActLoginForgotPassword && onSigninListener != null){
			onSigninListener.onForgotPasswordButtonClick();
		}
	}
}
