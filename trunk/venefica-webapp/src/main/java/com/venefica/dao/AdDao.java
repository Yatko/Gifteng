package com.venefica.dao;

import com.venefica.model.Ad;
import com.venefica.service.dto.FilterDto;
import java.util.List;

/**
 * Data access interface for {@link Ad} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
public interface AdDao {

    /**
     * Saves the ad in the database
     *
     * @param ad ad to save
     * @return id of the saved object
     */
    Long save(Ad ad);

    /**
     * Returns the ad by its id.
     *
     * @param adId the id of the ad
     * @return ad object
     */
    Ad get(Long adId);
    
    /**
     * Returns a list of ads with id is less than specified one.
     *
     * @param lastAdId last ad id or -1 to return ads from the beginning
     * @param numberAds the max number of ads to return
     * @return list of ads
     */
    List<Ad> get(Long lastAdId, int numberAds);

    /**
     * Returns a list of ads with id is less than the specified one which fit
     * the filter.
     *
     * @param lastAdId last ad id or -1 to return ads from the beginning
     * @param numberAds the max number of ads to return
     * @param filter filter
     * @return list of ads
     */
    List<Ad> get(Long lastAdId, int numberAds, FilterDto filter);

    /**
     * Set 'expired' flag to true for all expired ads in the database.
     */
    void markExpiredAds();

    /**
     * Set the 'online' flag for all approved ads in the database.
     */
    void markOnlineAds();
    
    /**
     * Marks the approved state of an ad. At this moment the ad can be exported
     * for online state.
     * @param ad
     */
    void approveAd(Ad ad);
    
    /**
     * Returns a list of 'active' or 'expired' ads created by the specified
     * user.
     *
     * @param userId id of the user
     * @return list of ads
     */
    List<Ad> getByUser(Long userId);
    
    /**
     * 
     * @return a list of un-approved ads
     */
    List<Ad> getUnapprovedAds();
    
    /**
     * 
     * @return a list of un-approved ads
     */
    List<Ad> getOfflineAds();
}
