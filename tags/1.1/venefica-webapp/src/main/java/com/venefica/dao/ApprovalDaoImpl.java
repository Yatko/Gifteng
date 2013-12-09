/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.dao;

import com.venefica.model.Approval;
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
public class ApprovalDaoImpl extends DaoBase<Approval> implements ApprovalDao {

    @Override
    public Long save(Approval approval) {
        approval.setApprovedAt(new Date());
        return saveEntity(approval);
    }

    @Override
    public List<Approval> getByAd(Long adId) {
        return createQuery(""
                + "from " + getDomainClassName() + " a where "
                + "a.ad.id = :adId "
                + "order by a.approvedAt desc"
                + "")
                .setParameter("adId", adId)
                .list();
    }
    
    @Override
    public Approval getByAdAndRevision(Long adId, Integer revision) {
        List<Approval> approvals = createQuery(""
                + "from " + getDomainClassName() + " a where "
                + "a.ad.id = :adId and "
                + "a.revision = :revision "
                + "order by a.approvedAt desc"
                + "")
                .setParameter("adId", adId)
                .setParameter("revision", revision)
                .list();
        if ( approvals != null && !approvals.isEmpty() ) {
            return approvals.get(0);
        }
        return null;
    }
}
