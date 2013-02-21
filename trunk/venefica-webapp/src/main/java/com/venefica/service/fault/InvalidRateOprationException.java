package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

/**
 * The exception is thrown when a user tries to rate his own ad.
 *
 * @author Sviatoslav Grebenchukov
 */
@WebFault(name = "InvalidRateOperationError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class InvalidRateOprationException extends Exception {

    public InvalidRateOprationException(String message) {
        super(message);
    }
}
