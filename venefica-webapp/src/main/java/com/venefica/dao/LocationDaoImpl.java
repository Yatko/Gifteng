/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Location;
import java.util.List;
import org.apache.commons.lang.StringUtils;
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
        
        String hql = ""
                + "select l "
                + "from " + getDomainClassName() + " l "
                + "where l.zipcode like '%' || :zipcode || '%' "
                + "";
        List<Location> locations = createQuery(hql)
                .setParameter("zipcode", zipcode)
                .setMaxResults(1)
                .list();
        
        if ( locations == null || locations.isEmpty() ) {
            //trying to find without 0 in front
            zipcode = StringUtils.stripStart(zipcode, "0");
            locations = createQuery(hql)
                    .setParameter("zipcode", zipcode)
                    .setMaxResults(1)
                    .list();
        }
        
        return locations.isEmpty() ? null : locations.get(0);
    }
    
}
