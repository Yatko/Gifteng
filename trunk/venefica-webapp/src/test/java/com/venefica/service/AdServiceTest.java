package com.venefica.service;

import com.venefica.dao.AdDao;
import com.venefica.dao.BookmarkDao;
import com.venefica.dao.CategoryDao;
import com.venefica.dao.ImageDao;
import com.venefica.model.Ad;
import com.venefica.model.AdStatus;
import com.venefica.model.Bookmark;
import com.venefica.model.Image;
import com.venefica.model.ImageType;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.RatingDto;
import com.venefica.service.dto.RequestDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AlreadyRequestedException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.CategoryNotFoundException;
import com.venefica.service.fault.ImageNotFoundException;
import com.venefica.service.fault.ImageValidationException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRateOperationException;
import com.venefica.service.fault.InvalidRequestException;
import com.venefica.service.fault.RequestNotFoundException;
import com.venefica.service.fault.UserNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.commons.lang.time.DateUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "/AdServiceTest-context.xml")
public class AdServiceTest extends ServiceTestBase<AdService> {

    private static final Long FIRST_AD_ID = new Long(1);
    
    private static final float EPSILON = 0.0001f;
    
    @Inject
    private CategoryDao categoryDao;
    @Inject
    private AdDao adDao;
    @Inject
    private BookmarkDao bookmarkDao;
    @Inject
    private ImageDao imageDao;
    
    private Ad ad;

    public AdServiceTest() {
        super(AdService.class);
    }
    
    @Before
    public void initAd() {
        ad = adDao.get(FIRST_AD_ID);
        assertNotNull("Fixture ad not found!", ad);
    }
    
    //**********************
    //* categories related *
    //**********************

    @Test
    public void checkBasicCategoriesTest() {
        assertTrue("There are no categories!", categoryDao.hasCategories());
    }

    @Test
    public void listCategoriesTest() {
        authenticateClientAsFirstUser();
        
        List<CategoryDto> categories = client.getCategories(null);
        assertNotNull("At least an empty list must be retured", categories);
        assertTrue("There are no root categoriles", categories.size() > 0);

        CategoryDto first = categories.get(0);
        client.getCategories(first.getId());
    }

    @Test
    public void unexistingParentCategoryTest() {
        authenticateClientAsFirstUser();
        
        List<CategoryDto> categories = client.getCategories(new Long(-1));
        assertTrue("There are subcategories in the unexisting category. WTF?", categories == null
                || categories.isEmpty());
    }

    @Test
    public void getAllCategoriesTest() {
        authenticateClientAsFirstUser();
        
        List<CategoryDto> categories = client.getAllCategories();
        assertTrue("No categories returned!", categories != null && categories.size() > 0);
    }

    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
    @Test
    public void placeAdTest() throws CategoryNotFoundException, AdValidationException {
        authenticateClientAsFirstUser();
        
        CategoryDto category = client.getCategories(null).get(0);

        AdDto adDto = new AdDto();
        adDto.setCategoryId(category.getId());
        adDto.setTitle("Test");
        adDto.setDescription("Test description");
        adDto.setPrice(new BigDecimal(5.0));
        adDto.setLatitude(new Double(5.0));
        adDto.setLongitude(new Double(1.0));
        adDto.setImage(new ImageDto(ImageType.JPEG, new byte[]{0x01, 0x02, 0x03}));

        Long adId = client.placeAd(adDto);
        assertNotNull("The id of the ad must be returned!", adId);

        Ad ad_ = adDao.get(adId);
        assertNotNull("The ad with id = " + adId + " not found!", ad_);
        assertTrue("The ad must be marked as unreviewed!", !ad_.isReviewed());
        assertNotNull("Expiration date must be set!", ad_.getExpiresAt());
    }

    @Test
    public void placeInvalidAdTest() {
        authenticateClientAsFirstUser();
        AdDto adDto = new AdDto();

        try {
            client.placeAd(adDto);
            fail("Validation exeption must be thrown (categoryId)");
        } catch (AdValidationException e) {
            // it's OK
        }

        CategoryDto category = client.getCategories(null).get(0);
        adDto.setCategoryId(category.getId());

        try {
            client.placeAd(adDto);
            fail("Validation exeption must be thrown (title)");
        } catch (AdValidationException e) {
            // it's OK
        }
    }
    
    //*************************
    //* ads listing/filtering *
    //*************************

