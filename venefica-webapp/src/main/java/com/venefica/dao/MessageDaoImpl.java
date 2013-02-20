package com.venefica.dao;

import com.venefica.model.Message;
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
public class MessageDaoImpl extends DaoBase implements MessageDao {

    @Override
    public Long save(Message message) {
        return saveEntity(message);
    }

    @Override
    public Message get(Long id) {
        return (Message) getEntity(Message.class, id);
    }

    @Override
    public void deleteMessage(Message message) {
        deleteEntity(message);
    }
}