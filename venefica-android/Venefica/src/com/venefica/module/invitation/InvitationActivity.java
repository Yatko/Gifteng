package com.venefica.module.invitation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.venefica.module.invitation.JoinGiftingFragment.OnJoinGiftingFragmentListener;
import com.venefica.module.invitation.RequestInvitationConfirmFragment.OnRequestInvitationConfirmClickListener;
import com.venefica.module.invitation.VerifyInvitationFragment.OnVerifyInvitationClickListener;
import com.venefica.module.invitation.VerifyInvitationSuccessFragment.OnVerifyInvitationSuccessClickListener;
import com.venefica.module.main.R;

public class InvitationActivity extends SherlockFragmentActivity 
	implements OnRequestInvitationConfirmClickListener, OnVerifyInvitationClickListener,
		OnJoinGiftingFragmentListener, OnVerifyInvitationSuccessClickListener{
	
	/**
	 * child Fragments
	 */
	private RequestInvitationConfirmFragment reqInvConfirmFragment;
	private VerifyInvitationFragment verifyInvitationFragment;
	
	public static int ACT_MODE_CONF_INVITATION_REQ = 4001;
	public static int ACT_MODE_INVITATION_REQ_SUCCESS = 4002;
	public static int ACT_MODE_VERIFY_INVTATION = 4003;
	public static int ACT_MODE_VERIFY_INVTATION_SUCCESS = 4004;
	public static int ACT_MODE_JOIN_GIFTENG = 4005;
	private int ACT_MODE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
		setContentView(R.layout.activity_invitation);
		
		ACT_MODE = getIntent().getExtras().getInt("act_mode", ACT_MODE_CONF_INVITATION_REQ);
		//add fragment as per mode
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		if (ACT_MODE == ACT_MODE_CONF_INVITATION_REQ) {
			reqInvConfirmFragment = new RequestInvitationConfirmFragment();
			fragmentTransaction.add(R.id.layActInvitationRoot, reqInvConfirmFragment);
		} else if (ACT_MODE == ACT_MODE_VERIFY_INVTATION){
			verifyInvitationFragment = new VerifyInvitationFragment();
			fragmentTransaction.add(R.id.layActInvitationRoot, verifyInvitationFragment);
		}
		
		fragmentTransaction.commit();
	}
	@Override
	public void onConfirmRequest() {
		RequestInvitationSuccessFragment requestInvitationSuccessFragment = new RequestInvitationSuccessFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layActInvitationRoot, requestInvitationSuccessFragment).commit();		
	}
	@Override
	public void OnVerifyInvitation(String invitationCode) {
		VerifyInvitationSuccessFragment invitationSuccessFragment = new VerifyInvitationSuccessFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layActInvitationRoot, invitationSuccessFragment).commit();
	}
	@Override
	public void onJoinButtonClick() {
		
	}
	@Override
	public void OnGotoJoinClick() {
		JoinGiftingFragment joinGiftingFragment = new JoinGiftingFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.layActInvitationRoot, joinGiftingFragment).commit();
	}
}
