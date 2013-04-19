/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

/**
 *
 * @author gyuszi
 */
public class MailException extends Exception {
    
    public static final int GENERAL_ERROR           = 0;
    //email sending related errors
    public static final int INVALID_FROM_ADDRESS    = 1;
    public static final int INVALID_TO_ADDRESS      = 2;
    public static final int INVALID_EMAIL_MESSAGE   = 3;
    public static final int EMAIL_SEND_ERROR        = 4;
    //MailJimp related errors
    public static final int ALREADY_SUBSCRIBED      = 11;
    
    private int errorCode;
    
    public MailException(String message) {
        super(message);
    }
    
    public MailException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public MailException(int errorCode, Throwable th) {
        super(th);
        this.errorCode = errorCode;
    }
    
    public MailException(int errorCode, String message, Throwable th) {
        super(message, th);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
