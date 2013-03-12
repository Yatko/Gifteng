package com.venefica.module.messages;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
	private ArrayList<Long> selectedPositions;
	/**
	 * Constructor for view
	 */
	public MessageListAdapter(Context context, ArrayList<MessageDto> messages) {
		this.context = context;
		this.messages = messages;
		selectedPositions = new ArrayList<Long>();
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
			holder.txtMessageText = (TextView) convertView.findViewById(R.id.txtMessageLItemText);
			holder.txtSenderName = (TextView) convertView.findViewById(R.id.txtMessageLItemSender);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtMessageLItemTime);
			holder.imgProfile = (ImageView) convertView.findViewById(R.id.imgMessageLItemImage);
			holder.imgViewMode = (ImageView) convertView.findViewById(R.id.imgMessageLItemMode);	
			holder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkMessageLItemSelect);
			holder.chkSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//handle action mode visibility and message selection 
					Long id = Long.parseLong(buttonView.getContentDescription().toString());
					if (buttonView.isChecked() && !selectedPositions.contains(id)) {
						selectedPositions.add(id);
						if (selectedPositions.size() >= 1) {
							((MessageListActivity)context).showActionMode(true);
						}
					} else if (!buttonView.isChecked() && selectedPositions.contains(id)) {
						selectedPositions.remove(id);
						selectedPositions.trimToSize();
						if (selectedPositions.size() == 0) {
							((MessageListActivity)context).showActionMode(false);
						}
					}
					//set action mode title
					((MessageListActivity)context).setActionModeTitle(selectedPositions.size() 
							+" "+ context.getResources().getString(R.string.label_selected));
				}
			});
			convertView.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (messages.get(position).isOwner()) {			
			holder.txtSenderName.setText(messages.get(position).getToFullName());
			holder.imgViewMode.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_arrow_top));
		} else {
			holder.txtSenderName.setText(messages.get(position).getFromFullName());
			holder.imgViewMode.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_arrow_bottom));
		}
		//set is owner value as description to send it on message detail
		holder.txtSenderName.setContentDescription(String.valueOf(messages.get(position).isOwner()));
		holder.txtMessageText.setText(messages.get(position).getText());
		holder.chkSelected.setContentDescription(messages.get(position).getId()+"");
		if (messages.get(position).getCreatedAt() != null) {
			holder.txtTime.setText(Utility.convertDateTimeToString(messages.get(position).getCreatedAt()));
		}		
		((VeneficaApplication) ((MessageListActivity)this.context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX 
						+ (messages.get(position).isOwner() ? messages.get(position)
								.getToAvatarUrl() : messages.get(position).getFromAvatarUrl())
				, holder.imgProfile, this.context.getResources().getDrawable(R.drawable.icon_user));				
				
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView imgProfile;
		TextView txtMessageText;
		TextView txtSenderName;
		TextView txtTime;
		ImageView imgViewMode;
		CheckBox chkSelected;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgMessageLItemImage) {
			
		} else {
			holder = (ViewHolder) view.getTag();
			Intent intent = new Intent((MessageListActivity)context,
					MessageDetailActivity.class);
			if (holder != null) {
				intent.putExtra("sender", holder.txtSenderName.getText().toString());
				intent.putExtra("time", holder.txtTime.getText().toString());
				intent.putExtra("message_text", holder.txtMessageText.getText().toString());
				intent.putExtra("is_owner", Boolean.parseBoolean(holder.txtSenderName.getContentDescription().toString()));
			}			
			context.startActivity(intent);
		}
	}

	/**
	 * @return the selectedPositions
	 */
	public ArrayList<Long> getSelectedPositions() {
		return selectedPositions;
	}

	/**
	 * @param selectedPositions the selectedPositions to set
	 */
	public void setSelectedPositions(ArrayList<Long> selectedPositions) {
		this.selectedPositions = selectedPositions;
	}
	
	public void clearSelectedPositions(){
		this.selectedPositions.clear();
	}
}
