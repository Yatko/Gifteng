package com.venefica.service.dto;

import com.venefica.model.Comment;
import com.venefica.model.Image;
import com.venefica.model.User;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Comment data transfer object.
 *
 * @author Sviatoslav Grebenchukov
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentDto extends DtoBase {

    // in, out
    private Long id;
    // in, out
    @NotNull
    private String text;
    // out
    private boolean owner;
    // out
    private String publisherName;
    // out
    private String publisherFullName;
    // out
    private String publisherAvatarUrl;
    // out
    private Date createdAt;

    // required for JAX-WS
    public CommentDto() {
    }

    public CommentDto(String text) {
        this.text = text;
    }

    public CommentDto(Comment comment, User currentUser) {
        Image avatar = comment.getPublisher().getAvatar();
        
        id = comment.getId();
        text = comment.getText();
        owner = comment.getPublisher().equals(currentUser);
        publisherName = comment.getPublisher().getName();
        publisherFullName = comment.getPublisher().getFullName();
        publisherAvatarUrl = ImageDto.imageUrl(avatar);
        createdAt = comment.getCreatedAt();
    }

    public void update(Comment comment) {
        comment.setText(text);
    }

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

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherFullName() {
        return publisherFullName;
    }

    public void setPublisherFullName(String publisherFullName) {
        this.publisherFullName = publisherFullName;
    }

    public String getPublisherAvatarUrl() {
        return publisherAvatarUrl;
    }

    public void setPublisherAvatarUrl(String publisherAvatarUrl) {
        this.publisherAvatarUrl = publisherAvatarUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
