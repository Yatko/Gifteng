package com.venefica.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jws.WebService;


import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.venefica.auth.ThreadSecurityContextHolder;
import com.venefica.dao.AdDao;
import com.venefica.dao.BookmarkDao;
import com.venefica.dao.CategoryDao;
import com.venefica.dao.ImageDao;
import com.venefica.dao.RatingDao;
import com.venefica.dao.SpamMarkDao;
import com.venefica.dao.UserDao;
import com.venefica.model.Ad;
import com.venefica.model.Bookmark;
import com.venefica.model.Category;
import com.venefica.model.Image;
import com.venefica.model.Rating;
import com.venefica.model.SpamMark;
import com.venefica.model.User;
import com.venefica.service.dto.AdDto;
import com.venefica.service.dto.CategoryDto;
import com.venefica.service.dto.FilterDto;
import com.venefica.service.dto.ImageDto;
import com.venefica.service.dto.builder.AdDtoBuilder;
import com.venefica.service.fault.AdField;
import com.venefica.service.fault.AdNotFoundException;
import com.venefica.service.fault.AdValidationException;
import com.venefica.service.fault.AlreadyRatedException;
import com.venefica.service.fault.AuthorizationException;
import com.venefica.service.fault.BookmarkNotFoundException;
import com.venefica.service.fault.ImageField;
import com.venefica.service.fault.ImageNotFoundException;
import com.venefica.service.fault.ImageValidationException;
import com.venefica.service.fault.InvalidAdStateException;
import com.venefica.service.fault.InvalidRateOprationException;

@Service("adService")
@WebService(endpointInterface = "com.venefica.service.AdService")
public class AdServiceImpl implements AdService {

	public static final int PROLONGATION_PERIOD_DAYS = 31;
	public static final int EXPIRATION_PERIOD_DAYS = 31 * 2;
	public static final int MAX_SPAM_MARKS = 3;

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(AdServiceImpl.class);

	@Inject
	private ThreadSecurityContextHolder securityContextHolder;

	@Inject
	private CategoryDao categoryDao;
	@Inject
	private AdDao adDao;
	@Inject
	private ImageDao imageDao;
	@Inject
	private BookmarkDao bookmarkDao;
	@Inject
	private UserDao userDao;
	@Inject
	private SpamMarkDao spamMarkDao;
	@Inject
	private RatingDao ratingDao;

	/**
	 * Creates basic categories
	 */
	@PostConstruct
	@Transactional
	public void init() {
		if (categoryDao.hasCategories())
			return;

		// TODO: Clean up the CODE!
		String[] categoryNames = new String[] { "local places", "buy/sell/trade", "automotive",
				"musician", "rentals", "real estate", "jobs" };

		for (String categoryName : categoryNames) {
			Category category = categoryDao.findByName(categoryName);

			if (category != null)
				continue;

			category = new Category(null, categoryName);
			categoryDao.save(category);
		}

		// create local places subcategories
		Category localPlacesCategory = categoryDao.findByName("local places");
		String[] localPlacesSubcategories = new String[] { "events", "bars/clubs", "restaurants",
				"salons/nails/spas" };

		for (String categoryName : localPlacesSubcategories) {
			Category category = categoryDao.findByName(categoryName);

			if (category != null)
				continue;

			category = new Category(localPlacesCategory, categoryName);
			categoryDao.save(category);
		}

	}

	@Override
	public List<CategoryDto> getCategories(Long categoryId) {
		return getCategoriesInternal(categoryId, false);
	}

	@Override
	@Transactional
	public List<CategoryDto> getAllCategories() {
		return getCategoriesInternal(null, true);
	}

	@Override
	@Transactional
	public Long placeAd(AdDto adDto) throws AdValidationException {
		User currentUser = getCurrentUser();

		Ad ad = new Ad();
		adDto.update(ad, currentUser);

		// ++ TODO: create ad validator
		if (adDto.getCategoryId() == null)
			throw new AdValidationException(AdField.CATEGORY_ID, "Category id not specified!");

		Category category = categoryDao.get(adDto.getCategoryId());

		if (category == null)
			throw new AdValidationException("Category with id = " + adDto.getCategoryId()
					+ " not found!");

		ad.setCategory(category);

		if (adDto.getTitle() == null)
			throw new AdValidationException(AdField.TITLE, "Title not specified!");

		ImageDto imageDto = adDto.getImage();

		// Assign main image?
		if (imageDto != null) {
			if (imageDto.isValid()) {
				Image image = imageDto.toImage();
				imageDao.save(image);
				ad.setMainImage(image);
			} else
				throw new AdValidationException(AdField.IMAGE, "Invalid image specified!");
		}

		ImageDto thumbImageDto = adDto.getImageThumbnail();

		// Assign thumb image?
		if (thumbImageDto != null) {
			if (thumbImageDto.isValid()) {
				Image thumbImage = thumbImageDto.toImage();
				imageDao.save(thumbImage);
				ad.setThumbImage(thumbImage);
			} else
				throw new AdValidationException(AdField.THUMB_IMAGE, "Invalid image specified!");
		}
		// ++

		ad.setCreator(currentUser);

		if (adDto.getExpiresAt() != null) {
			ad.setExpiresAt(adDto.getExpiresAt());
		} else {
			Date expiresAt = DateUtils.addDays(new Date(), EXPIRATION_PERIOD_DAYS);
			ad.setExpiresAt(expiresAt);
		}

		ad.setExpired(false);

		return adDao.save(ad);
	}

