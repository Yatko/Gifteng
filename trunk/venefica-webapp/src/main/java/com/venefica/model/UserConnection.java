package com.venefica.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Describes a connection between a social network user and a local one.
 *
 * @author Sviatoslav Grebenchukov
 */
@Entity
@Table(name = "userconnection", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"userId", "providerId", "rank"})
})
public class UserConnection {

    // create table UserConnection (userId varchar(255) not null,
    // providerId varchar(255) not null,
    // providerUserId varchar(255),
    // rank int not null,
    // displayName varchar(255),
    // profileUrl varchar(512),
    // imageUrl varchar(512),
    // accessToken varchar(255) not null,
    // secret varchar(255),
    // refreshToken varchar(255),
    // expireTime bigint,
    // primary key (userId, providerId, providerUserId));
    //
    // create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
    
    @EmbeddedId
    private UserConnectionPk id;
    
    private int rank;
    
    private String displayName;
    
    private String profileUrl;
    
    private String imageUrl;
    
    @Column(nullable = false)
    private String accessToken;
    
    private String secret;
    
    private String refreshToken;
    
    private Long expireTime;

    public UserConnection() {
    }

    public UserConnectionPk getId() {
        return id;
    }

    public void setId(UserConnectionPk id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
