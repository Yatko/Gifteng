/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.StaticListPromoCode;
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
public class StaticListPromoCodeDaoImpl extends DaoBase<StaticListPromoCode> implements StaticListPromoCodeDao {

    @Override
    public void update(StaticListPromoCode staticListPromoCode) {
        staticListPromoCode.setUsed(true);
        staticListPromoCode.setUsedAt(new Date());
        updateEntity(staticListPromoCode);
    }

    @Override
    public StaticListPromoCode getOneByAd(Long adId) {
        List<StaticListPromoCode> codes = createQuery(""
                + "from " + getDomainClassName() + " pc "
                + "where "
                + "pc.used = false and "
                + "pc.ad.id = :adId"
                + "")
                .setParameter("adId", adId)
                .list();
        if ( codes == null || codes.isEmpty() ) {
            return null;
        }
        return codes.get(0);
    }
    
}
