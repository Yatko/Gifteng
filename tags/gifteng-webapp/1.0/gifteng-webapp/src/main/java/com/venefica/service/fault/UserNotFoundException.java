package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The exception is thrown when no users found by the specified criteria.
 *
 * @author Sviatoslav Grebenchukov
 */
@SuppressWarnings("serial")
@WebFault(name = "UserNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}
