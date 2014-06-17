package com.venefica.service;

import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AdStatisticsDto;
import com.venefica.service.dto.ApprovalDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.RatingDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.dto.ShippingBoxDto;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.ApprovalNotFoundException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.GeneralException;
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
     * Returns the required category. If the ID is not found null will be returned.
     * 
     * @param categoryId
     * @return 
     */
    @WebMethod(operationName = "GetCategory")
    @WebResult(name = "category")
    CategoryDto getCategory(@WebParam(name = "categoryId") Long categoryId);
    
    /**
     * Returns a list of subcategories belonging to the specified parent category.
     * If the parent categoryId is null then the root categories are returned.
     * 
     * @param categoryId parent category id
     * @return list of categories
     */
    @WebMethod(operationName = "GetSubCategories")
    @WebResult(name = "category")
    List<CategoryDto> getSubCategories(@WebParam(name = "categoryId") Long categoryId);

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
    Long placeAd(@WebParam(name = "ad") @NotNull AdDto adDto) throws UserNotFoundException, AdValidationException;
    
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
            throws UserNotFoundException, AdNotFoundException, AdValidationException, AuthorizationException;
    
    /**
     * Clones the given ad as a new one. The old one will be marked as deleted.
     * 
     * @param adDto
     * @return
     * @throws AdNotFoundException
     * @throws AdValidationException
     * @throws AuthorizationException 
     * @throws InvalidAdStateException
     */
    @WebMethod(operationName = "CloneAd")
    @WebResult(name = "adId")
    Long cloneAd(@WebParam(name = "ad") @NotNull AdDto adDto)
            throws UserNotFoundException, AdNotFoundException, AdValidationException, AuthorizationException, InvalidAdStateException;
    
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
            throws UserNotFoundException, AdNotFoundException, AuthorizationException, InvalidAdStateException;
    
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
            throws UserNotFoundException, AdNotFoundException, AuthorizationException, ImageNotFoundException;
    
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
            throws UserNotFoundException, AdNotFoundException, AuthorizationException, ImageNotFoundException;
    
    
    
    //*********************
    //* approvals related *
    //*********************
    
    /**
     * Returns the available approvals for the given ad.
     * 
     * @param adId
     * @return
     * @throws AdNotFoundException
     * @throws AuthorizationException 
     */
    @WebMethod(operationName = "GetApprovals")
    @WebResult(name = "approval")
    List<ApprovalDto> getApprovals(@WebParam(name = "adId") Long adId)
            throws UserNotFoundException, AdNotFoundException, AuthorizationException;
    
    /**
     * 
     * @param adId
     * @param revision
     * @return
     * @throws AdNotFoundException
     * @throws AuthorizationException
     * @throws ApprovalNotFoundException 
     */
    @WebMethod(operationName = "GetApproval")
    @WebResult(name = "approval")
    ApprovalDto getApproval(@WebParam(name = "adId") Long adId, @WebParam(name = "revision") Integer revision)
            throws UserNotFoundException, AdNotFoundException, AuthorizationException, ApprovalNotFoundException;

    
    
    //*************************
    //* ads listing/filtering *
    //*************************
    
    /**
     * Returns a list of ads with id is less than specified one.
     * 
     * @param lastIndex last index used at paging
     * @param numberAds the maximum number of ads to return
     * @return list of ads
     */
    @WebMethod(operationName = "GetAds")
    @WebResult(name = "ad")
    List<AdDto> getAds(@WebParam(name = "lastIndex") int lastIndex,
            @WebParam(name = "numberAds") int numberAds) throws UserNotFoundException;

    /**
     * Returns a list of ads with id is less than specified one which fit the filter.
     * 
     * @param lastIndex last index used at paging
     * @param numberAds the maximum number ads to return
     * @param filter filter conditions
     * @return list of ads
     */
    @WebMethod(operationName = "GetAdsEx")
    @WebResult(name = "ad")
    List<AdDto> getAds(
            @WebParam(name = "lastIndex") int lastIndex,
            @WebParam(name = "numberAds") int numberAds,
            @WebParam(name = "filter") FilterDto filter)
            throws UserNotFoundException;

    /**
     * @see AdService#getAds(int, int, com.venefica.service.dto.FilterDto) 
     * 
     * @param lastIndex last index used at paging
     * @param numberAds the maximum number ads to return
     * @param filter filter conditions
     * @param includeImages flag to include ad images
     * @param includeCreator flag to include creator details
     * @return list of ads
     */
    @WebMethod(operationName = "GetAdsExDetail")
    @WebResult(name = "ad")
    List<AdDto> getAds(
            @WebParam(name = "lastIndex") int lastIndex,
            @WebParam(name = "numberAds") int numberAds,
            @WebParam(name = "filter") FilterDto filter,
            @WebParam(name = "includeImages") Boolean includeImages,
            @WebParam(name = "includeCreator") Boolean includeCreator,
            @WebParam(name = "includeCommentsNumber") int includeCommentsNumber,
            @WebParam(name = "includeCreatorStatistics") Boolean includeCreatorStatistics)
            throws UserNotFoundException;
    
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
     * @param numberAds if is negative or equals with 0 no any max result will be used
     * @param includeRequests flag to include available requests
     * @param includeUnapproved flag to mark if online and approved ads will be included in the result or not
     * @return list of ads
     * @throws UserNotFoundException is thrown when the user with the specified id is not found
     */
    @WebMethod(operationName = "GetUserAds")
    @WebResult(name = "ad")
    List<AdDto> getUserAds(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "numberAds") int numberAds,
            @WebParam(name = "includeRequests") Boolean includeRequests,
            @WebParam(name = "includeUnapproved") Boolean includeUnapproved) throws UserNotFoundException;
    
    /**
     * Not published via WS.
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    int getUserAdsSize(Long userId) throws UserNotFoundException;
    
    /**
     * Returns a list of all ads requested by the given user. The matching
     * requests should be visible (not hidden and not deleted) and their state
     * should not be CANCELED.
     * 
     * @return list of ads
     * @throws UserNotFoundException is thrown when the user with the specified id is not found
     */
    @WebMethod(operationName = "GetUserRequestedAds")
    @WebResult(name = "ad")
    List<AdDto> getUserRequestedAds(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "includeRequests") Boolean includeRequests) throws UserNotFoundException;
    
    /**
     * Not published via WS.
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    int getUserRequestedAdsSize(Long userId) throws UserNotFoundException;
    
    /**
     * Returns the ad by its id.
     * 
     * @param adId the id of the ad
     * @param includeRequests include or not the requests for this ad
     * @param includeCreatorStatistics include or not the ad creator (owner) user related statistics
     * @return detailed ad object
     * @throws AdNotFoundException is thrown when the ad with the specified id not found
     */
    @WebMethod(operationName = "GetAdById")
    @WebResult(name = "ad")
    AdDto getAdById(
            @WebParam(name = "adId") Long adId,
            @WebParam(name = "includeRequests") Boolean includeRequests,
            @WebParam(name = "includeCreatorStatistics") Boolean includeCreatorStatistics)
            throws UserNotFoundException, AdNotFoundException, AuthorizationException;
    
    /**
     * 
     * @return
     */
    @WebMethod(operationName = "GetHiddenForSearchAds")
    @WebResult(name = "ad")
    List<AdDto> getHiddenForSearchAds();
    
    
    
    //*****************
    //* ad statistics *
    //*****************
    
    @WebMethod(operationName = "GetStatistics")
    @WebResult(name = "statistics")
    AdStatisticsDto getStatistics(@WebParam(name = "adId") @NotNull Long adId) throws AdNotFoundException;
    
    
    
    //***********************
    //* ad lifecycle change *
    //***********************
    
    //
    // Note and todo: these are not complete as the delete is also a lifecycle change,
    // so needs a little bit of rethinking.
    //
    
    /**
     * Ends the ad with the specified id.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException is thrown when the ending ad not found.
     * @throws InvalidAdStateException if the ad is already ended
     * @throws AuthorizationException is thrown when a user different from
     * the creator is trying to end the ad.
     */
    @WebMethod(operationName = "EndAd")
    void endAd(@WebParam(name = "adId") Long adId)
            throws UserNotFoundException, AdNotFoundException, InvalidAdStateException, AuthorizationException;

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
    void relistAd(@WebParam(name = "adId") Long adId)
            throws UserNotFoundException, AdNotFoundException, AuthorizationException, InvalidAdStateException;
    
    
    
    //***************
    //* ad requests *
    //***************
    
    /**
     * Marks the given request as hidden in the GUI.
     * 
     * @param requestId
     * @throws RequestNotFoundException
     * @throws InvalidRequestException 
     */
    @WebMethod(operationName = "HideRequest")
    void hideRequest(@WebParam(name = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException, InvalidRequestException;
    
    /**
     * Creates a new request on the given ad. The owner cannot request
     * it's own ad.
     * 
     * @param adId
     * @param text 
     * @return the created request id
     * @throws AdNotFoundException 
     * @throws AlreadyRequestedException
     * @throws InvalidRequestException if the ad creator and requestor user is
     * the same
     */
    @WebMethod(operationName = "RequestAd")
    @WebResult(name = "requestId")
    Long requestAd(@WebParam(name = "adId") Long adId, @WebParam(name = "text") String text)
            throws UserNotFoundException, AdNotFoundException, AlreadyRequestedException, InvalidRequestException, InvalidAdStateException;
    
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
    void cancelRequest(@WebParam(name = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException, InvalidRequestException, InvalidAdStateException;
    
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
    void selectRequest(@WebParam(name = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException, InvalidRequestException, InvalidAdStateException;
    
    /**
     * Mark the request as redeemed.
     * 
     * @param requestId
     * @throws UserNotFoundException
     * @throws RequestNotFoundException
     * @throws InvalidRequestException
     * @throws InvalidAdStateException 
     */
    @WebMethod(operationName = "RedeemRequest")
    void redeemRequest(@WebParam(name = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException, InvalidRequestException, InvalidAdStateException;
    
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
    void markAsSent(@WebParam(name = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException, InvalidRequestException;
    
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
    void markAsReceived(@WebParam(name = "requestId") Long requestId)
            throws UserNotFoundException, RequestNotFoundException, InvalidRequestException;
    
    
    
    //*****************
    //* request issue *
    //*****************
    
    @WebMethod(operationName = "AddRequestIssue")
    void addRequestIssue(@WebParam(name = "requestId") Long requestId, @WebParam(name = "text") String text)
            throws UserNotFoundException, RequestNotFoundException;
    
    
    
    //************
    //* shipping *
    //************
    
    @WebMethod(operationName = "GetShippingBoxes")
    @WebResult(name = "shippingBox")
    List<ShippingBoxDto> getShippingBoxes() throws UserNotFoundException;
    
    
    
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
    Long bookmarkAd(@WebParam(name = "adId") @NotNull Long adId)
            throws UserNotFoundException, AdNotFoundException, GeneralException;

    /**
     * Removes the bookmark from the database.
     * 
     * @param bookmarkId id of the bookmark
     * @throws BookmarkNotFoundException if the bookmark with the specified
     * id not found
     */
    @WebMethod(operationName = "RemoveBookmark")
    void removeBookmark(@WebParam(name = "adId") @NotNull Long adId) 
            throws UserNotFoundException, BookmarkNotFoundException;

    /**
     * Returns a list of bookmarked ads.
     * 
     * @return list of ads
     */
    @WebMethod(operationName = "GetBookmarkedAds")
    @WebResult(name = "ad")
    List<AdDto> getBookmarkedAds(@WebParam(name = "includeRequests") Boolean includeRequests);
    
    /**
     * Returns a list of bookmarked ads for the specified user.
     * 
     * @return list of ads
     * @throws UserNotFoundException when the given user id does not exists
     */
    @WebMethod(operationName = "GetBookmarkedAdsForUser")
    @WebResult(name = "ad")
    List<AdDto> getBookmarkedAds(
            @WebParam(name = "userId") @NotNull Long userId,
            @WebParam(name = "includeRequests") Boolean includeRequests) throws UserNotFoundException;

    /**
     * Not published via WS.
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    int getBookmarkedAdsSize(Long userId) throws UserNotFoundException;
    
    
    
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
    void markAsSpam(@WebParam(name = "adId") @NotNull Long adId) throws UserNotFoundException, AdNotFoundException;

    /**
     * Un-marks the ad as spam.
     * 
     * @param adId id of the ad
     * @throws AdNotFoundException if the ad with the specified id not found
     */
    @WebMethod(operationName = "UnmarkAsSpam")
    void unmarkAsSpam(@WebParam(name = "adId") @NotNull Long adId) throws UserNotFoundException, AdNotFoundException;

    
    
    //*************
    //* ad rating *
    //*************
    
    /**
     * Add a rating to the ad.
     * 
     * @param ratingDto
     * @return new calculated rating
     * @throws RequestNotFoundException if the request with the specified id not found
     * @throws InvalidRateOprationException if user tries to rate his own ad
     * @throws AlreadyRatedException if user tries to rate the same ad more
     * than one time
     */
    @WebMethod(operationName = "RateAd")
    @WebResult(name = "rating")
    float rateAd(@WebParam(name = "rating") @NotNull RatingDto ratingDto)
            throws RequestNotFoundException, UserNotFoundException, InvalidRateOperationException, AlreadyRatedException, InvalidAdStateException;
    
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
    
    /**
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    @WebMethod(operationName = "GetReceivedRatingsAsOwner")
    @WebResult(name = "rating")
    public List<RatingDto> getReceivedRatingsAsOwner(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    @WebMethod(operationName = "GetReceivedRatingsAsReceiver")
    @WebResult(name = "rating")
    public List<RatingDto> getReceivedRatingsAsReceiver(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    @WebMethod(operationName = "GetAllReceivedRatings")
    @WebResult(name = "rating")
    public List<RatingDto> getAllReceivedRatings(@WebParam(name = "userId") @NotNull Long userId) throws UserNotFoundException;
    
    /**
     * The size of all the received ratings for the given user.
     * Not exposed via WS.
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    public int getAllReceivedRatingsSize(Long userId) throws UserNotFoundException;
    
    /**
     * The size of all the sent ratings for the given user.
     * Not exposed via WS.
     * 
     * @param userId
     * @return
     * @throws UserNotFoundException 
     */
    public int getAllSentRatingsSize(Long userId) throws UserNotFoundException;
}
