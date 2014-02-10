package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@WebFault(name = "InvalidAdStateError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class InvalidAdStateException extends Exception {

    public InvalidAdStateException(String message) {
        super(message);
    }
}
