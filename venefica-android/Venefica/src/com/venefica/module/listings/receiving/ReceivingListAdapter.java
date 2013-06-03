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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.user.UserDto;
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
			
			holder.txtCancelRequest = (TextView) convertView.findViewById(R.id.txtReceivingLItemCancelReq);
			
			holder.btnCancelRequest = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemCancelReq);
			holder.btnSendMessage = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemSendMsg);
			holder.btnMarkReceived = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemMarkReceived);
			holder.btnLeaveReview = (ImageButton) convertView.findViewById(R.id.btnReceivingListItemLeaveReview);
			
			holder.btnCancelRequest.setOnClickListener(this);
			holder.btnSendMessage.setOnClickListener(this);
			holder.btnMarkReceived.setOnClickListener(this);
			holder.btnLeaveReview.setOnClickListener(this);
			
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
		
				
		holder.txtStatus.setText(requests.get(position).getStatus().toString().toUpperCase());
		holder.btnCancelRequest.setTag(requests.get(position).getId());
		holder.btnSendMessage.setTag(requests.get(position).getUser());
		holder.btnMarkReceived.setTag(requests.get(position).getId());
		holder.btnLeaveReview.setTag(requests.get(position).getUser());
		holder.btnLeaveReview.setContentDescription(requests.get(position).getAdId()+"");
		return convertView;
	}

	private static class ViewHolder {
		ImageView imgListingCoverImg;
		TextView txtStatus, txtCancelRequest;
		ImageButton btnCancelRequest, btnSendMessage
					, btnMarkReceived, btnLeaveReview;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnReceivingListItemCancelReq) {
			buttonClickListener.onCancelRequest(Long.parseLong(view.getTag().toString()));
		} else if (id == R.id.btnReceivingListItemSendMsg) {
			buttonClickListener.onSendMessage((UserDto) view.getTag());
		} else if (id == R.id.btnReceivingListItemMarkReceived) {
			buttonClickListener.onMarkRceived(Long.parseLong(view.getTag().toString()));
		} else if (id == R.id.btnReceivingListItemLeaveReview) {
			buttonClickListener.onLeaveReview(Long.parseLong(view.getContentDescription().toString()), (UserDto) view.getTag());
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
