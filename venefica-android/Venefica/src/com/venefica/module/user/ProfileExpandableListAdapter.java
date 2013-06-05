/**
 * 
 */
package com.venefica.module.user;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;


/**
 * @author avinash
 * Adapter for Reviews, Following and Follower
 */
public class ProfileExpandableListAdapter extends BaseExpandableListAdapter implements OnClickListener {

	public interface OnProfileListButtonListener{
		public void onFollowButtonClick(long userId, boolean inFollowings);
	}
	private Context context;
	private ArrayList<ProfileGroup> profileGroups;
	private UserDetailViewHolder userDetailViewHolder;
	private OnProfileListButtonListener onProfileListButtonListener;
	
	public ProfileExpandableListAdapter(Context context, ArrayList<ProfileGroup> profileGroups){
		this.context = context;
		this.profileGroups = profileGroups;
		this.onProfileListButtonListener = (OnProfileListButtonListener)context;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {		
		return profileGroups.get(groupPosition).getUsers().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		userDetailViewHolder = new UserDetailViewHolder();
		if (convertView == null) {
			LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_user_details, null);
			userDetailViewHolder.userName = (TextView) convertView.findViewById(R.id.txtUserViewUserName);
			userDetailViewHolder.txtMemberInfo = (TextView) convertView.findViewById(R.id.txtUserViewMemberInfo);
//			userDetailViewHolder.txtAddress = (TextView) convertView.findViewById(R.id.txtUserViewAddress);
			userDetailViewHolder.profileImg = (ImageView) convertView.findViewById(R.id.imgUserViewProfileImg);
			userDetailViewHolder.btnFollow = (Button) convertView.findViewById(R.id.imgBtnUserViewFollow);
			userDetailViewHolder.btnFollow.setOnClickListener(this);
	        convertView.findViewById(R.id.imgBtnUserViewSendMsg).setVisibility(View.INVISIBLE);
	        convertView.setTag(userDetailViewHolder);

		} else {
			userDetailViewHolder = (UserDetailViewHolder) convertView.getTag();
		}
		//set details
		userDetailViewHolder.userName.setText(profileGroups.get(groupPosition).getUsers().get(childPosition).getFirstName() + " "
				+ (profileGroups.get(groupPosition).getUsers().get(childPosition).getLastName())) ;
		userDetailViewHolder.txtMemberInfo.setText(context.getResources().getText(
				R.string.label_detail_listing_member_since).toString());
		userDetailViewHolder.txtMemberInfo.append(" ");
		if (profileGroups.get(groupPosition).getUsers().get(childPosition).getJoinedAt() != null) {
			userDetailViewHolder.txtMemberInfo.append(
					Utility.convertShortDateToString(profileGroups.get(groupPosition).getUsers().get(childPosition)
					.getJoinedAt()));
		}
		if (profileGroups.get(groupPosition).getUsers().get(childPosition).getAvatar() != null
				&& profileGroups.get(groupPosition).getUsers().get(childPosition).getAvatar().getUrl() != null) {
			((VeneficaApplication) ((ProfileDetailActivity)this.context).getApplication())
					.getImgManager().loadImage(
							Constants.PHOTO_URL_PREFIX
									+ profileGroups.get(groupPosition).getUsers().get(childPosition).getAvatar().getUrl(),
							userDetailViewHolder.profileImg,
							context.getResources().getDrawable(
									R.drawable.icon_picture_white));
		}
		if (profileGroups.get(groupPosition).getUsers().get(childPosition).isInFollowings()) {
			userDetailViewHolder.btnFollow.setText(context.getResources().getString(R.string.g_label_unfollow));			
		} else {
			userDetailViewHolder.btnFollow.setText(context.getResources().getString(R.string.label_follow));
		}
		
		userDetailViewHolder.btnFollow.setTag(profileGroups.get(groupPosition).getUsers().get(childPosition).isInFollowings());
		userDetailViewHolder.btnFollow.setContentDescription(profileGroups.get(groupPosition).getUsers().get(childPosition).getId()+"");
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return profileGroups.get(groupPosition).getUsers().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return profileGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		System.out.println("Group size : "+ profileGroups.size());
		return profileGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
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
		if(profileGroups.get(groupPosition).getGroupName().equalsIgnoreCase(context.getResources().getString(R.string.g_label_following))){
			titleText.append(context.getResources().getString(R.string.g_label_following));
		} else if (profileGroups.get(groupPosition).getGroupName().equalsIgnoreCase(context.getResources().getString(R.string.g_label_followers))){
			titleText.append(context.getResources().getString(R.string.g_label_followers));
		}
		groupView.setText(titleText.toString());
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private static class UserDetailViewHolder{
		ImageView profileImg;
		TextView userName;
		TextView txtMemberInfo;
		Button btnFollow;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imgBtnUserViewFollow:
			onProfileListButtonListener.onFollowButtonClick(Long.parseLong(view.getContentDescription().toString())
					, Boolean.parseBoolean(view.getTag().toString()));
			break;

		default:
			break;
		}
	}
}
