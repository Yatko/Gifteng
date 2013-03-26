/**
 * 
 */
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
 * Fragment class to show reset new password layout
 */
public class PasswordResetFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnPasswordReset{
		public void onResetPasswordButtonClick(String password);
	}
	private OnPasswordReset onPasswordReset;
	
	private Button btnResetPassword;
	private EditText edtNewPassword, edtConfPassword;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_password_reset_confirm, container, false);
		btnResetPassword = (Button) view.findViewById(R.id.btnActLoginResetPasswd);
		btnResetPassword.setOnClickListener(this);
		edtNewPassword = (EditText) view.findViewById(R.id.edtActLoginNewPassword);
		edtConfPassword = (EditText) view.findViewById(R.id.edtActLoginConfirmPassword);
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
		if (id == R.id.btnActLoginResetPasswd) {
			onPasswordReset.onResetPasswordButtonClick(edtConfPassword.getText().toString());
		}
	}
}
