package com.venefica.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.venefica.activity.R;
import com.venefica.market.Product;
import com.venefica.skining.EditAdDetailsSkinDef;
import com.venefica.skining.EditAdDetailsTemplate;
import com.venefica.utils.CategoryListAdapter;

public class EditAdDetailsActivity extends ActivityEx
{
	EditAdDetailsTemplate T;

	Product Post;
	CategoryListAdapter categoryList;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new EditAdDetailsSkinDef(this);

		if (TabAdEditActivity.Item == null)
		{
			finish();
			return;
		}
		else
			Post = TabAdEditActivity.Item;

		T.editTitle.setText(Post.Title);
		T.editDesc.setText(Post.Desc);
		T.editPrice.setText(Post.Price);

		LinearLayout layoutSpiner = (LinearLayout)findViewById(R.id.linearLayoutSpiners);
		categoryList = new CategoryListAdapter(this, layoutSpiner);
		categoryList.CreateBackHierarchy(Post.Category);
	}

	@Override
	public void onPause()
	{
		super.onPause();

		Post.Title = T.editTitle.getText().toString();
		Post.Desc = T.editDesc.getText().toString();
		Post.Price = T.editPrice.getText().toString();
		Post.Category = categoryList.GetLastCategory().Id;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		HideKeyboard();
	}
}
