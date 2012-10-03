package com.venefica.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Describes an image stored in the database.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "image")
@SequenceGenerator(name = "img_gen", sequenceName = "img_seq", allocationSize = 1)
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "img_gen")
	private Long id;
	@Enumerated(EnumType.STRING)
	private ImageType imgType;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] data;

	public Image() {
	}

	public Image(ImageType imgType, byte[] data) {
		this.imgType = imgType;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public ImageType getImgType() {
		return imgType;
	}

	public void setImgType(ImageType imgType) {
		this.imgType = imgType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
