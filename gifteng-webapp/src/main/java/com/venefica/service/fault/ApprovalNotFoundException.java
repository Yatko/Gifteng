package com.venefica.service.fault;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(name = "ApprovalNotFoundError")
public class ApprovalNotFoundException extends Exception {

    public ApprovalNotFoundException(String message) {
        super(message);
    }

    public ApprovalNotFoundException(Long adId) {
        super("Approval for adId = " + adId + " not found!");
    }
}
