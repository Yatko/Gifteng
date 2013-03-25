package com.venefica.module.invitation;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.venefica.module.invitation.RequestInvitationConfirmFragment.OnRequestInvitationConfirmClickListener;
import com.venefica.module.main.R;

public class InvitationActivity extends SherlockFragmentActivity implements OnRequestInvitationConfirmClickListener{
	
	/**
	 * child Fragments
	 */
	private RequestInvitationConfirmFragment reqInvConfirmFragment;
	
	public static int ACT_MODE_CONF_INVITATION_REQ = 4001;
	public static int ACT_MODE_INVITATION_REQ_SUCCESS = 4002;
	public static int ACT_MODE_VERIFY_INVTATION = 4003;
	private int ACT_MODE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
		setContentView(R.layout.activity_invitation);
		
		ACT_MODE = getIntent().getExtras().getInt("act_mode", ACT_MODE_CONF_INVITATION_REQ);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		reqInvConfirmFragment = new RequestInvitationConfirmFragment();
		reqInvConfirmFragment.setOnRequestInvitationConfirmClickListener(this);
		fragmentTransaction.add(R.id.layActInvitationRoot, reqInvConfirmFragment);
		fragmentTransaction.commit();
	}
	@Override
	public void onConfirmRequest() {
		RequestInvitationSuccessFragment requestInvitationSuccessFragment = new RequestInvitationSuccessFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layActInvitationRoot, requestInvitationSuccessFragment).commit();		
	}
}
