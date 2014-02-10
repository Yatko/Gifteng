/**
 * 
 */
package com.venefica.module.listings.post;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.venefica.module.main.R;

/**
 * Dialog fragment to show edit listing options
 * @author avinash
 *
 */
public class UpdateListingDialogFragment extends SherlockDialogFragment implements OnClickListener {

	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface UpdateListingDialogListener{
		/**
		 * method to display update UI on update button click
		 */
		public void onUpdate();
		/**
		 * method to handle cancel button action
		 */
		public void onCancel();
	}
	private UpdateListingDialogListener updateListingDialogListener;
	
	/**
	 * fields
	 */
	Button btnUpdate, btnCancel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_update_listing_options_dialog, container, false);
		getDialog().setTitle(getResources().getString(R.string.label_update_listing));
		btnUpdate = (Button) view.findViewById(R.id.btnActPostListingUpdate);
		btnUpdate.setOnClickListener(this);
		btnCancel = (Button) view.findViewById(R.id.btnActPostListingCancel);
		btnCancel.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.updateListingDialogListener = (UpdateListingDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement UpdateListingDialogListener");
        }
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActPostListingUpdate) {
			this.updateListingDialogListener.onUpdate();
		} else if (id == R.id.btnActPostListingCancel) {
			this.updateListingDialogListener.onCancel();
		}
	}
}
