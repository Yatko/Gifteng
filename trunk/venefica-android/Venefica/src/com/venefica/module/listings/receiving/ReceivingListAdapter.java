/**
 * 
 */
package com.venefica.module.listings.receiving;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.listings.post.PostListingActivity;
import com.venefica.module.main.R;
import com.venefica.services.RequestDto;
import com.venefica.services.RequestDto.RequestStatus;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 *
 */
public class ReceivingListAdapter extends BaseAdapter {

	private Context context;
	private List<RequestDto> requests;
	private static ViewHolder holder;
	/**
	 * constructor
	 */
	public ReceivingListAdapter(Context context, List<RequestDto> requests) {
		this.context = context;
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtStatus.setText(requests.get(position).getStatus().toString().toUpperCase());
		((VeneficaApplication) ((ReceivingListActivity)this.context).getApplication())
		.getImgManager().loadImage(requests.get(position).getUser().getAvatar() != null 
		? Constants.PHOTO_URL_PREFIX + requests.get(position).getUser().getAvatar().getUrl():"", 
				holder.imgListingCoverImg, this.context.getResources().getDrawable(R.drawable.icon_picture_white));
		return convertView;
	}

	private static class ViewHolder {
		ImageView imgListingCoverImg;
		TextView txtStatus;
	}
}
