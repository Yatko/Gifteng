package com.venefica.service;

import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.ImageNotFoundException;
import com.venefica.service.fault.ImageValidationException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRateOperationException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.ws.soap.MTOM;

/**
 * Describes all business operation with advertisement objects.
 * 
 * @author Sviatoslav Grebenchukov
 */
@WebService(name = "AdService", targetNamespace = Namespace.SERVICE)
@SOAPBinding(parameterStyle = ParameterStyle.WRAPPED)
@MTOM(enabled = false, threshold = 1024)
public interface AdService {

    /**
     * Returns a list of subcategories belonging to the specified parent category. If the parent
     * categoryId is null then the root categories are returned.
     * 
     * @param categoryId parent category id
     * @return list of categories
     */
    @WebMethod(operationName = "GetCategories")
    @WebResult(name = "category")
    List<CategoryDto> getCategories(@WebParam(name = "categoryId") Long categoryId);

    /**
     * Returns all the categories (including subcategories) available in the system .
     */
    @WebMethod(operationName = "GetAllCategories")
    @WebResult(name = "category")
    List<CategoryDto> getAllCategories();

    /**
     * Creates the ad in the system.
     * 
     * @param adDto creating ad
     * @return id of the created ad
     * @throws AdValidationException is thrown when an invalid or incomplete
     * ad object is supplied
     */
    @WebMethod(operationName = "PlaceAd")
    @WebResult(name = "adId")
    Long placeAd(@WebParam(name = "ad") AdDto adDto) throws AdValidationException;

    /**
     * Returns a list of ads with id is less than specified one.
     * 
     * @param lastAdId last ad id or -1 to return from the beginning
     * @param numberAds the maximum number of ads to return
     * @return list of ads
     */
    @WebMethod(operationName = "GetAds")
    @WebResult(name = "ad")
    List<AdDto> getAds(@WebParam(name = "lastAdId") Long lastAdId,
            @WebParam(name = "numberAds") int numberAds);

    /**
     * Returns a list of all ads created by the current user.
     * 
     * @return list of ads
     */
    @WebMethod(operationName = "GetMyAds")
    @WebResult(name = "ad")
    List<AdDto> getMyAds();

    /**
     * Returns a list of ads with id is less than specified one which fit the filter.
     * 
     * @param lastAdId last ad id or -1 to return from the beginning
     * @param numberAds the maximum number ads to return
     * @param filter filter conditions
     * @return list of ads
     */
    @WebMethod(operationName = "GetAdsEx")
    @WebResult(name = "ad")
    List<AdDto> getAds(
            @WebParam(name = "lastAdId") Long lastAdId,
            @WebParam(name = "numberAds") int numberAds,
            @WebParam(name = "filter") FilterDto filter);

    /**
     * @see AdService#getAds(java.lang.Long, int, com.venefica.service.dto.FilterDto) 
     * 
     * @param lastAdId last ad id or -1 to return from the beginning
     * @param numberAds the maximum number ads to return
     * @param filter filter conditions
     * @param includeImages flag to include ad images
     * @param includeCreator flag to include creator details
     * @return list of ads
     */
    @WebMethod(operationName = "GetAdsExDetail")
    @WebResult(name = "ad")
    List<AdDto> getAds(
            @WebParam(name = "lastAdId") Long lastAdId,
            @WebParam(name = "numberAds") int numberAds,
            @WebParam(name = "filter") FilterDto filter,
            @WebParam(name = "includeImages") Boolean includeImages,
            @WebParam(name = "includeCreator") Boolean includeCreator,
            @WebParam(name = "includeCommentsNumber") int includeCommentsNumber);

    /**
     * Returns the ad by its id.
     * 
     * @param adId the id of the ad
     * @return detailed ad object
     * @throws AdNotFoundException is thrown when the ad with the specified id not found
     */
    @WebMethod(operationName = "GetAdById")
    @WebResult(name = "ad")
    AdDto getAdById(@WebParam(name = "adId") Long adId) throws AdNotFoundException;

    /**
     * Ads an image to the specified ad. The image date is stored in the database as LOB.
     * 
     * @param adId id of the ad
     * @param image image data
     * @return id of the added image
     * @throws AdNotFoundException is thrown when an ad with the specified
     * id not found
     * @throws ImageValidationException is thrown when the image contains
     * invalid fields
     */
    @WebMethod(operationName = "AddImageToAd")
    @WebResult(name = "imgId")
    Long addImageToAd(@WebParam(name = "adId") Long adId,
            @WebParam(name = "image") ImageDto imageDto) throws AdNotFoundException,
            ImageValidationException;

