package com.venefica.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.module.user.UserDto;
import com.venefica.utils.Constants;

public class AdDto implements KvmSerializable
{
	/**
	 * days
	 * @author avinash
	 */
	public enum WeekDay {
	    
	    MONDAY,
	    TUESDAY,
	    WEDNESDAY,
	    THURSDAY,
	    FRIDAY,
	    SATURDAY,
	    SUNDAY,
	    
	}
	/**
	 * The type of the ad. A MEMBER typed ad will have it's creator also a member
	 * user.
	 * 
	 * @author avinash
	 */
	public enum AdType {
	    
	    MEMBER,
	    BUSINESS,
	    
	}
	/**
	 * The ad place.
	 * 
	 * @author avinash
	 */
	public enum AdPlace {
	    
	    ONLINE,
	    LOCATION,
	    
	}
	/**
	 * The 'lifecycle' of an ad.
	 * 
	 * The following possible next statuses are allowed:
	 * - ACTIVE -> EXPIRED, SELECTED
	 * - EXPIRED -> ACTIVE, SELECTED
	 * - SELECTED -> SENT, ACTIVE
	 * - SENT -> RECEIVED, ACTIVE
	 * - RECEIVED
	 */
	public enum AdStatus {
	    
	    ACTIVE,
	    EXPIRED,
	    SELECTED,
	    SENT,
	    RECEIVED,
	    
	}
	private long id;
	private long categoryId;
	private String category = "";
	private String title = "";
	private String description = "";
	private BigDecimal price = new BigDecimal(0.0f);
//	private double latitude;
//	private double longitude;
	private ImageDto image;
	/** takes milliseconds */
	private String createdAt = "";
	/** true - the current user is the owner of ad*/
	private boolean owner;
	private ImageDto imageThumbnail;
	private boolean inBookmars;
	private Date expiresAt;
//	private boolean wanted;
	private long numViews;
	private UserDto creator;
	private boolean expired;
	private int numAvailProlongations;
	private boolean canMarkAsSpam;
	private float rating;
	private boolean canRate;
	private List<ImageDto> images;
	private boolean sent;
    private boolean received;
    private boolean requested;
    private Boolean freeShipping;
    private Boolean pickUp;
    private int quantity;
    private String subtitle;
    private AddressDto address;
    private List<CommentDto> comments;    
    
    private Boolean expires; //never expire?
    private Date availableAt;
    private AdPlace place;
    private AdType type;
     
    // business ad data
    
    private String promoCode;
    private String website;
    private Boolean needsReservation;
    private Date availableFromTime;
    private Date availableToTime;
    private Boolean availableAllDay;
    private Set<WeekDay> availableDays;
    private AdStatus status;
	public AdDto()
	{

	}

	
	public Object getProperty(int index)
	{
		switch (index)
		{
			case 0:
				return id;
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
//			case 6:
//				return String.valueOf(latitude);
//			case 7:
//				return String.valueOf(longitude);
			case 6:
				return image;
			case 7:
				return createdAt;
			case 8:
				return owner;
			case 9:
				return imageThumbnail;
			case 10:
				return inBookmars;
			case 11:
				return expiresAt != null ? expiresAt.getTime() : 0;
//			case 14:
//				return wanted;
			case 12:
				return numViews;
			case 13:
				return creator;
			case 14:
				return expired;
			case 15:
				return numAvailProlongations;
			case 16:
				return canMarkAsSpam;
			case 17:
				return String.valueOf(rating);
			case 18:
				return canRate;
			case 19:
				return null; //images no send
			case 20:
				return sent;
			case 21:
				return received;
			case 22:
				return requested;
			case 23:
				return freeShipping;
			case 24:
				return pickUp;
			case 25:
				return quantity;
			case 26:
				return subtitle;
			case 27:
				return address;
			case 28:
				return comments;
			case 29:
				return expires;
			case 30:
				return availableAt != null ? availableAt.getTime() : 0;
			case 31:
				if (place != null)
					return place.name();
				else
					return null;
			case 32:
				if (type != null)
					return type.name();
				else
					return null;			
			case 33:
				return promoCode;
			case 34:
				return website;
			case 35:
				return needsReservation;
			case 36:
				return availableFromTime;
			case 37:
				return availableToTime;
			case 38:
				return availableAllDay;
			case 39:
				return availableDays;
			case 40:
				if (status != null)
					return status.name();
				else
					return null;
		}

		return null;
	}

