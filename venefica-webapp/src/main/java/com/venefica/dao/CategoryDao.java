package com.venefica.dao;

import java.util.List;

import com.venefica.model.Category;


/**
 * Data access interface for {@link Category} entity.
 * 
 * @author Sviatoslav Grebenchukov
 */
public interface CategoryDao {

	/**
	 * Returns the list of subcategories.
	 * 
	 * @param parentCategoryId
	 *            the id of the parent category
	 * @return list of subcategories
	 */
	List<Category> getSubcategories(Long parentCategoryId);

	/**
	 * Finds the category by its name.
	 * 
	 * @param name
	 *            the name of the category
	 * @return category object or null
	 */
	Category findByName(String name);

	/**
	 * Saves the category object in the database.
	 * 
	 * @param category
	 *            the category object
	 * @return the id of stored category
	 */
	Long save(Category category);

	/**
	 * Returns true if at least one category exists in the database.
	 */
	boolean hasCategories();

	/**
	 * Returns category by its id;
	 * 
	 * @param categoryId
	 *            the id of the category
	 * @return
	 */
	Category get(Long categoryId);
}
