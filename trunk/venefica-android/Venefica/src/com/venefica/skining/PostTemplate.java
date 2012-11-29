package com.venefica.skining;

//import com.venefica.activity.DummyStepLogic;
//import com.venefica.activity.PostStepLogic;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public abstract class PostTemplate extends ActivityTemplate
{
	public Button btnNext;
	public Button btnTitle;
	public Button btnShow;
	public Button btnDetail;
	public Button btnLocate;
	public LinearLayout RootView;

	public ViewFlipper PostStepContainer;

	/*public PostStepLogic TitleLogic;
	public PostStepLogic ShowLogic;
	public PostStepLogic DetailLogic;
	public PostStepLogic LocateLogic;*/

	public PostTemplate(Activity Activity)
	{
		super(Activity);
	}

	@Override
	protected void RepairNullWidgets()
	{
		if (btnNext == null)
			btnNext = new Button(mActivity);

		if (btnTitle == null)
			btnTitle = new Button(mActivity);

		if (btnShow == null)
			btnShow = new Button(mActivity);

		if (btnDetail == null)
			btnDetail = new Button(mActivity);

		if (btnLocate == null)
			btnLocate = new Button(mActivity);
		
		if (RootView == null)
			RootView = new LinearLayout(mActivity);

		if (PostStepContainer == null)
			PostStepContainer = new ViewFlipper(mActivity);

		/*if (TitleLogic == null)
			TitleLogic = new DummyStepLogic();

		if (ShowLogic == null)
			ShowLogic = new DummyStepLogic();

		if (DetailLogic == null)
			DetailLogic = new DummyStepLogic();

		if (LocateLogic == null)
			LocateLogic = new DummyStepLogic();*/
	}
}
