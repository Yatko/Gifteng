package com.venefica.activity;

import java.util.Date;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;
import com.venefica.market.Product;

public abstract interface PostStepLogic
{
	public static class PostData
	{
		public long id = 0;
		public String Title = "";
		public String Desc = "";
		public String Price = "";
		public long Category = 0;
		public Bitmap ImageShow = null;
		public GeoPoint GeoLocation = null;
		public boolean useExpires = false;
		public Date expirate = null;
		public boolean wanted = false;

		public Bitmap MapPreview;

		public PostData()
		{
			
		}
		
		public PostData(Product product)
		{
			id = product.Id;
			Title =product.Title;
			Desc = product.Desc;
			Price = product.Price;
			Category = product.Category;
			GeoLocation = product.Location;
		}
	}

	/** Save the order */
	public abstract void Commit();

	/** We find the necessary widgets */
	public abstract void OnDisplay(PostActivity activity);

	/** Updates the form in accordance with the data of the order */
	public abstract void UpdateUI(PostData Post);

	/** Data validation and if everything is good to record */
	public abstract boolean ValidateAndCommit();
}
