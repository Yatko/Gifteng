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
@WebFault(name = "ShippingNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingNotFoundException extends Exception {

    public ShippingNotFoundException(String message) {
        super(message);
    }
    
    public ShippingNotFoundException(Long shippingId) {
        super("Shipping with id = " + shippingId + " not found!");
    }
}
