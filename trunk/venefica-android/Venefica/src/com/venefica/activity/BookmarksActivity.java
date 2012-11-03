package com.venefica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.venefica.activity.R;
import com.venefica.market.Product;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetBookmarkedAdsContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetBookmarkedAdsResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.skining.BookmarksSkinDef;
import com.venefica.skining.BookmarksTemplate;
import com.venefica.utils.BookmarkListAdapter;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class BookmarksActivity extends ActivityEx
{
	private BookmarksTemplate T;
	BookmarkListAdapter itemAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new BookmarksSkinDef(this);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		ShowLoadingDialog();
		VeneficaApplication.asyncServices.GetBookmarkedAds(new GetBookmarkedAdsContext(new ICallback()
		{
			public CallbackReturn Callback(IResult<?> result)
			{
				HideLoadingDialog();
				
				GetBookmarkedAdsResult  ret = (GetBookmarkedAdsResult)result;
				switch (ret.SoapResult)
				{
					case Ok:
						itemAdapter = new BookmarkListAdapter(BookmarksActivity.this, R.layout.product_row, ret.Return);
						T.listBookmarks.setAdapter(itemAdapter);						
						T.listBookmarks.setOnItemClickListener(new OnItemClickListener()
						{
							public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
							{
								Product item = (Product)arg0.getItemAtPosition(position);
								if (item != null)
								{
									GoToPreview(item.Id);
								}
							}
						});
						break;

					case Fault:
						ShowInfoDialog(GetStringResource(R.string.get_bookmarks_error), FinishRunnable);
						break;

					case SoapProblem:
						ShowInfoDialog(GetStringResource(R.string.soap_problem), FinishRunnable);
						break;
				}
				
				return CallbackReturn.Ok;
			}
		}));
	}

	public void GoToPreview(long productId)
	{
		final Intent intent = new Intent(this, TabProductViewActivity.class);
		intent.putExtra(Constants.PRODUCT_ID_PARAM_NAME, productId);
		startActivity(intent);
	}
}
