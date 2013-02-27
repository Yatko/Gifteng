package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.ws.WebFault;

@WebFault(name = "AuthenticationError")
//@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }
}
