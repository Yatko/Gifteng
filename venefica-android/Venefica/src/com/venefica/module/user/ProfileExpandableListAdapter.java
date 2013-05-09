/**
 * 
 */
package com.venefica.module.user;

import java.util.ArrayList;

import com.venefica.module.listings.ListingDetailsActivity;
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
 * Adapter for Reviews, Following and Follower
 */
public class ProfileExpandableListAdapter extends BaseExpandableListAdapter {

	private ArrayList<UserDto> userFollowingList;
    private ArrayList<UserDto> userFollowerList;
    private ArrayList<ReviewDto> reviewList;
	private Context context;
	private UserDetailViewHolder userDetailViewHolder;
	public ProfileExpandableListAdapter(Context context, ArrayList<UserDto> userFollowingList
			, ArrayList<UserDto> userFollowerList, ArrayList<ReviewDto> reviewList){
		this.userFollowingList = userFollowingList;
		this.userFollowerList = userFollowerList;
		this.reviewList = reviewList;
		this.context = context;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {		
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		userDetailViewHolder = new UserDetailViewHolder();
		if (convertView == null) {
			LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_user_details, null);
		}
		userDetailViewHolder.imgButtonMsg.setVisibility(View.INVISIBLE);
		/*if (groupPosition == 0) {
			profileListGroups.getReviewList().size();
		} else*/ if (groupPosition == 0) {
			userDetailViewHolder.userName.setText(userFollowingList.get(childPosition).getFirstName() + " "
					+ (userFollowingList.get(childPosition).getLastName())) ;
			userDetailViewHolder.txtMemberInfo.setText(context.getResources().getText(
					R.string.label_detail_listing_member_since).toString());
			userDetailViewHolder.txtMemberInfo.append(" ");
			if (userFollowingList.get(childPosition).getJoinedAt() != null) {
				userDetailViewHolder.txtMemberInfo.append(Utility.convertShortDateToString(userFollowingList.get(childPosition)
						.getJoinedAt()));
			}
			if (userFollowingList.get(childPosition).getAvatar() != null
					&& userFollowingList.get(childPosition).getAvatar().getUrl() != null) {
				((VeneficaApplication) ((UserProfileActivity)this.context).getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ userFollowingList.get(childPosition).getAvatar().getUrl(),
								userDetailViewHolder.profileImg,
								context.getResources().getDrawable(
										R.drawable.icon_picture_white));
			}
			
		} else if (groupPosition == 1) {
			userDetailViewHolder.userName.setText(userFollowerList.get(childPosition).getFirstName() + " "
					+ (userFollowerList.get(childPosition).getLastName())) ;
			userDetailViewHolder.txtMemberInfo.setText(context.getResources().getText(
					R.string.label_detail_listing_member_since).toString());
			userDetailViewHolder.txtMemberInfo.append(" ");
			if (userFollowerList.get(childPosition).getJoinedAt() != null) {
				userDetailViewHolder.txtMemberInfo.append(Utility.convertShortDateToString(userFollowerList.get(childPosition)
						.getJoinedAt()));
			}
			
			if (userFollowerList.get(childPosition).getAvatar() != null
					&& userFollowerList.get(childPosition).getAvatar().getUrl() != null) {
				((VeneficaApplication) ((UserProfileActivity)this.context).getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ userFollowerList.get(childPosition).getAvatar().getUrl(),
								userDetailViewHolder.profileImg,
								context.getResources().getDrawable(
										R.drawable.icon_picture_white));
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		/*if (groupPosition == 0) {
			profileListGroups.getReviewList().size();
		} else*/ if (groupPosition == 0 && userFollowingList != null) {
			return userFollowingList.size();
		} else if (groupPosition == 1 && userFollowerList != null) {
			return userFollowerList.size();
		}
//		return profileListGroups.get(groupPosition).getUserList().size();
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		return 2;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

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
			titleText.append(context.getResources().getString(R.string.g_label_following));
		} else if (groupPosition == 1) {
			titleText.append(context.getResources().getString(R.string.g_label_followers));
		}
		groupView.setText(titleText.toString());
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	private static class UserDetailViewHolder{
		ImageView profileImg;
		TextView userName;
		TextView txtMemberInfo;
		ImageButton imgButtonMsg;
		Button btnFollow;
	}
}
