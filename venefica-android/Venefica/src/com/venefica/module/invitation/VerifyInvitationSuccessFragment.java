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

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;

/**
 * @author avinash
 * Fragment class to show verify invitation success message and join button
 */
public class VerifyInvitationSuccessFragment extends SherlockFragment implements OnClickListener{
	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnVerifyInvitationSuccessClickListener{
		public void OnGotoJoinClick();
	}
	private OnVerifyInvitationSuccessClickListener invitationSuccessClickListener;
	
	/**
	 * button and edit text
	 */
	private Button btnGotoJoin;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_verify_invitation_success, container, false);
		btnGotoJoin = (Button) view.findViewById(R.id.btnActLoginGotoJoin);
		btnGotoJoin.setOnClickListener(this);
		return view;
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	invitationSuccessClickListener = (OnVerifyInvitationSuccessClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVerifyInvitationSuccessClickListener");
        }
    }
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginGotoJoin && this.invitationSuccessClickListener != null) {
			this.invitationSuccessClickListener.OnGotoJoinClick();
		}		
	}
}
