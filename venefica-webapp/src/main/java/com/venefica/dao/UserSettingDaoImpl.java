/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserSetting;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserSettingDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserSettingDaoImpl extends DaoBase<UserSetting> implements UserSettingDao {

    @Override
    public Long save(UserSetting userSetting) {
        return saveEntity(userSetting);
    }

    @Override
    public void update(UserSetting userSetting) {
        updateEntity(userSetting);
    }
    
    @Override
    public UserSetting get(Long id) {
        if (id == null) {
            throw new NullPointerException("id");
        }

        return getEntity(id);
    }
}
