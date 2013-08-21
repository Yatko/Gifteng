/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.UserVerification;
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
public class UserVerificationDaoImpl extends DaoBase<UserVerification> implements UserVerificationDao {

    @Override
    public Long save(UserVerification userVerification) {
        return saveEntity(userVerification);
    }

    @Override
    public UserVerification findByCode(String code) {
        List<UserVerification> verifications = createQuery(""
                + "from " + getDomainClassName() + " uv "
                + "where "
                + "uv.code = :code"
                + "")
                .setParameter("code", code)
                .list();
        return verifications.isEmpty() ? null : verifications.get(0);
    }
    
    @Override
    public UserVerification findByUser(Long userId) {
        List<UserVerification> verifications = createQuery(""
                + "from " + getDomainClassName() + " uv "
                + "where "
                + "uv.user.id = :userId "
                + "order by id desc"
                + "")
                .setParameter("userId", userId)
                .list();
        return verifications.isEmpty() ? null : verifications.get(0);
    }

    @Override
    public List<UserVerification> getAllUnverified() {
        List<UserVerification> verifications = createQuery(""
                + "from " + getDomainClassName() + " uv where "
                + "uv.verified = false "
                + "")
                .list();
        return verifications;
    }
    
}
