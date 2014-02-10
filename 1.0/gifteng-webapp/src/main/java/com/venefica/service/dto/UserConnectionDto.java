/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.service.dto;

import com.venefica.model.Provider;
import com.venefica.model.UserConnection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.springframework.social.facebook.api.FacebookProfile;

/**
 *
 * @author gyuszi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserConnectionDto extends DtoBase {
    
    // out
    private Long userId;
    // out
    private Provider provider;
    // out
    private String displayName;
    // out
    private String profileUrl;
    // out
    private String imageUrl;
    
    // out
    private String providerUserId;
    // out
    private String about;
    // out
    private String bio;
    // out
    private String name;
    // out
    private String gender;
    // out
    private String birthday;

    public UserConnectionDto() {
    }
    
    public UserConnectionDto(Provider provider) {
        this.provider = provider;
    }
    
    public UserConnectionDto(UserConnection userConnection) {
        userId = Long.valueOf(userConnection.getId().getUserId());
        provider = userConnection.getId().getProviderId();
        providerUserId = userConnection.getId().getProviderUserId();
        displayName = userConnection.getDisplayName();
        profileUrl = userConnection.getProfileUrl();
        imageUrl = userConnection.getImageUrl();
    }

    public void update(FacebookProfile profile) {
        about = profile.getAbout();
        bio = profile.getBio();
        name = profile.getName();
        gender = profile.getGender();
        birthday = profile.getBirthday();
        providerUserId = (providerUserId == null ? profile.getId() : providerUserId);
    }
    
    // getter/setter
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
