package com.venefica.activity;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.venefica.activity.R;
import com.venefica.activity.PostActivity.Step;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.MyApp;

public class PostLocateLogic implements PostStepLogic
{
	PostActivity mActivity;

	Button btnMyLocation;
	MapView Map;
	MapController Controller;
	LinearLayout post_locate_instructions;
	TextView lblAddress;

	GeoPoint loc;
	Bitmap MapPreview;

	boolean WaitAdress = false;

	class UpdateAdressTask extends AsyncTask<Location, Void, String>
	{
		@Override
		protected String doInBackground(Location... location)
		{
			String result;
			Geocoder coder = new Geocoder(mActivity);
			try
			{
				List<Address> ResultList = coder.getFromLocation(location[0].getLatitude(), location[0].getLongitude(), 1);
				if (ResultList.size() > 0)
				{
					result = ResultList.get(0).getAddressLine(0);
				}
				else
				{
					result = mActivity.GetStringResource(R.string.unknown_address);
				}
			}
			catch (IOException e)
			{
				result = mActivity.GetStringResource(R.string.unknown_address);
				Log.d("UpdateAdressTask.doInBackground IOException:", e.getLocalizedMessage());
			}
			catch (Exception e)
			{
				result = mActivity.GetStringResource(R.string.unknown_address);
				Log.d("UpdateAdressTask.doInBackground Exception:", e.getLocalizedMessage());
			}

			return result;
		}

		protected void onPostExecute(String result)
		{
			lblAddress.setText(result);
			WaitAdress = false;
		}
	}

	public void Commit()
	{
		if (mActivity != null && mActivity.Post != null)
		{
			CreateMapPreview();
			mActivity.Post.GeoLocation = loc;
			mActivity.Post.MapPreview = MapPreview;
		}
	}

	public void OnDisplay(PostActivity activity)
	{
		mActivity = activity;
		mActivity.T.PostStepContainer.setDisplayedChild(Step.Locate);

		btnMyLocation = (Button)mActivity.findViewById(R.id.btnMyLocation);
		Map = (MapView)mActivity.findViewById(R.id.MyMapView);
		Controller = Map.getController();
		post_locate_instructions = (LinearLayout)mActivity.findViewById(R.id.post_locate_instructions);
		lblAddress = (TextView)mActivity.findViewById(R.id.lblAddress);

		btnMyLocation.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				if (MyApp.MyLocation != null)
				{
					loc = GeoLocation.LocToGeo(MyApp.MyLocation);
					UpdateMapLocation();
				}
			}
		});

		Map.setOnTouchListener(new View.OnTouchListener()
		{
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
			{
				if (paramMotionEvent.getAction() == 1)
				{
					loc = GetMapCenter();
					UpdateAddressDisplayed();
				}
				return false;
			}
		});
	}

	public void UpdateUI(PostData Post)
	{
		if (Post == null)
		{
			Log.d("PostLocateLogic.UpdateUI", "Post==null");
			return;
		}

		if (Post.GeoLocation == null)
		{
			if (MyApp.MyLocation != null)
			{
				loc = GeoLocation.LocToGeo(MyApp.MyLocation);
			}
		}
		else
		{
			loc = Post.GeoLocation;
		}

		UpdateMapLocation();
	}

	public boolean ValidateAndCommit()
	{
		Commit();
		return true;
	}

	void UpdateMapLocation()
	{
		if (loc != null)
		{
			Controller.animateTo(loc);
			Controller.setZoom(18);
			Map.invalidate();
			UpdateAddressDisplayed();
		}
	}

	public void CreateMapPreview()
	{
		Map.setWillNotCacheDrawing(false);
		Map.destroyDrawingCache();
		Map.buildDrawingCache(true);

		Bitmap image = Bitmap.createBitmap(Map.getDrawingCache(true));
		Canvas canvas = new Canvas(image);
		View overlayView = mActivity.findViewById(R.id.map_overlay);

		SetVisibilityOfMapInteractionComponents(false);

		overlayView.buildDrawingCache(true);
		canvas.drawBitmap(overlayView.getDrawingCache(true), 0.0F, 0.0F, null);
		overlayView.destroyDrawingCache();

		Map.setWillNotCacheDrawing(true);
		SetVisibilityOfMapInteractionComponents(true);

		MapPreview = image;
	}

	private void SetVisibilityOfMapInteractionComponents(boolean Visibility)
	{
		if (Visibility)
		{
			post_locate_instructions.setVisibility(View.VISIBLE);
			btnMyLocation.setVisibility(View.VISIBLE);
		}
		else
		{
			post_locate_instructions.setVisibility(View.INVISIBLE);
			btnMyLocation.setVisibility(View.INVISIBLE);
		}
	}

	public void UpdateAddressDisplayed()
	{
		if (WaitAdress == false)
		{
			WaitAdress = true;
			new UpdateAdressTask().execute(GeoLocation.GeoToLoc(loc));
		}
	}

	public GeoPoint GetMapCenter()
	{
		int i = Map.getWidth() / 2;
		int j = Map.getHeight() / 2;
		return Map.getProjection().fromPixels(i, j);
	}
}
