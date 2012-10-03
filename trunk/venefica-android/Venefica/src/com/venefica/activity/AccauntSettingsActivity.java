package com.venefica.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.venefica.activity.R;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.DisconnectFromNetworkContext;
import com.venefica.services.AsyncServices.GetConnectedSocialNetworksContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.DisconnectFromNetworkResult;
import com.venefica.services.ServicesManager.GetConnectedSocialNetworksResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.services.ServicesManager.eSocialNetworks;
import com.venefica.skining.AccauntSettingsSkinDef;
import com.venefica.skining.AccauntSettingsTemplate;
import com.venefica.utils.MyApp;

public class AccauntSettingsActivity extends ActivityEx
{
	/** The form template */
	private AccauntSettingsTemplate T;
	private List<eSocialNetworks> socialNetList = new ArrayList<eSocialNetworks>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new AccauntSettingsSkinDef(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		ShowLoadingDialog();

		MyApp.AsyncServices.GetConnectedSocialNetworks(new GetConnectedSocialNetworksContext(new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				if (result.SoapResult != SoapRequestResult.Ok)
				{
					ShowInfoDialog(GetStringResource(R.string.get_social_networks_error), FinishRunnable);
				}
				else
				{
					GetConnectedSocialNetworksResult ret = (GetConnectedSocialNetworksResult)result;
					socialNetList = ret.Return;

					T.btnFacebook.setTag(new Boolean(false));
					T.btnTwitter.setTag(new Boolean(false));
					T.btnVk.setTag(new Boolean(false));
					
					for (eSocialNetworks it : socialNetList)
					{
						updateButton(it, true);
					}

					T.toggleUseMiles.setChecked(MyApp.user.useMiles);
					T.toggleUseMiles.setOnCheckedChangeListener(new OnCheckedChangeListener()
					{
						public void onCheckedChanged(CompoundButton paramCompoundButton, boolean checked)
						{
							MyApp.user.useMiles = checked;
						}
					});

					T.btnFacebook.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							Boolean connected = (Boolean)v.getTag();
							if (connected)
								disconnectedSocialNet(eSocialNetworks.facebook);
							else
								connectSocialNet(eSocialNetworks.facebook);
						}
					});

					T.btnTwitter.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							Boolean connected = (Boolean)v.getTag();
							if (connected)
								disconnectedSocialNet(eSocialNetworks.twitter);
							else
								connectSocialNet(eSocialNetworks.twitter);
						}
					});

					T.btnVk.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							Boolean connected = (Boolean)v.getTag();
							if (connected)
								disconnectedSocialNet(eSocialNetworks.vkontakte);
							else
								connectSocialNet(eSocialNetworks.vkontakte);
						}
					});
				}
				return CallbackReturn.Ok;
			}
		}));
	}

	@Override
	public void onBackPressed()
	{
		//TODO save the settings on the server, and then we depart from aktiviti
		super.onBackPressed();
	}

	private void connectSocialNet(final eSocialNetworks network)
	{
		Intent intent = new Intent(this, WebConnectToSocialNetworksActivity.class);
		intent.putExtra(WebConnectToSocialNetworksActivity.SOCIAL_NETWORK, network.toString());
		startActivity(intent);
	}

	private void disconnectedSocialNet(final eSocialNetworks network)
	{
		ShowLoadingDialog();

		MyApp.AsyncServices.DisconnectFromNetwork(new DisconnectFromNetworkContext(network, new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();

				DisconnectFromNetworkResult ret = (DisconnectFromNetworkResult)result;

				switch (ret.SoapResult)
				{
					case Ok:
					{
						ShowInfoDialog(GetStringResource(R.string.disconnect_social_networks_successful));
						updateButton(network, false);
						break;
					}

					case SoapProblem:
						ShowInfoDialog(GetStringResource(R.string.soap_problem));
						break;

					case Fault:
						ShowInfoDialog(GetStringResource(R.string.disconnect_social_networks_error));
						break;
				}

				return CallbackReturn.Ok;
			}
		}));
	}

	private void updateButton(eSocialNetworks network, boolean connected)
	{
		switch (network)
		{
			case facebook:
				if (connected)
				{
					T.btnFacebook.setText(R.string.disconnect_facebook);
					T.btnFacebook.setTag(new Boolean(true));
				}
				else
				{
					T.btnFacebook.setText(R.string.connect_facebook);
					T.btnFacebook.setTag(new Boolean(false));
				}
				break;
			case twitter:
				if (connected)
				{
					T.btnTwitter.setText(R.string.disconnect_twitter);
					T.btnTwitter.setTag(new Boolean(true));
				}
				else
				{
					T.btnTwitter.setText(R.string.connect_twitter);
					T.btnTwitter.setTag(new Boolean(false));
				}
				break;
			case vkontakte:
				if (connected)
				{
					T.btnVk.setText(R.string.disconnect_vk);
					T.btnVk.setTag(new Boolean(true));
				}
				else
				{
					T.btnVk.setText(R.string.connect_vk);
					T.btnVk.setTag(new Boolean(false));
				}
				break;
		}
	}
}
