package com.venefica.dao;

import com.venefica.model.Message;
import com.venefica.model.RequestStatus;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link MessageDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class MessageDaoImpl extends DaoBase<Message> implements MessageDao {

    @Override
    public Long save(Message message) {
        message.setCreatedAt(new Date());
        return saveEntity(message);
    }

    @Override
    public Message get(Long id) {
        return getEntity(id);
    }

    @Override
    public void deleteMessage(Message message) {
        message.setDeleted(true);
        message.setDeletedAt(new Date());
        updateEntity(message);
    }

    @Override
    public List<Message> getByAd(Long adId) {
        List<Message> messages = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "m.request.ad.id = :adId and "
                + "m.deleted = false "
                + "order by m.createdAt asc "
                + "")
                .setParameter("adId", adId)
                .list();
        return messages;
    }
    
    @Override
    public List<Message> getByRequest(Long requestId) {
        List<Message> messages = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "m.request.id = :requestId and "
                + "m.deleted = false "
                + "order by m.createdAt asc "
                + "")
                .setParameter("requestId", requestId)
                .list();
        return messages;
    }
    
    @Override
    public int getNumUnreadMessagesByRequestAndUser(Long requestId, Long userId) {
        return ((Long) createQuery(""
                + "select count(m) "
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "m.request.id = :requestId and "
                + "m.to.id = :userId and "
                + "m.deleted = false and "
                + "m.read = false "
                + "order by m.createdAt asc "
                + "")
                .setParameter("requestId", requestId)
                .setParameter("userId", userId)
                .uniqueResult()).intValue();
    }

    @Override
    public List<Message> getByUsers(Long user1, Long user2) {
        List<Message> messages = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "("
                + "(m.to.id = :user1Id and m.from.id = :user2Id) or "
                + "(m.to.id = :user2Id and m.from.id = :user1Id)"
                + ") "
                + "order by m.createdAt asc "
                + "")
                .setParameter("user1Id", user1)
                .setParameter("user2Id", user2)
                .list();
        return messages;
    }

    @Override
    public List<Message> getByAdAndUsers(Long adId, Long user1, Long user2) {
        List<Message> messages = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
                + "m.request.ad.id = :adId and "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "("
                + "(m.to.id = :user1Id and m.from.id = :user2Id) or "
                + "(m.to.id = :user2Id and m.from.id = :user1Id)"
                + ") "
                + "order by m.createdAt asc "
                + "")
                .setParameter("adId", adId)
                .setParameter("user1Id", user1)
                .setParameter("user2Id", user2)
                .list();
        return messages;
    }
    
    @Override
    public List<Message> getLastMessagePerRequestByUser(Long userId) {
        List<Message> messages = new LinkedList<Message>();
        List<Long> requestIds = createQuery(""
                + "select m.request.id "
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
                + "m.request.hidden = false and "
                + "m.request.deleted = false and "
                + "m.request.status in (:acceptedStatus) and " //including only "accepted" requests
                + "m.request.ad.deleted = false and "
                + "m.request.ad.creator.deleted = false and "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "("
                + "(m.request.ad.creator.id = :userId and m.request.messagesHiddenByCreator = false) or "
                + "(m.request.user.id = :userId and m.request.messagesHiddenByRequestor = false)"
                + ") "
                + "group by m.request.id "
                + "order by max(m.createdAt) desc"
                + "")
                .setParameter("userId", userId)
                .setParameterList("acceptedStatus", RequestStatus.ACCEPTED_STATUSES)
                .list();
        for ( Long requestId : requestIds ) {
            List<Message> lastMessage = createQuery(""
                    + "from " + getDomainClassName() + " m "
                    + "where "
                    + "m.request.id = :requestId and "
                    + "m.deleted = false and "
                    + "m.to.deleted = false and "
                    + "m.from.deleted = false "
                    + "order by m.createdAt desc, m.id desc "
                    + "")
                    .setParameter("requestId", requestId)
                    .setMaxResults(1)
                    .list();
            if ( lastMessage != null && !lastMessage.isEmpty() ) {
                messages.add(lastMessage.get(0));
            }
        }
        
        return messages;
    }

    @Override
    public List<Message> getUnreadMessages(Long userId) {
        List<Message> messages = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
                + "m.read = false and "
                + "m.to.id = :userId and "
                + "m.request.hidden = false and "
                + "m.request.deleted = false and "
                + "m.request.status in (:acceptedStatus) and " //including only "accepted" requests
                + "m.request.ad.deleted = false and "
                + "m.request.ad.creator.deleted = false and "
                + "m.to.deleted = false and "
                + "m.from.deleted = false and "
                + "("
                + "(m.request.ad.creator.id = :userId and m.request.messagesHiddenByCreator = false) or "
                + "(m.request.user.id = :userId and m.request.messagesHiddenByRequestor = false)"
                + ") "
                + "order by m.createdAt desc, m.id desc "
                + "")
                .setParameter("userId", userId)
                .setParameterList("acceptedStatus", RequestStatus.ACCEPTED_STATUSES)
                .list();
        return messages;
    }
}