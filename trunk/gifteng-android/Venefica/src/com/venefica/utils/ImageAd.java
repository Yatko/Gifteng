package com.venefica.utils;

import android.graphics.Bitmap;

public class ImageAd
{
	public final long id;
	/** Validity picture link */
	public final String url;
	public final Bitmap bitmap;

	public ImageAd(long id, String url, Bitmap bitmap)
	{
		this.id = id;
		this.url = url;
		this.bitmap = bitmap;
	}
}
