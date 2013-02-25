package com.venefica.dao;

import com.venefica.model.Comment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link CommentDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class CommentDaoImpl extends DaoBase<Comment> implements CommentDao {

    @Override
    public Long save(Comment comment) {
        return saveEntity(comment);
    }

    @Override
    public Comment get(Long id) {
        return getEntity(id);
    }
}