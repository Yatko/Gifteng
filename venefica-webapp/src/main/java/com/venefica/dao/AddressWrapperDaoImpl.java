package com.venefica.dao;

import com.venefica.model.AddressWrapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AddressWrapperDao} interface.
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class AddressWrapperDaoImpl extends DaoBase<AddressWrapper> implements AddressWrapperDao {

    @Override
    public Long save(AddressWrapper addressWrapper) {
        return saveEntity(addressWrapper);
    }
}
