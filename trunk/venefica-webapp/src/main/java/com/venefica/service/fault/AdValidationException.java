package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@WebFault(name = "AdValidationError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class AdValidationException extends Exception {

	@SuppressWarnings("unused")
	private AdField invalidField;

	public AdValidationException(String message) {
		super(message);
	}

	public AdValidationException(AdField invalidField, String message) {
		super(message);
		this.invalidField = invalidField;
	}
}
