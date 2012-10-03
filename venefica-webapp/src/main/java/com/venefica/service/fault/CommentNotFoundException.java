package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "CommentNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentNotFoundException extends Exception {

	public CommentNotFoundException(String message) {
		super(message);
	}
	
	public CommentNotFoundException(Long commentId) {
		this("Comment with id = " + commentId + " not found!");
	}
}
