/**
 * 
 */
package com.venefica.module.user;

import java.util.ArrayList;

import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.services.ReviewDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author avinash
 *
 */
public class ReviewsExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<ReviewDto> reviewsList;
	private ReviewViewHolder reviewViewHolder;
	/**
	 * Constructor
	 */
	public ReviewsExpandableListAdapter(Context context, ArrayList<ReviewDto> reviews) {
		this.context = context;
		this.reviewsList = reviews;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		reviewViewHolder = new ReviewViewHolder();
		if (convertView == null) {
			LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_reviews_list_item, null);
		}
		if (groupPosition == 0) {
			reviewViewHolder.txtUserName.setText(reviewsList.get(childPosition).getFrom().getFirstName() + " "
					+ (reviewsList.get(childPosition).getFrom().getLastName())) ;
//			reviewViewHolder.txtReview.setText(context.getResources().getText(
//					R.string.label_detail_listing_member_since).toString());
//			reviewViewHolder.txtMemberInfo.append(" ");
			if (reviewsList.get(childPosition).getReviewedAt() != null) {
				reviewViewHolder.txtReviwAt.append(
						Utility.convertShortDateToString(reviewsList.get(childPosition).getReviewedAt()));
			}
			if (reviewsList.get(childPosition).getFrom().getAvatar() != null
					&& reviewsList.get(childPosition).getFrom().getAvatar().getUrl() != null) {
				((VeneficaApplication) ((UserProfileActivity)this.context).getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ reviewsList.get(childPosition).getFrom().getAvatar().getUrl(),
								reviewViewHolder.imgProfile,
								context.getResources().getDrawable(
										R.drawable.icon_picture_white));
			}
			
		}
		return convertView;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return reviewsList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView groupView ;

		if (convertView == null) {
			LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_profile_exp_list_header_row, null);
		}

		groupView = (TextView) convertView.findViewById(R.id.txtActProfileGroupName);
		//set group title as per result count
		StringBuffer titleText = new StringBuffer();
		if (groupPosition == 0) {
			titleText.append(context.getResources().getString(R.string.g_label_reviews));
		}
		groupView.setText(titleText.toString());
		
		return convertView;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	private static class ReviewViewHolder{
		ImageView imgProfile;
		TextView txtUserName;
		TextView txtReviwAt;
		TextView txtReview;		
	}
}
