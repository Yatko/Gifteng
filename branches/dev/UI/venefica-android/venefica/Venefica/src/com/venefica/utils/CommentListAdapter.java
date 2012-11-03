package com.venefica.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;

import com.venefica.activity.R;
import com.venefica.services.CommentDto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentListAdapter extends ArrayAdapter<CommentDto>
{
	private List<CommentDto> Comment;
	private final SimpleDateFormat df;

	static class ViewHolder
	{
		public ImageView imgAvatar;
		public TextView lblUserName;
		public TextView lblComment;
		public TextView lblTime;
	}

	public CommentListAdapter(Context context, List<CommentDto> Comment)
	{
		super(context, R.layout.comment_row, Comment);
		this.Comment = Comment;

		df = new SimpleDateFormat(context.getString(R.string.message_date_format));
		try
		{
			DateFormatSymbols dfSymbols = df.getDateFormatSymbols();
			String[] ShortWeekdaysName = context.getResources().getStringArray(R.array.short_weekdays);
			dfSymbols.setShortWeekdays(ShortWeekdaysName);
			df.setDateFormatSymbols(dfSymbols);
		}
		catch (Exception e)
		{
			Log.d("MessageListAdapter Exception:", e.getLocalizedMessage());
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		View row = convertView;

		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater)super.getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.comment_row, parent, false);

			holder = new ViewHolder();

			holder.imgAvatar = (ImageView)row.findViewById(R.id.imgAvatar);
			holder.lblUserName = (TextView)row.findViewById(R.id.lblUserName);
			holder.lblComment = (TextView)row.findViewById(R.id.lblComment);
			holder.lblTime = (TextView)row.findViewById(R.id.lblTime);

			row.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)row.getTag();
		}

		if (position < Comment.size() && position >= 0)
		{
			CommentDto item = Comment.get(position);
			if (item != null)
			{
				if (item.publisherAvatarUrl!= null && item.publisherAvatarUrl.length() > 0 && MyApp.ImgLoader != null)
				{
					MyApp.ImgLoader.displayImage(Constants.PHOTO_URL_PREFIX + item.publisherAvatarUrl, holder.imgAvatar, MyApp.ImgLoaderOptions);
				}

				holder.lblUserName.setText(item.publisherFullName);
				holder.lblComment.setText(item.text);
				holder.lblTime.setText(df.format(item.createdAt));
			}
			else
				Log.d("MessageListAdapter.getView", "value == null");
		}
		else
			Log.d("MessageListAdapter.getView", "position(" + position + ") out of range(0 - " + Comment.size() + ")");

		return row;
	}

	@Override
	public long getItemId(int position)
	{
		return Comment.get(position).id;
	}
}
