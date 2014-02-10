package com.venefica.module.listings.browse;

import java.util.ArrayList;

/**
 * @author avinash
 * Class for category data
 */
public class CategoryData {

	private long catId;
	private long parentCatId;
	private ArrayList<CategoryData> subCategories;
	private String categoryName;
	/**
	 * @return the catId
	 */
	public long getCatId() {
		return catId;
	}
	/**
	 * @param catId the catId to set
	 */
	public void setCatId(long catId) {
		this.catId = catId;
	}
	/**
	 * @return the parentCatId
	 */
	public long getParentCatId() {
		return parentCatId;
	}
	/**
	 * @param parentCatId the parentCatId to set
	 */
	public void setParentCatId(long parentCatId) {
		this.parentCatId = parentCatId;
	}
	/**
	 * @return the subCategories
	 */
	public ArrayList<CategoryData> getSubCategories() {
		return subCategories;
	}
	/**
	 * @param subCategories the subCategories to set
	 */
	public void setSubCategories(ArrayList<CategoryData> subCategories) {
		this.subCategories = subCategories;
	}
	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
