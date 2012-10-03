package com.venefica.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.venefica.activity.R;
import com.venefica.skining.EditAdShowSkinDef;
import com.venefica.skining.EditAdShowTemplate;
import com.venefica.utils.ImageAd;
import com.venefica.utils.ImageAdapter;
import com.venefica.utils.Utils;

public class EditAdShowActivity extends ActivityEx
{
	EditAdShowTemplate T;
	private ImageAdapter adapter;
	private List<ImageAd> images;
	private List<Long> imagesForDelete;
	
	private final AdapterView.OnItemLongClickListener galleryClick = new AdapterView.OnItemLongClickListener()
	{
		public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, final long id)
		{
			ShowQuestionDialog(GetStringResource(R.string.delete_this_image), new Runnable()
			{				
				public void run()
				{
					if(id != TabAdEditActivity.Item.image.id && id != 0)
						imagesForDelete.add(Long.valueOf(id));
					
					images.remove(position);
					adapter.notifyDataSetChanged();
				}
			});
			
			return false;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new EditAdShowSkinDef(this);
		HideKeyboard();

		images = TabAdEditActivity.images;
		imagesForDelete = TabAdEditActivity.imagesForDelete;
		
		adapter = new ImageAdapter(this, images);
		T.galleryImage.setAdapter(adapter);
		
		T.galleryImage.setOnItemLongClickListener(galleryClick);

		T.btnTakePhoto.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Utils.StartActivityForResultGetCamera(EditAdShowActivity.this);
			}
		});

		T.btnChoosePhoto.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Utils.StartActivityForResultGetGallery(EditAdShowActivity.this);
			}
		});
	}

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Bitmap image = Utils.GetImageActivityResult(this, requestCode, resultCode, data);

		if (image != null)
		{
			images.add(new ImageAd(0, null, image));
			adapter.notifyDataSetChanged();
			T.galleryImage.setSelection(T.galleryImage.getCount()-1, true);
		}
		else
		{
			Log.d("POST_SHOW Alert!", "image == null");
		}
	}
}
