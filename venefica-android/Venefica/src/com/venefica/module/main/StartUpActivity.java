package com.venefica.module.main;


import java.io.IOException;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.venefica.module.invitation.RequestInvitationFragment;
import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.user.PasswordResetActivity;
import com.venefica.module.user.RegisterUserActivity;
import com.venefica.module.user.SigninFragment;
import com.venefica.module.user.SigninFragment.OnSigninListener;
import com.venefica.module.user.UserRegistrationResultWrapper;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class StartUpActivity extends SherlockFragmentActivity implements OnClickListener, OnSigninListener {

	/**
	 * Tabs
	 */
	private TabHost tabHost;
	private TabManager tabManager;
	private static String TAB_INVITATION =  "invitation", TAB_LOGIN = "log_in";
	
	/**
	 * activity request codes
	 */
	private int REQ_RESET_PASSWORD = 4001;
	
	/**
	 * Shared prefs
	 */
	private SharedPreferences prefs;
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_CUSTOM = 3;
	/**
	 * Constants to identify auth request
	 */
	private final int AUTH_FACEBOOK = 11, AUTH_TWITTER = 12, AUTH_VK = 13
			, AUTH_VENEFICA = 14, CHECK_REGRISTRATION = 15, MODE_GET_USER = 16;
	/**
	 * Auth type
	 */
	private static int AUTH_TYPE = -1;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * userId & password
	 */
	private String userId, password;
	/**
	 * flag processing (working) indicates post listing process is running
	 */
	private boolean uploadingData = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.abs__ab_transparent_light_holo));
        getSupportActionBar().setCustomView(R.layout.view_actionbar_title);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_start_up);
		
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

        tabManager.addTab(tabHost.newTabSpec(TAB_INVITATION).setIndicator(getResources().getString(R.string.g_label_invitation)),
                RequestInvitationFragment.class, null);
        tabManager.addTab(tabHost.newTabSpec(TAB_LOGIN).setIndicator(getResources().getString(R.string.g_label_log_in)),
                SigninFragment.class, null);

        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", tabHost.getCurrentTabTag());
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if(id == D_ERROR) {
			//Display error message as per the error code
			if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.error_network_01));
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.error_network_02));
			}else if(ERROR_CODE == Constants.ERROR_RESULT_USER_AUTH){
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.error_social_auth));
			}else if(ERROR_CODE == Constants.ERROR_USER_UNAUTHORISED){
				((AlertDialog) dialog).setMessage(getResources().getText(R.string.msg_invalid_user_password));
			}
		}    	
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		//Create progress dialog
		if(id == D_PROGRESS){
			ProgressDialog pDialog = new ProgressDialog(StartUpActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
		//Create error dialog
		if(id == D_ERROR){
			AlertDialog.Builder builder = new AlertDialog.Builder(StartUpActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}    	 	
		return null;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (!uploadingData) {
			
		}
	}

	/**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final SherlockFragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private SherlockFragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(SherlockFragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = (SherlockFragment) mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = (SherlockFragment) SherlockFragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }

	@Override
	public void onSigninButtonClick(boolean rememberUser, String userId, String password) {
		if (!uploadingData) {
			if (WSAction.isNetworkConnected(this)) {
				this.userId = userId;
				this.password = password;
				//Call web service to authenticate user
				new AuthenticationTask().execute(AUTH_VENEFICA + "");
				//remember user pass
				rememberUser(rememberUser, userId, password);
			} else {
				ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
				showDialog(D_ERROR);
			}
		}
	}

	@Override
	public void onForgotPasswordButtonClick() {
		if (!uploadingData) {
			Intent forgotPassIntent = new Intent(this,
					PasswordResetActivity.class);
			forgotPassIntent.putExtra("act_mode",
					PasswordResetActivity.ACT_MODE_VERIFY_EMAIL);
			startActivityForResult(forgotPassIntent, REQ_RESET_PASSWORD);
		}		
	}	
	
	/**
	 * Start home screen of application
	 */
	private void startHomeScreen(){
		Intent intent = new Intent(this, SearchListingsActivity.class);
		intent.putExtra("act_mode", SearchListingsActivity.ACT_MODE_SEARCH_BY_CATEGORY);
		startActivity(intent);
		finish();
	}
	/**
	 * Method to store authToken
	 */
	private void saveAuthToken(String authToken){
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.PREFERENCES_AUTH_TOKEN, authToken);
		editor.commit();
	}
	
	/**
	 * Method to store user password when remember me is checked
	 */
	private void rememberUser(boolean rememberUser, String userId, String password){
		if(prefs == null){
			prefs = getSharedPreferences(Constants.VENEFICA_PREFERENCES, Activity.MODE_PRIVATE);
		}
		SharedPreferences.Editor editor = prefs.edit();
		if (rememberUser) {
			editor.putString(Constants.PREF_KEY_LOGIN_TYPE, Constants.PREF_VAL_LOGIN_VENEFICA);
			editor.putString(Constants.PREF_KEY_LOGIN, userId);
			editor.putString(Constants.PREF_KEY_PASSWORD, password);
		} else {
			editor.putString(Constants.PREF_KEY_LOGIN, "");
			editor.putString(Constants.PREF_KEY_PASSWORD, "");
		}        
		editor.commit();
	}
	
	/**
	 * @author avinash
	 *	Class to perform login task
	 */
	class AuthenticationTask extends AsyncTask<String, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			uploadingData = true;
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected UserRegistrationResultWrapper doInBackground(String... params) {
			UserRegistrationResultWrapper wrapper = null;
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equalsIgnoreCase(AUTH_VENEFICA+"")) {
					wrapper = wsAction.authenticateUser(userId, password);					
				}else if (params[0].equalsIgnoreCase(CHECK_REGRISTRATION+"")) {
					wrapper = wsAction.checkUserRegistration(params[1]);
				}else if (params[0].equalsIgnoreCase(MODE_GET_USER+"")) {
					wrapper = new UserRegistrationResultWrapper();
					wrapper.userDto = wsAction.getUser(((VeneficaApplication)getApplication()).getAuthToken());
					if (wrapper.userDto == null) {//change after modifying Dto
						wrapper = null;
					}
				}
			} catch (IOException e) {
				Log.e("AuthenticationTask::doInBackground :", e.toString());
			} catch (XmlPullParserException e) {
				Log.e("AuthenticationTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(UserRegistrationResultWrapper result) {
			super.onPostExecute(result);
			uploadingData = false;
			setSupportProgressBarIndeterminateVisibility(false);
			if(result != null){
				if (result != null && result.result == Constants.RESULT_USER_AUTHORISED) {
					saveAuthToken(result.data);
					((VeneficaApplication)getApplication()).setAuthToken(result.data);
					//Call web service to authenticate user
					new AuthenticationTask().execute(MODE_GET_USER+"", result.data);
				}else if (result != null && result.result == Constants.ERROR_USER_UNAUTHORISED) {
					ERROR_CODE = result.result;
					showDialog(D_ERROR);
				}else if (result != null &&   result.result == Constants.RESULT_IS_USER_EXISTS_SUCCESS) {
					if(result.data.contains("false")){
						Intent intent = new Intent(StartUpActivity.this, RegisterUserActivity.class);
						intent.putExtra("activity_mode",RegisterUserActivity.MODE_UPDATE_PROF);
						if(AUTH_TYPE == AUTH_FACEBOOK){
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_FACEBOOK);
						}else if (AUTH_TYPE == AUTH_TWITTER) {
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_TWITTER);
						}else if (AUTH_TYPE == AUTH_VK) {
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_VK);
						}else if (AUTH_TYPE == AUTH_VENEFICA) {
							intent.putExtra("auth_mode",RegisterUserActivity.ACT_REGISTER_BY_VENEFICA);
						}
						startActivityForResult(intent, 0);
					}else{
						startHomeScreen();
					}
				}else if (result != null && result.userDto != null) {
					((VeneficaApplication)getApplication()).setUser(result.userDto);
					startHomeScreen();
				}else{
					ERROR_CODE  = Constants.ERROR_NETWORK_CONNECT;
					showDialog(D_ERROR);
				}				
			}else{
				ERROR_CODE  = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}
		}
	}
}
