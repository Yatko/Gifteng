package com.venefica.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.venefica.activity.R;
import com.venefica.activity.PostActivity.Step;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class PostTitleLogic implements PostStepLogic
{
	PostActivity mActivity;

	EditText editTitle;
	EditText editDesc;
	Button btnSalePost;
	Button btnWantedPost;
	Button btnDate;
	boolean useExpires = false;
	CheckBox cbCustomExpires;
	TextView lblExpires2Months;

	SimpleDateFormat df;
	final Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	private final DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			date.setDate(dayOfMonth);
			date.setMonth(monthOfYear);
			date.setYear(year - 1900);

			UpdateDateBirth();
		}
	};

	public void Commit()
	{
		if (mActivity != null && mActivity.Post != null)
		{
			mActivity.Post.Title = editTitle.getText().toString();
			mActivity.Post.Desc = editDesc.getText().toString();
			
			mActivity.Post.useExpires = useExpires;
			mActivity.Post.expirate = date;
			mActivity.Post.wanted = btnWantedPost.isSelected();
		}
	}

	public void OnDisplay(PostActivity activity)
	{
		mActivity = activity;
		mActivity.T.PostStepContainer.setDisplayedChild(Step.Title);

		editTitle = (EditText)mActivity.findViewById(R.id.editTitle);
		editDesc = (EditText)mActivity.findViewById(R.id.editDesc);
		btnSalePost = (Button)mActivity.findViewById(R.id.btnSalePost);
		btnWantedPost = (Button)mActivity.findViewById(R.id.btnWantedPost);
		btnDate = (Button)mActivity.findViewById(R.id.btnDate);
		cbCustomExpires = (CheckBox)mActivity.findViewById(R.id.cbCustomExpires);
		lblExpires2Months = (TextView)mActivity.findViewById(R.id.lblExpires2Months);

		editTitle.clearFocus();
		editDesc.clearFocus();

		btnSalePost.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				btnSalePost.setBackgroundResource(R.drawable.forsale_on);
				btnWantedPost.setBackgroundResource(R.drawable.wanted_off);
				btnWantedPost.setSelected(false);
			}
		});

		btnWantedPost.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				btnSalePost.setBackgroundResource(R.drawable.forsale_off);
				btnWantedPost.setBackgroundResource(R.drawable.wanted_on);
				btnWantedPost.setSelected(true);
			}
		});

		btnDate.setVisibility(View.GONE);
		lblExpires2Months.setVisibility(View.VISIBLE);
		cbCustomExpires.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				df = new SimpleDateFormat(mActivity.GetStringResource(R.string.date_format));
				if (isChecked)
				{
					useExpires = true;

					date.setDate(cal.get(Calendar.DAY_OF_MONTH));
					date.setMonth(cal.get(Calendar.MONTH));
					date.setYear(cal.get(Calendar.YEAR) - 1900);
					UpdateDateBirth();

					btnDate.setVisibility(View.VISIBLE);
					btnDate.setOnClickListener(new OnClickListener()
					{
						public void onClick(View v)
						{
							new DatePickerDialog(mActivity, pDateSetListener, date.getYear() + 1900, date.getMonth(), date.getDate()).show();
						}
					});
					
					lblExpires2Months.setVisibility(View.GONE);
				}
				else
				{
					useExpires = false;
					btnDate.setVisibility(View.GONE);
					lblExpires2Months.setVisibility(View.VISIBLE);
				}
			}
		});		
	}

	public void UpdateUI(PostData Post)
	{
		if (Post == null)
		{
			Log.d("PostLocateLogic.UpdateUI", "post==null");
			return;
		}

		if (editTitle != null && editDesc != null)
		{
			editTitle.setText(Post.Title);
			editDesc.setText(Post.Desc);
		}
	}

	public boolean ValidateAndCommit()
	{
		if (editTitle.getText().toString().trim().length() == 0)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage(mActivity.GetStringResource(R.string.small_title_message)).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
				}
			});
			builder.create().show();

			return false;
		}

		Calendar tmpCal = Calendar.getInstance();
		tmpCal.setTime(date);
		if (useExpires && tmpCal.before(cal))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage(mActivity.GetStringResource(R.string.expires_date_error)).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
				}
			});
			builder.create().show();
			return false;
		}

		Commit();
		return true;
	}

	//OVER FUNCTION

	protected void UpdateDateBirth()
	{
		String DateStr = df.format(date);

		if (DateStr != null)
			btnDate.setText(DateStr);
	}
}
