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
 *  Fragment class for confirm request invitation layout
 */
public class RequestInvitationConfirmFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash Listener to communicate with activity
	 */
	public interface OnRequestInvitationConfirmClickListener {
		public void onConfirmRequest();
	}

	/**
	 * listener object
	 */
	private OnRequestInvitationConfirmClickListener listener;

	/**
	 * Button
	 */
	private Button btnConfirmRequest;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//set layout for fragment
		View view = inflater.inflate(R.layout.view_confirm_invitation_request,
				container, false);
		btnConfirmRequest = (Button) view.findViewById(R.id.btnActLoginConfirmReq);
		btnConfirmRequest.setOnClickListener(this);
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
			this.listener.onConfirmRequest();
		}		
	}
}
