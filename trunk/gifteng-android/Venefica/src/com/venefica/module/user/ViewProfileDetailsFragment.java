package com.venefica.module.user;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.utils.BadgeView;
import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.services.RatingDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash Fragment to display user profile details
 */
public class ViewProfileDetailsFragment extends SherlockFragment {

	/**
	 * @author avinash listener to communicate with activity
	 */
	public interface ViewProfileDetailsListener {
		/**
		 * Method to get user details
		 * 
		 * @return UserDto
		 */
		public UserDto getUserDetails();

		public void onViewMessages();
	}

	/**
	 * user info
	 */
	private UserDto userDto;
	/**
	 * view to show user details
	 */
	private TextView txtUserName, txtMemberInfo, txtAddress;
	private ImageView profImgView;
	private ImageButton imgButtonMsg;
	private BadgeView badgeView;

	private ExpandableListView userExpandableListView;
	private ProfileExpandableListAdapter userExpandableListAdapter;
//	private ArrayList<UserDto> userFollowingList;
//	private ArrayList<UserDto> userFollowerList;
	
	private ExpandableListView reviewExpandableListView;
	private ArrayList<RatingDto> userReviewList;
	private ReviewsExpandableListAdapter reviewsExpandableListAdapter;
	
	private ArrayList<ProfileGroup> profileGroups;
	private boolean isAttached = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		userFollowingList = new ArrayList<UserDto>();
//		userFollowerList = new ArrayList<UserDto>();
		userReviewList = new ArrayList<RatingDto>();
		profileGroups = new ArrayList<ProfileGroup>();
		userExpandableListAdapter = new ProfileExpandableListAdapter(getActivity(), profileGroups);
		
		reviewsExpandableListAdapter = new ReviewsExpandableListAdapter(getActivity(), userReviewList);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_show_profile_details,
				container, false);
		view.findViewById(R.id.imgBtnUserViewFollow).setVisibility(View.GONE);

		//Reviews
		reviewExpandableListView = (ExpandableListView) view.findViewById(R.id.expListViewActUserProfieReviews);
		reviewExpandableListView.setAdapter(reviewsExpandableListAdapter);
		//expandable list view follower followings
		userExpandableListView = (ExpandableListView) view.findViewById(R.id.expListViewActUserProfie);
		userExpandableListView.setAdapter(userExpandableListAdapter);		
		
		// user details
		txtUserName = (TextView) view.findViewById(R.id.txtUserViewUserName);
		txtMemberInfo = (TextView) view
				.findViewById(R.id.txtUserViewMemberInfo);
		txtAddress = (TextView) view.findViewById(R.id.txtUserViewAddress);
		profImgView = (ImageView) view.findViewById(R.id.imgUserViewProfileImg);
		setUserDto(((VeneficaApplication) getActivity().getApplication())
				.getUser());
		imgButtonMsg = (ImageButton) view
				.findViewById(R.id.imgBtnUserViewSendMsg);
		badgeView = new BadgeView(getActivity(), imgButtonMsg);
		badgeView.setText("2");
		badgeView.setTextColor(Color.BLUE);
		badgeView.setBadgeBackgroundColor(Color.YELLOW);
		// badgeView.setTextSize(12);
		badgeView.toggle();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		isAttached = true;
	}
	@Override
	public void onDetach() {
		super.onDetach();
		isAttached = false;
	}
	/**
	 * Set user info
	 * 
	 * @param userDto
	 */
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
		if (userDto != null && isAttached) {
			txtUserName.setText(userDto.getFirstName() + " "
					+ (userDto.getLastName()));
			txtMemberInfo.setText(getResources().getText(
					R.string.label_detail_listing_member_since).toString());
			txtMemberInfo.append(" ");
			if (userDto.getJoinedAt() != null) {
				txtMemberInfo.append(Utility.convertShortDateToString(userDto
						.getJoinedAt()));
			}
			if (userDto.getAddress() != null) {
				txtAddress.setText(userDto.getAddress().getCity() + ", " + userDto.getAddress().getCounty());
			}			
			if (userDto.getAvatar() != null
					&& userDto.getAvatar().getUrl() != null) {
				((VeneficaApplication) getActivity().getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ userDto.getAvatar().getUrl(),
								profImgView,
								getResources().getDrawable(
										R.drawable.icon_picture_white));
			}
		}
	}

	/**
	 * Method to update followings list
	 * @param followingsList
	 */
	public void setFollowings(List<UserDto> followingsList) {
		if(isAttached){
	//		userFollowingList.clear();
	//		userFollowingList.addAll(followingsList);
			/*ProfileGroup profileGroup = new ProfileGroup(); 
			profileGroup.setGroupName(getActivity().getResources().getString(R.string.g_label_following));
			profileGroup.setUsers(followingsList);
			profileGroups.add(profileGroup);*/
			
			ProfileGroup profileGroup1 = new ProfileGroup(); 
			profileGroup1.setGroupName(getActivity().getResources().getString(R.string.g_label_followers));
			profileGroup1.setUsers(followingsList);
			profileGroups.add(profileGroup1);
			userExpandableListAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Method to update followers
	 * @param followersList
	 */
	public void setFollowers(List<UserDto> followersList) {
		if(isAttached){
	//		userFollowerList.clear();
	//		userFollowerList.addAll(followersList);
			ProfileGroup profileGroup = new ProfileGroup(); 
			profileGroup.setGroupName(getActivity().getResources().getString(R.string.g_label_followers));
			profileGroup.setUsers(followersList);
			profileGroups.add(profileGroup);
			userExpandableListAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * Method to update reviews
	 * @param reviewsList
	 */
	public void setReviews(List<RatingDto> reviewsList) {
		if(isAttached){
			reviewsList.clear();
			reviewsList.addAll(reviewsList);
			reviewsExpandableListAdapter.notifyDataSetChanged();
		}
	}
}
