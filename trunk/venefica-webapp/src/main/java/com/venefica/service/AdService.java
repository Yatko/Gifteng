package com.venefica.service;

import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.RatingDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.ImageNotFoundException;
import com.venefica.service.fault.ImageValidationException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRateOperationException;
import com.venefica.service.fault.InvalidRequestException;
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.validation.constraints.NotNull;
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
    
    //**********************
    //* categories related *
    //**********************
    
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

    
    
    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
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
    Long placeAd(@WebParam(name = "ad") @NotNull AdDto adDto) throws AdValidationException;
    
    /**
     * Updates the ad. Only ad creator can update ad.
     * 
     * @param adDto updated ad.
     * @throws AdNotFoundException is throw when the updating ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to update the ad
     */
    @WebMethod(operationName = "UpdateAd")
    void updateAd(@WebParam(name = "ad") @NotNull AdDto adDto)
            throws AdNotFoundException, AdValidationException, AuthorizationException;
    
    /**
     * Deletes the ad with the specified id. Only ad creator can delete ad.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException is thrown when the ending ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to delete the ad
     */
    @WebMethod(operationName = "DeleteAd")
    void deleteAd(@WebParam(name = "adId") @NotNull Long adId)
            throws AdNotFoundException, AuthorizationException;
    
    /**
     * Ads an image to the specified ad. The image data is stored in the
     * database as LOB.
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
    Long addImageToAd(
            @WebParam(name = "adId") @NotNull Long adId,
            @WebParam(name = "image") @NotNull ImageDto imageDto)
            throws AdNotFoundException, ImageValidationException;

    /**
     * Removes the image from the specified ad. Only ad creator can delete
     * images.
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
    void deleteImageFromAd(
            @WebParam(name = "adId") @NotNull Long adId,
            @WebParam(name = "imageId") @NotNull Long imageId)
            throws AdNotFoundException, AuthorizationException, ImageNotFoundException;
    
    /**
     * Removes the given list of image from the specified ad. Only ad creator
     * can delete images.
     * 
     * @param adId id of the ad
     * @param imageIds list of id of the images
     * @throws AdNotFoundException if an ad with the specified id not found
     * @throws AuthorizationException if a user different form the creator
     * tries to delete the image
     * @throws ImageNotFoundException if an image with the specified id not
     * found attached to the specified ad
     */
    @WebMethod(operationName = "DeleteImagesFromAd")
    void deleteImagesFromAd(
            @WebParam(name = "adId") @NotNull Long adId,
            @WebParam(name = "imageIds") List<Long> imageIds)
            throws AdNotFoundException, AuthorizationException, ImageNotFoundException;

    
    
    //*************************
    //* ads listing/filtering *
    //*************************
    
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
     * @param includeRequests flag to include available requests
     * @return list of ads
     */
    @WebMethod(operationName = "GetMyAds")
    @WebResult(name = "ad")
    List<AdDto> getMyAds(@WebParam(name = "includeRequests") Boolean includeRequests);

    /**
     * Returns a list of all ads created by the given user.
     * 
     * @param userId the creator user
     * @param includeRequests flag to include available requests
     * @return list of ads
     * @throws UserNotFoundException is thrown when the user with the specified id is not found
     */
    @WebMethod(operationName = "GetUserAds")
    @WebResult(name = "ad")
    List<AdDto> getUserAds(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "includeRequests") Boolean includeRequests) throws UserNotFoundException;
    
    /**
     * Returns a list of all ads requested by the given user.
     * 
     * @return list of ads
     * @throws UserNotFoundException is thrown when the user with the specified id is not found
     */
    @WebMethod(operationName = "GetUserRequestedAds")
    @WebResult(name = "ad")
    List<AdDto> getUserRequestedAds(@WebParam(name = "userId") Long userId) throws UserNotFoundException;
    
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

    
    
    //***********************
    //* ad lifecycle change *
    //***********************
    
    //
    // Note: these are not complete as the delete is also a lifecycle change, so
    // needs a little bit of rethinking
    //
    
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
     * Removes 'expired' flag and prolong the expiration period for xx days.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException is thrown when the ad not found.
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to relist the ad
     * @throws InvalidAdStateException is thrown when a user tries to relist
     * a deleted or completed ad
     */
    @WebMethod(operationName = "RelistAd")
    void relistAd(@WebParam(name = "adId") Long adId) throws AdNotFoundException,
            AuthorizationException, InvalidAdStateException;
    
    
    
    //***************
    //* ad requests *
    //***************
    
    /**
     * Creates a new request on the given ad. The owner cannot request
     * it's own ad.
     * 
     * @param adId
     * @return the created request id
     * @throws AdNotFoundException 
     * @throws AlreadyRequestedException
     * @throws InvalidRequestException if the ad creator and requestor user is
     * the same
     */
    @WebMethod(operationName = "RequestAd")
    @WebResult(name = "requestId")
    Long requestAd(@WebParam(name = "adId") Long adId) throws AdNotFoundException, AlreadyRequestedException, InvalidRequestException, InvalidAdStateException;
    
    /**
     * Cancels (removes) an existing request. Only the receiver
     * (that one that claimed for the ad - buyer) can use this function.
     * 
     * @param requestId
     * @throws AdNotFoundException 
     * @throws InvalidRequestException if the request owner and the caller user
     * does not match
     */
    @WebMethod(operationName = "CancelRequest")
    void cancelRequest(@WebParam(name = "requestId") Long requestId) throws RequestNotFoundException, InvalidRequestException, InvalidAdStateException;
    
    /**
     * Selects the given request as the choosed one (as the 'winner').
     * Only the giver (the seller) can use this function to choose one requestor.
     * 
     * @param requestId
     * @throws InvalidRequestException if the selector user and ad owner does
     * not match
     * @throws RequestNotFoundException if the given request could not be found
     */
    @WebMethod(operationName = "SelectRequest")
    void selectRequest(@WebParam(name = "requestId") Long requestId) throws RequestNotFoundException, InvalidRequestException, InvalidAdStateException;
    
    /**
     * 
     * @param requestId
     * @return
     * @throws RequestNotFoundException 
     */
    @WebMethod(operationName = "GetRequestById")
    @WebResult(name = "request")
    RequestDto getRequestById(@WebParam(name = "requestId") Long requestId) throws RequestNotFoundException;
    
    /**
     * Returns available requests for the given ad.
     * 
     * @param adId
     * @return
     * @throws AdNotFoundException 
     */
    @WebMethod(operationName = "GetRequests")
    @WebResult(name = "request")
    List<RequestDto> getRequests(@WebParam(name = "adId") Long adId) throws AdNotFoundException;
    
    /**
     * Returns available requests for the given user.
     * The requests should have status:
     * - PENDING
     * - ACCEPTED
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    @WebMethod(operationName = "GetRequestsByUser")
    @WebResult(name = "request")
    List<RequestDto> getRequestsByUser(@WebParam(name = "userId") Long userId) throws UserNotFoundException;
    
    /**
     * 
     * @param userId
     * @return list of requests that were selected, but no rating was given
     * @throws UserNotFoundException 
     */
    @WebMethod(operationName = "GetRequestsForUserWithoutRating")
    @WebResult(name = "request")
    List<RequestDto> getRequestsForUserWithoutRating(@WebParam(name = "userId") Long userId) throws UserNotFoundException;
    
    /**
     * Marks by the giver (seller or owner) the status of the request.
     * 
     * //mark as gifted
     * 
     * @param requestId 
     * @throws RequestNotFoundException 
     * @throws InvalidRequestException when the current user and the corresponding
     * request creator does not match
     */
    @WebMethod(operationName = "MarkAsSent")
    void markAsSent(@WebParam(name = "requestId") Long requestId) throws RequestNotFoundException, InvalidRequestException;
    
    /**
     * Marked by the receiver (client). Confirmation that the product (gift)
     * was delivered.
     * 
     * //mark as delivered
     * 
     * @param requestId
     * @throws RequestNotFoundException 
     * @throws InvalidRequestException when the current user and the corresponding
     * requestor does not match
     */
    @WebMethod(operationName = "MarkAsReceived")
    void markAsReceived(@WebParam(name = "requestId") Long requestId) throws RequestNotFoundException, InvalidRequestException;
    
    
    
    //***************
    //* ad bookmark *
    //***************

    /**
     * Creates a bookmark to the ad.
     * 
     * @param adId id of the ad
     * @return id of the created bookmark
     * @throws AdNotFoundException if the ad with the specified id not found.
     */
    @WebMethod(operationName = "BookmarkAd")
    @WebResult(name = "bookmarkId")
    Long bookmarkAd(@WebParam(name = "adId") @NotNull Long adId) throws AdNotFoundException;

    /**
     * Removes the bookmark from the database.
     * 
     * @param bookmarkId id of the bookmark
     * @throws BookmarkNotFoundException if the bookmark with the specified
     * id not found
     */
    @WebMethod(operationName = "RemoveBookmark")
    void removeBookmark(@WebParam(name = "adId") @NotNull Long adId) throws BookmarkNotFoundException;

    /**
     * Returns a list of bookmarked ads.
     * 
     * @return list of ads
     */
    @WebMethod(operationName = "GetBookmarkedAds")
    @WebResult(name = "ad")
    List<AdDto> getBookmarkedAds();
    
    /**
     * Returns a list of bookmarked ads for the specified user.
     * 
     * @return list of ads
     * @throws UserNotFoundException when the given user id does not exists
     */
    @WebMethod(operationName = "GetBookmarkedAdsForUser")
    @WebResult(name = "ad")
    List<AdDto> getBookmarkedAds(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;

    
    
    
    //****************
    //* ad spam mark *
    //****************
    
    /**
     * Marks the ad as spam.
     * 
     * @param adId id of the ad.
     * @throws AdNotFoundException if the ad with the specified id not found
     */
    @WebMethod(operationName = "MarkAsSpam")
    void markAsSpam(@WebParam(name = "adId") @NotNull Long adId) throws AdNotFoundException;

    /**
     * Un-marks the ad as spam.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException if the ad with the specified id not found
     */
    @WebMethod(operationName = "UnmarkAsSpam")
    void unmarkAsSpam(@WebParam(name = "adId") @NotNull Long adId) throws AdNotFoundException;

    
    
    //*************
    //* ad rating *
    //*************
    
    /**
     * Add a rating to the ad.
     * 
     * @param ratingDto
     * @return new calculated rating
     * @throws AdNotFoundException if the ad with the specified id not found
     * @throws InvalidRateOprationException if user tries to rate his own ad
     * @throws AlreadyRatedException if user tries to rate the same ad more
     * than one time
     */
    @WebMethod(operationName = "RateAd")
    @WebResult(name = "rating")
    float rateAd(@WebParam(name = "rating") @NotNull RatingDto ratingDto)
            throws AdNotFoundException, UserNotFoundException, InvalidRateOperationException, AlreadyRatedException, InvalidAdStateException;
    
    /**
     * Returns a list of received ratings for the given user.
     * 
     * @param userId
     * @return list of ratings
     * @throws UserNotFoundException if the given user could not be found
     */
    @WebMethod(operationName = "GetReceivedRatings")
    @WebResult(name = "rating")
    List<RatingDto> getReceivedRatings(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * Returns a list of ratings that were created by the given user.
     * 
     * @param userId
     * @return list of ratings
     * @throws UserNotFoundException if the given user could not be found
     */
    @WebMethod(operationName = "GetSentRatings")
    @WebResult(name = "rating")
    List<RatingDto> getSentRatings(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
}
