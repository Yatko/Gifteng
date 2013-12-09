package com.venefica.auth;

import com.venefica.config.Constants;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

/**
 * Represents an authentication token.
 *
 * @author Sviatoslav Grebenchukov
 */
public class Token implements Serializable {

    private static final long serialVersionUID = -5601354813824855907L;
    
    private Long userId;
    private Date expiresAt;

    public Token(Long userId, Date expiresAt) {
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public Token(Long userId) {
        this(userId, DateUtils.addDays(new Date(), Constants.TOKEN_EXPIRES_IN_DAYS));
    }

    public Long getUserId() {
        return userId;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        Date curDate = new Date();
        return curDate.after(expiresAt);
    }
}
