package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@WebFault(name = "ImageValidationError")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class ImageValidationException extends Exception {

    @SuppressWarnings("unused")
    private ImageField invalidField;

    public ImageValidationException(String message) {
        super(message);
    }

    public ImageValidationException(ImageField invalidField, String message) {
        super(message);
        this.invalidField = invalidField;
    }
}
