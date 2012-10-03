package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "MessageValidationError")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageValidationException extends Exception {
	@SuppressWarnings("unused")
	private MessageField invalidField;

	public MessageValidationException(MessageField invalidField, String message) {
		super(message);
		this.invalidField = invalidField;
	}

	public MessageValidationException(String message) {
		super(message);
	}
}
