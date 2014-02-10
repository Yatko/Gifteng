/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.ShippingBox;
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
public class ShippingBoxDaoImpl extends DaoBase<ShippingBox> implements ShippingBoxDao {

    @Override
    public ShippingBox get(Long boxId) {
        return getEntity(boxId);
    }

    @Override
    public List<ShippingBox> getAllShippingBoxes() {
        List<ShippingBox> issues = createQuery(""
                + "from " + getDomainClassName() + " sb "
                + "where sb.deleted = false "
                + "order by sortOrder asc"
                + "")
                .list();
        return issues;
    }
    
}
