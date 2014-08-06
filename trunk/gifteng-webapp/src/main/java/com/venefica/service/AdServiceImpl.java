package com.venefica.service;

import com.venefica.common.RandomGenerator;
import com.venefica.config.AppConfig;
import com.venefica.config.Constants;
import com.venefica.config.EmailConfig;
import com.venefica.dao.ApprovalDao;
import com.venefica.dao.BookmarkDao;
import com.venefica.dao.CategoryDao;
import com.venefica.dao.CommentDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.IssueDao;
import com.venefica.dao.MessageDao;
import com.venefica.dao.RatingDao;
import com.venefica.dao.ShippingBoxDao;
import com.venefica.dao.SpamMarkDao;
import com.venefica.dao.UserTransactionDao;
import com.venefica.dao.ViewerDao;
import com.venefica.model.Ad;
import com.venefica.model.AdData;
import com.venefica.model.AdStatus;
import com.venefica.model.AdType;
import com.venefica.model.Approval;
import com.venefica.model.Bookmark;
import com.venefica.model.BusinessAdData;
import com.venefica.model.Category;
import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.ImageModelType;
import com.venefica.model.Issue;
import com.venefica.model.MemberUserData;
import com.venefica.model.Message;
import com.venefica.model.NotificationType;
import com.venefica.model.PromoCodeProvider;
import com.venefica.model.Rating;
import com.venefica.model.Request;
import com.venefica.model.RequestStatus;
import com.venefica.model.Shipping;
import com.venefica.model.ShippingBox;
import com.venefica.model.SpamMark;
import com.venefica.model.TransactionStatus;
import com.venefica.model.User;
import com.venefica.model.UserPoint;
import com.venefica.model.UserTransaction;
import com.venefica.model.Viewer;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AdStatisticsDto;
import com.venefica.service.dto.ApprovalDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.FilterType;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.PromoCodeProviderDto;
import com.venefica.service.dto.RatingDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.dto.ShippingBoxDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdField;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.ApprovalNotFoundException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.GeneralException;
import com.venefica.service.fault.ImageField;
import com.venefica.service.fault.ImageNotFoundException;
import com.venefica.service.fault.ImageValidationException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRateOperationException;
import com.venefica.service.fault.InvalidRequestException;
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adService")
@WebService(endpointInterface = "com.venefica.service.AdService")
public class AdServiceImpl extends AbstractService implements AdService {

    @Inject
    private AppConfig appConfig;
    @Inject
    private EmailConfig emailConfig;
    
    @Inject
    private UserManagementService userManagementService;
    @Inject
    private PromoCodeService promoCodeService;
    
    @Inject
    private ApprovalDao approvalDao;
    @Inject
    private CategoryDao categoryDao;
    @Inject
    private ImageDao imageDao;
    @Inject
    private BookmarkDao bookmarkDao;
    @Inject
    private SpamMarkDao spamMarkDao;
    @Inject
    private RatingDao ratingDao;
    @Inject
    private CommentDao commentDao;
    @Inject
    private MessageDao messageDao;
    @Inject
    private ViewerDao viewerDao;
    @Inject
    private UserTransactionDao userTransactionDao;
    @Inject
    private IssueDao issueDao;
    @Inject
    private ShippingBoxDao shippingBoxDao;

    private final boolean useEmailSender = true;
    
    //**********************
    //* categories related *
    //**********************
    
    @Override
    @Transactional
    public CategoryDto getCategory(Long categoryId) {
        try {
            Category category = categoryDao.get(categoryId);
            CategoryDto categoryDto = new CategoryDto(category, true);
            return categoryDto;
        } catch ( Exception ex ) {
            logger.error("Exception thrown when trying to get category (categoryId: " + categoryId + ")", ex);
            return null;
        }
    }
    
    @Override
    public List<CategoryDto> getSubCategories(Long categoryId) {
        return getCategories(categoryId, false);
    }

    @Override
    @Transactional
    public List<CategoryDto> getAllCategories() {
        return getCategories(null, true);
    }
    
    

    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
    @Override
    @Transactional
    public Long placeAd(AdDto adDto) throws UserNotFoundException, AdValidationException {
        return placeAd(adDto, null);
    }
    
    @Override
    @Transactional
    public void updateAd(AdDto adDto) throws UserNotFoundException, AdNotFoundException, AdValidationException, AuthorizationException {
        Ad ad = validateAd(adDto.getId());
        User currentUser = getCurrentUser();
        
        // ++ TODO: create ad validator
        if (adDto.getTitle() == null) {
            throw new AdValidationException(AdField.TITLE, "Title is null!");
        } else if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("You can update only your own ads!");
        }

        ShippingBox shippingBox = getShipingBox(adDto.getShippingBox());
        PromoCodeProvider promoCodeProvider = getPromoCodeProvider(adDto.getPromoCodeProvider());
        Category category = validateCategory(adDto.getCategoryId());
        UserTransaction adTransaction = userTransactionDao.getByAd(ad.getId());
        
        if ( shippingBox == null && (adDto.getPickUp() == null || adDto.getPickUp() == false) ) {
            throw new AdValidationException(AdField.DELIVERY, "Pickup or shipping should be defined!");
        } else if ( adTransaction == null ) {
            throw new AdValidationException("There is no attached transaction for ad (adId: " + ad.getId() + ") - update failed.");
        }

        // WARNING: This update must be performed within an active transaction!
        adDto.updateAd(ad);

        ad.incrementRevision();
        ad.unmarkAsApproved();
        ad.unmarkAsOnline();
        ad.setStatus(AdStatus.OFFLINE);
        
        ad.getAdData().setShippingBox(shippingBox);
        ad.getAdData().setCategory(category);
        
        if ( ad.isBusinessAd() ) {
            ((BusinessAdData) getAdData(ad)).setPromoCodeProvider(promoCodeProvider);
        }
        
        updateImage(ad, adDto, AdField.IMAGE);
        updateImage(ad, adDto, AdField.THUMB_IMAGE);
        updateImage(ad, adDto, AdField.BARCODE_IMAGE);
        
        adDataDao.update(ad.getAdData());

        //update existing transaction with the new numbers
        adTransaction.setPendingGivingNumber(UserTransaction.getGivingNumber(ad));
        userTransactionDao.update(adTransaction);
        
        Set<Request> validRequests = ad.getValidRequests();
        if ( validRequests != null && !validRequests.isEmpty() ) {
            for ( Request request : validRequests ) {
                Long requestId = request.getId();
                UserTransaction requestTransaction = userTransactionDao.getByRequest(requestId);
                if ( requestTransaction == null ) {
                    logger.error("There is no user transaction for the request (requestId: " + requestId + ")");
                    continue;
                }
                
                //update existing transactions with the new numbers
                requestTransaction.setPendingReceivingNumber(UserTransaction.getReceivingNumber(request));
                userTransactionDao.update(requestTransaction);
            }
        }
        
//        if (adDto.getExpiresAt() != null && currentUser.isBusinessAccount()) {
//                ad.setExpiresAt(adDto.getExpiresAt());
//                ad.setExpired(false);
//        }

