package com.venefica.module.listings;

import java.util.List;

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
import com.venefica.services.CommentDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Adapter for comment list on DetailView
 */
public class CommentListAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private List<CommentDto> comments;
	private TextView txtComment, txtName, txtTime;
	private ImageView imgProfile;
	private ImageButton imgBtnEdit;
	private static ViewHolder holder;
	/**
	 * Constructor 
	 */
	public CommentListAdapter(Context context, List<CommentDto> comments) {
		this.context = context;
		this.comments = comments;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.comments.size();
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
			convertView = inflater.inflate(R.layout.view_listing_comments, parent, false);convertView.setTag(holder);
			txtComment = (TextView) convertView.findViewById(R.id.txtCommentListComment);
			txtName = (TextView) convertView.findViewById(R.id.txtCommentListSender);
			txtTime = (TextView) convertView.findViewById(R.id.txtCommentListTime);
			imgProfile = (ImageView) convertView.findViewById(R.id.imgCommentListProfileImg);
			imgBtnEdit = (ImageButton) convertView.findViewById(R.id.imgBtnCommentListEdit);
			holder.txtComment = txtComment;
			holder.txtName = txtName;
			holder.txtTime = txtTime;
			holder.imgProfile = imgProfile;
			holder.imgBtnEdit = imgBtnEdit;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtComment.setText(comments.get(position).getText());
		holder.txtName.setText(comments.get(position).getPublisherFullName());
		if (comments.get(position).getCreatedAt() != null) {
			holder.txtTime.setText(Utility.convertDateTimeToString(comments.get(position).getCreatedAt()));
		}		
		((VeneficaApplication) ((ListingDetailsActivity)this.context).getApplication())
				.getImgManager().loadImage(Constants.PHOTO_URL_PREFIX +comments.get(position)
				.getPublisherAvatarUrl(), holder.imgProfile, this.context.getResources().getDrawable(R.drawable.icon_picture_white));
		if(position == 0){
			holder.imgBtnEdit.setVisibility(View.VISIBLE);
			holder.imgBtnEdit.setOnClickListener(this);				
		}else {
			holder.imgBtnEdit.setVisibility(View.GONE);			
		}		
		return convertView;
	}

	private static class ViewHolder {
		ImageView imgProfile;
		TextView txtComment;
		TextView txtName;
		TextView txtTime;
		ImageButton imgBtnEdit;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgBtnCommentListEdit) {
			((ListingDetailsActivity)this.context).setMessageLayoutVisiblity(true);
		}/* else if (comments.size() > 1 
				&& (view.getId() == R.id.txtCommentListComment || view.getId() == R.id.imgCommentListProfileImg)) {
			((ListingDetailsActivity)this.context).expandComments();
		}*/
	}
}
