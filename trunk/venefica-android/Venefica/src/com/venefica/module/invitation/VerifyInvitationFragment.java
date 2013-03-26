/**
 * 
 */
package com.venefica.module.invitation;

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
 * Fragment class to show verify invitation layout
 */
public class VerifyInvitationFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnVerifyInvitationClickListener{
		public void OnVerifyInvitation(String invitationCode);
	}
	private OnVerifyInvitationClickListener invitationClickListener;
	
	/**
	 * button and edit text
	 */
	private Button btnVerify;
	private EditText edtInvitationCode;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_verify_invitation, container, false);
		btnVerify = (Button) view.findViewById(R.id.btnActLoginVerifyInvite);
		btnVerify.setOnClickListener(this);
		edtInvitationCode = (EditText) view.findViewById(R.id.edtActLoginInvitationCode);
		return view;
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            invitationClickListener = (OnVerifyInvitationClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVerifyInvitationClickListener");
        }
    }
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginVerifyInvite && this.invitationClickListener != null) {
			this.invitationClickListener.OnVerifyInvitation(edtInvitationCode.getText().toString());
		}		
	}
}