    @Test
    public void getAdsTest() {
        authenticateClientAsFirstUser();
        List<AdDto> ads = client.getAds(new Long(-1), 10);
        assertTrue("There must be at least one ad in the collection.", ads != null
                && ads.size() > 0);

        ads = client.getAds(new Long(1), 10);
    }

    @Test(expected = AdNotFoundException.class)
    public void getAdByIdTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.getAdById(new Long(-1));
    }
    
    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
    @Test
    public void addImageTest() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        ImageDto img = new ImageDto(ImageType.JPEG, new byte[]{0x01, 0x02, 0x03, 0x04});
        Long imgId = client.addImageToAd(ad.getId(), img);
        assertNotNull("Image id must be returned!", imgId);
    }
    
    @Test
    public void addLargeImageTest() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        byte[] data = new byte[1024 * 1024];
        for ( int i = 0; i < data.length; i++ ) {
            data[i] = 0x01;
        }
        ImageDto img = new ImageDto(ImageType.JPEG, data);
        Long imgId = client.addImageToAd(ad.getId(), img);
        assertNotNull("Image id must be returned!", imgId);
    }

    @Test(expected = ImageValidationException.class)
    public void addInvalidImageTest() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        ImageDto image = new ImageDto(null, null);
        client.addImageToAd(ad.getId(), image);
    }

    @Test(expected = ImageValidationException.class)
    public void addInvalidImageTest2() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        ImageDto image = new ImageDto(ImageType.JPEG, null);
        client.addImageToAd(ad.getId(), image);
    }

    @Test(expected = ImageValidationException.class)
    public void addInvalidImageTest3() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        ImageDto image = new ImageDto(null, new byte[]{0x01, 0x02});
        client.addImageToAd(ad.getId(), image);
    }

    @Test(expected = AdNotFoundException.class)
    public void addImageWithUnexistingAdTest() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        ImageDto image = new ImageDto(ImageType.JPEG, new byte[]{0x01, 0x02});
        client.addImageToAd(new Long(-1), image);
    }

    @Test(expected = SOAPFaultException.class)
    public void addImageWithNullAdTest() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        ImageDto image = new ImageDto(ImageType.JPEG, new byte[]{0x01, 0x02});
        client.addImageToAd(null, image);
    }

    @Test(expected = SOAPFaultException.class)
    public void addImageWithNullImageTest() throws AdNotFoundException, ImageValidationException {
        authenticateClientAsFirstUser();
        client.addImageToAd(ad.getId(), null);
    }

    @Test(expected = AdNotFoundException.class)
    public void deleteImageFromUnexistingAdTest() throws AdNotFoundException,
            AuthorizationException, ImageNotFoundException {
        authenticateClientAsFirstUser();
        client.deleteImageFromAd(new Long(-1), new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void deleteImageWithDifferentUserTest() throws AdNotFoundException,
            AuthorizationException, ImageNotFoundException {
        authenticateClientAsSecondUser();
        client.deleteImageFromAd(FIRST_AD_ID, new Long(-1));
    }

    @Test(expected = ImageNotFoundException.class)
    public void deleteUnexistingImageTest() throws AdNotFoundException, AuthorizationException,
            ImageNotFoundException {
        authenticateClientAsFirstUser();
        client.deleteImageFromAd(FIRST_AD_ID, new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void deleteImageNotBelongingToAdTest() throws AdValidationException,
            AdNotFoundException, ImageValidationException, AuthorizationException, ImageNotFoundException {
        authenticateClientAsFirstUser();

        // Create an ad with an image
        CategoryDto categoryDto = client.getCategories(null).get(0);

        AdDto adDto = new AdDto();
        adDto.setCategoryId(categoryDto.getId());
        adDto.setTitle("New ad with extra image");
        adDto.setDescription("Some descr");
        adDto.setPrice(new BigDecimal(100.0));

        Long newAdId = client.placeAd(adDto);
        Long newImageId = client.addImageToAd(newAdId, new ImageDto(ImageType.PNG, new byte[]{
                    0x03, 0x02, 0x01}));

        client.deleteImageFromAd(FIRST_AD_ID, newImageId);
    }

    @Test
    public void deleteImageFromAdTest() throws AdNotFoundException, ImageValidationException,
            AuthorizationException, ImageNotFoundException {
        authenticateClientAsFirstUser();
        Long imageId = client.addImageToAd(FIRST_AD_ID, new ImageDto(ImageType.JPEG, new byte[]{0x01,
                    0x02, 0x03}));
        client.deleteImageFromAd(FIRST_AD_ID, imageId);

        Image image = imageDao.get(imageId);
        assertTrue("Image not deleted!", image == null);
    }

    @Test(expected = AdNotFoundException.class)
    public void updateUnexistingAdTest() throws AdNotFoundException, AdValidationException,
            AuthorizationException {
        AdDto adDto = new AdDto();
        adDto.setId(new Long(-1));
        authenticateClientAsFirstUser();
        client.updateAd(adDto);
    }

    @Test
    public void updateInvalidAdTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(getFirstUser()).build();

        adDto.setTitle(null);
        try {
            client.updateAd(adDto);
            fail("Validation exception must be thrown (title)");
        } catch (AdValidationException e) {
            // It's OK
        }

        adDto.setTitle("Some title");
        adDto.setCategoryId(new Long(-1));

        try {
            client.updateAd(adDto);
            fail("Validation exception must be thrown (categoryId)");
        } catch (AdValidationException e) {
            // It's OK
        }

        adDto.setCategoryId(null);

        try {
            client.updateAd(adDto);
            fail("Validation exception must be thrown (categoryId)");
        } catch (AdValidationException e) {
            // It's OK
        }
    }

    @Test(expected = AuthorizationException.class)
    public void unauthorizedUpdateAdTest() throws AdNotFoundException, AdValidationException,
            AuthorizationException {
        authenticateClientAsSecondUser();
        AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(getFirstUser()).build();
        adDto.setTitle("Hacked category");
        client.updateAd(adDto);
    }

    @Test
    public void updateAdTest() throws AdNotFoundException, AdValidationException,
            AuthorizationException {
        authenticateClientAsFirstUser();

        AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(getFirstUser()).build();
        adDto.setTitle("New Title");
        adDto.setDescription("New Description");
        client.updateAd(adDto);

        Ad ad_ = adDao.get(adDto.getId());
        assertNotNull(ad_);
        assertTrue("Ad not updated!", adDto.getTitle().equals(ad_.getTitle()));
    }
    
    //***************
    //* ad bookmark *
    //***************

    @Test
    public void bookmarkAdTest() throws AdNotFoundException {
        authenticateClientAsSecondUser();
        Long bookmarkId = client.bookmarkAd(ad.getId());
        assertNotNull("Bookmark id must be returned!", bookmarkId);
        Bookmark bookmark = bookmarkDao.get(bookmarkId);
        assertNotNull("Bookmark not stored!", bookmark);
    }

    @Test(expected = AdNotFoundException.class)
    public void bookmarkUnexistingAdTest() throws AdNotFoundException {
        authenticateClientAsSecondUser();
        client.bookmarkAd(new Long(-1));
    }

    @Test(expected = BookmarkNotFoundException.class)
    public void removeUnexistingBookmarkTest() throws BookmarkNotFoundException {
        authenticateClientAsSecondUser();
        client.removeBookmark(new Long(-1));
    }

    @Test
    public void removeBookmarkTest() throws BookmarkNotFoundException, AdNotFoundException {
        authenticateClientAsSecondUser();
        Long bookmarkId = client.bookmarkAd(ad.getId());
        client.removeBookmark(ad.getId());
        Bookmark bookmark = bookmarkDao.get(bookmarkId);
        assertTrue("Bookmark not deleted!", bookmark == null);
    }

    @Test
    public void getBookmarkedAdsTest() {
        authenticateClientAsFirstUser();
        List<AdDto> bookmarkedAds = client.getBookmarkedAds();
        assertNotNull("List of ads must be returened!");
        assertTrue("There must be at least one ad in the list!", !bookmarkedAds.isEmpty());
    }

    //*************************
    //* ads listing/filtering *
    //*************************
    
    @Test
    public void getAdsExTest() {
        authenticateClientAsFirstUser();

        FilterDto filter = new FilterDto();
        LinkedList<Long> categories = new LinkedList<Long>();
        categories.add(new Long(1));
        categories.add(new Long(2));
        filter.setCategories(categories);

        filter.setDistance(new Long(100));
        filter.setMaxPrice(new BigDecimal("5.3"));
        filter.setHasPhoto(true);

        List<AdDto> ads = client.getAds(new Long(-1), 10, filter);
        assertTrue("There must be at least one ad in the collection.", ads != null
                && ads.size() > 0);
    }

    @Test
    @Transactional
    public void transactionalTest() {
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        assertTrue("Transaction is not active", active);
    }
    
    //****************
    //* ad spam mark *
    //****************
    
    @Test
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void markAsSpamTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.markAsSpam(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Spam mark not added!", !updatedAd.getSpamMarks().isEmpty());

        AdDto adDto = client.getAdById(ad.getId());
        assertTrue("canMarkAsSpam must be false!", !adDto.getCanMarkAsSpam());

        // try to mark it twice
        client.markAsSpam(ad.getId());

        updatedAd = adDao.get(ad.getId());
        assertTrue("You can mark the ad as spam only once!",
                updatedAd.getSpamMarks().size() == 1);
    }

    @Test(expected = AdNotFoundException.class)
    public void markAsSpamWithInvalidAdIdTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.markAsSpam(new Long(-1));
    }

    @Test
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void unmarkAsSpamTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.unmarkAsSpam(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Spam mark not removed from the ad!", updatedAd.getSpamMarks().isEmpty());
    }

    @Test(expected = AdNotFoundException.class)
    public void unmarkAsSpamWithInvalidAdIdTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.unmarkAsSpam(new Long(-1));
    }

    @Test
    public void markAsSpamAndDeleteTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.markAsSpam(ad.getId());

        authenticateClientAsSecondUser();
        client.markAsSpam(ad.getId());

        authenticateClientAsThirdUser();
        client.markAsSpam(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Ad must be marked as deleted!", updatedAd.isDeleted());
        assertTrue("Ad must be marked as spam!", updatedAd.isSpam());
    }

    @Test
    public void unmarkAsSpamAndRestoreTest() throws AdNotFoundException {
        authenticateClientAsFirstUser();
        client.unmarkAsSpam(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Ad must not be marked as deleted!", !updatedAd.isDeleted());
    }

    //***********************
    //* ad lifecycle change *
    //***********************
    
    @Test(expected = AdNotFoundException.class)
    public void endUnexistingAdTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        client.endAd(new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void endAdWithDifferentUserTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsSecondUser();
        client.endAd(ad.getId());
    }

    @Test
    public void endAdTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        client.endAd(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertNotNull("Ad not found!", updatedAd);
        assertTrue("Ad must be marked as ended!", updatedAd.isSold());
        assertNotNull("Ad must contain the date of selling", updatedAd.getSoldAt());
        assertTrue("Ad must be marked as expired!", updatedAd.isExpired());
        assertNotNull("Ad must contain the date of expiration!", updatedAd.getExpiresAt());
    }

    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
    @Test(expected = AdNotFoundException.class)
    public void deleteUnexistingAdTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        client.deleteAd(new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void deleteAdWithDifferentUserTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsSecondUser();
        client.deleteAd(ad.getId());
    }

    @Test
    public void deleteAdTest() throws AdNotFoundException, AuthorizationException {
        authenticateClientAsFirstUser();
        client.deleteAd(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertNotNull("Ad not found!", updatedAd);
        assertTrue("Ad must be marked as deleted!", updatedAd.isDeleted());
        assertNotNull("Ad must contain the date of removal", updatedAd.getDeletedAt());
    }

    //***********************
    //* ad lifecycle change *
    //***********************
    
    @Test(expected = AdNotFoundException.class)
    public void relistUnexistingAdTest() throws AdNotFoundException, AuthorizationException, InvalidAdStateException {
        authenticateClientAsFirstUser();
        client.relistAd(new Long(-1));
    }

    @Test(expected = AuthorizationException.class)
    public void relistAdWithDifferentUserTest() throws AdNotFoundException, AuthorizationException, InvalidAdStateException {
        authenticateClientAsSecondUser();
        client.relistAd(ad.getId());
    }

    @Test(expected = InvalidAdStateException.class)
    public void relistActiveAdTest() throws AdNotFoundException, AuthorizationException, InvalidAdStateException {
        makeAdActive();

        authenticateClientAsFirstUser();
        client.relistAd(ad.getId());

        fail("This line should not be reached");
        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Relist operation should not be performed on an active ad",
                updatedAd.getNumAvailProlongations() == ad.getNumAvailProlongations());
    }

    @Test(expected = InvalidAdStateException.class)
    public void relistDeletedAdTest() throws AdNotFoundException, AuthorizationException, InvalidAdStateException {
        authenticateClientAsFirstUser();
        client.deleteAd(ad.getId());
        client.relistAd(ad.getId());
    }

    @Test
    public void relistAdTest() throws AdNotFoundException, AuthorizationException, InvalidAdStateException {
        makeAdExpired();

        authenticateClientAsFirstUser();
        client.relistAd(ad.getId());

        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Number of available prolongations might have changed!", ad.getNumAvailProlongations() != updatedAd.getNumAvailProlongations());
        assertTrue("ExpiresAt date must be in future!", (new Date()).before(updatedAd.getExpiresAt()));
    }

    //*************************
    //* ads listing/filtering *
    //*************************
    
    @Test
    public void getMyAdsTest() {
        authenticateClientAsFirstUser();
        List<AdDto> ads = client.getMyAds();
        assertNotNull(ads);
        assertTrue("At least one ad might have returened!", !ads.isEmpty());

        authenticateClientAsThirdUser();
        ads = client.getMyAds();
        assertTrue(ads == null);
    }
    
    //*************
    //* ad rating *
    //*************

    @Test(expected = AdNotFoundException.class)
    public void rateUnexistingAd() throws AdNotFoundException, InvalidRateOperationException, AlreadyRatedException, UserNotFoundException, InvalidAdStateException {
        authenticateClientAsFirstUser();
        client.rateAd(new RatingDto(new Long(-1), SECOND_USER_ID, 0));
    }

//    @Test(expected = InvalidRateOperationException.class)
//    public void rateAdByCreatorTest() throws AdNotFoundException, InvalidRateOperationException, AlreadyRatedException, UserNotFoundException, InvalidAdStateException {
//        authenticateClientAsFirstUser();
//        client.rateAd(new RatingDto(ad.getId(), SECOND_USER_ID, 1));
//    }

    @Test(expected = InvalidRateOperationException.class)
    public void rateAdWithInvalidValueTest() throws AdNotFoundException, InvalidRateOperationException, AlreadyRatedException, UserNotFoundException, InvalidAdStateException {
        makeAdSent();
        authenticateClientAsSecondUser();
        client.rateAd(new RatingDto(ad.getId(), SECOND_USER_ID, -10));
    }
    
    @Test
    public void rateAdTest() throws AdNotFoundException, RequestNotFoundException, InvalidRequestException, AlreadyRequestedException, UserNotFoundException, InvalidRateOperationException, AlreadyRatedException, InvalidAdStateException {
        makeAdActive();
        authenticateClientAsThirdUser();
        client.requestAd(FIRST_AD_ID);
        
        authenticateClientAsSecondUser();
        Long requestId = client.requestAd(FIRST_AD_ID);
        
        authenticateClientAsFirstUser();
        client.selectRequest(requestId);
        
        List<RequestDto> unratedRequestsBefore = client.getRequestsForUserWithoutRating(FIRST_USER_ID);
        assertEquals(1, unratedRequestsBefore.size());
        assertEquals(SECOND_USER_ID, unratedRequestsBefore.get(0).getUser().getId());
        
        makeAdSent();
        authenticateClientAsSecondUser();
        RatingDto ratingDto_1 = new RatingDto();
        ratingDto_1.setAdId(FIRST_AD_ID);
        ratingDto_1.setToUserId(FIRST_USER_ID);
        ratingDto_1.setValue(1); //positive
        ratingDto_1.setText("Delivery on time");
        client.rateAd(ratingDto_1);
        
        
        Ad updatedAd = adDao.get(ad.getId());
        assertTrue("Rating value might have changed!", Math.abs(ad.getRating() - updatedAd.getRating()) > EPSILON);
        
        AdDto adDto = client.getAdById(ad.getId());
        assertTrue("Multiple ratings not allowed!", !adDto.getCanRate());
        
        
        List<RatingDto> sentRatings_1 = client.getSentRatings(FIRST_USER_ID);
        List<RatingDto> receivedRatings_1 = client.getReceivedRatings(FIRST_USER_ID);
        List<RatingDto> sentRatings_2 = client.getSentRatings(SECOND_USER_ID);
        List<RatingDto> receivedRatings_2 = client.getReceivedRatings(SECOND_USER_ID);
        List<RequestDto> unratedRequestsAfter = client.getRequestsForUserWithoutRating(FIRST_USER_ID);
        
        assertTrue(unratedRequestsAfter == null || unratedRequestsAfter.isEmpty());
        assertTrue(sentRatings_1 == null || sentRatings_1.isEmpty());
        assertEquals(1, receivedRatings_1.size());
        assertEquals(1, sentRatings_2.size());
        assertTrue(receivedRatings_2 == null || receivedRatings_2.isEmpty());
        
        
        
        authenticateClientAsFirstUser();
        RatingDto ratingDto_2 = new RatingDto();
        ratingDto_2.setAdId(FIRST_AD_ID);
        ratingDto_2.setToUserId(SECOND_USER_ID);
        ratingDto_2.setValue(1); //positive
        ratingDto_2.setText("Pickup on time");
        client.rateAd(ratingDto_2);
        
        List<RatingDto> sentRatings_1_ = client.getSentRatings(FIRST_USER_ID);
        List<RatingDto> receivedRatings_1_ = client.getReceivedRatings(FIRST_USER_ID);
        List<RatingDto> sentRatings_2_ = client.getSentRatings(SECOND_USER_ID);
        List<RatingDto> receivedRatings_2_ = client.getReceivedRatings(SECOND_USER_ID);
        
        assertEquals(1, sentRatings_1_.size());
        assertEquals(1, receivedRatings_1_.size());
        assertEquals(1, sentRatings_2_.size());
        assertEquals(1, receivedRatings_2_.size());
        
        
        try {
            client.rateAd(new RatingDto(ad.getId(), SECOND_USER_ID, -1));
            fail("Multiple ratings by the same user are not allowed!");
        } catch (AlreadyRatedException e) {
            // OK
        }
    }
    
    // internal helpers
    
    private void makeAdActive() {
        TransactionStatus status = beginNewTransaction();
        try {
            Date expiresAt = DateUtils.addDays(new Date(), 10);
            Ad ad_ = adDao.get(FIRST_AD_ID);
            ad_.unmarkAsDeleted();
            ad_.unmarkAsSold();
            ad_.unmarkAsSpam();
            ad_.setExpired(false);
            ad_.setExpiresAt(expiresAt);
            ad_.setStatus(AdStatus.ACTIVE);
            commitTransaction(status);
        } catch (Exception e) {
            rollbackTransaction(status);
            throw new RuntimeException(e);
        }
    }
    
    private void makeAdExpired() {
        TransactionStatus status = beginNewTransaction();
        try {
            Date expiresAt = DateUtils.addDays(new Date(), 10);
            
            Ad ad_ = adDao.get(FIRST_AD_ID);
            ad_.unmarkAsDeleted();
            ad_.unmarkAsSold();
            ad_.unmarkAsSpam();
            ad_.setExpired(true);
            ad_.setExpiresAt(expiresAt);
            ad_.setStatus(AdStatus.EXPIRED);
            commitTransaction(status);
        } catch (Exception e) {
            rollbackTransaction(status);
            throw new RuntimeException(e);
        }
    }
    
    private void makeAdSent() {
        TransactionStatus status = beginNewTransaction();
        try {
            Date expiresAt = DateUtils.addDays(new Date(), 10);
            
            Ad ad_ = adDao.get(FIRST_AD_ID);
            ad_.unmarkAsDeleted();
            ad_.unmarkAsSold();
            ad_.unmarkAsSpam();
            ad_.setExpired(true);
            ad_.setExpiresAt(expiresAt);
            ad_.setStatus(AdStatus.SENT);
            commitTransaction(status);
        } catch (Exception e) {
            rollbackTransaction(status);
            throw new RuntimeException(e);
        }
    }
    
    private void makeAdEnded() {
        TransactionStatus status = beginNewTransaction();
        try {
            Date expiresAt = DateUtils.addDays(new Date(), -10);
            
            Ad ad_ = adDao.get(FIRST_AD_ID);
            ad_.unmarkAsDeleted();
            ad_.unmarkAsSold();
            ad_.unmarkAsSpam();
            ad_.setExpired(true);
            ad_.setExpiresAt(expiresAt);
            commitTransaction(status);
        } catch (Exception e) {
            rollbackTransaction(status);
            throw new RuntimeException(e);
        }
    }
}
