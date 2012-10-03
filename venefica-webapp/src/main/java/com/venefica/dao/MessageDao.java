package com.venefica.dao;

import com.venefica.model.Message;

/**
 * Data access interface for {@link Message} entity.
 * 
 * @author Sviatoslav Grebenchukov
 */
public interface MessageDao {

	/**
	 * Saves the message in the database
	 * 
	 * @param message
	 *            message to save
	 * @return id of the saved message
	 */
	Long save(Message message);

	/**
	 * Returns the message by its id.
	 * 
	 * @param id
	 *            id of the message
	 * @return message or null if a message with the specified id not found
	 */
	Message get(Long id);

	/**
	 * Removes the messaged from the database.
	 * @param message message to delete 
	 */
	void deleteMessage(Message message);
}
