/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Invitation;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link InvitationDao} interface.
 * 
 * @author gyuszi
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class InvitationDaoImpl extends DaoBase<Invitation> implements InvitationDao {

    private static final Log log = LogFactory.getLog(InvitationDaoImpl.class);

    @Override
    public Invitation get(Long invitationId) {
        if (invitationId == null) {
            throw new NullPointerException("invitationId");
        }
        return getEntity(invitationId);
    }
    
    @Override
    public Long save(Invitation invitation) {
        return saveEntity(invitation);
    }
    
    @Override
    public void update(Invitation invitation) {
        updateEntity(invitation);
    }

    @Override
    public Invitation findByCode(String code) {
        List<Invitation> invitations = createQuery(""
                + "from " + getDomainClassName() + " i "
                + "where i.code=:code "
                + "")
                .setParameter("code", code)
                .list();
        return invitations.isEmpty() ? null : invitations.get(0);
    }
    
    @Override
    public void markExpiredInvitations() {
        int numRows = createQuery(""
                + "update " + getDomainClassName() + " i "
                + "set i.expired = true "
                + "where "
                + "i.expired = false and "
                + "i.numAvailUse > 0 and "
                + "i.expiresAt <= current_date() "
                + "")
                .executeUpdate();
        if (numRows > 0) {
            log.info(numRows + " invitations marked as expired.");
        }
    }
    
    @Override
    public List<Invitation> getByRemainingDay(int day) {
        Date dateTo = new Date();
        Date dateFrom = DateUtils.addDays(dateTo, -day);
        
        List<Invitation> invitations = createQuery(""
                + "from " + getDomainClassName() + " i "
                + "where "
                + "i.expired = false and "
                + "i.numAvailUse > 0 and "
                + "i.expiresAt > :dateFrom and "
                + "i.expiresAt <= :dateTo "
                + "")
                .setParameter("dateFrom", dateFrom)
                .setParameter("dateTo", dateTo)
                .list();
        return invitations;
    }
}
