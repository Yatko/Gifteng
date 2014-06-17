/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Share;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ShareDto extends DtoBase {
    
    // out
    private Long id;
    // in
    private Long adId;
    // in, out
    private String email1;
    // in, out
    private String email2;
    // in, out
    private String email3;
    // in, out
    private String message;
    // in, out
    private String fromName;
    // in, out
    private String fromEmail;
    // out
    private AdDto adDto;

    public ShareDto() {
    }

    public ShareDto(Share share) {
        this.id = share.getId();
        this.adId = share.getAd().getId();
        this.email1 = share.getEmail1();
        this.email2 = share.getEmail2();
        this.email3 = share.getEmail3();
        this.message = share.getMessage();
        this.fromName = share.getFromName();
        this.fromEmail = share.getFromEmail();
    }
    
    // getter/setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public AdDto getAdDto() {
        return adDto;
    }

    public void setAdDto(AdDto adDto) {
        this.adDto = adDto;
    }
}
