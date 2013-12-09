package com.venefica.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Describes an image stored in the database.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private ImageType imgType;
    
//    @javax.persistence.Lob
//    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
//    @javax.persistence.Column(name = "dataa")
//    @org.hibernate.annotations.Type(type = "org.hibernate.type.PrimitiveByteArrayBlobType")
    @Transient
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
