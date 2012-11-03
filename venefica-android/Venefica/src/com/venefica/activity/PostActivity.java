package com.venefica.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.venefica.activity.PostStepLogic.PostData;
import com.venefica.skining.PostSkinDef;
import com.venefica.skining.PostTemplate;
import com.venefica.utils.Utils;

public class PostActivity extends MapActivityEx
{
	public class Step
	{
		static final int Complite = -1;
		static final int Title = 0;
		static final int Show = 1;
		static final int Detail = 2;
		static final int Locate = 3;

		public int step = Title;

		public void SetStep(int index)
		{
			switch (index)
			{
				case Title:
					step = Title;
					break;

				case Show:
					step = Show;
					break;

				case Detail:
					step = Detail;
					break;

				case Locate:
					step = Locate;
					break;

				case Complite:
					step = Complite;
					break;

				default:
					step = Complite;
					break;
			}
		}

		public int NextStep()
		{
			switch (step)
			{
				case Title:
					step = Show;
					break;

				case Show:
					step = Detail;
					break;

				case Detail:
					step = Locate;
					break;

				case Locate:
					step = Complite;
					break;

				case Complite:
					step = Complite;
					break;

				default:
					step = Title;
					break;
			}

			return step;
		}

		public int PreviousStep()
		{
			switch (step)
			{
				case Title:
					step = Title;
					break;

				case Show:
					step = Title;
					break;

				case Detail:
					step = Show;
					break;

				case Locate:
					step = Detail;
					break;

				case Complite:
					step = Locate;
					break;

				default:
					step = Title;
					break;
			}

			return step;
		}

		public PostStepLogic GetCurrentLogic()
		{
			PostStepLogic result;

			switch (step)
			{
				case Title:
					result = T.TitleLogic;
					break;

				case Show:
					result = T.ShowLogic;
					break;

				case Detail:
					result = T.DetailLogic;
					break;

				case Locate:
					result = T.LocateLogic;
					break;

				case Complite:
					result = null;
					break;

				default:
					result = null;
					break;
			}

			return result;
		}

		public boolean NeedKeyboard()
		{
			boolean result = true;

			switch (step)
			{
				case Title:
					result = true;
					break;

				case Show:
					result = false;
					break;

				case Detail:
					result = true;
					break;

				case Locate:
					result = false;
					break;

				case Complite:
					result = false;
					break;

				default:
					result = true;
					break;
			}

			return result;
		}
	}

	/** ������ ����� */
	PostTemplate T;

	/** ������ � �������� */
	PostData Post;

	/** ������ ����� ��������� ������ � ������ */
	Step step = new Step();
	PostStepLogic CurrentLogic;

	/** �������� ������ ����� */
	private InputMethodManager cachedImm;

	View.OnClickListener ButtonBarClick = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{
				Integer tag = (Integer)v.getTag();
				SetStep(tag);
			}
			catch (Exception e)
			{
				Log.d("ButtonBarClick Exception:", e.getLocalizedMessage());
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		T = new PostSkinDef(this);

		T.btnNext.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				SetStep(step.NextStep());
			}
		});

		T.btnTitle.setClickable(false);
		T.btnShow.setClickable(false);
		T.btnDetail.setClickable(false);
		T.btnLocate.setClickable(false);

		SetStep(Step.Title);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		TabMainActivity.HideTabs();
		T.PostStepContainer.postInvalidateDelayed(500);
		T.RootView.postInvalidateDelayed(500);

		if (App.post == null)
		{
			App.post = Post = new PostData();
			SetStep(Step.Title);
			CurrentLogic = step.GetCurrentLogic();
			CurrentLogic.OnDisplay(this);
			CurrentLogic.UpdateUI(Post);
			UpdateButtonBar();
		}
		else
		{
			Post = App.post;
		}
	}

	void SetStep(int NewStep)
	{
		step.SetStep(NewStep);
		if (step.step == Step.Complite)
		{
			if (CurrentLogic.ValidateAndCommit())
			{
				GoToPreview();
			}
		}
		else
		{
			PostStepLogic NewLogic = step.GetCurrentLogic();
			if (NewLogic != null)
			{
				if (CurrentLogic != null)
				{
					if (CurrentLogic.ValidateAndCommit())
					{
						NewLogic.OnDisplay(PostActivity.this);
						NewLogic.UpdateUI(Post);
						CurrentLogic = NewLogic;
						HideKeyboard();
						UpdateButtonBar();
						T.PostStepContainer.postInvalidateDelayed(500);
						T.RootView.postInvalidateDelayed(500);
					}
					else
					{
						step.PreviousStep();
					}
				}
			}
			else
			{
				throw new RuntimeException("step.GetCurrentLogic() == null");
			}
		}

		if (step.NeedKeyboard())
			ShowKeyboard();
		else
			HideKeyboard(getWindow().getDecorView());
	}

	@Override
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
	{
		boolean bool = true;
		if (paramInt != 4)
			bool = super.onKeyDown(paramInt, paramKeyEvent);
		else
			onBackPressed();
		
		return bool;
	}

	@Override
	public void onBackPressed()
	{
		if (step.step == Step.Title)
		{
			Object parent = getParent();
			if (parent == null)
			{
				finish();
			}
			else if (parent instanceof TabMainActivity)
			{
				((TabMainActivity)parent).GoToPrevious();
			}
			else
			{
				finish();
			}
		}
		else
		{
			SetStep(step.PreviousStep());
		}
	}

	void UpdateButtonBar()
	{
		switch (step.step)
		{
			case Step.Title:
				T.btnTitle.setSelected(true);
				T.btnShow.setSelected(false);
				T.btnDetail.setSelected(false);
				T.btnLocate.setSelected(false);
				break;

			case Step.Show:
				T.btnTitle.setSelected(false);
				T.btnShow.setSelected(true);
				T.btnDetail.setSelected(false);
				T.btnLocate.setSelected(false);
				break;

			case Step.Detail:
				T.btnTitle.setSelected(false);
				T.btnShow.setSelected(false);
				T.btnDetail.setSelected(true);
				T.btnLocate.setSelected(false);
				break;

			case Step.Locate:
				T.btnTitle.setSelected(false);
				T.btnShow.setSelected(false);
				T.btnDetail.setSelected(false);
				T.btnLocate.setSelected(true);
				break;
		}
	}

	void GoToPreview()
	{
		Intent intent = new Intent(this, PostPreviewActivity.class);
		startActivity(intent);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Bitmap image = Utils.GetImageActivityResult(this, requestCode, resultCode, data);

		if (image != null)
		{
			Post.ImageShow = image;
			CurrentLogic.UpdateUI(Post);
		}
		else
		{
			Log.d("POST_SHOW Alert!", "image == null");
		}

	}

	private InputMethodManager getIMM()
	{
		if (cachedImm == null)
			cachedImm = ((InputMethodManager)getSystemService("input_method"));
		return cachedImm;
	}

	public void ShowKeyboard()
	{
		getIMM().toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public void HideKeyboard(View v)
	{
		if (v != null)
			getIMM().hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
