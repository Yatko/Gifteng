package com.venefica.dao;

import com.venefica.model.Ad;
import com.venefica.model.Bookmark;
import com.venefica.model.User;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements {@link BookmarkDao} interface.
 *
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class BookmarkDaoImpl extends DaoBase<Bookmark> implements BookmarkDao {

    @Override
    public Bookmark get(Long bookmarkId) {
        return getEntity(bookmarkId);
    }

    @Override
    public Bookmark get(Long userId, Long adId) {
        // @formatter:off
        return (Bookmark) createQuery("from Bookmark b where b.user.id = :userId and b.ad.id = :adId")
                .setParameter("userId", userId)
                .setParameter("adId", adId)
                .uniqueResult();
        // @formatter:off
    }

    @Override
    public Long save(Bookmark bookmark) {
        bookmark.setBookmarkedAt(new Date());
        return saveEntity(bookmark);
    }

    @Override
    public void delete(Bookmark bookmark) {
        deleteEntity(bookmark);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ad> getBookmarkedAds(User user) {
        return createQuery(
                "select b.ad from Bookmark b where b.user = :user order by b.id desc")
                .setParameter("user", user)
                .list();
    }
}
