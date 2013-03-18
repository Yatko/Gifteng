package com.venefica.auth;

/**
 * The exception is thrown when an exception occurs during token encryption.
 *
 * @author Sviatoslav Grebenchukov
 */
@SuppressWarnings("serial")
public class TokenEncryptionException extends Exception {

    public TokenEncryptionException(Throwable e) {
        super(e);
    }
}
