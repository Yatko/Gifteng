package com.venefica.service.fault;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "ImageNotFoundError")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageNotFoundException extends Exception {

    public ImageNotFoundException(String message) {
        super(message);
    }

    public ImageNotFoundException(Long imageId) {
        this("Image with id = " + imageId + " not found!");
    }
}
