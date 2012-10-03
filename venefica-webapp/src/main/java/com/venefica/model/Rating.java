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
 * Advertisement rating.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Entity
@SequenceGenerator(name = "rating_gen", sequenceName = "rating_seq", allocationSize = 1)
@Table(name = "rating")
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_gen")
	private Long id;

	@ManyToOne
	@ForeignKey(name = "rating_ad_fk")
	private Ad ad;

	@ManyToOne
	@ForeignKey(name = "rating_usr_fk")
	private User user;

	private int value;

	public Rating() {
		value = 0;
	}

	public Rating(Ad ad, User user, int value) {
		this.ad = ad;
		this.user = user;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
