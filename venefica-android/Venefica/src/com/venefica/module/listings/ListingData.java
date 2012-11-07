/**
 * 
 */
package com.venefica.module.listings;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.venefica.module.user.UserDto;
import com.venefica.services.ImageDto;

/**
 * @author avinash
 *
 */
public class ListingData {
	/**
	 * listing id
	 */
	private long listingId;
	/**
	 * Category id
	 */
	private long categoryId;
	/**
	 * Category name
	 */
	private String category = "";
	/**
	 * listing Title
	 */
	private String title = "";
	/**
	 * Listing Description
	 */
	private String description = "";
	/**
	 * Price
	 */
	private BigDecimal price = new BigDecimal(0.0f);
	/**
	 * Location
	 */
	private double latitude;
	private double longitude;
	/**
	 * Main image
	 */
	private ImageDto image;
	/**
	 * Creation time
	 */
	/** takes milliseconds */
	private String createdAt = "";
	/** true - the current user is the owner of ad*/
	private boolean isOwner;
	/**
	 * Thumbnail to show on list 
	 */
	private ImageDto imageThumbnail;
	/**
	 * Is already bookmarked
	 */
	private boolean isBookmarked;
	/**
	 * Expiary date
	 */
	private Date expiresAt;
//	private boolean wanted;
	/**
	 * No of users who viewd this listing
	 */
	private long numOfViews;
	/**
	 * Owner details
	 */
	private UserDto owner;
	/**
	 * Is listing expired
	 */
	private boolean isExpired;
//	private int numAvailProlongations;
	
	private boolean isMarkedAsSpam;
	/**
	 * Rating
	 */
	private float rating;
	/**
	 * rating status of listing by logged in user
	 */
	private boolean isAlreadyRated;
	/**
	 * Images
	 */
	private List<ImageDto> images;
	/**
	 * @return the listingId
	 */
	public long getListingId() {
		return listingId;
	}
	/**
	 * @param listingId the listingId to set
	 */
	public void setListingId(long listingId) {
		this.listingId = listingId;
	}
	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the image
	 */
	public ImageDto getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(ImageDto image) {
		this.image = image;
	}
	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * @return the isOwner
	 */
	public boolean isOwner() {
		return isOwner;
	}
	/**
	 * @param isOwner the isOwner to set
	 */
	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}
	/**
	 * @return the imageThumbnail
	 */
	public ImageDto getImageThumbnail() {
		return imageThumbnail;
	}
	/**
	 * @param imageThumbnail the imageThumbnail to set
	 */
	public void setImageThumbnail(ImageDto imageThumbnail) {
		this.imageThumbnail = imageThumbnail;
	}
	/**
	 * @return the isBookmarked
	 */
	public boolean isBookmarked() {
		return isBookmarked;
	}
	/**
	 * @param isBookmarked the isBookmarked to set
	 */
	public void setBookmarked(boolean isBookmarked) {
		this.isBookmarked = isBookmarked;
	}
	/**
	 * @return the expiresAt
	 */
	public Date getExpiresAt() {
		return expiresAt;
	}
	/**
	 * @param expiresAt the expiresAt to set
	 */
	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}
	/**
	 * @return the numOfViews
	 */
	public long getNumOfViews() {
		return numOfViews;
	}
	/**
	 * @param numOfViews the numOfViews to set
	 */
	public void setNumOfViews(long numOfViews) {
		this.numOfViews = numOfViews;
	}
	/**
	 * @return the owner
	 */
	public UserDto getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(UserDto owner) {
		this.owner = owner;
	}
	/**
	 * @return the isExpired
	 */
	public boolean isExpired() {
		return isExpired;
	}
	/**
	 * @param isExpired the isExpired to set
	 */
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
	/**
	 * @return the isMarkedAsSpam
	 */
	public boolean isMarkedAsSpam() {
		return isMarkedAsSpam;
	}
	/**
	 * @param isMarkedAsSpam the isMarkedAsSpam to set
	 */
	public void setMarkedAsSpam(boolean isMarkedAsSpam) {
		this.isMarkedAsSpam = isMarkedAsSpam;
	}
	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}
	/**
	 * @return the isAlreadyRated
	 */
	public boolean isAlreadyRated() {
		return isAlreadyRated;
	}
	/**
	 * @param isAlreadyRated the isAlreadyRated to set
	 */
	public void setAlreadyRated(boolean isAlreadyRated) {
		this.isAlreadyRated = isAlreadyRated;
	}
	/**
	 * @return the images
	 */
	public List<ImageDto> getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List<ImageDto> images) {
		this.images = images;
	}
	

}