	@Override
	public List<AdDto> getAds(Long lastAdId, int numberAds) {
		List<AdDto> result = new LinkedList<AdDto>();
		List<Ad> ads = adDao.get(lastAdId, numberAds);

		User currentUser = getCurrentUser();

		for (Ad ad : ads) {
			AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(currentUser).build();
			result.add(adDto);
		}

		return result;
	}

	@Override
	@Transactional
	public List<AdDto> getAds(Long lastAdId, int numberAds, FilterDto filter) {
		List<AdDto> result = new LinkedList<AdDto>();
		List<Ad> ads = adDao.get(lastAdId, numberAds, filter);

		User currentUser = getCurrentUser();

		// TODO: Optimize this		
		// Get current user's bookmarks
		// TODO: REMOVE IT!!!!!!!!
		List<Ad> bokmarkedAds = bookmarkDao.getBookmarkedAds(currentUser);

		for (Ad ad : ads) {
			AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(currentUser).build();
			adDto.setInBookmars(bokmarkedAds.contains(ad));
			result.add(adDto);
		}

		return result;
	}

	@Override
	@Transactional
	public List<AdDto> getMyAds() {
		User currentUser = getCurrentUser();
		List<AdDto> result = new LinkedList<AdDto>();
		List<Ad> ads = adDao.getByUser(currentUser.getId());

		for (Ad ad : ads) {
			AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(currentUser).build();
			result.add(adDto);
		}

		return result;
	}

	@Override
	@Transactional
	public AdDto getAdById(Long adId) throws AdNotFoundException {
		User currentUser = getCurrentUser();
		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException("Ad with id = " + adId + " not found!");

		if (!ad.getCreator().equals(currentUser))
			ad.visit();

		// @formatter:off
		AdDto adDto = new AdDtoBuilder(ad)
			.setCurrentUser(currentUser)
			.includeImages()
			.includeCreator()
			.includeCanMarkAsSpam()
			.includeCanRate()
			.build();
		// @formatter:on

		Bookmark bookmark = bookmarkDao.get(currentUser.getId(), ad.getId());

		if (bookmark != null)
			adDto.setInBookmars(true);

		return adDto;
	}

	@Override
	@Transactional
	public void relistAd(Long adId) throws AdNotFoundException, AuthorizationException,
			InvalidAdStateException {
		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException(adId);

		User currentUser = getCurrentUser();

		if (!ad.getCreator().equals(currentUser))
			throw new AuthorizationException("Only the creator can relist the ad!");

		if (ad.isDeleted() /* || ad.isSold() || ad.isSpam() */)
			throw new InvalidAdStateException("Ad can't be relisted!");

		if (ad.isExpired())
			ad.prolong(PROLONGATION_PERIOD_DAYS);
	}

	@Override
	@Transactional
	public Long addImageToAd(Long adId, ImageDto imageDto) throws AdNotFoundException,
			ImageValidationException {
		if (adId == null)
			throw new NullPointerException("adId is null");
		if (imageDto == null)
			throw new NullPointerException("imageDto is null");

		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException("Ad with id = " + adId + " not found!");

		// ++ TODO: create image validator
		if (imageDto.getImgType() == null)
			throw new ImageValidationException(ImageField.IMAGE_TYPE, "Image type not specified!");
		if (imageDto.getData() == null || imageDto.getData().length == 0)
			throw new ImageValidationException(ImageField.DATA, "Image data must be not empty!");
		// ++

		// Save and attache the image to the ad
		Image image = imageDto.toImage();
		imageDao.save(image);
		ad.getImages().add(image);

		return image.getId();
	}
	
	@Override
	@Transactional
	public void deleteImageFromAd(Long adId, Long imageId) throws AdNotFoundException,
			AuthorizationException, ImageNotFoundException {

		Ad ad = adDao.get(adId);
		
		if (ad == null)
			throw new AdNotFoundException(adId);
		
		User currentUser = getCurrentUser();
		
		if (!ad.getCreator().equals(currentUser))
			throw new AuthorizationException("Only creator can delete images!");
		
		Image image = imageDao.get(imageId);
		
		if (image == null)
			throw new ImageNotFoundException(imageId);
		
		if (!ad.getImages().contains(image))
			throw new AuthorizationException("Image doesn't belong to the specified ad!");
		
		ad.getImages().remove(image);
		imageDao.delete(image);
	}

