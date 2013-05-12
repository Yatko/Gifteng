package com.venefica.service;

import com.venefica.dao.AdDao;
import com.venefica.dao.BookmarkDao;
import com.venefica.dao.CategoryDao;
import com.venefica.dao.CommentDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.RatingDao;
import com.venefica.dao.RequestDao;
import com.venefica.dao.ReviewDao;
import com.venefica.dao.SpamMarkDao;
import com.venefica.dao.ViewerDao;
import com.venefica.model.Ad;
import com.venefica.model.Bookmark;
import com.venefica.model.Category;
import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.Rating;
import com.venefica.model.Request;
import com.venefica.model.RequestStatus;
import com.venefica.model.Review;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.model.Viewer;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.dto.ReviewDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdField;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.AlreadyReviewedException;
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

    private static final int PROLONGATION_PERIOD_DAYS = 31;
    private static final int EXPIRATION_PERIOD_DAYS = 31 * 2;
    private static final int MAX_SPAM_MARKS = 3;
    private static final int MAX_REQUESTS = 3;
    
    @Inject
    private CategoryDao categoryDao;
    @Inject
    private AdDao adDao;
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
    @Inject
    protected ReviewDao reviewDao;

//    /**
//     * Creates basic categories
//     */
//    @javax.annotation.PostConstruct
//    @Transactional
//    public void init() {
//        if (categoryDao.hasCategories()) {
//            return;
//        }
//
//        // TODO: Clean up the CODE!
//        String[] categoryNames = new String[]{
//            "local places",
//            "buy/sell/trade",
//            "automotive",
//            "musician",
//            "rentals",
//            "real estate",
//            "jobs"
//        };
//
//        for (String categoryName : categoryNames) {
//            Category category = categoryDao.findByName(categoryName);
//
//            if (category != null) {
//                continue;
//            }
//
//            category = new Category(null, categoryName);
//            categoryDao.save(category);
//        }
//
//        // create local places subcategories
//        Category localPlacesCategory = categoryDao.findByName("local places");
//        String[] localPlacesSubcategories = new String[]{
//            "events",
//            "bars/clubs",
//            "restaurants",
//            "salons/nails/spas"
//        };
//
//        for (String categoryName : localPlacesSubcategories) {
//            Category category = categoryDao.findByName(categoryName);
//
//            if (category != null) {
//                continue;
//            }
//
//            category = new Category(localPlacesCategory, categoryName);
//            categoryDao.save(category);
//        }
//    }

    
    
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

        Ad ad = new Ad();
        ad.setSent(false);
        ad.setReceived(false);
        
        adDto.update(ad);

        Category category = validateCategory(adDto.getCategoryId());
        
        // ++ TODO: create ad validator
        ad.setCategory(category);

        if (adDto.getTitle() == null) {
            throw new AdValidationException(AdField.TITLE, "Title not specified!");
        }

        ImageDto imageDto = adDto.getImage();

        // Assign main image?
        if (imageDto != null) {
            if (imageDto.isValid()) {
                Image image = imageDto.toImage();
                imageDao.save(image);
                ad.setMainImage(image);
            } else {
                throw new AdValidationException(AdField.IMAGE, "Invalid image specified!");
            }
        }

        ImageDto thumbImageDto = adDto.getImageThumbnail();

        // Assign thumb image?
        if (thumbImageDto != null) {
            if (thumbImageDto.isValid()) {
                Image thumbImage = thumbImageDto.toImage();
                imageDao.save(thumbImage);
                ad.setThumbImage(thumbImage);
            } else {
                throw new AdValidationException(AdField.THUMB_IMAGE, "Invalid image specified!");
            }
        }
        // ++

        ad.setCreator(currentUser);
        ad.setExpired(false);

        if (adDto.getExpiresAt() != null) {
            ad.setExpiresAt(adDto.getExpiresAt());
        } else {
            Date expiresAt = DateUtils.addDays(new Date(), EXPIRATION_PERIOD_DAYS);
            ad.setExpiresAt(expiresAt);
        }

        return adDao.save(ad);
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
            
            if (!ad.getImages().contains(image)) {
                throw new AuthorizationException("Image doesn't belong to the specified ad!");
            }
            
            ad.removeImage(image);
            imageDao.delete(image);
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

                if (!ad.getImages().contains(image)) {
                    logger.warn("Image (imageId: " + imageId + ") doesn't belong to the specified ad!");
                    continue;
                }

                ad.removeImage(image);
                imageDao.delete(image);
            }
        }
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

        ad.setCategory(category);

        ImageDto imageDto = adDto.getImage();

        if (imageDto != null) {
            if (imageDto.isValid()) {
                Image image = ad.getMainImage();

                if (image != null) {
                    ad.setMainImage(null);
                    imageDao.delete(image);
                }

                image = imageDto.toImage();
                imageDao.save(image);
                ad.setMainImage(image);
            } else {
                throw new AdValidationException(AdField.IMAGE, "Invalid image specified!");
            }
        }

        ImageDto thumbImageDto = adDto.getImageThumbnail();

        if (thumbImageDto != null) {
            if (thumbImageDto.isValid()) {
                Image thumbImage = ad.getThumbImage();

                if (thumbImage != null) {
                    ad.setThumbImage(null);
                    imageDao.delete(thumbImage);
                }

                thumbImage = thumbImageDto.toImage();
                imageDao.save(thumbImage);
                ad.setThumbImage(thumbImage);
            } else {
                throw new AdValidationException(AdField.THUMB_IMAGE, "Invalid image specified!");
            }
        }

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
//            adDto.setFavorited(favorited(currentUser, ad));
            
            result.add(adDto);
        }

        return result;
    }

    @Override
    @Transactional
    public List<AdDto> getMyAds() {
        try {
            Long userId = getCurrentUserId();
            return getUserAds(userId);
        } catch ( UserNotFoundException ex ) {
            logger.error("User (current/logged) not found", ex);
            return new ArrayList<AdDto>();
        }
    }
    
    @Override
    @Transactional
    public List<AdDto> getUserAds(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Ad> ads = adDao.getByUser(userId);
        List<AdDto> result = new LinkedList<AdDto>();
        
        for (Ad ad : ads) {
            AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(user).build();
            result.add(adDto);
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public List<AdDto> getUserRequestedAds(Long userId) throws UserNotFoundException {
        List<RequestDto> requests = getRequestsForUser(userId);
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
//        adDto.setFavorited(favorited(currentUser, ad));
        
        return adDto;
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
            throw new InvalidAdStateException("Ad can't be relisted!");
        }

        if (ad.isExpired()) {
            //relist possible if ad is expired
            ad.prolong(PROLONGATION_PERIOD_DAYS);
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
    public Long requestAd(Long adId) throws AdNotFoundException, AlreadyRequestedException, InvalidRequestException {
        Ad ad = validateAd(adId);
        User user = getCurrentUser();
        Long userId = getCurrentUserId();
        
        Request request = requestDao.get(userId, adId);
        if ( request != null ) {
            throw new AlreadyRequestedException("Ad (id: " + adId + ") already requested by the user (id: " + userId + ")");
        }
        
        if ( ad.getCreator().equals(user) ) {
            throw new InvalidRequestException("Cannot request owned.");
        }
        if ( ad.isSelected() ) {
            throw new InvalidRequestException("Cannot request after selected.");
        }
        if ( ad.getRequests() != null && ad.getRequests().size() - 1 > MAX_REQUESTS ) {
            throw new InvalidRequestException("Max request limit reached.");
        }
        
        request = new Request();
        request.setAd(ad);
        request.setUser(user);
        request.setStatus(RequestStatus.PENDING);
        return requestDao.save(request);
    }
    
    @Override
    @Transactional
    public void cancelRequest(Long requestId) throws RequestNotFoundException, InvalidRequestException {
        User user = getCurrentUser();
        Request request = validateRequest(requestId);
        
        if ( !request.getUser().equals(user) ) {
            throw new InvalidRequestException("Only owned requests can be cancelled");
        }
        
        request.markAsDeleted();
        
        Ad ad = request.getAd();
        if ( ad.getSelectedRequest() != null && ad.getSelectedRequest().equals(request) ) {
            ad.unmarkAsSelected();
        }
    }
    
    @Override
    @Transactional
    public void selectRequest(Long requestId) throws RequestNotFoundException, InvalidRequestException {
        Request request = validateRequest(requestId);
        User user = getCurrentUser();
        
        request.setStatus(RequestStatus.ACCEPTED);
        
        Ad ad = request.getAd();
        
        if ( !ad.getCreator().equals(user) ) {
            throw new InvalidRequestException("Only owner can select a requestor!");
        }
        
        ad.markAsSelected(request);
        adDao.save(ad);
    }
    
    @Override
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
    public List<RequestDto> getRequestsForUser(Long userId) throws UserNotFoundException {
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
    public void markAsSent(Long requestId) throws RequestNotFoundException, InvalidRequestException {
        Request request = validateRequest(requestId);
        User user = getCurrentUser();
        Ad ad = request.getAd();
        
        if ( !ad.getCreator().equals(user) ) {
            throw new InvalidRequestException("Only creator users can mark as sent.");
        }
        
        ad.setSent(true);
        ad.setSentAt(new Date());
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
                        .includeCreator(true)
                        .build();
                adDto.setInBookmars(true);
                result.add(adDto);
            }
        }
        return result;
    }

    
    
//    //***************
//    //* ad favorite *
//    //***************
//    
//    @Override
//    @Transactional
//    public void markAsFavorite(Long adId) throws AdNotFoundException {
//        Ad ad = validateAd(adId);
//        User currentUser = getCurrentUser();
//        currentUser.addFavorite(ad);
//    }
//    
//    @Override
//    @Transactional
//    public void unmarkAsFavorite(Long adId) throws AdNotFoundException {
//        Ad ad = validateAd(adId);
//        User currentUser = getCurrentUser();
//        currentUser.removeFavorite(ad);
//    }
//    
//    @Override
//    @Transactional
//    public List<AdDto> getFavorites(Long userId) throws UserNotFoundException {
//        User user = validateUser(userId);
//        List<AdDto> result = new LinkedList<AdDto>();
//        if ( user.getFavorites() != null ) {
//            for ( Ad ad : user.getFavorites() ) {
//                AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(user).build();
//                result.add(adDto);
//            }
//        }
//        return result;
//    }
    
    
    
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

        if (ad.getSpamMarks().size() + 1 >= MAX_SPAM_MARKS) {
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
            if (ad.getSpamMarks().size() < MAX_SPAM_MARKS) {
                ad.unmarkAsDeleted();
                ad.unmarkAsSpam();
            }

            spamMarkDao.delete(markToRemove);
        }
    }
    
    
    
    //***********
    //* reviews *
    //***********

    @Override
    @Transactional
    public void addReview(ReviewDto reviewDto) throws AdNotFoundException, RequestNotFoundException, InvalidRequestException, AlreadyReviewedException {
        User currentUser = getCurrentUser();
        Ad ad = validateAd(reviewDto.getAdId());
        Long userId = currentUser.getId();
        
        Request request = requestDao.get(userId, ad.getId());
        if ( request == null ) {
            throw new RequestNotFoundException("There is no request for user (userId: " + userId + ")");
        }
        
        if ( !ad.isSelected() ) {
            throw new InvalidRequestException("There is no selected request for this ad (id: " + ad.getId() + ").");
        }
        if ( !request.equals(ad.getSelectedRequest()) ) {
            throw new InvalidRequestException("The users request is not selected.");
        }
        
        Long requestId = request.getId();
        Review review = reviewDao.getByRequest(requestId);
        if ( review != null ) {
            throw new AlreadyReviewedException("Request (id: " + requestId + ") already reviewed.");
        }
        
        review = new Review();
        review.setPositive(reviewDto.getPositive());
        review.setText(reviewDto.getText());
        review.setRequest(request);
        reviewDao.save(review);
    }
    
    @Override
    @Transactional
    public List<ReviewDto> getReceivedReviews(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Review> reviews = reviewDao.getReceivedForUser(user.getId());
        List<ReviewDto> result = new LinkedList<ReviewDto>();
        
        if ( reviews != null && !reviews.isEmpty() ) {
            for ( Review review : reviews ) {
                result.add(new ReviewDto(review));
            }
        }
        return result;
    }
    
    @Override
    @Transactional
    public List<ReviewDto> getSentReviews(Long userId) throws UserNotFoundException {
        User user = validateUser(userId);
        List<Review> reviews = reviewDao.getSentForUser(user.getId());
        List<ReviewDto> result = new LinkedList<ReviewDto>();
        
        if ( reviews != null && !reviews.isEmpty() ) {
            for ( Review review : reviews ) {
                result.add(new ReviewDto(review));
            }
        }
        return result;
    }
    
    //*************
    //* ad rating *
    //*************
    
    @Override
    @Transactional
    public float rateAd(Long adId, int ratingValue) throws AdNotFoundException, InvalidRateOperationException, AlreadyRatedException {
        Ad ad = validateAd(adId);
        User currentUser = getCurrentUser();
        
        if (ad.getCreator().equals(currentUser)) {
            throw new InvalidRateOperationException("You can't rate your own ads!");
        }

        if (ratingValue != -1 && ratingValue != 1) {
            throw new InvalidRateOperationException("Only '1' or '-1' can be used as rating values!");
        }

        for (Rating r : ad.getRatings()) {
            if (r.getUser().equals(currentUser)) {
                throw new AlreadyRatedException("You've already rated this ad!");
            }
        }

        Rating rating = new Rating(ad, currentUser, ratingValue);
        ratingDao.save(rating);
        ad.getRatings().add(rating);

        float totalRating = 0.0f;

        // recalculate ad rating
        for (Rating r : ad.getRatings()) {
            totalRating += r.getValue();
        }

        ad.setRating(totalRating);
        return totalRating;
    }

    
    
    // internal helpers
    
    private Ad validateAd(Long adId) throws AdNotFoundException {
        if (adId == null) {
            throw new NullPointerException("adId is null!");
        }
        
        Ad ad = adDao.get(adId);
        if (ad == null) {
            throw new AdNotFoundException(adId);
        }
        return ad;
    }
    
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
    
    private User validateUser(Long userId) throws UserNotFoundException {
        if (userId == null) {
            throw new NullPointerException("userId is null!");
        }
        
        User user = userDao.get(userId);
        if ( user == null ) {
            throw new UserNotFoundException("User with id '" + userId + "' not found");
        }
        return user;
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
    
//    private boolean favorited(User user, Ad ad) {
//        if ( user.getFavorites() != null && !user.getFavorites().isEmpty() ) {
//            for ( Ad favorit : user.getFavorites()) {
//                if ( favorit.getId().equals(ad.getId()) ) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
