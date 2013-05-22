/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.ForgotPassword;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ForgotPasswordDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ForgotPasswordDaoImpl extends DaoBase<ForgotPassword> implements ForgotPasswordDao {
    
    private static final Log log = LogFactory.getLog(ForgotPasswordDaoImpl.class);
    
    @Override
    public Long save(ForgotPassword forgotPassword) {
        forgotPassword.setCreatedAt(new Date());
        return saveEntity(forgotPassword);
    }
    
    @Override
    public ForgotPassword findByCode(String code) {
        List<ForgotPassword> requests = createQuery("from " + getDomainClassName() + " i where i.code=:code")
                .setParameter("code", code)
                .list();

        return requests.isEmpty() ? null : requests.get(0);
    }
    
    @Override
    public void markExpiredRequests() {
        // @formatter:off		
        int numRows = createQuery(
                "update " + getDomainClassName() + " fp set fp.expired = true where fp.expiresAt < current_date() "
                + "and fp.expired = false and fp.used = false")
                .executeUpdate();
        // @formatter:on

        if (numRows > 0) {
            log.info(numRows + " forgot password requests marked as expired.");
        }
    }
}
