/**
 * 
 */
package com.venefica.module.listings.mylistings;

import java.util.List;

import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.services.AdDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author avinash
 *
 */
public class MyListingsAdapter extends BaseAdapter {

	private List<AdDto> listings;
	private Context context;
	private ViewHolder holder;
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (this.listings.get(position).getImage() != null) {
			((VeneficaApplication) ((MyListingsActivity)context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + this.listings.get(position).getImage().getUrl()
						, holder.imgListingImage, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}else {
			((VeneficaApplication) ((MyListingsActivity)context).getApplication())
				.getImgManager().loadImage("", holder.imgListingImage, context.getResources().getDrawable(R.drawable.icon_picture_white));
		}
		
		holder.txtStatus.setText(this.listings.get(position).getStatus().toString());
		
		return convertView;
	}

	static class ViewHolder{
		ImageView imgListingImage;
		TextView txtStatus;
	}
}