        // ++
    }
    
    @Override
    @Transactional
    public Long cloneAd(AdDto adDto) throws UserNotFoundException, AdNotFoundException, AdValidationException, AuthorizationException, InvalidAdStateException {
        Ad oldAd = validateAd(adDto.getId());
        User currentUser = getCurrentUser();
        
        if (!oldAd.getCreator().equals(currentUser)) {
            throw new AuthorizationException("You can clone only your own ads!");
        }
        
        ImageDto imageDto = adDto.getImage();
        if ( !imageDto.isValid() ) {
            try {
                Image image = imageDao.get(imageDto.getId(), ImageModelType.AD);
                imageDto.updateImageDto(image);
            } catch ( IOException ex ) {
                logger.error("Exception thrown when trying to get cloned ad (adId: " + adDto.getId() + ") image", ex);
            }
        }
        
        adDto.setExpires(true);
        adDto.setExpiresAt(calculateExpiration());
        adDto.setAvailableAt(new Date());
        adDto.setQuantity(adDto.getQuantity() == null || adDto.getQuantity() <= 0 ? 1 : adDto.getQuantity()); //the default quantity
        
        Long adId = placeAd(adDto, oldAd);
        deleteAd(oldAd.getId()); //removing the old (cloned) ad
        
        return adId;
    }
    
    @Override
    @Transactional
    public void deleteAd(Long adId) throws UserNotFoundException, AdNotFoundException, AuthorizationException, InvalidAdStateException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();
        UserTransaction adTransaction = userTransactionDao.getByAd(adId);

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can delete the ad");
        }
        if ( ad.isDeleted() ) {
            throw new InvalidAdStateException("Ad (adId: " + adId + ") is already deleted");
        }
        if ( adTransaction == null ) {
            throw new InvalidAdStateException("There is no transaction associated with this ad (adId: " + adId + ")");
        }
        
        UserPoint userPoint = currentUser.getUserPoint();
        if ( userPoint == null ) {
            throw new InvalidAdStateException("There is no valid user point for current user (userId: " + currentUser.getId() + ")");
        }
        
        ad.markAsDeleted();
        
        adTransaction.markAsFinalized(TransactionStatus.DELETED);
        userTransactionDao.update(adTransaction);
        
        if ( UserPoint.canUpdateRequestLimit(ad) ) {
            userPoint.incrementRequestLimit(appConfig.getRequestLimitAdDeleted());
            userPointDao.update(userPoint);
        }
        
        Set<Request> validRequests = ad.getValidRequests();
        if ( validRequests != null && !validRequests.isEmpty() ) {
            for ( Request request : validRequests ) {
                Long requestId = request.getId();
                UserTransaction requestTransaction = userTransactionDao.getByRequest(requestId);
                if ( requestTransaction == null ) {
                    logger.error("There is no user transaction for the request (requestId: " + requestId + ")");
                    continue;
                } else if ( requestTransaction.isFinalized() ) {
                    //transaction is already finalized
                    continue;
                }
                
                requestTransaction.markAsFinalized(TransactionStatus.DELETED);
                userTransactionDao.update(requestTransaction);
            }
        }
    }

    @Override
    @Transactional
    public Long addImageToAd(Long adId, ImageDto imageDto) throws AdNotFoundException, ImageValidationException {
        Ad ad = validateAd(adId);

        // ++ TODO: create image validator
        if (imageDto == null) {
            throw new NullPointerException("imageDto is null");
        }
        if (imageDto.getImgType() == null) {
            throw new ImageValidationException(ImageField.IMAGE_TYPE, "Image type not specified!");
        }
        if (imageDto.getData() == null || imageDto.getData().length == 0) {
            throw new ImageValidationException(ImageField.DATA, "Image data must be not empty!");
        }
        // ++

        // Save and attache the image to the ad
        Image image = imageDto.toImage();
        try {
            imageDao.save(image, ImageModelType.AD);
        } catch ( IOException ex ) {
            logger.error("Exception when adding image to ad (adId: " + adId + ")", ex);
            throw new ImageValidationException(ImageField.DATA, "Image cannot be added to ad (adId: " + adId + ")");
        }
        ad.addImage(image);

        return image.getId();
    }

    @Override
    @Transactional
    public void deleteImageFromAd(Long adId, Long imageId) throws UserNotFoundException, AdNotFoundException, AuthorizationException, ImageNotFoundException {
        deleteImagesFromAd(adId, Arrays.<Long>asList(imageId));
    }
    
    @Override
    @Transactional
    public void deleteImagesFromAd(Long adId, List<Long> imageIds) throws UserNotFoundException, AdNotFoundException, AuthorizationException, ImageNotFoundException {
        if ( imageIds == null || imageIds.isEmpty() ) {
            logger.debug("Cannot delete empty list of images!");
            return;
        }
        
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only creator can delete images!");
        }

        if ( imageIds.size() == 1 ) {
            Long imageId = imageIds.get(0);
            Image image = validateImage(imageId);
            
            if (!ad.getAdData().getImages().contains(image)) {
                throw new AuthorizationException("Image doesn't belong to the specified ad!");
            }
            
            try {
                imageDao.delete(image, ImageModelType.AD);
            } catch ( Exception ex ) {
                logger.error("Exception when removing image (imageId: " + imageId + ") from ad (adId: " + adId + ")", ex);
                throw new ImageNotFoundException("Image (imageId: " + imageId + ") cannot be removed");
            }
            
            ad.removeImage(image);
            adDataDao.update(ad.getAdData());
        } else {
            //some exceptions are not thrown, instead are captured and continued with the next image
            for ( Long imageId : imageIds ) {
                Image image;
                try {
                    image = validateImage(imageId);
                } catch ( ImageNotFoundException ex ) {
                    logger.warn("Cannot find image (imageId: " + imageId + ").");
                    continue;
                }

                if (!ad.getAdData().getImages().contains(image)) {
                    logger.warn("Image (imageId: " + imageId + ") doesn't belong to the specified ad!");
                    continue;
                }

                try {
                    imageDao.delete(image, ImageModelType.AD);
                    ad.removeImage(image);
                } catch ( Exception ex ) {
                    logger.error("Exception when removing image (imageId: " + imageId + ") from ad (adId: " + adId + ")", ex);
                }
            }
            adDataDao.update(ad.getAdData());
        }
    }

    
    
    //*********************
    //* approvals related *
    //*********************
    
    @Override
    @Transactional
    public List<ApprovalDto> getApprovals(Long adId) throws UserNotFoundException, AdNotFoundException, AuthorizationException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can access approvals!");
        }
        
        List<ApprovalDto> result = new LinkedList<ApprovalDto>();
        List<Approval> approvals = approvalDao.getByAd(adId);
        
        for ( Approval approval : approvals ) {
            ApprovalDto approvalDto = new ApprovalDto(approval);
            result.add(approvalDto);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public ApprovalDto getApproval(Long adId, Integer revision) throws UserNotFoundException, AdNotFoundException, AuthorizationException, ApprovalNotFoundException {
        User currentUser = getCurrentUser();
        Ad ad = validateAd(adId);
        
        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can access approvals!");
        }
        
        Approval approval = approvalDao.getByAdAndRevision(adId, revision);
        if ( approval == null ) {
            throw new ApprovalNotFoundException(adId);
        }
        
        return new ApprovalDto(approval);
    }
    
    
    //*************************
    //* ads listing/filtering *
    //*************************
    
    @Override
    @Transactional
    public List<AdDto> getAds(int lastIndex, int numberAds) throws UserNotFoundException {
        return getAds(lastIndex, numberAds, null);
    }

    @Override
    @Transactional
    public List<AdDto> getAds(int lastIndex, int numberAds, FilterDto filter) throws UserNotFoundException {
        return getAds(lastIndex, numberAds, filter, false, false, 0, false);
    }
    
    @Override
    @Transactional
    public List<AdDto> getAds(int lastIndex, int numberAds, FilterDto filter, Boolean includeImages, Boolean includeCreator, int includeCommentsNumber, Boolean includeCreatorStatistics) throws UserNotFoundException {
        List<AdDto> result = new LinkedList<AdDto>();
        getAds(result, getCurrentUser(), lastIndex, numberAds, filter, includeImages, includeCreator, includeCommentsNumber, includeCreatorStatistics);
        return new LinkedList<AdDto>(new LinkedHashSet<AdDto>(result)); //eliminating duplicates (if any)
    }

    @Override
    @Transactional
    public List<AdDto> getMyAds(Boolean includeRequests) {
        try {
            User currentUser = getCurrentUser();
            return getUserAds(currentUser.getId(), Integer.MAX_VALUE, includeRequests, true);
        } catch ( UserNotFoundException ex ) {
            logger.error("User (current/logged) not found", ex);
            return new ArrayList<AdDto>();
        }
    }
    
    @Override
    @Transactional
    public List<AdDto> getUserAds(Long userId, int numberAds, Boolean includeRequests, Boolean includeUnapproved) throws UserNotFoundException {
        User currentUser = getCurrentUser();
        User user = validateUser(userId);
        
        if ( includeRequests == null ) {
            includeRequests = false;
        }
        if ( includeUnapproved == null ) {
            includeUnapproved = currentUser.equals(user);
        }
        if ( numberAds <= 0 ) {
            numberAds = Integer.MAX_VALUE;
        }
        
        List<Ad> ads = getUserOwnedAds(userId, numberAds, includeUnapproved);
        List<AdDto> result = new LinkedList<AdDto>();
        
        for (Ad ad : ads) {
            AdDto adDto = new AdDtoBuilder(ad, includeRequests ? getAdData(ad) : null, getUserData(currentUser))
                    .setCurrentUser(currentUser)
                    .includeRequests(includeRequests)
                    .includeCanRequest()
                    .includeCanRate()
                    .build();
            
            if ( includeRequests ) {
                setNumUnreadMessages(adDto, currentUser.getId());
                setApproval(adDto);
            }
            
            result.add(adDto);
        }
        return result;
    }
    
    @Override
    @Transactional
    public int getUserAdsSize(Long userId) throws UserNotFoundException {
        User currentUser = getCurrentUser();
        User user = validateUser(userId);
        boolean includeUnapproved = currentUser.equals(user);
        List<Ad> ads = getUserOwnedAds(userId, Integer.MAX_VALUE, includeUnapproved);
        return ads != null ? ads.size() : 0;
    }
    
    @Override
    @Transactional
    public List<AdDto> getUserRequestedAds(Long userId, Boolean includeRequests) throws UserNotFoundException {
        User currentUser = getCurrentUser();
        List<Ad> ads = getUserRequestedAds(userId);
        List<AdDto> result = new LinkedList<AdDto>();
        
        if ( includeRequests == null ) {
            includeRequests = false;
        }
        
        for ( Ad ad : ads ) {
            getUserData(ad.getCreator()); //this sets the correct user data
            
            AdDto adDto = new AdDtoBuilder(ad, includeRequests ? getAdData(ad) : null, getUserData(currentUser))
                    .setCurrentUser(currentUser)
                    .includeCreator()
                    .includeRequests(includeRequests)
                    .includeCanRequest()
                    .includeCanRate()
                    .build();
            adDto.setInBookmarks(inBookmarks(currentUser, ad));
            adDto.setRequested(ad.isRequested(currentUser));
            
            if ( includeRequests ) {
                setNumUnreadMessages(adDto, currentUser.getId());
            }
            
            result.add(adDto);
        }
        return result;
    }
    
    @Override
    @Transactional
    public int getUserRequestedAdsSize(Long userId) throws UserNotFoundException {
        List<Ad> ads = getUserRequestedAds(userId);
        return ads != null ? ads.size() : 0;
    }

    @Override
    @Transactional
    public AdDto getAdById(Long adId, Boolean includeRequests, Boolean includeCreatorStatistics) throws UserNotFoundException, AdNotFoundException, AuthorizationException {
        User currentUser = getCurrentUser();
        Ad ad = validateAd(adId);
        
        if ( includeRequests == null ) {
            includeRequests = true;
        }
        if ( includeCreatorStatistics == null ) {
            includeCreatorStatistics = false;
        }
        
        if ( !ad.isOnline() || !ad.isApproved() ) {
            if ( !currentUser.isAdmin() && !ad.getCreator().equals(currentUser)  ) {
                //current is not admin and is not ad owner
                throw new AuthorizationException("Ad (id: " + adId + ") is not yet available (only for admins)");
            }
        }
        
        if (!ad.getCreator().equals(currentUser) && !ad.isAlreadyViewedBy(currentUser)) {
            ad.visit();
        }
        
        Viewer viewer = new Viewer(ad, currentUser);
        viewerDao.save(viewer);

        getUserData(ad.getCreator()); //this sets the correct user data
        
        AdDto adDto = new AdDtoBuilder(ad, includeRequests ? getAdData(ad) : null, getUserData(currentUser))
                .setCurrentUser(currentUser)
                .includeImages()
                .includeCreator()
                .includeRequests(includeRequests)
                .includeCanMarkAsSpam()
                .includeCanRate()
                .includeCanRequest()
                .build();

        adDto.setInBookmarks(inBookmarks(currentUser, ad));
        adDto.setRequested(ad.isRequested(currentUser));
        
        if ( includeRequests ) {
            setNumUnreadMessages(adDto, currentUser.getId());
        }
        if ( includeCreatorStatistics ) {
            adDto.getCreator().setStatistics(userManagementService.getStatistics(ad.getCreator().getId()));
        }
        
        return adDto;
    }
    
    @Override
    @Transactional
    public List<AdDto> getHiddenForSearchAds() {
        List<Ad> ads = adDao.getHiddenForSearchAds();
        List<AdDto> result = new LinkedList<AdDto>();
        
        for ( Ad ad : ads ) {
            AdDto adDto = new AdDtoBuilder(ad, getAdData(ad), null)
                    .includeCreator(false)
                    .includeFollower(false)
                    .includeRelist(false)
                    .includeRequests(false)
                    .includeAdStatistics(false)
                    .build();
            result.add(adDto);
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<AdDto> getStaffPickAds() {
        List<Ad> ads = adDao.getStaffPickAds();
        List<AdDto> result = new LinkedList<AdDto>();
        
        for ( Ad ad : ads ) {
            AdDto adDto = new AdDtoBuilder(ad, getAdData(ad), null)
                    .includeCreator(false)
                    .includeFollower(false)
                    .includeRelist(false)
                    .includeRequests(false)
                    .includeAdStatistics(false)
                    .build();
            result.add(adDto);
        }
        return result;
    }
    
    @Override
    @Transactional
    public void setStaffPick(Long adId, Boolean isStaffPick) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        if ( isStaffPick == null ) {
            isStaffPick = false;
        }
        
        ad.setStaffPick(isStaffPick);
    }
    
    
    
    //*****************
    //* ad statistics *
    //*****************
    
    @Override
    @Transactional
    public AdStatisticsDto getStatistics(Long adId) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        return AdStatisticsDto.build(ad);
    }

    
    
    //***********************
    //* ad lifecycle change *
    //***********************
    
    @Override
    @Transactional
    public void relistAd(Long adId) throws UserNotFoundException, AdNotFoundException, AuthorizationException, InvalidAdStateException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can relist the ad!");
        }
        if (ad.isDeleted() /* || ad.isSold() || ad.isSpam() */) {
            throw new InvalidAdStateException("Ad can't be relisted as is deleted!");
        }
        if ( !ad.isExpired() ) {
            throw new InvalidAdStateException("Ad can't be relisted as its state (" + ad.getStatus() + ") is not as expected!");
        }
        if ( !ad.canProlong() ) {
            throw new InvalidAdStateException("Ad can't be relisted anymore!");
        }
        
        //only EXPIRED ads can be relisted
        ad.prolong(appConfig.getAdProlongationPeriodMinutes());
        
        UserTransaction adTransaction = userTransactionDao.getByAd(ad.getId());
        if ( adTransaction != null ) {
            adTransaction.unmarkAsFinalized();
            userTransactionDao.update(adTransaction);
        } else {
            logger.error("There is no transaction associated with this ad (adId: " + ad.getId() + ")");
        }
    }

    @Override
    @Transactional
    public void endAd(Long adId) throws UserNotFoundException, AdNotFoundException, InvalidAdStateException, AuthorizationException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can ends the ad");
        }
        if ( ad.isSold() ) {
            throw new InvalidAdStateException("The ad (adId: " + adId + ") is already ended/finalized");
        }

        ad.markAsSold();
        //ad.setExpired(true);
        //ad.setExpiresAt(new Date());
    }
    
    
    
    //***************
    //* ad requests *
    //***************
    
    @Override
    @Transactional
    public void hideRequest(Long requestId) throws UserNotFoundException, RequestNotFoundException, InvalidRequestException {
        Request request = validateRequest(requestId);
        User currentUser = getCurrentUser();
        Ad ad = request.getAd();
        
        if ( !request.getUser().equals(currentUser) ) {
            throw new InvalidRequestException("Only requestor can hide requests.");
        }
        if ( !ad.isExpired() && !request.isUnaccepted() && !request.isDeclined() && !request.isCanceled() ) {
            throw new InvalidRequestException("Only 'expired' (unaccepted or declined or canceled) requests (or expired ads) can be hidden.");
        }
        
        requestDao.hide(requestId);
    }
    
    @Override
    @Transactional
    public Long requestAd(Long adId, String text) throws UserNotFoundException, AdNotFoundException, AlreadyRequestedException, InvalidRequestException, InvalidAdStateException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();
        Long currentUserId = currentUser.getId();
        User creator = ad.getCreator();
        
        if (ad.getStatus() == AdStatus.ACTIVE || ad.getStatus() == AdStatus.IN_PROGRESS) {
            //continue
        } else {
            //only ACTIVE or IN_PROGRESS status ads can be requested
            throw new InvalidAdStateException("Ad can't be requested as its state (" + ad.getStatus() + ") is not as expected!");
        }
        
        Request request = requestDao.get(currentUserId, adId);
        if ( request != null ) {
            throw new AlreadyRequestedException("Ad (id: " + adId + ") already requested by the user (id: " + currentUserId + ")");
        }
        
        if ( UserPoint.canUpdateRequestLimit(ad) && !currentUser.canRequest() ) {
            //only member typed ads needs request limit verification
            throw new InvalidRequestException("Request limit reached - user cannot request.");
        }
        if ( currentUser.isBusinessAccount() ) {
            throw new InvalidRequestException("Business users cannot request ads.");
        }
        if ( creator.equals(currentUser) ) {
            throw new InvalidRequestException("Cannot request owned ads.");
        }
        if ( ad.getAdData().getQuantity() <= 0 ) {
            throw new InvalidRequestException("No more available.");
        }
        if ( ad.isMemberAd() && ad.getActiveRequests().size() >= Constants.REQUEST_MAX_ALLOWED ) {
            throw new InvalidRequestException("Max request limit reached.");
        }
        
        MemberUserData memberUserData = userDataDao.getMemberUserDataByUser(currentUser.getId());
        UserPoint userPoint = currentUser.getUserPoint();
        
        if ( userPoint == null ) {
            throw new InvalidRequestException("There is no valid user point for current user (userId: " + currentUserId + ")");
        } else if ( !userPoint.canRequest(ad, memberUserData.getUserSocialPoint()) ) {
            throw new InvalidRequestException("Insufficient user points to request ad (adId: " + adId + ")");
        }
        
        if ( UserPoint.canUpdateRequestLimit(ad) ) {
            userPoint.incrementRequestLimit(appConfig.getRequestLimitRequestNew());
            userPointDao.update(userPoint);
        }
        
        String promoCode = null;
        if ( ad.isBusinessAd() ) {
            BusinessAdData adData = (BusinessAdData) getAdData(ad);
            if ( adData.isGeneratePromoCodeForRequests() ) {
                promoCode = RandomGenerator.generateAlphanumeric(Constants.REQUEST_PROMO_CODE_DEFAULT_CODE_LENGTH);
            }
        }
        
        request = new Request();
        request.setSent(false);
        request.setReceived(false);
        request.setAd(ad);
        request.setUser(currentUser);
        request.setStatus(RequestStatus.PENDING);
        request.setPromoCode(promoCode);
        Long requestId = requestDao.save(request);
        
        PromoCodeProvider provider = null;
        if ( ad.isBusinessAd() ) {
            BusinessAdData adData = (BusinessAdData) getAdData(ad);
            provider = adData.getPromoCodeProvider();
            if ( provider != null ) {
                promoCodeService.processProvider(provider.getId(), request.getId());
            }
        }
        
        ad.setStatus(AdStatus.IN_PROGRESS);
        ad.setExpired(false);
        ad.setExpiresAt(calculateExpiration());
        
        UserTransaction requestTransaction = new UserTransaction(request);
        requestTransaction.setUser(currentUser);
        requestTransaction.setUserPoint(currentUser.getUserPoint());
        userTransactionDao.save(requestTransaction);
        
        if ( text != null && !text.trim().isEmpty() ) {
            text = text.trim();
        }
        
        if ( text != null ) {
            Message message = new Message(text);
            message.setTo(creator);
            message.setFrom(currentUser);
            message.setRequest(request);
            messageDao.save(message);
        }
        
        try {
            //NOTE: in case of request existing bookmark should be removed
            removeBookmark(adId);
        } catch ( BookmarkNotFoundException ex ) {
        }
        
        if ( ad.isBusinessAd() ) {
            //auto select request for business ads
            selectRequest(request, ad.getCreator());
            //auto sending ad by creator
            markAsSent(creator, request, false);
            
            if ( provider == null || provider.isAutoReceiveRequest() ) {
                //auto receiving request
                markAsReceived(currentUser, request);
            }
        }
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("creator", ad.getCreator());
        vars.put("request", request);
        vars.put("text", text);
        
        emailSender.sendNotification(NotificationType.AD_REQUESTED, creator, vars);
        
        return requestId;
    }
    
    @Override
    @Transactional
    public void cancelRequest(Long requestId) throws UserNotFoundException, RequestNotFoundException, InvalidRequestException, InvalidAdStateException {
        User currentUser = getCurrentUser();
        Request request = validateRequest(requestId);
        Ad ad = request.getAd();
//        AdData adData = ad.getAdData();
        
        if ( ad.getStatus() == AdStatus.IN_PROGRESS ) {
            //continue
        } else {
            throw new InvalidAdStateException("Request can't be cancelled as its ad state (" + ad.getStatus() + ") is not as expected!");
        }
        
        if ( ad.getCreator().equals(currentUser) || request.getUser().equals(currentUser) ) {
            //the ad creator cancelling the request (or the requestor)
        } else {
            throw new InvalidRequestException("Only owned requests can be cancelled");
        }
        
        boolean isDecline = false;
        if ( ad.getCreator().equals(currentUser) ) {
            isDecline = true;
        }
        
//        if ( ad.isMemberAd() ) {
//            if ( request.isAccepted() && !adData.isOnlyPickUp() ) {
//                //member typed ads (that are not only pickup) accepted request cannot be cancelled
//                throw new InvalidRequestException("Request (requestId: " + requestId + ") is already accepted/selected, canceling/declining not allowed as ad (adId: " + ad.getId() + ") is not only pickup.");
//            }
//        }
        
        UserTransaction requestTransaction = userTransactionDao.getByRequest(requestId);
        if ( requestTransaction == null ) {
            throw new InvalidRequestException("No associated transaction for the request (requestId: " + requestId + ")");
        }
        
        if ( isDecline ) {
            if ( request.isDeclined() ) {
                throw new InvalidRequestException("Request (requestId: " + requestId + ") is already canceled (declined)");
            }
            
            //canceling (declining) by the ad owner
            ad.decline(request);
        } else {
            if ( request.isCanceled() ) {
                throw new InvalidRequestException("Request (requestId: " + requestId + ") is already canceled");
            }

            //canceling by the requestor
            ad.cancel(request);
        }
        
        requestTransaction.markAsFinalized(TransactionStatus.CANCELED);
        userTransactionDao.update(requestTransaction);
        
        if ( UserPoint.canUpdateRequestLimit(ad) ) {
            UserPoint userPoint = request.getUser().getUserPoint();
            userPoint.incrementRequestLimit(isDecline ? appConfig.getRequestLimitRequestDeclined() : appConfig.getRequestLimitRequestCanceled());
            userPointDao.update(userPoint);
        }
        
        if ( isDecline ) {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("user", request.getUser());
            vars.put("ad", ad);
            
            emailSender.sendNotification(NotificationType.REQUEST_DECLINED, request.getUser(), vars);
        } else {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("user", ad.getCreator());
            vars.put("ad", ad);
            
            emailSender.sendNotification(NotificationType.REQUEST_CANCELED, ad.getCreator(), vars);
        }
    }
    
    @Override
    @Transactional
    public void selectRequest(Long requestId) throws UserNotFoundException, RequestNotFoundException, InvalidRequestException, InvalidAdStateException {
        Request request = validateRequest(requestId);
        User currentUser = getCurrentUser();
        
        selectRequest(request, currentUser);
    }
    
    @Override
    @Transactional
    public void redeemRequest(Long requestId) throws UserNotFoundException, RequestNotFoundException, InvalidRequestException, InvalidAdStateException {
        Request request = validateRequest(requestId);
        User currentUser = getCurrentUser();
        
        request.markAsRedeemed();
        requestDao.update(request);
    }
    
    @Override
    @Transactional
    public RequestDto getRequestById(Long requestId) throws RequestNotFoundException {
        Request request = validateRequest(requestId);
        getUserData(request.getUser()); //this sets the correct user data
        
        return new RequestDto(request, getAdData(request.getAd()));
    }
    
    @Override
    @Transactional
    public List<RequestDto> getRequests(Long adId) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        AdData adData = getAdData(ad);
        List<Request> requests = requestDao.getByAd(adId);
        List<RequestDto> result = new LinkedList<RequestDto>();
        
        if ( requests != null && !requests.isEmpty() ) {
            for ( Request request : requests ) {
                getUserData(request.getUser()); //this sets the correct user data
                
                result.add(new RequestDto(request, adData));
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<RequestDto> getRequestsByUser(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Request> requests = getActiveRequestsByUser(userId, true);
        List<RequestDto> result = new LinkedList<RequestDto>();
        
        for ( Request request : requests ) {
            AdData adData = getAdData(request.getAd());
            getUserData(request.getUser()); //this sets the correct user data
            
            result.add(new RequestDto(request, adData));
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<RequestDto> getRequestsForUserWithoutRating(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Request> requests = requestDao.getForUser(userId);
        List<RequestDto> result = new LinkedList<RequestDto>();
        
        if ( requests != null && !requests.isEmpty() ) {
            for ( Request request : requests ) {
                if ( !request.isAccepted() ) {
                    continue;
                }
                
                Long fromUserId = request.getUser().getId();
                Rating rating = ratingDao.get(fromUserId, request.getId());
                if ( rating == null ) {
                    AdData adData = getAdData(request.getAd());
                    getUserData(request.getUser()); //this sets the correct user data
                    
                    result.add(new RequestDto(request, adData));
                }
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public void markAsSent(Long requestId) throws UserNotFoundException, RequestNotFoundException, InvalidRequestException {
        User currentUser = getCurrentUser();
        Request request = validateRequest(requestId);
        
        if ( currentUser.isBusinessAccount() ) {
            throw new InvalidRequestException("Business users cannot send ads.");
        }
        
        markAsSent(currentUser, request, false);
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("user", request.getUser());
        vars.put("request", request);
        
        emailSender.sendNotification(NotificationType.REQUEST_SENT, request.getUser(), vars);
    }
    
    @Override
    @Transactional
    public void markAsReceived(Long requestId) throws UserNotFoundException, RequestNotFoundException, InvalidRequestException {
        Request request = validateRequest(requestId);
        User currentUser = getCurrentUser();
        Ad ad = request.getAd();
        
        if ( currentUser.isBusinessAccount() ) {
            throw new InvalidRequestException("Business users cannot receive ads.");
        }
        
        markAsReceived(currentUser, request);
        
        Map<String, Object> vars = new HashMap<String, Object>(0);
        vars.put("ad", ad);
        vars.put("user", request.getUser());
        vars.put("request", request);
        
        emailSender.sendNotification(NotificationType.REQUEST_RECEIVED, ad.getCreator(), vars);
    }
    
    
    
    //*****************
    //* request issue *
    //*****************
    
    @Override
    @Transactional
    public void addRequestIssue(Long requestId, String text) throws UserNotFoundException, RequestNotFoundException {
        Request request = validateRequest(requestId);
        User currentUser = getCurrentUser();
        boolean sent = false;
        
        Issue issue = new Issue();
        issue.setFrom(currentUser);
        issue.setRequest(request);
        issue.setText(text);
        
        if ( useEmailSender ) {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("issue", issue);
            vars.put("from", currentUser);
            vars.put("request", request);
            vars.put("ad", request.getAd());

            if ( emailSender.sendNotification(NotificationType.ISSUE_NEW, emailConfig.getIssueEmailAddress(), vars) ) {
                sent = true;
            }
        }
        
        if ( sent ) {
            issue.setEmailSent(sent);
            issue.setEmailSentAt(new Date());
        }
        issueDao.save(issue);
    }
    
    
    
    //************
    //* shipping *
    //************
    
    @Override
    @Transactional
    public List<ShippingBoxDto> getShippingBoxes() throws UserNotFoundException {
        List<ShippingBox> boxes = shippingBoxDao.getAllShippingBoxes();
        List<ShippingBoxDto> result = new LinkedList<ShippingBoxDto>();
        if ( boxes != null && !boxes.isEmpty() ) {
            for ( ShippingBox box : boxes ) {
                result.add(new ShippingBoxDto(box));
            }
        }
        return result;
    }
    
    
    
    //***************
    //* ad bookmark *
    //***************

    @Override
    @Transactional
    public Long bookmarkAd(Long adId) throws UserNotFoundException, AdNotFoundException, GeneralException {
        User currentUser = getCurrentUser();
        Bookmark bookmark = bookmarkDao.get(currentUser.getId(), adId);
        Ad ad = validateAd(adId);
        
        if ( ad.getCreator().equals(currentUser) ) {
            throw new GeneralException("Cannot bookmark owned ads.");
        }
        
        if (bookmark == null) {
            bookmark = new Bookmark(currentUser, ad);
            bookmarkDao.save(bookmark);
        }

        return bookmark.getId();
    }

    @Override
    @Transactional
    public void removeBookmark(Long adId) throws UserNotFoundException, BookmarkNotFoundException {
        User currentUser = getCurrentUser();
        Bookmark bookmark = bookmarkDao.get(currentUser.getId(), adId);

        if (bookmark == null) {
            throw new BookmarkNotFoundException(adId);
        }

        bookmarkDao.delete(bookmark);
    }
    
    @Override
    @Transactional
    public List<AdDto> getBookmarkedAds(Boolean includeRequests) {
        try {
            User currentUser = getCurrentUser();
            return getBookmarkedAds(currentUser.getId(), includeRequests);
        } catch ( UserNotFoundException ex ) {
            logger.error("User (current/logged) not found", ex);
            return new ArrayList<AdDto>();
        }
    }
    
    @Override
    @Transactional
    public List<AdDto> getBookmarkedAds(Long userId, Boolean includeRequests) throws UserNotFoundException {
        User currentUser = getCurrentUser();
        User user = validateUser(userId);
        List<Ad> bookmarkedAds = bookmarkDao.getBookmarkedAds(user);
        List<AdDto> result = new LinkedList<AdDto>();

        if ( includeRequests == null ) {
            includeRequests = false;
        }
        
        if ( bookmarkedAds != null && !bookmarkedAds.isEmpty() ) {
            for (Ad ad : bookmarkedAds) {
                getUserData(ad.getCreator()); //this sets the correct user data
                
                AdDto adDto = new AdDtoBuilder(ad, includeRequests ? getAdData(ad) : null, getUserData(currentUser))
                        .setCurrentUser(currentUser)
                        .includeCreator(true) //TODO: maybe this is not needed
                        .includeRequests(includeRequests)
                        .includeCanRequest()
                        .includeCanRate()
                        .build();
                adDto.setInBookmarks(true);
                adDto.setRequested(ad.isRequested(user));
                
                if ( includeRequests ) {
                    setNumUnreadMessages(adDto, currentUser.getId());
                }
                
                result.add(adDto);
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public int getBookmarkedAdsSize(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Long> bookmarkedAdIds = bookmarkDao.getBookmarkedAdIds(user);
        return bookmarkedAdIds != null ? bookmarkedAdIds.size() : 0;
    }

    
    
    //****************
    //* ad spam mark *
    //****************
    
    @Override
    @Transactional
    public void markAsSpam(Long adId) throws UserNotFoundException, AdNotFoundException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();
        
        // Don't allow to mark twice as the same user
        for (SpamMark mark : ad.getSpamMarks()) {
            if (mark.getWitness().equals(currentUser)) {
                return;
            }
        }

        if (ad.getSpamMarks().size() + 1 >= Constants.SPAMMARK_MAX_ALLOWED) {
            ad.markAsSpam();
            //ad.markAsDeleted();
        }

        SpamMark newMark = new SpamMark(ad, currentUser);
        spamMarkDao.save(newMark);
    }

    @Override
    @Transactional
    public void unmarkAsSpam(Long adId) throws UserNotFoundException, AdNotFoundException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();
        SpamMark markToRemove = null;

        for (SpamMark mark : ad.getSpamMarks()) {
            if (mark.getWitness().equals(currentUser)) {
                markToRemove = mark;
                break;
            }
        }

        if (markToRemove != null) {
            ad.getSpamMarks().remove(markToRemove);

            // restore ad
            if (ad.getSpamMarks().size() < Constants.SPAMMARK_MAX_ALLOWED) {
                //ad.unmarkAsDeleted();
                ad.unmarkAsSpam();
            }

            spamMarkDao.delete(markToRemove);
        }
    }
    
    
    
    //*************
    //* ad rating *
    //*************
    
    @Override
    @Transactional
    public float rateAd(RatingDto ratingDto) throws RequestNotFoundException, UserNotFoundException, InvalidRateOperationException, AlreadyRatedException, InvalidAdStateException {
        Long requestId = ratingDto.getRequestId();
        Request request = validateRequest(requestId);
        Ad ad = request.getAd();
        Long adId = ad.getId();
        User owner = ad.getCreator();
        User receiver = request.getUser();
        User from = getCurrentUser();
        User to = validateUser(ratingDto.getToUserId());
        String text = ratingDto.getText();
        int ratingValue = ratingDto.getValue();
        
        if (ratingValue != -1 && ratingValue != 0 && ratingValue != 1) {
            throw new InvalidRateOperationException("Only '1' or '-1' can be used as rating values!");
        }
        if ( from.equals(to) ) {
            throw new InvalidRateOperationException("You cannot rate your request for yourself.");
        }
        if ( !owner.equals(from) && !owner.equals(to) ) {
            throw new InvalidRateOperationException("Invalid owner user to rate.");
        }
        if ( !receiver.equals(from) && !receiver.equals(to) ) {
            throw new InvalidRateOperationException("Invalid receiver user to rate.");
        }
        if ( request.isAlreadyRated(from) ) {
            throw new AlreadyRatedException("You've already rated this request!");
        }
        if ( !request.isAccepted() ) {
            throw new InvalidRateOperationException("The request is not selected (requestId: " + requestId + ", adId: " + adId + ")");
        }
        if ( !request.isSent() && !request.isReceived() ) {
            throw new InvalidRateOperationException("The request is not sent nor received (requestId: " + requestId + ", adId: " + adId + ")");
        }

        Rating rating = new Rating(request, from, to, text, ratingValue);
        ratingDao.save(rating);
        
        request.getRatings().add(rating);

        // recalculate ad rating
        float totalRating = 0.0f;
        for (Rating r : request.getRatings()) {
            totalRating += r.getValue();
        }

        ad.setRating(totalRating);
        return totalRating;
    }
    
    @Override
    @Transactional
    public List<RatingDto> getReceivedRatings(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getReceivedForUser(user.getId());
        return getRatings(ratings);
    }
    
    @Override
    @Transactional
    public List<RatingDto> getSentRatings(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getSentByUser(user.getId());
        return getRatings(ratings);
    }
    
    @Override
    @Transactional
    public List<RatingDto> getReceivedRatingsAsOwner(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getReceivedRatingsAsOwner(user.getId());
        return getRatings(ratings);
    }
    
    @Override
    @Transactional
    public List<RatingDto> getReceivedRatingsAsReceiver(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getReceivedRatingsAsReceiver(user.getId());
        return getRatings(ratings);
    }
    
    @Override
    @Transactional
    public List<RatingDto> getAllReceivedRatings(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getAllReceivedRatings(user.getId());
        return getRatings(ratings);
    }
    
    @Override
    @Transactional
    public int getAllReceivedRatingsSize(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getAllReceivedRatings(user.getId());
        return ratings != null ? ratings.size() : 0;
    }
    
    @Override
    @Transactional
    public int getAllSentRatingsSize(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getAllSentRatings(user.getId());
        return ratings != null ? ratings.size() : 0;
    }
    
    
    
    // internal helpers
    
    @Transactional
    private Long placeAd(AdDto adDto, Ad clonedFrom) throws UserNotFoundException, AdValidationException {
        // ++ TODO: create ad validator
        if (adDto.getTitle() == null) {
            throw new AdValidationException(AdField.TITLE, "Title not specified!");
        }
        
        ShippingBox shippingBox = getShipingBox(adDto.getShippingBox());
        PromoCodeProvider promoCodeProvider = getPromoCodeProvider(adDto.getPromoCodeProvider());
        Category category = validateCategory(adDto.getCategoryId());
        Image image = saveImage(adDto.getImage(), AdField.IMAGE); //main image
        Image thumbImage = saveImage(adDto.getImageThumbnail(), AdField.THUMB_IMAGE); //thumb image
        Image barcodeImage = saveImage(adDto.getImageBarcode(), AdField.BARCODE_IMAGE); //barcode image
        // ++
        
        if ( shippingBox == null && (adDto.getPickUp() == null || adDto.getPickUp() == false) ) {
            throw new AdValidationException(AdField.DELIVERY, "Pickup or shipping should be defined!");
        }
        
        User currentUser = getCurrentUser();
        boolean expires = adDto.getExpires() != null ? adDto.getExpires() : true;
        Date expiresAt = adDto.getExpiresAt() != null ? adDto.getExpiresAt() : calculateExpiration();
        Date availableAt = adDto.getAvailableAt() != null ? adDto.getAvailableAt() : new Date();
        
        Ad ad = new Ad(currentUser.isBusinessAccount() ? AdType.BUSINESS : AdType.MEMBER, appConfig.getAdMaxAllowedProlongations());
        ad.setClonedFrom(clonedFrom);
        ad.initRevision();
        adDto.updateAd(ad);
        
        ad.getAdData().setShippingBox(shippingBox);
        ad.getAdData().setCategory(category);
        ad.getAdData().setMainImage(image);
        ad.getAdData().setThumbImage(thumbImage);
        
        if ( ad.isBusinessAd() ) {
            ((BusinessAdData) ad.getAdData()).setPromoCodeProvider(promoCodeProvider);
            ((BusinessAdData) ad.getAdData()).setBarcodeImage(barcodeImage);
        }
        
        adDataDao.save(ad.getAdData());
        
        ad.unmarkAsApproved();
        ad.unmarkAsOnline();
        ad.setStatus(AdStatus.OFFLINE);
        ad.setCreator(currentUser);
        ad.setExpires(expires);
        ad.setExpired(false);
        ad.setExpiresAt(expiresAt);
        ad.setAvailableAt(availableAt);
        Long adId = adDao.save(ad);
        
        UserTransaction adTransaction = new UserTransaction(ad);
        adTransaction.setUser(currentUser);
        adTransaction.setUserPoint(currentUser.getUserPoint());
        userTransactionDao.save(adTransaction);
        
        if ( useEmailSender ) {
            List<User> admins = userDao.getAdminUsers();
            if ( admins != null ) {
                for ( User admin : admins ) {
                    Map<String, Object> vars = new HashMap<String, Object>(0);
                    vars.put("ad", ad);
                    
                    emailSender.sendNotification(NotificationType.AD_NEW, admin, vars);
                }
            }
        }
        
        return adId;
    }
    
    
    
    private void getAds(List<AdDto> result, User currentUser, int lastIndex, int numberAds, FilterDto filter, Boolean includeImages, Boolean includeCreator, int includeCommentsNumber, Boolean includeCreatorStatistics) {
        List<Ad> ads = adDao.get(lastIndex, numberAds, filter);
        List<Long> bookmarkedAdIds = bookmarkDao.getBookmarkedAdIds(currentUser);
        FilterType filterType = filter != null && filter.getFilterType() != null ? filter.getFilterType() : FilterType.ACTIVE;
        
        if ( includeImages == null ) {
            includeImages = false;
        }
        if ( includeCreator == null ) {
            includeCreator = false;
        }
        if ( includeCreatorStatistics == null ) {
            includeCreatorStatistics = false;
        }
        
        for (Ad ad : ads) {
            if ( ad.isExpired() || ad.getStatus() == AdStatus.EXPIRED ) {
                continue;
            }
            
            boolean include = false;
            if ( filterType == FilterType.ACTIVE ) {
                boolean isOwner = ad.getCreator().equals(currentUser);
                boolean canRequest = ad.canRequest();
                boolean isRequested = ad.isRequested(currentUser);
                boolean hasAccepted = !ad.getAcceptedRequests().isEmpty();
                
                if ( canRequest || (!canRequest && isRequested && !hasAccepted) ) {
                    include = true;
                } else if ( isOwner && canRequest ) {
                    include = true;
                }
            } else if ( filterType == FilterType.GIFTED ) {
                boolean isSold = ad.isSold();
                boolean hasShipped = !ad.getShippedRequests(true, true).isEmpty();
                
                if ( isSold || hasShipped ) {
                    include = true;
                }
            }
            
            if ( !include ) {
                continue;
            }
            
            List<Comment> comments = null;
            if ( includeCommentsNumber > 0 ) {
                comments = commentDao.getAdComments(ad.getId(), -1L, includeCommentsNumber);
            }
            
            if ( includeCreator ) {
                getUserData(ad.getCreator()); //this sets the correct user data
            }
            
            AdDto adDto = new AdDtoBuilder(ad, getAdData(ad), getUserData(currentUser))
                    .setCurrentUser(currentUser)
                    .setFilteredComments(comments)
                    .includeImages(includeImages)
                    .includeCreator(includeCreator)
                    .includeCanRequest()
                    .includeCanRate()
                    .build();
            adDto.setInBookmarks(bookmarkedAdIds.contains(ad.getId()));
            adDto.setRequested(ad.isRequested(currentUser));
            adDto.setLastIndex(lastIndex + numberAds);
            
            if ( includeCreator && includeCreatorStatistics ) {
                //trying to use the already loaded user statistics
                for ( AdDto adDto_ : result ) {
                    if ( adDto_.getCreator() == null ) {
                        continue;
                    } else if ( adDto_.getCreator().getStatistics() == null ) {
                        continue;
                    } else if ( !adDto_.getCreator().getId().equals(adDto.getCreator().getId()) ) {
                        continue;
                    }
                    adDto.getCreator().setStatistics(adDto_.getCreator().getStatistics());
                    break;
                }
                
                if ( adDto.getCreator().getStatistics() == null ) {
                    try {
                        adDto.getCreator().setStatistics(userManagementService.getStatistics(ad.getCreator().getId()));
                    } catch ( UserNotFoundException ex ) {
                        logger.error("User (userId: " + ad.getCreator().getId() + ") not found when trying to build its statistics");
                    }
                }
            }
            
            result.add(adDto);
        }
        
        if ( ads.size() > 0 && result.size() < numberAds ) {
            int lastIndex_ = lastIndex + numberAds;
            int numberAds_ = numberAds - result.size();
            
            getAds(
                    result,
                    currentUser,
                    lastIndex_,
                    numberAds_,
                    filter,
                    includeImages,
                    includeCreator,
                    includeCommentsNumber,
                    includeCreatorStatistics
                    );
        }
    }
    
    
    
    private Image validateImage(Long imageId) throws ImageNotFoundException {
        Image image = null;
        try {
            image = imageDao.get(imageId, ImageModelType.AD);
        } catch ( IOException ex ) {
            logger.error("Could not load image (imageId: " + imageId + ")", ex);
            throw new ImageNotFoundException(imageId);
        }
        if (image == null) {
            throw new ImageNotFoundException(imageId);
        }
        return image;
    }
    
    private Category validateCategory(Long categoryId) throws AdValidationException {
        if (categoryId == null) {
            throw new AdValidationException(AdField.CATEGORY_ID, "Category id not specified!");
        }

        Category category = categoryDao.get(categoryId);
        if (category == null) {
            throw new AdValidationException(AdField.CATEGORY_ID, "Category with id = " + categoryId
                    + " not found!");
        }
        return category;
    }
    
    private ShippingBox getShipingBox(ShippingBoxDto shippingBoxDto) {
        if ( shippingBoxDto == null || shippingBoxDto.getId() == null ) {
            return null;
        }
        
        ShippingBox shippingBox = shippingBoxDao.get(shippingBoxDto.getId());
        return shippingBox;
    }
    
    private PromoCodeProvider getPromoCodeProvider(PromoCodeProviderDto promoCodeProviderDto) {
        if ( promoCodeProviderDto == null || promoCodeProviderDto.getId() == null ) {
            return null;
        }
        
        PromoCodeProvider promoCodeProvider = promoCodeProviderDao.get(promoCodeProviderDto.getId());
        return promoCodeProvider;
    }
    
    private Image saveImage(ImageDto imageDto, AdField field) throws AdValidationException {
        if (imageDto != null) {
            if ( !imageDto.isValid() ) {
                throw new AdValidationException(field, "Invalid image specified!");
            }
            
            try {
                Image image = imageDto.toImage();
                imageDao.save(image, ImageModelType.AD);
                return image;
            } catch ( IOException ex ) {
                logger.error("Exception when saving image", ex);
                throw new AdValidationException(field, "Could not save image!");
            }
        }
        return null;
    }
    
    private void updateImage(Ad ad, AdDto adDto, AdField field) throws AdValidationException {
        ImageDto imageDto;
        Image image;
        
        if ( field == AdField.IMAGE ) {
            imageDto = adDto.getImage();
            image = ad.getAdData().getMainImage();
        } else if ( field == AdField.THUMB_IMAGE ) {
            imageDto = adDto.getImageThumbnail();
            image = ad.getAdData().getThumbImage();
        } else if ( ad.isBusinessAd() && field == AdField.BARCODE_IMAGE ) {
            BusinessAdData adData = (BusinessAdData) getAdData(ad);
            imageDto = adDto.getImageBarcode();
            image = adData.getBarcodeImage();
        } else {
            return;
        }
        
        if ( imageDto == null ) {
            return;
        } else if ( !imageDto.isValid() ) {
            if ( imageDto.getUrl() == null || imageDto.getUrl().trim().isEmpty() ) {
                throw new AdValidationException(field, "Invalid image specified!");
            }
            return;
        }
        
        try {
            if ( image != null ) {
                if ( field == AdField.IMAGE ) {
                    ad.getAdData().setMainImage(null);
                } else if ( field == AdField.THUMB_IMAGE ) {
                    ad.getAdData().setThumbImage(null);
                } else if ( field == AdField.BARCODE_IMAGE ) {
                    BusinessAdData adData = (BusinessAdData) getAdData(ad);
                    adData.setBarcodeImage(null);
                }
                imageDao.delete(image, ImageModelType.AD);
            }

            image = saveImage(imageDto, field);
            
            if ( field == AdField.IMAGE ) {
                ad.getAdData().setMainImage(image);
            } else if ( field == AdField.THUMB_IMAGE ) {
                ad.getAdData().setThumbImage(image);
            } else if ( field == AdField.BARCODE_IMAGE ) {
                BusinessAdData adData = (BusinessAdData) getAdData(ad);
                adData.setBarcodeImage(image);
            }
        } catch ( Exception ex ) {
            logger.error("Exception when saving image (field: " + field + ")", ex);
        }
    }
    
    
    
    private List<RatingDto> getRatings(List<Rating> ratings) {
        List<RatingDto> result = new LinkedList<RatingDto>();
        if ( ratings != null && !ratings.isEmpty() ) {
            for ( Rating rating : ratings ) {
                getUserData(rating.getFrom()); //this sets the correct user data
                getUserData(rating.getTo()); //this sets the correct user data
                
                result.add(new RatingDto(rating));
            }
        }
        return result;
    }
    
    private List<CategoryDto> getCategories(Long categoryId, boolean includeSubcategories) {
        List<CategoryDto> result = new LinkedList<CategoryDto>();
        List<Category> categories = categoryDao.getSubcategories(categoryId);

        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto(category, includeSubcategories);
            result.add(categoryDto);
        }

        return result;
    }
    
    private boolean inBookmarks(User user, Ad ad) {
        Bookmark bookmark = bookmarkDao.get(user.getId(), ad.getId());
        if (bookmark != null) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns all the visible requests for the specified user. The 2nd
     * parameter should be used to explicitly set the active status. If is set
     * to false only visible (not hidden and not deleted) requests will be included.
     * 
     * @param user
     * @return 
     */
    private List<Request> getActiveRequestsByUser(Long userId, boolean includeOnlyActiveRequests) {
        List<Request> requests = requestDao.getByUser(userId);
        List<Request> result = new LinkedList<Request>();
        
        if ( requests == null || requests.isEmpty() ) {
            return result;
        }
        
        for ( Request request : requests ) {
            if ( includeOnlyActiveRequests ) {
                if ( request.isActive() ) {
                    result.add(request);
                }
            } else {
                if ( request.isVisible() ) {
                    result.add(request);
                }
            }
        }
        return result;
    }
    
    /**
     * Returns all visible (not hidden and not deleted) requests that are
     * not having CANCELED state for the given user.
     * 
     * @param userId
     * @return
     */
    private List<Ad> getUserRequestedAds(Long userId) {
        List<Request> requests = getActiveRequestsByUser(userId, false);
        List<Ad> result = new LinkedList<Ad>();
        if ( requests == null || requests.isEmpty() ) {
            return result;
        }
        
        for ( Request request : requests ) {
            if ( request.isCanceled() ) {
                //CANCELED requests are not used
                continue;
            }
            
            Ad ad = request.getAd();
            if ( !result.contains(ad) ) {
                result.add(ad);
            }
        }
        return result;
    }
    
    /**
     * Returns all ads for the given user.
     * 
     * @param userId
     * @param includeUnapproved
     * @return 
     */
    private List<Ad> getUserOwnedAds(Long userId, int numberAds, boolean includeUnapproved) {
        List<Ad> ads = adDao.getByUser(userId, numberAds);
        List<Ad> result = new LinkedList<Ad>();
        if ( ads == null || ads.isEmpty() ) {
            return result;
        }
        
        for (Ad ad : ads) {
            if ( !includeUnapproved && (!ad.isApproved() || !ad.isOnline()) ) {
                //ad is not approved or is not marked as online
                continue;
            }

            if ( !result.contains(ad) ) {
                result.add(ad);
            }
        }
        return result;
    }
    
    
    
    private void selectRequest(Request request, User selector) throws InvalidRequestException, InvalidAdStateException {
        Ad ad = request.getAd();
        AdData adData = ad.getAdData();
        User creator = ad.getCreator();
        User receiver = request.getUser();
        
        if ( ad.getStatus() == AdStatus.IN_PROGRESS ) {
            //continue
        } else {
            //ad must be IN_PROGRESS to allow request selection
            throw new InvalidAdStateException("Ad can't be selected as its state (" + ad.getStatus() + ") is not as expected!");
        }
        if ( !creator.equals(selector) ) {
            throw new InvalidRequestException("Only owner can select a request!");
        }
        if ( request.isAccepted()) {
            throw new InvalidRequestException("Request already selected");
        }
        if ( ad.getAdData().getQuantity() <= 0 ) {
            throw new InvalidRequestException("No more available.");
        }
        
        ad.select(request);
        //ad.setExpiresAt(new Date()); //reset (???) the expiration date
        
        
        boolean shipping = (adData.getShippingBox() != null) ? true : false;
        boolean pickup = (adData.getPickUp() != null && adData.getPickUp() == true) ? true : false;
        boolean both = shipping && pickup;
        
        String subtype;
        if ( both ) {
            subtype = NotificationType.SUBTYPE_REQUEST_ACCEPTED_BOTH;
        } else if ( pickup ) {
            subtype = NotificationType.SUBTYPE_REQUEST_ACCEPTED_PICKUP;
        } else if ( shipping ) {
            subtype = NotificationType.SUBTYPE_REQUEST_ACCEPTED_SHIPPING;
        } else {
            logger.warn("Notification type cannot be detected at request select (requestId: " + request.getId() + ") - using BOTH");
            subtype = NotificationType.SUBTYPE_REQUEST_ACCEPTED_BOTH;
        }
        
        
        if ( shipping ) {
            Shipping ship = new Shipping();
            ship.setRequest(request);
            shippingDao.save(ship);
        }
        
        
        if ( UserPoint.canUpdateRequestLimit(ad) ) {
            UserPoint userPoint = receiver.getUserPoint();
            userPoint.incrementRequestLimit(appConfig.getRequestLimitRequestAccepted());
            userPointDao.update(userPoint);
        }
        
        
        if ( ad.isMemberAd() ) {
            Map<String, Object> vars = new HashMap<String, Object>(0);
            vars.put("user", receiver);
            vars.put("request", request);
            vars.put("creator", creator);
            vars.put("ad", ad);
            vars.put("shippingBox", adData.getShippingBox());
            
            emailSender.sendNotification(NotificationType.REQUEST_ACCEPTED, subtype, receiver, vars);
        }
    }
    
    private void markAsSent(User user, Request request, boolean finalizeTransaction) throws InvalidRequestException {
        Ad ad = request.getAd();
        Long adId = ad.getId();
        UserTransaction adTransaction = userTransactionDao.getByAd(adId);
        
        if ( !ad.getCreator().equals(user) ) {
            throw new InvalidRequestException("Only creator users can mark as sent.");
        }
        if ( !request.isAccepted() ) {
            throw new InvalidRequestException("The request (requestId: " + request.getId() + ") is not accepted, cannot mark as sent.");
        }
        if ( adTransaction == null ) {
            throw new InvalidRequestException("No associated transaction for the ad (adId: " + adId + ")");
        }
        if ( request.getStatus() != RequestStatus.ACCEPTED && request.getStatus() != RequestStatus.SENT ) {
            throw new InvalidRequestException("The request (requestId: " + request.getId() + ") is not in accepted state, cannot mark as sent.");
        }
        
        request.markAsSent();
        
        if ( finalizeTransaction && !user.isBusinessAccount() ) {
            updateUserPoint(user, adTransaction, ad);
            
            adTransaction.markAsFinalized(TransactionStatus.SENT);
            userTransactionDao.update(adTransaction);
        }
    }
    
    private void markAsReceived(User user, Request request) throws InvalidRequestException {
        Ad ad = request.getAd();
        Long requestId = request.getId();
        UserTransaction requestTransaction = userTransactionDao.getByRequest(requestId);
        
        if ( !request.getUser().equals(user) ) {
            throw new InvalidRequestException("Only requestor can mark as received.");
        }
        if ( !request.isAccepted()) {
            throw new InvalidRequestException("Cannot mark request (requestId: " + requestId + ") as received as it is not selected/accepted.");
        }
        if ( requestTransaction == null ) {
            throw new InvalidRequestException("No associated transaction for the request (requestId: " + requestId + ")");
        }
        
        User owner = ad.getCreator();
        
        if ( request.getStatus() == RequestStatus.ACCEPTED || request.getStatus() == RequestStatus.SENT ) {
            //received can be mark without passing through SENT status as well
            markAsSent(owner, request, true);
        }
        
        if ( !request.isSent() ) {
            throw new InvalidRequestException("Cannot mark request (requestId: " + requestId + ") as received as it was not yet sent.");
        }
        
        request.markAsReceived();
        
        if ( ad.getAdData().getQuantity() == 0 ) {
            //ending ad if there are no more items
            ad.markAsSold();
            //ad.setExpired(true);
        }
        
        updateUserPoint(user, requestTransaction, ad);
        
        requestTransaction.markAsFinalized(TransactionStatus.RECEIVED);
        userTransactionDao.update(requestTransaction);
        
        if ( !owner.isBusinessAccount() && UserPoint.canUpdateRequestLimit(ad) ) {
            UserPoint ownerUserPoint = owner.getUserPoint();
            ownerUserPoint.incrementRequestLimit(appConfig.getRequestLimitRequestReceived());
            userPointDao.update(ownerUserPoint);
        }
    }
    
    private void updateUserPoint(User user, UserTransaction transaction, Ad ad) {
        UserPoint userPoint = user.getUserPoint();
        userPoint.addPendingGivingNumber(transaction);
        if ( ad.isMemberAd() ) {
            userPoint.addPendingMemberReceivingNumber(transaction);
        } else if ( ad.isBusinessAd() ) {
            userPoint.addPendingBusinessReceivingNumber(transaction);
        }
        userPointDao.update(userPoint);
    }
    
    private Date calculateExpiration() {
        Date start = new Date();
        Date expiresAt = DateUtils.addMinutes(start, appConfig.getAdExpirationPeriodMinutes());
        return expiresAt;
    }
    
    private void setNumUnreadMessages(AdDto adDto, Long userId) {
        for ( RequestDto request : adDto.getRequests() ) {
            int numUnreadMessages = messageDao.getNumUnreadMessagesByRequestAndUser(request.getId(), userId);
            request.setNumUnreadMessages(numUnreadMessages);
        }
    }

    private void setApproval(AdDto adDto) {
        Approval approval = approvalDao.getByAdAndRevision(adDto.getId(), adDto.getRevision());
        if ( approval != null ) {
            ApprovalDto approvalDto = new ApprovalDto(approval);
            adDto.setApproval(approvalDto);
        }
    }
}
