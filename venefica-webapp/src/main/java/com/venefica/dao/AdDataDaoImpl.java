/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Ad;
import com.venefica.model.AdData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AdDataDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class AdDataDaoImpl extends DaoBase<AdData> implements AdDataDao {

    @Override
    public Long save(AdData adData) {
        return saveEntity(adData);
    }
    
    @Override
    public void update(AdData adData) {
        updateEntity(adData);
    }

    @Override
    public AdData getByAd(Long adId) {
        return (AdData) createQuery(""
                + "select a.adData "
                + "from " + Ad.class.getSimpleName() + " a where "
                + "a.id = :adId"
                + "")
                .setParameter("adId", adId)
                .uniqueResult();
    }
}
