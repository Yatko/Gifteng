package com.venefica.dao;

import com.venefica.model.Message;
import com.venefica.model.Request;
import java.util.ArrayList;
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
                + "m.request.id = :requestId and "
                + "m.deleted = false "
                + "order by m.createdAt asc "
                + "")
                .setParameter("requestId", requestId)
                .list();
        return messages;
    }

    @Override
    public List<Message> getByUsers(Long user1, Long user2) {
        List<Message> messages = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
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
        /**
        List<Message> messages = createQuery(""
                + "select m "
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
                + "(m.from.id = :userId or m.to.id = :userId) "
                + "group by m.request "
                + "order by m.createdAt desc, m.id desc "
                + "")
                .setParameter("userId", userId)
                .list();
        /**/
        
        /**/
        List<Message> messages = new LinkedList<Message>();
        List<Request> requests = createQuery(""
                + "select m.request "
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.deleted = false and "
                + "(m.request.ad.creator.id = :userId or m.request.user.id = :userId) "
                + "group by m.request "
                + "order by m.createdAt desc "
                + "")
                .setParameter("userId", userId)
                .list();
        for ( Request request : requests ) {
            List<Message> message = createQuery(""
                + "from " + getDomainClassName() + " m "
                + "where "
                + "m.request = :request and "
                + "m.deleted = false and "
                + "m.from.id != :fromId "
                + "order by m.createdAt desc, m.id desc "
                + "")
                .setParameter("request", request)
                .setParameter("fromId", userId)
                .setMaxResults(1)
                .list();
            if ( message != null && !message.isEmpty() ) {
                messages.add(message.get(0));
            }
        }
        /**/
        return messages;
    }
}