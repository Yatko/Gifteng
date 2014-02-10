package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "CommentValidationError")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentValidationException extends Exception {

    @SuppressWarnings("unused")
    private CommentField invalidField;

    public CommentValidationException(CommentField invalidField, String message) {
        super(message);
        this.invalidField = invalidField;
    }

    public CommentValidationException(String message) {
        super(message);
    }
}
