package com.venefica.module.dashboard;

/**
 * Callback interface for {@link SlideMenuView}.
 * 
 * @author avinash
 *
 */
public interface ISlideMenuCallback {

	/**
	 * Validation clicking on side navigation item.
	 * @param itemId id of selected item
	 */
	public void onSideNavigationItemClick(int itemId);

}
