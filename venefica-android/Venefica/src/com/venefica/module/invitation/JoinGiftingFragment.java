/**
 * 
 */
package com.venefica.module.invitation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;
import com.venefica.module.user.SigninFragment.OnSigninListener;

/**
 * @author avinash
 * Fragment class to show join/user registration fragment
 */
public class JoinGiftingFragment extends SherlockFragment implements OnClickListener{

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnJoinGiftingFragmentListener{
		public void onJoinButtonClick();
	}
	private OnJoinGiftingFragmentListener joinGiftingFragmentListener;
	private Button btnJoin;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_join_user, container, false);
		btnJoin = (Button) view.findViewById(R.id.btnActLoginJoin);
		btnJoin.setOnClickListener(this);
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.joinGiftingFragmentListener = (OnJoinGiftingFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnJoinGiftingFragmentListener");
        }
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActLoginJoin && this.joinGiftingFragmentListener != null) {
			joinGiftingFragmentListener.onJoinButtonClick();
		}		
	}
}
