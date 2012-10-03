package com.venefica.dao;

import java.util.List;

import com.venefica.model.Ad;
import com.venefica.service.dto.FilterDto;


/**
 * Data access interface for {@link Ad} entity.
 * 
 * @author Sviatoslav Grebenchukov
 */
public interface AdDao {

	/**
	 * Saves the ad in the database
	 * 
	 * @param ad
	 *            ad to save
	 * @return id of the saved object
	 */
	Long save(Ad ad);

	/**
	 * Returns a list of ads with id is less than specified one.
	 * 
	 * @param lastAdId
	 *            last ad id or -1 to return ads from the beginning
	 * @param numberAds
	 *            the max number of ads to return
	 * @return list of ads
	 */
	List<Ad> get(Long lastAdId, int numberAds);

	/**
	 * Returns a list of ads with id is less than the specified one which fit the filter.
	 * 
	 * @param lastAdId
	 *            last ad id or -1 to return ads from the beginning
	 * @param numberAds
	 *            the max number of ads to return
	 * @param filter
	 *            filter
	 * @return list of ads
	 */
	List<Ad> get(Long lastAdId, int numberAds, FilterDto filter);

	/**
	 * Returns the ad by its id.
	 * 
	 * @param adId
	 *            the id of the ad
	 * @return ad object
	 */
	Ad get(Long adId);

	/**
	 * Set 'expired' flag to true for all expired ads in the database.
	 */
	void markExpiredAds();

	/**
	 * Returns a list of 'active' or 'expired' ads created by the specified user.
	 * 
	 * @param userId
	 *            id of the user
	 * @return list of ads
	 */
	List<Ad> getByUser(Long userId);
}
