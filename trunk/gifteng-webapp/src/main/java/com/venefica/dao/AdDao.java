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
     * 
     * @param ad 
     */
    void update(Ad ad);
    
    /**
     * Returns the ad by its id.
     *
     * @param adId the id of the ad
     * @return ad object
     */
    Ad get(Long adId);
    
    /**
     * 
     * @param adId
     * @return 
     */
    Ad getEager(Long adId);
    
    /**
     * Returns a list of ads with id is less than specified one.
     *
     * @param lastIndex last index used at paging
     * @param numberAds the max number of ads to return
     * @return list of ads
     */
    List<Ad> get(int lastIndex, int numberAds);

    /**
     * Returns a list of ads with id is less than the specified one which fit
     * the filter.
     *
     * @param lastIndex last index used at paging
     * @param numberAds the max number of ads to return
     * @param filter filter
     * @return list of ads
     */
    List<Ad> get(int lastIndex, int numberAds, FilterDto filter);

    /**
     * 
     * @return 
     */
    List<Ad> getExpiredAds();
    

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
     * Sets an ad to online state. This means that will appear in ad browser.
     * @param ad 
     */
    void onlineAd(Ad ad);
    
    /**
     * Returns a list of 'active' or 'expired' ads created by the specified
     * user. If the maxResult is equal with Integer.MAX_VALUE no max result is set.
     *
     * @param userId id of the user
     * @param numberAds max result
     * @return list of ads
     */
    List<Ad> getByUser(Long userId, int numberAds);
    
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
    
    /**
     * 
     * @return 
     */
    List<Ad> getHiddenForSearchAds();
    
    /**
     * 
     * @return 
     */
    List<Ad> getStaffPickAds();
}
