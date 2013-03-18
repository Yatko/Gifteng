/**
 * 
 */
package com.venefica.module.listings;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Adapter class for Listing List Adapter
 */
public class ListingListAdapter extends BaseAdapter implements OnClickListener{
	private List<AdDto> listings;
	private Context context;
	private boolean isGridView;
	private ViewHolder holder;
	public ListingListAdapter(Context context, List<AdDto> listings, boolean isGridView) {
		this.context = context;
		this.listings = listings;
		this.isGridView = isGridView;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return listings.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {		
		if (convertView == null) {
			holder  = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_listing_tile_item, parent, false);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtListingTileTitle);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtListingTilePrice);
			holder.imgBtnShare = (ImageButton) convertView.findViewById(R.id.imgBtnListingTileShare);
			holder.imgBtnShare.setOnClickListener(this);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgListingTileBg);
			holder.imgView.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtTitle.setText(listings.get(position).getTitle());
		holder.txtPrice.setText("USD ");
		holder.txtPrice.append(listings.get(position).getPrice().toString());
		holder.imgBtnShare.setContentDescription(listings.get(position).getTitle()+ " "+ listings.get(position).getDescription());
		holder.imgView.setContentDescription(listings.get(position).getId()+"");
//		txtPrice.append(" ");
//		txtPrice.append(listing.getCurrencyCode());
		if (this.listings.get(position).getImage() != null) {
			((VeneficaApplication) ((SearchListingsActivity)context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + this.listings.get(position).getImage().getUrl()
						, holder.imgView, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}else {
			((VeneficaApplication) ((SearchListingsActivity)context).getApplication())
				.getImgManager().loadImage("", holder.imgView, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}
		
		if (position == getCount()-3) {
			Log.d("last Item", ""+position);
			if(this.context instanceof SearchListingsActivity){
				((SearchListingsActivity)this.context).getMoreListings();
			}
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView txtTitle, txtPrice;
		ImageButton imgBtnShare;
		ImageView imgView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imgBtnListingTileShare) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, v.getContentDescription());
			sendIntent.setType("text/plain");
			context.startActivity(sendIntent);
		}else if (v.getId() == R.id.imgListingTileBg){
			Intent intent = new Intent(context, ListingDetailsActivity.class);
			intent.putExtra("ad_id", Long.parseLong(v.getContentDescription().toString()));
			int mode = SearchListingsActivity.getCURRENT_MODE();
			if (mode == SearchListingsActivity.ACT_MODE_DOWNLOAD_BOOKMARKS) {
				mode = ListingDetailsActivity.ACT_MODE_BOOKMARK_LISTINGS;
			} else if (mode == SearchListingsActivity.ACT_MODE_DOWNLOAD_MY_LISTINGS) {
				mode = ListingDetailsActivity.ACT_MODE_MY_LISTINGS_DETAILS;
			}else if (mode == SearchListingsActivity.ACT_MODE_SEARCH_BY_CATEGORY) {
				mode = ListingDetailsActivity.ACT_MODE_DOWNLOAD_LISTINGS_DETAILS;
			}
			intent.putExtra("act_mode", mode);
			context.startActivity(intent);
		}
	}

}
