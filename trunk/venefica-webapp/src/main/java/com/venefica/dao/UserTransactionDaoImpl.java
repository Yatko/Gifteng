/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserTransaction;
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
public class UserTransactionDaoImpl extends DaoBase<UserTransaction> implements UserTransactionDao {

    @Override
    public Long save(UserTransaction userPoint) {
        userPoint.setCreatedAt(new Date());
        return saveEntity(userPoint);
    }

    @Override
    public void update(UserTransaction userPoint) {
        updateEntity(userPoint);
    }

    @Override
    public UserTransaction getByAd(Long userId, Long adId) {
        List<UserTransaction> transactions = createQuery(""
                + "from " + UserTransaction.class.getSimpleName() + " ut "
                + "where "
                + "ut.user.id = :userId and "
                + "ut.ad.id = :adId"
                + "")
                .setParameter("userId", userId)
                .setParameter("adId", adId)
                .list();
        return transactions.isEmpty() ? null : transactions.get(0);
    }

    @Override
    public UserTransaction getByRequest(Long userId, Long requestId) {
        List<UserTransaction> transactions = createQuery(""
                + "from " + UserTransaction.class.getSimpleName() + " ut "
                + "where "
                + "ut.user.id = :userId and "
                + "ut.request.id = :requestId"
                + "")
                .setParameter("userId", userId)
                .setParameter("requestId", requestId)
                .list();
        return transactions.isEmpty() ? null : transactions.get(0);
    }
    
}
