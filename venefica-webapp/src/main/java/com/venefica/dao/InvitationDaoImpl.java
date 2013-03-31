/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Invitation;
import java.util.List;
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
    public Invitation findByCode(String code) {
        List<Invitation> invitations = createQuery("from Invitation where code=:code")
                .setParameter("code", code)
                .list();

        return invitations.isEmpty() ? null : invitations.get(0);
    }
}
