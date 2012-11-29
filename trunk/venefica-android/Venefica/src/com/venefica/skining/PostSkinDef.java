package com.venefica.skining;

import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.app.Activity;

//import com.venefica.activity.PostDetailLogic;
//import com.venefica.activity.PostLocateLogic;
//import com.venefica.activity.PostShowLogic;
//import com.venefica.activity.PostTitleLogic;
import com.venefica.activity.R;

public class PostSkinDef extends PostTemplate
{

	public PostSkinDef(Activity Activity)
	{
		super(Activity);
	}

	@Override
	public void CreateWidgets()
	{
		try
		{
			mActivity.setContentView(R.layout.post);

			btnNext = (Button)mActivity.findViewById(R.id.btnNext);
			btnTitle = (Button)mActivity.findViewById(R.id.btnTitle);
			btnShow = (Button)mActivity.findViewById(R.id.btnShow);
			btnDetail = (Button)mActivity.findViewById(R.id.btnDetail);
			btnLocate = (Button)mActivity.findViewById(R.id.btnLocate);
			RootView = (LinearLayout)mActivity.findViewById(R.id.RootView);

			PostStepContainer = (ViewFlipper)mActivity.findViewById(R.id.PostStepContainer);

			/*TitleLogic = new PostTitleLogic();
			ShowLogic = new PostShowLogic();
			DetailLogic = new PostDetailLogic();
			LocateLogic = new PostLocateLogic();*/

		}
		catch (Exception e)
		{
			Log.d("PostSkinDef.CreateWidgets Exception:", e.getLocalizedMessage());
		}
	}
}
