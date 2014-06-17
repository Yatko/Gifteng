/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Share;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ShareDaoImpl extends DaoBase<Share> implements ShareDao {

    @Override
    public Long save(Share share) {
        return saveEntity(share);
    }

    @Override
    public Share get(Long shareId) {
        return getEntity(shareId);
    }
    
}
