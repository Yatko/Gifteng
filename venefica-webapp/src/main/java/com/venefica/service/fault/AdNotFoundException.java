package com.venefica.service.fault;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "AdNotFoundError")
public class AdNotFoundException extends Exception {

    public AdNotFoundException(String message) {
        super(message);
    }

    public AdNotFoundException(Long adId) {
        super("Ad with id = " + adId + " not found!");
    }
}
