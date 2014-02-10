package com.venefica.dao;

import com.venefica.model.BusinessCategory;
import java.util.List;

/**
 * Data access interface for {@link BusinessCategory} entity.
 *
 * @author gyuszi
 */
public interface BusinessCategoryDao {

    /**
     * Returns the list of categories.
     *
     * @return list of business categories
     */
    List<BusinessCategory> getCategories();

    /**
     * Finds the category by its name.
     *
     * @param name the name of the category
     * @return category object or null
     */
    BusinessCategory findByName(String name);

    /**
     * Saves the category object in the database.
     *
     * @param category the category object
     * @return the id of stored category
     */
    Long save(BusinessCategory category);

    /**
     * Returns true if at least one category exists in the database.
     *
     * @return
     */
    boolean hasCategories();

    /**
     * Returns category by its id;
     *
     * @param categoryId the id of the category
     * @return
     */
    BusinessCategory get(Long categoryId);
}
