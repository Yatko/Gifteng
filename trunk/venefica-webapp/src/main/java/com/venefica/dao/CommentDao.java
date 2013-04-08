package com.venefica.dao;

import com.venefica.model.Comment;
import java.util.List;

/**
 * Data access interface for {@link Comment} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface CommentDao {

    /**
     * Saves the comment in the database
     *
     * @param comment comment to save
     * @return id of the saved comment
     */
    Long save(Comment message);

    /**
     * Returns the comment by its id.
     *
     * @param id id of the comment
     * @return comment or null if a comment with the specified id not found
     */
    Comment get(Long id);
    
    /**
     * Returns a list of comments for the specified ad.
     * 
     * @param adId
     * @return list of comments
     */
    List<Comment> getAdComments(Long adId);
}
