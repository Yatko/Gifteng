package com.venefica.module.user;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.utils.BadgeView;
import com.venefica.module.main.R;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Fragment to display user profile details
 */
public class ViewProfileDetailsFragment extends SherlockFragment {

	/**
	 * @author avinash
	 * listener to communicate with activity
	 */
	public interface ViewProfileDetailsListener {
		/**
		 * Method to get user details
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_show_profile_details, container, false);
		view.findViewById(R.id.imgBtnUserViewFollow).setVisibility(View.GONE);
		
		 //user details
        txtUserName = (TextView) view.findViewById(R.id.txtUserViewUserName);
        txtMemberInfo = (TextView) view.findViewById(R.id.txtUserViewMemberInfo);
        txtAddress = (TextView) view.findViewById(R.id.txtUserViewAddress);
        profImgView = (ImageView) view.findViewById(R.id.imgUserViewProfileImg);
        setUserDto(((VeneficaApplication) getActivity().getApplication()).getUser());
        imgButtonMsg = (ImageButton) view.findViewById(R.id.imgBtnUserViewSendMsg);
        badgeView = new BadgeView(getActivity(), imgButtonMsg);
        badgeView.setText("2");
        badgeView.setTextColor(Color.BLUE);
        badgeView.setBadgeBackgroundColor(Color.YELLOW);
//        badgeView.setTextSize(12);
        badgeView.toggle();
		return view;
	}
	
	/**
	 * Set user info
	 * @param userDto
	 */
	public void setUserDto(UserDto userDto){
		this.userDto = userDto;
		if (userDto != null) {
			txtUserName.setText(userDto.getFirstName() + " "
					+ (userDto.getLastName()));
			txtMemberInfo.setText(getResources().getText(
					R.string.label_detail_listing_member_since).toString());
			txtMemberInfo.append(" ");
			if (userDto.getJoinedAt() != null) {
				txtMemberInfo.append(Utility.convertShortDateToString(userDto
						.getJoinedAt()));
			}
			txtAddress.setText(userDto.getCity() + ", " + userDto.getCounty());
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
}
