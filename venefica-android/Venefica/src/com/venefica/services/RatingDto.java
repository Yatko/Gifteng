package com.venefica.services;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.venefica.module.user.UserDto;
import com.venefica.utils.Constants;

/**
 * @author avinash Class to transfer/hold Review Data
 */
public class RatingDto implements KvmSerializable {

	private Long adId;
    private String text;
    private int value;
    private Long toUserId;
    private Date ratedAt;
    private UserDto fromUser;
    private UserDto toUser;

	public RatingDto() {
	}	

	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return adId;
		case 1:
			return text;
		case 2:
			return value;
		case 3:
			return toUserId;
		case 4:
			return ratedAt;
		case 5:
			return fromUser;
		case 6:
			return toUser;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 7;
	}

	@Override
	public void setProperty(int index, Object value) {
		try {
			switch (index) {
			case 0:
				adId = Long.valueOf(value.toString());
				break;
			case 1:
				text = String.valueOf(value);
				break;
			case 2:
				this.value = Integer.valueOf(value.toString());
				break;
			case 3:
				toUserId = Long.valueOf(value.toString());
				break;
			case 4:
				ratedAt = new Date(Long.parseLong(value.toString()));
				break;
			case 5:
				fromUser = (UserDto) value;
				break;
			case 6:
				toUser = (UserDto) value;
				break;
			}
		} catch (Exception e) {
			Log.d("RatingDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	@Override
	public void getPropertyInfo(int index,
			@SuppressWarnings("rawtypes") Hashtable properties,
			PropertyInfo info) {
		switch (index) {
		case 0:
			info.name = "adId";
			info.type = Long.class;
			break;

		case 1:
			info.name = "text";
			info.type = String.class;
			break;

		case 2:
			info.name = "value";
			info.type = Integer.class;
			break;

		case 3:
			info.name = "toUserId";
			info.type = Long.class;
			break;
			
		case 4:
			info.name = "ratedAt";
			info.type = Long.class;
			break;
			
		case 5:
			info.name = "fromUser";
			info.type = UserDto.class;
			break;
			
		case 6:
			info.name = "toUser";
			info.type = UserDto.class;
			break;
		}
	}
	public void register(SoapSerializationEnvelope envelope){
		envelope.addMapping(Constants.SERVICES_NAMESPACE, this.getClass().getName(), this.getClass());
	}

	public void registerRead(SoapSerializationEnvelope envelope){
		envelope.addMapping(null, "RatingDto", this.getClass());
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the toUserId
	 */
	public Long getToUserId() {
		return toUserId;
	}

	/**
	 * @param toUserId the toUserId to set
	 */
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * @return the ratedAt
	 */
	public Date getRatedAt() {
		return ratedAt;
	}

	/**
	 * @param ratedAt the ratedAt to set
	 */
	public void setRatedAt(Date ratedAt) {
		this.ratedAt = ratedAt;
	}

	/**
	 * @return the fromUser
	 */
	public UserDto getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser the fromUser to set
	 */
	public void setFromUser(UserDto fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @return the toUser
	 */
	public UserDto getToUser() {
		return toUser;
	}

	/**
	 * @param toUser the toUser to set
	 */
	public void setToUser(UserDto toUser) {
		this.toUser = toUser;
	}
}
