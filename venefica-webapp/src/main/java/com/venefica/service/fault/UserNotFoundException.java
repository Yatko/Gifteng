package com.venefica.service.fault;

/**
 * The exception is thrown when no users found by the specified criteria.
 *
 * @author Sviatoslav Grebenchukov
 */
@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}
