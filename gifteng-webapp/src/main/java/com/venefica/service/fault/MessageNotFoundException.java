package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "MessageNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageNotFoundException extends Exception {

    public MessageNotFoundException(String message) {
        super(message);
    }

    public MessageNotFoundException(Long messageId) {
        this("Message with id = " + messageId + " not found!");
    }
}
