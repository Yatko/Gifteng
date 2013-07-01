package com.venefica.service.dto;

import com.venefica.model.Message;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Message data transfer object.
 *
 * @author Sviatoslav Grebenchukov
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageDto extends DtoBase {

    // in, out
    private Long id;
    // in, out
    private Long adId;
    // in, out
    private String text;
    // out
    private boolean owner;
    // out, in
    private Long toId;
    // out, in
    private String toName;
    // out
    private String toFullName;
    // out
    private String toAvatarUrl;
    // out
    private Long fromId;
    // out
    private String fromName;
    // out
    private String fromFullName;
    // out
    private String fromAvatarUrl;
    // out
    private boolean read;
    // out
    private Date createdAt;

    // required for JAX-WS
    public MessageDto() {
    }

    public MessageDto(String toName, String text) {
        this.toName = toName;
        this.text = text;
    }

    // getters/setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToFullName() {
        return toFullName;
    }

    public void setToFullName(String toFullName) {
        this.toFullName = toFullName;
    }

    public String getToAvatarUrl() {
        return toAvatarUrl;
    }

    public void setToAvatarUrl(String toAvatarUrl) {
        this.toAvatarUrl = toAvatarUrl;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromFullName() {
        return fromFullName;
    }

    public void setFromFullName(String fromFullName) {
        this.fromFullName = fromFullName;
    }

    public String getFromAvatarUrl() {
        return fromAvatarUrl;
    }

    public void setFromAvatarUrl(String fromAvatarUrl) {
        this.fromAvatarUrl = fromAvatarUrl;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }
}
