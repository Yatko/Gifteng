package com.venefica.service;

import com.venefica.config.Constants;
import com.venefica.dao.AdDataDao;
import com.venefica.dao.BookmarkDao;
import com.venefica.dao.CategoryDao;
import com.venefica.dao.CommentDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.RatingDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.SpamMarkDao;
import com.venefica.dao.ViewerDao;
import com.venefica.model.Ad;
import com.venefica.model.AdStatus;
import com.venefica.model.AdType;
import com.venefica.model.Bookmark;
import com.venefica.model.BusinessAdData;
import com.venefica.model.Category;
import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.Rating;
import com.venefica.model.Request;
import com.venefica.model.RequestStatus;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.model.Viewer;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.AdStatisticsDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.RatingDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdField;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.ImageField;
import com.venefica.service.fault.ImageNotFoundException;
import com.venefica.service.fault.ImageValidationException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRateOperationException;
import com.venefica.service.fault.InvalidRequestException;
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adService")
@WebService(endpointInterface = "com.venefica.service.AdService")
public class AdServiceImpl extends AbstractService implements AdService {

    @Inject
    private AdDataDao adDataDao;
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
    private ViewerDao viewerDao;
    @Inject
    private RequestDao requestDao;

    
    
    //**********************
    //* categories related *
    //**********************
    
    @Override
    public List<CategoryDto> getCategories(Long categoryId) {
        return getCategoriesInternal(categoryId, false);
    }

    @Override
    @Transactional
    public List<CategoryDto> getAllCategories() {
        return getCategoriesInternal(null, true);
    }
    
    

    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
    @Override
    @Transactional
    public Long placeAd(AdDto adDto) throws AdValidationException {
        User currentUser = getCurrentUser();

        // ++ TODO: create ad validator
        if (adDto.getTitle() == null) {
            throw new AdValidationException(AdField.TITLE, "Title not specified!");
        }
        
        Category category = validateCategory(adDto.getCategoryId());
        Image image = saveImage(adDto.getImage(), AdField.IMAGE); //main image
        Image thumbImage = saveImage(adDto.getImageThumbnail(), AdField.THUMB_IMAGE); //thumb image
        Image barcodeImage = saveImage(adDto.getImageBarcode(), AdField.BARCODE_IMAGE); //barcode image
        // ++
        
        boolean expires = adDto.isExpires() != null ? adDto.isExpires() : true;
        Date expiresAt = adDto.getExpiresAt() != null ? adDto.getExpiresAt() : DateUtils.addDays(new Date(), Constants.AD_EXPIRATION_PERIOD_DAYS);
        Date availableAt = adDto.getAvailableAt() != null ? adDto.getAvailableAt() : new Date();
        
        Ad ad = new Ad(currentUser.isBusinessAccount() ? AdType.BUSINESS : AdType.MEMBER);
        adDto.update(ad);
        
        ad.getAdData().setCategory(category);
        ad.getAdData().setMainImage(image);
        ad.getAdData().setThumbImage(thumbImage);
        
        if ( ad.getType() == AdType.BUSINESS ) {
            ((BusinessAdData) ad.getAdData()).setBarcodeImage(barcodeImage);
        }
        
        adDataDao.save(ad.getAdData());
        
        ad.setStatus(AdStatus.ACTIVE);
        ad.setSent(false);
        ad.setReceived(false);
        ad.setCreator(currentUser);
        ad.setExpires(expires);
        ad.setExpired(false);
        ad.setExpiresAt(expiresAt);
        ad.setAvailableAt(availableAt);
        return adDao.save(ad);
    }
    
    @Override
    @Transactional
    public void updateAd(AdDto adDto) throws AdNotFoundException, AdValidationException, AuthorizationException {
        Ad ad = validateAd(adDto.getId());
        
        // ++ TODO: create ad validator
        if (adDto.getTitle() == null) {
            throw new AdValidationException(AdField.TITLE, "Title is null!");
        }

        Category category = validateCategory(adDto.getCategoryId());
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("You can update only your own ads!");
        }

        // WARNING: This update must be performed within an active transaction!
        adDto.update(ad);

        ad.getAdData().setCategory(category);

