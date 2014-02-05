/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Ad;
import com.venefica.model.AdData;
import com.venefica.model.BusinessAdData;
import com.venefica.model.MemberAdData;
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
    public MemberAdData getMemberAdDataByAd(Long adId) {
        Object result = createSQLQuery(""
                + "select mad.*, addt.* "
                + "from " + MemberAdData.TABLE_NAME + " mad "
                + "inner join " + Ad.TABLE_NAME + " a on a.adData_id = mad.id "
                + "inner join " + AdData.TABLE_NAME + " addt on addt.id = mad.id "
                + "where a.id = :adId"
                + "")
                .addEntity(MemberAdData.class)
                .setParameter("adId", adId)
                .uniqueResult();
        return (MemberAdData) result;
    }
    
    @Override
    public BusinessAdData getBusinessAdDataByAd(Long adId) {
        Object result = createSQLQuery(""
                + "select bad.*, addt.* "
                + "from " + BusinessAdData.TABLE_NAME + " bad "
                + "inner join " + Ad.TABLE_NAME + " a on a.adData_id = bad.id "
                + "inner join " + AdData.TABLE_NAME + " addt on addt.id = bad.id "
                + "where a.id = :adId"
                + "")
                .addEntity(BusinessAdData.class)
                .setParameter("adId", adId)
                .uniqueResult();
        return (BusinessAdData) result;
    }
}
