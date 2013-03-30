/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserDataDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserDataDaoImpl extends DaoBase<UserData> implements UserDataDao {

    @Override
    public void saveOrUpdate(UserData userData) {
        saveOrUpdateEntity(userData);
    }
    
}
