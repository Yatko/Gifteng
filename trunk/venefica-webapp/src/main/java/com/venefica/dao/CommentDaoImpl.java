package com.venefica.dao;

import com.venefica.model.Comment;
import java.util.List;
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
    
    @Override
    public List<Comment> getAdComments(Long adId) {
        return createQuery("from Comment c where c.ad.id = :adId order by c.id desc")
                .setParameter("adId", adId)
                .list();
    }
}