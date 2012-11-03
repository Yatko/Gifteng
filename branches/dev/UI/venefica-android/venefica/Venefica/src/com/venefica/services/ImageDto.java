package com.venefica.services;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.venefica.utils.BitmapUtil;
import com.venefica.utils.Constants;

public class ImageDto implements KvmSerializable
{
	enum imageType
	{
		JPEG, PNG
	}

	public String data = "";
	public imageType imgType;
	public String url = "";
	public long id;

	public ImageDto()
	{

	}

	public ImageDto(Bitmap image)
	{
		if (image == null)
			return;

		imgType = imageType.JPEG;

		//to jpeg
		ByteArrayOutputStream FinalImageByteArray = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, Constants.JPEG_COMPRESS_QUALITY, FinalImageByteArray);

		data = Base64.encodeToString(FinalImageByteArray.toByteArray(), 0);
		Log.i("imageDto", "data size - " + (float)data.length() / 1024.0f + " kb");
	}

	public ImageDto(Bitmap image, boolean Thumbnails)
	{
		if (image == null && Thumbnails == false)
			return;

		imgType = imageType.JPEG;

		//scale
		Bitmap tmpImage = BitmapUtil.DecodeImagef(image, Constants.IMAGE_THUMBNAILS_MAX_SIZE);
		
		//to jpeg
		ByteArrayOutputStream FinalImageByteArray = new ByteArrayOutputStream();
		tmpImage.compress(Bitmap.CompressFormat.JPEG, Constants.JPEG_COMPRESS_QUALITY, FinalImageByteArray);

		data = Base64.encodeToString(FinalImageByteArray.toByteArray(), 0);
		Log.i("imageDto", "data size - " + (float)data.length() / 1024.0f + " kb");
	}

	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return data;
			case 1:
				if (imgType != null)
					return imgType.name();
				else
					return null;
			case 2:
				return url;
			case 3:
				return id;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 4;
	}

	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info)
	{
		switch (index)
		{
			case 0:
				info.name = "data";
				info.type = new byte[0].getClass();
				break;

			case 1:
				info.name = "imgType";
				info.type = String.class;
				break;

			case 2:
				info.name = "url";
				info.type = String.class;
				break;
				
			case 3:
				info.name = "id";
				info.type = Long.class;
				break;

			default:
				break;
		}
	}

	public void setProperty(int index, Object value)
	{
		switch (index)
		{
			case 0:
				data = value.toString();
				break;
			case 1:
				imgType = imageType.valueOf(value.toString());
				break;
			case 2:
				url = value.toString();
				break;
			case 3:
				id = Long.valueOf(value.toString());
				break;
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "ImageDto", this.getClass());
	}
}
