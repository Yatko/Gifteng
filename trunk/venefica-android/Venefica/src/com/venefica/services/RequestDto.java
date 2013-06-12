/**
 * 
 */
package com.venefica.services;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.module.user.UserDto;
import com.venefica.services.AdDto.AdStatus;
import com.venefica.services.AdDto.AdType;
import com.venefica.utils.Constants;


/**
 * @author avinash
 *
 */
public class RequestDto implements KvmSerializable {

	public enum RequestStatus {
	    
	    PENDING, //REQUESTED - giver didn't make a decision
	    EXPIRED, //REJECTED - if someone else selected
	    ACCEPTED, //selected
	    ;
	}
	private Long id;
    private Long adId;
    private UserDto user;
    private Date requestedAt;
    private RequestStatus status;
    private ImageDto image;
    private ImageDto imageThumbnail;
    private AdType type;
    private AdStatus adStatus;
    private Date adExpiresAt;
	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getProperty(int)
	 */
	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return id;
		case 1:
			return adId;
		case 2:
			return user;
		case 3:
			return requestedAt != null ? requestedAt.getTime() : 0;
		case 4:
			if (status != null)
				return status.name();
			else
				return null;
		case 5:
			return image;
		case 6:
			return imageThumbnail;
		case 7:
			if (type != null)
				return type.name();
			else
				return null;
		case 8:
			if (adStatus != null)
				return adStatus.name();
			else
				return null;
		case 9:
			return adExpiresAt != null ? adExpiresAt.getTime() : 0;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyCount()
	 */
	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public void setProperty(int index, Object value) {
		try
		{
			switch (index)
			{
				case 0:
					id = Long.valueOf(value.toString());
					break;
				case 1:
					adId = Long.valueOf(value.toString());
					break;
				case 2:
					user = (UserDto)value;
					break;
				case 3:
					requestedAt = new Date(Long.parseLong(value.toString()));
					break;
				case 4:
					status = RequestStatus.valueOf(value.toString());
					break;	
				case 5:
					image = (ImageDto)value;
					break;
				case 6:
					imageThumbnail = (ImageDto)value;
					break;
				case 7:
					type = AdType.valueOf(value.toString());
					break;
				case 8:
					adStatus = AdStatus.valueOf(value.toString());
					break;
				case 9:
					adExpiresAt = new Date(Long.parseLong(value.toString()));
					break;
			}
		}catch (Exception e){
			Log.d("AdDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyInfo(int, java.util.Hashtable, org.ksoap2.serialization.PropertyInfo)
	 */
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings ("rawtypes") Hashtable properties, PropertyInfo info) {
		switch (index)
		{
			case 0:
				info.name = "id";
				info.type = Long.TYPE;
				break;
			case 1:
				info.name = "adId";
				info.type = Long.TYPE;
				break;
			case 2:
				info.name = "user";
				info.type = UserDto.class;
				break;
			case 3:
				info.name = "requestedAt";
				info.type = String.class;
				break;
			case 4:
				info.name = "status";
				info.type = RequestStatus.class;
				break;
			case 5:
				info.name = "image";
				info.type = ImageDto.class;
				break;
			case 6:
				info.name = "imageThumbnail";
				info.type = ImageDto.class;
				break;
			case 7:
				info.name = "type";
				info.type = AdType.class;
				break;
			case 8:
				info.name = "adStatus";
				info.type = AdStatus.class;
				break;
			case 9:
				info.name = "adExpiresAt";
				info.type = String.class;
				break;
		}
	}
	public void register(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
		new UserDto().register(envelope);
		new ImageDto().register(envelope);
	}

	public void registerRead(SoapSerializationEnvelope envelope)
	{
		envelope.addMapping(null, "RequestDto", this.getClass());
		new UserDto().registerRead(envelope);
		new ImageDto().registerRead(envelope);
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the adId
	 */
	public Long getAdId() {
		return adId;
	}

	/**
	 * @param adId the adId to set
	 */
	public void setAdId(Long adId) {
		this.adId = adId;
	}

	/**
	 * @return the user
	 */
	public UserDto getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserDto user) {
		this.user = user;
	}

	/**
	 * @return the requestedAt
	 */
	public Date getRequestedAt() {
		return requestedAt;
	}

	/**
	 * @param requestedAt the requestedAt to set
	 */
	public void setRequestedAt(Date requestedAt) {
		this.requestedAt = requestedAt;
	}

	/**
	 * @return the status
	 */
	public RequestStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(RequestStatus status) {
		this.status = status;
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
	 * @return the adStatus
	 */
	public AdStatus getAdStatus() {
		return adStatus;
	}

	/**
	 * @param adStatus the adStatus to set
	 */
	public void setAdStatus(AdStatus adStatus) {
		this.adStatus = adStatus;
	}

	/**
	 * @return the adExpiresAt
	 */
	public Date getAdExpiresAt() {
		return adExpiresAt;
	}

	/**
	 * @param adExpiresAt the adExpiresAt to set
	 */
	public void setAdExpiresAt(Date adExpiresAt) {
		this.adExpiresAt = adExpiresAt;
	}

}
