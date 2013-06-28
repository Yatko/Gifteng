package com.venefica.dao;

import com.venefica.model.Message;
import java.util.Date;
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
                 + "m.ad.id = :adId and "
                 + "m.deleted = false "
                 + "order by m.createdAt asc"
                 + "")
                .setParameter("adId", adId)
                .list();
         return messages;
    }
}