	@Override
	@Transactional
	public void updateAd(AdDto adDto) throws AdNotFoundException, AdValidationException,
			AuthorizationException {
		Long adId = adDto.getId();

		Ad ad = adDao.get(adId);
		if (ad == null)
			throw new AdNotFoundException("Ad with id = " + adId + " not found!");

		// ++ TODO: create ad validator
		if (adDto.getTitle() == null)
			throw new AdValidationException(AdField.TITLE, "Title is null!");

		Long categoryId = adDto.getCategoryId();
		if (categoryId == null)
			throw new AdValidationException(AdField.CATEGORY_ID, "Category id not specified!");

		Category category = categoryDao.get(categoryId);
		if (category == null)
			throw new AdValidationException(AdField.CATEGORY_ID, "Category with id = " + categoryId
					+ " not found!");

		User currentUser = getCurrentUser();

		if (!ad.getCreator().equals(currentUser))
			throw new AuthorizationException("You can update only your own ads!");

		// WARNING: This update must be performed within an active transaction!
		adDto.update(ad, currentUser);

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
			} else
				throw new AdValidationException(AdField.IMAGE, "Invalid image specified!");
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
			} else
				throw new AdValidationException(AdField.THUMB_IMAGE, "Invalid image specified!");
		}

//		if (adDto.getExpiresAt() != null && currentUser.isBusinessAcc()) {
//			ad.setExpiresAt(adDto.getExpiresAt());
//			ad.setExpired(false);
//		}

		// ++
	}

	@Override
	@Transactional
	public void endAd(Long adId) throws AdNotFoundException, AuthorizationException {
		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException(adId);

		User currentUser = getCurrentUser();

		if (!ad.getCreator().equals(currentUser))
			throw new AuthorizationException("Only the creator can ends the ad");

		if (!ad.isSold()) {
			ad.markAsSold();
			ad.setExpired(true);
			ad.setExpiresAt(new Date());
		}
	}

	@Override
	@Transactional
	public void deleteAd(Long adId) throws AdNotFoundException, AuthorizationException {
		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException(adId);

		User currentUser = getCurrentUser();

		if (!ad.getCreator().equals(currentUser))
			throw new AuthorizationException("Only the creator can delete the ad");

		if (!ad.isDeleted())
			ad.markAsDeleted();
	}

	@Override
	@Transactional
	public Long bookmarkAd(Long adId) throws AdNotFoundException {
		Long currentUserId = securityContextHolder.getContext().getUserId();
		Bookmark bookmark = bookmarkDao.get(currentUserId, adId);

		if (bookmark == null) {
			User currentUser = userDao.get(currentUserId);
			Ad ad = adDao.get(adId);

			if (ad == null)
				throw new AdNotFoundException("Ad with id = " + adId + " not found!");

			bookmark = new Bookmark(currentUser, ad);
			bookmarkDao.save(bookmark);
		}

		return bookmark.getId();
	}

	@Override
	@Transactional
	public List<AdDto> getBookmarkedAds() {
		User currentUser = getCurrentUser();
		List<Ad> bookmaredAds = bookmarkDao.getBookmarkedAds(currentUser);

		List<AdDto> result = new LinkedList<AdDto>();

		for (Ad ad : bookmaredAds) {
			AdDto adDto = new AdDtoBuilder(ad).setCurrentUser(currentUser).build();
			adDto.setInBookmars(true);
			result.add(adDto);
		}

		return result;
	}

	@Override
	public void removeBookmark(Long adId) throws BookmarkNotFoundException {
		Long currentUserId = securityContextHolder.getContext().getUserId();
		Bookmark bookmark = bookmarkDao.get(currentUserId, adId);

		if (bookmark == null)
			throw new BookmarkNotFoundException(adId);

		bookmarkDao.delete(bookmark);
	}

	@Override
	@Transactional
	public void markAsSpam(Long adId) throws AdNotFoundException {
		if (adId == null)
			throw new NullPointerException("adId is null!");

		User currentUser = getCurrentUser();
		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException(adId);

		// Don't allow to mark twice as the same user
		for (SpamMark mark : ad.getSpamMarks()) {
			if (mark.getWitness().equals(currentUser))
				return;
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
	public void unmarkAsSapm(Long adId) throws AdNotFoundException {
		if (adId == null)
			throw new NullPointerException("adId is null!");

		User currentUser = getCurrentUser();
		Ad ad = adDao.get(adId);

		if (ad == null)
			throw new AdNotFoundException(adId);

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

	@Override
	@Transactional
	public float rateAd(Long adId, int ratingValue) throws AdNotFoundException,
			InvalidRateOprationException, AlreadyRatedException {
		Ad ad = adDao.get(adId);
		User currentUser = getCurrentUser();

		if (ad == null)
			throw new AdNotFoundException(adId);

		if (ad.getCreator().equals(currentUser))
			throw new InvalidRateOprationException("You can't rate your own ads!");

		if (ratingValue != -1 && ratingValue != 1)
			throw new InvalidRateOprationException("Only '1' or '-1' can be used as rating values!");

		for (Rating r : ad.getRatings()) {
			if (r.getUser().equals(currentUser))
				throw new AlreadyRatedException("You've already rated this ad!");
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

//	private User getCurrentUser() {
//		return securityContextHolder.getContext().getUser();
//	}

	private User getCurrentUser() {
		Long currentUserId = securityContextHolder.getContext().getUserId();
		return userDao.get(currentUserId);
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

}
