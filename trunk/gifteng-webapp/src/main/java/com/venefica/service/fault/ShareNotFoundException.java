package com.venefica.service.fault;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "ShareNotFoundError")
public class ShareNotFoundException extends Exception {

    public ShareNotFoundException(String message) {
        super(message);
    }

    public ShareNotFoundException(Long shareId) {
        super("Share with id = " + shareId + " not found!");
    }
}
