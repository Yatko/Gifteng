/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserSocialPoint;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserSocialPointDaoImpl extends DaoBase<UserSocialPoint> implements UserSocialPointDao {
    
    @Override
    public Long save(UserSocialPoint socialPoint) {
        return saveEntity(socialPoint);
    }

    @Override
    public void update(UserSocialPoint socialPoint) {
        updateEntity(socialPoint);
    }
}
