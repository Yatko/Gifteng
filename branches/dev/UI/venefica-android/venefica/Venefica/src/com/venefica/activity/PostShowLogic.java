package com.venefica.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.activity.R;
import com.venefica.activity.PostActivity.Step;
import com.venefica.utils.Utils;

public class PostShowLogic implements PostStepLogic
{
	PostActivity mActivity;

	ImageView imgShow;
	TextView lblAddPhoto;
	Button btnDeleteShow;
	Button btnTakePhoto;
	Button btnChoosePhoto;
	TextView lblNoCrop;

	public void Commit()
	{

	}

	public void OnDisplay(PostActivity activity)
	{
		mActivity = activity;
		mActivity.T.PostStepContainer.setDisplayedChild(Step.Show);

		imgShow = (ImageView)mActivity.findViewById(R.id.imgShow);
		lblAddPhoto = (TextView)mActivity.findViewById(R.id.lblAddPhoto);
		btnDeleteShow = (Button)mActivity.findViewById(R.id.btnDeleteShow);
		btnTakePhoto = (Button)mActivity.findViewById(R.id.btnTakePhoto);
		btnChoosePhoto = (Button)mActivity.findViewById(R.id.btnChoosePhoto);
		lblNoCrop = (TextView)mActivity.findViewById(R.id.lblNoCrop);
		//Let there be hidden because CROP dumb
		lblNoCrop.setVisibility(View.GONE);
		//lblNoCrop.setVisibility(VeneficaApplication.useSdCard ? View.GONE : View.VISIBLE);
		
		btnDeleteShow.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mActivity.Post.ImageShow = null;
				imgShow.setImageResource(R.drawable.default_photo);
			}
		});

		btnTakePhoto.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Utils.StartActivityForResultGetCamera(mActivity);
			}
		});

		btnChoosePhoto.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Utils.StartActivityForResultGetGallery(mActivity);
			}
		});
	}

	public void UpdateUI(PostData Post)
	{
		if (Post == null)
		{
			Log.d("PostShowLogic.UpdateUI", "post==null");
			return;
		}

		if (Post.ImageShow != null)
		{
			imgShow.setImageBitmap(Post.ImageShow);
			lblAddPhoto.setVisibility(View.INVISIBLE);
		}
		else
		{
			imgShow.setImageResource(R.drawable.default_photo);
			lblAddPhoto.setVisibility(View.VISIBLE);
		}
	}

	public boolean ValidateAndCommit()
	{
		Commit();
		return true;
	}
}
