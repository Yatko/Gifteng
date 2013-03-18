/**
 * 
 */
package com.venefica.module.listings;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.module.user.UserDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;

/**
 * @author avinash
 *
 */
public class ListingData implements KvmSerializable{
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
	private BigDecimal price;// = new BigDecimal(1.0f);
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
	private boolean isExpired = false;
//	private int numAvailProlongations;
	
	private boolean isMarkedAsSpam = false;
	/**
	 * Rating
	 */
	private float rating;
	/**
	 * rating status of listing by logged in user
	 */
	private boolean isAlreadyRated =false;
	/**
	 * Images
	 */
	private List<ImageDto> images;
	/**
	 * Currency code
	 */
	private String currencyCode="";
	/**
	 * Address
	 */
	private int zipcode;
	private String county ="", city="", state="", area="";
	
	private int numAvailProlongations;
	private boolean isWanted = false; 
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
	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	/**
	 * @return the zipcode
	 */
	public int getZipcode() {
		return zipcode;
	}
	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
	
	/**
	 * @return the isWanted
	 */
	public boolean isWanted() {
		return isWanted;
	}
	/**
	 * @param isWanted the isWanted to set
	 */
	public void setWanted(boolean isWanted) {
		this.isWanted = isWanted;
	}
	/**
	 * @return the numAvailProlongations
	 */
	public int getNumAvailProlongations() {
		return numAvailProlongations;
	}
	/**
	 * @param numAvailProlongations the numAvailProlongations to set
	 */
	public void setNumAvailProlongations(int numAvailProlongations) {
		this.numAvailProlongations = numAvailProlongations;
	}
	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return listingId;
			case 1:
				return categoryId;
			case 2:
				return category;
			case 3:
				return title;
			case 4:
				return description;
			case 5:
				return String.valueOf(price);
			case 6:
				return String.valueOf(latitude);
			case 7:
				return String.valueOf(longitude);
			case 8:
				return image;
			case 9:
				return createdAt;
			case 10:
				return owner;
			case 11:
				return imageThumbnail;
			case 12:
				return isBookmarked;
			case 13:
				return expiresAt != null ? expiresAt.getTime() : 0;
			case 14:
				return isWanted;
			case 15:
				return numOfViews;
			case 16:
				return owner;
			case 17:
				return isExpired;
			case 18:
				return numAvailProlongations;
			case 19:
				return isMarkedAsSpam;
			case 20:
				return String.valueOf(rating);
			case 21:
				return rating;
			case 22:
				return null; //images no send
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 23;
	}

	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info)
	{
		switch (index)
		{
			case 0:
				info.name = "id";
				info.type = Long.TYPE;
				break;

			case 1:
				info.name = "categoryId";
				info.type = Long.TYPE;
				break;

			case 2:
				info.name = "category";
				info.type = String.class;
				break;

			case 3:
				info.name = "title";
				info.type = String.class;
				break;

			case 4:
				info.name = "description";
				info.type = String.class;
				break;

			case 5:
				info.name = "price";
				info.type = BigDecimal.class;
				break;

			case 6:
				info.name = "latitude";
				info.type = Double.TYPE;
				break;

			case 7:
				info.name = "longitude";
				info.type = Double.TYPE;
				break;

			case 8:
				info.name = "image";
				info.type = ImageDto.class;
				break;

			case 9:
				info.name = "createdAt";
				info.type = String.class;
				break;

			case 10:
				info.name = "owner";
				info.type = Boolean.class;
				break;

			case 11:
				info.name = "imageThumbnail";
				info.type = ImageDto.class;
				break;

			case 12:
				info.name = "inBookmars";
				info.type = Boolean.class;
				break;

			case 13:
				info.name = "expiresAt";
				info.type = Long.class;
				break;

			case 14:
				info.name = "wanted";
				info.type = Boolean.class;
				break;

			case 15:
				info.name = "numViews";
				info.type = Long.class;
				break;

			case 16:
				info.name = "creator";
				info.type = UserDto.class;
				break;

			case 17:
				info.name = "expired";
				info.type = Boolean.class;
				break;

			case 18:
				info.name = "numAvailProlongations";
				info.type = Integer.class;
				break;

			case 19:
				info.name = "canMarkAsSpam";
				info.type = Boolean.class;
				break;

			case 20:
				info.name = "rating";
				info.type = Float.class;
				break;

			case 21:
				info.name = "canRate";
				info.type = Boolean.class;
				break;

			case 22:
				info.name = "images";
				info.type = new Vector<ImageDto>().getClass();
				break;

			default:
				break;
		}
	}

	@SuppressWarnings ("unchecked")
	public void setProperty(int index, Object value)
	{
		try
		{
			switch (index)
			{
				case 0:
					listingId = Long.valueOf(value.toString());
					break;
				case 1:
					categoryId = Long.valueOf(value.toString());
					break;
				case 2:
					category = String.valueOf(value);
					break;
				case 3:
					title = String.valueOf(value);
					break;
				case 4:
					description = String.valueOf(value);
					break;
				case 5:
					price = BigDecimal.valueOf(Double.valueOf(String.valueOf(value)));
					break;
				case 6:
					latitude = Double.valueOf(value.toString());
					break;
				case 7:
					longitude = Double.valueOf(value.toString());
					break;
				case 8:
					image = (ImageDto)value;
					break;
				case 9:
					createdAt = String.valueOf(value);
					break;
				case 10:
					isOwner = Boolean.parseBoolean(value.toString());
					break;
				case 11:
					imageThumbnail = (ImageDto)value;
					break;
				case 12:
					isBookmarked = Boolean.parseBoolean(value.toString());
					break;
				case 13:
					expiresAt = new Date(Long.parseLong(value.toString()));
					break;
				case 14:
					isWanted = Boolean.parseBoolean(value.toString());
					break;
				case 15:
					numOfViews = Long.valueOf(value.toString());
					break;
				case 16:
					owner = (UserDto)value;
					break;
				case 17:
					isExpired = Boolean.parseBoolean(value.toString());
					break;
				case 18:
					numAvailProlongations = Integer.valueOf(value.toString());
					break;
				case 19:
					isMarkedAsSpam = Boolean.parseBoolean(value.toString());
					break;
				case 20:
					rating = Float.valueOf(value.toString());
					break;
				case 21:
					isAlreadyRated = Boolean.parseBoolean(value.toString());
					break;
				case 22:
					images = (Vector<ImageDto>)value;
					
					/*if(images == null)
						images = new ArrayList<ImageDto>();
					
					images.add((ImageDto)value);*/
					break;
			}
		}
		catch (Exception e)
		{
			Log.d("AdDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
		new ImageDto().register(envelope);
		new UserDto().register(envelope);
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "ListingData", this.getClass());
		new ImageDto().registerRead(envelope);
		new UserDto().registerRead(envelope);
	}


}
