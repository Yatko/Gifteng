/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.service.dto.Provider;
import com.venefica.model.UserConnection;
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
public class UserConnectionDaoImpl extends DaoBase<UserConnection> implements UserConnectionDao {

    @Override
    public UserConnection getByUserId(Provider provider, Long userId) {
        List<UserConnection> userConnections = createQuery(""
                + "from " + getDomainClassName() + " uc where "
                + "uc.id.providerId = :provider and "
                + "uc.id.userId = :userId"
                + "")
                .setParameter("provider", provider.getName())
                .setParameter("userId", userId.toString())
                .list();
        return userConnections.isEmpty() ? null : userConnections.get(0);
    }
    
}