	public int getPropertyCount()
	{
		return 41;
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

//			case 6:
//				info.name = "latitude";
//				info.type = Double.TYPE;
//				break;
//
//			case 7:
//				info.name = "longitude";
//				info.type = Double.TYPE;
//				break;

			case 6:
				info.name = "image";
				info.type = ImageDto.class;
				break;

			case 7:
				info.name = "createdAt";
				info.type = String.class;
				break;

			case 8:
				info.name = "owner";
				info.type = Boolean.class;
				break;

			case 9:
				info.name = "imageThumbnail";
				info.type = ImageDto.class;
				break;

			case 10:
				info.name = "inBookmars";
				info.type = Boolean.class;
				break;

			case 11:
				info.name = "expiresAt";
				info.type = Long.class;
				break;

//			case 14:
//				info.name = "wanted";
//				info.type = Boolean.class;
//				break;

			case 12:
				info.name = "numViews";
				info.type = Long.class;
				break;

			case 13:
				info.name = "creator";
				info.type = UserDto.class;
				break;

			case 14:
				info.name = "expired";
				info.type = Boolean.class;
				break;

			case 15:
				info.name = "numAvailProlongations";
				info.type = Integer.class;
				break;

			case 16:
				info.name = "canMarkAsSpam";
				info.type = Boolean.class;
				break;

			case 17:
				info.name = "rating";
				info.type = Float.class;
				break;

			case 18:
				info.name = "canRate";
				info.type = Boolean.class;
				break;

			case 19:
				info.name = "images";
				info.type = new Vector<ImageDto>().getClass();
				break;
			case 20:
				info.name = "sent";
				info.type = Boolean.class;
				break;
			case 21:
				info.name = "received";
				info.type = Boolean.class;
				break;
			case 22:
				info.name = "requested";
				info.type = Boolean.class;
				break;
			case 23:
				info.name = "freeShipping";
				info.type = Boolean.class;
				break;
			case 24:
				info.name = "pickUp";
				info.type = Boolean.class;
				break;
			case 25:
				info.name = "quantity";
				info.type = Integer.class;
				break;
			case 26:
				info.name = "subtitle";
				info.type = String.class;
				break;
			case 27:
				info.name = "address";
				info.type = AddressDto.class;
				break;
			case 28:
				info.name = "comments";
				info.type = new Vector<CommentDto>().getClass();
				break;
			case 29:
				info.name = "expires";
				info.type = Boolean.class;
				break;				
			case 30:
				info.name = "availableAt";
				info.type = String.class;
				break;
			case 31:
				info.name = "place";
				info.type = String.class;
				break;
			case 32:
				info.name = "type";
				info.type = AdType.class;
				break;
			case 33:
				info.name = "promoCode";
				info.type = String.class;
				break;
			case 34:
				info.name = "website";
				info.type = String.class;
				break;
			case 35:
				info.name = "needsReservation";
				info.type = Boolean.class;
				break;
			case 36:
				info.name = "availableFromTime";
				info.type = String.class;
				break;
			case 37:
				info.name = "availableToTime";
				info.type = String.class;
				break;
			case 38:
				info.name = "availableAllDay";
				info.type = Boolean.class;
				break;
			case 39:
				info.name = "availableDays";
				info.type = new Vector<WeekDay>().getClass();
				break;
			case 40:
				info.name = "status";
				info.type = AdStatus.class;
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
//			System.out.println("AdDto index"+index);
			switch (index)
			{
				case 0:
					id = Long.valueOf(value.toString());
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
//				case 6:
//					latitude = Double.valueOf(value.toString());
//					break;
//				case 7:
//					longitude = Double.valueOf(value.toString());
//					break;
				case 6:
					image = (ImageDto)value;
					break;
				case 7:
					createdAt = String.valueOf(value);
					break;
				case 8:
					owner = Boolean.parseBoolean(value.toString());
					break;
				case 9:
					imageThumbnail = (ImageDto)value;
					break;
				case 10:
					inBookmars = Boolean.parseBoolean(value.toString());
					break;
				case 11:
					expiresAt = new Date(Long.parseLong(value.toString()));
					break;
//				case 14:
//					wanted = Boolean.parseBoolean(value.toString());
//					break;
				case 12:
					numViews = Long.valueOf(value.toString());
					break;
				case 13:
					creator = (UserDto)value;
					break;
				case 14:
					expired = Boolean.parseBoolean(value.toString());
					break;
				case 15:
					numAvailProlongations = Integer.valueOf(value.toString());
					break;
				case 16:
					canMarkAsSpam = Boolean.parseBoolean(value.toString());
					break;
				case 17:
					rating = Float.valueOf(value.toString());
					break;
				case 18:
					canRate = Boolean.parseBoolean(value.toString());
					break;
				case 19:
					images = (Vector<ImageDto>)value;
					
					/*if(images == null)
						images = new ArrayList<ImageDto>();
					
					images.add((ImageDto)value);*/
					break;
				case 20:
					sent = Boolean.parseBoolean(value.toString());
					break;
				case 21:
					received = Boolean.parseBoolean(value.toString());
					break;
				case 22:
					requested = Boolean.parseBoolean(value.toString());
					break;
				case 23:
					freeShipping = Boolean.parseBoolean(value.toString());
					break;
				case 24:
					pickUp = Boolean.parseBoolean(value.toString());
					break;
				case 25:
					quantity = Integer.valueOf(value.toString());
					break;
				case 26:
					subtitle = String.valueOf(value);
					break;
				case 27:
					address = (AddressDto)value;
					break;
				case 28:
					comments = (List<CommentDto>)value;
					break;
				case 29:
					expires = Boolean.parseBoolean(value.toString());
					break;
				case 30:
					availableAt = new Date(Long.parseLong(value.toString()));
					break;
				case 31:
					place = AdPlace.valueOf(value.toString());
					break;
				case 32:
					type = 	AdType.valueOf(value.toString());
					break;
				case 33:
					promoCode = String.valueOf(value);
					break;
				case 34:
					website = String.valueOf(value);
					break;
				case 35:
					needsReservation = Boolean.parseBoolean(value.toString());
					break;
				case 36:
					availableFromTime = new Date(Long.parseLong(value.toString()));
					break;
				case 37:
					availableToTime = new Date(Long.parseLong(value.toString()));
					break;
				case 38:
					availableAllDay = Boolean.parseBoolean(value.toString());
					break;
				case 39:
					availableDays = (Set<AdDto.WeekDay>)value;
					break;
				case 40:
					status = AdStatus.valueOf(value.toString());
					break;
			}
		}catch (Exception e){
			Log.d("AdDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
		new ImageDto().register(envelope);
		new UserDto().register(envelope);
		new AddressDto().register(envelope);
		new CommentDto().register(envelope);
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "AdDto", this.getClass());
		new ImageDto().registerRead(envelope);
		new UserDto().registerRead(envelope);
		new AddressDto().registerRead(envelope);
		new CommentDto().registerRead(envelope);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 *//*
	public double getLatitude() {
		return latitude;
	}

	*//**
	 * @param latitude the latitude to set
	 *//*
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	*//**
	 * @return the longitude
	 *//*
	public double getLongitude() {
		return longitude;
	}

	*//**
	 * @param longitude the longitude to set
	 *//*
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}*/

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
	 * @return the owner
	 */
	public boolean isOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(boolean owner) {
		this.owner = owner;
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
	 * @return the inBookmars
	 */
	public boolean isInBookmars() {
		return inBookmars;
	}

	/**
	 * @param inBookmars the inBookmars to set
	 */
	public void setInBookmars(boolean inBookmars) {
		this.inBookmars = inBookmars;
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
	 * @return the wanted
	 */
	/*public boolean isWanted() {
		return wanted;
	}

	*//**
	 * @param wanted the wanted to set
	 *//*
	public void setWanted(boolean wanted) {
		this.wanted = wanted;
	}
*/
	/**
	 * @return the numViews
	 */
	public long getNumViews() {
		return numViews;
	}

	/**
	 * @param numViews the numViews to set
	 */
	public void setNumViews(long numViews) {
		this.numViews = numViews;
	}

	/**
	 * @return the creator
	 */
	public UserDto getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(UserDto creator) {
		this.creator = creator;
	}

	/**
	 * @return the expired
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
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

	/**
	 * @return the canMarkAsSpam
	 */
	public boolean isCanMarkAsSpam() {
		return canMarkAsSpam;
	}

	/**
	 * @param canMarkAsSpam the canMarkAsSpam to set
	 */
	public void setCanMarkAsSpam(boolean canMarkAsSpam) {
		this.canMarkAsSpam = canMarkAsSpam;
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
	 * @return the canRate
	 */
	public boolean isCanRate() {
		return canRate;
	}

	/**
	 * @param canRate the canRate to set
	 */
	public void setCanRate(boolean canRate) {
		this.canRate = canRate;
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
	 * @return the sent
	 */
	public boolean isSent() {
		return sent;
	}


	/**
	 * @param sent the sent to set
	 */
	public void setSent(boolean sent) {
		this.sent = sent;
	}


	/**
	 * @return the received
	 */
	public boolean isReceived() {
		return received;
	}


	/**
	 * @param received the received to set
	 */
	public void setReceived(boolean received) {
		this.received = received;
	}


	/**
	 * @return the requested
	 */
	public boolean isRequested() {
		return requested;
	}


	/**
	 * @param requested the requested to set
	 */
	public void setRequested(boolean requested) {
		this.requested = requested;
	}


	/**
	 * @return the freeShipping
	 */
	public Boolean getFreeShipping() {
		return freeShipping;
	}


	/**
	 * @param freeShipping the freeShipping to set
	 */
	public void setFreeShipping(Boolean freeShipping) {
		this.freeShipping = freeShipping;
	}


	/**
	 * @return the pickUp
	 */
	public Boolean getPickUp() {
		return pickUp;
	}


	/**
	 * @param pickUp the pickUp to set
	 */
	public void setPickUp(Boolean pickUp) {
		this.pickUp = pickUp;
	}


	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}


	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}


	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


	/**
	 * @return the address
	 */
	public AddressDto getAddress() {
		return address;
	}


	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDto address) {
		this.address = address;
	}


	/**
	 * @return the comments
	 */
	public List<CommentDto> getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<CommentDto> comments) {
		this.comments = comments;
	}


