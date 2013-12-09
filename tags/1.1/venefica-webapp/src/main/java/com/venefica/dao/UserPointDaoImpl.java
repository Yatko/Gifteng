/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserPoint;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserPointDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserPointDaoImpl extends DaoBase<UserPoint> implements UserPointDao {

    @Override
    public Long save(UserPoint userPoint) {
        return saveEntity(userPoint);
    }

    @Override
    public void update(UserPoint userPoint) {
        updateEntity(userPoint);
    }
    
}
