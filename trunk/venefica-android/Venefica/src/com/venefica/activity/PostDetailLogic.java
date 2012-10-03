package com.venefica.activity;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.venefica.activity.R;
import com.venefica.activity.PostActivity.Step;
import com.venefica.market.Category;
import com.venefica.utils.CategoryListAdapter;

public class PostDetailLogic implements PostStepLogic
{
	PostActivity mActivity;

	EditText editPrice;
	Spinner spinnerCategory;
	Spinner spinnerSubCategory;
	CategoryListAdapter catList;

	public void Commit()
	{
		if (mActivity != null && mActivity.Post != null)
		{
			mActivity.Post.Price = editPrice.getText().toString();
			mActivity.Post.Category = catList.GetLastCategory().Id;//CategoryId;
		}
	}

	public void OnDisplay(PostActivity activity)
	{
		mActivity = activity;
		mActivity.T.PostStepContainer.setDisplayedChild(Step.Detail);

		editPrice = (EditText)mActivity.findViewById(R.id.editPrice);
		editPrice.clearFocus();

		if (catList == null)
		{
			LinearLayout layoutSpiner = (LinearLayout)mActivity.findViewById(R.id.linearLayoutSpiners);
			catList = new CategoryListAdapter(mActivity, layoutSpiner);
			catList.AddCategory(Category.GetRootCategory());
		}
	}

	public void UpdateUI(PostData Post)
	{
		if (Post == null)
		{
			Log.d("PostDetailLogic.UpdateUI", "Post==null");
			return;
		}
	}

	public boolean ValidateAndCommit()
	{
		Commit();

		return true;
	}
}
