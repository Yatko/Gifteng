/**
 * 
 */
package com.venefica.module.invitation;

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

	/**
	 * Set listener
	 * @param listener
	 */
	public void setOnRequestInvitationConfirmClickListener(
			OnRequestInvitationConfirmClickListener listener) {
		if (listener != null) {
			this.listener = listener;
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
