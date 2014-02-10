package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The exception is thrown when no invitation found by the specified criteria.
 *
 * @author gyuszi
 */
@SuppressWarnings("serial")
@WebFault(name = "InvitationNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
public class InvitationNotFoundException extends Exception {

    public InvitationNotFoundException(String message) {
        super(message);
    }
}
