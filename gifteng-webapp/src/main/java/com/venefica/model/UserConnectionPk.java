package com.venefica.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Composite primary key for {@link UserConnection} entity.
 *
 * @author Sviatoslav Grebenchukov
 */
@Embeddable
@SuppressWarnings("serial")
public class UserConnectionPk implements Serializable {

    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String providerId;
    
    @Column(nullable = false)
    private String providerUserId;

    public UserConnectionPk() {
    }

    public UserConnectionPk(String userId, String providerId, String providerUserId) {
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserConnectionPk)) {
            return false;
        }

        UserConnectionPk other = (UserConnectionPk) obj;
        return userId.equals(other.getUserId())
                && providerId.equals(other.getProviderId())
                && providerUserId.equals(other.getProviderUserId()); //the getter usage is a must as proxies needs to be activated
    }

    @Override
    public int hashCode() {
        return userId.hashCode() * 1000 + providerId.hashCode() * 100 + providerUserId.hashCode();
    }

    public String getUserId() {
        return userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }
}
