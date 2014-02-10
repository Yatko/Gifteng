/**
 * 
 */
package com.venefica.module.listings.mylistings;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.module.user.UserDto;
import com.venefica.services.AdDto;
import com.venefica.services.AdDto.AdStatus;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 *
 */
public class MyListingsAdapter extends BaseAdapter implements OnClickListener {

	public interface OnButtonClickListener{
		public void onDeleteAd(long adId);
		public void onSendMessage(UserDto toUser);
		public void onMarkGifted(long requestId);
		public void onLeaveReview(long adId, UserDto toUser);
		public void onCoverImageClick(long adId);
		public void onEditAd(long adId);
	}
	private List<AdDto> listings;
	private Context context;
	private ViewHolder holder;
	private OnButtonClickListener buttonClickListener;
	/**
	 * constructor
	 */
	public MyListingsAdapter(Context context, List<AdDto> listings) {
		this.context = context;
		this.listings = listings;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.listings.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
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
			convertView = inflater.inflate(R.layout.view_mylisting_list_item, null);
			holder.imgListingImage = (ImageView) convertView.findViewById(R.id.imgMyListingLItemImage);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.txtMyListingLItemStatus);
			
			holder.layoutEdit =  (LinearLayout) convertView.findViewById(R.id.layMyListingLItemEditButton);
			holder.layoutRequest = (LinearLayout) convertView.findViewById(R.id.layMyListingLItemRequests);
			holder.layoutButtons = (LinearLayout) convertView.findViewById(R.id.layMyListingLItemButtons);
			//buttons
			holder.editButton = (Button) convertView.findViewById(R.id.btnMyListingLItemEdit);
			holder.cancelButton = (Button) convertView.findViewById(R.id.btnMyListingLItemCancel);
			
			holder.btnDeleteAd = (ImageButton) convertView.findViewById(R.id.btnMyListingLItemDeleteAd);
			holder.btnSendMessage = (ImageButton) convertView.findViewById(R.id.btnMyListingLItemSendMsg);
			holder.btnMarkGifted = (ImageButton) convertView.findViewById(R.id.btnMyListingLItemMarkGifted);
			holder.btnLeaveReview = (ImageButton) convertView.findViewById(R.id.btnMyListingLItemLeaveReview);
			//set listeners
			holder.editButton.setOnClickListener(this);
			holder.cancelButton.setOnClickListener(this);
			holder.imgListingImage.setOnClickListener(this);
			holder.btnDeleteAd.setOnClickListener(this);
			holder.btnSendMessage.setOnClickListener(this);
			holder.btnMarkGifted.setOnClickListener(this);
			holder.btnLeaveReview.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtStatus.setText(this.listings.get(position).getStatus().toString());
		if (this.listings.get(position).getImage() != null) {
			((VeneficaApplication) ((MyListingsActivity)context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + this.listings.get(position).getImage().getUrl()
						, holder.imgListingImage, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}else {
			((VeneficaApplication) ((MyListingsActivity)context).getApplication())
				.getImgManager().loadImage("", holder.imgListingImage, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}
		
		//show layouts
		if (this.listings.get(position).getStatus() == AdStatus.ACTIVE) {
			holder.layoutButtons.setVisibility(ViewGroup.GONE);
			holder.layoutEdit.setVisibility(ViewGroup.VISIBLE);
			holder.layoutRequest.setVisibility(ViewGroup.GONE);
		} else if (this.listings.get(position).getStatus()  == AdStatus.SELECTED) {
			holder.layoutButtons.setVisibility(ViewGroup.GONE);
			holder.layoutEdit.setVisibility(ViewGroup.GONE);
			holder.layoutRequest.setVisibility(ViewGroup.VISIBLE);
		} else if (this.listings.get(position).getStatus() == AdStatus.EXPIRED) {
			holder.layoutButtons.setVisibility(ViewGroup.VISIBLE);
			holder.layoutEdit.setVisibility(ViewGroup.GONE);
			holder.layoutRequest.setVisibility(ViewGroup.GONE);
		}
		
		//Tags
		holder.imgListingImage.setTag(this.listings.get(position).getId());
		holder.btnDeleteAd.setTag(this.listings.get(position).getId());
//		holder.btnSendMessage.setTag(this.listings.get(position).getId());
//		holder.btnMarkGifted.setTag(this.listings.get(position).getId());
//		holder.btnLeaveReview.setTag(this.listings.get(position).getId());
		holder.editButton.setTag(this.listings.get(position).getId());	
		
		return convertView;
	}

	static class ViewHolder{
		ImageView imgListingImage;
		TextView txtStatus;
		ImageButton btnDeleteAd, btnSendMessage, btnMarkGifted, btnLeaveReview;
		LinearLayout layoutEdit, layoutRequest, layoutButtons;
		Button editButton, cancelButton;
	}

	/**
	 * @param buttonClickListener the buttonClickListener to set
	 */
	public void setButtonClickListener(OnButtonClickListener buttonClickListener) {
		this.buttonClickListener = buttonClickListener;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.imgMyListingLItemImage && this.buttonClickListener != null) {
			//show details
			this.buttonClickListener.onCoverImageClick(Long.parseLong(view.getTag().toString()));
		} else if (id == R.id.btnMyListingLItemDeleteAd && this.buttonClickListener != null) {
			//delete
		} else if (id == R.id.btnMyListingLItemMarkGifted && this.buttonClickListener != null) {
			//mark gifted
		} else if (id == R.id.btnMyListingLItemLeaveReview && this.buttonClickListener != null) {
			//leave review
		} else if (id == R.id.btnMyListingLItemSendMsg && this.buttonClickListener != null) {
			//send message
		} else if (id == R.id.btnMyListingLItemEdit && this.buttonClickListener != null) {
			//edit listing
			this.buttonClickListener.onEditAd(Long.parseLong(view.getTag().toString()));
		}
	}
}
