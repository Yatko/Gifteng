package com.venefica.services;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.util.Log;

import com.venefica.module.user.UserDto;

/**
 * @author avinash Class to transfer/hold Review Data
 */
public class ReviewDto implements KvmSerializable {

	/*
	 * Review text
	 */
	private String text;
	/**
	 * Review by
	 */
	private UserDto from;
	/**
	 * Review for
	 */
	private UserDto to;
	/**
	 * date time
	 */
	private Date reviewedAt;

	public ReviewDto() {
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the from
	 */
	public UserDto getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(UserDto from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public UserDto getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(UserDto to) {
		this.to = to;
	}

	/**
	 * @return the reviewedAt
	 */
	public Date getReviewedAt() {
		return reviewedAt;
	}

	/**
	 * @param reviewedAt
	 *            the reviewedAt to set
	 */
	public void setReviewedAt(Date reviewedAt) {
		this.reviewedAt = reviewedAt;
	}

	@Override
	public Object getProperty(int index) {
		switch (index) {
		case 0:
			return text;
		case 1:
			return from;
		case 2:
			return reviewedAt.getTime();
		case 3:
			return to;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@Override
	public void setProperty(int index, Object value) {
		try {
			switch (index) {
			case 0:
				text = String.valueOf(value);
				break;
			case 1:
				from = (UserDto) value;
				break;
			case 2:
				reviewedAt = new Date(Long.parseLong(value.toString()));
				break;
			case 3:
				to = (UserDto) value;
				break;

			}
		} catch (Exception e) {
			Log.d("AdDto.setProperty Exception:", e.getLocalizedMessage());
		}
	}

	@Override
	public void getPropertyInfo(int index,
			@SuppressWarnings("rawtypes") Hashtable properties,
			PropertyInfo info) {
		switch (index) {
		case 0:
			info.name = "text";
			info.type = String.class;
			break;

		case 1:
			info.name = "from";
			info.type = UserDto.class;
			break;

		case 2:
			info.name = "reviewedAt";
			info.type = Long.class;
			break;

		case 3:
			info.name = "to";
			info.type = UserDto.class;
			break;
		}
	}

}