        if (adDto.getImage() != null) {
            ImageDto imageDto = adDto.getImage();
            if (imageDto.isValid()) {
                Image image = ad.getAdData().getMainImage();

                if (image != null) {
                    ad.getAdData().setMainImage(null);
                    imageDao.delete(image);
                }

                image = imageDto.toImage();
                imageDao.save(image);
                ad.getAdData().setMainImage(image);
            } else {
                throw new AdValidationException(AdField.IMAGE, "Invalid image specified!");
            }
        }

        if (adDto.getImageThumbnail() != null) {
            ImageDto thumbImageDto = adDto.getImageThumbnail();
            if (thumbImageDto.isValid()) {
                Image thumbImage = ad.getAdData().getThumbImage();

                if (thumbImage != null) {
                    ad.getAdData().setThumbImage(null);
                    imageDao.delete(thumbImage);
                }

                thumbImage = thumbImageDto.toImage();
                imageDao.save(thumbImage);
                ad.getAdData().setThumbImage(thumbImage);
            } else {
                throw new AdValidationException(AdField.THUMB_IMAGE, "Invalid image specified!");
            }
        }
        
        if (ad.getType() == AdType.BUSINESS && adDto.getImageBarcode() != null) {
            ImageDto barcodeImageDto = adDto.getImageBarcode();
            if (barcodeImageDto.isValid()) {
                Image barcodeImage = ((BusinessAdData) ad.getAdData()).getBarcodeImage();

                if (barcodeImage != null) {
                    ad.getAdData().setThumbImage(null);
                    imageDao.delete(barcodeImage);
                }

                barcodeImage = barcodeImageDto.toImage();
                imageDao.save(barcodeImage);
                ((BusinessAdData) ad.getAdData()).setBarcodeImage(barcodeImage);
            } else {
                throw new AdValidationException(AdField.BARCODE_IMAGE, "Invalid image specified!");
            }
        }
        
        adDataDao.update(ad.getAdData());

//        if (adDto.getExpiresAt() != null && currentUser.isBusinessAcc()) {
//                ad.setExpiresAt(adDto.getExpiresAt());
//                ad.setExpired(false);
//        }

