package com.venefica.auth;

import com.venefica.model.User;

/**
 * Represents information about an authenticated user.
 * 
 * @author Sviatoslav Grebenchukov
 * 
 */
public class SecurityContext {

	private User user;

	public SecurityContext(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getUserId() {
		return user != null ? user.getId() : null;
	}
}
