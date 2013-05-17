package com.venefica.module.dashboard;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.venefica.module.listings.browse.SearchListingsActivity;
import com.venefica.module.main.R;
import com.venefica.module.user.UserDto;
import com.venefica.module.user.UserProfileActivity;
import com.venefica.module.utils.Utility;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * View of displaying side navigation.
 * 
 * @author avinash
 * 
 */
public class SlideMenuView extends LinearLayout implements View.OnClickListener {
	private static final String LOG_TAG = SlideMenuView.class.getSimpleName();

	private RelativeLayout navigationMenu;
	private ListView listView;
	private ViewGroup userView;

	/**
     * Text view to show user details
     */
    private TextView txtUserName, txtMemberInfo, txtAddress;
    private ImageView profImgView;
	private ISlideMenuCallback callback;
	private ArrayList<SlideMenuItem> menuItems;

	/**
	 * Constructor of {@link SlideMenuView}.
	 * @param context
	 */
	public SlideMenuView(Context context) {
		super(context);
		load();
	}

	/**
	 * Constructor of {@link SlideMenuView}.
	 * @param context
	 * @param attrs
	 */
	public SlideMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		load();
	}

	/**
	 * Loading of side navigation view.
	 */
	private void load() {
		if (isInEditMode()) {
			return;
		}
		initView();
	}

	/**
	 * Initialization layout of side menu.
	 */
	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.side_navigation, this, true);
		userView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_user_details, null, false);
//		userView.setPadding(0, (int) getResources().getDimension(R.dimen.abs__action_bar_default_height), 0, 0);
		 //user details
        txtUserName = (TextView) userView.findViewById(R.id.txtUserViewUserName);
        txtMemberInfo = (TextView) userView.findViewById(R.id.txtUserViewMemberInfo);
        txtAddress = (TextView) userView.findViewById(R.id.txtUserViewAddress);
        profImgView = (ImageView) userView.findViewById(R.id.imgUserViewProfileImg);
        userView.findViewById(R.id.imgBtnUserViewFollow).setVisibility(INVISIBLE);
        userView.findViewById(R.id.imgBtnUserViewSendMsg).setVisibility(INVISIBLE);
        userView.setOnClickListener(this);
		navigationMenu = (RelativeLayout) findViewById(R.id.side_navigation_menu);
		listView = (ListView) findViewById(R.id.side_navigation_listview);
		listView.addHeaderView(userView);
		listView.setFastScrollEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (callback != null && position > 0) {
					callback.onSideNavigationItemClick(menuItems.get(position-1).getId());
				}
				hideMenu();
			}
		});
	}

	/**
	 * Setup of {@link ISlideMenuCallback} for callback of item click.
	 * @param callback
	 */
	public void setMenuClickCallback(ISlideMenuCallback callback) {
		this.callback = callback;
	}

	/**
	 * Setup of side menu items.
	 * @param menu - resource ID
	 */
	public void setMenuItems(int menu) {
		parseXml(menu);
		if (menuItems != null && menuItems.size() > 0) {
			listView.setAdapter(new SideNavigationAdapter());
		}
	}

	/**
	 * 
	 */
	public void setBackgroundResource(int resource) {
		listView.setBackgroundResource(resource);
	}

	/**
	 * Show side navigation menu.
	 */
	public void showMenu() {
		navigationMenu.setVisibility(View.VISIBLE);
		navigationMenu.startAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.side_navigation_in_from_left));
	}

	/**
	 * Hide side navigation menu.
	 */
	public void hideMenu() {
		navigationMenu.setVisibility(View.GONE);
		navigationMenu.startAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.side_navigation_out_to_left));
	}

	/**
	 * Show/Hide side navigation menu depending on visibility.
	 */
	public void toggleMenu() {
		if (isShown()) {
			hideMenu();
		} else {
			showMenu();
			setUserDetails(((VeneficaApplication) ((SearchListingsActivity) getContext())
					.getApplication()).getUser());
		}
	}
	
	@Override
	public boolean isShown() {
		return navigationMenu.isShown();
	}

	/**
	 * Parse XML describe menu.
	 * @param menu - resource ID
	 */
	private void parseXml(int menu) {
		menuItems = new ArrayList<SlideMenuItem>();
		try {
			XmlResourceParser xrp = getResources().getXml(menu);
			xrp.next();
			int eventType = xrp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String elemName = xrp.getName();
					if (elemName.equals("item")) {
						String textId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"title");
						String iconId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"icon");
						String resId = xrp.getAttributeValue(
								"http://schemas.android.com/apk/res/android",
								"id");
						SlideMenuItem item = new SlideMenuItem();
						item.setId(Integer.valueOf(resId.replace("@", "")));
						item.setText(resourceIdToString(textId));
						item.setIcon(Integer.valueOf(iconId.replace("@", "")));
						menuItems.add(item);
					}
				}
				eventType = xrp.next();
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, e);
		}
	}

	/**
	 * Convert resource ID to String.
	 * @param text
	 * @return
	 */
	private String resourceIdToString(String resId) {
		if (!resId.contains("@")) {
			return resId;
		} else {
			String id = resId.replace("@", "");
			return getResources().getString(Integer.valueOf(id));
		}
	}

	private class SideNavigationAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public SideNavigationAdapter() {
			inflater = LayoutInflater.from(getContext());
		}

		@Override
		public int getCount() {
			return menuItems.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.side_navigation_item, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.side_navigation_item_icon);
				holder.text = (TextView) convertView.findViewById(R.id.side_navigation_item_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.image.setImageResource(menuItems.get(position).getIcon());
			holder.text.setText(menuItems.get(position).getText());
			return convertView;
		}

		class ViewHolder {
			TextView text;
			ImageView image;
		}

	}
	public void setUserDetails(UserDto user){
		if (user != null) {
			if (user.getAvatar() != null && user.getAvatar().getUrl() != null) {
				((VeneficaApplication) ((SearchListingsActivity) getContext())
						.getApplication()).getImgManager().loadImage(
						Constants.PHOTO_URL_PREFIX + user.getAvatar().getUrl(),
						profImgView,
						getResources().getDrawable(
								R.drawable.icon_picture_white));
			}
			txtUserName.setText(user.getFirstName() + " "
					+ (user.getLastName()));
			txtMemberInfo
					.setText(getResources().getText(
							R.string.label_detail_listing_member_since)
							.toString());
			txtMemberInfo.append(" ");
			if (user.getJoinedAt() != null) {
				txtMemberInfo.append(Utility.convertShortDateToString(user
						.getJoinedAt()));
			}
			txtAddress.setText(user.getCity() + ", " + user.getCounty());
		}
	}

	@Override
	public void onClick(View view) {
		Intent accountIntent = new Intent(getContext(), UserProfileActivity.class);
		accountIntent.putExtra("act_mode",UserProfileActivity.ACT_MODE_VIEW_PROFILE);
		getContext().startActivity(accountIntent);
	}
}
