package com.venefica.activity;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.venefica.activity.R;
import com.venefica.skining.EditAdLocationSkinDef;
import com.venefica.skining.EditAdLocationTemplate;
import com.venefica.utils.GeoLocation;
import com.venefica.utils.MyApp;

public class EditAdLocationActivity extends MapActivityEx
{
	EditAdLocationTemplate T;

	MapController Controller;
	boolean WaitAdress = false;
	boolean NeedRecreatePreview = true;

	class UpdateAdressTask extends AsyncTask<Location, Void, String>
	{
		@Override
		protected String doInBackground(Location... location)
		{
			String result;
			Geocoder coder = new Geocoder(EditAdLocationActivity.this);
			try
			{
				List<Address> ResultList = coder.getFromLocation(location[0].getLatitude(), location[0].getLongitude(), 1);
				if (ResultList.size() > 0)
				{
					result = ResultList.get(0).getAddressLine(0);
				}
				else
				{
					result = GetStringResource(R.string.unknown_address);
				}
			}
			catch (IOException e)
			{
				result = GetStringResource(R.string.unknown_address);
				Log.d("UpdateAdressTask.doInBackground IOException:", e.getLocalizedMessage());
			}
			catch (Exception e)
			{
				result = GetStringResource(R.string.unknown_address);
				Log.d("UpdateAdressTask.doInBackground Exception:", e.getLocalizedMessage());
			}

			return result;
		}

		protected void onPostExecute(String result)
		{
			T.lblAddress.setText(result);
			WaitAdress = false;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new EditAdLocationSkinDef(this);
		HideKeyboard();

		Controller = T.Map.getController();

		T.btnMyLocation.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View paramView)
			{
				if (MyApp.MyLocation != null)
				{
					TabAdEditActivity.Item.Location = GeoLocation.LocToGeo(MyApp.MyLocation);
					UpdateMapLocation();
				}
			}
		});

		T.Map.setOnTouchListener(new View.OnTouchListener()
		{
			public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
			{
				if (paramMotionEvent.getAction() == 1)
				{
					TabAdEditActivity.Item.Location = GetMapCenter();
					UpdateAddressDisplayed();
					NeedRecreatePreview = true;
				}
				return false;
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		CreateMapPreview();
	}

	void UpdateMapLocation()
	{
		if (TabAdEditActivity.Item.Location != null)
		{
			Controller.animateTo(TabAdEditActivity.Item.Location);
			Controller.setZoom(18);
			T.Map.invalidate();
			UpdateAddressDisplayed();
		}
	}

	public void CreateMapPreview()
	{
		if (NeedRecreatePreview == false)
			return;

		T.Map.setWillNotCacheDrawing(false);
		T.Map.destroyDrawingCache();
		T.Map.buildDrawingCache(true);

		Bitmap image = Bitmap.createBitmap(T.Map.getDrawingCache(true));
		Canvas canvas = new Canvas(image);
		View overlayView = findViewById(R.id.map_overlay);

		SetVisibilityOfMapInteractionComponents(false);

		overlayView.buildDrawingCache(true);
		canvas.drawBitmap(overlayView.getDrawingCache(true), 0.0F, 0.0F, null);
		overlayView.destroyDrawingCache();

		T.Map.setWillNotCacheDrawing(true);
		SetVisibilityOfMapInteractionComponents(true);

		TabAdEditActivity.MapPreview = image;
		NeedRecreatePreview = false;
	}

	private void SetVisibilityOfMapInteractionComponents(boolean Visibility)
	{
		if (Visibility)
		{
			T.post_locate_instructions.setVisibility(View.VISIBLE);
			T.btnMyLocation.setVisibility(View.VISIBLE);
		}
		else
		{
			T.post_locate_instructions.setVisibility(View.INVISIBLE);
			T.btnMyLocation.setVisibility(View.INVISIBLE);
		}
	}

	public void UpdateAddressDisplayed()
	{
		if (WaitAdress == false)
		{
			WaitAdress = true;
			new UpdateAdressTask().execute(GeoLocation.GeoToLoc(TabAdEditActivity.Item.Location));
		}
	}

	public GeoPoint GetMapCenter()
	{
		int i = T.Map.getWidth() / 2;
		int j = T.Map.getHeight() / 2;
		return T.Map.getProjection().fromPixels(i, j);
	}
}
