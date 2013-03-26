package com.venefica.module.invitation;

import android.content.Intent;
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
 * Fragment class for request invitation layout
 */
public class RequestInvitationFragment extends SherlockFragment implements OnClickListener{
		
	/**
	 * Buttons
	 */
	private Button btnReqInvite, btnHaveInvite;
	private EditText edtEmail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_request_invitation, container, false);
		btnReqInvite = (Button) view.findViewById(R.id.btnActLoginRequestInvite);
		btnReqInvite.setOnClickListener(this);
		btnHaveInvite = (Button) view.findViewById(R.id.btnActLoginHaveInviteCode);
		btnHaveInvite.setOnClickListener(this);
		
		edtEmail = (EditText) view.findViewById(R.id.edtActLoginReqInvEmail);
		return view;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginRequestInvite) {
			Intent invitationIntent = new Intent(getSherlockActivity(), InvitationActivity.class);
			invitationIntent.putExtra("email", edtEmail.getText().toString());
			invitationIntent.putExtra("act_mode", InvitationActivity.ACT_MODE_CONF_INVITATION_REQ);
			startActivity(invitationIntent);					
		} else if (id == R.id.btnActLoginHaveInviteCode){
			Intent invitationIntent = new Intent(getSherlockActivity(), InvitationActivity.class);
			invitationIntent.putExtra("act_mode", InvitationActivity.ACT_MODE_VERIFY_INVTATION);
			startActivity(invitationIntent);
		}
	}
}
