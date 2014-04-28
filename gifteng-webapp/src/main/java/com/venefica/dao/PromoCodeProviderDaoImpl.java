/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.PromoCodeProvider;
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
public class PromoCodeProviderDaoImpl extends DaoBase<PromoCodeProvider> implements PromoCodeProviderDao {

    @Override
    public PromoCodeProvider get(Long providerId) {
        return getEntity(providerId);
    }

    @Override
    public List<PromoCodeProvider> getProviders() {
        List<PromoCodeProvider> providers = createQuery(""
                + "from " + getDomainClassName() + " pcp "
                + "where pcp.deleted = false "
                + "order by pcp.sortOrder asc"
                + "")
                .list();
        return providers;
    }
    
}
