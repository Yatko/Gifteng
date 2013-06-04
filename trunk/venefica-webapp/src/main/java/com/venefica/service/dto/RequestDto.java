/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.AdType;
import com.venefica.model.Request;
import com.venefica.model.RequestStatus;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestDto extends DtoBase {
    
    // out
    private Long id;
    // out
    private Long adId;
    // out
    private UserDto user;
    // out
    private Date requestedAt;
    // out
    private RequestStatus status;
    
    // out
    private AdType type;
    // out
    private ImageDto image;
    // out
    private ImageDto imageThumbnail;

    public RequestDto() {
    }
    
    public RequestDto(Request request) {
        this.id = request.getId();
        this.adId = request.getAd().getId();
        this.user = new UserDto(request.getUser());
        this.requestedAt = request.getRequestedAt();
        this.status = request.getStatus();
        this.type = request.getAd().getType();
        
        if (request.getAd().getAdData().getMainImage() != null) {
            this.image = new ImageDto(request.getAd().getAdData().getMainImage());
        }
        if (request.getAd().getAdData().getThumbImage()!= null) {
            this.imageThumbnail = new ImageDto(request.getAd().getAdData().getThumbImage());
        }
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public ImageDto getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(ImageDto imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }
    
}
