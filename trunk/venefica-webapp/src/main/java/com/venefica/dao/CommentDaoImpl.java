package com.venefica.dao;

import com.venefica.model.Comment;
import java.util.Date;
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
    
    private static final int MAX_COMMENTS_TO_RETURN = 100;
    
    @Override
    public Long save(Comment comment) {
        comment.setCreatedAt(new Date());
        return saveEntity(comment);
    }

    @Override
    public Comment get(Long id) {
        return getEntity(id);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Comment> getAdComments(Long adId, Long lastCommentId, int numComments) {
        List<Comment> comments;

        numComments = numComments > MAX_COMMENTS_TO_RETURN ? MAX_COMMENTS_TO_RETURN : numComments;

        if (lastCommentId < 0) {
            comments = createQuery("from " + getDomainClassName() + " c where c.ad.id = :adId order by c.createdAt desc")
                    .setParameter("adId", adId)
                    .setMaxResults(numComments)
                    .list();
        } else {
            comments = createQuery("from " + getDomainClassName() + " c where c.ad.id = :adId and c.id < :lastId order by c.createdAt desc")
                    .setParameter("adId", adId)
                    .setParameter("lastId", lastCommentId)
                    .setMaxResults(numComments)
                    .list();
        }
        return comments;
    }
}