package com.venefica.dao;

import com.venefica.model.AddressWrapper;

/**
 * Data access interface for {@link AddressWrapper} entity.
 *
 * @author gyuszi
 */
public interface AddressWrapperDao {

    /**
     * Saves or updates the address in the database.
     *
     * @param addressWrapper addressWrapper to save
     */
    void saveOrUpdate(AddressWrapper addressWrapper);
}
