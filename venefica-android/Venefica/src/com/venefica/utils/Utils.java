package com.venefica.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.venefica.activity.R;

public class Utils
{
	public static void StartActivityForResultGetCamera(Activity activity)
	{
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		if (MyApp.UseSdCard)
		{
			//intent.putExtra("crop", "true");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(MyApp.TEMP_FILE));
		}
		activity.startActivityForResult(intent, 0);
	}

	public static void StartActivityForResultGetGallery(Activity activity)
	{
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		if (MyApp.UseSdCard)
		{
			//intent.putExtra("crop", "true");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(MyApp.TEMP_FILE));
		}
		activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_image)), 1);
	}

	public static Bitmap GetImageActivityResult(final Activity activity, final int requestCode, final int resultCode, final Intent data)
	{
		Bitmap result = null;

		if ((requestCode == 0 || requestCode == 1) && resultCode == -1)
		{
			try
			{
				if (MyApp.UseSdCard)
				{
					//result = BitmapUtil.DecodeFile(MyApp.TEMP_FILE, 320);
					result = BitmapUtil.DecodeImagef(BitmapFactory.decodeStream(new FileInputStream(MyApp.TEMP_FILE)), Constants.IMAGE_MAX_SIZE);
				}
				else
				{
					Uri localUri = data.getData();
					String[] arrayOfString = new String[1];
					arrayOfString[0] = "_data";

					Cursor localCursor = activity.managedQuery(localUri, arrayOfString, null, null, null);
					int i = localCursor.getColumnIndexOrThrow("_data");
					localCursor.moveToFirst();

					//result = BitmapUtil.DecodeFile(new File(localCursor.getString(i)), 320);
					result = BitmapUtil.DecodeImagef(BitmapFactory.decodeStream(new FileInputStream(new File(localCursor.getString(i)))), Constants.IMAGE_MAX_SIZE);
				}

				Log.v("New image", "Width: " + result.getWidth());
				Log.v("New image", "Height: " + result.getHeight());
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				result = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				result = null;
			}
		}

		return result;
	}

	public static HttpTransportSE GetServicesTransport(String url)
	{
		if (Constants.USE_SSL_SERVICES_TRANSPORT)
			return new HttpsTransportSE(url);
		else
			return new HttpTransportSE(url);
	}

	public static interface ISearchFilter<T>
	{
		/** true - if the item fits */
		public abstract boolean Filter(T object);
	}

	/**
	 * Search for an item in the list
	 * 
	 * @returnposition of the desired item in the list or -1
	 */
	public static <T> int SearchInList(List<? extends T> list, ISearchFilter<? super T> filter)
	{
		int result = -1;

		for (int i = 0; i < list.size(); i++)
		{
			if (filter.Filter(list.get(i)))
			{
				result = i;
				break;
			}
		}

		return result;
	}
}
