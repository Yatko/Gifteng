package com.venefica.dao;

import com.venefica.model.Ad;
import com.venefica.model.Bookmark;
import com.venefica.model.User;
import java.util.List;

/**
 * Data access interface for {@link Bookmark} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface BookmarkDao {

    /**
     * Returns the bookmark by its id.
     *
     * @param bookmarkId id of the bookmark
     * @return bookmark object or null if the bookmark not found.
     */
    Bookmark get(Long bookmarkId);

    /**
     * Returns the bookmark by the user and ad.
     *
     * @param userId id of the user who created the bookmark
     * @param adId id of the ad
     * @return bookmark object or null if the bookmark not found.
     */
    Bookmark get(Long userId, Long adId);

    /**
     * Saves the bookmark in the database.
     *
     * @param bookmark bookmark object to save
     * @return id of the stored bookmark.
     */
    Long save(Bookmark bookmark);

    /**
     * Deletes the bookmark from the database.
     *
     * @param bookmark bookmark to delete
     */
    void delete(Bookmark bookmark);

    /**
     * Returns a list of bookmarked ads for the specified user.
     *
     * @param user user object
     * @return list of ads
     */
    List<Ad> getBookmarkedAds(User user);
}
