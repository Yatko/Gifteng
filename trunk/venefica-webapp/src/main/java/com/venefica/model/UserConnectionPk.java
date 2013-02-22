package com.venefica.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Composite primary key for ${link UserConnection} entity.
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
        super();
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserConnectionPk) {
            UserConnectionPk other = (UserConnectionPk) obj;

            return userId.equals(other.userId) && providerId.equals(other.providerId)
                    && providerUserId.equals(other.providerUserId);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return userId.hashCode() * 1000 + providerId.hashCode() * 100 + providerUserId.hashCode();
    }
}
