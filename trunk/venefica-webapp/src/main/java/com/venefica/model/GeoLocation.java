package com.venefica.model;

import javax.persistence.Embeddable;

/**
 * Describes a geographic location.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Embeddable
public class GeoLocation {
	private Long latitude;
	private Long longitude;

	public GeoLocation() {
	}

	public GeoLocation(Long latitude, Long longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}
}
