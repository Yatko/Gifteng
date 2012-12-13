package com.venefica.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;

import com.venefica.module.main.R;
import com.venefica.services.MessageDto;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListAdapter extends ArrayAdapter<MessageDto>
{
	private List<MessageDto> Messages;
	private final SimpleDateFormat df;

	static class ViewHolder
	{
		public ImageView imgAvatar;

		public TextView lblUserName;
		public TextView lblMessage;
		public TextView lblTime;
	}

	public MessageListAdapter(Context context, List<MessageDto> Messages)
	{
		super(context, R.layout.message_row, Messages);
		this.Messages = Messages;

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
			row = inflater.inflate(R.layout.message_row, parent, false);

			holder = new ViewHolder();

			holder.imgAvatar = (ImageView)row.findViewById(R.id.imgAvatar);
			holder.lblUserName = (TextView)row.findViewById(R.id.lblUserName);
			holder.lblMessage = (TextView)row.findViewById(R.id.lblMessage);
			holder.lblTime = (TextView)row.findViewById(R.id.lblTime);

			row.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)row.getTag();
		}

		if (position < Messages.size() && position >= 0)
		{
			MessageDto item = Messages.get(position);
			if (item != null)
			{
				/*if (item.fromAvatarUrl.length() > 0 && VeneficaApplication.ImgLoader != null)
				{
					VeneficaApplication.ImgLoader.displayImage(Constants.PHOTO_URL_PREFIX + item.fromAvatarUrl, holder.imgAvatar, VeneficaApplication.ImgLoaderOptions);
				}*/

				holder.lblUserName.setText(item.fromFullName);
				holder.lblMessage.setText(item.text);
				holder.lblTime.setText(df.format(item.createdAt));

				if (item.read == false && item.owner == false)
				{
					holder.lblUserName.setTypeface(Typeface.DEFAULT_BOLD);
					holder.lblMessage.setTypeface(Typeface.DEFAULT_BOLD);
					holder.lblTime.setTypeface(Typeface.DEFAULT_BOLD);
				}
				else
				{
					holder.lblUserName.setTypeface(Typeface.DEFAULT);
					holder.lblMessage.setTypeface(Typeface.DEFAULT);
					holder.lblTime.setTypeface(Typeface.DEFAULT);
				}
			}
			else
				Log.d("MessageListAdapter.getView", "value == null");
		}
		else
			Log.d("MessageListAdapter.getView", "position(" + position + ") out of range(0 - " + Messages.size() + ")");

		return row;
	}

	@Override
	public long getItemId(int position)
	{
		return Messages.get(position).id;
	}
}
