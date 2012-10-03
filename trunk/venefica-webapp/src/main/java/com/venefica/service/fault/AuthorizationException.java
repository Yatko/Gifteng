package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@WebFault(name = "AuthorizationError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class AuthorizationException extends Exception {
	public AuthorizationException(String message) {
		super(message);
	}
}
