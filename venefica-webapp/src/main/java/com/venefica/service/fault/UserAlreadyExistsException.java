package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;


@SuppressWarnings("serial")
@WebFault(name = "UserAlreadyExists")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAlreadyExistsException extends Exception {

	@SuppressWarnings("unused")
	private UserField duplicatedField;
	
	public UserAlreadyExistsException(String message) {
		super(message);
	}

	public UserAlreadyExistsException(UserField duplicatedField, String message) {
		super(message);
		this.duplicatedField = duplicatedField;
	}
}