        // ++
    }
    
    @Override
    @Transactional
    public void deleteAd(Long adId) throws AdNotFoundException, AuthorizationException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can delete the ad");
        }

        if (!ad.isDeleted()) {
            ad.markAsDeleted();
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
        imageDao.save(image);
        ad.addImage(image);

        return image.getId();
    }

    @Override
    @Transactional
    public void deleteImageFromAd(Long adId, Long imageId) throws AdNotFoundException, AuthorizationException, ImageNotFoundException {
        deleteImagesFromAd(adId, Arrays.<Long>asList(imageId));
    }
    
    @Override
    @Transactional
    public void deleteImagesFromAd(Long adId, List<Long> imageIds) throws AdNotFoundException, AuthorizationException, ImageNotFoundException {
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
            
            ad.removeImage(image);
            imageDao.delete(image);
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

                ad.removeImage(image);
                imageDao.delete(image);
            }
            adDataDao.update(ad.getAdData());
        }
    }

    
    
    //*************************
    //* ads listing/filtering *
    //*************************
    
    @Override
    @Transactional
    public List<AdDto> getAds(Long lastAdId, int numberAds) {
        return getAds(lastAdId, numberAds, null);
    }

    @Override
    @Transactional
    public List<AdDto> getAds(Long lastAdId, int numberAds, FilterDto filter) {
        return getAds(lastAdId, numberAds, filter, false, false, 0);
    }
    
    @Override
    @Transactional
    public List<AdDto> getAds(Long lastAdId, int numberAds, FilterDto filter, Boolean includeImages, Boolean includeCreator, int includeCommentsNumber) {
        List<AdDto> result = new LinkedList<AdDto>();
        List<Ad> ads = adDao.get(lastAdId, numberAds, filter);

        User currentUser = getCurrentUser();

        // TODO: Optimize this
        // Get current user's bookmarks
        // TODO: REMOVE IT!!!!!!!!
        List<Ad> bokmarkedAds = bookmarkDao.getBookmarkedAds(currentUser);

        for (Ad ad : ads) {
            if ( !includeOwnedAd(ad, currentUser, filter) ) {
                //skipping owned ads
                continue;
            }
            
            List<Comment> comments = null;
            if ( includeCommentsNumber > 0 ) {
                comments = commentDao.getAdComments(ad.getId(), -1L, includeCommentsNumber);
            }
            
            AdDto adDto = new AdDtoBuilder(ad)
                    .setCurrentUser(currentUser)
                    .setFilteredComments(comments)
                    .includeImages(includeImages != null ? includeImages : false)
                    .includeCreator(includeCreator != null ? includeCreator : false)
                    .build();
            adDto.setInBookmars(inBookmarks(bokmarkedAds, ad));
            adDto.setRequested(requested(currentUser, ad));
            
            result.add(adDto);
        }

        return result;
    }

    @Override
    @Transactional
    public List<AdDto> getMyAds(Boolean includeRequests) {
        try {
            Long userId = getCurrentUserId();
            return getUserAds(userId, includeRequests);
        } catch ( UserNotFoundException ex ) {
            logger.error("User (current/logged) not found", ex);
            return new ArrayList<AdDto>();
        }
    }
    
    @Override
    @Transactional
    public List<AdDto> getUserAds(Long userId, Boolean includeRequests) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Ad> ads = adDao.getByUser(userId);
        List<AdDto> result = new LinkedList<AdDto>();
        
        for (Ad ad : ads) {
            AdDto adDto = new AdDtoBuilder(ad)
                    .setCurrentUser(user)
                    .includeRequests(includeRequests != null ? includeRequests : false)
                    .build();
            result.add(adDto);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<AdDto> getUserRequestedAds(Long userId) throws UserNotFoundException {
        List<RequestDto> requests = getRequestsByUser(userId);
        List<AdDto> result = new LinkedList<AdDto>();
        
        for ( RequestDto requestDto : requests ) {
            Long adId = requestDto.getAdId();
            try {
                result.add(getAdById(adId));
            } catch ( AdNotFoundException ex ) {
                logger.error("Ad not found (adId: " + adId + ") when building user requested ads", ex);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public AdDto getAdById(Long adId) throws AdNotFoundException {
        User currentUser = getCurrentUser();
        Ad ad = validateAd(adId);

        if (!ad.getCreator().equals(currentUser) && !ad.isAlreadyViewedBy(currentUser)) {
            ad.visit();
        }
        
        Viewer viewer = new Viewer(ad, currentUser);
        viewerDao.save(viewer);

        // @formatter:off
        AdDto adDto = new AdDtoBuilder(ad)
                .setCurrentUser(currentUser)
                .includeImages()
                .includeCreator()
                .includeCanMarkAsSpam()
                .includeCanRate()
                .build();
        // @formatter:on

        adDto.setInBookmars(inBookmarks(currentUser, ad));
        adDto.setRequested(requested(currentUser, ad));
        
        return adDto;
    }
    
    
    
    //*****************
    //* ad statistics *
    //*****************
    
    @Override
    @Transactional
    public AdStatisticsDto getStatistics(Long adId) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        
        AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(getCurrentUser()).build();
        return adDto.getStatistics();
    }

    
    
    //***********************
    //* ad lifecycle change *
    //***********************
    
    @Override
    @Transactional
    public void relistAd(Long adId) throws AdNotFoundException, AuthorizationException, InvalidAdStateException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can relist the ad!");
        }

        if (ad.isDeleted() /* || ad.isSold() || ad.isSpam() */) {
            throw new InvalidAdStateException("Ad can't be relisted as is deleted!");
        }
        
        if ( ad.getStatus() == AdStatus.EXPIRED || ad.getStatus() == AdStatus.SELECTED ) {
            //only EXPIRED or SELECTED status ads can be relisted
            if ( ad.canProlong() ) {
                //relist ad
                ad.prolong(Constants.AD_PROLONGATION_PERIOD_DAYS);
                ad.setStatus(AdStatus.ACTIVE);
            } else {
                throw new InvalidAdStateException("Ad can't be relisted anymore!");
            }
        } else {
            throw new InvalidAdStateException("Ad can't be relisted as its state (" + ad.getStatus() + ") is not as expected!");
        }
    }

    @Override
    @Transactional
    public void endAd(Long adId) throws AdNotFoundException, AuthorizationException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();

        if (!ad.getCreator().equals(currentUser)) {
            throw new AuthorizationException("Only the creator can ends the ad");
        }

        if (!ad.isSold()) {
            ad.markAsSold();
            ad.setExpired(true);
            ad.setExpiresAt(new Date());
        }
    }
    
    
    
    //***************
    //* ad requests *
    //***************
    
    @Override
    @Transactional
    public Long requestAd(Long adId) throws AdNotFoundException, AlreadyRequestedException, InvalidRequestException, InvalidAdStateException {
        Ad ad = validateAd(adId);
        User user = getCurrentUser();
        Long userId = getCurrentUserId();
        
        if (ad.getStatus() == AdStatus.ACTIVE) {
            //continue
        } else {
            //only ACTIVE status ads can be requested
            throw new InvalidAdStateException("Ad can't be requested as its state (" + ad.getStatus() + ") is not as expected!");
        }
        
        Request request = requestDao.get(userId, adId);
        if ( request != null ) {
            throw new AlreadyRequestedException("Ad (id: " + adId + ") already requested by the user (id: " + userId + ")");
        }
        
        if ( ad.getCreator().equals(user) ) {
            throw new InvalidRequestException("Cannot request owned ads.");
        }
        if ( ad.getRequests() != null && ad.getRequests().size() - 1 > Constants.REQUEST_MAX_ALLOWED ) {
            throw new InvalidRequestException("Max request limit reached.");
        }
        if ( ad.getAdData().getQuantity() <= 0 ) {
            throw new InvalidRequestException("No more available.");
        }
        
        request = new Request();
        request.setAd(ad);
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        Long requestId = requestDao.save(request);
        
        
        if ( ad.getType() == AdType.BUSINESS ) {
            //auto select request for business ads
            selectRequest(request, ad.getCreator());
        }
        
        return requestId;
    }
    
    @Override
    @Transactional
    public void cancelRequest(Long requestId) throws RequestNotFoundException, InvalidRequestException, InvalidAdStateException {
        User user = getCurrentUser();
        Request request = validateRequest(requestId);
        Ad ad = request.getAd();
        
        if ( ad.getStatus() == AdStatus.ACTIVE || ad.getStatus() == AdStatus.EXPIRED || ad.getStatus() == AdStatus.SELECTED ) {
            //continue
        } else {
            throw new InvalidAdStateException("Ad can't be requested as its state (" + ad.getStatus() + ") is not as expected!");
        }
        
        if ( ad.getCreator().equals(user) || request.getUser().equals(user) ) {
            //continue
        } else {
            throw new InvalidRequestException("Only owned requests can be cancelled");
        }
        
        request.unmarkAsSelected();
        request.markAsDeleted();
        
        ad.unselect(); //increment available quantity
    }
    
    @Override
    @Transactional
    public void selectRequest(Long requestId) throws RequestNotFoundException, InvalidRequestException, InvalidAdStateException {
        Request request = validateRequest(requestId);
        User user = getCurrentUser();
        
        selectRequest(request, user);
    }
    
    @Override
    @Transactional
    public RequestDto getRequestById(Long requestId) throws RequestNotFoundException {
        Request request = validateRequest(requestId);
        return new RequestDto(request);
    }
    
    @Override
    @Transactional
    public List<RequestDto> getRequests(Long adId) throws AdNotFoundException {
        Ad ad = validateAd(adId);
        List<Request> requests = requestDao.getByAd(adId);
        List<RequestDto> result = new LinkedList<RequestDto>();
        
        if ( requests != null && !requests.isEmpty() ) {
            for ( Request request : requests ) {
                result.add(new RequestDto(request));
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<RequestDto> getRequestsByUser(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Request> requests = requestDao.getByUser(userId);
        List<RequestDto> result = new LinkedList<RequestDto>();
        
        if ( requests != null && !requests.isEmpty() ) {
            for ( Request request : requests ) {
                switch ( request.getStatus() ) {
                    case PENDING:
                    case ACCEPTED: {
                        result.add(new RequestDto(request));
                        break;
                    }
                    case EXPIRED:
                    default: {
                        //expired requests should not be returned
                    }
                }
            }
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
                if ( request.getStatus() == RequestStatus.ACCEPTED ) {
                    Long fromUserId = request.getUser().getId();
                    Long adId = request.getAd().getId();
                    Rating rating = ratingDao.get(fromUserId, adId);
                    if ( rating == null ) {
                        result.add(new RequestDto(request));
                    }
                }
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public void markAsSent(Long requestId) throws RequestNotFoundException, InvalidRequestException {
        Request request = validateRequest(requestId);
        User user = getCurrentUser();
        Ad ad = request.getAd();
        
        if ( !ad.getCreator().equals(user) ) {
            throw new InvalidRequestException("Only creator users can mark as sent.");
        }
        
        ad.setSent(true);
        ad.setSentAt(new Date());
        ad.setStatus(AdStatus.SENT);
    }
    
    @Override
    @Transactional
    public void markAsReceived(Long requestId) throws RequestNotFoundException, InvalidRequestException {
        Request request = validateRequest(requestId);
        User user = getCurrentUser();
        Ad ad = request.getAd();
        
        if ( !request.getUser().equals(user) ) {
            throw new InvalidRequestException("Only requestor can mar as received.");
        }
        
        ad.setReceived(true);
        ad.setReceivedAt(new Date());
        ad.setStatus(AdStatus.RECEIVED);
    }
    
    
    
    //***************
    //* ad bookmark *
    //***************

    @Override
    @Transactional
    public Long bookmarkAd(Long adId) throws AdNotFoundException {
        Long currentUserId = getCurrentUserId();
        Bookmark bookmark = bookmarkDao.get(currentUserId, adId);

        if (bookmark == null) {
            User currentUser = getCurrentUser();
            Ad ad = validateAd(adId);
            
            bookmark = new Bookmark(currentUser, ad);
            bookmarkDao.save(bookmark);
        }

        return bookmark.getId();
    }

    @Override
    public void removeBookmark(Long adId) throws BookmarkNotFoundException {
        Long currentUserId = getCurrentUserId();
        Bookmark bookmark = bookmarkDao.get(currentUserId, adId);

        if (bookmark == null) {
            throw new BookmarkNotFoundException(adId);
        }

        bookmarkDao.delete(bookmark);
    }
    
    @Override
    @Transactional
    public List<AdDto> getBookmarkedAds() {
        try {
            return getBookmarkedAds(getCurrentUserId());
        } catch ( UserNotFoundException ex ) {
            logger.error("User (current/logged) not found", ex);
            return new ArrayList<AdDto>();
        }
    }
    
    @Override
    @Transactional
    public List<AdDto> getBookmarkedAds(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Ad> bookmarkedAds = bookmarkDao.getBookmarkedAds(user);
        List<AdDto> result = new LinkedList<AdDto>();

        if ( bookmarkedAds != null && !bookmarkedAds.isEmpty() ) {
            for (Ad ad : bookmarkedAds) {
                AdDto adDto = new AdDtoBuilder(ad)
                        .setCurrentUser(user)
                        .includeCreator(true) //TODO: maybe this is not needed
                        .build();
                adDto.setInBookmars(true);
                adDto.setRequested(requested(user, ad));
                result.add(adDto);
            }
        }
        return result;
    }

    
    
    //****************
    //* ad spam mark *
    //****************
    
    @Override
    @Transactional
    public void markAsSpam(Long adId) throws AdNotFoundException {
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
            ad.markAsDeleted();
        }

        SpamMark newMark = new SpamMark(ad, currentUser);
        spamMarkDao.save(newMark);
    }

    @Override
    @Transactional
    public void unmarkAsSpam(Long adId) throws AdNotFoundException {
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
                ad.unmarkAsDeleted();
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
    public float rateAd(RatingDto ratingDto) throws AdNotFoundException, UserNotFoundException, InvalidRateOperationException, AlreadyRatedException, InvalidAdStateException {
        Ad ad = validateAd(ratingDto.getAdId());
        User from = getCurrentUser();
        User to = validateUser(ratingDto.getToUserId());
        String text = ratingDto.getText();
        int ratingValue = ratingDto.getValue();
        
        if ( ad.getStatus() == AdStatus.SENT || ad.getStatus() == AdStatus.RECEIVED ) {
            //continue
        } else {
            throw new InvalidAdStateException("Ad can't be selected as its state (" + ad.getStatus() + ") is not as expected!");
        }
        
        if (ratingValue != -1 && ratingValue != 1) {
            throw new InvalidRateOperationException("Only '1' or '-1' can be used as rating values!");
        } else if ( from.equals(to) ) {
            throw new InvalidRateOperationException("You cannot rate your ad for yourself.");
        }

        for (Rating r : ad.getRatings()) {
            if (r.getFrom().equals(from)) {
                throw new AlreadyRatedException("You've already rated this ad!");
            }
        }
        
        if (ad.getCreator().equals(from) || ad.getCreator().equals(to)) {
            Long adId = ad.getId();
            Long userId;
            if (ad.getCreator().equals(to)) {
                userId = from.getId();
            } else {
                userId = to.getId();
            }
            
            Request request = requestDao.get(userId, adId);
            if ( request == null ) {
                throw new InvalidRateOperationException("There is no request for user (userId: " + userId + ") on this ad (adId: " + adId + ")");
            }
            if ( request.getStatus() != RequestStatus.ACCEPTED ) {
                throw new InvalidRateOperationException("The request is not selected on this ad (adId: " + adId + ")");
            }
        } else {
            throw new InvalidRateOperationException("Invalid user to rate.");
        }
        

        Rating rating = new Rating(ad, from, to, text, ratingValue);
        ratingDao.save(rating);
        
        ad.getRatings().add(rating);

        // recalculate ad rating
        float totalRating = 0.0f;
        for (Rating r : ad.getRatings()) {
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
        List<RatingDto> result = new LinkedList<RatingDto>();
        
        if ( ratings != null && !ratings.isEmpty() ) {
            for ( Rating rating : ratings ) {
                result.add(new RatingDto(rating));
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<RatingDto> getSentRatings(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Rating> ratings = ratingDao.getSentByUser(user.getId());
        List<RatingDto> result = new LinkedList<RatingDto>();
        
        if ( ratings != null && !ratings.isEmpty() ) {
            for ( Rating rating : ratings ) {
                result.add(new RatingDto(rating));
            }
        }
        return result;
    }

    
    
    // internal helpers
    
    private Request validateRequest(Long requestId) throws RequestNotFoundException {
        if (requestId == null) {
            throw new NullPointerException("requestId is null!");
        }
        
        Request request = requestDao.get(requestId);
        if (request == null) {
            throw new RequestNotFoundException(requestId);
        }
        return request;
    }
    
    private Image validateImage(Long imageId) throws ImageNotFoundException {
        Image image = imageDao.get(imageId);
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
    
    
    
    private Image saveImage(ImageDto imageDto, AdField field) throws AdValidationException {
        if (imageDto != null) {
            if (imageDto.isValid()) {
                Image image = imageDto.toImage();
                imageDao.save(image);
                return image;
            } else {
                throw new AdValidationException(field, "Invalid image specified!");
            }
        }
        return null;
    }
    
    
    
    private List<CategoryDto> getCategoriesInternal(Long categoryId, boolean includeSubcategories) {
        List<CategoryDto> result = new LinkedList<CategoryDto>();
        List<Category> categories = categoryDao.getSubcategories(categoryId);

        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto(category, includeSubcategories);
            result.add(categoryDto);
        }

        return result;
    }
    
    private boolean includeOwnedAd(Ad ad, User user, FilterDto filter) {
        if ( filter == null ) {
            return true;
        }
        Boolean includeOwned = filter.getIncludeOwned() != null ? filter.getIncludeOwned() : true;
        if ( !includeOwned && ad.getCreator().equals(user) ) {
            //skipping owned ads
            return false;
        }
        return true;
    }
    
    private boolean inBookmarks(List<Ad> bokmarkedAds, Ad ad) {
        return bokmarkedAds.contains(ad);
    }
    
    private boolean inBookmarks(User user, Ad ad) {
        Bookmark bookmark = bookmarkDao.get(user.getId(), ad.getId());
        if (bookmark != null) {
            return true;
        }
        return false;
    }
    
    private boolean requested(User user, Ad ad) {
        if ( ad.getRequests() != null && !ad.getRequests().isEmpty() ) {
            for ( Request request : ad.getRequests() ) {
                if ( request.getUser().equals(user) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    
    private void selectRequest(Request request, User selector) throws InvalidRequestException, InvalidAdStateException {
        Ad ad = request.getAd();
        
        if ( ad.getStatus() == AdStatus.ACTIVE || ad.getStatus() == AdStatus.EXPIRED ) {
            //continue
        } else {
            throw new InvalidAdStateException("Ad can't be selected as its state (" + ad.getStatus() + ") is not as expected!");
        }
        if ( !ad.getCreator().equals(selector) ) {
            throw new InvalidRequestException("Only owner can select a requestor!");
        }
        if ( request.isSelected() ) {
            throw new InvalidRequestException("Request already selected");
        }
        
        request.setStatus(RequestStatus.ACCEPTED);
        request.markAsSelected();
        
        ad.select(); //decrementing available quantity
        ad.setStatus(AdStatus.SELECTED);
        ad.setExpiresAt(new Date()); //reset the expiration date
    }
    
}
