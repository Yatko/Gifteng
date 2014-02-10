/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Shipping;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ShippingDaoImpl extends DaoBase<Shipping> implements ShippingDao {

    @Override
    public Long save(Shipping shipping) {
        shipping.setCreatedAt(new Date());
        return saveEntity(shipping);
    }

    @Override
    public void update(Shipping shipping) {
        shipping.setLastUpdatedAt(new Date());
        updateEntity(shipping);
    }

    @Override
    public Shipping get(Long shippingId) {
        return getEntity(shippingId);
    }

    @Override
    public Shipping getByRequest(Long requestId) {
        return (Shipping) createQuery(""
                + "from " + getDomainClassName() + " s "
                + "where "
                + "s.deleted = false and "
                + "s.request.id = :requestId"
                + "")
                .setParameter("requestId", requestId)
                .uniqueResult();
    }

    @Override
    public void delete(Long shippingId) {
        Shipping shipping = getEntity(shippingId);
        shipping.setDeleted(true);
        shipping.setDeletedAt(new Date());
        updateEntity(shipping);
    }

    @Override
    public List<Shipping> getShippings() {
        return createQuery(""
                + "from " + getDomainClassName() + " s "
                + "where "
                + "s.deleted = false and "
                + "s.request.deleted = false and "
                + "s.request.user.deleted = false and "
                + "s.request.ad.creator.deleted = false"
                + "")
                .list();
    }
    
}
