/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Location;
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
public class LocationDaoImpl extends DaoBase<Location> implements LocationDao {

    @Override
    public Location findByZipcode(String zipcode) {
        if (zipcode == null) {
            throw new NullPointerException("zipcode");
        }
        
        List<Location> locations = createQuery(""
                + "select l "
                + "from " + getDomainClassName() + " l "
                + "where l.zipcode like '%' || :zipcode || '%' "
                + "")
                .setParameter("zipcode", zipcode)
                .setMaxResults(1)
                .list();
        return locations.isEmpty() ? null : locations.get(0);
    }
    
}