    /**
     * Removes the image from the specified ad.
     * 
     * @param adId id of the ad
     * @param imageId id of the image
     * @throws AdNotFoundException if an ad with the specified id not found
     * @throws AuthorizationException if a user different form the creator
     * tries to delete the image
     * @throws ImageNotFoundException if an image with the specified id not
     * found attached to the specified ad
     */
    @WebMethod(operationName = "DeleteImageFromAd")
    void deleteImageFromAd(@WebParam(name = "adId") Long adId,
            @WebParam(name = "imageId") Long imageId) throws AdNotFoundException,
            AuthorizationException, ImageNotFoundException;

    /**
     * Updates the ad
     * 
     * @param adDto updated ad.
     * @throws AdNotFoundException is throw when the updating ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to update the ad
     */
    @WebMethod(operationName = "UpdateAd")
    void updateAd(@WebParam(name = "ad") AdDto adDto) throws AdNotFoundException,
            AdValidationException, AuthorizationException;

    /**
     * Ends the ad with the specified id.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException is thrown when the ending ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to end the ad.
     */
    @WebMethod(operationName = "EndAd")
    void endAd(@WebParam(name = "adId") Long adId) throws AdNotFoundException,
            AuthorizationException;

    /**
     * Deletes the ad with the specified id.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException is thrown when the ending ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to delete the ad
     */
    @WebMethod(operationName = "DeleteAd")
    void deleteAd(@WebParam(name = "adId") Long adId) throws AdNotFoundException,
            AuthorizationException;

    /**
     * Removes 'expired' flag and prolong the expiration period for 15 days.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException is thrown when the ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to relist the ad
     * @throws InvalidAdStateException is thrown when a user tries to relist
     * a deleted or completed ad
     * 
     */
    @WebMethod(operationName = "RelistAd")
    void relistAd(@WebParam(name = "adId") Long adId) throws AdNotFoundException,
            AuthorizationException, InvalidAdStateException;

    /**
     * Creates a bookmark to the ad.
     * 
     * @param adId id of the ad
     * @return id of the created bookmark
     * @throws AdNotFoundException if the ad with the specified id not found.
     */
    @WebMethod(operationName = "BookmarkAd")
    @WebResult(name = "bookmarkId")
    Long bookmarkAd(@WebParam(name = "adId") Long adId) throws AdNotFoundException;

    /**
     * Removes the bookmark from the database.
     * 
     * @param bookmarkId id of the bookmark
     * @throws BookmarkNotFoundException if the bookmark with the specified
     * id not found
     */
    @WebMethod(operationName = "RemoveBookmark")
    void removeBookmark(@WebParam(name = "adId") Long adId) throws BookmarkNotFoundException;

    /**
     * Returns a list of bookmarked ads.
     * 
     * @return list of ads
     */
    @WebMethod(operationName = "GetBookmarkedAds")
    @WebResult(name = "ad")
    List<AdDto> getBookmarkedAds();

    /**
     * Marks the ad as spam.
     * 
     * @param adId id of the ad.
     * @throws AdNotFoundException if the ad with the specified id not found
     */
    @WebMethod(operationName = "MarkAsSpam")
    void markAsSpam(@WebParam(name = "adId") Long adId) throws AdNotFoundException;

    /**
     * Un-marks the ad as spam.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException if the ad with the specified id not found
     */
    @WebMethod(operationName = "UnmarkAsSpam")
    void unmarkAsSpam(@WebParam(name = "adId") Long adId) throws AdNotFoundException;

    /**
     * Add a rating to the ad.
     * 
     * @param adId id of the ad
     * @param ratingValue value of rating
     * @return new calculated rating
     * @throws AdNotFoundException if the ad with the specified id not found
     * @throws InvalidRateOprationException if user tries to rate his own ad
     * @throws AlreadyRatedException if user tries to rate the same ad more
     * than one time
     */
    @WebMethod(operationName = "RateAd")
    @WebResult(name = "rating")
    float rateAd(@WebParam(name = "adId") Long adId, @WebParam(name = "ratingValue") int ratingValue)
            throws AdNotFoundException, InvalidRateOperationException, AlreadyRatedException;
}
