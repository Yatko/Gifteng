/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Approval;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ApprovalDto extends DtoBase {
    
    // out
    private Long id;
    // out
    private Long adId;
    // out
    private Long deciderId;
    // out
    private String deciderFullName;
    // out
    private Date approvedAt;
    // out
    private String text;

    // Required for JAX-WS
    public ApprovalDto() {
    }
    
    public ApprovalDto(Approval approval) {
        this.id = approval.getId();
        this.adId = approval.getAd().getId();
        this.deciderId = approval.getDecider().getId();
        this.deciderFullName = approval.getDecider().getFullName();
        this.approvedAt = approval.getApprovedAt();
        this.text = approval.getText();
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

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDeciderId() {
        return deciderId;
    }

    public void setDeciderId(Long deciderId) {
        this.deciderId = deciderId;
    }

    public String getDeciderFullName() {
        return deciderFullName;
    }

    public void setDeciderFullName(String deciderFullName) {
        this.deciderFullName = deciderFullName;
    }
    
}
