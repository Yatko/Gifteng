package com.venefica.dao;

import com.venefica.model.Message;
import java.util.List;

/**
 * Data access interface for {@link Message} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface MessageDao {

    /**
     * Saves the message in the database
     *
     * @param message message to save
     * @return id of the saved message
     */
    Long save(Message message);

    /**
     * Returns the message by its id.
     *
     * @param id id of the message
     * @return message or null if a message with the specified id not found
     */
    Message get(Long id);

    /**
     * Removes the messaged from the database.
     *
     * @param message message to delete
     */
    void deleteMessage(Message message);
    
    /**
     * Extracts messages for the specified ad.
     * 
     * @param adId
     * @return 
     */
    List<Message> getByAd(Long adId);
    
    /**
     * Extracts messages for the specified request.
     * 
     * @param requestId
     * @return 
     */
    List<Message> getByRequest(Long requestId);
    
    /**
     * Returns all conversations between the given users.
     * 
     * @param user1
     * @param user2
     * @return 
     */
    List<Message> getByUsers(Long user1, Long user2);
    
    /**
     * Returns all conversations between the given users related with the
     * configured ad.
     * 
     * @param adId
     * @param user1
     * @param user2
     * @return 
     */
    List<Message> getByAdAndUsers(Long adId, Long user1, Long user2);
    
    /**
     * Returns the last message for every request conversations. The given
     * user can be on requestor or creator side.
     * 
     * @param userId
     * @return 
     */
    List<Message> getLastMessagePerRequestByUser(Long userId);
    
    /**
     * Returns all the unread messages for the given user.
     * 
     * @param userId
     * @return 
     */
    List<Message> getUnreadMessages(Long userId);
}
