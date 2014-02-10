/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Ad;
import com.venefica.model.Image;
import com.venefica.model.Request;
import com.venefica.model.RequestStatus;
import com.venefica.model.Shipping;
import com.venefica.model.User;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingDto extends DtoBase {
    
    // in, out
    private Long id;
    // out
    private Long requestId;
    // out
    private Long adId;
    
    // out
    private Long creatorId;
    // out
    private String creatorName;
    // out
    private String creatorFullName;
    
    // out
    private Long receiverId;
    // out
    private String receiverName;
    // out
    private String receiverFullName;
    
    // out
    private Date acceptedAt;
    
    // in, out
    private BigDecimal receivedAmount;
    // in, out
    private String trackingNumber;
    // in, out
    private ImageDto barcodeImage;
    
    // out
    private boolean emailCreatorSent;
    // out
    private boolean emailReceiverSent;
    
    // out
    private RequestStatus requestStatus;
    
    // Required for JAX-WS
    public ShippingDto() {
    }
    
    public ShippingDto(Shipping shipping) {
        Request request = shipping.getRequest();
        Ad ad = request.getAd();
        User creator = ad.getCreator();
        User receiver = request.getUser();
        Image barcode = shipping.getBarcodeImage();
        
        this.id = shipping.getId();
        this.adId = ad.getId();
        this.requestId = request.getId();
        this.creatorId = creator.getId();
        this.creatorName = creator.getName();
        this.creatorFullName = creator.getFullName();
        this.receiverId = receiver.getId();
        this.receiverName = receiver.getName();
        this.receiverFullName = receiver.getFullName();
        this.acceptedAt = request.getAcceptedAt();
        this.receivedAmount = shipping.getReceivedAmount();
        this.trackingNumber = shipping.getTrackingNumber();
        this.barcodeImage = barcode != null ? new ImageDto(barcode) : null;
        this.emailCreatorSent = shipping.isEmailCreatorSent();
        this.emailReceiverSent = shipping.isEmailReceiverSent();
        this.requestStatus = request.getStatus();
    }
    
    // getter/setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }

    public Date getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Date acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public ImageDto getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(ImageDto barcodeImage) {
        this.barcodeImage = barcodeImage;
    }

    public boolean isEmailCreatorSent() {
        return emailCreatorSent;
    }

    public void setEmailCreatorSent(boolean emailCreatorSent) {
        this.emailCreatorSent = emailCreatorSent;
    }

    public boolean isEmailReceiverSent() {
        return emailReceiverSent;
    }

    public void setEmailReceiverSent(boolean emailReceiverSent) {
        this.emailReceiverSent = emailReceiverSent;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
