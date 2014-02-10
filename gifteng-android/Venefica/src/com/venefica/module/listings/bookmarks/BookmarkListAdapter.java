package com.venefica.module.listings.bookmarks;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.venefica.module.main.R;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Adapter for bookmark list
 */
public class BookmarkListAdapter extends BaseAdapter implements OnClickListener {

	public interface BookmarkButtonClickListener{
		public long onRequestButtonClick(long adId, boolean isRequestd);
		public void onImageClick(long adId);
	}
	private List<AdDto> listings;
	private Context context;
	private ViewHolder holder;
	private BookmarkButtonClickListener bookmarkButtonClickListener;
	/**
	 * constructor
	 */
	public BookmarkListAdapter(Context context, List<AdDto> listings) {
		this.context = context;
		this.listings = listings;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {		
		return listings.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
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
			holder  = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_bookmark_list_item, parent, false);
			holder.imgProfile = (ImageView) convertView.findViewById(R.id.imgBookmarkLItemImage);
			holder.btnRequest = (Button) convertView.findViewById(R.id.btnBookmarkLItemRequest);
			holder.btnRequest.setOnClickListener(this);
			holder.imgProfile.setOnClickListener(this);
			convertView.setTag(holder);
		}  else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		try {
			if (this.listings.get(position).getImage() != null && this.listings.get(position).getImage().getUrl() != null) {
				((VeneficaApplication) ((BookmarkListingsActivity)context).getApplication())
					.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + this.listings.get(position).getImage().getUrl()
							, holder.imgProfile, context.getResources().getDrawable(R.drawable.icon_picture_white));
			}else {
				((VeneficaApplication) ((BookmarkListingsActivity)context).getApplication())
					.getImgManager().loadImage("", holder.imgProfile, context.getResources().getDrawable(R.drawable.icon_picture_white));
			}
			
			holder.btnRequest.setTag(this.listings.get(position).getId());
			holder.btnRequest.setContentDescription(this.listings.get(position).isRequested()+"");
			holder.imgProfile.setTag(this.listings.get(position).getId());
		} catch (Exception ex) {
			Log.d("BookmarkListAdapter::getView::", ex.getMessage());
		}
		return convertView;
	}
	
	static class ViewHolder{
		ImageView imgProfile;
		Button btnRequest;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnBookmarkLItemRequest && bookmarkButtonClickListener != null) {
			bookmarkButtonClickListener.onRequestButtonClick(Long.parseLong(view.getTag().toString())
					, Boolean.parseBoolean(view.getContentDescription().toString()));
		} else if (id == R.id.imgBookmarkLItemImage && bookmarkButtonClickListener!= null) {
			bookmarkButtonClickListener.onImageClick(Long.parseLong(view.getTag().toString()));
		}
	}

	/**
	 * @param bookmarkButtonClickListener the bookmarkButtonClickListener to set
	 */
	public void setBookmarkButtonClickListener(
			BookmarkButtonClickListener bookmarkButtonClickListener) {
		this.bookmarkButtonClickListener = bookmarkButtonClickListener;
	}
}
