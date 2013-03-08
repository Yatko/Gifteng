package com.venefica.module.messages;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.services.MessageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Adapter for message list
 */
public class MessageListAdapter extends BaseAdapter implements OnClickListener{
	private Context context;
	private ArrayList<MessageDto> messages;
	private static ViewHolder holder;
	/**
	 * Constructor for view
	 */
	public MessageListAdapter(Context context, ArrayList<MessageDto> messages) {
		this.context = context;
		this.messages = messages;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return messages.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_message_list_item, parent, false);
			convertView.setTag(holder);
			holder.txtMessageText = (TextView) convertView.findViewById(R.id.txtMessageLItemText);
			holder.txtSenderName = (TextView) convertView.findViewById(R.id.txtMessageLItemSender);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtMessageLItemTime);
			holder.imgProfile = (ImageView) convertView.findViewById(R.id.imgMessageLItemImage);
			holder.imgBtnQuickReply = (ImageButton) convertView.findViewById(R.id.imgBtnMessageLItemReply);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtMessageText.setText(messages.get(position).getText());
		holder.txtSenderName.setText(messages.get(position).getFromFullName());
		holder.imgBtnQuickReply.setOnClickListener(this);
		
		if (messages.get(position).getCreatedAt() != null) {
			holder.txtTime.setText(Utility.convertDateTimeToString(messages.get(position).getCreatedAt()));
		}		
		((VeneficaApplication) ((MessageListActivity)this.context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + messages.get(position)
				.getFromAvatarUrl(), holder.imgProfile, this.context.getResources().getDrawable(R.drawable.icon_user));				
				
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView imgProfile;
		TextView txtMessageText;
		TextView txtSenderName;
		TextView txtTime;
		ImageButton imgBtnQuickReply;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgMessageLItemImage) {
			
		} else if (view.getId() == R.id.imgBtnMessageLItemReply) {
			
		}
	}
}
