package com.venefica.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

/**
 * Describes a bookmark to an ad.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Entity
@SequenceGenerator(name = "bookmark_gen", sequenceName = "bookmark_seq", allocationSize = 1)
@Table(name = "bookmark")
public class Bookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_gen")
	private Long id;

	@ManyToOne(optional = false)
	@ForeignKey(name = "bookmark_user_fk")
	private User user;

	@ManyToOne(optional = false)
	@ForeignKey(name = "bookmark_ad_fk")
	private Ad ad;

	// Required for hibernate
	public Bookmark() {
	}

	public Bookmark(User user, Ad ad) {
		this.user = user;
		this.ad = ad;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}	
}
