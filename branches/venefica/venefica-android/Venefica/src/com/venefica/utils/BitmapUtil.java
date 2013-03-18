package com.venefica.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class BitmapUtil
{
	private static int ComputeCorrectScaleValue(int MaxSize, BitmapFactory.Options options)
	{
		int i = options.outWidth;
		int j = options.outHeight;
		int k = 1;
		while (true)
		{
			if ((i / 2 < MaxSize) || (j / 2 < MaxSize))
				return k;
			i /= 2;
			j /= 2;
			k *= 2;
		}
	}

	private static BitmapFactory.Options DecodeImageSize(File FileImage) throws FileNotFoundException
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(new FileInputStream(FileImage), null, options);

		return options;
	}

	private static BitmapFactory.Options BitmapDecodeOptions(int paramInt)
	{
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inDither = false;
		localOptions.inSampleSize = paramInt;
		localOptions.inScaled = false;
		localOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return localOptions;
	}

	@Deprecated
	public static Bitmap DecodeFile(File FileName, int MaxSize)
	{
		Bitmap result = null;
		try
		{
			int i = ComputeCorrectScaleValue(MaxSize, DecodeImageSize(FileName));
			result = BitmapFactory.decodeStream(new FileInputStream(FileName), null, BitmapDecodeOptions(i));
		}
		catch (FileNotFoundException e)
		{
			Log.d("DecodeFile FileNotFoundException:", e.getLocalizedMessage());
		}
		catch (Exception e)
		{
			Log.d("DecodeFile Exception:", e.getLocalizedMessage());
		}

		return result;
	}

	@Deprecated
	public static Bitmap DecodeImage(Bitmap image, int MaxSize)
	{
		Bitmap result = null;
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.outWidth = image.getWidth();
			options.outHeight = image.getHeight();
			float i = (float)ComputeCorrectScaleValue(MaxSize, options);

			Matrix matrix = new Matrix();
			matrix.postScale(i, i);
			result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
		}
		catch (Exception e)
		{
			Log.d("DecodeFile Exception:", e.getLocalizedMessage());
		}

		return result;
	}

	public static Bitmap DecodeImagef(Bitmap image, int MaxSize)
	{
		Bitmap result = null;
		result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), GetMatrixScale(image, MaxSize), false);
		return result;
	}
	
	public static Bitmap DecodeImageSquare(Bitmap image, int MaxSize)
	{
		Bitmap result = null;		
		
		result = Bitmap.createBitmap(MaxSize, MaxSize, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(result);

		int w = image.getWidth();
		int h = image.getHeight();
		
		Rect srcRect;
		if (w>h)
		{
			int wSize = h;
			int hSize = h;
			
			srcRect = new Rect((w-wSize)/2, (h-hSize)/2, (w+wSize)/2, (h+hSize)/2);
		}
		else
		{
			int wSize = w;
			int hSize = w;
			
			srcRect = new Rect((w-wSize)/2, (h-hSize)/2, (w+wSize)/2, (h+hSize)/2);
		}
		
		Rect dstRect = new Rect(0, 0, MaxSize, MaxSize);

		int dx = (srcRect.width() - dstRect.width()) / 2;
		int dy = (srcRect.height() - dstRect.height()) / 2;

		// If the srcRect is too big, use the center part of it.
		srcRect.inset(Math.max(0, dx), Math.max(0, dy));

		// If the dstRect is too big, use the center part of it.
		dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

		// Draw the cropped bitmap in the center
		canvas.drawBitmap(image, srcRect, dstRect, null);
		
		return result;
	}

	public static Matrix GetMatrixScale(Bitmap image, int MaxSize)
	{
		Matrix matrix = new Matrix();

		float w = image.getWidth();
		float h = image.getHeight();

		float scale = 1.0f;
		if (w >= h)
		{//scale w
			scale = (float)MaxSize / w;
		}
		else
		{//scale h
			scale = (float)MaxSize / h;
		}

		matrix.postScale(scale, scale);

		return matrix;
	}
	
	public static Matrix GetMatrixScaleSquare(Bitmap image, int MaxSize)
	{
		Matrix matrix = new Matrix();

		float w = image.getWidth();
		float h = image.getHeight();

		float scaleW = (float)MaxSize / w;
		float scaleH = (float)MaxSize / h;

		matrix.postScale(scaleW, scaleH);

		return matrix;
	}
}
