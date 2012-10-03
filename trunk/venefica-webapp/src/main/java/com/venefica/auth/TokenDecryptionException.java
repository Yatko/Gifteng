package com.venefica.auth;

/**
 * The exception is thrown when an exception occurs during token decryption.
 * 
 * @author Sviatoslav Grebenchukov
 */
@SuppressWarnings("serial")
public class TokenDecryptionException extends Exception {
	public TokenDecryptionException(Throwable e) {
		super(e);
	}
}
