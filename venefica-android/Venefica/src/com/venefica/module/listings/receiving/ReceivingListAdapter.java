/**
 * 
 */
package com.venefica.module.listings.receiving;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.module.user.UserDto;
import com.venefica.services.AdDto.AdType;
import com.venefica.services.RequestDto;
import com.venefica.services.RequestDto.RequestStatus;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 *
 */
public class ReceivingListAdapter extends BaseAdapter implements OnClickListener {

	public interface OnButtonClickListener{
		public void onCancelRequest(long requestId);
		public void onSendMessage(UserDto toUser);
		public void onMarkRceived(long requestId);
		public void onLeaveReview(long adId, UserDto toUser);
		public void onCoverImageClick(long adId);
	}
	private Context context;
	private List<RequestDto> requests;
	private static ViewHolder holder;
	private OnButtonClickListener  buttonClickListener;
	/**
	 * constructor
	 */
	public ReceivingListAdapter(Context context, List<RequestDto> requests) {
		this.context = context;
		this.buttonClickListener = (OnButtonClickListener) context;
		this.requests = requests;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return requests.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_receiving_list_item, null);
			holder.imgListingCoverImg = (ImageView) convertView.findViewById(R.id.imgReceivingLItemImage);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.txtReceivingLItemStatus);
			holder.txtExpiaryDate = (TextView) convertView.findViewById(R.id.txtReceivingLItemExpiaryDate);
			
			holder.txtCancelRequest = (TextView) convertView.findViewById(R.id.txtReceivingLItemCancelReq);
			
			holder.btnCancelRequest = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemCancelReq);
			holder.btnSendMessage = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemSendMsg);
			holder.btnMarkReceived = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemMarkReceived);
			holder.btnLeaveReview = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemLeaveReview);
			
			holder.layExpiaryDate = (LinearLayout) convertView.findViewById(R.id.layExpiaryDate);
			
			holder.btnCancelRequest.setOnClickListener(this);
			holder.btnSendMessage.setOnClickListener(this);
			holder.btnMarkReceived.setOnClickListener(this);
			holder.btnLeaveReview.setOnClickListener(this);
			holder.imgListingCoverImg.setOnClickListener(this);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
				
		if (this.requests.get(position).getImage() != null) {
			((VeneficaApplication) ((ReceivingListActivity)context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + this.requests.get(position).getImage().getUrl()
						, holder.imgListingCoverImg, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}else {
			((VeneficaApplication) ((ReceivingListActivity)context).getApplication())
				.getImgManager().loadImage("", holder.imgListingCoverImg, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}
		if (requests.get(position).getStatus() == RequestStatus.ACCEPTED) {
			holder.btnCancelRequest.setEnabled(false);
			holder.btnSendMessage.setEnabled(true);
			holder.btnMarkReceived.setEnabled(true);
			holder.btnLeaveReview.setEnabled(true);
		} else if (requests.get(position).getStatus() == RequestStatus.PENDING){
			holder.btnCancelRequest.setEnabled(true);
			holder.btnSendMessage.setEnabled(false);
			holder.btnMarkReceived.setEnabled(false);
			holder.btnLeaveReview.setEnabled(false);
		} else if (requests.get(position).getStatus() == RequestStatus.EXPIRED) {
			holder.txtCancelRequest.setText(context.getResources().getString(R.string.label_menu_delete).toUpperCase());
			holder.btnCancelRequest.setImageResource(R.drawable.icon_trash);
			holder.btnCancelRequest.setEnabled(true);
			holder.btnSendMessage.setEnabled(false);
			holder.btnMarkReceived.setEnabled(false);
			holder.btnLeaveReview.setEnabled(false);
		}
		
		if (requests.get(position).getType() == AdType.MEMBER) {
			holder.layExpiaryDate.setVisibility(ViewGroup.GONE);
			holder.txtStatus.setText(requests.get(position).getStatus().toString().toUpperCase());
		} else {
			holder.layExpiaryDate.setVisibility(ViewGroup.VISIBLE);
			holder.txtStatus.setText("");
//			holder.txtExpiaryDate.setText(requests.get(position).get);
		}
		
		holder.btnCancelRequest.setTag(requests.get(position).getId());
		holder.btnSendMessage.setTag(requests.get(position).getUser());
		holder.btnMarkReceived.setTag(requests.get(position).getId());
		holder.btnLeaveReview.setTag(requests.get(position).getUser());
		holder.btnLeaveReview.setContentDescription(requests.get(position).getAdId()+"");
		holder.imgListingCoverImg.setTag(requests.get(position).getAdId());
		return convertView;
	}

	private static class ViewHolder {
		ImageView imgListingCoverImg;
		TextView txtStatus, txtCancelRequest, txtExpiaryDate;
		ImageButton btnCancelRequest, btnSendMessage
					, btnMarkReceived, btnLeaveReview;
		LinearLayout layExpiaryDate;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnReceivingListItemCancelReq && buttonClickListener != null) {
			buttonClickListener.onCancelRequest(Long.parseLong(view.getTag().toString()));
		} else if (id == R.id.btnReceivingListItemSendMsg && buttonClickListener != null) {
			buttonClickListener.onSendMessage((UserDto) view.getTag());
		} else if (id == R.id.btnReceivingListItemMarkReceived && buttonClickListener != null) {
			buttonClickListener.onMarkRceived(Long.parseLong(view.getTag().toString()));
		} else if (id == R.id.btnReceivingListItemLeaveReview && buttonClickListener != null) {
			buttonClickListener.onLeaveReview(Long.parseLong(view.getContentDescription().toString()), (UserDto) view.getTag());
		} else if (id == R.id.imgReceivingLItemImage && buttonClickListener != null) {
			buttonClickListener.onCoverImageClick(Long.parseLong(view.getTag().toString()));
		}
	}

	/**
	 * @return the buttonClickListener
	 */
	public OnButtonClickListener getButtonClickListener() {
		return buttonClickListener;
	}

	/**
	 * @param buttonClickListener the buttonClickListener to set
	 */
	public void setButtonClickListener(OnButtonClickListener buttonClickListener) {
		this.buttonClickListener = buttonClickListener;
	}
}