	/**
	 * @return the expires
	 */
	public Boolean getExpires() {
		return expires;
	}


	/**
	 * @param expires the expires to set
	 */
	public void setExpires(Boolean expires) {
		this.expires = expires;
	}


	/**
	 * @return the availableAt
	 */
	public Date getAvailableAt() {
		return availableAt;
	}


	/**
	 * @param availableAt the availableAt to set
	 */
	public void setAvailableAt(Date availableAt) {
		this.availableAt = availableAt;
	}


	/**
	 * @return the place
	 */
	public AdPlace getPlace() {
		return place;
	}


	/**
	 * @param place the place to set
	 */
	public void setPlace(AdPlace place) {
		this.place = place;
	}


	/**
	 * @return the type
	 */
	public AdType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(AdType type) {
		this.type = type;
	}


	/**
	 * @return the promoCode
	 */
	public String getPromoCode() {
		return promoCode;
	}


	/**
	 * @param promoCode the promoCode to set
	 */
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}


	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}


	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}


	/**
	 * @return the needsReservation
	 */
	public Boolean getNeedsReservation() {
		return needsReservation;
	}


	/**
	 * @param needsReservation the needsReservation to set
	 */
	public void setNeedsReservation(Boolean needsReservation) {
		this.needsReservation = needsReservation;
	}


	/**
	 * @return the availableFromTime
	 */
	public Date getAvailableFromTime() {
		return availableFromTime;
	}


	/**
	 * @param availableFromTime the availableFromTime to set
	 */
	public void setAvailableFromTime(Date availableFromTime) {
		this.availableFromTime = availableFromTime;
	}


	/**
	 * @return the availableToTime
	 */
	public Date getAvailableToTime() {
		return availableToTime;
	}


	/**
	 * @param availableToTime the availableToTime to set
	 */
	public void setAvailableToTime(Date availableToTime) {
		this.availableToTime = availableToTime;
	}


	/**
	 * @return the availableAllDay
	 */
	public Boolean getAvailableAllDay() {
		return availableAllDay;
	}


	/**
	 * @param availableAllDay the availableAllDay to set
	 */
	public void setAvailableAllDay(Boolean availableAllDay) {
		this.availableAllDay = availableAllDay;
	}


	/**
	 * @return the availableDays
	 */
	public Set<WeekDay> getAvailableDays() {
		return availableDays;
	}


	/**
	 * @param availableDays the availableDays to set
	 */
	public void setAvailableDays(Set<WeekDay> availableDays) {
		this.availableDays = availableDays;
	}


	/**
	 * @return the status
	 */
	public AdStatus getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(AdStatus status) {
		this.status = status;
	}
}
