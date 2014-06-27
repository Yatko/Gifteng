/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.BusinessUserData;
import com.venefica.model.MemberUserData;
import com.venefica.model.User;
import com.venefica.model.UserData;
import java.util.List;
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
    public Long save(UserData userData) {
        return saveEntity(userData);
    }
    
    @Override
    public void update(UserData userData) {
        updateEntity(userData);
    }

    @Override
    public BusinessUserData findByBusinessName(String businessName) {
        List<BusinessUserData> userDatas = createQuery(""
                + "from " + BusinessUserData.class.getSimpleName() + " ud where "
                + "ud.businessName = :businessName"
                + "")
                .setParameter("businessName", businessName)
                .list();
        return userDatas.isEmpty() ? null : userDatas.get(0);
    }
    
    @Override
    public MemberUserData getMemberUserDataByUser(Long userId) {
        Object result = createSQLQuery(""
                + "select mud.*, udt.* "
                + "from " + MemberUserData.TABLE_NAME + " mud "
                + "inner join " + User.TABLE_NAME + " u on u.userData_id = mud.id "
                + "inner join " + UserData.TABLE_NAME + " udt on udt.id = mud.id "
                + "where u.id = :userId"
                + "")
                .addEntity(MemberUserData.class)
                .setParameter("userId", userId)
                .uniqueResult();
        return (MemberUserData) result;
    }
    
    @Override
    public BusinessUserData getBusinessUserDataByUser(Long userId) {
        Object result = createSQLQuery(""
                + "select bud.*, udt.* "
                + "from " + BusinessUserData.TABLE_NAME + " bud "
                + "inner join " + User.TABLE_NAME + " u on u.userData_id = bud.id "
                + "inner join " + UserData.TABLE_NAME + " udt on udt.id = bud.id "
                + "where u.id = :userId"
                + "")
                .addEntity(BusinessUserData.class)
                .setParameter("userId", userId)
                .uniqueResult();
        return (BusinessUserData) result;
    }
}
